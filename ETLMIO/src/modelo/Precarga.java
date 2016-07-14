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
 * Esta clase permite realizar el proceso de Precarga de las dimensiones
 * Fecha, Tiempo, Ruta Estacion con los datos extraidos desde archivos
 * en formato Excel que son leidos de un directorio de archivos fuentes,
 * una vez se han extraido los datos, cada archivo Excel es movido al
 * directorio de archivo procesados.
 * 
 * @author sebaxtian
 * @date jue jun 23 20:00:54 COT 2016
 * @version 0.1.0
 */


public class Precarga {
    
    
    // Path del directorio de archivos Excel fuente
    private String pathDirFuente;
    // Path del directorio de archivos Excel ya Procesados
    private String pathDirProcesados;
    
    // Coneccion a Base de Datos
    private ConnectionDB connectionDB;
    
    
    
    /**
     * Metodo Contructor de Clase
     * 
     * Recibe como argumento el path del directorio de archivos Excel fuente y
     * el path del directorio de archivos Excel que terminan de ser procesados.
     * 
     * @param pathDirFuente 
     * @param pathDirProcesados 
     */
    public Precarga(String pathDirFuente, String pathDirProcesados) {
        // Path del directorio de archivos Excel fuente
        this.pathDirFuente = pathDirFuente;
        // Path del directorio de archivos Excel ya Procesados
        this.pathDirProcesados = pathDirProcesados;
        
        // Coneccion a Base de Datos
        this.connectionDB = new ConnectionDB(ConnectionDB.DBMSMYSQL, "localhost", 3306, "ETLMIO", "root", "sebaxtian");
    }
    
    
    
    /**
     * Este metodo se encarga de realizar el proceso de precarga para las
     * dimensiones Fecha, Tiempo, Ruta Estacion.
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
                    
                    // Se extrae la dimension Fecha
                    this.extraerDimFecha(fileExcel.getName());
                    
                    // Itera por cada Hoja, Fila y Celda de un Libro Excel
                    for(Sheet hojaExcel : libroExcel ) {
                        
                        // Se extrae la dimension Tiempo
                        this.extraerDimTiempo(hojaExcel.getSheetName());
                        
                        Row fila = hojaExcel.getRow(0);
                        
                        // Se extrae la dimension Ruta-Estacion
                        this.extraerDimRutaEstacion(fila);
                        
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
     * Este metodo extrae la dimension Fecha apartir del nombre de archivo Excel.
     * 
     * Obtiene la fecha bruta del nombre de archivo y construye un String SQL
     * para realizar un Insert en la tabla de dimension Fecha.
     * 
     * @param nombreArchivo 
     */
    private void extraerDimFecha(String nombreArchivo) {
        // Fecha bruta extraida del nombre del archivo Excel
        String fechaBruta = nombreArchivo.substring(0, 6);
        
        try {
            // Fecha con formato limpio
            String fechaLimpia = nombreArchivo.substring(0, 2) + "-" + nombreArchivo.substring(2, 4) + "-" + nombreArchivo.substring(4, 6);

            Date fechaLibro = new SimpleDateFormat("yy-MM-dd").parse(fechaLimpia);

            // Obtiene informacion de la fecha del libro
            SimpleDateFormat dateFormatter = new SimpleDateFormat();

            // Fecha Fuente
            dateFormatter.applyPattern("yyyy-MM-dd");
            String strFechaLibro = dateFormatter.format(fechaLibro);

            // Anio
            dateFormatter.applyPattern("yyyy");
            String anio = dateFormatter.format(fechaLibro);

            // Mes del anio
            dateFormatter.applyPattern("MM");
            String mes = dateFormatter.format(fechaLibro);

            // Nombre del mes
            dateFormatter.applyPattern("MMMM");
            String nombreMes = dateFormatter.format(fechaLibro);

            // Semana en el anio
            dateFormatter.applyPattern("w");
            String semanaAnio = dateFormatter.format(fechaLibro);

            // Semana en el mes
            dateFormatter.applyPattern("W");
            String semanaMes = dateFormatter.format(fechaLibro);

            // Dia en el anio
            dateFormatter.applyPattern("D");
            String diaAnio = dateFormatter.format(fechaLibro);

            // Dia en el mes
            dateFormatter.applyPattern("dd");
            String dia = dateFormatter.format(fechaLibro);

            // Dia de la semana en el mes
            dateFormatter.applyPattern("F");
            String diaSemanaMes = dateFormatter.format(fechaLibro);

            // Nombre del dia en la semana
            dateFormatter.applyPattern("EEEEE");
            String nombreDia = dateFormatter.format(fechaLibro);

            // Numero de dia de la semana
            dateFormatter.applyPattern("u");
            String numDiaSemana = dateFormatter.format(fechaLibro);

            // Nombre del dia festivo
            dateFormatter.applyPattern("yyyy-MM-dd");
            String nombreFestivo = buildDiasFestivos().get(dateFormatter.format(fechaLibro));
            boolean esFestivo = true;
            if(nombreFestivo == null) {
                nombreFestivo = "";
                esFestivo = false;
            }

            // Nombre del dia feriado
            dateFormatter.applyPattern("yyyy-MM-dd");
            String nombreFeriado = buildDiasFeriados().get(dateFormatter.format(fechaLibro));
            boolean esFeriado = true;
            if(nombreFeriado == null) {
                nombreFeriado = "";
                esFeriado = false;
            }

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

            // Inserta el Registro en la Tabla dim_fecha
            connectionDB.insertDimFecha(fechaBruta, insertinto);
            
        } catch (ParseException ex) {
            System.err.println("Error al transformar nombre de libro en Formato Fecha " + ex.getMessage());
        }
    }
    
    
    
    /**
     * Este metodo extrae la dimension Tiempo apartir del nombre de una hoja Excel.
     * 
     * Obtiene el tiempo bruto del nombre de la hoja y construye un String SQL
     * para realizar un Insert en la tabla de dimension Tiempo.
     * 
     * @param nombreHoja 
     */
    private void extraerDimTiempo(String nombreHoja) {
        // Tiempo bruto extraido del nombre de la hoja Excel
        String tiempoBruto = nombreHoja.substring(4, nombreHoja.length());
        
        // Obtiene las horas del tiempo bruto
        StringTokenizer strToken = new StringTokenizer(tiempoBruto, "-");
        int horaInicio = Integer.parseInt(strToken.nextToken());
        int horaFin = Integer.parseInt(strToken.nextToken());
        // String SQL INSERT INTO
        String insertinto = "INSERT INTO dim_tiempo (tiempo_bruto, "
                + "hora_inicio, hora_fin) "
                + "VALUES ('" 
                + tiempoBruto + "', " 
                + horaInicio + ", " 
                + horaFin + ");";

        // Inserta el Registro en la Tabla dim_tiempo
        connectionDB.insertDimTiempo(tiempoBruto, insertinto);
    }
    
    
    
    /**
     * Este metodo extrae la dimension Ruta Estacion apartir de los valores
     * de una fila de una hoja Excel.
     * 
     * Obtiene los nombres de rutas o estaciones y construye un String SQL
     * para realizar un Insert en la tabla de dimension Ruta Estacion.
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
                    // Verifica que sea una Ruta
                    Pattern regex = Pattern.compile("[A-Z][0-9]");
                    Matcher matcher = regex.matcher(estacionOruta);
                    String insertinto;
                    // Si es una Ruta
                    if(matcher.find()) {
                        // String SQL INSERT INTO
                        insertinto = "INSERT INTO dim_ruta_estacion (nombre_ruta_estacion, es_ruta) VALUES ('" + estacionOruta + "', " + true + ");";
                        // Inserta el Registro en la Tabla dim_ruta_estacion
                        connectionDB.insertDimRutaEstacion(estacionOruta, insertinto);
                    } else { // Si no es una ruta es una Estacion
                        // String SQL INSERT INTO
                        insertinto = "INSERT INTO dim_ruta_estacion (nombre_ruta_estacion, es_ruta) VALUES ('" + estacionOruta + "', " + false + ");";    
                    }
                    // Inserta el Registro en la Tabla dim_ruta_estacion
                    connectionDB.insertDimRutaEstacion(estacionOruta, insertinto);
                }
            }
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
    
    
}
