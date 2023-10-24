# Import necessary modules
from flask import Flask, render_template, jsonify
import random  # Replace with a module to fetch real GPS coordinates
import time

app = Flask(__name__, static_url_path='/static')

# Replace this function with one that fetches real GPS coordinates
def get_realtime_coordinates():
    # Simulate real GPS coordinates for demonstration purposes
    latitude = random.uniform(-90, 90)
    longitude = random.uniform(-180, 180)
    return latitude, longitude

@app.route('/')
def index():
    return render_template('map.html')

@app.route('/get_coordinates')
def get_coordinates():
    # Fetch real-time coordinates (replace with actual data)
    coordinates = get_realtime_coordinates()
    return jsonify({'latitude': coordinates[0], 'longitude': coordinates[1]})

if __name__ == '__main__':
    app.run(debug=True)