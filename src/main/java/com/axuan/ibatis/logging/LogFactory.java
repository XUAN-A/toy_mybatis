package com.axuan.ibatis.logging;

import com.axuan.ibatis.logging.jdk14.Jdk14LoggingImpl;

import java.lang.reflect.Constructor;

/**
 * 日志工厂
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 10:04
 */
public final class LogFactory {

    // 给支持marker功能的logger使用（目前有slf4j,log4j2)
    public static final String MARKER = "MYBATIS";

    // 具体究竟用哪个日志框架，那个框架所对应logger的构造函数
    private static Constructor<? extends Log> logConstructor;

    static {
        tryImplementation(new Runnable() {
            @Override
            public void run() {
                useJdkLogging();
            }
        });
    }

    // 根据传入的类来构建Log
    public static Log getLog(Class<?> aClass) {
        return getLog(aClass.getName());
    }

    // 根据传入的类名来构建Log
    public static Log getLog(String logger) {
        try {
            // 构造函数，参数必须是一个，为String型，指明logger的名称
            return logConstructor.newInstance(new Object[]{logger});
        } catch (Throwable t) {
            throw new LogException("Error creating logger for logger " + logger + ". Cause" + t, t);
        }
    }

    public static synchronized void useJdkLogging() {
       setImplementation(com.axuan.ibatis.logging.jdk14.Jdk14LoggingImpl.class);
    }


    private static void setImplementation(Class<? extends Log> implClass) {
        try {
            Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
            Log log = candidate.newInstance(LogFactory.class.getName());
            if (log.isDebugEnabled()) {
                log.debug("Logging initialized using '" + implClass + "' adapter.");
            }
            logConstructor = candidate;
        } catch (Throwable t) {
            throw  new LogException("Error setting Log implementation. Cause: " + t, t);
        }
    }


    private static void tryImplementation(Runnable runnable) {
        if (logConstructor == null) {
            try {
                runnable.run();
            } catch (Throwable t) {

            }
        }
    }
}
