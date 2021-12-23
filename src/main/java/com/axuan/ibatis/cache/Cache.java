package com.axuan.ibatis.cache;

/**
 * 缓存
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 11:03
 */
public interface Cache {


    // 取值ID
    String getId();

    // 存入值
    void putObject(Object key, Object value);

    // 获取值
    Object getObject(Object key);

    // 删除值
    Object removeObject(Object key);

    // 清空
    void clear();

    // 取得大小
    int getSize();
}
