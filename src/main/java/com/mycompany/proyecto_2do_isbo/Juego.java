package com.mycompany.proyecto_2do_isbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

import java.util.Timer;
import java.util.TimerTask;

public class Juego extends javax.swing.JFrame {
    
    private Timer temporizador;
    
    private int tiempoRestante = 10;
    
    private String preguntaCorrecta;
    
    private String textoPregunta;
    
    private String[] opcionesRespuesta = new String[4];
    
    private int indiceRespuestaCorrecta;
    
    private int idJugadorActivo;
  
    BaseDeDatos baseDeDatos = new BaseDeDatos();

    private int jugadorId;
    
    public Juego(int jugadorId) 
    {
        initComponents();
        this.jugadorId = jugadorId;
        this.idJugadorActivo = jugadorId;

        cargarPreguntasYRespuestas();       
        cargarPreguntaAleatoria();
        iniciarTemporizador();
    }
    
    private void cargarPreguntaAleatoria() 
    {
        Connection conexion = baseDeDatos.getConnection();
    
        if (conexion != null) 
        {
            try 
            {
                // SELECCIONAMOS UNA PREGUNTA ALEATORIA
                String consultaPregunta = "SELECT * FROM pregunta ORDER BY RAND() LIMIT 1";
                Statement sentencia = conexion.createStatement();
                ResultSet resultadoPregunta = sentencia.executeQuery(consultaPregunta);
                
                if (resultadoPregunta.next())
                {
                    textoPregunta = resultadoPregunta.getString("textoPregunta");
                    preguntaCorrecta = resultadoPregunta.getString("respuestaCorrecta"); 
                    int idPregunta = resultadoPregunta.getInt("idPregunta");
                    
                    // OBTENEMOS LAS OPCIONES DE RESPUESTA
                    String consultaOpciones = "SELECT textoOpcion FROM opcionRespuesta WHERE idPregunta = ?";
                    PreparedStatement premisa = conexion.prepareStatement(consultaOpciones);
                    premisa.setInt(1, idPregunta);
                    ResultSet resultadoOpciones = premisa.executeQuery();
                    
                    int i = 0;
                    indiceRespuestaCorrecta = i;
                    
                    while (resultadoOpciones.next() && i < 4) 
                    {
                        String opcion = resultadoOpciones.getString("textoOpcion");
                        opcionesRespuesta[i] = opcion;  
                       
                        if (opcion.equals(preguntaCorrecta)) 
                        {
                            indiceRespuestaCorrecta = i; // ALOJAR EL INDICE DE LA RESPUESTA CORRECTA (i)
                        }
                        
                        i++;
                    }

                    // RELLENAMOS OPCIONES VACIAS
                    while (i < 4) 
                    {
                        opcionesRespuesta[i] = "";
                        i++;
                    }
                }

                cargarPreguntasYRespuestas();
              
            }

            catch (SQLException e) 
            { 
                JOptionPane.showMessageDialog(null, "ERROR AL CARGAR LA PREGUNTA: " + e.getMessage());
            }
        }
    }

    private void cargarPreguntasYRespuestas() 
    {
        labelParrafo.setText(textoPregunta);
        btn1.setText(opcionesRespuesta[0]);
        btn2.setText(opcionesRespuesta[1]); 
        btn3.setText(opcionesRespuesta[2]);
        btn4.setText(opcionesRespuesta[3]);
    }

    private void iniciarTemporizador()
    {
        temporizador = new Timer();
        temporizador.scheduleAtFixedRate(new TimerTask() 
        {
            @Override
            public void run() 
            {
                
                tiempoRestante--;
                labelTemporizador.setText("Tiempo: " + tiempoRestante + " s");
                
                if (tiempoRestante <= 0) 
                {
                    temporizador.cancel();
                }
            }
        }, 1000, 1000);
    }
    
    private void respuesta(int indiceSeleccionado) 
    {

        boolean esCorrecta = indiceSeleccionado == indiceRespuestaCorrecta;
        temporizador.cancel();
        
        
        try 
        {
            BaseDeDatos bd = new BaseDeDatos();
            PreparedStatement sentencia = bd.getConnection().prepareStatement("UPDATE ranking SET puntosObtenidos = puntosObtenidos + 10 WHERE idJugador = ?");

            sentencia.setInt(1, idJugadorActivo);

            int filasActualizadas = sentencia.executeUpdate();

            // Verifica si se actualizO alguna fila
            //if (filasActualizadas > 0) 
            //{
                //JOptionPane.showMessageDialog(null, "Se han asignado 10 puntos al usuario con ID " + idJugadorActivo);

            //}

            //else 
            //{
                //JOptionPane.showMessageDialog(null, "No se encontró el usuario con ID " + idJugadorActivo);
            //}

        }
        
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        
        tiempoRestante = 11;
        iniciarTemporizador();      
        cargarPreguntaAleatoria();
        cargarPreguntasYRespuestas(); 
    }

                       
    private void initComponents() 
    {

        jLabel1 = new javax.swing.JLabel();
        btn1 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        labelParrafo = new javax.swing.JLabel();
        labelTemporizador = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("PT Sans", 0, 48)); // NOI18N
        jLabel1.setText("RESPONDI-2");

        btn1.setText("boton 1");
        btn1.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                btn1ActionPerformed(evt);
            }
        });

        btn2.setText("boton 2");
        btn2.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                btn2ActionPerformed(evt);
            }
        });

        btn3.setText("boton 3");
        btn3.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                btn3ActionPerformed(evt);
            }
        });

        btn4.setText("boton 4");
        btn4.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                btn4ActionPerformed(evt);
            }
        });

        labelParrafo.setFont(new java.awt.Font("PT Sans", 0, 18)); // NOI18N
        labelParrafo.setText("parrafo");

        labelTemporizador.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        labelTemporizador.setText("10");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(175, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(199, 199, 199))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelParrafo)
                        .addGap(270, 270, 270))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(154, 154, 154))))
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(labelTemporizador)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTemporizador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(labelParrafo)
                .addGap(80, 80, 80)
                .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(154, 154, 154))
        );

        pack();
    }                     

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) 
    {                                     
        // TODO add your handling code here:
         respuesta(0);
    }                                    

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) 
    {                                     
        // TODO add your handling code here:
         respuesta(1);  
    }                                    

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) 
    {                                     
        // TODO add your handling code here:
        respuesta(2);  
    }                                    

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) 
    {                                     
        // TODO add your handling code here:
        respuesta(3);  
    }                                    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
        try 
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) 
            {
                if ("Nimbus".equals(info.getName())) {

                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }

        catch (ClassNotFoundException ex) 
        {
            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        catch (InstantiationException ex) 
        {
            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 

        catch (IllegalAccessException ex) 
        {
            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        
        catch (javax.swing.UnsupportedLookAndFeelException ex) 
        {
            java.util.logging.Logger.getLogger(Juego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new Juego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelParrafo;
    private javax.swing.JLabel labelTemporizador;
    // End of variables declaration                   

}