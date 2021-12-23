package com.axuan.test;

import com.axuan.ibatis.io.Resources;
import com.axuan.ibatis.session.SqlSession;
import com.axuan.ibatis.session.SqlSessionFactory;
import com.axuan.ibatis.session.SqlSessionFactoryBuilder;
import com.axuan.mapper.BlogMapper;
import com.axuan.pojo.Blog;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/10 10:41
 */
public class MyBatisTest {

    public static void main(String[] args) throws IOException {
        // 1.读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2.创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        // 3.使用工厂产生SqlSession对象
        SqlSession session = factory.openSession(true);

        // 4.使用SqlSession创建Dao接口的代理对象
        BlogMapper blogMapper = session.getMapper(BlogMapper.class);

        //List<Blog> blogs = blogMapper.listBlogs();
        //for (Blog blog : blogs) {
        //    System.out.println(blog);
        //}

        //Blog blog = blogMapper.selectBlog(2);
        //System.out.println(blog);

        //Blog blog2 = blogMapper.selectBlog(2);
        //System.out.println(blog2);


        //int result = blogMapper.deleteBlogById(38);
        //System.out.println(result);

        //int result = blogMapper.insertBlog(new Blog( "测试4", "测试4"));
        //int result2 = blogMapper.insertBlog(new Blog("测试2", "测试2"));
        //System.out.println(result);
        //System.out.println(result2);

        //session.commit();
    }
}
