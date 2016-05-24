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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
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

/**
 * Esta clase representa al modelo de ETL en un sistema ETL
 
 La extracion de los datos se hace tomando como fuente un
 directorio de archivos en formato Excel.
 * @author sebaxtian
 * @version 0.1.0
 * @date mar may 24 00:04:55 COT 2016
 */
public class ETL {

    // Path del directorio de archivos Excel fuente
    private String pathDirFuente;
    // Lista de rutas del MIO
    private ArrayList<String> listRutas;
    // Lista de estaciones del MIO
    private ArrayList<String> listEstaciones;
    // Lista de INSERT INTO en dim_ruta_estacion
    private ArrayList<String> dimRutaEstacion;
    // Lista de INSERT INTO en dim_fecha
    private ArrayList<String> dimFecha;
    
    
    /**
     * Metodo Contructor de clase
     * Recibe como argumento el path del directorio de archivos Excel fuente.
     * @param pathDirFuente 
     */
    public ETL(String pathDirFuente) {
        this.pathDirFuente = pathDirFuente;
        this.listRutas = new ArrayList<>();
        this.listEstaciones = new ArrayList<>();
        this.dimRutaEstacion = new ArrayList<>();
        this.dimFecha = new ArrayList<>();
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
                    this.extraerFecha(fileExcel.getName());
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
    
    
    private void extraerFecha(String nombreArchivo) {
        // Obtiene el nombre del libro
        String nombreLibro = nombreArchivo.substring(0, 2) + "-" + nombreArchivo.substring(2, 4) + "-" + nombreArchivo.substring(4, 6);
        try {
            Date fechaLibro = new SimpleDateFormat("yy-MM-dd").parse(nombreLibro);
            
            // Obtiene informacion de la fecha del libro
            SimpleDateFormat dateFormatter = new SimpleDateFormat();
            
            // Fecha Fuente
            dateFormatter.applyPattern("yyyy-MM-dd");
            String strFechaLibro = dateFormatter.format(fechaLibro);
            //System.out.println("Fecha Libro: " + strFechaLibro);
            
            // Anio
            dateFormatter.applyPattern("yyyy");
            String anio = dateFormatter.format(fechaLibro);
            //System.out.println("Año: " + anio);
            
            // Mes del anio
            dateFormatter.applyPattern("MM");
            String mes = dateFormatter.format(fechaLibro);
            //System.out.println("Mes: " + mes);
            
            // Nombre del mes
            dateFormatter.applyPattern("MMMM");
            String nombreMes = dateFormatter.format(fechaLibro);
            //System.out.println("Nombre del Mes: " + nombreMes);
            
            // Semana en el anio
            dateFormatter.applyPattern("w");
            String semanaAnio = dateFormatter.format(fechaLibro);
            //System.out.println("Semana en el Año: " + semanaAnio);
            
            // Semana en el mes
            dateFormatter.applyPattern("W");
            String semanaMes = dateFormatter.format(fechaLibro);
            //System.out.println("Semana en el Mes: " + semanaMes);
            
            // Dia en el anio
            dateFormatter.applyPattern("D");
            String diaAnio = dateFormatter.format(fechaLibro);
            //System.out.println("Dia en el Año: " + diaAnio);
            
            // Dia en el mes
            dateFormatter.applyPattern("dd");
            String dia = dateFormatter.format(fechaLibro);
            //System.out.println("Dia en el Mes: " + dia);
            
            // Dia de la semana en el mes
            dateFormatter.applyPattern("F");
            String diaSemanaMes = dateFormatter.format(fechaLibro);
            //System.out.println("Dia de la Semana en el Mes: " + diaSemanaMes);
            
            // Nombre del dia en la semana
            dateFormatter.applyPattern("EEEEE");
            String nombreDia = dateFormatter.format(fechaLibro);
            //System.out.println("Nombre del Dia en la Semana: " + nombreDia);
            
            // Numero de dia de la semana
            dateFormatter.applyPattern("u");
            String numDiaSemana = dateFormatter.format(fechaLibro);
            //System.out.println("Numero de Dia de la Semana: " + numDiaSemana);
            
            // Nombre del dia festivo
            dateFormatter.applyPattern("yyyy-MM-dd");
            String nombreFestivo = buildDiasFestivos().get(dateFormatter.format(fechaLibro));
            boolean esFestivo = true;
            if(nombreFestivo == null) {
                nombreFestivo = "";
                esFestivo = false;
            }
            //System.out.println("Dia Festivo: " + nombreFestivo);
            
            // Nombre del dia feriado
            dateFormatter.applyPattern("yyyy-MM-dd");
            String nombreFeriado = buildDiasFeriados().get(dateFormatter.format(fechaLibro));
            boolean esFeriado = true;
            if(nombreFeriado == null) {
                nombreFeriado = "";
                esFeriado = false;
            }
            //System.out.println("Dia Feriado: " + nombreFeriado);
            
            
            String insertinto = "INSERT INTO dim_fecha VALUES ('" +
                    strFechaLibro + "', '" +
                    anio + "', '" +
                    mes + "', '" +
                    nombreMes + "', '" +
                    semanaAnio + "', '" +
                    semanaMes + "', '" +
                    diaAnio + "', '" +
                    dia + "', '" +
                    diaSemanaMes + "', '" +
                    nombreDia + "', '" +
                    numDiaSemana + "', " +
                    esFestivo + ", '" +
                    nombreFestivo + "', " +
                    esFeriado + ", '" +
                    nombreFeriado +
                    "');";
            
            dimFecha.add(insertinto);
            
            
        } catch (ParseException ex) {
            System.err.println("Error al transformar nombre de libro en formato fecha");
            ex.printStackTrace();
        }
    }
    
    
    /**
     * Metodo que construye una tabla Hash con los dias festivos de
     * Colombia
     * 
     * La tabla Hash toma como key una fecha "2014-01-01" y el valor
     * es el nombre del dia festivo
     * 
     * @return diasFestivos
     */
    private Hashtable<String, String> buildDiasFestivos() {
        // Dias Festivos En Colombia
        Hashtable<String, String> diasFestivos = new Hashtable<>();
        
        // Dias Festivos Anio 2014
        diasFestivos.put("2014-01-01", "Anio Nuevo");
        diasFestivos.put("2014-01-06", "Dia de los Reyes Magos");
        diasFestivos.put("2014-03-24", "Dia de San Jose");
        diasFestivos.put("2014-04-13", "Domingo de Ramos");
        diasFestivos.put("2014-04-17", "Jueves Santo");
        diasFestivos.put("2014-04-18", "Viernes Santo");
        diasFestivos.put("2014-04-20", "Domingo de Resurreccion");
        diasFestivos.put("2014-05-01", "Dia del Trabajo");
        diasFestivos.put("2014-06-02", "Dia de la Ascension");
        diasFestivos.put("2014-06-23", "Corpus Christi");
        diasFestivos.put("2014-06-30", "Sagrado Corazon, San Pedro y San Pablo");
        diasFestivos.put("2014-07-20", "Dia de la Independencia");
        diasFestivos.put("2014-08-07", "Batalla de Boyaca");
        diasFestivos.put("2014-08-18", "La asuncion de la Virgen");
        diasFestivos.put("2014-10-13", "Dia de la Raza");
        diasFestivos.put("2014-11-03", "Todos los Santos");
        diasFestivos.put("2014-11-17", "Independencia de Cartagena");
        diasFestivos.put("2014-12-08", "Dia de la Inmaculada Concepcion");
        diasFestivos.put("2014-12-25", "Dia de Navidad");
        // Dias Festivos Anio 2015
        diasFestivos.put("2015-01-01", "Anio Nuevo");
        diasFestivos.put("2015-01-12", "Dia de los Reyes Magos");
        diasFestivos.put("2015-03-23", "Dia de San Jose");
        diasFestivos.put("2015-03-29", "Domingo de Ramos");
        diasFestivos.put("2015-04-02", "Jueves Santo");
        diasFestivos.put("2015-04-03", "Viernes Santo");
        diasFestivos.put("2015-04-05", "Domingo de Resurreccion");
        diasFestivos.put("2015-05-01", "Dia del Trabajo");
        diasFestivos.put("2015-05-18", "Dia de la Ascension");
        diasFestivos.put("2015-06-08", "Corpus Christi");
        diasFestivos.put("2015-06-15", "Sagrado Corazon");
        diasFestivos.put("2015-06-29", "San Pedro y San Pablo");
        diasFestivos.put("2015-07-20", "Dia de la Independencia");
        diasFestivos.put("2015-08-07", "Batalla de Boyaca");
        diasFestivos.put("2015-08-17", "La asuncion de la Virgen");
        diasFestivos.put("2015-10-12", "Dia de la Raza");
        diasFestivos.put("2015-11-02", "Todos los Santos");
        diasFestivos.put("2015-11-16", "Independencia de Cartagena");
        diasFestivos.put("2015-12-08", "Dia de la Inmaculada Concepcion");
        diasFestivos.put("2015-12-25", "Dia de Navidad");
        
        
        return diasFestivos;
    }
    
    
    /**
     * Metodo que construye una tabla Hash con los dias feriados de
     * Cali
     * 
     * La tabla Hash toma como key una fecha "2014-01-01" y el valor
     * es el nombre del dia feriado
     * 
     * @return diasFeriados
     */
    private Hashtable<String, String> buildDiasFeriados() {
        // Dias Festivos En Colombia
        Hashtable<String, String> diasFeriados = new Hashtable<>();
        
        // Feria de Cali Anio 2014
        diasFeriados.put("2014-12-25", "Inicio Feria de Cali");
        diasFeriados.put("2014-12-26", "Feria de Cali");
        diasFeriados.put("2014-12-27", "Feria de Cali");
        diasFeriados.put("2014-12-28", "Feria de Cali");
        diasFeriados.put("2014-12-29", "Feria de Cali");
        diasFeriados.put("2014-12-30", "Fin Feria de Cali");
        // Feria Petronio Alvarez 2014
        diasFeriados.put("2014-08-11", "Inicio Feria Petronio Alvarez");
        diasFeriados.put("2014-08-12", "Feria Petronio Alvarez");
        diasFeriados.put("2014-08-13", "Feria Petronio Alvarez");
        diasFeriados.put("2014-08-14", "Feria Petronio Alvarez");
        diasFeriados.put("2014-08-15", "Feria Petronio Alvarez");
        diasFeriados.put("2014-08-16", "Feria Petronio Alvarez");
        diasFeriados.put("2014-08-17", "Fin Feria Petronio Alvarez");
        // Feria de Cali Anio 2015
        diasFeriados.put("2015-12-25", "Inicio Feria de Cali");
        diasFeriados.put("2015-12-26", "Feria de Cali");
        diasFeriados.put("2015-12-27", "Feria de Cali");
        diasFeriados.put("2015-12-28", "Feria de Cali");
        diasFeriados.put("2015-12-29", "Feria de Cali");
        diasFeriados.put("2015-12-30", "Fin Feria de Cali");
        // Feria Petronio Alvarez 2015
        diasFeriados.put("2015-08-10", "Inicio Feria Petronio Alvarez");
        diasFeriados.put("2015-08-11", "Feria Petronio Alvarez");
        diasFeriados.put("2015-08-12", "Feria Petronio Alvarez");
        diasFeriados.put("2015-08-13", "Feria Petronio Alvarez");
        diasFeriados.put("2015-08-14", "Feria Petronio Alvarez");
        diasFeriados.put("2015-08-15", "Feria Petronio Alvarez");
        diasFeriados.put("2015-08-16", "Fin Feria Petronio Alvarez");
        
        
        return diasFeriados;
    }
    
    
    /**
     * Este metodo lee y construye la lista de Rutas y Estaciones
     * Construye la lista de INSERT INTO en dim_ruta_estacion.
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
                            dimRutaEstacion.add("INSERT INTO dim_ruta_estacion VALUES ('" + estacionOruta + "', " + true + ");");
                        }
                    } else { // Si no es una ruta es una Estacion
                        // Verifica que no se encuentre en la lista
                        if(!listEstaciones.contains(estacionOruta)) {
                            // Agrega la Estacion a la lista
                            listEstaciones.add(estacionOruta);
                            dimRutaEstacion.add("INSERT INTO dim_ruta_estacion VALUES ('" + estacionOruta + "', " + false + ");");
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
    
    
    /**
     * Metodo que imprime en la salida estandar del sistema los Insert de dim_ruta_estacion.
     */
    public void printDimRutaEstacion() {
        System.out.println("Dimension Ruta - Estacion");
        for (String next : dimRutaEstacion) {
            System.out.println(next);
        }
    }
    
    
    /**
     * Metodo que imprime en la salida estandar del sistema los Insert de dim_fecha.
     */
    public void printDimFecha() {
        System.out.println("Dimension Fecha");
        for (String next : dimFecha) {
            System.out.println(next);
        }
    }
    
}
