
# Rust TCP Flood

A simple Rust-based networking tool that does three things:

1. Lists Network Interfaces: Shows Ethernet and Wi-Fi interfaces with their IP and MAC addresses.(Note* use cli arg '-a' to enable this)
2. Handles TCP Connections: Accepts TCP clients, reads data, and responds.
3. Multithreaded TCP Sender: Sends data to a target IP and port using multiple threads to simulate high network traffic.(TODO: Enable IP,Data,Port config from CLI)



