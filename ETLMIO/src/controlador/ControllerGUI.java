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
    
    
    
    
    private static String[] loadRangoFecha() {
        String[] rangoFecha = {"N", "N"};
        
        // 140515
        
        int anio = selectFecha1.getModel().getYear();
        int mes = selectFecha1.getModel().getMonth() + 1;
        int dia = selectFecha1.getModel().getDay();
        
        String fecha1 = anio + "";
        fecha1 = fecha1.substring(2);
        if(mes < 10) {
            fecha1 += "0" + mes;
        } else {
            fecha1 += mes;
        }
        if(dia < 10) {
            fecha1 += "0" + dia;
        } else {
            fecha1 += dia;
        }
        
        anio = selectFecha2.getModel().getYear();
        mes = selectFecha2.getModel().getMonth() + 1;
        dia = selectFecha2.getModel().getDay();
        
        String fecha2 = anio + "";
        fecha2 = fecha2.substring(2);
        if(mes < 10) {
            fecha2 += "0" + mes;
        } else {
            fecha2 += mes;
        }
        if(dia < 10) {
            fecha2 += "0" + dia;
        } else {
            fecha2 += dia;
        }
        
        
        rangoFecha[0] = fecha1;
        rangoFecha[1] = fecha2;
        
        System.out.println("Rango Fecha: " + rangoFecha[0] + " - " + rangoFecha[1]);
        
        return rangoFecha;
    }
    
    
    
    
    
    private static String loadFranjaHoraria() {
        String franjaHoraria = "N-N";
        
        int indexFranja = gui.selectFranjaHoraria.getSelectedIndex();
        switch(indexFranja) {
            case 0:
                franjaHoraria = "N-N";
                break;
            case 1:
                franjaHoraria = "4-23";
                break;
            case 2:
                franjaHoraria = "4-5";
                break;
            case 3:
                franjaHoraria = "5-6";
                break;
            case 4:
                franjaHoraria = "6-7";
                break;
            case 5:
                franjaHoraria = "7-8";
                break;
            case 6:
                franjaHoraria = "8-9";
                break;
            case 7:
                franjaHoraria = "9-10";
                break;
            case 8:
                franjaHoraria = "10-11";
                break;
            case 9:
                franjaHoraria = "11-12";
                break;
            case 10:
                franjaHoraria = "12-13";
                break;
            case 11:
                franjaHoraria = "13-14";
                break;
            case 12:
                franjaHoraria = "14-15";
                break;
            case 13:
                franjaHoraria = "15-16";
                break;
            case 14:
                franjaHoraria = "16-17";
                break;
            case 15:
                franjaHoraria = "17-18";
                break;
            case 16:
                franjaHoraria = "18-19";
                break;
            case 17:
                franjaHoraria = "19-20";
                break;
            case 18:
                franjaHoraria = "20-21";
                break;
            case 19:
                franjaHoraria = "21-22";
                break;
            case 20:
                franjaHoraria = "22-23";
                break;
            case 21:
                franjaHoraria = "23-24";
                break;
            default:
                franjaHoraria = "N-N";
        }
        
        System.out.println("Franja Horaria: " + franjaHoraria);
        
        return franjaHoraria;
    }
    
    
    
    public static void realizarConsulta() {
        if(gui.radioReporte1.isSelected()) {
            
        }
        if(gui.radioReporte2.isSelected()) {
            
        }
        if(gui.radioReporte3.isSelected()) {
            
        }
        if(gui.radioReporte4.isSelected()) {
            
        }
        if(gui.radioReporte5.isSelected()) {
            
        }
    }
    
    
    
    public static void log(String mensaje) {
        if(gui != null) {
            gui.lblMensajeLog.setText(" Log: " + mensaje);
        }
    }
    
}
