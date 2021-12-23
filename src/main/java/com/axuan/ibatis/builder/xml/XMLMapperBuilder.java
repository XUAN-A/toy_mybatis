package com.axuan.ibatis.builder.xml;

import com.axuan.ibatis.Exceptions.BuilderException;
import com.axuan.ibatis.builder.BaseBuilder;
import com.axuan.ibatis.builder.IncompleteElementException;
import com.axuan.ibatis.builder.MapperBuilderAssistant;
import com.axuan.ibatis.io.Resources;
import com.axuan.ibatis.parsing.XNode;
import com.axuan.ibatis.parsing.XPathParser;
import com.axuan.ibatis.session.Configuration;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/11 15:51
 */
public class XMLMapperBuilder extends BaseBuilder {


    private XPathParser parser;
    // 映射器构建助手
    private MapperBuilderAssistant builderAssistant;
    // 用来存放sql片段的哈希表
    private Map<String, XNode> sqlFragments;
    private String resource;


    public XMLMapperBuilder(Configuration configuration) {
        super(configuration);
    }

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource, Map<String, XNode> sqlFragments, String namespace) {
        this(inputStream, configuration, resource, sqlFragments);
        this.builderAssistant.setCurrentNamespace(namespace);
    }

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource, Map<String, XNode> sqlFragments) {
        this(new XPathParser(inputStream, true),
                configuration, resource, sqlFragments);
    }

    public XMLMapperBuilder(XPathParser parser, Configuration configuration, String resource, Map<String, XNode> sqlFragments) {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration, resource);
        this.parser = parser;
        this.sqlFragments = sqlFragments;
        this.resource = resource;
    }


    // 解析
    public void parse() {
        // 如果没有加载过再加载，防止重复加载
        if (!configuration.isResourceLoaded(resource)) {
            // 配置mapper
            configurationElement(parser.evalNode("/mapper"));
            // 标记一下，已经加载过了
            configuration.addLoadedResource(resource);
            // 绑定映射器到namespace
            bindMapperForNamespace();
        }
    }

    private void bindMapperForNamespace() {
        String namespace = builderAssistant.getCurrentNamespace();
        if (namespace != null) {
            Class<?> boundType = null;
            try {
                boundType = Resources.classForName(namespace);
            } catch (ClassNotFoundException e) {

            }
            if (boundType != null) {
                if (!configuration.hasMapper(boundType)) {
                    configuration.addLoadedResource("namespace:" + namespace);
                    configuration.addMapper(boundType);
                }
            }
        }

    }


    // 配置mapper元素
    //	<mapper namespace="org.mybatis.example.BlogMapper">
    //	  <select id="selectBlog" parameterType="int" resultType="Blog">
    //	    select * from Blog where id = #{id}
    //	  </select>
    //	</mapper>
    private void configurationElement(XNode context) {
        try {
            // 1.配置namespace
            String namespace = context.getStringAttribute("namespace");
            if (namespace.equals("")) {
                throw new BuilderException("Mapper's namespace cannot be empty");
            }
            builderAssistant.setCurrentNamespace(namespace);

            // 配置select|insert|update|delete
            buildStatementFromContext(context.evalNodes("select|insert|update|delete"));

        } catch(Exception e) {
            throw new BuilderException("Error parsing Mapper XML. Cause: " + e, e);
        }
    }

    // 配置select|insert|update|delete
    private void buildStatementFromContext(List<XNode> list) {
        buildStatementFromContext(list, null);
    }

    // 构建语句
    private void buildStatementFromContext(List<XNode> list, String requiredDatabaseId) {
        for (XNode context : list) {
            // 构建所有语句，一个mapper下可以有很多select
            // 语句比较复杂，核心都在这里面，所以调用XMLStatementBuilder
            final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration, builderAssistant, context, requiredDatabaseId);
            try {
                // 核心XMLStatementBuilder.parseStatementNode
                statementParser.parseStatementNode();
            } catch (IncompleteElementException e) {
                //// 如果出现SQL语句不完整，把它记下来，塞到configuration去
                //configuration.addIncompleteStatement(statementParser);
            }
        }
    }

}
