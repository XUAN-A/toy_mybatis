package com.axuan.ibatis.transaction;

import com.axuan.ibatis.Exceptions.PersistenceException;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/10 17:14
 */
public class TransactionException extends PersistenceException {

    private static final long serialVersionUID = -433589569461084605L;

    public TransactionException() {
        super();
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
