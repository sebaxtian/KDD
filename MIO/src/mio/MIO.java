/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mio;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import modelo.ETL;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static Connection.ConnectionSQL.conexBD;

/**
 * Esta es la clase main del proyecto MIO,
 * 
 * Mini-proyecto para el curso de KDD,
 * diseño de una bodega de datos que permite
 * responder las siguientes preguntas:
 * 
 * Cantidad de pasajeros movilizados por el sistema en franjas
 * horarias y fechas especificas
 * 
 * Estaciones donde hay mas demanda de pasajeros
 * 
 * Rutas que mas pasajeros mueven
 * 
 * Franjas horarias donde hay mas movimiento de pasajerso
 * 
 * Comparacion de la demanda en dias laborales, fines de
 * semana y dias festivos.
 * 
 * @author sebaxtian
 * @version 0.1.0
 * @date lun may 23 09:35:42 COT 2016
 */


public class MIO {

    
    
    /**
     * Prueba de crear un libro de trabajo en archivo Excel
     * @param nameFile
     * @throws java.io.IOException
     */
    public static void testNewWorkbook(String nameFile) throws IOException {
        Workbook wb = new XSSFWorkbook();
        FileOutputStream fileOut = new FileOutputStream(nameFile);
        wb.write(fileOut);
    }
    
    
    /**
     * Prueba de crear hojas sobre un libro de trabajo Excel
     * @param nameSheet
     * @throws java.io.IOException
     */
    public static void testNewSheet(String nameSheet) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(nameSheet);

        // Note that sheet name is Excel must not exceed 31 characters
        // and must not contain any of the any of the following characters:
        // 0x0000
        // 0x0003
        // colon (:)
        // backslash (\)
        // asterisk (*)
        // question mark (?)
        // forward slash (/)
        // opening square bracket ([)
        // closing square bracket (])

        // You can use org.apache.poi.ss.util.WorkbookUtil#createSafeSheetName(String nameProposal)}
        // for a safe way to create valid names, this utility replaces invalid characters with a space (' ')
        // String safeName = WorkbookUtil.createSafeSheetName("[O'Brien's sales*?]"); // returns " O'Brien's sales   "
        // Sheet sheet3 = wb.createSheet(safeName);

        FileOutputStream fileOut = new FileOutputStream("workbook.xlsx");
        wb.write(fileOut);
        fileOut.close();
    }
    
    
    /**
     * Prueba de crear celdas sobre hojas de libro Excel
     * @throws java.io.IOException
     */
    public static void testNewCell() throws IOException {
        //Workbook wb = new HSSFWorkbook();
        Workbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("HojaNueva");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow((short)0);
        // Create a cell and put a value in it.
        Cell cell = row.createCell(0);
        cell.setCellValue(1);

        // Or do it on one line.
        row.createCell(1).setCellValue(1.2);
        row.createCell(2).setCellValue(
             createHelper.createRichTextString("Esto es un String"));
        row.createCell(3).setCellValue(true);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xlsx");
        wb.write(fileOut);
        fileOut.close();
    }
    
    
    /**
     * Prueba de Iteracion por cada Fila y Celda en cada Hoja de un Libro Excel.
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static void iteratorRowsCells() throws IOException, InvalidFormatException {
        // These iterators are available by calling workbook.sheetIterator(),
        // sheet.rowIterator(), and row.cellIterator(), or implicitly using a for-each loop.
        // Note that a rowIterator and cellIterator iterate over rows or cells that have been created,
        // skipping empty rows and cells.
        
        // Use a file
        //Workbook wb = WorkbookFactory.create(new File("MyExcel.xls"));

        // Use an InputStream, needs more memory
        Workbook wb = WorkbookFactory.create(new FileInputStream("workbook.xlsx"));
        
        // Itera por cada hoja, fila y celda de un libro Excel
        for(Sheet sheet : wb ) {
            for(Row row : sheet) {
                for(Cell cell : row) {
                    // Do something here
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                    System.out.print(cellRef.formatAsString());
                    System.out.print(" - ");

                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            System.out.println(cell.getRichStringCellValue().getString());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                System.out.println(cell.getDateCellValue());
                            } else {
                                System.out.println(cell.getNumericCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            System.out.println(cell.getCellFormula());
                            break;
                        default:
                            System.out.println();
                    }
                }
            }
        }        
    }
    
    
    /**
     * Prueba de leer directorio de Matrices de archivos Excel.
     * @param pathDir
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static void testReadMatrix(String pathDir) throws IOException, InvalidFormatException {
        // Directorio de archivos Excel
        File dirMatrix = new File(pathDir);
        // Verifica que sea un directorio
        if(dirMatrix.isDirectory()) {
            // Itera por cada archivo Excel del directorio
            File[] listExcel = dirMatrix.listFiles();
            for(int i = 0; i < listExcel.length; i++) {
                File fileExcel = listExcel[i];
                // Verifica que sea un archivo
                if(fileExcel.isFile()) {
                    // Crea un libro de trabajo por cada archivo Excel
                    // BUILD SUCCESSFUL (total time: 5 minutes 42 seconds)
                    Workbook libroExcel = WorkbookFactory.create(new FileInputStream(fileExcel.getAbsolutePath()));
                    //BUILD SUCCESSFUL (total time: 5 minutes 38 seconds)
                    //Workbook libroExcel = WorkbookFactory.create(fileExcel);
                    System.out.println("Abre Libro Excel: " + fileExcel.getName());
                    // Itera por cada hoja, fila y celda de un libro Excel
                    for(Sheet hojaExcel : libroExcel ) {
                        System.out.println("Abre Hoja: " + hojaExcel.getSheetName());
                        for(Row fila : hojaExcel) {
                            System.out.println("Lee Fila: " + fila.getRowNum());
                            for(Cell celda : fila) {
                                switch (celda.getCellType()) {
                                    case Cell.CELL_TYPE_STRING:
                                        System.out.println("Lee Celda: " + celda.getRichStringCellValue().getString());
                                        break;
                                    case Cell.CELL_TYPE_NUMERIC:
                                        if (DateUtil.isCellDateFormatted(celda)) {
                                            System.out.println("Lee Celda: " + celda.getDateCellValue());
                                        } else {
                                            System.out.println("Lee Celda: " + celda.getNumericCellValue());
                                        }
                                        break;
                                    case Cell.CELL_TYPE_BOOLEAN:
                                        System.out.println("Lee Celda: " + celda.getBooleanCellValue());
                                        break;
                                    case Cell.CELL_TYPE_FORMULA:
                                        System.out.println("Lee Celda: " + celda.getCellFormula());
                                        break;
                                    default:
                                        System.out.println();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    public static void testEstacionRuta(String pathDir) throws IOException, InvalidFormatException {
        // Lista donde se guardan las estaciones y las rutas
        ArrayList<String> listEstacionRuta = new ArrayList<>();
        // Lista donde se guardan las rutas
        ArrayList<String> listRutas = new ArrayList<>();
        // Lista donde se guardan las estaciones
        ArrayList<String> listEstaciones = new ArrayList<>();
        // Directorio de archivos Excel
        File dirMatrix = new File(pathDir);
        // Verifica que sea un directorio
        if(dirMatrix.isDirectory()) {
            // Itera por cada archivo Excel del directorio
            File[] listExcel = dirMatrix.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String nombreArchivo = pathname.getName();
                    if(nombreArchivo.substring(nombreArchivo.length()-4, nombreArchivo.length()).equals("xlsx")) {
                        return true;
                    }
                    return false;
                }
            });
            for(int i = 0; i < listExcel.length; i++) {
                File fileExcel = listExcel[i];
                // Verifica que sea un archivo
                if(fileExcel.isFile()) {
                    System.out.println("Obtiene Archivo: " + fileExcel.getName());
                    // Crea un libro de trabajo por cada archivo Excel
                    // BUILD SUCCESSFUL (total time: 5 minutes 42 seconds)
                    Workbook libroExcel = WorkbookFactory.create(new FileInputStream(fileExcel.getAbsolutePath()));
                    //BUILD SUCCESSFUL (total time: 5 minutes 38 seconds)
                    //Workbook libroExcel = WorkbookFactory.create(fileExcel);
                    System.out.println("Abre Libro Excel: " + fileExcel.getName());
                    // Itera por cada hoja, fila y celda de un libro Excel
                    for(Sheet hojaExcel : libroExcel ) {
                        //System.out.println("Abre Hoja: " + hojaExcel.getSheetName());
                        // Obtiene la fila 0 donde se muestran los nombre de rutas y estaciones
                        Row fila0 = hojaExcel.getRow(0);
                        //System.out.println("Lee Fila: " + fila0.getRowNum());
                        for(Cell celda : fila0) {
                            // Los nombre de estacion y ruta son de tipo string
                            if(celda.getCellType() == Cell.CELL_TYPE_STRING) {
                                String estacionOruta = celda.getRichStringCellValue().getString();
                                //System.out.println("estacionOruta = " + estacionOruta);
                                // Se omiten valores
                                if(!estacionOruta.equals("ORIGEN \\ DESTINO") 
                                        && !estacionOruta.equals("TOTAL") 
                                        && !estacionOruta.equals("SIN DATO") 
                                        && !estacionOruta.equals("SIN HORARIO")) {
                                    // Verifica que en la lista no se encuentre estacion o ruta
                                    if(!listEstacionRuta.contains(estacionOruta)) {
                                        listEstacionRuta.add(estacionOruta);
                                        // Verifica que sea una Ruta
                                        Pattern regex = Pattern.compile("[A-Z][0-9]");
                                        Matcher matcher = regex.matcher(estacionOruta);
                                        if(matcher.find()) {
                                            listRutas.add(estacionOruta);
                                        } else {
                                            // Si no es una ruta es una Estacion
                                            listEstaciones.add(estacionOruta);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Cierra el stream
                    libroExcel.close();
                }
            }
        }
        // Imprime la lista de estaciones y rutas
        System.out.println("Lista de Estaciones y Rutas");
        for (String next : listEstacionRuta) {
            System.out.println(next);
        }
        System.out.println("Lista de Rutas");
        for (String next : listRutas) {
            System.out.println(next);
        }
        System.out.println("Lista de Estaciones");
        for (String next : listEstaciones) {
            System.out.println(next);
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        String servidor=null,usuario=null,password=null;
        if(args.length>2)
        {servidor=args[0];usuario=args[1];password=args[2];}

        conexBD.connect(servidor,usuario,password);
        try {
            // Pruebas con Libreria POI
            //testNewWorkbook("workbook.xlsx");
            //testNewSheet("PruebaNuevaHoja");
            //testNewCell();
            //iteratorRowsCells();
            //testReadMatrix("/home/sebaxtian/Dropbox/Cloud/Sebaxtian/Documentos/Universia/2016-I/KDD/MatricesSimplificadas");
            //testEstacionRuta("/home/sebaxtian/Dropbox/Cloud/Sebaxtian/Documentos/Universia/2016-I/KDD/MatricesSimplificadas");
            
            //DIRECTORIO SEBASTIAN
            //String pathDirFuente = "/home/sebaxtian/Dropbox/Cloud/Sebaxtian/Documentos/Universia/2016-I/KDD/MatricesSimplificadas";
            
            //DIRECTORIO RYAN
            String pathDirFuente = "C:\\Users\\Shalóm\\Documents\\NetBeansProjects\\KDD\\BD_Excell";
            ETL E = new ETL(pathDirFuente);
            E.execute();
            E.cargarFrecuencias();//
            //E.printRutas();
            //E.printEstaciones();
            //E.printDimRutaEstacion();
            //E.printDimFecha();
            
        } catch (IOException ex) {
            System.err.println("Error al crear libro de trabajo en archivo Excel");
            ex.printStackTrace();
        } catch (InvalidFormatException ex) {
            System.err.println("Error al abrir libro de trabajo en archivo Excel ");
            ex.printStackTrace();
        }        
    }
    
}
