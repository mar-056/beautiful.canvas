import os
import requests

# Function to send POST request with fingerprint file
def send_authentication_request(file_name, device_id):
    # Base URL of the Flask server
    base_url = 'http://localhost:5000/authenticate'

    # File path of the fingerprint image
    file_path = f'{file_name}.bmp'

    # Check if the file exists
    if not os.path.exists(file_path):
        print(f"Error: File '{file_path}' does not exist.")
        return

    # Prepare data for POST request
    files = {'file': open(file_path, 'rb')}
    data = {'device_id': device_id}  # Include device ID in the request

    try:
        # Send POST request
        response = requests.post(base_url, files=files, data=data)
        
        # Display response
        print("Response:")
        print(response.text)
    except Exception as e:
        print(f"Error sending POST request: {e}")

def main():
    print("IoT Device Authentication Simulator")
    print("Type 'exit' to quit.")

    # Get the current working directory
    current_path = os.getcwd()

    # Print the current working directory
    print("Current Path:", current_path)

    while True:
        # Input fingerprint file name
        fingerprint_file_name = input("Enter fingerprint file name (without extension): ")

        if fingerprint_file_name.lower() == 'exit':
            print("Exiting program.")
            break

        # Input device ID
        device_id = input("Enter device ID: ")

        if device_id.lower() == 'exit':
            print("Exiting program.")
            break

        # Send authentication request with device ID
        send_authentication_request(fingerprint_file_name, device_id)

if __name__ == '__main__':
    main()
   #send_authentication_request('3_3', 'AHAHA')   