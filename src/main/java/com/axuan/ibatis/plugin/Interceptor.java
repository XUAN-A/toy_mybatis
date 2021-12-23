package com.axuan.ibatis.plugin;

import java.util.Properties;

/**
 * 拦截器
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/12 16:03
 */
public interface Interceptor {

    // 拦截
    Object intercept(Invocation invocation) throws Throwable;

    // 插入
    Object plugin(Object target);

    // 设置属性
    void setProperties(Properties properties);
}
