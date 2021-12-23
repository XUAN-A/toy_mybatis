package com.axuan.ibatis.session;


import com.axuan.ibatis.Exceptions.ExceptionFactory;
import com.axuan.ibatis.builder.xml.XMLConfigBuilder;
import com.axuan.ibatis.session.defaults.DefaultSqlSessionFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用于创建一个SqlSessionFactory对象
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 10:26
 */
public class SqlSessionFactoryBuilder {

        public SqlSessionFactory build(InputStream inputStream) {
            return build(inputStream, null);
        }

        public SqlSessionFactory build(InputStream inputStream, String environment) {
            try {
                XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment);
                return build(parser.parse());
            } catch (Exception e) {
                throw ExceptionFactory.wrapException("Error building SqlSession", e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public SqlSessionFactory build(Configuration config) {
            return new DefaultSqlSessionFactory(config);
        }

}
