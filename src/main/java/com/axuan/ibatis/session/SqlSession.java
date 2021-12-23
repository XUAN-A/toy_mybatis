package com.axuan.ibatis.session;

import java.util.List;

/**
 * 这是Mybatis主要的一个类，用来执行SQL，获取映射器，管理事务
 *
 * 通常情况下，我们在应用程序中使用的mybatis的API就是这个接口定义的方法
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 10:28
 */
public interface SqlSession {

    /**
     * 根据指定的SqlID获取一条记录的封装对象
     * @param statement:
     * @return T
     * @author ZhouXuan
     * @date 2021/12/12 16:13
     */
    <T> T selectOne(String statement);


    <T> T selectOne(String statement, Object parameter);


    /**
     * 根据指定的sqlId获取多条记录
     * @param statement:
     * @return java.util.List<T>
     * @author ZhouXuan
     * @date 2021/12/12 16:14
     */
    <E> List<E> selectList(String statement);

    /**
     * 获取多条记录，这个方法允许我们可以传递一些参数
     * @param statement:
     * @param parameter:
     * @return java.util.List<E>
     * @author ZhouXuan
     * @date 2021/12/13 9:12
     */
    <E> List<E> selectList(String statement, Object parameter);


    /**
     * 插入记录，容许传入参数
     * @param statement:
     * @param parameter:
     * @return int
     * @author ZhouXuan
     * @date 2021/12/14 8:18
     */
    int insert(String statement, Object parameter);

    /**
     * 更新记录
     * @param statement:
     * @param parameter:
     * @return int
     * @author ZhouXuan
     * @date 2021/12/14 8:20
     */
    int update(String statement, Object parameter);


    /**
     * 删除记录
     * @param statement:
     * @param parameter:
     * @return int
     * @author ZhouXuan
     * @date 2021/12/14 16:34
     */
    int delete(String statement, Object parameter);

    /**
     * 得到配置
     * @return com.axuan.ibatis.session.Configuration
     * @author ZhouXuan
     * @date 2021/12/13 8:43
     */
    Configuration getConfiguration();

    /**
     * 得到映射器
     * 这个巧妙的使用了泛型，使得类型安全
     * @return T
     * @author ZhouXuan
     * @date 2021/11/30 10:29
     */
    <T> T getMapper(Class<T> type);


    // 以下是事务控制方法，commit，rollback
    void commit();

    void commit(boolean force);

    void rollback();

    void rollback(boolean force);


}
