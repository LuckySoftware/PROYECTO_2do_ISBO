package com.mycompany.proyecto_2do_isbo;


import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;



public class Juego extends javax.swing.JFrame {
    
    private String respuestaCorrecta;
    
    private int idPregunta;
    
    private Timer temporizador;
    
    private int tiempoRestante = 10;
    
    private String textoPregunta;
    
    private String[] opcionesRespuesta = new String[4];
    
    private int indiceRespuestaCorrecta;
    
    private int idJugadorActivo;
    
    public int preguntasRespondidasContador = 0;
  
    private Set<Integer> preguntasRespondidas = new HashSet<>();
    
    BaseDeDatos baseDeDatos = new BaseDeDatos();

    private int jugadorId;
    
    public Juego(int jugadorId) 
    {
        initComponents();
        this.jugadorId = jugadorId;
        this.idJugadorActivo = jugadorId;

        cargarPreguntasYRespuestas(opcionesRespuesta, textoPregunta);
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
            if (preguntasRespondidasContador >= 4) {
                JOptionPane.showMessageDialog(null, "Juego terminado. Has respondido 4 preguntas.");
                preguntasRespondidasContador = 0;

                Final ventanaFinal = new Final(jugadorId);
                ventanaFinal.setVisible(true);
                this.setVisible(false);
                return;
            }

            String consultaDePregunta = "SELECT * FROM pregunta WHERE idPregunta NOT IN (SELECT idPregunta FROM pregunta WHERE idPregunta IN (?)) ORDER BY RAND() LIMIT 1";
            PreparedStatement premisaPregunta = conexion.prepareStatement(consultaDePregunta);

            StringBuilder idsRespondidas = new StringBuilder();
            for (Integer id : preguntasRespondidas) {
                idsRespondidas.append(id).append(",");
            }

            if (idsRespondidas.length() > 0) {
                idsRespondidas.setLength(idsRespondidas.length() - 1);
            }

            premisaPregunta.setString(1, idsRespondidas.toString());
            ResultSet resultadoPregunta = premisaPregunta.executeQuery();

            if (resultadoPregunta.next()) {
                idPregunta = resultadoPregunta.getInt("idPregunta");
                textoPregunta = resultadoPregunta.getString("textoPregunta");

                String consultaOpciones = "SELECT * FROM opcionRespuesta WHERE idPregunta = ?";
                PreparedStatement premisaOpciones = conexion.prepareStatement(consultaOpciones);
                premisaOpciones.setInt(1, idPregunta);
                ResultSet resultadoOpciones = premisaOpciones.executeQuery();

                List<String> opciones = new ArrayList<>();
                respuestaCorrecta = ""; // Reiniciar la respuesta correcta antes de cargar una nueva pregunta
                int indiceRespuestaCorrecta = -1; // Para almacenar el índice de la respuesta correcta

                // Obtener opciones de respuesta y guardar la respuesta correcta
                while (resultadoOpciones.next()) {
                    String textoOpcion = resultadoOpciones.getString("textoOpcion");
                    boolean esCorrecta = resultadoOpciones.getBoolean("esCorrecta");

                    opciones.add(textoOpcion);
                    if (esCorrecta) {
                        respuestaCorrecta = textoOpcion; // Guardar la respuesta correcta
                        indiceRespuestaCorrecta = opciones.size() - 1; // Guardar el índice de la respuesta correcta
                    }
                }

                // Asegurarse de que la respuesta correcta esté entre las opciones
                if (!respuestaCorrecta.isEmpty() && !opciones.contains(respuestaCorrecta)) {
                    opciones.add(respuestaCorrecta); // Agregar la respuesta correcta si no está
                }

                // Mezclar las opciones
                Collections.shuffle(opciones);

                // Asignar las opciones a un array para que pueda ser utilizado por cargarPreguntasYRespuestas
                for (int i = 0; i < opcionesRespuesta.length; i++) {
                    opcionesRespuesta[i] = (i < opciones.size()) ? opciones.get(i) : ""; // Asigna las opciones o vacío si no hay más opciones
                }

                // Actualiza el texto de la pregunta y las opciones
                cargarPreguntasYRespuestas(opcionesRespuesta, textoPregunta);

                // Agregar la pregunta a la lista de preguntas respondidas
                preguntasRespondidas.add(idPregunta);
                preguntasRespondidasContador++;

                // Guardar el índice de la respuesta correcta
                this.indiceRespuestaCorrecta = indiceRespuestaCorrecta; // Asegúrate de tener esta variable en tu clase
            } else {
                JOptionPane.showMessageDialog(null, "No hay más preguntas disponibles.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la pregunta: " + e.getMessage());
        }
    }
}

private void cargarPreguntasYRespuestas(String[] opcionesRespuesta, String textoPregunta) {
    labelParrafo.setText(textoPregunta);
    btn1.setText(opcionesRespuesta.length > 0 ? opcionesRespuesta[0] : "");
    btn2.setText(opcionesRespuesta.length > 1 ? opcionesRespuesta[1] : "");
    btn3.setText(opcionesRespuesta.length > 2 ? opcionesRespuesta[2] : "");
    btn4.setText(opcionesRespuesta.length > 3 ? opcionesRespuesta[3] : "");
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
    
    private void respuesta(int indiceSeleccionado) {
    // Detener el temporizador
    temporizador.cancel();

    // Verificar si la respuesta seleccionada es correcta
    boolean esCorrecta = opcionesRespuesta[indiceSeleccionado].equals(respuestaCorrecta);

    // Conectar a la base de datos y actualizar los puntos
    Connection conexion = null;
    try {
        BaseDeDatos bd = new BaseDeDatos();
        conexion = bd.getConnection();

        if (conexion != null) {
            // Solo sumar puntos si la respuesta es correcta
            if (esCorrecta) {
                // Preparar la sentencia para actualizar los puntos
                String sql = "UPDATE ranking SET puntosObtenidos = puntosObtenidos + 10 WHERE idJugador = ?";
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                sentencia.setInt(1, idJugadorActivo);

                int filasActualizadas = sentencia.executeUpdate();

                // Verificar si se actualizaron filas
                if (filasActualizadas > 0) {
                    JOptionPane.showMessageDialog(null, "Se han asignado 10 puntos al usuario con ID " +  jugadorId);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el usuario con ID " +  jugadorId);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Respuesta incorrecta. No se han sumado puntos.");
            }

            // Reiniciar el temporizador para la próxima pregunta
            tiempoRestante = 11;
            iniciarTemporizador();

            // Cargar una nueva pregunta aleatoria
            cargarPreguntaAleatoria();

        }

            else 
            {
                JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos.");
            }
       }   
            catch (SQLException e) 
            {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
    
    }
    
    
    
    
        public void nuevaPartida() 
        {
            BaseDeDatos bd = new BaseDeDatos();
            Connection conexion = null; 

            // FECHA Y HORA DE AHORA MISMO
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fecha= date.format(formato);
            LocalTime horaComienza = LocalTime.now();
            
            
            String estado = "En curso";
            
            String query = "INSERT INTO partida (fecha, horaComienza, estado) VALUES (?, ?, ?)";

                conexion = bd.getConnection();
                try (PreparedStatement sentencia = conexion.prepareStatement(query)) 
                {
                    // PARAMETROS
                    sentencia.setString(1, fecha);
                    sentencia.setObject(2, horaComienza);
                    sentencia.setString(3, estado);

                    // Ejecutar la inserción
                    int filasInsertadas = sentencia.executeUpdate();
                    
                    if (filasInsertadas > 0) 
                    {
                        JOptionPane.showMessageDialog(null, "Partida insertada correctamente.");
                    }
                    
                    else 
                    {
                    JOptionPane.showMessageDialog(null, "Error al insertar la partida.");
                    }
            }
            
            catch (SQLException e) 
            {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
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