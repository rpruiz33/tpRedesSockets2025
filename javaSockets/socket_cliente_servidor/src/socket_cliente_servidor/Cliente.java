package socket_cliente_servidor;

// Importación de clases necesarias para manejo de entradas/salidas, sockets y utilidades.
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Servidor {

    // Lista de dominios válidos para los correos electrónicos generados.
    static final String[] DOMINIOS_VALIDOS = {"@gmail.com", "@hotmail.com"};
    
    // Letras vocales y consonantes que se utilizarán para validar/generar nombres de usuario.
    static final String VOCALES = "aeiou";
    static final String CONSONANTES = "bcdfghjklmnpqrstvwxyz";

    public static void main(String[] args) {
        // Puerto en el que el servidor estará escuchando conexiones.
        final int PUERTO = 8014;

        // Bloque try-with-resources que asegura el cierre automático del servidor cuando termine.
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado en el puerto " + PUERTO);
            
            // Bucle infinito que permite que el servidor acepte múltiples clientes secuencialmente.
            while (true) {
                System.out.println("Esperando cliente...");
                
                // Espera y acepta la conexión de un cliente.
                try (
                    Socket sc = servidor.accept(); // Socket de comunicación con el cliente.
                    DataInputStream in = new DataInputStream(sc.getInputStream()); // Flujo de entrada desde el cliente.
                    DataOutputStream out = new DataOutputStream(sc.getOutputStream()) // Flujo de salida hacia el cliente.
                ) {
                    System.out.println("Cliente conectado.");

                    boolean continuar = true;
                    // Ciclo que permite comunicación con el cliente mientras este lo desee.
                    while (continuar) {
                        String mensaje = in.readUTF(); // Se lee un mensaje enviado por el cliente.

                        // Si el mensaje comienza con "USERNAME:", se debe generar un nombre de usuario.
                        if (mensaje.startsWith("USERNAME:")) {
                            int longitud = Integer.parseInt(mensaje.substring(9)); // Se extrae la longitud deseada.
                            if (longitud < 5 || longitud > 20) {
                                // Validación de la longitud.
                                out.writeUTF("Error: la longitud debe estar entre 5 y 20.");
                            } else {
                                // Genera el nombre de usuario válido y lo envía al cliente.
                                String username = generarNombreUsuario(longitud);
                                out.writeUTF("Nombre de usuario generado: " + username);
                            }

                        // Si el mensaje comienza con "EMAIL:", se debe generar un correo con el nombre dado.
                        } else if (mensaje.startsWith("EMAIL:")) {
                            String nombreUsuario = mensaje.substring(6).toLowerCase(); // Se extrae y convierte el nombre a minúsculas.
                            // Verifica que el nombre sea válido antes de generar el correo.
                            if (!esNombreUsuarioValido(nombreUsuario)) {
                                out.writeUTF("Error: el nombre de usuario debe tener entre 5 y 20 letras, al menos una vocal y una consonante.");
                            } else {
                                // Genera un correo con dominio aleatorio.
                                String correo = nombreUsuario + obtenerDominioAleatorio();
                                out.writeUTF("Correo electrónico generado: " + correo);
                            }

                        // Si el mensaje es "SALIR", se desconecta al cliente.
                        } else if (mensaje.equals("SALIR")) {
                            out.writeUTF("Desconectando...");
                            continuar = false;

                        // Si no se reconoce el comando recibido.
                        } else {
                            out.writeUTF("Comando no reconocido.");
                        }
                    }

                // Captura errores durante la comunicación con el cliente.
                } catch (IOException e) {
                    System.err.println("Error con el cliente: " + e.getMessage());
                }

                System.out.println("Cliente desconectado.");
            }

        // Captura errores al iniciar el servidor.
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    /**
     * Genera un nombre de usuario con la longitud especificada.
     * Asegura al menos una vocal y una consonante en el nombre.
     */
    private static String generarNombreUsuario(int longitud) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        // Agrega una vocal y una consonante obligatoriamente.
        sb.append(VOCALES.charAt(rand.nextInt(VOCALES.length())));
        sb.append(CONSONANTES.charAt(rand.nextInt(CONSONANTES.length())));

        // Llena el resto de la cadena con letras aleatorias.
        String letras = VOCALES + CONSONANTES;
        for (int i = 2; i < longitud; i++) {
            sb.append(letras.charAt(rand.nextInt(letras.length())));
        }

        // Mezcla los caracteres del nombre para no dejar vocal y consonante fijas al principio.
        return mezclarCadena(sb.toString());
    }

    /**
     * Mezcla aleatoriamente los caracteres de una cadena.
     */
    private static String mezclarCadena(String input) {
        char[] a = input.toCharArray();
        Random rand = new Random();
        for (int i = 0; i < a.length; i++) {
            int j = rand.nextInt(a.length); // Índice aleatorio para intercambio.
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        return new String(a);
    }

    /**
     * Verifica que el nombre de usuario sea válido:
     * - Solo letras.
     * - Longitud entre 5 y 20.
     * - Al menos una vocal y una consonante.
     */
    private static boolean esNombreUsuarioValido(String nombre) {
        if (nombre.length() < 5 || nombre.length() > 20 || !nombre.matches("[a-zA-Z]+")) return false;

        // Verifica si tiene al menos una vocal.
        boolean tieneVocal = nombre.chars().anyMatch(c -> VOCALES.indexOf(c) >= 0);
        // Verifica si tiene al menos una consonante.
        boolean tieneConsonante = nombre.chars().anyMatch(c -> CONSONANTES.indexOf(c) >= 0);
        return tieneVocal && tieneConsonante;
    }

    /**
     * Devuelve aleatoriamente uno de los dominios válidos definidos.
     */
    private static String obtenerDominioAleatorio() {
        Random rand = new Random();
        return DOMINIOS_VALIDOS[rand.nextInt(DOMINIOS_VALIDOS.length)];
    }
}
