from flask import Flask,jsonify, render_template, request, redirect, url_for
from datetime import datetime, timedelta
from io import BytesIO
from flask_login import LoginManager, UserMixin, login_user, login_required, logout_user, current_user
from PIL import Image
import os
from postgresclient import Update_Local_Cache 
from postgresclient import get_last_n_events
from finger_print_gen import authenticate_finger_print
from postgresclient import return_user_permissions
from postgresclient import check_files_in_table
from postgresclient import insert_log_entry
from postgresclient import remove_bmp_from_postgres
from postgresclient import store_fingerprint_in_db



app = Flask(__name__)
app.secret_key = 'your_secret_key'

login_manager = LoginManager()
login_manager.init_app(app)


class User(UserMixin):
    def __init__(self, username):
        self.username = username

    def get_id(self):
        return self.username

@login_manager.user_loader
def load_user(user_id):
    # Load user from database or any other data source
    return User(user_id)


    
# Hardcoded username and password
USER = 'admin'
PASS = 'password'

@app.route('/', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']

        if username == USER and password == PASS:
            user = User(username)
            login_user(user)
            return redirect(url_for('protected_controls'))
        else:
            return "Invalid username or password. Please try again."

    return render_template('login.html')

@app.route('/logout')
@login_required
def logout():
    logout_user()
    
    return redirect(url_for('login'))

@app.route('/controls', methods=['GET', 'POST'])
@login_required
def protected_controls():
    
    if not current_user.is_authenticated:
        return redirect(url_for('login'))

    return render_template('controls.html')

@app.route('/authenticate', methods=['POST'])
def authenticate():
    if request.method == 'POST':

        device_id = request.form.get('device_id')
        if not device_id:
            return "No device ID provided", 400
        if 'file' not in request.files:
            return "No file part", 400
        
        file = request.files['file']
        if file.filename == '':
            return "No selected file", 400
        

        # device ID
        # time
        # Authentication
        # Finger Print ID

        if file:
            filename= file.filename
            filename=filename.replace('.bmp', '') # Remove File Extension
            try:
                # Read the image file
                img = Image.open(BytesIO(file.read()))
                print("FingerPrint received")
                
                # Store Received Finger Print Temporarily
                temp_file_path='temp_image.bmp'
                img.save(temp_file_path)

                Update_Local_Cache() # Retrieve Finger Prints from postgres

                # Authentication Logic
                if authenticate_finger_print(temp_file_path):
                    os.remove(temp_file_path)   # Remove the Temp Finger Print file
                    insert_log_entry(device_id, 'Authorized', filename)# Logic to Push Log to PostGres
                    return "Authorized"+device_id
                
                    
                else:
                    os.remove(temp_file_path)   # Remove the Temp Finger Print file
                                                 # Logic to Push Log to PostGres
                    insert_log_entry(device_id, 'Denied', '-')# Logic to Push Log to PostGres
                    return "Denied" +device_id
                    
                               
            except Exception as e:
                return f"Error processing file: {str(e)}", 400

# Define the GET endpoint
@app.route('/last_5_events', methods=['GET'])
def get_last_5_events():
    # Call the function to get the last 5 events
    last_5_events = get_last_n_events(5)
    if last_5_events:
        # Return the last 5 events as JSON response
        return jsonify(last_5_events)
    else:
        # Return error message if unable to fetch events
        return jsonify({'error': 'Unable to fetch last 5 events'}), 500

# Define the GET endpoint for returning user permissions
@app.route('/return_user_permissions', methods=['GET'])
def get_user_permissions():
    # Call the function to retrieve user permissions
    user_permissions = check_files_in_table()
    
    if user_permissions:
        # Return the user permissions as JSON response
        #print(user_permissions)
        return jsonify(user_permissions)
    else:
        # Return error message if unable to fetch user permissions
        return jsonify({'error': 'Unable to fetch user permissions'}), 500

@app.route('/delete_user', methods=['GET'])
def delete_user():
    if request.method == 'GET':
        fingerprint_id = request.args.get('fingerprint_id')
        fingerprint_id = fingerprint_id+'.bmp'
        print(fingerprint_id)
        if fingerprint_id:
            fingerprint_id = fingerprint_id.replace('.bmp', '');
            remove_bmp_from_postgres(fingerprint_id)
            # Perform deletion logic here
            return f"Deleted user with fingerprint ID: {fingerprint_id}"
        else:
            return "No fingerprint ID provided", 400


@app.route('/add_user', methods=['POST'])
def add_user():
    # Check if a file was sent in the request
    if 'file' not in request.files:
        return "No file part", 400

    file = request.files['file']
    

    # Check if the file is a BMP file
    if file.filename == '' or not file.filename.endswith('.bmp'):
        return "Invalid file. Please upload a BMP file.", 400

    # Store the BMP file in PostgreSQL
    if file:
        store_fingerprint_in_db(file)
        return "User added successfully.", 200
    else:
        return "Error occurred while adding user.", 500




if __name__ == '__main__':
    app.run(debug=True)
