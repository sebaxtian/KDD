/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browserdocindexer;

import core.TikaIndexer;

/**
 *
 * @author sebaxtian
 */
public class BrowserDocIndexer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String dataDir = args[0];
        String indexDir = args[1];
        
        System.out.println("Data Dir: " + dataDir);
        System.out.println("Index Dir: " + indexDir);
        
        TikaIndexer.indexer(dataDir, indexDir);
    }
    
}
