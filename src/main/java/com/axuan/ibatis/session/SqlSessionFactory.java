package com.axuan.ibatis.session;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 10:27
 */
public interface SqlSessionFactory {

    /**
     * 用于打开一个新的SqlSession对象
     * @return com.axuan.ibatis.session.SqlSession
     * @author ZhouXuan
     * @date 2021/11/30 10:30
     */
    SqlSession openSession();

    // 自动提交
    SqlSession openSession(boolean autoCommit);


}
