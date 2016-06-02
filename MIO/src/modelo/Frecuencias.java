/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import static Connection.ConnectionSQL.conexBD;

/**
 *
 * @author Shalóm
 * //Clase controlador de la BD en la tabla de abordaje
 */
public class Frecuencias 
{
    private static final String select_base="SELECT numero_pasajeros FROM abordaje ";
   
    // Retorna la cantidad de pasajeros según los criterios dados
    
    public String cantidadPasajeros(String fecha,String ruta,String franja)
    {
        int out=0;
        String where=crearWhere( fecha, ruta, franja);
        if(where==null)
            where="WHERE 1";
        if(conexBD.connected())
            conexBD.query(select_base+where);
        
        ResultSet r = conexBD.lastResult();
        try 
        {
           while(r.next())
           {  out+= r.getInt("numero_pasajeros"); }
        }catch (SQLException ex) { return "";  }      
        String SQL= "(null,'"+fecha+"','"+franja+"','"+ruta+"',null,'"+out+"')";
        System.out.println(SQL);
        return SQL;
    } //crea la consulta
    private String crearWhere(String fecha,String ruta,String franja)
    { //Se verifica si la consulta debe ser anidada
        String out=" WHERE ";
        boolean anidacion=false;
        if(fecha!=null)
        {  out+=" fecha LIKE '"+fecha+"'";anidacion=true;}
        if(ruta!=null)
        {   out+=anidacion?" AND ":""; out+=" nombre LIKE '"+ruta+"'"; anidacion=true;}
        if(fecha!=null)
        {   out+=anidacion?" AND ":""; out+=" franja LIKE '"+franja+"'";}
       
        return out;
    }
    
    public static final void main(String[] agrs)
    { //Main para test
        conexBD.connect(null,null,null);
        Frecuencias f= new Frecuencias();
        Scanner teclado= new Scanner(System.in);
        String ruta,fecha,franja;
        while(true)
        {
            System.out.println("Ingresar: Ruta, Fecha, Franja horaria");
            ruta=teclado.nextLine();
            fecha=teclado.nextLine();
            franja=teclado.nextLine();
            
            System.out.println("Cantidad de pasajeros ruta: "+ruta+"fecha: "+fecha+" franja: "+franja);

            System.out.println(f.cantidadPasajeros( fecha,ruta, franja));
        }        
    }
    
}
