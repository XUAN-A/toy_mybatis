package com.axuan.ibatis.Exceptions;

/**
 * 构建异常，继承PersistenceException,语义分类
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/1 19:09
 */
public class BuilderException extends PersistenceException{

    private static final long serialVersionUID = -3885164021020443281L;

    public BuilderException() {
        super();
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderException(Throwable cause) {
        super(cause);
    }

}
