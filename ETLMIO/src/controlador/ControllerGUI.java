/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import gui.GUI;
import java.util.List;
import javax.swing.JComponent;
import modelo.ConnectionDB;
import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;

/**
 *
 * @author sebaxtian
 */


public class ControllerGUI {
    
    
    private static ConnectionDB connectionDB;
    private static String DBMS;
    private static String host;
    private static String dbname;
    private static String user;
    private static String pwd;
    private static int port;
    private static JDatePicker selectFecha1, selectFecha2;
    public static boolean visibleGuiEstaciones = false;
    public static boolean visibleGuiRutas = false;
    public static GUI gui;
    
    
    
    
    public static void loadSelectFecha() {
        selectFecha1 = new JDateComponentFactory().createJDatePicker();
        selectFecha1.setTextEditable(false);
        selectFecha1.setShowYearButtons(false);
        gui.panelFecha1.add((JComponent) selectFecha1);
        
        selectFecha2 = new JDateComponentFactory().createJDatePicker();
        selectFecha2.setTextEditable(false);
        selectFecha2.setShowYearButtons(false);
        gui.panelFecha2.add((JComponent) selectFecha2);
    }
    
    
    
    public static void guardarAttrDB(String DBMS, String host, int port, String dbname, String user, String pwd) {
        ControllerGUI.DBMS = DBMS;
        ControllerGUI.host = host;
        ControllerGUI.port = port;
        ControllerGUI.dbname = dbname;
        ControllerGUI.user = user;
        ControllerGUI.pwd = pwd;
        
        ControllerGUI.gui.menuConectar.setEnabled(true);
    }
    
    
    
    public static void conectarDB() {
        connectionDB = new ConnectionDB(DBMS, host, port, dbname, user, pwd);
    }
    
    
    
    public static void setSelectEstaciones(List<String> estaciones) {
        gui.selectEstaciones.removeAllItems();
        gui.selectEstaciones.addItem("Selecionar");
        for (String estacion : estaciones) {
            gui.selectEstaciones.addItem(estacion);
        }
        gui.selectEstaciones.setSelectedIndex(0);
    }
    
    
    
    public static void setSelectRutas(List<String> rutas) {
        gui.selectRutas.removeAllItems();
        gui.selectRutas.addItem("Selecionar");
        for (String ruta : rutas) {
            gui.selectRutas.addItem(ruta);
        }
        gui.selectRutas.setSelectedIndex(0);
    }
    
    
    
    public static void loadDescReporte() {
        if(gui.radioReporte1.isSelected()) {
            //log("Descripcion de Reporte1");
            gui.textAreaReporte.setText("Cantidad de pasajeros movilizados por el sistema en franjas horarias y fechas específicas.");
        }
        if(gui.radioReporte2.isSelected()) {
            //log("Descripcion de Reporte2");
            gui.textAreaReporte.setText("Estaciones donde hay más demanda de pasajeros.");
        }
        if(gui.radioReporte3.isSelected()) {
            //log("Descripcion de Reporte3");
            gui.textAreaReporte.setText("Rutas que más pasajeros mueven.");
        }
        if(gui.radioReporte4.isSelected()) {
            //log("Descripcion de Reporte4");
            gui.textAreaReporte.setText("Franjas horarias donde más hay movimiento de pasajeros.");
        }
        if(gui.radioReporte5.isSelected()) {
            //log("Descripcion de Reporte5");
            gui.textAreaReporte.setText("Comparación de la demanda en días laborables, fines de semana y días festivos.");
        }
    }
    
    
    
    public static void log(String mensaje) {
        if(gui != null) {
            gui.lblMensajeLog.setText(" Log: " + mensaje);
        }
    }
    
}
