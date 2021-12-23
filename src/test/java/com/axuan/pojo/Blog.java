package com.axuan.pojo;

/**
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/11/30 17:35
 */
public class Blog {
    private int id;
    private String name;
    private String author;

    public Blog() {
    }

    public Blog(int id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public Blog(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
