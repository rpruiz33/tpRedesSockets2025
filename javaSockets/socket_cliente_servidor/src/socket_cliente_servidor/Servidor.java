package socket_cliente_servidor;

import java.io.*;
import java.net.*;
import java.util.Random;

public class Servidor {

    static final String[] DOMINIOS_VALIDOS = {"@gmail.com", "@hotmail.com"};
    static final String VOCALES = "aeiou";
    static final String CONSONANTES = "bcdfghjklmnpqrstvwxyz";

    public static void main(String[] args) {
        final int PUERTO = 2522;

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado en el puerto " + PUERTO);

            while (true) {
                System.out.println("Esperando cliente");
                try (
                    Socket socket = servidor.accept();
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream())
                ) {
                    System.out.println("Cliente conectado.");
                    boolean continuar = true;

                    while (continuar) {
                        String mensaje = in.readUTF();

                        if (mensaje.startsWith("USERNAME:")) {
                            int longitud = Integer.parseInt(mensaje.substring(9));
                            if (longitud < 5 || longitud > 20) {
                                out.writeUTF("Error: longitud entre 5 y 20.");
                            } else {
                                String username = generarNombreUsuario(longitud);
                                out.writeUTF("Usuario generado: " + username);
                            }

                        } else if (mensaje.startsWith("EMAIL:")) {
                            String nombreUsuario = mensaje.substring(6).toLowerCase();
                            if (!esNombreValido(nombreUsuario)) {
                                out.writeUTF("Error: nombre debe tener 5-20 letras, al menos 1 vocal y 1 consonante.");
                            } else {
                                String correo = nombreUsuario + obtenerDominioAleatorio();
                                out.writeUTF("Correo generado: " + correo);
                            }

                        } else if (mensaje.equals("SALIR")) {
                            out.writeUTF("Desconectanda");
                            continuar = false;
                        } else {
                            out.writeUTF("Comando no reconocido.");
                        }
                    }

                    System.out.println("Cliente desconectado.");
                } catch (IOException e) {
                    System.err.println("Error de cliente: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }

    private static String generarNombreUsuario(int longitud) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        sb.append(VOCALES.charAt(rand.nextInt(VOCALES.length())));
        sb.append(CONSONANTES.charAt(rand.nextInt(CONSONANTES.length())));

        String letras = VOCALES + CONSONANTES;
        for (int i = 2; i < longitud; i++) {
            sb.append(letras.charAt(rand.nextInt(letras.length())));
        }

        return mezclarCadena(sb.toString());
    }

    private static String mezclarCadena(String input) {
        char[] a = input.toCharArray();
        Random rand = new Random();
        for (int i = 0; i < a.length; i++) {
            int j = rand.nextInt(a.length);
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        return new String(a);
    }

    private static boolean esNombreValido(String nombre) {
        if (nombre.length() < 5 || nombre.length() > 20 || !nombre.matches("[a-zA-Z]+")) return false;
        boolean tieneVocal = nombre.chars().anyMatch(c -> VOCALES.indexOf(c) >= 0);
        boolean tieneConsonante = nombre.chars().anyMatch(c -> CONSONANTES.indexOf(c) >= 0);
        return tieneVocal && tieneConsonante;
    }

    private static String obtenerDominioAleatorio() {
        return DOMINIOS_VALIDOS[new Random().nextInt(DOMINIOS_VALIDOS.length)];
    }
}
