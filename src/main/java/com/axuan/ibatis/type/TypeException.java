package com.axuan.ibatis.type;

import com.axuan.ibatis.Exceptions.PersistenceException;

/**
 * 类型异常
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 16:39
 */
public class TypeException extends PersistenceException {

    private static final long serialVersionUID = 8614420898975117130L;

    public TypeException() {
        super();
    }

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeException(Throwable cause) {
        super(cause);
    }
}
