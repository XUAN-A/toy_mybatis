package com.axuan.ibatis.executor;

import com.axuan.ibatis.Exceptions.PersistenceException;

/**
 * 执行异常
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/15 23:52
 */
public class ExecutorException extends PersistenceException {

    private static final long serialVersionUID = 4060977051977364820L;

    public ExecutorException() {
        super();
    }

    public ExecutorException(String message) {
        super(message);
    }

    public ExecutorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutorException(Throwable cause) {
        super(cause);
    }
}
