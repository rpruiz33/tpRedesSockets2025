package socket_cliente_servidor;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Servidor {

    static final String[] dominios = {"@gmail.com", "@hotmail.com"};
    static final String vocales = "aeiou";
    static final String consonantes = "bcdfghjklmnpqrstvwxyz";

    public static void main(String[] args) {
        final int PUERTO = 2566;
   
        try (ServerSocket servidor = new ServerSocket(PUERTO)){//crea el server con el puerto y al finalizar el bloque try , no cuando el cliente se va
        	
            System.out.println("Servidor java iniciado en el puerto  " + PUERTO);

            while (true) {
                System.out.println("Esperando cliente");
                try (
                    Socket socket = servidor.accept();//la clase socket esta en el la libreria .net, espera la conxion del cliente, establece la conxion del lado del servidos
                	
                    DataInputStream in = new DataInputStream(socket.getInputStream());//getInputStream() obtiene el flujo del clientte y el new DataInputStream envuelve el flujo para leerlo mas facuimente,Variable para leer mensajes del cliente
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream())//el getOutputStream(), obtiene el flujo  de salida del socket,DataOutputStream envuelve
                ) {
                    System.out.println("Cliente conectado.");
                    boolean continuar = true;

                    while (continuar) {
                        String mensaje = in.readUTF();//lee un mensaje por lado del cliente

                        if (mensaje.startsWith("USERNAME:")) { //el mensaje comieenza con username
                            int longitud = Integer.parseInt(mensaje.substring(9));//toma la longitud desde el caracter 9 pq los primeros son username:
                            if (longitud < 5 || longitud > 20) {
                                out.writeUTF("Error: longitud entre 5 y 20.");
                            } else {
                                String username = generarNombreUsuario(longitud);//si pasa la vallidacion genera el nombre aleatorio
                                out.writeUTF("Usuario generado: " + username);//y se lo manda al cliente por el protocolo write UTF-8 por el socket
                            }

                        } else if (mensaje.startsWith("EMAIL:")) {//hace el substring 6 poruqe el mensaje se compone de USERNAME:  y son 6 caracteres
                        	String nombreUsuario = mensaje.substring(6).toLowerCase().replaceAll("\\s", "");//el replaze lo que hace es remplazar todos los espacion vacios de un string es una expresion regular (que busca para validarr que no haya espacios en blanco)
                            if (!esNombreValido(nombreUsuario)) {
                                out.writeUTF("Error: nombre debe tener 5-20 letras, al menos 1 vocal y 1 consonante.");;//y se lo manda al cliente por el protocolo write UTF-8 por el socket
                            } else {
                                String correo = nombreUsuario + obtenerDominioAleatorio();
                                out.writeUTF("Correo generado: " + correo);;//y se lo manda al cliente por el protocolo write UTF-8 por el socket
                            }

                        } else if (mensaje.equals("SALIR")) {//si el mensaje que mando el cliente es salir hace eso
                            out.writeUTF("Desconectado");;//y se lo manda al cliente por el protocolo write UTF-8 por el socket
                            continuar = false;//corta el bucle
                        } else {
                            out.writeUTF("Comando no reconocido.");;//y se lo manda al cliente por el protocolo write UTF-8 por el socket
                        }
                    }

                    System.out.println("Cliente desconectado.");//imprime por consola que el cliente se desconecto
                } catch (IOException e) {//lanza el error
                    System.err.println("Error de cliente: " + e.getMessage());
                }
            }

        } catch (IOException e) {//hace el catch si esto tiene error (ServerSocket servidor = new ServerSocket(PUERTO)
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }

    private static String generarNombreUsuario(int longitud) {
        Random rand = new Random();//instancia la clase ramdom que esta en la clase java.util.Random;
        StringBuilder sb = new StringBuilder();//crea un objeto para construi texto , el StringBuilder

        sb.append(vocales.charAt(rand.nextInt(vocales.length())));//busca una vocal aleatoria y con append la agrega al final en sb
        sb.append(consonantes.charAt(rand.nextInt(consonantes.length())));//busca una consonante aleatoria y con appened la agrega al final

        String letras = vocales + consonantes;//concatena las vocales con las consonates del alfabeto
        for (int i = 2; i < longitud; i++) {
            sb.append(letras.charAt(rand.nextInt(letras.length())));//y en este for va agrandoo letras de la concatenacion hasta  la longitud
        }

        return mezclarCadena(sb.toString());//pasa el sb(osea el string builder a sttriing y llama a mezclar cadena
    }

    private static String mezclarCadena(String input) {//le ingresa la cadena
        char[] a = input.toCharArray();//lo pasa a arrays de caracteres
        Random rand = new Random();//crea nums aleatorios para usar posiciones aleatorias
        for (int i = 0; i < a.length; i++) {//rrecorre el array desde 0 hasta el final
            int j = rand.nextInt(a.length);//genera un aleatorio entre o y a.length
            char temp = a[i];//hace el famoso inercambio
            a[i] = a[j];
            a[j] = temp;
        }
        return new String(a);//retorna creando un nuevos string a partir dell char a
    }

    private static boolean esNombreValido(String nombre) {
        if (nombre.length() < 5 || nombre.length() > 20 || !nombre.matches("[a-zA-Z]+")) return false;// Valida que el nombre tenga entre 5 y 20 letras, y que solo contenga letras del alfabeto (mayúsculas o minúsculas).
        boolean tieneVocal = nombre.chars().anyMatch(c -> vocales.indexOf(c) >= 0);//nombre.chars() convierte cada caracter a un codigo unicode, el animatchbusca si algun caracter cimple con la condicion osea true o false, llama a una funcion anonima(callback)
        //c es un caracter que se esta recorriendo, busca si c esta dentro de la cadeno vocales si lo encuentra devuelve la pos , sino -1
        boolean tieneConsonante = nombre.chars().anyMatch(c -> consonantes.indexOf(c) >= 0);//aca hace lo mismo pero con las consonates
        return tieneVocal && tieneConsonante; //retorna si se cumplen las dos cosas
    }

    private static String obtenerDominioAleatorio() {
        return dominios[new Random().nextInt(dominios.length)];//aca se le devuelve con un aleaatorio entre los dominios .length, osea o un gmail o un hotmail
    }
}
