package com.itheima.lucene.dao;

import com.itheima.lucene.pojo.Book;

import java.util.List;

/*图书数据访问接口*/
public interface IBookDao {
    /*查询全部图书*/
    List<Book> findAll();
}
