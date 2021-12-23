package com.axuan.ibatis.builder.xml;

import com.axuan.ibatis.Exceptions.BuilderException;
import com.axuan.ibatis.annotations.Select;
import com.axuan.ibatis.builder.BaseBuilder;
import com.axuan.ibatis.cfg.Mapper;
import com.axuan.ibatis.datasource.DataSourceFactory;
import com.axuan.ibatis.io.Resources;
import com.axuan.ibatis.mapping.Environment;
import com.axuan.ibatis.parsing.XNode;
import com.axuan.ibatis.parsing.XPathParser;
import com.axuan.ibatis.session.Configuration;
import com.axuan.ibatis.transaction.TransactionFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/** 
 * XML配置构建器，建造者模式，继承BaseBuilder
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 10:35
 */
public class XMLConfigBuilder extends BaseBuilder {


    // 是否已解析，XPath解析器
    private boolean parsed;
    private final XPathParser parser;
    private String environment;

    public XMLConfigBuilder(InputStream inputStream, String environment) {
        this(new XPathParser(inputStream), environment);
    }

    private XMLConfigBuilder(XPathParser parser, String environment) {
        super(new Configuration());
        this.parsed = false;
        this.environment = environment;
        this.parser = parser;
    }

    // 上面的构建函数最后到合流到这个函数，传入XPathParser
    private XMLConfigBuilder(XPathParser parser) {
        // 首先调用父类初始化Configuration
        super(new Configuration());

        this.parsed = false;
        this.parser = parser;
    }

    public Configuration parse() {
        // 如果已经解析过了，报错
        if(parsed) {
            throw new BuilderException("Each XMLConfigBuilder can only be used once");
        }
        parsed = true;
        // 根节点是Configuration
        parseConfiguration(parser.evalNode("/configuration"));
        return configuration;
    }

    // 解析配置
    private void parseConfiguration(XNode root) {
        try {
            // 分步骤解析
            // 环境
            environmentsElement(root.evalNode("environments"));

            // 映射器
            mapperElement(root.evalNode("mappers"));
        } catch (Exception e) {
            throw new BuilderException("Error parsing SQL Mapper Configuration. Cause:" + e, e);
        }
    }

    private void environmentsElement(XNode context) throws InstantiationException, IllegalAccessException {
        if (context != null) {
            if (environment == null) {
                environment = context.getStringAttribute("default");
            }
            for (XNode child : context.getChildren()) {
                String id = child.getStringAttribute("id");
                if (isSpecifiedEnvironment(id)) {
                    TransactionFactory txFactory = transactionManagerElement(child.evalNode("transactionManager"));
                    DataSourceFactory dsFactory = dataSourceElement(child.evalNode("dataSource"));
                    DataSource dataSource = dsFactory.getDataSource();
                    Environment.Builder environmentBuilder = new Environment.Builder(id).transactionFactory(txFactory).dataSource(dataSource);
                    configuration.setEnvironment(environmentBuilder.build());
                }
            }
        }
    }

    private DataSourceFactory dataSourceElement(XNode context) throws IllegalAccessException, InstantiationException {
        if (context != null) {
            String type = context.getStringAttribute("type");
            Properties props = context.getChildrenAsProperties();
            DataSourceFactory factory = (DataSourceFactory) resolveAlias(type).newInstance();
            factory.setProperties(props);
            return factory;
        }
        throw new BuilderException("Environment declaration requires a DataSourceFactory.");
    }

    private TransactionFactory transactionManagerElement(XNode context) throws IllegalAccessException, InstantiationException {
        if (context != null) {
            String type = context.getStringAttribute("type");
            Properties props = context.getChildrenAsProperties();
            TransactionFactory factory = (TransactionFactory) resolveAlias(type).newInstance();
            factory.setProperties(props);
            return factory;
        }
        throw new BuilderException("Environment declaration requires a TransactionFactory.");
    }

    private boolean isSpecifiedEnvironment(String id) {
        if (environment == null) {
            throw new BuilderException("No environment specified");
        } else if (id == null) {
            throw new BuilderException("Environment requires an id attribute");
        } else if (environment.equals(id)) {
            return true;
        }
        return false;
    }

    // 1.properties
    //<properties resource="org/mybatis/example/config.properties">
    //    <property name="username" value="dev_user"/>
    //    <property name="password" value="F2Fa3!33TYyg"/>
    //</properties>
    private void propertiesElement(XNode context) throws Exception {
        if (context != null) {
            // 如果在这里地方，属性多于一个的话，Mybatis 按照如下的顺序加载它们:

            // 1.在properties元素体内指定的属性首先被读取。
            // 2.在类路径下资源或 properties 元素的url属性中加载的属性第二被读取，它会覆盖已经存在的完全一样的属性
            // 3.作为方法参数传递的属性最后被读取，它也会覆盖任一已经存在的完全一样的属性，这些很属性可能是从 properties 元素体内 和 资源 /url 属性中加载的。
            // 传入方式是调用构造函数时传入，public XMlConfigBuilder(Reader reader, String environment, Properties props)

            // 1.XNode.getChildrenAsProperties函数方便得到孩子所有Properties
            Properties defaults = context.getChildrenAsProperties();
            // 2.然后查找resource或者url，加入前面的Properties
            String resource = context.getStringAttribute("resource");
            String url = context.getStringAttribute("url");
            if (resource != null && url != null) {
                throw new BuilderException("The properties element cannot specify both a URL and a resource based property file reference. Please specify one or the other");
            }
            //if (resource != null) {
            //    defaults.putAll(Resources.getResourceAsProperties(resource));
            //} else if (url != null) {
            //    defaults.putAll(Resources.getUrlAsProperties(url));
            //}

            //// 3.Variables也全部加入Properties
            //Properties vars = configuration.getVariables();
            //if (vars != null) {
            //    defaults.putAll(vars);
            //}
            //
            //parser.setVariables(defaults);
            //configuration.setVariables(defaults);
        }
    }

    // 10.自动扫描包下所有映射器
    //	<mappers>
    //	  <package name="org.mybatis.builder"/>
    //	</mappers>
    private void mapperElement(XNode parent) throws Exception {
        if (parent != null) {
            for (XNode child : parent.getChildren()) {
                if ("package".equals(child.getName())) {
                    String mapperPackage = child.getStringAttribute("name");
                    // 自动扫描包下所有映射器
                } else {
                    String resource = child.getStringAttribute("resource");
                    String mapperClass = child.getStringAttribute("class");
                    if (resource != null && mapperClass == null) {
                        // xml
                        InputStream inputStream = Resources.getResourceAsStream(resource);
                        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
                        mapperParser.parse();
                    } else if (resource == null && mapperClass != null) {
                        // 注解
                        Class<?> mapperInterface = Resources.classForName(mapperClass);
                        // 直接把这个映射加入配置
                        configuration.addMapper(mapperInterface);
                    }
                }
            }
        }
    }


}
