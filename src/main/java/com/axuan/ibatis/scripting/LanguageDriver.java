package com.axuan.ibatis.scripting;

import com.axuan.ibatis.mapping.SqlSource;
import com.axuan.ibatis.parsing.XNode;
import com.axuan.ibatis.session.Configuration;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/11 16:31
 */
public interface LanguageDriver {



    // 创建SQL源码(mapper xml)方式
    SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType);
}
