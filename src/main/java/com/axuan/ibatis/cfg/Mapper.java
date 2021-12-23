package com.axuan.ibatis.cfg;

/**
 * 用于封装执行SQL语句和结果类型的全限定类名
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 10:33
 */
public class Mapper {
    private String queryString; // SQL
    private String resultType; // 实体类的全限定类名

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
