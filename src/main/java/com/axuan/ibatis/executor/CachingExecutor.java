package com.axuan.ibatis.executor;

import com.axuan.ibatis.cache.Cache;
import com.axuan.ibatis.cache.CacheKey;
import com.axuan.ibatis.cache.TransactionalCacheManager;
import com.axuan.ibatis.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * 二级缓存执行器
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/12 15:59
 */
public class CachingExecutor implements Executor{

    private Executor delegate;
    private TransactionalCacheManager tcm = new TransactionalCacheManager();

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
        delegate.setExecutorWrapper(this);
    }

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        // 刷新缓存完再update
        return delegate.update(ms, parameter);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter) throws Exception {
        String sql = ms.getSql();
        // query时传入一个cacheKey参数
        CacheKey key = createCacheKey(ms, parameter, sql);
        return query(ms, parameter, key);
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameter, String sql) {
        return delegate.createCacheKey(ms, parameter, sql);
    }


    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, CacheKey key) throws Exception {
        Cache cache = ms.getCache();
        // 默认情况下是没有开启缓存的（二级缓存），要开启二级缓存，你需要在你的SQL映射文件中添加一行:<cache/>
        // 简单的说，就是先查CacheKey查不到再委托给实际的执行器去查
        if (cache != null) {
            flushCacheIfRequired(ms);
            if (ms.isUseCache()) {
                List<E> list  = (List<E>) tcm.getObject(cache, key);
                if (list == null) {
                    list = delegate.<E> query(ms, parameter, key);
                    tcm.putObject(cache, key, list);
                }
                return list;
            }
        }

        return delegate.<E>query(ms, parameter, key);
    }

    @Override
    public void commit(boolean required) throws SQLException {
        delegate.commit(required);
        tcm.commit();
    }

    @Override
    public void rollback(boolean required) throws SQLException {

    }

    @Override
    public void setExecutorWrapper(Executor executor) {
        throw new UnsupportedOperationException("This method should not be called");
    }

    private void flushCacheIfRequired(MappedStatement ms) {
        Cache cache = ms.getCache();
        if (cache != null && ms.isFlushCacheRequired()) {
            tcm.clear(cache);
        }
    }

    @Override
    public void clearLocalCache() {
        delegate.clearLocalCache();
    }
}
