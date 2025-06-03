package socket_cliente_servidor;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        final String HOST = "localhost";
        final int PUERTO = 2257;

        try (
            Socket socket = new Socket(HOST, PUERTO);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            Scanner teclado = new Scanner(System.in)
        ) {
            System.out.println("Conectado al servidor.");

            boolean continuar = true;

            while (continuar) {
                System.out.println("\nOpciones: cliente java");
                System.out.println("1. Generar nombre de usuario");
                System.out.println("2. Generar correo electrónico");
                System.out.println("3. Salir");
                System.out.print("Elija una opción: ");
                int opcion = teclado.nextInt();
                teclado.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese longitud (5 a 20): ");
                        int longitud = teclado.nextInt();
                        teclado.nextLine();
                        out.writeUTF("USERNAME:" + longitud);
                        System.out.println("Servidor: " + in.readUTF());
                        break;

                    case 2:
                        System.out.print("Ingrese nombre de usuario: ");
                        String nombre = teclado.nextLine();
                        out.writeUTF("EMAIL:" + nombre);
                        System.out.println("Servidor: " + in.readUTF());
                        break;

                    case 3:
                        out.writeUTF("SALIR");
                        System.out.println("Servidor: " + in.readUTF());
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            }

        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}
