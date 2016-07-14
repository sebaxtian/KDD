/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author sebaxtian
 */
public class Carga {
    
    
    // Path del directorio de archivos Excel fuente
    private String pathDirFuente;
    // Path del directorio de archivos Excel ya Procesados
    private String pathDirProcesados;
    
    // Coneccion a Base de Datos
    private ConnectionDB connectionDB;
    
    
    
    /**
     * Metodo Contructor de clase
     * Recibe como argumento el path del directorio de archivos Excel fuente.
     * 
     * @param pathDirFuente 
     * @param pathDirProcesados 
     */
    public Carga(String pathDirFuente, String pathDirProcesados) {
        // Path del directorio de archivos Excel fuente
        this.pathDirFuente = pathDirFuente;
        // Path del directorio de archivos Excel ya Procesados
        this.pathDirProcesados = pathDirProcesados;
        
        // Coneccion a Base de Datos
        this.connectionDB = new ConnectionDB(ConnectionDB.DBMSMYSQL, "localhost", 3306, "ETLMIO", "root", "sebaxtian");
    }
    
    
    
    /**
     * Este metodo se encarga de realizar el proceso ETL.
     * 
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public void execute() throws IOException, InvalidFormatException {
        // Directorio de archivos Excel fuente
        File dirFuente = new File(this.pathDirFuente);
        // Verifica que sea un directorio
        if(dirFuente.isDirectory()) {
            // Obtiene la lista de archivos Excel del directorio
            File[] listExcel = dirFuente.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    // Filtro, los archivos deben tener extension .xlsx
                    String nombreArchivo = pathname.getName();
                    if(nombreArchivo.substring(nombreArchivo.length()-4, nombreArchivo.length()).equals("xlsx")) {
                        return true;
                    }
                    return false;
                }
            });
            // Imprime cuantos archivos de Excel tiene que procesar
            int numArchivosExcel = listExcel.length;
            System.out.println("Numero de Archivos Excel Para Procesar: " + numArchivosExcel);
            // Itera cada archivo Excel del directorio
            for(int i = 0; i < listExcel.length; i++) {
                // Obtiene archivo Excel i
                File fileExcel = listExcel[i];
                // Verifica que sea un archivo
                if(fileExcel.isFile()) {
                    
                    System.out.println("Obtiene Archivo Excel: " + fileExcel.getName());
                    // Crea un Libro de trabajo por cada archivo Excel
                    Workbook libroExcel = WorkbookFactory.create(new FileInputStream(fileExcel.getAbsolutePath()));
                    System.out.println("Abre Libro Excel: " + fileExcel.getName());
                    
                    // Se extrae la Fecha bruta
                    String fechaBruta = this.extraerFechaBruta(fileExcel.getName());
                    
                    // Itera por cada Hoja, Fila y Celda de un Libro Excel
                    for(Sheet hojaExcel : libroExcel ) {
                        
                        // Se extrae el Tiempo bruto
                        String tiempoBruto = this.extraerTiempoBruto(hojaExcel.getSheetName());
                        
                        for(Row fila : hojaExcel) {
                            // Si es la primera Fila de la Hoja
                            if(fila.getRowNum() == 0) {
                                continue;
                            }
                            // Si No es la ultima Fila de la Hoja
                            else if(hojaExcel.getLastRowNum() != fila.getRowNum()) {
                                // Opera los datos de cantidad de pasajeros por ruta estacion
                                String rutaEstacion = null;
                                int cantPasajeros = -1;
                                
                                // Primera celda de la fila
                                Cell celda1 = fila.getCell(fila.getFirstCellNum());
                                // Ultima ceda de la fila
                                Cell celdaN = fila.getCell(fila.getLastCellNum()-1);
                                
                                // Obtiene el Nombre de la Ruta-Estacion
                                if(celda1.getCellType() == Cell.CELL_TYPE_STRING) {
                                    //System.out.println("Primera Celda = " + celda1.getStringCellValue());
                                    rutaEstacion = celda1.getStringCellValue();
                                }
                                
                                // Obtiene la Cantidad de Pasajeros en la Ruta-Estacion
                                if(celdaN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    //System.out.println("Ultima Celda = " + celdaN.getNumericCellValue());
                                    cantPasajeros = (int)celdaN.getNumericCellValue();
                                }
                                
                                // Se extrae la dimension frecuencias
                                this.extraerHechosFrecuencias(fechaBruta, tiempoBruto, rutaEstacion, cantPasajeros);
                            }
                        }
                    }
                    // Cierra el stream
                    libroExcel.close();
                    
                    // Mueve el archivo Excel que ya ha sido Procesado
                    Files.move(FileSystems.getDefault().getPath(fileExcel.getAbsolutePath()), FileSystems.getDefault().getPath(this.pathDirProcesados+"/"+fileExcel.getName()), StandardCopyOption.REPLACE_EXISTING);
                    // Libera Recursos
                    libroExcel = null;
                    fileExcel = null;
                    System.gc();
                    // Imprime cuantos archivos Excel faltan procesar
                    System.out.println("Numero de Archivos Excel por Procesar: " + (numArchivosExcel--));
                }
            }
        }
    }
    
    
    
    
    /**
     * Este metodo construye el HashMap de la Dimension Fecha.
     * 
     * La llave del HashMap corresponde a valor en bruto de la fecha.
     * El valor del HashMap corresponde a un String SQL INSERT INTO.
     * 
     * @param nombreArchivo 
     */
    private String extraerFechaBruta(String nombreArchivo) {
        // Fecha con formato bruto
        String fechaBruta = nombreArchivo.substring(0, 6);
        return fechaBruta;
    }
    
    
    
    /**
     * Este metodo construye el HashMap de la Dimension Tiempo.
     * 
     * La llave del HashMap corresponde a valor en bruto del tiempo.
     * El valor del HashMap corresponde a un String SQL INSERT INTO.
     * 
     * @param nombreHoja 
     */
    private String extraerTiempoBruto(String nombreHoja) {
        //System.out.println("Hoja del Libro: " + nombreHoja);
        String tiempoBruto = nombreHoja.substring(4, nombreHoja.length());
        return tiempoBruto;
    }
    
    
    
    
    private void extraerHechosFrecuencias(String fechaBruta, String tiempoBruto, String rutaEstacion, int cantPasajeros) {
        // Obtiene las llaves primarias de las dimensiones
        int id_fecha = connectionDB.selectIdFecha(fechaBruta);
        int id_tiempo = connectionDB.selectIdTiempo(tiempoBruto);
        int id_ruta_estacion = connectionDB.selectIdRutaEstacion(rutaEstacion);
        
        // Valida que existan las llaves primarias
        if(id_fecha > 0 && id_tiempo > 0 && id_ruta_estacion > 0) {
            // String SQL INSERT INTO
            String strSQL = "INSERT INTO frecuencias (fk_fecha, fk_tiempo, fk_ruta_estacion, cant_pasajeros) "
                    + "VALUES (" + id_fecha + ", " + id_tiempo + ", " + id_ruta_estacion + ", " + cantPasajeros + ");";
            connectionDB.insertTablaFrecuencias(strSQL);
        }
    }
    
    
}
