#include <Arduino.h>

/*
 This Program Runs on the ESP32-S3 which acts as 
 a Lora Wan Node
*/
char message[] = "Blinking"; // Test Message




void uart_bridge(){
  // Forward data from Serial to Serial1
  while (Serial.available()) {
    char data = Serial.read();
    Serial1.write(data);
  }

  // Forward data from Serial1 to Serial
  while (Serial1.available()) {
    char data = Serial1.read();
    Serial.write(data);
  }

}

void read_Lora(){
  while (Serial1.available()) {
    char data = Serial1.read();
    Serial.write(data);
  }
}

void Lora_Setup() {
  
    Serial1.print("AT\r\n"); // Check if Module Connected
    delay(200);
   //read_Lora();
    delay(1000);
    Serial1.print("AT+ADDRESS=3\r\n"); // Set ADDRESS to 3
    delay(200);
   //read_Lora();
    // Default Network ID for RYLR993 is 18(Helium Network)
    // Default Band is US, Freq 915Mhz
}
/*
void send_message_lora(String message, int address) {
  String Command = "AT+SEND=" + String(address) + "," + (message.length() + 2) + "," + message;
  Serial1.print(Command + "\r\n");
}
*/

void send_message_lora(char* message, int address) {
  char command[100];  // Adjust the size as per your requirements
  snprintf(command, sizeof(command), "AT+SEND=%d,%d,%s\r\n", address, strlen(message) + 2, message);
  Serial1.print(command);
}


void setup() {
  Serial.begin(9600);  // Serial port for debugging
  Serial1.begin(9600, SERIAL_8N1, 18, 17);  // UART1 using default pins
  delay(2000);
  Lora_Setup();
  delay(400);
  send_message_lora(message,2);
}




void loop() {

  
 // uart_bridge();
 send_message_lora(message,2);
 delay(5000);
}
