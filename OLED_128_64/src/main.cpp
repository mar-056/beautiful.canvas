#include <Arduino.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <WiFi.h>
#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64
#define SDA_OLED 4
#define SCL_OLED 41
#define OLED_ADDRESS  0x3D
/*
 This code drives the OLED SSD1306 128x64
 Author: Ahmad Rehman 
 June 14 2023
*/

char *WIFI_SSID = "bucknell_iot"; // Holds Wifi SSID Name in Char
const char* password = ""; // Holds Wifi Password
String Wifi_Name(WIFI_SSID);      // Holds WIFI_SSID as String *for display.println() compatiibility
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1); // Initialize OLED Display Object

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

    display.display();
    counter++;


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


/*
 This Function creates RTOS Tasks
 */
void RTOS_SETUP(){
  xTaskCreate(
      WiFi_Connect,    // Name of Function
      "WiFi", // Name of Task
      5000,     // Stack Size
      NULL,   // Parameters
      1,    // Task Priority
      NULL   // Task Handle
);

  xTaskCreate(
      Update_Oled,    // Name of Function
      "OLED_Display", // Name of Task
      5000,     // Stack Size
      NULL,   // Parameters
      2,    // Task Priority
      NULL
);



}


void setup() {
  Serial.begin(9600); // Start UART 9600 Baud Rate
  
  Oled_Setup(); // Setup OLED
  RTOS_SETUP(); // Starts FreeRTOS
 
}

void loop() {

}
