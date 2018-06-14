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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private ServerSocket sc;
    private final int PUERTO = 5000;
    private int jugador = 0, ultimo = 0;
    // Lista encargada de agregar a los clientes.
    private List<Cliente> listaClientes = new ArrayList<>();
    // Lista encargada de agregar las puntuaciones de todos los jugadores.
    private List<Integer> puntuacion = new ArrayList<>();
    //Lista encargada de agregar jugadores que ganaron para declarar un empate.
    private List<Boolean> empate = new ArrayList<>();
    // Lista encargada de agregar a los jugadores que desean vovler a jugar.
    private List<String> listareiniciar = new ArrayList<>();
    String nom = "";

    /**
     * Metodo encargado de inicar el servidor y aceptar a los clientes.
     */
    public void iniciar() {
        /**
         * Hilo encargado de aceptar los peticiones de los clientes, distribuir
         * los mensajes de quien se une y añadirlos a la listaClientes.
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sc = new ServerSocket(PUERTO);

                    while (true) {
                        Socket so = sc.accept();

                        Cliente cliente = new Cliente(so);

                        distribuirMensaje(cliente.getNombre() + " Se ha unido", cliente);
                        listaClientes.add(cliente);
                    }

                } catch (IOException ex) {

                }
            }
        }).start();
    }

    /**
     * Se evalua al cliente que envio ese mensaje, con el proposito de no
     * enviarle el mensaje al mismo cliente que lo envia.
     *
     * @param mensaje mensaje a enviar.
     * @param cliente cliente que envio ese mensaje.
     */
    public void distribuirMensaje(String mensaje, Cliente cliente) {
        for (Cliente clienteEnviar : listaClientes) {
            if (clienteEnviar != cliente) {
                clienteEnviar.enviar(mensaje);
            }
        }
    }

    /**
     * Metodo encargado de evular los turnos de todos los jugadores.
     */
    public void Turnos() {
        /**
         * Primero se evalua si el jugador no es igual al utlimo jugador de la
         * lista en caso de no serlo, se envia un mensaje al siguiente cliente
         * con el contenido es tu turno.
         */
        if (jugador < listaClientes.size() - 1) {
            jugador++;
            listaClientes.get(jugador).enviar("Es tu turno");
        } /**
         * En caso de ser igual se sabe que todos los juadores ya tuvieron su
         * turno, primero se obtiene la puntuacion mayor y se comparan todas las putnuaciones
         * despues de evalaur todos los clientes se termina el for reach y se manada a llamar evaluar..
         */
        else if (jugador == listaClientes.size() - 1 && jugador != 0) {
            Integer puntuacionMaxima = Collections.max(puntuacion);
            ultimo = 0;
            for (Cliente listaCliente : listaClientes) {
                if (puntuacion.get(ultimo) < puntuacionMaxima) {
                    listaCliente.gano = false;
                } else if (puntuacion.get(ultimo) == puntuacionMaxima) {
                    listaCliente.gano = true;
                    empate.add(listaCliente.gano);
                }
                ultimo++;
            }
            evaluar();
        }

    }

    /**
     * En caso de que un jugador abandone la partidqa se informa a los demas
     * jugadores y se elimina de la listaClientes.
     *
     * @param cliente
     */
    public void perdioConexion(Cliente cliente) {
        distribuirMensaje(cliente.getNombre() + "Abandono la partida", cliente);
        listaClientes.remove(cliente);
    }

    /**
     * Metodo encargado de evaluar todas las putnuaciones de la lista puntuacion
     * y enviar los mensajes correspondientes: perdio, empato y gano.
     *
     */
    public void evaluar() {
        /**
         * Se recorre todos los clientes comparando
         */
        for (Cliente listaCliente : listaClientes) {
            if (listaCliente.gano == false) {
                listaCliente.enviar("Usted perdio");
            } else if (listaCliente.gano == true) {
                if(empate.size() > 1){
                    listaCliente.enviar("Usted empato");
                }else if(empate.size() == 1){
                    listaCliente.enviar("Usted gano");
                }
            }
        }

    }

    class Cliente {

        private final String nombre;
        private DataOutputStream salida;
        private DataInputStream entrada;
        private boolean gano = false;
        private Socket so;
        private Cliente cliente;

        /**
         * Clase encargado de gestionar los procesos de los clientes.
         *
         * @param s
         */
        public Cliente(Socket s) {
            cliente = this;
            /**
             * Se obtienen el socket que se paso como parametro y se crean los
             * canales de comunicación.
             */
            so = s;
            try {
                salida = new DataOutputStream(so.getOutputStream());
                entrada = new DataInputStream(so.getInputStream());
            } catch (IOException ex) {
            }

            nombre = this.recibir();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String mensaje = recibir();
                        // evaluamos que el mensaje no se null, para asegurarnos que envio un mensaje
                        if (mensaje != null) {
                            /**
                             * Evaluamos si el mensaje es igual a termine para
                             * informar a los demas jugadores que dicho jugador
                             * termino su turno.
                             */
                            if (mensaje.equalsIgnoreCase("termine")) {
                                mensaje = cliente.getNombre() + " termino su turno:";
                                distribuirMensaje(mensaje, cliente);

                            } /**
                             * Evaluamos si el mensaje es igual a reiniciar,
                             * para informarle a los demas jugadores quien desea
                             * volver a jugar.
                             */
                            else if (mensaje.equalsIgnoreCase("reiniciar")) {
                                mensaje = cliente.getNombre() + " acepto volver a jugar";
                                distribuirMensaje(mensaje, cliente);
                            }/**
                             * Se evalua si el mensaje es igual a si, esto
                             * significa que el cliente acepto reiniciar el
                             * juego, con esto lo agregamos a la lista reiniciar
                             * y reiniciamos los valores De todas las otras
                             * listas.
                             */
                            else if (mensaje.equals("Si")) {
                                nom = cliente.getNombre() + mensaje;
                                listareiniciar.add(nom);
                                reiniciar();

                            } /**
                             * Si el mensaje reiniciar es igual a no procedemos
                             * agregarlo a la lista y despues cerrar su socket.
                             * finalmente reiniciamos los valores de todas las
                             * otras listas.
                             */
                            else if (mensaje.equals("No")) {
                                nom = cliente.getNombre() + mensaje;
                                listareiniciar.add(nom);
                                cliente.cerrar();
                                reiniciar();
                            } else if (mensaje.contains("El jugador")) {
                                distribuirMensaje(mensaje, cliente);
                            } /**
                             * Evaluamos si el mensaje es mayor a 0, esto
                             * significa que el jugador envio su puntuacion y lo
                             * agrgamos a la lista de puntuacion.
                             */
                            else if (Integer.parseInt(mensaje) > 0) {
                                puntuacion.add(Integer.parseInt(mensaje));
                                Turnos();
                            }

                        } else {
                            perdioConexion(cliente);
                            break;
                        }
                    }
                }
            }
            ).start();
        }

        // Metodo encargado de recibir mensajes del servidor
        public String recibir() {
            String mensaje = null;
            try {
                mensaje = entrada.readUTF();
            } catch (IOException ex) {
            }

            return mensaje;
        }

        // Metodo encargado de enviar mensajes al servidor
        public void enviar(String mensaje) {
            try {
                salida.writeUTF(mensaje);
            } catch (IOException ex) {
            }
        }

        // Regresa el nombre del cliente.
        public String getNombre() {
            return nombre;
        }

        // Con este metodo cerramos el socket del cliente.
        public void cerrar() {
            try {
                listaClientes.remove(cliente);
                so.close();
            } catch (IOException ex) {
            }
        }

        /**
         * En este metodo evaluamos si la lista reiniciar es del mismo tamaño de
         * la listaClientes, esto significa que todos los clientes ya decidieron
         * si volver a jugar o no. Se reinician todas las listas y algunas
         * variables.
         */
        public void reiniciar() {
            if (listareiniciar.size() == listaClientes.size()) {
                jugador = 0;
                ultimo = 0;
                String nom = "";
                puntuacion.clear();
                listareiniciar.clear();
                empate.clear();
                for (Cliente listaCliente : listaClientes) {
                    listaCliente.enviar("reiniciar");
                }

            }
        }

    }

}
