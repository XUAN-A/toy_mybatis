package com.axuan.ibatis.binding;

import com.axuan.ibatis.builder.annotation.MapperAnnotationBuilder;
import com.axuan.ibatis.session.Configuration;
import com.axuan.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 映射器注册机
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 9:41
 */
public class MapperRegistry {

    private Configuration config;
    // 将已经添加的映射都放入HashMap
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    // 返回代理类
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    // 添加一个映射
    public <T> void addMapper(Class<T> type) {
        // mapper必须是接口！才会添加
        if (type.isInterface()) {
            if (hasMapper(type)) {
                // 如果重复添加了，报错
                throw new BindingException("Type " + type +" is already known to the MapperRegistry");
            }
            boolean loadCompleted = false;
            try {
                knownMappers.put(type, new MapperProxyFactory<T>(type));

                MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
                parser.parse();
            } finally {

            }
        }
    }


    public void addMappers(String packageName, Class<?> superType) {
        // 查找包下所有是superType的类
        //ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        //resolverUtil.find(new ResolverUtil.IsA(superType),packageName);
        //Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        //for (Class<?> aClass : mapperSet) {
        //    addMapper(mapperClass);
        //}
    }

    // 查找包下所有类
    public void addMappers(String packageName) {
        addMappers(packageName, Object.class);
    }
}
