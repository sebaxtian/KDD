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
        gui.selectEstaciones.setSelectedIndex(1);
    }
    
    
    public static void log(String mensaje) {
        gui.lblMensajeLog.setText(" Log: " + mensaje);
    }
    
}
