/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.proyecto_2do_isbo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author ilucky
 */
public class Final extends javax.swing.JFrame {

    public int jugadorId;
    public String id = String.valueOf(jugadorId);
    
    public Final(int jugadorId) 
    {
        this.jugadorId = jugadorId;
        initComponents();
        mostrarPuntos();
        mostrarTiempo();
    }

    private void mostrarPuntos() {
        BaseDeDatos bd = new BaseDeDatos();
    
        try 
        {
            PreparedStatement sentencia = bd.getConnection().prepareStatement("SELECT * FROM ranking WHERE idJugador = ? LIMIT 1");
            sentencia.setInt(1, jugadorId);

            ResultSet resultadoFinal = sentencia.executeQuery(); 
        

            if (resultadoFinal.next()) 
            {
                String puntosObtenidos = resultadoFinal.getString("puntosObtenidos");
                labelPuntos.setText(puntosObtenidos);
            }
        }
        
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    
    private void mostrarTiempo() 
    {
        BaseDeDatos bd = new BaseDeDatos();
    
        try 
        {
            PreparedStatement sentencia = bd.getConnection().prepareStatement("SELECT * FROM partida WHERE idPartida = 1 LIMIT 1");
        
            ResultSet resultadoFinal = sentencia.executeQuery(); 
        
            if (resultadoFinal.next()) 
            {
                String tiempoComienzo = resultadoFinal.getString("horaComienza");
                String tiempoFin = resultadoFinal.getString("horaFinalizacion");
            
                labelTiempoInicio.setText(tiempoComienzo);
                labelTiempoFinal.setText(tiempoFin);
            }
        } 
        
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tiempo1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        Inicio = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        labelTiempoFinal = new javax.swing.JLabel();
        cantidadRondas = new javax.swing.JLabel();
        labelPuntos = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labelTiempoInicio = new javax.swing.JLabel();

        tiempo1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        tiempo1.setText("x");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Inicio.setText("Inicio");
        Inicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InicioActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("PT Sans", 0, 48)); // NOI18N
        jLabel1.setText("GAME OVER");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel2.setText("Tiempo Final:");

        labelTiempoFinal.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        labelTiempoFinal.setText("x");

        cantidadRondas.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N

        labelPuntos.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        labelPuntos.setText("x");

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel5.setText("puntos:");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel3.setText("Tiempo Inicial:");

        labelTiempoInicio.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        labelTiempoInicio.setText("x");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(cantidadRondas))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelTiempoInicio))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelPuntos))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelTiempoFinal)))))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(41, 41, 41)
                .addComponent(cantidadRondas)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labelTiempoInicio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(labelTiempoFinal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPuntos)
                    .addComponent(jLabel5))
                .addGap(185, 185, 185)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Inicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(48, 48, 48))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void InicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InicioActionPerformed
        // TODO add your handling code here:
        Landing ventanaLanding = new Landing(jugadorId);
        ventanaLanding.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_InicioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {   
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() {
                //new Final().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Inicio;
    private javax.swing.JLabel cantidadRondas;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel labelPuntos;
    private javax.swing.JLabel labelTiempoFinal;
    private javax.swing.JLabel labelTiempoInicio;
    private javax.swing.JLabel tiempo1;
    // End of variables declaration//GEN-END:variables
}
