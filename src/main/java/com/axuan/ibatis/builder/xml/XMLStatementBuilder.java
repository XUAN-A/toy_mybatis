package com.axuan.ibatis.builder.xml;

import com.axuan.ibatis.builder.BaseBuilder;
import com.axuan.ibatis.builder.MapperBuilderAssistant;
import com.axuan.ibatis.mapping.MappedStatement;
import com.axuan.ibatis.mapping.SqlCommandType;
import com.axuan.ibatis.mapping.SqlSource;
import com.axuan.ibatis.parsing.XNode;
import com.axuan.ibatis.scripting.LanguageDriver;
import com.axuan.ibatis.session.Configuration;

import java.util.Locale;

/**
 * XML语句构建器，建造者模式，继承BaseBuilder
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 15:56
 */
public class XMLStatementBuilder extends BaseBuilder {

    private MapperBuilderAssistant builderAssistant;
    private XNode context;
    private String requiredDatabaseId;

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.context = context;
    }

    public XMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, XNode context, String requiredDatabaseId) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.context = context;
        this.requiredDatabaseId = requiredDatabaseId;
    }

    //解析语句(select|insert|update|delete)
    //<select
    //  id="selectPerson"
    //  parameterType="int"
    //  parameterMap="deprecated"
    //  resultType="hashmap"
    //  resultMap="personResultMap"
    //  flushCache="false"
    //  useCache="true"
    //  timeout="10000"
    //  fetchSize="256"
    //  statementType="PREPARED"
    //  resultSetType="FORWARD_ONLY">
    //  SELECT * FROM PERSON WHERE ID = #{id}
    //</select>

    public void parseStatementNode() {
        String id = context.getStringAttribute("id");


        // 参数类型
        String parameterType = context.getStringAttribute("parameterType");
        Class<?> parameterTypeClass = resolveClass(parameterType);


        //结果类型
        String resultType = context.getStringAttribute("resultType");
        //// 脚本语言，mybatis3.2的新功能
        //String lang = context.getStringAttribute("lang");
        //// 得到语言驱动
        //LanguageDriver langDriver = getLanguageDriver(lang);


        Class<?> resultTypeClass = resolveClass(resultType);

        // 获取命令类型(result|insert|update|delete)
        String nodeName = context.getNode().getNodeName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
        boolean flushCache = context.getBooleanAttribute("flushCache", !isSelect);
        // 是否要缓存结果
        boolean useCache = context.getBooleanAttribute("useCache", isSelect);

        String sql = context.getStringBody();

        // 解析成SqlSource,一般是DynamicSqlSource
        //SqlSource sqlSource = langDriver.createSqlSource(configuration, context, parameterTypeClass);

        // 又去调助手类
        builderAssistant.addMappedStatement(id, sql, sqlCommandType, parameterTypeClass, resultTypeClass, flushCache, useCache);
    }


}
