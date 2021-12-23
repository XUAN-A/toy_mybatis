package com.axuan.ibatis.io;

import java.io.InputStream;

/**
 * 封装了5个类加载器，见getClassLoaders方法
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/1 15:53
 */
public class ClassLoaderWrapper {


    //defaultClassLoader没地方初始化啊?
    ClassLoader defaultClassLoader;
    ClassLoader systemClassLoader;

    ClassLoaderWrapper() {
        try {
            systemClassLoader = ClassLoader.getSystemClassLoader();
        } catch (SecurityException ignored) {
            // AccessControlException on Google App Engine
        }
    }

    public InputStream getResourceAsStream(String resource) {
        return getResourceAsStream(resource, getClassLoaders(null));
    }

    public InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
        return getResourceAsStream(resource, getClassLoaders(classLoader));
    }


    /**
     * 用5个类加载器一个个查找资源，只要其中任何一个找到，就返回
     * @param resource:
     * @param classLoader:
     * @return java.io.InputStream
     * @author ZhouXuan
     * @date 2021/12/1 15:59
     */
    InputStream getResourceAsStream(String resource, ClassLoader[] classLoader) {
        for (ClassLoader cl : classLoader) {
            if (null != cl) {

                InputStream returnValue = cl.getResourceAsStream(resource);

                // 很多类加载器想要以'/'为前缀，因此我们加上它再试一次
                if (null == returnValue) {
                    returnValue = cl.getResourceAsStream("/" + resource);
                }

                if (null != returnValue) {
                    return returnValue;
                }
            }
        }
        return null;
    }

    ClassLoader[] getClassLoaders(ClassLoader classLoader) {
        return new ClassLoader[] {
                classLoader,
                defaultClassLoader,
                Thread.currentThread().getContextClassLoader(),
                getClass().getClassLoader(),
                systemClassLoader};
    }

    public Class<?> classForName(String name) throws ClassNotFoundException {
        return classForName(name, getClassLoaders(null));
    }

    public Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return classForName(name, getClassLoaders(classLoader));
    }




     // 用5个类加载器一个个调用Class.forName(加载类)，只要其中任何一个加载成功，就返回
    Class<?> classForName(String name, ClassLoader[] classLoader) throws ClassNotFoundException {

        for (ClassLoader cl : classLoader) {

            if (null != cl) {

                try {
                    Class<?> c = Class.forName(name,true, cl);

                    if (null != c) {
                        return c;
                    }
                } catch (ClassNotFoundException e) {
                    // we'll ignore this until all classloaders fail to locate the class
                }
            }
        }

        throw new ClassNotFoundException("Cannot find class: " + name);
    }

}
