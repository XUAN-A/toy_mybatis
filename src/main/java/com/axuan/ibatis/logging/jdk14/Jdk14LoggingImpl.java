package com.axuan.ibatis.logging.jdk14;

import com.axuan.ibatis.logging.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用的jdk1.4 logger里的Logger
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/12 16:35
 */
public class Jdk14LoggingImpl implements Log {
    private Logger log;

    public Jdk14LoggingImpl(String clazz) {
        this.log = Logger.getLogger(clazz);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isLoggable(Level.FINE);
    }

    @Override
    public boolean isTraceEnable() {
        return log.isLoggable(Level.FINER);
    }

    @Override
    public void error(String s, Throwable e) {
        log.log(Level.SEVERE, s , e);
    }

    @Override
    public void error(String s) {
        log.log(Level.SEVERE, s);
    }

    @Override
    public void debug(String s) {
        log.log(Level.FINE, s);
    }

    @Override
    public void trace(String s) {
        log.log(Level.FINER, s);
    }

    @Override
    public void warn(String s) {
        log.log(Level.WARNING, s);
    }
}
