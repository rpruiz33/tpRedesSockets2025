import socket

# Crear socket y conexion
servidor = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = '127.0.0.1'  
puerto = 65432
servidor.bind((host, puerto))

# Escuchar conexiones entrantes
servidor.listen()
print(f"Servidor en {host}:{puerto}")

# Aceptar conexión
conexion, direccion = servidor.accept()
print(f"Conexión establecida con {direccion}")

# recibir dato
datos = conexion.recv(1024).decode()
print(f"Mensaje recibido del cliente: {datos}")

respuesta = "Mensaje recibido"
conexion.sendall(respuesta.encode())
conexion.close()
