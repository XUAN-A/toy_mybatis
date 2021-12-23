package com.axuan.ibatis.mapping;

/**
 * 绑定的SQL，是从SqlSource而来，将动态内容都处理完成得到的SQL语句字符串，其中包括?，还有绑定的参数
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 16:15
 */
public class BoundSql {

    private String sql;

    public BoundSql(String sql) {
        this.sql = sql;
    }
}
