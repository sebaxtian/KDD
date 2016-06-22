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
import java.util.Map;
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
 * Esta clase representa al modelo de Extraccion en un sistema ETL
 * 
 * La extracion de los datos se hace tomando como fuente un
 * directorio de archivos en formato Excel.
 * 
 * @author sebaxtian
 * @version 0.1.0
 * @date mar may 24 00:04:55 COT 2016
 */


public class ETL {

    // Path del directorio de archivos Excel fuente
    private String pathDirFuente;
    // Path del directorio de archivos Excel ya Procesados
    private String pathDirProcesados;
    
    // Hashmap para las dimensiones
    //private HashMap<String, String> dim_fecha;
    //private HashMap<String, String> dim_tiempo;
    //private HashMap<String, String> dim_ruta_estacion;
    
    // Hashmap para tabla de hechos
    //private HashMap<String, String> tabla_frecuencias;
    
    
    // Variables temporales
    //private String tmpFecha;
    //private String tmpTiempo;
    
    
    // ID generado en cada insert de dimension
    private int id_fecha;
    private int id_tiempo;
    
    
    // Coneccion a Base de Datos
    private ConnectionDB connectionDB;
    
    
    // Numero maximo de horas en la dimension tiempo
    private int maxTiempo;
    
    
    /**
     * Metodo Contructor de clase
     * Recibe como argumento el path del directorio de archivos Excel fuente.
     * 
     * @param pathDirFuente 
     * @param pathDirProcesados 
     */
    public ETL(String pathDirFuente, String pathDirProcesados) {
        // Path del directorio de archivos Excel fuente
        this.pathDirFuente = pathDirFuente;
        // Path del directorio de archivos Excel ya Procesados
        this.pathDirProcesados = pathDirProcesados;
        // Hashmap para las dimensiones
        //this.dim_fecha = new HashMap<>();
        //this.dim_tiempo = new HashMap<>();
        //this.dim_ruta_estacion = new HashMap<>();
        
        // Hashmap para tabla de hechos
        //this.tabla_frecuencias = new HashMap<>();
        
        // Coneccion a Base de Datos
        this.connectionDB = new ConnectionDB(ConnectionDB.DBMSMYSQL, "localhost", 3306, "ETLMIO", "root", "sebaxtian");
        
        // Numero maximo de horas en la dimension tiempo
        this.maxTiempo = 0;
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
            for(int i = 0; i < numArchivosExcel; i++) {
                // Obtiene archivo Excel i
                File fileExcel = listExcel[i];
                // Verifica que sea un archivo
                if(fileExcel.isFile()) {
                    
                    System.out.println("Obtiene Archivo Excel: " + fileExcel.getName());
                    // Crea un Libro de trabajo por cada archivo Excel
                    Workbook libroExcel = WorkbookFactory.create(new FileInputStream(fileExcel.getAbsolutePath()));
                    System.out.println("Abre Libro Excel: " + fileExcel.getName());
                    
                    // Se extrae la dimension Fecha
                    this.extraerDimFecha(fileExcel.getName());
                    
                    // Itera por cada Hoja, Fila y Celda de un Libro Excel
                    for(Sheet hojaExcel : libroExcel ) {
                        
                        /*
                        // Si no se han cargado las 20 horas
                        if(maxTiempo < 20) {
                            // Se extrae la dimension Tiempo
                            this.extraerDimTiempo(hojaExcel.getSheetName());
                        }
                        */
                        // Se extrae la dimension Tiempo
                        this.extraerDimTiempo(hojaExcel.getSheetName());
                        
                        
                        for(Row fila : hojaExcel) {
                            
                            // Si es la primera Fila de la Hoja
                            if(fila.getRowNum() == 0) {
                                
                                // Se extrae la dimension Ruta-Estacion
                                this.extraerDimRutaEstacion(fila);
                                
                            }
                            // Si No es la ultima Fila de la Hoja
                            else if(hojaExcel.getLastRowNum() != fila.getRowNum()) {
                                // Opera los datos de cantidad de pasajeros
                                String estacionOruta = null;
                                int cantPasajeros = 0;
                                
                                // Primera celda de la fila
                                Cell celda1 = fila.getCell(fila.getFirstCellNum());
                                // Ultima ceda de la fila
                                Cell celdaN = fila.getCell(fila.getLastCellNum()-1);
                                
                                // Obtiene el Nombre de la Ruta-Estacion
                                if(celda1.getCellType() == Cell.CELL_TYPE_STRING) {
                                    //System.out.println("Primera Celda = " + celda1.getStringCellValue());
                                    estacionOruta = celda1.getStringCellValue();
                                }
                                
                                // Obtiene la Cantidad de Pasajeros en la Ruta-Estacion
                                if(celdaN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    //System.out.println("Ultima Celda = " + celdaN.getNumericCellValue());
                                    cantPasajeros = (int)celdaN.getNumericCellValue();
                                }
                                
                                // Se extrae la tabla de hechos Frecuencias
                                //this.extraerTablaFrecuencias(tmpFecha, tmpTiempo, estacionOruta, cantPasajeros);
                                // Select en dim_ruta_estacion del id_ruta_estacion
                                int id_ruta_estacion = connectionDB.selectIdRutaEstacion(estacionOruta);
                                if(id_ruta_estacion != -1 && id_ruta_estacion != 0) {
                                    if(id_fecha != -1 && id_fecha != 0) {
                                        if(id_tiempo != -1 && id_tiempo != 0) {
                                            this.extraerTablaFrecuencias(id_fecha, id_tiempo, id_ruta_estacion, cantPasajeros);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Cierra el stream
                    libroExcel.close();
                    // Libera Recursos
                    libroExcel = null;
                    System.gc();
                }
                // Mueve el archivo Excel que ya ha sido Procesado
                Files.move(FileSystems.getDefault().getPath(fileExcel.getAbsolutePath()), FileSystems.getDefault().getPath(this.pathDirProcesados+"/"+fileExcel.getName()), StandardCopyOption.REPLACE_EXISTING);
                // Libera Recursos
                fileExcel = null;
                System.gc();
                // Imprime cuantos archivos Excel faltan procesar
                System.out.println("Numero de Archivos Excel por Procesar: " + (numArchivosExcel--));
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
    private void extraerDimFecha(String nombreArchivo) {
        // Fecha con formato bruto
        String fechaBruta = nombreArchivo.substring(0, 6);
        
        // Actualiza variable temporal de fecha
        //tmpFecha = fechaBruta;
        
        try {
            // Valida que la llave no exista
            //if(!dim_fecha.containsKey(fechaBruta)) {
                // Fecha con formato limpio
                String fechaLimpia = nombreArchivo.substring(0, 2) + "-" + nombreArchivo.substring(2, 4) + "-" + nombreArchivo.substring(4, 6);
                
                Date fechaLibro = new SimpleDateFormat("yy-MM-dd").parse(fechaLimpia);
            
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
                
                // String SQL INSERT INTO
                String insertinto = "INSERT INTO dim_fecha "
                        + "(fecha_bruta, fecha_limpia, anio, mes, nombre_mes, "
                        + "semana_anio, semana_mes, dia_anio, dia, dia_semana_mes, "
                        + "nombre_dia, numero_dia_semana, es_festivo, nombre_festivo, "
                        + "es_feriado, nombre_feriado) "
                        + "VALUES ('" +
                        fechaBruta + "', '" +
                        strFechaLibro + "', " +
                        anio + ", " +
                        mes + ", '" +
                        nombreMes + "', " +
                        semanaAnio + ", " +
                        semanaMes + ", " +
                        diaAnio + ", " +
                        dia + ", " +
                        diaSemanaMes + ", '" +
                        nombreDia + "', " +
                        numDiaSemana + ", " +
                        esFestivo + ", '" +
                        nombreFestivo + "', " +
                        esFeriado + ", '" +
                        nombreFeriado +
                        "');";
                
                // Agrega un nuevo dato al HashMap
                //dim_fecha.put(fechaBruta, insertinto);
                
                // Inserta el Registro en la Tabla dim_fecha
                id_fecha = connectionDB.insertDimFecha(fechaBruta, insertinto);
            //}
            
            
        } catch (ParseException ex) {
            System.err.println("Error al transformar nombre de libro en Formato Fecha");
            ex.printStackTrace();
        }
    }
    
    
    
    /**
     * Este metodo construye el HashMap de la Dimension Tiempo.
     * 
     * La llave del HashMap corresponde a valor en bruto del tiempo.
     * El valor del HashMap corresponde a un String SQL INSERT INTO.
     * 
     * @param nombreHoja 
     */
    private void extraerDimTiempo(String nombreHoja) {
        //System.out.println("Hoja del Libro: " + nombreHoja);
        String tiempoBruto = nombreHoja.substring(4, nombreHoja.length());
        
        // Actualiza variable temporal de tiempo
        //tmpTiempo = tiempoBruto;
        
        // Valida que la llave no exista
        //if(!dim_tiempo.containsKey(tiempoBruto)) {
            // Obtiene las horas del tiempo bruto
            StringTokenizer strToken = new StringTokenizer(tiempoBruto, "-");
            int horaInicio = Integer.parseInt(strToken.nextToken());
            int horaFin = Integer.parseInt(strToken.nextToken());
            //System.out.println(horaFin + " - " + horaInicio + " = " + (horaFin-horaInicio));
            // Tiempo entre horas
            int deltaTiempo = (horaFin - horaInicio);
            // Valida que la diferencia de tiempo sea 1 hora
            if(deltaTiempo == 1) {
                // String SQL INSERT INTO
                String insertinto = "INSERT INTO dim_tiempo (tiempo_bruto, "
                        + "hora_inicio, hora_fin) "
                        + "VALUES ('" 
                        + tiempoBruto + "', " 
                        + horaInicio + ", " 
                        + horaFin + ");";
                
                // Agrega un nuevo dato al HashMap
                //dim_tiempo.put(tiempoBruto, insertinto);
                
                // Inserta el Registro en la Tabla dim_tiempo
                id_tiempo = connectionDB.insertDimTiempo(tiempoBruto, insertinto);
                
                // Numero maximo de horas en la dimension tiempo
                this.maxTiempo++;
            }
        //}
        
    }
    
    
    
    /**
     * Este metodo construye el HashMap de la Dimension Ruta-Estacion.
     * 
     * La llave del HashMap corresponde a valor en bruto de la ruta-estacion.
     * El valor del HashMap corresponde a un String SQL INSERT INTO.
     * 
     * @param fila 
     */
    private void extraerDimRutaEstacion(Row fila) {
        for(Cell celda : fila) {
            // Los nombre de Estacion y Ruta son de tipo string
            if(celda.getCellType() == Cell.CELL_TYPE_STRING) {
                String estacionOruta = celda.getRichStringCellValue().getString();
                // Se omiten valores
                if(!estacionOruta.equals("ORIGEN \\ DESTINO") && !estacionOruta.equals("TOTAL") && !estacionOruta.equals("SIN DATO") && !estacionOruta.equals("SIN HORARIO")) {
                    // Valida que la llave no exista
                    //if(!dim_ruta_estacion.containsKey(estacionOruta)) {
                        // Verifica que sea una Ruta
                        Pattern regex = Pattern.compile("[A-Z][0-9]");
                        Matcher matcher = regex.matcher(estacionOruta);
                        // Si es una Ruta
                        if(matcher.find()) {
                            // String SQL INSERT INTO
                            String insertinto = "INSERT INTO dim_ruta_estacion (nombre_ruta_estacion, es_ruta) VALUES ('" + estacionOruta + "', " + true + ");";
                            // Agrega un nuevo dato al HashMap
                            //dim_ruta_estacion.put(estacionOruta, insertinto);
                            
                            // Inserta el Registro en la Tabla dim_ruta_estacion
                            connectionDB.insertDimRutaEstacion(estacionOruta, insertinto);
                        } else { // Si no es una ruta es una Estacion
                            // String SQL INSERT INTO
                            String insertinto = "INSERT INTO dim_ruta_estacion (nombre_ruta_estacion, es_ruta) VALUES ('" + estacionOruta + "', " + false + ");";
                            // Agrega un nuevo dato al HashMap
                            //dim_ruta_estacion.put(estacionOruta, insertinto);
                            
                            // Inserta el Registro en la Tabla dim_ruta_estacion
                            connectionDB.insertDimRutaEstacion(estacionOruta, insertinto);
                        }
                    //}
                }
            }
        }
    }
    
    
    
    /**
     * Este metodo construye el HashMap de la Tabla de Hechos Frecuencias.
     * 
     * La llave del HashMap corresponde a una llave compuesta por las llaves
     * de fecha, franjaHoraria, estacionOruta.
     * 
     * El valor del HashMap corresponde a un String SQL INSERT INTO.
     * 
     * Ejemplo:
     * 
     * [150509&13-14&A06_2] -> "INSERT INTO frecuencias VALUES (fecha, franjaHoraria, estacionOruta, cantPasajeros);"
     * 
     * @param fecha
     * @param franjaHoraria
     * @param estacionOruta
     * @param cantPasajeros 
     */
    private void extraerTablaFrecuencias(String fecha, String franjaHoraria, String estacionOruta, int cantPasajeros) {
        String llave = fecha + "&" + franjaHoraria + "&" + estacionOruta;
        // Valida que la llave no exista
        //if(!tabla_frecuencias.containsKey(llave)) {
            // String SQL INSERT INTO
            String insertinto = "INSERT INTO frecuencias VALUES (" + fecha + ", " + franjaHoraria + ", " + estacionOruta + ", " + cantPasajeros + ");";
            // Agrega un nuevo dato al HashMap
            //tabla_frecuencias.put(llave, insertinto);
            
            // Select en dim_ruta_estacion del id_ruta_estacion
            int id_ruta_estacion = connectionDB.selectIdRutaEstacion(estacionOruta);
            if(id_ruta_estacion != -1 && id_ruta_estacion != 0) {
                if(id_fecha != -1 && id_fecha != 0) {
                    if(id_tiempo != -1 && id_tiempo != 0) {
                        this.extraerTablaFrecuencias(id_fecha, id_tiempo, id_ruta_estacion, cantPasajeros);
                    }
                }
            }
        //}
    }
    
    
    
    
    /**
     * Este metodo construye el HashMap de la Tabla de Hechos Frecuencias.
     * 
     * La llave del HashMap corresponde a una llave compuesta por las llaves
     * de fecha, franjaHoraria, estacionOruta.
     * 
     * El valor del HashMap corresponde a un String SQL INSERT INTO.
     * 
     * Ejemplo:
     * 
     * [id_fecha&id_tiempo&id_ruta_estacion] -> "INSERT INTO frecuencias VALUES (fecha, franjaHoraria, estacionOruta, cantPasajeros);"
     * 
     * @param fecha
     * @param franjaHoraria
     * @param estacionOruta
     * @param cantPasajeros 
     */
    private void extraerTablaFrecuencias(int id_fecha, int id_tiempo, int id_ruta_estacion, int cantPasajeros) {
        // String SQL INSERT INTO
        String insertinto = "INSERT INTO frecuencias (fk_fecha, fk_tiempo, fk_ruta_estacion, cant_pasajeros) "
                + "VALUES (" + id_fecha + ", " + id_tiempo + ", " + id_ruta_estacion + ", " + cantPasajeros + ");";
        // Inserta el Registro en la Tabla dim_ruta_estacion
        connectionDB.insertTablaFrecuencias(insertinto);
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
    private HashMap<String, String> buildDiasFestivos() {
        // Dias Festivos En Colombia
        HashMap<String, String> diasFestivos = new HashMap<>();
        
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
    private HashMap<String, String> buildDiasFeriados() {
        // Dias Festivos En Colombia
        HashMap<String, String> diasFeriados = new HashMap<>();
        
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
     * Este Metodo imprime en la salida estandar del sistema el
     * HashMap de la Dimension Fecha [key] -> value.
     */
    public void printHashMapDimFecha() {
        System.out.println("HashMap Dimension Fecha");
        /*
        for (Map.Entry<String, String> entry : dim_fecha.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("[" + key + "] -> " + value);
        }
        */
        System.out.println("");
    }
    
    
    
    /**
     * Este Metodo imprime en la salida estandar del sistema el
     * HashMap de la Dimension Tiempo [key] -> value.
     */
    public void printHashMapDimTiempo() {
        System.out.println("HashMap Dimension Tiempo");
        /*
        for (Map.Entry<String, String> entry : dim_tiempo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("[" + key + "] -> " + value);
        }
        */
        System.out.println("");
    }
    
    
    
    /**
     * Este Metodo imprime en la salida estandar del sistema el
     * HashMap de la Dimension Ruta-Estacion [key] -> value.
     */
    public void printHashMapDimRutaEstacion() {
        System.out.println("HashMap Dimension Ruta-Estacion");
        /*
        for (Map.Entry<String, String> entry : dim_ruta_estacion.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("[" + key + "] -> " + value);
        }
        */
        System.out.println("");
    }
    
    
    
    /**
     * Este Metodo imprime en la salida estandar del sistema el
     * HashMap de la Tabla de Hechos Frecuencias [key] -> value.
     */
    public void printHashMapTablaFrecuencias() {
        System.out.println("HashMap Tabla Frecuencias");
        /*
        for (Map.Entry<String, String> entry : tabla_frecuencias.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("[" + key + "] -> " + value);
        }
        */
        System.out.println("");
    }
    
    
}
