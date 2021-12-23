package com.axuan.ibatis.builder;

import com.axuan.ibatis.Exceptions.BuilderException;

/**
 * 元素不全异常，比如XMLIncludeTransformer里使用
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/3 10:40
 */
public class IncompleteElementException extends BuilderException {

    private static final long serialVersionUID = -3697292286890900315L;

    public IncompleteElementException() {
        super();
    }

    public IncompleteElementException(String message) {
        super(message);
    }

    public IncompleteElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteElementException(Throwable cause) {
        super(cause);
    }
}
