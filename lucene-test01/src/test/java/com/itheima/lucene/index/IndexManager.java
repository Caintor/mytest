package com.itheima.lucene.index;

import com.itheima.lucene.dao.IBookDao;
import com.itheima.lucene.dao.impl.BookDaoImpl;
import com.itheima.lucene.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*索引管理类*/
public class IndexManager {
    @Test
    public void createIndex() throws Exception {
        //采集数据
        IBookDao bookDao = new BookDaoImpl();
        List<Book> bookList = bookDao.findAll();
        //创建文档集合对象
        List<Document> documents = new ArrayList<Document>();
        for (Book book: bookList) {
            //创建文档对象
            Document doc = new Document();
          /*  *
             * 给文档对象添加域
             * 方法：add（）
             *   参数：TextField
             * TextField参数：
             *   参数一：域的名称
             *   参数二：域的值
             *   参数三：指定是否把域值存储到文档对象中
*/
          //图书id
            doc.add(new TextField("id",book.getId()+"", Field.Store.YES));
            //图书名称
           doc.add(new TextField("bookName",book.getBookName(),Field.Store.YES));
            //图书价格
            doc.add(new TextField("bookPrice",book.getPrice() + "", Field.Store.YES));
            // 图书图片
            doc.add(new TextField("bookPic",book.getPic(), Field.Store.YES));

            //图书描述
            doc.add(new TextField("bookDesc",book.getBookDesc(),Field.Store.YES));
            documents.add(doc);
        }
        //创建分词器(Analyzer),用于分词
       // Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        //创建索引库配置对象,用于配置索引库
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        //设置索引库打开模式,(每次都重新创建)
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //创建索引库目录对象,用于指定索引库储存位置
        Directory directory = FSDirectory.open( new File("E:\\index"));
        //创建索引库操作对象,用于把文档写入索引库
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
        //循环文档,写入索引库
        for (Document document:documents) {
        /*    * addDocument方法：把文档对象写入索引库 */
        indexWriter.addDocument(document);
        //提交事务
            indexWriter.commit();

        }
        //释放资源
        indexWriter.close();
    }

}
