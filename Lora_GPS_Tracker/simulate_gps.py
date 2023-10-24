import serial

# Define the virtual serial port name (e.g., COMx on Windows, /dev/ttyUSBx on Linux)
virtual_port_name = "/dev/cu.URT4"  # Change this to the desired virtual port name

# Create a virtual UART port
virtual_uart = serial.Serial(port=virtual_port_name, baudrate=9600, timeout=1)

# Write data to the virtual UART port
virtual_uart.write(b"Hello, UART!")

# Read data from the virtual UART port
received_data = virtual_uart.read(12)  # Read up to 12 bytes

# Print the received data
print("Received:", received_data.decode())

# Close the virtual UART port when done
virtual_uart.close()
