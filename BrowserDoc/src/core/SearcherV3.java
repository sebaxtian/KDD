/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author oswaldo
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.lucene.index.*;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

// From chapter 1

/**
 * This code was originally written for
 * Erik's Lucene intro java.net article
 */
public class SearcherV3 {
 private static IndexSearcher is = null;
  public static void main(String[] args) throws IllegalArgumentException,
        IOException, ParseException {
      
    String indexDir ="/home/sebaxtian/Descargas/index";
    String q = JOptionPane.showInputDialog("Digite su consulta");                 //2   

    search(indexDir, q);
  }
  public IndexSearcher  getIndexSearcher(){
      return is;
  }

  public static TopDocs search(String indexDir, String q)
    throws IOException, ParseException {

    Directory dir = FSDirectory.open(new File(indexDir)); //3
    IndexReader reader = IndexReader.open(dir);
    is = new IndexSearcher(reader);   //3   

    QueryParser parser = new QueryParser(Version.LUCENE_30, // 4
                                         "contents",  //4
                     new StandardAnalyzer(          //4
                       Version.LUCENE_30));  //4
    Query query = parser.parse(q);              //4   
    long start = System.currentTimeMillis();
    TopDocs hits = is.search(query, 100); //5
    long end = System.currentTimeMillis();

    System.err.println("Found " + hits.totalHits +   //6  
      " document(s) (in " + (end - start) +        // 6
      " milliseconds) that matched query '" +     // 6
      q + "':");                                   // 6
     
     for(ScoreDoc scoreDoc : hits.scoreDocs) {
      Document doc = is.doc(scoreDoc.doc);               //7 
     
      System.out.println(doc.get("filename"));  //8
      System.out.println("   Doc id = " + scoreDoc.doc + "  Score" + scoreDoc.score);
      //Muestra la relevancia del documento para la consulta q
      //System.out.println("  Explain==>>  " + is.explain(query, scoreDoc.doc));
      
    //System.out.println("Similarity" + is.getSimilarity().toString());
     }
        is.close();   
        return hits;//9
  }
}//end class
  


/*
#1 Parse provided index directory
#2 Parse provided query string
#3 Open index
#4 Parse query
#5 Search index
#6 Write search stats
#7 Retrieve matching document
#8 Display filename
#9 Close IndexSearcher
*/