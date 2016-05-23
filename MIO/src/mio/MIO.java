/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * Franjas horarias donde mas hay movimiento de pasajerso
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
     * @return Workbook
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            
            // Pruebas con Libreria POI
            testNewWorkbook("workbook.xlsx");
            testNewSheet("PruebaNuevaHoja");
            testNewCell();
            iteratorRowsCells();
            
        } catch (IOException ex) {
            System.err.println("Error al crear libro de trabajo en archivo Excel");
        } catch (InvalidFormatException ex) {
            System.err.println("Error al abrir libro de trabajo en archivo Excel");
        }
        
    }
    
}
