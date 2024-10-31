use pnet::datalink; // Get Network Interfaces
use pnet::datalink::NetworkInterface;
use core::num;
use std::env; // Read CLI
use std::thread;

use std::io::{Read, Write};
use std::net::{TcpListener, TcpStream};
use std::thread::sleep;
use std::time::Duration;
use std::sync::{Arc, Mutex};
use std::sync::atomic::{AtomicBool, Ordering};
// Prints the Availabe Ethernet and WiFi interfaces
fn print_network_interfaces() {
    let interfaces = datalink::interfaces();

    for interface in interfaces {
        if interface.name.starts_with("en") || interface.name.starts_with("wl") {
            if !interface.ips.is_empty() {
                println!("Name: {}", interface.name);
                println!("MAC: {:?}", interface.mac);

                // Filter and collect only IPv4 addresses
                let ipv4_list: Vec<String> = interface
                    .ips
                    .iter()
                    .filter(|ip_network| ip_network.is_ipv4())
                    .map(|ip_network| ip_network.ip().to_string())
                    .collect();

                // Filter and collect only IPv6 addresses
                let ipv6_list: Vec<String> = interface
                    .ips
                    .iter()
                    .filter(|ip_network| ip_network.is_ipv6())
                    .map(|ip_network| ip_network.ip().to_string())
                    .collect();

                // Print the IPv4 and IPv6 addresses if they exist
                if !ipv4_list.is_empty() {
                    println!("IPv4: [{}]", ipv4_list.join(", "));
                }
                if !ipv6_list.is_empty() {
                    println!("IPv6: [{}]", ipv6_list.join(", "));
                }

                println!("Flags: {:?}", interface.flags);
                println!("---------------------------------");
            }
        }
    }
}

fn handle_client(mut stream: TcpStream) {
    // this is a buffer to read data from the client
    let mut buffer = [0; 1024];
    loop {
        match stream.read(&mut buffer) {
            Ok(0) => break,

            Ok(_) => {
                // Reads data from stream and stores it in the buffer
                stream
                    .read(&mut buffer)
                    .expect("Failed to read from client!");

                // Converts bytes in buffer into a UTF-8 encoded string
                let request = String::from_utf8_lossy(&buffer[..]);
                println!("Received request : {}", request);

                // Create a Respone Variable
                let response = "Hello, Client!".as_bytes(); // Convert string into as Raw Bytes
                stream.write(response).expect("Failed to Write to Client");
            }

            Err(e) => {
                eprintln!("Failed to read from client: {}", e);
                break;
            }
        }
    }
}

// Create TCP Socket at IP Address, Port, Data to be sent
fn send_tcp(ip: Arc<String>, port: u16, data: Arc<String>, exit_flag: Arc<AtomicBool>) {
    
    // Create a buffer and store data as raw bytes
    let buffer = data.as_bytes();

    // Create Address String
    let address = format!("{}:{}", ip, port);
    print!("Thread Created\n");
    while !exit_flag.load(Ordering::SeqCst) {
        match TcpStream::connect(&address) {
            Ok(mut stream) => {
                stream.write_all(&buffer).expect("Failed to send data");
                
                print!("TCP Sent\n");
            }

            Err(e) => {
                eprintln!("Failed to connect {}", e);
                break;
            }
        }
        sleep(Duration::from_millis(100));
    }
}

fn main() {
    let args: Vec<String> = env::args().collect(); // Get Args from CLI

    // Check for -a
    if args.contains(&String::from("-a")) {
        print_network_interfaces();
    }

    // let listener = TcpListener::bind("127.0.0.1:23").expect("Failed to bind to address");
    // println!("Server Listening on Port 23");
    // for stream in listener.incoming(){
    //     match stream{
    //         Ok(stream) => {
    //             std::thread::spawn(|| handle_client(stream));
    //         }
    //         Err(e) => {
    //             eprintln!("Failed to establish connection: {}",e);

    //         }

    //     }
    // }

    // Target IP, port, and data to send
    let ip = "127.0.0.1"; // Replace with the actual IP address
    let port: u16 = 8080; // Replace with the actual port
    let data = "Hello, world!".repeat(100000); // Data to sendƒƒ
    let num_threads = 1000;

    

    let bytes_sent = data.as_bytes().len();
    // Print out config
    print!("Target IP: {}\n",ip);
    print!("port: {}\n",port);
    print!("Bytes Sent: {}\n",bytes_sent);
    print!("Threads: {}\n",num_threads);

     // Flag for Threads to check if time to exit
    let exit_flag = Arc::new(AtomicBool::new(false));

    // Store handles of each thread so we can join them later
    let mut handles = Vec::new();
    // Clone the Arc references for each thread
    
    let ip = Arc::new(String::from("127.0.0.1"));// Replace with the actual IP address
    let port: u16 = 8080;// Replace with the actual port
    let data = Arc::new(String::from("Hello, world!")); //Data to sendƒƒ

    
    for _ in 0..num_threads{
        let ip = Arc::clone(&ip);
        let data = Arc::clone(&data);
        let port: u16 = 8080;

         let exit_flag_clone = Arc::clone(&exit_flag); // Clone the Arc for the thread

        let handle = thread::spawn(move || {
        send_tcp(ip, port, data,exit_flag_clone);
     });
     handles.push(handle);
     
   
    }
    // Sleep for 5 seconds
     thread::sleep(Duration::from_secs(600));
     
      // Signal threads to exit
     exit_flag.store(true, Ordering::SeqCst);
    
    /// Wait for threads to finish
    for handle in handles{
        handle.join().expect("Thread Failed");
    }



}
