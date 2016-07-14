/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;


import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.lucene.index.IndexWriterConfig;
//import thesis_analyzer.*;




public class IRIndexer {
    
   private IndexWriter writer ;
  
   
   String filesIndexed ="ok"; //Sirve para pintar en el JTextArea los archivos indexados
   
  
   public IRIndexer(String indexDir) throws IOException {
    Directory dir = FSDirectory.open(new File(indexDir));
   
    boolean actual_index=true;//Sirve para saber si se crea un indice nuevo o se abre uno existente
    String mensaje="";
    
    
    actual_index=checkOldIndex(indexDir);//Verifica si existe un indice en  la ruta dada
    System.out.println("Actual index:" + actual_index);
    
    if (actual_index==true){//Si existe un indice actal, se abre el existente
    
          writer = new IndexWriter(dir,            //3
                 new StandardAnalyzer(Version.LUCENE_30),//3
                 false,
                 IndexWriter.MaxFieldLength.UNLIMITED); //3 
          
         
          mensaje="Se abrió el indice IR existente  en esta ruta";       
    }
    else{
        writer = new IndexWriter(dir,            //3
                 new StandardAnalyzer(Version.LUCENE_30),//3
                 true,
                 IndexWriter.MaxFieldLength.UNLIMITED); //3   
        mensaje="Se ha creado un nuevo  indexador Sintactico IR";
    }
    
    
        mensaje="Se abrió el indice IR existente  en esta rute";
    System.out.println("************************a*****************");
    System.out.println(mensaje);
    System.out.println("******************************************");
   
     
   }//end constructor
   
  
  
  public static boolean checkOldIndex(String indexDir){
      
       File[] files = new File(indexDir).listFiles();
      
       if (files.length>0){
           return true;
       }
       else{
           return false;
       }
       
              
  }

  public void close() throws IOException {
    writer.close();                             //4
  }

  public int index(String dataDir, FileFilter filter)
    throws Exception {

    File[] files = new File(dataDir).listFiles();
    int i=0;
    for (File f: files) {
      if (!f.isDirectory() &&
          !f.isHidden() &&
          f.exists() &&
          f.canRead() &&
          (filter == null || filter.accept(f))) {
        indexFile(f);
        i++;
      }
    }
    writer.close();
    

    return writer.numDocs();                     //5
  }

  private static class TextFilesFilter implements FileFilter {
    public boolean accept(File path) {
      return path.getName().toLowerCase()        //6
             .endsWith(".txt");                  //6
    }
  }

  protected Document getDocument(File f) throws Exception {
    Document doc = new Document();    
    doc.add(new Field("contents", new FileReader(f)));      //7
    doc.add(new Field("filename", f.getName(),              //8
                Field.Store.YES, Field.Index.NOT_ANALYZED));//8
    doc.add(new Field("fullpath", f.getCanonicalPath(),     //9
                Field.Store.YES, Field.Index.NOT_ANALYZED));//9
    return doc;
  }

  private void indexFile(File f) throws Exception {
    System.out.println("Indexing " + f.getCanonicalPath());
    filesIndexed = filesIndexed  +  "Indexing " + f.getCanonicalPath() + "\n";    
    Document doc = getDocument(f);  
    
    //********   Aquí se hace la indexación sintactica para el documento********
    writer.addDocument(doc);//10
     //********    Fin de indexacion IR sintactica ********    
    
    
   // int docId = Integer.parseInt(doc.get("id"));
    
    
    
  }//end indexFile
  
  public String getFilesIndexed(){
      System.out.println("\n\n\n");
      System.out.println("files:" + filesIndexed);
      return filesIndexed;
  }
  
  
 public static void main(String[] args){ 
     try { 
         String data="/home/sebaxtian/Descargas/data";
         String index="/home/sebaxtian/Descargas/index";
         IRIndexer indexer = null;
         
         try {
             indexer = new IRIndexer(index);
         } catch (IOException ex) {
             Logger.getLogger(IRIndexer.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         TextFilesFilter tf = new  TextFilesFilter();
         indexer.index(data, tf);
         
     } catch (Exception ex) {
           Logger.getLogger(IRIndexer.class.getName()).log(Level.SEVERE, null, ex);
     }
     
 }
  
  
  
}//end class

