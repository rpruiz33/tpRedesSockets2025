import socket

# = que en el servidor para hacer la conexion
cliente = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = '127.0.0.1'
puerto = 65432
cliente.connect((host, puerto))

#  mensaje
mensaje = "Hola server"
cliente.sendall(mensaje.encode())

# respuesta del servidor
respuesta = cliente.recv(1024).decode()
print(f"Respuesta del servidor: {respuesta}")
cliente.close()
