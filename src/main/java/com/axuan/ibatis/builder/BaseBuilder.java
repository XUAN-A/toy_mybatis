package com.axuan.ibatis.builder;


import com.axuan.ibatis.Exceptions.BuilderException;
import com.axuan.ibatis.session.Configuration;
import com.axuan.ibatis.type.TypeAliasRegistry;

/**
 * 构建器的基类，建造者模式
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/1 16:46
 */
public abstract class BaseBuilder {

    // 需要配置,类型别名注册
    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }



    public Configuration getConfiguration() {
        return configuration;
    }


    // 根据别名解析Class，其实是去查看 类型别名注册/事务管理器别名
    protected Class<?> resolveClass(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return resolveAlias(alias);
        } catch (Exception e) {
            throw new BuilderException("Error resolving class. Cause: " + e, e);
        }
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }


}
