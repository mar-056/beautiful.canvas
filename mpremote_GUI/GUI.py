import tkinter as tk
from tkinter import ttk, filedialog
import subprocess
import serial.tools.list_ports
import platform

# Global variable to store the selected COM port
selected_com_port = None
selected_file_path = None  # Variable to store the selected file path
selected_file_path_run = None  # Variable to store the selected file path

def button_clicked(button_num):
    result_var.set(f"Button {button_num} clicked!")

def button_one():
    # Run the 'mpremote fs ls :lib' command
    command_output = run_command(['mpremote', 'fs', 'ls', ':lib'])

    # Update the label with the command output
    result_var.set(command_output)

def run_command_in_terminal(command):
    system_platform = platform.system()

    if system_platform == "Linux":
        terminal_command = f"x-terminal-emulator -e {command}"
    elif system_platform == "Darwin":  # Mac
        terminal_command = f"osascript -e 'tell application \"Terminal\" to do script \"{command}\"'"
    else:
        print("Unsupported operating system.")
        return

    subprocess.run(terminal_command, shell=True)


def button_two():
    global selected_file_path

    # Open a file dialog to select a file
    selected_file_path = filedialog.askopenfilename()
    
    # Run the 'mpremote fs cp' command
    copy_command = ['mpremote', 'fs', 'cp', selected_file_path, ':lib/']
    print(copy_command)
    copy_output = run_command(copy_command)

    # Update the label with the selected file path and copy command output
    result_var.set(f"Selected file: {selected_file_path}\nCopy command output: {copy_output}")

def button_three():
    # Run the 'mpremote' command in a new terminal window
    run_command_in_terminal('mpremote')

def button_four():
    # Run the 'mpremote' command in a new terminal window
   
    run_command_in_terminal('mpremote reset')

def button_five():
    global selected_file_path_new

    # Open a file dialog to select a file
    selected_file_path_new = filedialog.askopenfilename()
    
    # Run the 'mpremote fs cp' command
    copy_command = ['mpremote', 'run', selected_file_path_new]
    print(copy_command)
    run_command_in_terminal('mpremote run '+selected_file_path_new)

    # Update the label with the selected file path and copy command output
    result_var.set(f"Selected file: {selected_file_path_new}\nCopy command output: {copy_output}")

def run_command(command):
    try:
        result = subprocess.run(command, text=True, capture_output=True, check=True)
        return result.stdout
    except subprocess.CalledProcessError as e:
        print(f"Error executing command: {e}")
        return f"Error: {e}\nOutput: {e.output}"


def on_select_change(event):
    global selected_com_port
    selected_com_port = select_var.get()
    result_var.set(f"Selected COM port: {selected_com_port}")

def update_select_options():
    # Get the list of available COM ports
    options = get_com_ports()
    select_box['values'] = options
    if options:
        select_box.set(options[0])  # Set default value

def get_com_ports():
    com_ports = [port.device for port in serial.tools.list_ports.comports()]
    return com_ports

# Create the main window
root = tk.Tk()
root.title("Mpremote GUI")


# Set a uniform style for ttk widgets
style = ttk.Style()
style.configure('TButton', padding=5, width=20)
style.configure('TCombobox', padding=5, width=20)
style.configure('TLabel', padding=5, width=40)

# Create buttons
button1 = ttk.Button(root, text="Get Installed Lib", command=button_one)
button2 = ttk.Button(root, text="Install Lib", command=button_two)
button3 = ttk.Button(root, text="Connect", command=button_three)
button4 = ttk.Button(root, text="Reset Device", command=button_four)
button5 = ttk.Button(root, text="Run Script", command=button_five)

# Create a select box
select_var = tk.StringVar()
select_box = ttk.Combobox(root, textvariable=select_var)
select_box.bind("<<ComboboxSelected>>", on_select_change)

# Create a button to update select options
update_button = ttk.Button(root, text="Set COM port", command=update_select_options)

# Create a label to display the result
result_var = tk.StringVar()
result_label = ttk.Label(root, textvariable=result_var)

# Place widgets in the grid
button1.grid(row=0, column=0, padx=10, pady=10)
button2.grid(row=0, column=1, padx=10, pady=10)
button3.grid(row=1, column=0, padx=10, pady=10)
button4.grid(row=1, column=1, padx=10, pady=10)
button5.grid(row=2, column=0, columnspan=2, padx=10, pady=10)
select_box.grid(row=5, column=0, columnspan=2, padx=10, pady=10)
update_button.grid(row=3, column=0, columnspan=2, padx=10, pady=10)
result_label.grid(row=4, column=0, columnspan=2, padx=10, pady=10)

# Update select options on startup
update_select_options()

# Start the Tkinter event loop
root.mainloop()