package com.axuan.ibatis.cache.impl;

import com.axuan.ibatis.cache.Cache;
import com.axuan.ibatis.cache.CacheException;

import java.util.HashMap;
import java.util.Map;

/**
 * 永久缓存
 * 一旦存入就一直保持
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/15 23:36
 */
public class PerpetualCache implements Cache {

    // 每个永久缓存有一个ID来识别
    private String id;

    // 内部就是HashMap，所有方法基本就是直接调用HashMap的方法，不支持多线程？
    private Map<Object, Object> cache = new HashMap<>();

    public PerpetualCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return cache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int getSize() {
        return cache.size();
    }


    @Override
    public int hashCode() {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // 只要id相等就认为两个cache相同
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cache)) {
            return false;
        }

        Cache otherCache = (Cache)obj;
        return getId().equals(otherCache.getId());
    }
}
