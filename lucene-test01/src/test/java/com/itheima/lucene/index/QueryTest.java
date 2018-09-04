package com.itheima.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/*定义搜索 方法*/
public class QueryTest {
   /* TermQuery 关键词查询
        需求：查询图书名称域中包含有 java 的图书。*/
   @Test
   public void testTermQuery() throws Exception{
       //创建查询对象
       TermQuery q = new TermQuery(new Term("bookName","java"));
       //执行搜索
       search(q);
   }

    /**
     * NumericRangeQuery: 数值范围查询对象
     * 需求：查询图书价格在80到100之间的图书
     * 不包含边界值
     */


    @Test
    public void  testNumericRangeQuery() throws Exception {

        /**
         * NumericRangeQuery: 数值范围查询对象
         * String field: 域的名称
         * Double min : 最小边界值
         * Double max: 最大边界值
         * boolean minInclusive: 是否包含最小边界值
         * boolean maxInclusive: 是否包含最大边界值
         */
        Query q = NumericRangeQuery.newDoubleRange("bookPrice",80d,100d,true,true);
        //执行搜索
        search(q);


    }
    /**
     * BooleanQuery: 组装条件的查询对象
     * 需求：查询图书名称域中包含有java的图书，并且价格在80到100之间（包含边界值）
     */
    @Test
    public void testBooleanQuery() throws Exception{
        //创建条件对象一
        Query q1 = new TermQuery(new Term("bookName","java"));
        //创建条件对象二
        Query q2 = NumericRangeQuery.newDoubleRange("bookPrice",80d,100d,true,true);
        BooleanQuery q = new BooleanQuery();
        q.add(q1,BooleanClause.Occur.MUST);
        q.add(q2,BooleanClause.Occur.MUST);
        //执行搜索
        search(q);
    }
    /**
     * QueryParser 把一个表达式解析成Query对象
     * 需求：查询图书名称域中包含有java，并且图书名称域中包含有lucene的图书
     */
    @Test
    public void testQueryParser() throws Exception{
        //创建分词器,用于分词
        Analyzer analyzer = new IKAnalyzer();
        //创建QueryParser
        QueryParser queryParser = new QueryParser("bookName",analyzer);
        //把查询表达式解析成Query对象
        Query q = queryParser.parse("bookName:java OR bookName:lucene");
        //执行搜索
        search(q);
    }
    private void search(Query query) throws Exception{
        //查询语句
        System.out.println("查询语法:" + query);
        //创建索引库储存目录
        Directory directory = FSDirectory.open(new File("E:\\index"));
        //创建IndexReader,读取索引库对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建IndexSearcher,读取搜索索引库
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
       /* search 方法：执行搜索
                * 参数一：查询对象
                * 参数二：指定搜索结果排序后的前 n 个（前 10 个）*/
        TopDocs topDocs = indexSearcher.search(query,10);
        //处理结果集
        System.out.println("总命中的记录数:"+ topDocs.totalHits);
        //获取搜索得到文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // ScoreDoc 对象：只有文档 id 和分值信息
        for (ScoreDoc scoreDoc:scoreDocs) {
            System.out.println("--------------------------");
            System.out.println("文档id:"+scoreDoc.doc+"\t文档分值:"+scoreDoc.score);
            //根据文档id获取指定的文档
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("图书Id:"+doc.get("id"));
            System.out.println("图书名称:"+doc.get("bookName"));
            System.out.println("图书价格:"+doc.get("bookPrice"));
            System.out.println("图书图片:"+doc.get("bookPic"));
            System.out.println("图书描述:"+doc.get("bookDesc"));

        }
        //释放资源
        indexReader.close();
    }

}
