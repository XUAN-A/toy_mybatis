package com.axuan.ibatis.Exceptions;

/**
 * Ibatis异常，所有异常的父类，但被废弃了
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/1 19:12
 */
@Deprecated
public class IbatisException extends RuntimeException{

    private static final long serialVersionUID = 3880206998166270511L;

    public IbatisException() { super();}

    public IbatisException(String message) { super(message);}

    public IbatisException(String message, Throwable cause) {
        super(message, cause);
    }

    public IbatisException(Throwable cause) {
        super(cause);
    }
}
