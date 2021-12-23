package com.axuan.ibatis.io;

import com.axuan.ibatis.io.ClassLoaderWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * 通过类加载器获得resource的辅助类
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 10:22
 */
public class Resources {


    // 大多数方法都是委托给ClassLoaderWrapper,再去做真正的事
    private static ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();

    public Resources() {
    }


    public static InputStream getResourceAsStream(String resource) throws IOException {
        return getResourceAsStream(null, resource);
    }

    public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
        InputStream in = classLoaderWrapper.getResourceAsStream(resource, loader);
        if (in == null) {
            throw new IOException("Could not find resource" + resource);
        }
        return in;
    }
    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return classLoaderWrapper.classForName(className);
    }
}
