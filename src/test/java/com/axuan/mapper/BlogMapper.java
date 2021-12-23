package com.axuan.mapper;

import com.axuan.ibatis.annotations.Delete;
import com.axuan.ibatis.annotations.Insert;
import com.axuan.ibatis.annotations.Select;
import com.axuan.pojo.Blog;

import java.util.List;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 17:35
 */
public interface BlogMapper {

    @Select("select * from blog where id = #{id}")
    Blog selectBlog(Integer id);

    @Select("select * from blog")
    List<Blog> listBlogs();

    @Insert("insert into blog(name,author) values(#{name},#{author})")
    int insertBlog(Blog blog);

    @Delete("delete from blog where id = #{id}")
    int deleteBlogById(int id);
}
