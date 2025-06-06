package socket_cliente_servidor;

import java.io.*;
import java.net.*;//la clase socket viene incorporada desde esta libreria
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        final String HOST = "localhost";//localhost indica donde se esta corriendo el programa
        final int PUERTO = 2566;

        try (
            Socket socket = new Socket(HOST, PUERTO);//se conecta con el server
        		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        		// Se crea un canal de salida llamado "out" a partir del socket. Esto significa que "out" es un flujo por donde el cliente puede enviar datos al servidor.El método socket.getOutputStream() devuelve el flujo básico para enviar bytes,
        		// y DataOutputStream lo envuelve para facilitar escribir datos con writeUTF.

            DataInputStream in = new DataInputStream(socket.getInputStream());
        		// Se crea un canal de entrada llamado "in" a partir del socket.Esto significa que "in" es un flujo por donde el cliente puede recibir datos que envía el servidor.
        		//El método socket.getInputStream() devuelve el flujo básico para leer bytes, y DataInputStream lo envuelve para facilitar la lectura de datos primitivos y cadenas (por ejemplo, con readUTF).

            Scanner teclado = new Scanner(System.in)//seinstancia para leer de la consola lo que se manda por teclado
        ) {
            System.out.println("Conectado al servidor.");//si esto funciono servidor conectado

            boolean continuar = true;//pone un true en el teclado para que ingrese al menu

            while (continuar) {
                System.out.println("\nOpciones: cliente java");
                System.out.println("1. Generar nombre de usuario");
                System.out.println("2. Generar correo electrónico");
                System.out.println("3. Salir");
                System.out.print("Elija una opción: ");
                int opcion = teclado.nextInt();//lee la opcion ingresada por teclado
                teclado.nextLine(); // limpiar buffer por el salto de linea

                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese longitud (5 a 20): ");
                        int longitud = teclado.nextInt();//lee la longitud
                        teclado.nextLine(); // limpiar buffer por el salto de linea
                        out.writeUTF("USERNAME:" + longitud);// manda al servidor la cadena mas la longitud para que la valide con el whiteUTF 
                        System.out.println("Servidor: " + in.readUTF());//el servidor responde en forma utf
                        break;

                    case 2:
                        System.out.print("Ingrese nombre de usuario: ");
                        String nombre = teclado.nextLine();//lee el nombre del usuario
                        out.writeUTF("EMAIL:" + nombre);// manda al servidor la cadena emai mas el nombre para que la valide con el whiteUTF 
                        System.out.println("Servidor: " + in.readUTF());//el servidor responde en forma utf
                        break;

                    case 3:
                        out.writeUTF("SALIR");// manda al servidor la cadena  para que la valide con el whiteUTF  
                        System.out.println("Servidor: " + in.readUTF());//el servidor responde en forma utf
                        continuar = false;//corta el while
                        break;

                    default:
                        System.out.println("Opción no válida.");//por si hay una opcion no valida
                }
            }

        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());//manda la excepcio a la consola o error
        }
    }
}
