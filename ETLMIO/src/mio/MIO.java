/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mio;


import gui.GUI;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import modelo.Carga;
import modelo.ETL;
import modelo.Precarga;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

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
     * Este metodo inicia el proceso de los datos mediante
     * la herramienta ETL.
     */
    private static void procesarETL() {
        try {
            
            Date fechaInicio = new Date();
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat();
            
            dateFormatter.applyPattern("MM/dd/yyyy");
            String fecha = dateFormatter.format(fechaInicio);
            dateFormatter.applyPattern("HH:mm:ss");
            String hora = dateFormatter.format(fechaInicio);
            
            System.out.println("Inicia Proceso ETL " + fecha + " " + hora);
            
            
            String pathDirFuente = "/home/sebaxtian/Descargas/MatricesSimplificadas";
            String pathDirProcesados = "/home/sebaxtian/Descargas/MatricesProcesadas";
            
            /*
            Precarga precarga = new Precarga(pathDirFuente, pathDirProcesados);
            // Ejecuta
            precarga.execute();
            */
            Carga carga = new Carga(pathDirFuente, pathDirProcesados);
            // Ejecuta
            carga.execute();
            /*
            ETL etl = new ETL(pathDirFuente, pathDirProcesados);
            // Ejecuta
            etl.execute();
            */
            
            Date fechaFin = new Date();
            
            dateFormatter.applyPattern("MM/dd/yyyy");
            fecha = dateFormatter.format(fechaFin);
            dateFormatter.applyPattern("HH:mm:ss");
            hora = dateFormatter.format(fechaFin);
            
            System.out.println("Termina Proceso ETL " + fecha + " " + hora);
            
            
        } catch (IOException ex) {
            System.err.println("Error al crear libro de trabajo en archivo Excel " + ex.getMessage());
        } catch (InvalidFormatException ex) {
            System.err.println("Error al abrir libro de trabajo en archivo Excel " + ex.getMessage());
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        // Procesar datos ETL
        procesarETL();
        
        // Muestra la interfaz grafica para generar reportes
        //GUI.main(args);
        
    }
    
}
