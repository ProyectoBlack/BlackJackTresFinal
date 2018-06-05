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
    private List<Cliente> listaClientes = new ArrayList();
    private List<Integer> puntuacion = new ArrayList();
    private List<Boolean> ganadores = new ArrayList();
    private List<Boolean> ganadoressin21 = new ArrayList();

    public void iniciar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sc = new ServerSocket(PUERTO);
                    System.out.println("El servidor está iniciado, esperando conexiones...");

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

    public void distribuirMensaje(String mensaje, Cliente cliente) {
        System.out.println(mensaje);
        for (Cliente clienteEnviar : listaClientes) {
            if (clienteEnviar != cliente) {
                clienteEnviar.enviar(mensaje);
            }
        }
    }

    public void Turnos() {
        if (jugador < listaClientes.size() - 1) {
            jugador++;
            listaClientes.get(jugador).enviar("Es tu turno");
        } else if (jugador == listaClientes.size() - 1 && jugador != 0) {
            evaluar();
        }

    }

    public void perdioConexion(Cliente cliente) {
        distribuirMensaje(cliente.getNombre() + "Abandono la partida", cliente);
        listaClientes.remove(cliente);
    }

    public void evaluar() {
        Integer i = Collections.max(puntuacion);
        for (Cliente listaCliente : listaClientes) {
            if (listaCliente.gano == true && ganadores.size() > 1) {
                listaCliente.enviar("Usted empato");
            } else if (listaCliente.gano == true && ganadores.size() == 1) {
                listaCliente.enviar("Usted gano");
            } else if (listaCliente.gano == false && ganadores.size() > 0) {
                listaCliente.enviar("Usted ha perdido");
            }
            if (ganadores.size() == 0) {
                if (puntuacion.get(ultimo) < i) {
                    listaCliente.gano = false;
                    listaCliente.enviar("Usted perdio");
                } else if (puntuacion.get(ultimo) == i) {
                    listaCliente.gano = true;
                    ganadoressin21.add(listaCliente.gano);
                }
            }
            ultimo++;
        }
        for (Cliente listaCliente : listaClientes) {
            if (ganadoressin21.size() > 1 && listaCliente.gano == true) {
                listaCliente.enviar("Usted empato");
            } else if (ganadoressin21.size() == 1 && listaCliente.gano == true) {
                listaCliente.enviar("Usted gano");
            }
        }

    }

    class Cliente {

        private final String nombre;
        private DataOutputStream salida;
        private DataInputStream entrada;
        private boolean gano = false;

        public Cliente(Socket s) {
            Cliente cliente = this;

            try {
                salida = new DataOutputStream(s.getOutputStream());
                entrada = new DataInputStream(s.getInputStream());
            } catch (IOException ex) {
            }

            nombre = this.recibir();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String mensaje = recibir();

                        if (mensaje != null) {
                            if (mensaje.equalsIgnoreCase("termine")) {
                                mensaje = cliente.getNombre() + " termino su turno:";
                                distribuirMensaje(mensaje, cliente);

                            } else if (Integer.parseInt(mensaje) > 0) {
                                if (Integer.parseInt(mensaje) == 21) {
                                    gano = true;
                                    ganadores.add(gano);
                                }
                                puntuacion.add(Integer.parseInt(mensaje));
                                Turnos();
                            }
                        } else {
                            perdioConexion(cliente);
                            break;
                        }
                    }
                }
            }).start();
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

        public void enviar(String mensaje) {
            try {
                salida.writeUTF(mensaje);
            } catch (IOException ex) {
                System.err.println("Sucedió un error al enviar");
            }
        }

        public String getNombre() {
            return nombre;
        }

    }
}
