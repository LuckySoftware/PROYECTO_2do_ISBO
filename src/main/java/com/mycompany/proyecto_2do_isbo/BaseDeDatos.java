/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_2do_isbo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/*
 *
 * @author LucianoMoreira
 *
 */

public class BaseDeDatos 
{
        // PARAMETROS DE LA BASE DE DATOS (DB)
        private final String url = "jdbc:mysql://localhost:4306/proyecto_db";
        private final String usuario = "root";
        private final String pass = "";
        
        
        // PROPIEDADES DE LA CLASE
        private Connection connection;
        private ResultSet result;
        
        
        // METODO CONSTRUCTUR DE LA CLASE
        public BaseDeDatos() 
        {
            
            try
            {
                this.connection = DriverManager.getConnection(url, usuario, pass);
                        
            }
            
            catch(SQLException ex)
            {
                JOptionPane.showMessageDialog(null,  "Error: " + ex.getMessage());
            }
            
            
            this.result = null;
        }
        
        
        // METODO PARA EJECUTAR CONSULTAS SELECT
        public ResultSet ejecutarSQL(PreparedStatement sql)
        {
           
            try
            {
                
                this.result = sql.executeQuery();
                
            }
            
            
            catch(SQLException ex)
            {
                
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                this.result = null;  
                
            }
            return this.result;
        }
        
         //Método para ejecutar consultas de tipo Ingreso, modificación o borrado
         public void ejecutar(PreparedStatement sql)
         {
            try
            {
                sql.execute();
            }
            catch(SQLException ex)
            {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
        

        // SACAR EL ID DE JUGADOR

        public ResultSet identificador(PreparedStatement sql)
        {

            try
            {
                String nombreJugador = "";
                BaseDeDatos bd = new BaseDeDatos();
                PreparedStatement sentencia = bd.getConnection().prepareStatement("SELECT * FROM jugador WHERE nombreCompleto = ?");
                sentencia.setString(1, nombreJugador);
                ResultSet resultado = sentencia.executeQuery();
                return resultado;  
            }
            
            
            catch(SQLException ex)
            {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }

            return this.result;

        }




        // GETTERS & SETTERS
        public Connection getConnection() 
        {
            return connection;
        }

        
        public void setConnection(Connection connection) 
        {
            this.connection = connection;
        }

        
        public ResultSet getResult() 
        {
            return result;
        }

        
        public void setResult(ResultSet result) 
        {
            this.result = result;
        }
}
