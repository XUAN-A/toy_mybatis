package com.axuan.ibatis.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器链
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/12 16:02
 */
public class InterceptorChain {

    // 内部就是一个拦截器的List
    private final List<Interceptor> interceptors = new ArrayList<>();

    public Object pluginAll(Object target) {
        // 循环调用每个Interceptor.plugin方法
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }
        return target;
    }
}
