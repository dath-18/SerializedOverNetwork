package mx.uv.fiee.iinf.paradigmas.networks;

import java.io.IOException;
import mx.uv.fiee.iinf.paradigmas.networks.models.Persona;
import java.io.ObjectInputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Clase principal que recibe objetos serializados desde un socket.
 */
public class Receiver {

    /**
     * Método principal que inicializa la conexión y recibe objetos.
     */
    public static void main(String[] args) {

        SocketUtils utils = new SocketUtils ("localhost", 19000);
        utils.Receive ();

    }

    /**
     * Clase interna para manejar la conexión del socket y la recepción de datos.
     */
    private static class SocketUtils {

        private SSLSocket socket;
        /**
         * Constructor que inicializa un socket cliente para conectarse a un servidor.
         * @param address Dirección del servidor.
         * @param port Puerto del servidor.
         */
        public SocketUtils (String address, int port) {
            try {
                System.setProperty("javax.net.ssl.keyStore", "src/main/resources/miclave.jks");
                System.setProperty("javax.net.ssl.keyStorePassword", "alexis18");
                SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket = (SSLSocket) sf.createSocket(address, port);
            } catch (IOException e) {
                e.printStackTrace ();
                throw new RuntimeException("Error creating socket");
            }
        }

        /**
         * Método para recibir objetos serializados desde el socket.
         */
        public void Receive() {
            try (ObjectInputStream ois = new ObjectInputStream (socket.getInputStream ())) {
                while (true) {
                    try {
                        Persona p = (Persona) ois.readObject (); // Deserialize Persona object
                        System.out.println ("Received UUID: " + p.getUuid ()); // Print UUID
                    } catch (ClassNotFoundException e) {
                        System.err.println ("Class not found: " + e.getMessage ());
                        break;
                    } catch (java.io.EOFException e) {
                        System.err.println ("End of stream reached.");
                        break; // Exit the loop when the stream ends
                    }
                }
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
    }

}
