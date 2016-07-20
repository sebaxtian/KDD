package core;

/**
 * Copyright Manning Publications Co.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific lan      
*/

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.config.TikaConfig;      

import org.xml.sax.ContentHandler;
import javax.swing.JOptionPane;
//import thesis_analyzer.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;





public class TikaIndexer extends IRIndexer {

  private boolean DEBUG = false;                     //1
  

  static Set<String> textualMetadataFields           //2
        = new HashSet<String>();                     //2
  static {                                           //2
    textualMetadataFields.add(Metadata.TITLE);       //2
    textualMetadataFields.add(Metadata.AUTHOR);      //2
    textualMetadataFields.add(Metadata.COMMENTS);    //2
    textualMetadataFields.add(Metadata.KEYWORDS);    //2
    textualMetadataFields.add(Metadata.DESCRIPTION); //2
    textualMetadataFields.add(Metadata.SUBJECT);     //2
  }
  
  
  
  
  
  public static void indexer(String dataDir, String indexDir) {
      try {

          
          TikaConfig config = TikaConfig.getDefaultConfig();  //3
          
          
          long start = new Date().getTime();
          TikaIndexer indexer = null;
          try {
              indexer = new TikaIndexer(indexDir);
          } catch (IOException ex) {
              Logger.getLogger(TikaIndexer.class.getName()).log(Level.SEVERE, null, ex);
          }
          
          
          int numIndexed = 0;
          try {
              numIndexed = indexer.index(dataDir, null);
          } catch (Exception ex) {
              Logger.getLogger(TikaIndexer.class.getName()).log(Level.SEVERE, null, ex);
          }
          indexer.close();
          long end = new Date().getTime();
          
          System.out.println("Indexing " + numIndexed + " files took "
                  + (end - start) + " milliseconds");
      } catch (IOException ex) {
          Logger.getLogger(TikaIndexer.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
  
  
  
  
  
/*
  public static void main(String[] args)  {
    
      
      try {

          
          TikaConfig config = TikaConfig.getDefaultConfig();  //3
          
          //3
          
          //String  dataDir=JOptionPane.showInputDialog("Digite la ruta de los datos");
          //String indexDir=JOptionPane.showInputDialog("Digite la ruta del index");
          
          String dataDir="/home/sebaxtian/Descargas/BrowserDoc/data";
          String indexDir="/home/sebaxtian/Descargas/BrowserDoc/index";
          
          
          long start = new Date().getTime();
          TikaIndexer indexer = null;
          try {
              indexer = new TikaIndexer(indexDir);
          } catch (IOException ex) {
              Logger.getLogger(TikaIndexer.class.getName()).log(Level.SEVERE, null, ex);
          }
          
          
          int numIndexed = 0;
          try {
              numIndexed = indexer.index(dataDir, null);
          } catch (Exception ex) {
              Logger.getLogger(TikaIndexer.class.getName()).log(Level.SEVERE, null, ex);
          }
          indexer.close();
          long end = new Date().getTime();
          
          System.out.println("Indexing " + numIndexed + " files took "
                  + (end - start) + " milliseconds");
      } catch (IOException ex) {
          Logger.getLogger(TikaIndexer.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
*/



  public TikaIndexer(String indexDir) throws IOException {
    super(indexDir); 
    
    
  }
  

    @Override
  protected Document getDocument(File f) throws Exception {
    //String pruebaDoc;//sirve para colocar el archivo de entrada temporalmente, borrar luego, OJO solo es temporal
    String docId;
    String documentContent;
    Metadata metadata = new Metadata();
    metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());   // 4
    // If you know content type (eg because this document
    // was loaded from an HTTP server), then you should also
    // set Metadata.CONTENT_TYPE

    // If you know content encoding (eg because this
    // document was loaded from an HTTP server), then you
    // should also set Metadata.CONTENT_ENCODING

    InputStream is = new FileInputStream(f);      // 5
    Parser parser = new AutoDetectParser();       // 6
    ContentHandler handler = new BodyContentHandler(); // 7    
    ParseContext context = new ParseContext();   // 8
    context.set(Parser.class, parser);           // 8
    //System.out.println("\n ok2");
    try {
      parser.parse(is, handler, metadata,      // 9
                   new ParseContext());        // 9
    } finally {
      is.close();
    }

    Document doc = new Document();
   
    doc.add(new Field("contents", handler.toString(),           // 10
                      Field.Store.NO, Field.Index.ANALYZED));   // 10
    
    //documentContent se usara para crear el archivo que será anotado semanticamente
    documentContent=handler.toString();
     
    if (DEBUG) {
      System.out.println("  all text: " + handler.toString());
      System.out.println("------------------------------------------" );
    }
    
    System.out.println("Metadata name: " + metadata.get(Metadata.TITLE));
    for(String name : metadata.names()) {         //11
      String value = metadata.get(name);
      //System.out.println("Metadata name: " + name + "  Metadata value: " +  value);
     
      if (textualMetadataFields.contains(name)) {
        doc.add(new Field("contents", value,      //12
                          Field.Store.NO, Field.Index.ANALYZED));
        
      }

      doc.add(new Field(name, value, Field.Store.YES, Field.Index.NO)); //13

      if (DEBUG) {
        System.out.println("  " + name + ": " + value);
      }
    }

    if (DEBUG) {
      System.out.println();
    }
    
    doc.add(new Field("filename", f.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
    //----Lo nuevo----------------------
    
    //doc.add(new Field ("id",  docId, Field.Store.YES, Field.Index.NOT_ANALYZED));   
    
     //----Lo nuevo----------------------
    return doc;
  }//end getDocument
  
    
 
    
 
  
    
    

}//end TikaIndexer

