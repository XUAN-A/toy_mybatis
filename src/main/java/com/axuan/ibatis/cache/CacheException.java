package com.axuan.ibatis.cache;

import com.axuan.ibatis.Exceptions.PersistenceException;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/15 23:43
 */
public class CacheException extends PersistenceException {

    private static final long serialVersionUID = -193202262468464650L;

    public CacheException() {
        super();
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}
