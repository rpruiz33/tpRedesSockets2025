import socket
import struct

def write_utf(sock, mensaje):# Convierte el mensaje a bytes UTF-8
    mensaje_utf8 = mensaje.encode('utf-8')
    
    
    longitud = len(mensaje_utf8)# Obtiene la longitud del mensaje en bytes
    
    
    encabezado = struct.pack('>H', longitud)  # convierte el número en 2 bytes para decirle al servidor cuánto mide el mensaje que voy a enviar
    # es un protocolo de comunicación que indica la longitud del mensaje que se va a enviar
    # '>H' significa que se espera un número de 2 bytes (sin signo) en orden grande primero (big-endian), pq estamos trabajando con java en e servidor
    
    sock.sendall(encabezado + mensaje_utf8)# Envía primero la longitud y luego el mensaje codificado
 
 # recibe los primeros 2 bytes, que indican la longitud del mensaje
def read_utf(sock):
   
   # lee (recibe) los primeros 2 bytes del mensaje enviado por el servidor.
   # esos 2 bytes indican la longitud del mensaje que viene después.
    encabezado = sock.recv(2)
    if not encabezado:
        return ''  # si no se recibe nada, retorna cadena vacía
    longitud = struct.unpack('>H', encabezado)[0]  # convierte los 2 bytes recibidos en un número. 
    # '>H' significa que se espera un número de 2 bytes (sin signo) en orden grande primero (big-endian). pq estamos trabajando con java en e servidor
    # [0] es porque devuelve una lista con un solo número, y tomamos ese número.

    datos = b''  # crea una variable vacía para ir guardando los datos recibidos en formato binario

    # recibe hasta completar el mensaje de la longitud indicada
    while len(datos) < longitud:
        parte = sock.recv(longitud - len(datos))
        if not parte:
            break
        datos += parte
    
    # decodifica los bytes recibidos a UTF-8 y retorna el mensaje como string
    return datos.decode('utf-8')

# Configuración del host y puerto al que se conecta el cliente
HOST = 'localhost'
PUERTO = 2566

# crea un socket con IPv4 (AF_INET) y protocolo TCP (SOCK_STREAM)
# el 'with' asegura que el socket se cierre automáticamente al salir del bloque
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PUERTO))  # Conecta al servidor
    print("Conectado al servidor.")

    # el menu mientras esta en verdadero
    while True:
        print("\n*******MENÚ-python********")
        print("1. Generar nombre de usuario")
        print("2. Generar dirección de correo electrónico")
        print("3. Salir")
        opcion = input("Opción: ")

        if opcion == '1':
            # pide la longitud y manda el comando USERNAME con esa longitud
            longitud = input("Ingrese la longitud del nombre de usuario (5-20): ")
            write_utf(s, f"USERNAME:{longitud}")# f de formato string
            respuesta = read_utf(s)
            print("Servidor:", respuesta)

        elif opcion == '2':
            # pide el nombre de usuario y manda el comando EMAIL con ese nombre
            nombre = input("Ingrese su nombre de usuario: ")
            write_utf(s, f"EMAIL:{nombre}")# f de formato string
            respuesta = read_utf(s)
            print("Servidor:", respuesta)

        elif opcion == '3':
            # manda el comando SALIR para terminar la conexión
            write_utf(s, "SALIR")
            respuesta = read_utf(s)
            print("Servidor:", respuesta)
            print("Saliendo del cliente...")
            break

        else:
            print("Opción inválida.")
