package com.axuan.ibatis.logging;

import com.axuan.ibatis.Exceptions.PersistenceException;

/**
 * 日志异常，继承PersistenceException，语义分类
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 10:10
 */
public class LogException extends PersistenceException {

    private static final long serialVersionUID = 1022924004852350942L;

    public LogException() {
        super();
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogException(Throwable cause) {
        super(cause);
    }
}
