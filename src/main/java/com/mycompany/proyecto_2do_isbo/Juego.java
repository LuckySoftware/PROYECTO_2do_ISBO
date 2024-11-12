package com.mycompany.proyecto_2do_isbo;



import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    
    private int idJugadorActivo;
    
    public int indiceRespuestaCorrecta;
    
    public int preguntasRespondidasContador = 0;
  
    private Set<Integer> preguntasRespondidas = new HashSet<>();
    
    BaseDeDatos baseDeDatos = new BaseDeDatos();

    private int jugadorId;
    
    public int cantidadPreguntas = 4;
    
    private int idPartida;
    
    public Juego(int jugadorId) 
    {
        initComponents();
        
        this.jugadorId = jugadorId;
        this.idJugadorActivo = jugadorId;

        cargarPreguntasYRespuestas(opcionesRespuesta, textoPregunta);
        cargarPreguntaAleatoria();
        iniciarTemporizador();
        
        nuevaPartida();
    }

    
    
private void cargarPreguntaAleatoria() 
{
    Connection conexion = baseDeDatos.getConnection();

    if (conexion != null) 
    {
        try 
        {
            if (preguntasRespondidasContador >= cantidadPreguntas) 
            {
                finalizarPartida();
                preguntasRespondidasContador = 0;

                Final ventanaFinal = new Final(jugadorId, idPartida);
                ventanaFinal.setVisible(true);
                this.setVisible(false);
                return;
            }

            String consultaDePregunta = "SELECT * FROM pregunta WHERE idPregunta NOT IN (SELECT idPregunta FROM pregunta WHERE idPregunta IN (?)) ORDER BY RAND() LIMIT 1";
            PreparedStatement premisaPregunta = conexion.prepareStatement(consultaDePregunta);

            // CONSTRUIR LOS TEXTOS DE MANERA EFICIENTE
            StringBuilder idsRespondidas = new StringBuilder();
            for (Integer id : preguntasRespondidas) {
                idsRespondidas.append(id).append(",");
            }

            if (idsRespondidas.length() > 0) 
            {
                idsRespondidas.setLength(idsRespondidas.length() - 1);
            }

            premisaPregunta.setString(1, idsRespondidas.toString());
            ResultSet resultadoPregunta = premisaPregunta.executeQuery();

            // SELECCIONAR LAS PREGUNTAS JUNTO A LOS TEXTOS
            if (resultadoPregunta.next()) 
            {
                idPregunta = resultadoPregunta.getInt("idPregunta");
                textoPregunta = resultadoPregunta.getString("textoPregunta");

                String consultaOpciones = "SELECT * FROM opcionRespuesta WHERE idPregunta = ?";
                PreparedStatement premisaOpciones = conexion.prepareStatement(consultaOpciones);
                premisaOpciones.setInt(1, idPregunta);
                ResultSet resultadoOpciones = premisaOpciones.executeQuery();

                List<String> opciones = new ArrayList<>();
                respuestaCorrecta = "";
                int indiceRespuestaCorrecta = -1;

                // OBTENER LAS OPCIONES DE RESPUESTA Y LA RESPUESTA CORRECTA
                while (resultadoOpciones.next()) 
                {
                    String textoOpcion = resultadoOpciones.getString("textoOpcion");
                    boolean esCorrecta = resultadoOpciones.getBoolean("esCorrecta");

                    opciones.add(textoOpcion);
                    if (esCorrecta) {
                        respuestaCorrecta = textoOpcion;
                        indiceRespuestaCorrecta = opciones.size() - 1;
                    }
                }

                // ASEGURARSE DE QUE LA RESPUESTA CORRECTA ESTE DENTRO DE LAS OPCIONES
                // EN CASO DE NO ESTAR SE AGREGA
                if (!respuestaCorrecta.isEmpty() && !opciones.contains(respuestaCorrecta)) 
                {
                    opciones.add(respuestaCorrecta);
                }

                // MEZCLAR LAS OPCIONES
                Collections.shuffle(opciones);

                // ASIGNAR LAS OPCIONES A UN ARRAY 
                for (int i = 0; i < opcionesRespuesta.length; i++) 
                {
                    opcionesRespuesta[i] = (i < opciones.size()) ? opciones.get(i) : "";
                }

                cargarPreguntasYRespuestas(opcionesRespuesta, textoPregunta);

                preguntasRespondidas.add(idPregunta);
                preguntasRespondidasContador++;

                // GUARDAR EL indice DE LA RESPUESTA CORRECTA
                this.indiceRespuestaCorrecta = indiceRespuestaCorrecta;
            }

        }
        
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}

    private void cargarPreguntasYRespuestas(String[] opcionesRespuesta, String textoPregunta) 
    {
    
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
    
    
    private void respuesta(int indiceSeleccionado) 
    {

        
        temporizador.cancel();

        // VERIFICACION DE LA RESPUESTA A VER SI ES CORRECTA 
        boolean esCorrecta = opcionesRespuesta[indiceSeleccionado].equals(respuestaCorrecta);

        Connection conexion = null;
    
        try 
        {
            BaseDeDatos bd = new BaseDeDatos();
            conexion = bd.getConnection();

            if (conexion != null) 
            {    
            
                // SI ES CORRECTA LA RESPUESTA LE SUMA LOS PUNTOS
                if (esCorrecta) 
                {
                    String sql = "UPDATE ranking SET puntosObtenidos = puntosObtenidos + 10 WHERE idJugador = ?";
                    PreparedStatement sentencia = conexion.prepareStatement(sql);
                    sentencia.setInt(1, idJugadorActivo);

                    sentencia.executeUpdate();
                }
            
                else 
                {
                    JOptionPane.showMessageDialog(null, "Respuesta incorrecta");
                }

                // RECARGAR TODO
                tiempoRestante = 11;
                iniciarTemporizador();
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
        Connection conexion = null;

        // FECHA Y HORA DE AHORA MISMO
        LocalDateTime ahora = LocalDateTime.now();
        Timestamp horaComienza = Timestamp.valueOf(ahora);
        String estado = "En curso";

        String query = "INSERT INTO partida (horaComienza, estado) VALUES (?, ?)";

        conexion = baseDeDatos.getConnection();
        try (PreparedStatement sentencia = conexion.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            sentencia.setTimestamp(1, horaComienza);
            sentencia.setString(2, estado);

            int filasInsertadas = sentencia.executeUpdate();

            if (filasInsertadas > 0) 
            {
                // Obtener el ID de la partida insertada
                ResultSet generatedKeys = sentencia.getGeneratedKeys();
                if (generatedKeys.next()) 
                {
                    idPartida = generatedKeys.getInt(1);
                }
            } 
            else 
            {
                JOptionPane.showMessageDialog(null, "Error al iniciar la partida.");
            }

        } 
        catch (SQLException e) 
        {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void finalizarPartida() 
    {
        Connection conexion = null;

        // FECHA Y HORA DE AHORA MISMO
        LocalDateTime ahora = LocalDateTime.now();
        Timestamp horaFinalizacion = Timestamp.valueOf(ahora);

        String query = "UPDATE partida SET horaFinalizacion = ?, estado = ? WHERE idPartida = ?";

        conexion = baseDeDatos.getConnection();
        try (PreparedStatement sentencia = conexion.prepareStatement(query)) 
        {
            sentencia.setTimestamp(1, horaFinalizacion);
            sentencia.setString(2, "Finalizada");
            sentencia.setInt(3, idPartida);

            sentencia.executeUpdate();
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