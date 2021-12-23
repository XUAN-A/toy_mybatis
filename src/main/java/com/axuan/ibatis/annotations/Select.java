package com.axuan.ibatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询的注解
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 16:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {

    /**
     * 配置SQL语句的
     * @return java.lang.String
     * @author ZhouXuan
     * @date 2021/11/30 16:35
     */
    String[] value();
}
