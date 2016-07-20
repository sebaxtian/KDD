/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browserdocsearcher;

/**
 *
 * @author sebaxtian
 */
public class Documento {
    
    
    private final int docId; // Identificador del documento
    private final float ranking; // Ranking del documento
    private final String resourceName; // Nombre de archivo
    private final String filename; // Path de archivo
    private final String created; // Fecha de creacion
    private final String xmpTPgNPages; // Numero de paginas
    private final String contentType; // Tipo de documento
    
    
    
    public Documento(int docId, float ranking, String filename, String resourceName, String created, String xmpTPgNPages, String contentType) {
        this.docId = docId;
        this.ranking = ranking;
        this.filename = filename;
        this.resourceName = resourceName;
        this.created = created;
        this.xmpTPgNPages = xmpTPgNPages;
        this.contentType = contentType;
    }
    
    
    
    public int getDocId() {
        return docId;
    }
    
    
    public float getRanking() {
        return ranking;
    }
    
    
    public String getPath() {
        return filename;
    }
    
    
    public String getName() {
        return resourceName;
    }
    
    
    public String getFechaCreado() {
        return created;
    }
    
    
    public String getNumPaginas() {
        return xmpTPgNPages;
    }
    
    
    public String getTipo() {
        return contentType;
    }
    
    
}
