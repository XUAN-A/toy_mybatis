package com.axuan.ibatis.mapping;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 16:15
 */
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);
}
