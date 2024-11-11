package com.mycompany.proyecto_2do_isbo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class Jugador 
{
    private int id;
    private String email;


    // METODO CONSTRUCTOR
    public Jugador(int id, String email) 
    {
        this.id = id;
        this.email = email;
    }

    // GETTERS
    public int getId() 
    {
        return id;
    }

    public String getEmail() 
    {
        return email;
    }



    // OBTENER IDE DEL JUGADOR POR EMAIL Y PASSWORD
    public static Jugador obtenerJugador(String email, String pass) 
    {
        PreparedStatement sentencia = null;
        ResultSet resultSet = null;
        Jugador jugador = null;

        try 
        {
            // CONEXION A LA DB
            BaseDeDatos db = new BaseDeDatos();
            sentencia = db.getConnection().prepareStatement("SELECT idJugador, correoElectronico FROM jugador WHERE correoElectronico = ? AND userPassword = ?");

            sentencia.setString(1, email);
            sentencia.setString(2, pass);
            resultSet = sentencia.executeQuery();

            if (resultSet.next()) 
            {
                // SI EXISTE Y ENCUENTRA UN JUGADOR CREA UNA NUEVA INSTANCIA
                jugador = new Jugador(resultSet.getInt("idJugador"), resultSet.getString("correoElectronico"));
            }
        }

        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return jugador;
        
    }
    
    public static Jugador obtenerJugadorPorId(int idJugador) {
    PreparedStatement sentencia = null;
    ResultSet resultSet = null;
    Jugador jugador = null;

    try 
    {
        // CONEXION A LA DB
        BaseDeDatos db = new BaseDeDatos();
        sentencia = db.getConnection().prepareStatement("SELECT idJugador, correoElectronico, nombreCompleto FROM jugador WHERE idJugador = ?");
        sentencia.setInt(1, idJugador);
        resultSet = sentencia.executeQuery();

        if (resultSet.next()) 
        {
            // SI EXISTE Y ENCUENTRA UN JUGADOR CREA UNA NUEVA INSTANCIA
            jugador = new Jugador(resultSet.getInt("idJugador"), resultSet.getString("correoElectronico"));
        }

    } 
    catch (SQLException e) 
    {
        e.printStackTrace();
    }
    
    return jugador;
    
    }
}