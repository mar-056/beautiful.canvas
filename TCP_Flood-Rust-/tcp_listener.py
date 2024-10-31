import socket

def start_tcp_listener(port=8080):
    # Create a TCP socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server_socket:
        server_socket.bind(('127.0.0.1', port))
        server_socket.listen()

        print(f"Listening for connections on port {port}...")

        while True:
            # Accept a new connection
            client_socket, addr = server_socket.accept()
            with client_socket:
                print(f"Connection from {addr}")
                data = client_socket.recv(1024)
                if data:
                    print(f"Received data: {data.decode()}")
                    client_socket.sendall(b'Hello, Client!')

if __name__ == "__main__":
    start_tcp_listener(8080)
