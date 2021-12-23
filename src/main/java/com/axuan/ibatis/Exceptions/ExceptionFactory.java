package com.axuan.ibatis.Exceptions;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/10 14:51
 */
public class ExceptionFactory {

    public ExceptionFactory() {

    }

    public static RuntimeException wrapException(String message, Exception e) {
        return new PersistenceException(message, e);
    }
}
