/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import gui.GUI;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import modelo.ConnectionDB;
import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

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
    private static JFreeChart graficaReporte1, graficaReporte2, graficaReporte3, graficaReporte4, graficaReporte5;
    private static List<String> listEstaciones;
    private static List<String> listRutas;
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
        gui.selectEstaciones.addItem("Todas");
        for (String estacion : estaciones) {
            gui.selectEstaciones.addItem(estacion);
        }
        gui.selectEstaciones.setSelectedIndex(1);
        listEstaciones = estaciones;
    }
    
    
    
    public static void setSelectRutas(List<String> rutas) {
        gui.selectRutas.removeAllItems();
        gui.selectRutas.addItem("Selecionar");
        gui.selectRutas.addItem("Todas");
        for (String ruta : rutas) {
            gui.selectRutas.addItem(ruta);
        }
        gui.selectRutas.setSelectedIndex(1);
        listRutas = rutas;
    }
    
    
    
    public static void loadDescReporte() {
        if(gui.radioReporte1.isSelected()) {
            //log("Descripcion de Reporte1");
            gui.textAreaReporte.setText("Cantidad de pasajeros movilizados por el sistema en franjas horarias y fechas específicas.");
            gui.selectEstaciones.setEnabled(true);
            gui.selectRutas.setEnabled(true);
        }
        if(gui.radioReporte2.isSelected()) {
            //log("Descripcion de Reporte2");
            gui.textAreaReporte.setText("Estaciones donde hay más demanda de pasajeros.");
            gui.selectEstaciones.setEnabled(true);
            gui.selectRutas.setEnabled(false);
        }
        if(gui.radioReporte3.isSelected()) {
            //log("Descripcion de Reporte3");
            gui.textAreaReporte.setText("Rutas que más pasajeros mueven.");
            gui.selectEstaciones.setEnabled(false);
            gui.selectRutas.setEnabled(true);
        }
        if(gui.radioReporte4.isSelected()) {
            //log("Descripcion de Reporte4");
            gui.textAreaReporte.setText("Franjas horarias donde más hay movimiento de pasajeros.");
            gui.selectEstaciones.setEnabled(true);
            gui.selectRutas.setEnabled(true);
        }
        if(gui.radioReporte5.isSelected()) {
            //log("Descripcion de Reporte5");
            gui.textAreaReporte.setText("Comparación de la demanda en días laborables, fines de semana y días festivos.");
            gui.selectEstaciones.setEnabled(true);
            gui.selectRutas.setEnabled(true);
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
    
    
    
    
    private static String[] loadEstaciones() {
        String[] estaciones = {};
        
        if(gui.selectEstaciones.getSelectedIndex() == 1) {
            estaciones = new String[gui.selectEstaciones.getItemCount()-2];
            int i = 0;
            for (String estacion : listEstaciones) {
                estaciones[i] = estacion;
                i++;
            }
        } else if(gui.selectEstaciones.getSelectedIndex() != 0) {
            estaciones = new String[1];
            estaciones[0] = listEstaciones.get(gui.selectEstaciones.getSelectedIndex()-2);
        }
        
        return estaciones;
    }
    
    
    
    private static String[] loadRutas() {
        String[] rutas = {};
        
        if(gui.selectRutas.getSelectedIndex() == 1) {
            rutas = new String[gui.selectRutas.getItemCount()-2];
            int i = 0;
            for (String ruta : listRutas) {
                rutas[i] = ruta;
                i++;
            }
        } else if(gui.selectRutas.getSelectedIndex() != 0) {
            rutas = new String[1];
            rutas[0] = listRutas.get(gui.selectRutas.getSelectedIndex()-2);
        }
        
        return rutas;
    }
    
    
    
    public static void realizarConsulta() {
        if(gui.radioReporte1.isSelected()) {
            graficarConsultaReporte1();
        }
        if(gui.radioReporte2.isSelected()) {
            graficarConsultaReporte2();
        }
        if(gui.radioReporte3.isSelected()) {
            graficarConsultaReporte3();
        }
        if(gui.radioReporte4.isSelected()) {
            graficarConsultaReporte4();
        }
        if(gui.radioReporte5.isSelected()) {
           graficarConsultaReporte5();
        }
    }
    
    
    
    
    private static void graficarConsultaReporte1() {
        // Obtiene los parametros de consulta
        String[] rangoFecha = loadRangoFecha();
        String franjaHoraria = loadFranjaHoraria();
        String[] estaciones = loadEstaciones();
        String[] rutas = loadRutas();
        // Realizar Consulta
        ResultSet resultSet = connectionDB.selectReporte1(rangoFecha, franjaHoraria, estaciones, rutas);
        
        // Crea una grafica de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try {
            
            while(resultSet.next()) {
                dataset.setValue(Integer.parseInt(resultSet.getString("cant_pasajeros")), resultSet.getString("nombre_ruta_estacion"), resultSet.getString("fecha_bruta"));
            }
            
            graficaReporte1 = ChartFactory.createBarChart("Reporte1",
                    "Fecha", "Cantidad Pasajeros", dataset, PlotOrientation.VERTICAL,
                    true, true, false);
            
            ChartPanel gPanel = new ChartPanel(graficaReporte1);
            gui.panelGraficas.removeAll();
            gui.panelGraficas.add(gPanel);
            gui.panelGraficas.updateUI();
            gui.pack();
            
        } catch (SQLException ex) {
            log("Error al crear DataSet para la grafica de Reporte1");
        }
    }
    
    
    
    
    private static void graficarConsultaReporte2() {
        // Obtiene los parametros de consulta
        String[] rangoFecha = loadRangoFecha();
        String franjaHoraria = loadFranjaHoraria();
        String[] estaciones = loadEstaciones();
        // Realizar Consulta
        ResultSet resultSet = connectionDB.selectReporte2(rangoFecha, franjaHoraria, estaciones);
        
        // Crea una grafica de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try {
            
            while(resultSet.next()) {
                dataset.setValue(Integer.parseInt(resultSet.getString("total_pasajeros")), resultSet.getString("nombre_ruta_estacion"), resultSet.getString("total_pasajeros"));
            }
            
            graficaReporte2 = ChartFactory.createBarChart("Reporte2",
                    "Estaciones", "Cantidad Pasajeros", dataset, PlotOrientation.VERTICAL,
                    true, true, false);
            
            ChartPanel gPanel = new ChartPanel(graficaReporte2);
            gui.panelGraficas.removeAll();
            gui.panelGraficas.add(gPanel);
            gui.panelGraficas.updateUI();
            gui.pack();
            
        } catch (SQLException ex) {
            log("Error al crear DataSet para la grafica de Reporte2");
        }
    }
    
    
    
    
    private static void graficarConsultaReporte3() {
        // Obtiene los parametros de consulta
        String[] rangoFecha = loadRangoFecha();
        String franjaHoraria = loadFranjaHoraria();
        String[] rutas = loadRutas();
        // Realizar Consulta
        ResultSet resultSet = connectionDB.selectReporte3(rangoFecha, franjaHoraria, rutas);
        
        // Crea una grafica de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try {
            
            while(resultSet.next()) {
                dataset.setValue(Integer.parseInt(resultSet.getString("total_pasajeros")), resultSet.getString("nombre_ruta_estacion"), resultSet.getString("total_pasajeros"));
            }
            
            graficaReporte3 = ChartFactory.createBarChart("Reporte3",
                    "Rutas", "Cantidad Pasajeros", dataset, PlotOrientation.VERTICAL,
                    true, true, false);
            
            ChartPanel gPanel = new ChartPanel(graficaReporte3);
            gui.panelGraficas.removeAll();
            gui.panelGraficas.add(gPanel);
            gui.panelGraficas.updateUI();
            gui.pack();
            
        } catch (SQLException ex) {
            log("Error al crear DataSet para la grafica de Reporte3");
        }
    }
    
    
    
    
    private static void graficarConsultaReporte4() {
        // Obtiene los parametros de consulta
        String[] rangoFecha = loadRangoFecha();
        String[] estaciones = loadEstaciones();
        String[] rutas = loadRutas();
        // Realizar Consulta
        ResultSet resultSet = connectionDB.selectReporte4(rangoFecha, estaciones, rutas);
        
        // Crea una grafica de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try {
            
            while(resultSet.next()) {
                dataset.setValue(Integer.parseInt(resultSet.getString("total_pasajeros")), resultSet.getString("nombre_ruta_estacion"), resultSet.getString("tiempo_bruto"));
            }
            
            graficaReporte4 = ChartFactory.createBarChart("Reporte4",
                    "Franja Horaria", "Cantidad Pasajeros", dataset, PlotOrientation.VERTICAL,
                    true, true, false);
            
            
            
            ChartPanel gPanel = new ChartPanel(graficaReporte4);
            
            gui.panelGraficas.removeAll();
            gui.panelGraficas.add(gPanel);
            gui.panelGraficas.updateUI();
            gui.pack();
            
        } catch (SQLException ex) {
            log("Error al crear DataSet para la grafica de Reporte4");
        }
    }
    
    
    
    
    private static void graficarConsultaReporte5() {
        // Obtiene los parametros de consulta
        String[] rangoFecha = loadRangoFecha();
        String franjaHoraria = loadFranjaHoraria();
        String[] estaciones = loadEstaciones();
        String[] rutas = loadRutas();
        // Realizar Consulta , Dias Laborales
        ResultSet diasLaborales = connectionDB.selectReporte5_1(rangoFecha, franjaHoraria, estaciones, rutas);
        // Realizar Consulta , Fin de Semana
        ResultSet finSemana = connectionDB.selectReporte5_2(rangoFecha, franjaHoraria, estaciones, rutas);
        // Realizar Consulta , Dias Festivos
        ResultSet diasFestivos = connectionDB.selectReporte5_3(rangoFecha, franjaHoraria, estaciones, rutas);
        // Realizar Consulta , Dias Feriados
        ResultSet diasFeriados = connectionDB.selectReporte5_4(rangoFecha, franjaHoraria, estaciones, rutas);
        
        // Crea una grafica de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try {
            
            
            // Dias Laborales
            while(diasLaborales.next()) {
                dataset.setValue(Integer.parseInt(diasLaborales.getString("cant_pasajeros")), "Dia Laboral", diasLaborales.getString("fecha_bruta") + " - " + diasLaborales.getString("nombre_dia"));
            }
            // Fin de Semana
            while(finSemana.next()) {
                dataset.setValue(Integer.parseInt(finSemana.getString("cant_pasajeros")), "Fin de Semana", finSemana.getString("fecha_bruta") + " - " + finSemana.getString("nombre_dia"));
            }
            // Dias Festivos
            while(diasFestivos.next()) {
                dataset.setValue(Integer.parseInt(diasFestivos.getString("cant_pasajeros")), "Dias Festivos", diasFestivos.getString("fecha_bruta") + " - " + diasFestivos.getString("nombre_dia"));
            }
            // Dias Feriados
            while(diasFeriados.next()) {
                dataset.setValue(Integer.parseInt(diasFeriados.getString("cant_pasajeros")), "Dias Feriados", diasFeriados.getString("fecha_bruta") + " - " + diasFeriados.getString("nombre_dia"));
            }
            
            
            graficaReporte5 = ChartFactory.createBarChart("Reporte5",
                    "Franja Horaria", "Cantidad Pasajeros", dataset, PlotOrientation.VERTICAL,
                    true, true, false);
            
            
            
            ChartPanel gPanel = new ChartPanel(graficaReporte5);
            
            gui.panelGraficas.removeAll();
            gui.panelGraficas.add(gPanel);
            gui.panelGraficas.updateUI();
            gui.pack();
            
        } catch (SQLException ex) {
            log("Error al crear DataSet para la grafica de Reporte5");
        }
    }
    
    
    
    
    public static void guardarGrafica() {
        if(gui.panelGraficas.getComponentCount() > 0) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo JPG", "jpg");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(gui);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                String pathGrafica = chooser.getSelectedFile().getAbsolutePath();
                try {
                    if(gui.groupReportes.isSelected(gui.radioReporte1.getModel())) {
                        ChartUtilities.saveChartAsJPEG(new File(pathGrafica), graficaReporte1, 500, 300);
                    }
                    if(gui.groupReportes.isSelected(gui.radioReporte2.getModel())) {
                        ChartUtilities.saveChartAsJPEG(new File(pathGrafica), graficaReporte2, 500, 300);
                    }
                    if(gui.groupReportes.isSelected(gui.radioReporte3.getModel())) {
                        ChartUtilities.saveChartAsJPEG(new File(pathGrafica), graficaReporte3, 500, 300);
                    }
                    if(gui.groupReportes.isSelected(gui.radioReporte4.getModel())) {
                        ChartUtilities.saveChartAsJPEG(new File(pathGrafica), graficaReporte4, 500, 300);
                    }
                    if(gui.groupReportes.isSelected(gui.radioReporte5.getModel())) {
                        ChartUtilities.saveChartAsJPEG(new File(pathGrafica), graficaReporte5, 500, 300);
                    }
                } catch (IOException ex) {
                    log("Error al generar archivo de imagen del reporte " + ex.getMessage());
                }
                log("Guarda imagen de reporte " + pathGrafica);
            }
        }
    }
    
    
    
    
    public static void log(String mensaje) {
        if(gui != null) {
            gui.lblMensajeLog.setText(" Log: " + mensaje);
        }
    }
    
}
