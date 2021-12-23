package com.axuan.ibatis.builder;

import com.axuan.ibatis.Exceptions.BuilderException;
import com.axuan.ibatis.cache.Cache;
import com.axuan.ibatis.mapping.MappedStatement;
import com.axuan.ibatis.mapping.SqlCommandType;
import com.axuan.ibatis.mapping.SqlSource;
import com.axuan.ibatis.scripting.LanguageDriver;
import com.axuan.ibatis.session.Configuration;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 11:01
 */
public class MapperBuilderAssistant extends BaseBuilder{

    // 每个助手都有1个namespace,resource,cache
    private String currentNamespace;
    private String resource;
    private Cache currentCache;
    private boolean unresolvedCacheRef;

    public MapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }

    public void setCurrentNamespace(String currentNamespace) {
        if (currentNamespace == null) {
            throw new BuilderException("The mapper element requires a namespace attribute to be specified");
        }

        if (this.currentNamespace != null && !this.currentNamespace.equals(currentNamespace)) {
            throw new BuilderException("Wrong namespace. Expected '"
                + this.currentNamespace + "' but found '" + currentNamespace + "'.");
        }

        this.currentNamespace = currentNamespace;
    }

    // 为id加上namespace前缀，如selectPerson --> org.a.b.selectPerson
    public String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        }
        if (isReference) {
            if (base.contains(".")) {
                return base;
            }
        } else {
            if (base.startsWith(currentNamespace + ".")) {
                return base;
            }
            if (base.contains(".")) {
                throw new BuilderException("Dots are not allowed in element names, please remove it from " + base);
            }
        }
        return currentNamespace + "." + base;
    }

    public MappedStatement addMappedStatement(String id,
                                              String sql,
                                              SqlCommandType sqlCommandType,
                                              Class<?> parameterTypeClass,
                                              Class<?> resultTypeClass,
                                              boolean flushCache,
                                              boolean useCache) {

        // 为id加上namespace前缀
        id = applyCurrentNamespace(id, false);
        // 是否是select语句
        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;

        // 建造者模式
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sql, sqlCommandType)
                .parameterType(parameterTypeClass)
                .resultType(resultTypeClass);

        // 1.参数映射
        // 2.结果映射
        setStatementCache(isSelect, flushCache, useCache, currentCache,statementBuilder);

        MappedStatement statement = statementBuilder.build();

        // 建造好调用configuration.addMappedStatement
        configuration.addMappedStatement(statement);
        return statement;
    }


    private void setStatementCache(
            boolean isSelect,
            boolean flushCache,
            boolean useCache,
            Cache cache,
            MappedStatement.Builder statementBuilder) {
        statementBuilder.flushCacheRequired(flushCache);
        statementBuilder.useCache(useCache);
        statementBuilder.cache(cache);
        statementBuilder.flushCacheRequired(flushCache);
        statementBuilder.useCache(useCache);
        statementBuilder.cache(cache);
    }


    public String getCurrentNamespace() {
        return currentNamespace;
    }
}
