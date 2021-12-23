package com.axuan.ibatis.session;

import com.axuan.ibatis.binding.MapperRegistry;
import com.axuan.ibatis.datasource.pooled.PooledDataSourceFactory;
import com.axuan.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.axuan.ibatis.executor.CachingExecutor;
import com.axuan.ibatis.executor.Executor;
import com.axuan.ibatis.executor.SimpleExecutor;
import com.axuan.ibatis.mapping.Environment;
import com.axuan.ibatis.mapping.MappedStatement;
import com.axuan.ibatis.parsing.XNode;
import com.axuan.ibatis.plugin.InterceptorChain;
import com.axuan.ibatis.transaction.Transaction;
import com.axuan.ibatis.transaction.jdbc.JdbcTransactionFactory;
import com.axuan.ibatis.type.TypeAliasRegistry;
import com.sun.beans.WeakCache;
import sun.misc.SoftCache;

import java.sql.SQLException;
import java.util.*;

/**
 * 自定义mybatis的配置类
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 10:32
 */
public class Configuration {

    protected Environment environment;


    protected ExecutorType defaultExecutorType = ExecutorType.SIMPLE;
    // ----------以上都是<settings>节点
    // 默认启用缓存
    protected boolean cacheEnabled = true;

    protected LocalCacheScope localCacheScope = LocalCacheScope.SESSION;

    protected final InterceptorChain interceptorChain = new InterceptorChain();

    // 映射注册机
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    // 类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    // 映射的语句，存在Map里
    protected final Map<String, MappedStatement> mappedStatements = new StrictMap<>("Mapped Statements collection");

    // 结果映射，存在Map里
    protected final Set<String> loadedResources = new HashSet<>();
    protected final Map<String, XNode> sqlFragments = new StrictMap<>("XML fragments parsed from previous mappers");


    public Configuration(Environment environment) {
        this.environment = environment;
    }

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        //typeAliasRegistry.registerAlias("MANAGED", ManagedTransactionFactory.class);

        //typeAliasRegistry.registerAlias("JNDI", JndiDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);

    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }


    public boolean hasStatement(String statement) {
        return hasStatement(statement,true);
    }

    public boolean hasStatement(String statementName, boolean validateIncompleteStatement) {
        if (validateIncompleteStatement) {

        }
        return mappedStatements.containsKey(statementName);
    }

    public Map<String, XNode> getSqlFragments() {
        return sqlFragments;
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public ExecutorType getDefaultExecutorType() {
        return defaultExecutorType;
    }

    public void setDefaultExecutorType(ExecutorType defaultExecutorType) {
        this.defaultExecutorType = defaultExecutorType;
    }

    public LocalCacheScope getLocalCacheScope() {
        return localCacheScope;
    }

    public void setLocalCacheScope(LocalCacheScope localCacheScope) {
        this.localCacheScope = localCacheScope;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }
    // 产生执行器
    public Executor newExecutor(Transaction transaction, ExecutorType executorType) throws SQLException {
        executorType = executorType == null ? defaultExecutorType : executorType;
        // 这句再做一下保护,避免有人将defaultExecutorType 设为null
        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
        Executor executor = null;
        // 然后就是简单的三个分支，产生3中执行器BatExecutor/ReuseExecutor/SimpleExecutor
        if (ExecutorType.BATCH == executorType) {

        } else if (ExecutorType.REUSE == executorType) {

        } else {
            executor = new SimpleExecutor(this, transaction);
        }
        // 如果要求缓存，生成另一种CachingExecutor(默认就是有缓存),装饰器模式，所以默认都是返回CachingExecutor
        if (cacheEnabled) {
            executor = new CachingExecutor(executor);
        }
        // 此处调用插件，通过插件可以改变Executor行为
        executor = (Executor) interceptorChain.pluginAll(executor);
        return executor;

    }

    // 由DefaultSession.selectList调用过来
    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    // 静态内部类，严格的Map，不允许多次覆盖key所对应的value
    protected static class StrictMap<V> extends HashMap<String,V> {
        private static final long serialVersionUID = -4950446264854982944L;
        private String name;

        public StrictMap(String name, int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
            this.name = name;
        }

        public StrictMap(String name, int initialCapacity) {
            super(initialCapacity);
            this.name = name;
        }

        public StrictMap(String name) {
            this.name = name;
        }

        public V put(String key, V value) {
            if (containsKey(key)) {
                // 如果已经存在此key了， 直接报错
                throw new IllegalArgumentException(name + " already contains value for " + key);
            }
            if (key.contains(".")) {
                // 如果有. 符号，取得短名称，大致用意就是包名不同，类名相同，提供模糊查询的功能
                final String shortKey = getShortName(key);
                if (super.get(shortKey) == null) {
                    // 如果没有这个缩略，则放一个缩略
                    super.put(shortKey, value);
                } else {
                    // 如果已经有此缩略，表示模糊，放一个Ambiguity型的
                    super.put(shortKey, (V) new Ambiguity(shortKey));
                }
            }

            // 再放一个全名
            return super.put(key, value);
            // 可以看到，如果有包名，会放2个key到这个map，一个缩略，一个全名
        }


        // 取得短名称,也就是取得最后那个句号的后面的那部分
        private String getShortName(String key) {
            final String[] keyparts = key.split("\\.");
            return keyparts[keyparts.length - 1];
        }

        // 模糊，居然放在Map里面的一个静态内部类
        protected static class Ambiguity {
            // 提供一个主题
            private String subject;

            public Ambiguity(String subject) {
                this.subject = subject;
            }

            public String getSubject() {
                return subject;
            }
        }
    }


}
