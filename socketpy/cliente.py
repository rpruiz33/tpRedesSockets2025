mport socket
import struct

def write_utf(sock, mensaje):
    # Codifica el mensaje como UTF-8
    mensaje_utf8 = mensaje.encode('utf-8')
    # Prepara los 2 bytes del encabezado con la longitud
    longitud = len(mensaje_utf8)
    encabezado = struct.pack('>H', longitud)  # '>H' = big-endian short (2 bytes)
    # Envía encabezado + mensaje
    sock.sendall(encabezado + mensaje_utf8)

def read_utf(sock):
    # Lee los primeros 2 bytes para conocer la longitud
    encabezado = sock.recv(2)
    if not encabezado:
        return ''
    longitud = struct.unpack('>H', encabezado)[0]
    # Lee el contenido del mensaje
    datos = b''
    while len(datos) < longitud:
        parte = sock.recv(longitud - len(datos))
        if not parte:
            break
        datos += parte
    return datos.decode('utf-8')

# Datos del servidor
HOST = '127.0.0.103'
PUERTO = 8014

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PUERTO))
    print("Conectado al servidor.")

    while True:
        print("\n=== MENÚ ===")
        print("1. Generar nombre de usuario")
        print("2. Generar dirección de correo electrónico")
        print("3. Salir")
        opcion = input("Opción: ")

        if opcion == '1':
            longitud = input("Ingrese la longitud del nombre de usuario (5-20): ")
            write_utf(s, f"USERNAME:{longitud}")
            respuesta = read_utf(s)
            print("Servidor:", respuesta)

        elif opcion == '2':
            nombre = input("Ingrese su nombre de usuario: ")
            write_utf(s, f"EMAIL:{nombre}")
            respuesta = read_utf(s)
            print("Servidor:", respuesta)

        elif opcion == '3':
            write_utf(s, "SALIR")
            respuesta = read_utf(s)
            print("Servidor:", respuesta)
            print("Saliendo del cliente...")
            break

        else:
            print("Opción inválida.")
