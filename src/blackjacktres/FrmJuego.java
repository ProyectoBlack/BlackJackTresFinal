/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjacktres;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author missa
 */
public class FrmJuego extends javax.swing.JFrame {

    public boolean terminado = false;
    private String nombre;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private Carta cartas;
    private Carta carta[] = new Carta[52];
    private Mano[] manoJugador = new Mano[2];
    private Baraja baraja = new Baraja();
    private int contadorMano = 2, puntos = 0, k = 0;

    /**
     * Creates new form FrmJuego
     */
    public FrmJuego(Socket s, String Nombre) {
        initComponents();

        try {
            entrada = new DataInputStream(s.getInputStream());
            salida = new DataOutputStream(s.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(FrmJuego.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.nombre = Nombre;
        setTitle("Jugador - " + nombre);
        enviar(nombre);
        bloquearBotones();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String mensaje = recibir();

                    if (mensaje != null) {
                        if ("Es tu turno".equalsIgnoreCase(mensaje)) {
                            desbloquearBotones();
                        }
                        txtMensajes.setText(txtMensajes.getText() + "\n" + mensaje);

                    } else {
                        txtMensajes.setText(txtMensajes.getText() + "\nServer is disconnected.");
                        break;
                    }
                }
            }
        }).start();

        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 12; j++) {
                cartas = new Carta(i, j);
                carta[k] = cartas;
                k++;
            }
            baraja.barajar();
        }
        setvisibleCartas();
        evaluar();
    }

    public FrmJuego() {

        initComponents();

        String nombre = "tugfa";
        setTitle("Jugador - " + nombre);

        Servidor serv = new Servidor();
        serv.iniciar();

        try {
            Socket so = new Socket("", 5000);

            entrada = new DataInputStream(so.getInputStream());
            salida = new DataOutputStream(so.getOutputStream());

            enviar(nombre);
        } catch (IOException ex) {
            System.out.println("No se pudo");;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String mensaje = recibir();

                    if (mensaje != null) {
                        if (mensaje.equalsIgnoreCase("termine")) {

                        }
                        txtMensajes.setText(txtMensajes.getText() + "\n" + mensaje);
                    } else {
                        txtMensajes.setText(txtMensajes.getText() + "\nServer is disconnected.");
                        break;
                    }
                }
            }
        }).start();

        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 12; j++) {
                cartas = new Carta(i, j);
                carta[k] = cartas;
                k++;
            }
            baraja.barajar();
        }

        setvisibleCartas();
        evaluar();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCarta2 = new javax.swing.JLabel();
        lblCarta1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnRobar = new javax.swing.JButton();
        btnQuedarse = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMensajes = new javax.swing.JTextArea();
        lblCarta3 = new javax.swing.JLabel();
        lblCarta5 = new javax.swing.JLabel();
        lblCarta4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblCarta2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCarta1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        btnRobar.setText("Robar");
        btnRobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRobarActionPerformed(evt);
            }
        });

        btnQuedarse.setText("Quedarse");
        btnQuedarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuedarseActionPerformed(evt);
            }
        });

        txtMensajes.setEditable(false);
        txtMensajes.setColumns(20);
        txtMensajes.setLineWrap(true);
        txtMensajes.setRows(5);
        jScrollPane1.setViewportView(txtMensajes);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRobar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnQuedarse, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRobar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addComponent(btnQuedarse, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        lblCarta3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCarta5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCarta4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCarta1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCarta2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCarta3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCarta4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCarta5, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCarta2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCarta1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCarta3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCarta4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCarta5, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRobarActionPerformed
        if (contadorMano < 5) {
            for (int i = 0; i < 1; i++) {
                manoJugador[i].cogerCarta(baraja.robar());
                //  lblCarta1.setText(manoJugador[i].obtenerCarta(i) + "");
                if (contadorMano + 1 == 3) {
                    lblCarta3.setVisible(true);
                    lblCarta3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/"+manoJugador[i].obtenerCarta(i+2)+".jpg")));
                    System.out.println(manoJugador[i].obtenerCarta(manoJugador[i].contar() - 1) + "");
                } else if (contadorMano + 1 == 4) {
                    lblCarta4.setVisible(true);
                    lblCarta4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/"+manoJugador[i].obtenerCarta(i+3)+".jpg")));
                    System.out.println(manoJugador[i].obtenerCarta(manoJugador[i].contar() - 1) + "");
                } else if (contadorMano + 1 == 5) {
                    lblCarta5.setVisible(true);
                    lblCarta5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/"+manoJugador[i].obtenerCarta(i+4)+".jpg")));
                    System.out.println(manoJugador[i].obtenerCarta(manoJugador[i].contar() - 1) + "");
                }

//            lblCarta2.setIcon(new javax.swing.ImageIcon(getClass().getResource
//        ("/Imagenes/"+manoJugador[i].obtenerCarta(i)+".jpg")));
                contadorMano++;
                evaluar();
            }

        } else {
            btnRobar.setEnabled(false);
            enviar("termine");
            enviar("21");
        }
    }//GEN-LAST:event_btnRobarActionPerformed

    private void btnQuedarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuedarseActionPerformed
        btnRobar.setEnabled(false);
        btnQuedarse.setEnabled(false);
        txtMensajes.setText(txtMensajes.getText() + "\n" + "Usted tiene " + manoJugador[0].getBlackjackValor());
        terminado = true;
        bloquearBotones();
        enviar("termine");
        enviar(String.valueOf(manoJugador[0].getBlackjackValor()));
    }//GEN-LAST:event_btnQuedarseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnQuedarse;
    private javax.swing.JButton btnRobar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCarta1;
    private javax.swing.JLabel lblCarta2;
    private javax.swing.JLabel lblCarta3;
    private javax.swing.JLabel lblCarta4;
    private javax.swing.JLabel lblCarta5;
    private javax.swing.JTextArea txtMensajes;
    // End of variables declaration//GEN-END:variables

    public void enviar(String mensaje) {
        try {
            salida.writeUTF(mensaje);
        } catch (IOException ex) {
            System.err.println("Sucedió un error al enviar");
        }
    }

    public String recibir() {
        String mensaje = null;
        try {
            mensaje = entrada.readUTF();
        } catch (IOException ex) {
            System.err.println("Sucedió un error al recibir");
        }

        return mensaje;
    }

    public void bloquearBotones() {
        btnRobar.setEnabled(false);
        btnQuedarse.setEnabled(false);
    }

    public void desbloquearBotones() {
        btnRobar.setEnabled(true);
        btnQuedarse.setEnabled(true);
    }

    private void setvisibleCartas() {

        for (int i = 0; i < 1; i++) {

            manoJugador[i] = new Mano();

            manoJugador[i].cogerCarta(baraja.robar());
            manoJugador[i].cogerCarta(baraja.robar());

            System.out.println(manoJugador[i].obtenerCarta(i) + "");
            System.out.println(manoJugador[i].obtenerCarta(i + 1) + "");

            lblCarta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/" + manoJugador[i].obtenerCarta(i) + ".jpg")));
            lblCarta2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/" + manoJugador[i].obtenerCarta(i+1) + ".jpg")));
        }
        lblCarta3.setVisible(false);
        lblCarta4.setVisible(false);
        lblCarta5.setVisible(false);
    }

    private void evaluar() {
        if (manoJugador[0].getBlackjackValor() == 21) {
            btnRobar.setEnabled(false);
            txtMensajes.setText(txtMensajes.getText() + "\n" + "Tienes Black Jack... Esperando a los demas jugadores");
        } else if (manoJugador[0].getBlackjackValor() > 21) {
            bloquearBotones();
            txtMensajes.setText(txtMensajes.getText() + "\n" + "Usted a perdido... Esperando a los demas jugadores");
            enviar("termine");
            enviar(String.valueOf(1));
        }

    }

}