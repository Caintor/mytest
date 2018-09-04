package com.itheima.lucene.dao.impl;

import com.itheima.lucene.dao.IBookDao;
import com.itheima.lucene.pojo.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*图书数据访问接口实现类*/
public class BookDaoImpl implements IBookDao{
    @Override
    public List<Book> findAll() {
        //创建List集合封装 查询 结果
        List<Book> bookList= new ArrayList<Book>();
        Connection connection = null;
        PreparedStatement psmt =null;
        ResultSet rs = null;

        try {
            //加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //创建数库连接对象
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lucene_db","root","123456");
                 //编写sql语句
                String sql = "select * from book";
                //创建statement
                psmt = connection.prepareStatement(sql);
                //执行查询
            rs = psmt.executeQuery();
            //处理结果集
            while (rs.next()){
                //创建图书对象
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setBookName(rs.getString("bookname"));
                book.setPic(rs.getString("pic"));
                book.setPrice(rs.getFloat("price"));
                book.setBookDesc(rs.getString("bookdesc"));
                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                //释放资源
                if (rs!=null) rs.close();
                if (psmt!= null) psmt.close();;
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bookList;
    }
}
