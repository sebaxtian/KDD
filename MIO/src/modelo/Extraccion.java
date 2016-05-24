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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Esta clase representa al modelo de Extraccion en un sistema ETL
 * 
 * La extracion de los datos se hace tomando como fuente un
 * directorio de archivos en formato Excel.
 * 
 * @author sebaxtian
 * @version 0.1.0
 * @date mar may 24 00:04:55 COT 2016
 */


public class Extraccion {

    // Path del directorio de archivos Excel fuente
    private String pathDirFuente;
    // Lista de rutas del MIO
    private ArrayList<String> listRutas;
    // Lista de estaciones del MIO
    private ArrayList<String> listEstaciones;
    
    
    /**
     * Metodo Contructor de clase
     * Recibe como argumento el path del directorio de archivos Excel fuente
     * @param pathDirFuente 
     */
    public Extraccion(String pathDirFuente) {
        this.pathDirFuente = pathDirFuente;
        this.listRutas = new ArrayList<>();
        this.listEstaciones = new ArrayList<>();
    }
    
    
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
                    // Itera por cada Hoja, Fila y Celda de un Libro Excel
                    for(Sheet hojaExcel : libroExcel ) {
                        for(Row fila : hojaExcel) {
                            // Si es la primera Fila de la Hoja
                            if(fila.getRowNum() == 0) {
                                // Obtiene la fila 0 donde se muestran los nombre de Rutas y Estaciones
                                this.extraerRutaEstacion(fila);
                            } else {
                                // Opera los datos de cantidad de pasajeros
                                for(Cell celda : fila) {
                                    
                                }
                            }
                        }
                    }
                    // Cierra el stream
                    libroExcel.close();
                }
            }
        }
    }
    
    
    /**
     * Este metodo lee y construye la lista de Rutas y Estaciones
     * @param fila 
     */
    private void extraerRutaEstacion(Row fila) {
        for(Cell celda : fila) {
            // Los nombre de Estacion y Ruta son de tipo string
            if(celda.getCellType() == Cell.CELL_TYPE_STRING) {
                String estacionOruta = celda.getRichStringCellValue().getString();
                // Se omiten valores
                if(!estacionOruta.equals("ORIGEN \\ DESTINO") && !estacionOruta.equals("TOTAL") && !estacionOruta.equals("SIN DATO") && !estacionOruta.equals("SIN HORARIO")) {
                    // Verifica que sea una Ruta
                    Pattern regex = Pattern.compile("[A-Z][0-9]");
                    Matcher matcher = regex.matcher(estacionOruta);
                    // Si es una Ruta
                    if(matcher.find()) {
                        // Verifica que no se encuetre en la lista
                        if(!listRutas.contains(estacionOruta)) {
                            // Agrega la Ruta a la lista
                            listRutas.add(estacionOruta);
                        }
                    } else { // Si no es una ruta es una Estacion
                        // Verifica que no se encuentre en la lista
                        if(!listEstaciones.contains(estacionOruta)) {
                            // Agrega la Estacion a la lista
                            listEstaciones.add(estacionOruta);
                        }
                    }
                }
            }
        }
    }
    
    
    /**
     * Metodo que imprime en la salida estandar del sistema la lista de Rutas.
     */
    public void printRutas() {
        System.out.println("Lista de Rutas:");
        for (String next : listRutas) {
            System.out.println(next);
        }
    }
    
    
    /**
     * Metodo que imprime en la salida estandar del sistema la lista de Estaciones.
     */
    public void printEstaciones() {
        System.out.println("Lista de Estaciones:");
        for (String next : listEstaciones) {
            System.out.println(next);
        }
    }
    
}
