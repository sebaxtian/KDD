/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
public final class ConnectionSQL
{    
    public static ConnectionSQL conexBD=new ConnectionSQL();
    private Connection conexion;
    private ResultSet rs;// Almacena el resultado de la ultima consulta ejecutada.
    public ConnectionSQL()
    {
        try { Class.forName("com.mysql.jdbc.Driver");}
        catch (Exception e){ e.printStackTrace();}
    }
    /**
    *@param s: Direccion del servidor y la base de datos 
    *Ej:    "jdbc:mysql://localhost/Exce" donde  Exce, es el nombre de la base de datos.
    *@param u: Usuario 
    *@param p: password
    */    
    public boolean connect(String s,String u,String p)
    {
        try 
        {
            if(s==null||u==null||p==null)
                conexion = DriverManager.getConnection ("jdbc:mysql://localhost/bd_mio","root", "");
            else
                conexion = DriverManager.getConnection (s,u, p);
            return true;
        } catch (SQLException ex) {return false;}
    }    
    //@ q: query example "select palabra from exce.palabras where id<11"
    public void query(String q)
    {        
        try
        {           
            if(conexion==null){return;}
            
            Statement s = conexion.createStatement();
           rs = s.executeQuery(q);           
                   
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    public void queryInsert(String q)
    {        
        try
        {           
            if(conexion==null){return;}
            
            Statement s = conexion.createStatement();
            s.execute(q);
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //@return: resultado de la ultima consulta 
    public ResultSet lastResult()
    {   return rs; }
    public void disconnec()
    {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }      
    public boolean connected() 
    {
        try {
            return !conexion.isClosed();
        } catch (SQLException ex) {
           return false;}
    }
}
