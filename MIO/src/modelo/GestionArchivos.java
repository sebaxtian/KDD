/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Shalóm
 */
public final class GestionArchivos 
{   
    FileWriter dim_fecha;
    FileWriter dim_tiempo;
    FileWriter dim_rutas;
    FileWriter abordaje;
    FileWriter frecuencias; 
    
    public GestionArchivos() throws IOException
    {   //Crea archivo. En caso de que ya exista, lo abre y lo trunca 
        dim_fecha = new FileWriter("sql/dim_fecha.sql");
        dim_tiempo = new FileWriter("sql/dim_tiempo.sql");
        dim_rutas = new FileWriter("sql/dim_rutas.sql");
        abordaje = new FileWriter("sql/abordaje.sql");
        frecuencias = new FileWriter("sql/frecuencias.sql");
        
        dim_fecha.write("INSERT INTO dim_fecha VALUES ");
        dim_tiempo.write("INSERT INTO dim_tiempo VALUES "); 
        abordaje.write("INSERT INTO dim_ruta_estacion VALUES ");
        dim_rutas.write("INSERT INTO abordaje VALUES "); 
        frecuencias.write("INSERT INTO frecuencias VALUES "); 
    }
    
    public void close()
    {        
        insertFecha(";");
        insertTiempo(";"); 
        insertAbordaje(";");
        insertRutas(";"); 
        insertFrecuencia(";");
        
        try
        { //Cerrar archivos por si no están en uso
            dim_fecha.close();
            dim_tiempo.close();
            dim_rutas.close();
            abordaje.close();
            frecuencias.close();
         } catch (IOException ex){}    
    }
    
    public final void insertFrecuencia(String sql)
    {
        try
        {
        frecuencias.write(sql+",\n");
        frecuencias.flush();
        } catch (IOException ex){}
    } public final void insertFecha(String sql)
    {
        try
        {
        dim_fecha.write(sql+",\n");
        dim_fecha.flush();
        } catch (IOException ex){}
    }
    public void insertTiempo(String sql)
    {
        try
        {
        dim_tiempo.write(sql+",\n");
        dim_tiempo.flush();
        } catch (IOException ex){}
    }
    public void insertRutas(String sql)
    {
        try
        {
        dim_rutas.write(sql+",\n");
        dim_rutas.flush();
        } catch (IOException ex){}
    }
    
    public void insertAbordaje(String sql)
    {
        try
        {
        abordaje.write(sql+",\n");
        abordaje.flush();
        } catch (IOException ex){}
    }
   
}
