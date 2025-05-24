package socket_cliente_servidor;

// Importación de clases necesarias para entrada/salida, red y lectura de consola.
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        // Dirección IP del servidor. Debe ser la misma que utiliza el servidor.
        final String HOST = "127.0.0.103";

        // Puerto de conexión. Debe coincidir con el puerto del servidor.
        final int PUERTO = 8014;

        // Scanner para leer datos desde la consola.
        Scanner scanner = new Scanner(System.in);

        try {
            // Crear un socket para conectarse al servidor.
            Socket sc = new Socket(HOST, PUERTO);

            // Flujo de entrada para recibir datos del servidor.
            DataInputStream in = new DataInputStream(sc.getInputStream());

            // Flujo de salida para enviar datos al servidor.
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());

            System.out.println("Conectado al servidor.");

            boolean salir = false;

            // Bucle principal que muestra el menú hasta que el usuario elige salir.
            while (!salir) {
                System.out.println("\n=== MENÚ ===");
                System.out.println("1. Generar nombre de usuario");
                System.out.println("2. Generar dirección de correo electrónico");
                System.out.println("3. Salir");
                System.out.print("Opción: ");

                // Leer la opción ingresada por el usuario.
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer del scanner (evita errores al leer cadenas luego de números).

                switch (opcion) {
                    case 1:
                        // Solicitar longitud para el nombre de usuario.
                        System.out.print("Ingrese la longitud del nombre de usuario (5-20): ");
                        int longitud = scanner.nextInt();
                        scanner.nextLine(); // Limpiar buffer

                        // Enviar el comando al servidor.
                        out.writeUTF("USERNAME:" + longitud);

                        // Leer y mostrar la respuesta del servidor.
                        String respuestaNombre = in.readUTF();
                        System.out.println("Servidor: " + respuestaNombre);
                        break;

                    case 2:
                        // Solicitar al usuario el nombre de usuario para generar un correo.
                        System.out.print("Ingrese su nombre de usuario: ");
                        String nombreUsuario = scanner.nextLine();

                        // Enviar el comando al servidor.
                        out.writeUTF("EMAIL:" + nombreUsuario);

                        // Leer y mostrar la respuesta del servidor.
                        String respuestaCorreo = in.readUTF();
                        System.out.println("Servidor: " + respuestaCorreo);
                        break;

                    case 3:
                        // El usuario quiere salir. Enviar el comando "SALIR" al servidor.
                        out.writeUTF("SALIR");

                        // Leer la respuesta de confirmación del servidor.
                        String respuestaSalida = in.readUTF();
                        System.out.println("Servidor: " + respuestaSalida);

                        // Terminar el bucle.
                        salir = true;
                        System.out.println("Saliendo del cliente...");
                        break;

                    default:
                        // Si el usuario ingresa una opción inválida.
                        System.out.println("Opción inválida.");
                }
            }

            // Cerrar el socket una vez terminada la comunicación.
            sc.close();

        } catch (IOException e) {
            // Mostrar error si falla la conexión o la comunicación.
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
