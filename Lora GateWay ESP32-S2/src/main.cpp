#include <Arduino.h>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <WiFi.h>
#include <algorithm>
#include <cctype>
#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64
#define SDA_OLED 4
#define SCL_OLED 41
#define OLED_ADDRESS  0x3D

/*
 This Program Runs on the ESP32-S2 which acts as
 a Lora Wan GateWay
*/

//Lora Comm Variables
bool DEBUG = false; // Debug Flag, When true, All debug statements are printed
char message[] = "Blinking"; // Test Message
char received_message[100]; // Global char array to store the received message
bool message_received = false; // Flag to indicate if a message is received
String RX_Parsed = ""; // Stores Most Recent Received Message

//WiFi Variables
char *WIFI_SSID = "bucknell_iot"; // Holds Wifi SSID Name in Char
const char* password = ""; // Holds Wifi Password
String Wifi_Name(WIFI_SSID);      // Holds WIFI_SSID as String *for display.println() compatiibility
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1); // Initialize OLED Display Object

// For Debug Purposes
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

/*
 This Functon Sets up I2c for the OLED and initializes the display
*/
void Oled_Setup(){
   // Initialize the OLED display
  Wire.begin(SDA_OLED, SCL_OLED);  // Initialize I2C with custom pin mapping
  if (!display.begin(SSD1306_SWITCHCAPVCC, OLED_ADDRESS)) {
    Serial.println("SSD1306 allocation failed");
    while (true); // Stop execution if the display fails to initialize
  }
}

/*
This function updates the OLED to 
 display latest information:
  1.Wifi Status
  2.Wifi NetWork Name
*/

int counter = 0;
void Update_Oled(void * parameters){

  for (;;) {
    Serial.println("OLED UPDATED");
    // Clear the display buffer
    display.clearDisplay();

  // Set text color to white
  display.setTextColor(SSD1306_WHITE);

  // Print Wifi Status
   if(WiFi.status()==WL_CONNECTED){ // if Wifi Connected
     
     display.setTextSize(1);
     display.setCursor(0, 0);
     display.println("WiFi:"+Wifi_Name); // Print Wifi SSID
      
      }
   else{  // if Wifi Not Connected
     display.setTextSize(1);
     display.setCursor(0, 0);
     display.println("Wifi : NOT CONNECTED");
    }

    display.setCursor(0, 20);
    display.println(counter);

    display.setCursor(0,40);

   if (RX_Parsed.length() == 0 || std::all_of(RX_Parsed.begin(), RX_Parsed.end(), ::isspace)) {
    display.println("RX: NULL");
   } else {
    display.println(RX_Parsed); // Print Parsed Message to OLED
    }

    display.display();
    counter++;
      
    RX_Parsed = ""; // Set to Null after message is printed to Display

    vTaskDelay(3000/portTICK_PERIOD_MS);
  }

  
}


/*
 Creates Wifi Connection
 Handles Re Connection if Disconnected

*/
void WiFi_Connect(void * parameters){
  for (;;){

    if (WiFi.status() != WL_CONNECTED)
    {
     WiFi.begin(WIFI_SSID, password); // Connect to Wifi
     if (WiFi.status() == WL_CONNECTED)
     {
       Serial.println("Connected to WiFi!");
     }
    } 
  Serial.print("WiFi Status: ");
  Serial.println(WiFi.status()); // Print Status Code

  vTaskDelay(10000/portTICK_PERIOD_MS); // Delay for 10 seconds
  }
}




void Lora_Setup() {
  
    Serial1.print("AT\r\n"); // Check if Module Connected
    delay(200);
    Serial1.print("AT+ADDRESS=2\r\n"); // Set ADDRESS to 2
    delay(200);
    // Default Network ID for RYLR993 is 18(Helium Network)
    // Default Band is US, Freq 915Mhz
}

/*
 Sends Message Over LORA, takes two inputs
  1. char* message that has to be sent,
  2. int address device to send to
*/
void send_message_lora(char* message, int address) {
  char command[100];  // Adjust the size as per your requirements
  snprintf(command, sizeof(command), "AT+SEND=%d,%d,%s\r\n", address, strlen(message) + 2, message);
  Serial1.print(command);
}




/*
 Receives Data on Lora
*/
void RX_Lora(void * parameters){

  for (;;){
  Serial1.setTimeout(50); // Read for 50ms
  if (Serial1.available() > 0)
  {                                                       // Check if Lora Module has received anything
  std::string RX_Data_Raw = Serial1.readString().c_str(); // Read and Store Raw RX Data
  std::stringstream ss(RX_Data_Raw.c_str());              // Convert String to const char*
  std::vector<std::string> parts;
  std::string token;
  std::string delimiter = ","; // String Delimiter is :,

  if (DEBUG)
  {
     Serial.println(RX_Data_Raw.c_str()); // Print RX Data, for DEBUG
  }

  while (std::getline(ss, token, delimiter[0]))
  {
     parts.push_back(token);
  }

  RX_Parsed = parts[2].c_str(); // Update Received Data

  if (DEBUG)
  {
     Serial.print("Received: ");
     Serial.println(parts[0].c_str());

     Serial.print("Bytes: ");
     Serial.println(parts[1].c_str());

     Serial.print("Message: ");
     Serial.println(parts[2].c_str());
  }

  Serial.print("Received Message: ");
  Serial.println(RX_Parsed.c_str());
 }
    Serial.println(RX_Parsed);

   
  }
}

/*
 This Function creates RTOS Tasks
 */
void RTOS_SETUP(){

  xTaskCreatePinnedToCore(
    WiFi_Connect,   // Task function
    "WiFi",     
    5000,          // Stack size 
    NULL,           // parameter 
    2,              // priority
    NULL,           // Task handle 
    1               // Pin Task to Core 1
  );

  xTaskCreatePinnedToCore(
    Update_Oled,   // Task function
    "OLED_Display",     
    5000,          // Stack size 
    NULL,           // parameter 
    3,              // priority
    NULL,           // Task handle 
    1               // Pin Task to Core 1
  );

 
  xTaskCreatePinnedToCore(
    RX_Lora,   // Task function
    "Receive Lora",     
    10000,          // Stack size 
    NULL,           // parameter 
    1,              // priority
    NULL,           // Task handle 
    0               // Pin Task to Core 0
  );
  



}

void setup() {
 Serial.begin(9600);  // Serial port for debugging
 Serial1.begin(9600, SERIAL_8N1, 18, 17);  // UART1 using default pins
 Lora_Setup(); // Sets Up LORA Module 
 Serial.println("Lora GateWay : ESP32 S2"); // Print Initial Boot Message
 Serial.println("Booting ....");
 Oled_Setup(); // Sets up OLED
 RTOS_SETUP(); // Start FreeRTOS
}

void loop() {

}
