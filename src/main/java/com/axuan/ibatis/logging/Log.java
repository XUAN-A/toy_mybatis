package com.axuan.ibatis.logging;

/**
 * 日志接口
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 10:00
 */
public interface Log {

    // 和一般的log4j很像，提供日志接口的一些方法，error,debug，warn
    // 用自己的日志是为了通用，不绑死在某个特定的日志框架中
    // 有类似的slf4j，为什么还要自己写？可能是不想引入额外的jar包

    boolean isDebugEnabled();

    boolean isTraceEnable();

    void error(String s, Throwable e);

    void error(String s);

    void debug(String s);

    void trace(String s);

    void warn(String s);
}
