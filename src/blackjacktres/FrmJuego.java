/**
 * a)
 * Missael Flores González 16410118.
 * Osvaldo Bosquez Garcia 16410108.
 *
 * b)
 * 12/06/2018
 *
 * c)
 * 52 horas aprox.
 *
 * d)
 * En este proyecto tuvimos 3 principales problemas.
 * 1) No sabiamos como empezar.
 * Al principio no entendiamos muy bien como comenzar a trabajar, queriamos hacer
 * todo al mismo tiempo, esto creo muchos problemas en el codigo hasta que decidimos
 * volver a empezar.
 *
 * 2) Volver a comenzar.
 * Una vez que decidimos volver a empezar todo fue más facil, ya que ahora
 * nos enfocamos en completar tareas especificas por dia. De igual forma, esto
 * fue un contratiempo ya que volvimos a empezar de cero.
 *
 * 3) Pensar.
 * Sin duda una de los mayores contratiempos fue el echo de pensar en formas
 * de que el codigo funcionaram, en ocasiones nos tamaba todo un dia o dos
 * pensar en alguna forma de hacerlo funcionar.
 *
 * e)
 * Este proyecto sin duda alguna nos ayudo mucho a comprender la programación en
 * java y reafirmar nuestros conocimientos previamente adquiridos.
 * De igual forma pude aquirir nuevos conocimientos y nos ayudo a desarrollar
 * un pensamiento analitico para la solución de problemas dificiles.
 *
 * f)
 *
 * codigo que fue usado para la base de nuestro proyecto.
 * https://www.taringa.net/posts/ciencia-educacion/15381041/Codigo-de-Blackjack-en-java-netbeans.html
 *
 * Codigo para obtener el maximo valor de una lista.
 * https://stackoverflow.com/questions/8304767/how-to-get-maximum-value-from-the-list-arraylist
 *
 * Codigo para borrar todos los elementos de una lista.
 * https://stackoverflow.com/questions/17279519/removing-items-from-a-list
 *
 */
package blackjacktres;

import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author missa
 */
public class FrmJuego extends javax.swing.JFrame {

    /**
     * Declaramos todas las variables que utlizamos para la creacion de los
     * distintos metodo o precesos.
     */
    private boolean terminado = false;
    private String nombre, servcli;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private Carta cartas;
    private Carta carta[] = new Carta[52];
    private Mano[] manoJugador = new Mano[2];
    private Baraja baraja = new Baraja();
    private int contadorMano = 2, puntos = 0, k = 0;
    String mensaje = "";

    /**
     * Constructor encargado de fungir como cliente, se inicia pidiendo como
     * parametros el socket y nombre del cliente. En este constructor nos
     * encargamos de mandar a llamar todos los metodos y procesos requeridos
     * para que el cliente pueda comunicarse con los demas clientes.
     */
    public FrmJuego(Socket s, String Nombre) {
        initComponents();
        setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("icono\\icon.png"));
        lblIp.setVisible(false);
        // Inicializamos la variable servcli con el valor de "cliente", esto nos
        // servira para evaluar los turnos al momento de reiniciar el jeugo
        servcli = "cliente";

        try {
            entrada = new DataInputStream(s.getInputStream());
            salida = new DataOutputStream(s.getOutputStream());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro al establecer la comunicacion con el servidor");
        }

        this.nombre = Nombre;
        // Ponemos como titulo del frame el nombre que nos pasaron como parametro
        // y tambien lo enviamos al servidor para informar a los demas usuarios
        // que nos hemos unido a la partida.
        setTitle("Jugador - " + nombre);
        enviar(nombre);
        bloquearBotones();

        /**
         * Hilo encargado de recibir y procesar todos los mensajes entrantes
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mensaje = recibir();

                    if (mensaje != null) {
                        if ("Es tu turno".equalsIgnoreCase(mensaje)) {
                            desbloquearBotones();
                        }
                        if (recibireiniciar(mensaje)) {
                        } else {

                            txtMensajes.setText(txtMensajes.getText() + "\n" + mensaje);
                        }

                    } else {
                        txtMensajes.setText(txtMensajes.getText() + "\n Se ha perdido la conexion con el servidor");
                        break;
                    }
                }
            }
        }).start();

        /**
         * Nos encargamos de barjar las carta, ponerlas como visibles y evaluar
         * en caso de que al jugador tenga 21 desde el comienzo de la partida.
         */
        baraja.barajar();
        setvisibleCartas();
        evaluar();
    }

    /**
     * Constructor encargado de gestionar al servidor y de igual forma jugara
     * como cliente.
     */
    public FrmJuego() {

        initComponents();
        while(nombre == null || nombre.trim().isEmpty()){
             nombre = JOptionPane.showInputDialog(this, "Se requiere tu nombre para continuar");
        } 
         

        this.setIconImage(Toolkit.getDefaultToolkit().getImage("icono\\icon.png"));
        // Ponemos como titulo del frame el nombre del servidor 
        setTitle("Jugador - " + nombre);
        // Inicializamos la variable servcli con el valor de "servidor", esto nos
        // servira para evaluar los turnos al momento de reiniciar el jeugo
        servcli = "servidor";

        //iniciamos el servidor.
        Servidor serv = new Servidor();
        serv.iniciar();

        /**
         * Construimos un socket de cliente, con la finalidad de gestionar el
         * frame igual que como si no fuera servidor, de esta forma el servidor
         * solo se encarga de recibir, evular y distribuir mensajes.
         */
        try {
            Socket so = new Socket("", 5000);
            entrada = new DataInputStream(so.getInputStream());
            salida = new DataOutputStream(so.getOutputStream());
            enviar(nombre); 
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Hubo un problema al establecer la comunicacion con el servidor");
        }
        
 
        


        lblIp.setText("Direccion ip " + obtenerIPsLocales());
        bloquearBotones();
        /**
         * Hilo encargado de recibir y procesar todos los mensajes entrantes
         */
        new Thread(new Runnable() {
            int mensajes = 0;

            @Override
            public void run() {
                while (true) {
                    mensaje = recibir();

                    if (mensaje != null) {
//                        if (mensaje.equalsIgnoreCase("termine") || mensaje.equalsIgnoreCase("reiniciar")) {
//
//                        }
                        if (recibireiniciar(mensaje)) {
                        } else {

                            txtMensajes.setText(txtMensajes.getText() + "\n" + mensaje);
                            mensajes++;
                            if (mensajes == 1) {
                                desbloquearBotones();
                            }
                        }

                    } else {
                        txtMensajes.setText(txtMensajes.getText() + "\n El servidor se desconecto de la partida.");
                        break;
                    }
                }
            }
        }).start();

        /**
         * Nos encargamos de barjar las carta, ponerlas como visibles y evaluar
         * en caso de que al jugador tenga 21 desde el comienzo de la partida.
         */
        baraja.barajar();
        setvisibleCartas();
        evaluar();
    }

    /**
     * Metodo encargado de recibir el mensaje de ganar, empatar y perder y
     * preguntar al usuario si desea volver a jugar, dependiendo de su respuesta
     * se envia al servidor, en caso elegir la opcion si el servidor regresa un
     * mensaje con la string reiniciar que al ser evaluada reinicia el juego.
     */
    private boolean recibireiniciar(String mensaje) {
        boolean si = false;
        if (mensaje.equalsIgnoreCase("Usted empato") || mensaje.equalsIgnoreCase("Usted gano") || mensaje.equalsIgnoreCase("Usted perdio")) {
            Object[] opciones = new Object[2];
            opciones[0] = "No";
            opciones[1] = "Si";
            String reiniciar = "";
            int resp = JOptionPane.showOptionDialog(rootPane, mensaje + " \n Deseas Volver a jugar", "Volver a jugar", puntos, HEIGHT, null, opciones, opciones[0]);
            if (resp == 1) {
                reiniciar = "Si";
                enviar("reiniciar");
            } else if (resp == 0) {
                reiniciar = "No";
                bloquearBotones();
            }
            enviar(reiniciar);
            si = true;
        } else if (mensaje.equalsIgnoreCase("reiniciar")) {
            reiniciar();
            si = true;
        } else {
            si = false;
        }
        return si;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblCarta1 = new javax.swing.JLabel();
        lblCarta2 = new javax.swing.JLabel();
        lblCarta3 = new javax.swing.JLabel();
        lblCarta5 = new javax.swing.JLabel();
        lblCarta4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnRobar = new javax.swing.JButton();
        btnQuedarse = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMensajes = new javax.swing.JTextArea();
        lblIp = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BlackJack");
        setBackground(new java.awt.Color(0, 102, 0));
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(0, 102, 0));

        lblCarta1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCarta2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCarta3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCarta5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCarta4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel1.setBackground(new java.awt.Color(0, 153, 102));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        btnRobar.setBackground(new java.awt.Color(255, 255, 255));
        btnRobar.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        btnRobar.setMnemonic('R');
        btnRobar.setText("Robar");
        btnRobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRobarActionPerformed(evt);
            }
        });

        btnQuedarse.setBackground(new java.awt.Color(255, 255, 255));
        btnQuedarse.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        btnQuedarse.setMnemonic('Q');
        btnQuedarse.setText("Quedarse");
        btnQuedarse.setToolTipText("");
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnQuedarse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRobar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        lblIp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblIp.setForeground(new java.awt.Color(255, 255, 255));
        lblIp.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblCarta1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCarta2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCarta3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCarta4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCarta5, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 14, Short.MAX_VALUE))
                    .addComponent(lblIp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCarta3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCarta1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCarta2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCarta4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCarta5, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblIp))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Boton encargado de robar cartas.
     *
     * @param evt
     */
    private void btnRobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRobarActionPerformed
        /**
         * Evaluamos el contador de contador de cartas, ya que es el que nos
         * indica cuantas cartas tiene el ciente y con esto decidimos si puede o
         * no robar mas ya que el limite son 5 cartas.
         *
         */
        if (contadorMano < 5) {

            manoJugador[0].cogerCarta(baraja.robar());

            /**
             * Inicialmente tenemos 5 labels, pero solo 2 visibles, con
             * cantdorMano podemos saber cual label poner visible y despues con
             * el metodo obtener carta obtenemos el nombre de la carta y con la
             * extension png ponemos la imagen de fondo. Despues de eso mandamos
             * a llamar evaluar para asegurarnos que despues de robar no se ha
             * pasado de 21.
             */
            if (contadorMano + 1 == 3) {
                lblCarta3.setVisible(true);
                lblCarta3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/" + manoJugador[0].obtenerCarta(2) + ".jpg")));
            } else if (contadorMano + 1 == 4) {
                lblCarta4.setVisible(true);
                lblCarta4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/" + manoJugador[0].obtenerCarta(3) + ".jpg")));
            } else if (contadorMano + 1 == 5) {
                lblCarta5.setVisible(true);
                lblCarta5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/" + manoJugador[0].obtenerCarta(4) + ".jpg")));
            }
            contadorMano++;
            evaluar();

            /**
             * En caso de que el usuario robo 5 cartas y no se paso de 21
             * terminamos su turno y mandamos el valor 21.
             */
        } else {
            terminado = true;
            bloquearBotones();
            enviar("El jugador " + nombre + " se quedo con " + contadorMano + " cartas");
            enviar("termine");
            enviar("21");
        }
    }//GEN-LAST:event_btnRobarActionPerformed

    /**
     * En el boton quedarse el se le bloquean los botones al usuario terminamos
     * su turno y le avisamos a los demas jugadores, Tambien le decimos el valor
     * de sus cartas y le enviamos el valor al servidor.
     *
     * @param evt
     */
    private void btnQuedarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuedarseActionPerformed
        btnRobar.setEnabled(false);
        btnQuedarse.setEnabled(false);
        txtMensajes.setText(txtMensajes.getText() + "\n" + "Usted tiene " + manoJugador[0].getBlackjackValor());
        terminado = true;
        bloquearBotones();
        enviar("termine");
        enviar("El jugador " + nombre + " se quedo con " + contadorMano + " cartas");
        enviar(String.valueOf(manoJugador[0].getBlackjackValor()));
    }//GEN-LAST:event_btnQuedarseActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnQuedarse;
    private javax.swing.JButton btnRobar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCarta1;
    private javax.swing.JLabel lblCarta2;
    private javax.swing.JLabel lblCarta3;
    private javax.swing.JLabel lblCarta4;
    private javax.swing.JLabel lblCarta5;
    private javax.swing.JLabel lblIp;
    private javax.swing.JTextArea txtMensajes;
    // End of variables declaration//GEN-END:variables

    /**
     * Metodo encargado de enviar mensajes al servidor
     *
     * @param mensaje
     */
    public void enviar(String mensaje) {
        try {
            salida.writeUTF(mensaje);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Sucedió un error al enviar informacion al servidor");
        }
    }

    /**
     * Metodo encargador de recibir mensajes por parte del servidor
     *
     * @return
     */
    public String recibir() {
        String mensaje = null;
        try {
            mensaje = entrada.readUTF();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Sucedió un error al recibir informacion del servidor");
        }
        return mensaje;
    }

    /**
     * Metodo encargado de bloquear los botones
     */
    public void bloquearBotones() {
        btnRobar.setEnabled(false);
        btnQuedarse.setEnabled(false);
    }

    /**
     * Metodo encargado de habilitar los botones
     */
    public void desbloquearBotones() {
        btnRobar.setEnabled(true);
        btnQuedarse.setEnabled(true);
    }

    /**
     * En este metodo se pone el valor y la imagen de las dos primeras label y
     * ponemos invisibles las otras 3.
     */
    private void setvisibleCartas() {

        for (int i = 0; i < 1; i++) {
            manoJugador[i] = new Mano();

            manoJugador[i].cogerCarta(baraja.robar());
            manoJugador[i].cogerCarta(baraja.robar());

            lblCarta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/" + manoJugador[i].obtenerCarta(i) + ".jpg")));
            lblCarta2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/" + manoJugador[i].obtenerCarta(i + 1) + ".jpg")));
        }
        lblCarta3.setVisible(false);
        lblCarta4.setVisible(false);
        lblCarta5.setVisible(false);
    }

    /**
     * Metodo encarado de evaluar si el usario tiene 21 o si ya se paso, sea
     * cual sea el caso se le informa al cliente y se procede a terminar su
     * turno
     */
    private void evaluar() {
        if (manoJugador[0].getBlackjackValor() == 21) {
            btnRobar.setEnabled(false);
            txtMensajes.setText(txtMensajes.getText() + "\n" + "Tienes Black Jack");
        } else if (manoJugador[0].getBlackjackValor() > 21) {
            bloquearBotones();
            txtMensajes.setText(txtMensajes.getText() + "\n" + "Usted a perdido... Esperando a los demas jugadores");
            enviar("termine");
            enviar(String.valueOf(1));
            enviar("El jugador " + nombre + " se quedo con " + contadorMano + " cartas");
        }

    }

    /**
     * Metodo encargado de obtener las ip para que el cliente pueda conectarse
     * por medio de red al servidor.
     *
     * @return
     */
    public String obtenerIPsLocales() {
        String ip = "";
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();

                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    String nombre = n.getName();

                    if (i instanceof Inet4Address && (nombre.startsWith("eth") || nombre.startsWith("wlan"))) {
                        if (!ip.isEmpty()) {
                            ip += ", ";
                        }

                        ip += i.getHostAddress() + " (" + nombre + ")";
                    }
                }
            }
        } catch (Exception ex) {

        }

        return ip;
        // TODO: Completar este método... 
    }

    /**
     * Metodo encargado de barajar las cartas de nuevo poner el contadormano de
     * nuevo a 2 y mandar a llamar setvisibleCartas para volver a empezar el
     * juego. Tambien le desbloquea los botones solo al servidor ya que es el
     * que siempre empieza.
     */
    private void reiniciar() {
        baraja.barajar();
        contadorMano = 2;
        setvisibleCartas();
        if (servcli.equals("servidor")) {
            desbloquearBotones();
        }
    }
}
