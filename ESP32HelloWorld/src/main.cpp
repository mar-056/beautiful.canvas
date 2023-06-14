#include <Arduino.h>
/*
 This Program creates a UART Bridge using
 the ESP32.

*/

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

void setup() {
  Serial.begin(9600);  // Serial port for debugging
  Serial1.begin(19200, SERIAL_8N1, 18, 17);  // UART1 using default pins
  Serial.println("UART Bridge Running ....");
}

int value = 0;
void loop()
{
  uart_bridge();
  analogWrite(18, 130);
  //value += 20;
}