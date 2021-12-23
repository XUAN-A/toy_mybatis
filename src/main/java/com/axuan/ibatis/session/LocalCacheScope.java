package com.axuan.ibatis.session;

/**
 * 本地缓存机制（Local Cache）防止循环引用（circular reference）和加速重复嵌套查询
 * 默认值为 SESSION，这种情况不会缓存一个会话中执行的所有查询
 * 若设置值为 STATEMENT，本地会话仅用在语句执行上，对相同 SqlSession的不同调用将不会共享数据。
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/16 10:23
 */
public enum LocalCacheScope {
    SESSION, STATEMENT
}
