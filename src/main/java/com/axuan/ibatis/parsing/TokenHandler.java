package com.axuan.ibatis.parsing;

/**
 * 记号处理器
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/2 8:04
 */
public interface TokenHandler {

    // 处理记号
    String handleToken(String content);
}
