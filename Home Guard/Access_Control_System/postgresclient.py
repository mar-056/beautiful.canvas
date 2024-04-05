import psycopg2
import os
def connect_to_postgres():
    try:
        # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )
        print("Connected to PostgreSQL")
        return connection
    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL:", error)
        return None

def create_database():
    connection = psycopg2.connect(
    user="postgres",     # The default username for PostgreSQL
    password="Rayman_45",  # The password you specified during Docker setup
    host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
    port="5432",         # The port PostgreSQL is listening on (default is 5432)
    database="postgres"  # The name of the database to connect to (default is "postgres")
        )
    database_name="postgres"
    try:

        # Set autocommit mode to True
        connection.autocommit = True

        # Create a cursor object
        cursor = connection.cursor()

            # SQL command to add a new column named 'file_name' of type 'TEXT' to the 'bmp_files' table
        alter_table_command = """
        ALTER TABLE bmp_files
        ADD COLUMN file_name TEXT;
         """

    # Execute the SQL command
        cursor.execute(alter_table_command)

        # Execute a SQL query to create the database
        cursor.execute(f"CREATE DATABASE {database_name};")

        print(f"Database '{database_name}' created successfully")
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while executing SQL command:", error)
        return False
    finally:
        if cursor:
            cursor.close()

def extract_file_name(file_path):
    try:
        # Get the base name of the file (without directory)
        base_name = os.path.basename(file_path)
        # Remove the ".bmp" extension
        file_name = os.path.splitext(base_name)[0]
        print("Extracted file name:", file_name)  # Add this line to print the extracted file name
        return file_name
    except Exception as e:
        print("Error extracting file name:", e)
        return None

def close_connection(connection):
    if connection:
        connection.close()
        print("PostgreSQL connection is closed")

def create_bmp_files_table(connection):
    try:
                # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )
        # Create a cursor object
        cursor = connection.cursor()

        # SQL query to create the bmp_files table
        create_table_query = '''
            CREATE TABLE IF NOT EXISTS bmp_files (
                id SERIAL PRIMARY KEY,
                file_data BYTEA
            );
        '''

        # Execute the SQL query
        cursor.execute(create_table_query)
        connection.commit()
        print("bmp_files table created successfully")
    except (Exception, psycopg2.Error) as error:
        print("Error while creating bmp_files table:", error)
        if connection:
            connection.rollback()

def Update_Local_Cache():
    delete_all_files_in_fp() # Clear Local Cache First
    try:
        # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )

        # Create a cursor object
        cursor = connection.cursor()

        # Execute SQL query to select file data from the table
        cursor.execute("SELECT file_data, file_name FROM bmp_files;")

        # Fetch all records
        records = cursor.fetchall()

        if not records:
            print("No files found in the bmp_files table.")
        else:
            print("Downloading .bmp files...")
            for record in records:
                file_data ,file_name= record
                file_path = os.path.join("fp_db", f"{file_name}.bmp")  # Use original file name
                with open(file_path, "wb") as file:
                    file.write(file_data)
                print(f"Downloaded {file_name}.bmp")
                
                

    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL or executing SQL command:", error)

    finally:
        # Close the cursor and connection
        if connection:
            cursor.close()
            connection.close()
            print("PostgreSQL connection is closed")

def setup_database():
        # Connect to PostgreSQL
    connection = connect_to_postgres()
    if not connection:
        return

    # Create the database
    database_name = "finger_print_db"
    #create_database(connection, database_name)
    create_bmp_files_table(connection)
    close_connection(connection)

def delete_all_files_in_fp():
    try:
        folder_path = "fp_db"
        # List all files in the directory
        file_list = os.listdir(folder_path)
        
        # Iterate over the files and delete them
        for file_name in file_list:
            file_path = os.path.join(folder_path, file_name)
            if os.path.isfile(file_path):
                os.remove(file_path)
                print(f"File '{file_name}' deleted successfully")

        print("All files deleted from 'fp_db' directory.")
    except Exception as e:
        print("Error deleting files:", e)

'''
This function takes file name of fingerprint .bmp and stores it in PostGres DataBase and Updates Local Cache
'''
def store_bmp_in_postgres(file_path):
            # Establish a connection to the PostgreSQL database
    
    cursor = 0
    connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )
    try:
        # Read binary content of the BMP file
        with open(file_path, 'rb') as file:
            binary_data = file.read()

        # Create a cursor object
        cursor = connection.cursor()

        # Execute SQL query to insert binary data into table
        
        
        file_name = extract_file_name(file_path)
        print(file_name)
        cursor.execute("INSERT INTO bmp_files (file_name, file_data) VALUES (%s, %s);", (file_name, psycopg2.Binary(binary_data)))
        connection.commit()

        print("BMP file stored in PostgreSQL successfully")
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while storing BMP file in PostgreSQL:", error)
        connection.rollback()
        return False
    finally:
        if cursor:
            cursor.close()
            Update_Local_Cache()

def store_fingerprint_in_db(file):
    # Establish a connection to the PostgreSQL database
    connection = None
    cursor = None
    try:
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )

        # Read binary content of the BMP file
        binary_data = file.read()

        # Create a cursor object
        cursor = connection.cursor()

        # Execute SQL query to insert binary data into table
        file_name = file.filename
        file_name=file_name[:-4]
        cursor.execute("INSERT INTO bmp_files (file_name, file_data) VALUES (%s, %s);", (file_name, psycopg2.Binary(binary_data)))
        connection.commit()

        print("BMP file stored in PostgreSQL successfully")
        return True
    except (Exception, psycopg2.Error) as error:
        print("Error while storing BMP file in PostgreSQL:", error)
        if connection:
            connection.rollback()
        return False
    finally:
        if cursor:
            cursor.close()
        if connection:
            connection.close()
            Update_Local_Cache()

'''
 This Function Returns the files in the database
'''
def check_files_in_table():
    try:
        # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )

        # Create a cursor object
        cursor = connection.cursor()

        # Execute SQL query to select files from the table
        cursor.execute("SELECT file_name FROM bmp_files;")

        # Fetch all records
        records = cursor.fetchall()

        if not records:
            print("No files found in the bmp_files table.")
            return None
        else:
            print("Files found in the bmp_files table:")
            for record in records:
                print(record[0])  # Assuming file_name is the first column in the table
            return records

    except (Exception, psycopg2.Error) as error:
        print("Error while connecting to PostgreSQL or executing SQL command:", error)

    finally:
        # Close the cursor and connection
        if connection:
            cursor.close()
            connection.close()
            print("PostgreSQL connection is closed")

'''
This Functions Removes All Finger Prints from the PostGres DataBase and Updates Local Cache
'''
def clear_finger_prints():
    try:
        # Establish a connection to the PostgreSQL database
        # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )

        # Create a cursor object
        cursor = connection.cursor()

        # Execute SQL query to clear all items from the table
        cursor.execute("DELETE FROM bmp_files;")

        # Commit the transaction
        connection.commit()

        print("All items removed from bmp_files table successfully.")

    except (Exception, psycopg2.Error) as error:
        print("Error while clearing the table:", error)
        # Rollback the transaction in case of error
        connection.rollback()

    finally:
        # Close the cursor and connection
        if cursor:
            cursor.close()
        if connection:
            connection.close()
            print("PostgreSQL connection is closed")
        Update_Local_Cache()

'''
This Functions Removes a single Finger Print from the PostGres DataBase and Updates Local Cache
'''
def remove_bmp_from_postgres(file_name):
    try:
        # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",
            password="Rayman_45",
            host="localhost",
            port="5432",
            database="postgres"
        )

        # Create a cursor object
        cursor = connection.cursor()

        # Execute SQL query to remove the file from the table
        cursor.execute("DELETE FROM bmp_files WHERE file_name = %s;", (file_name,))
        connection.commit()

        print(f"BMP file '{file_name}' removed from PostgreSQL successfully")

    except (Exception, psycopg2.Error) as error:
        print("Error while removing BMP file from PostgreSQL:", error)
        connection.rollback()

    finally:
        # Close the cursor and connection
        if connection:
            cursor.close()
            connection.close()
            print("PostgreSQL connection is closed")
            Update_Local_Cache()

import psycopg2

def create_log_table():
    try:
        # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )

        # Create a cursor object
        cursor = connection.cursor()

        # SQL query to drop the authentication_logs table if it exists
        drop_table_query = '''
            DROP TABLE IF EXISTS authentication_logs;
        '''
        cursor.execute(drop_table_query)
        connection.commit()
        print("authentication_logs table dropped successfully")

        # SQL query to create the authentication_logs table
        create_table_query = '''
            CREATE TABLE authentication_logs (
                id SERIAL PRIMARY KEY,
                device_id VARCHAR(255) NOT NULL,
                time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                authentication_status VARCHAR(10) NOT NULL,
                fingerprint_id VARCHAR(255) NOT NULL
            );
        '''

        # Execute the SQL query
        cursor.execute(create_table_query)
        connection.commit()
        print("authentication_logs table created successfully")
    except (Exception, psycopg2.Error) as error:
        print("Error while creating authentication_logs table:", error)
        if connection:
            connection.rollback()
    finally:
        if connection:
            cursor.close()
            connection.close()

def insert_log_entry(device_id, authentication_status, fingerprint_id):
    try:
        # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )

        # Create a cursor object
        cursor = connection.cursor()

        # SQL query to insert a new entry into the authentication_logs table
        insert_query = '''
            INSERT INTO authentication_logs (device_id, authentication_status, fingerprint_id)
            VALUES (%s, %s, %s);
        '''

        # Execute the SQL query
        cursor.execute(insert_query, (device_id, authentication_status, fingerprint_id))

        # Commit the transaction
        connection.commit()

        print("Log entry inserted successfully")
    except (Exception, psycopg2.Error) as error:
        print("Error while inserting log entry:", error)
        if connection:
            connection.rollback()
    finally:
        # Close the cursor and connection
        if cursor:
            cursor.close()
        if connection:
            connection.close()
    

def get_last_n_events(n):
    try:
        # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )

        # Create a cursor object
        cursor = connection.cursor()

        # SQL query to select the last n entries from the authentication_logs table
        select_query = '''
            SELECT * FROM (
                SELECT * FROM authentication_logs ORDER BY time DESC LIMIT %s
            ) AS last_n_events ORDER BY time ASC;
        '''

        # Execute the SQL query
        cursor.execute(select_query, (n,))

        # Fetch all records
        records = cursor.fetchall()

        # Convert records to a list of dictionaries
        event_list = []
        for record in records:
            event_dict = {
                'id': record[0],
                'device_id': record[1],
                'time': record[2],
                'authentication_status': record[3],
                'fingerprint_id': record[4]
            }
            event_list.append(event_dict)

        print(f"Last {n} events fetched successfully")
        return event_list
    except (Exception, psycopg2.Error) as error:
        print("Error while fetching last n events:", error)
        return None
    finally:
        # Close the cursor and connection
        if cursor:
            cursor.close()
        if connection:
            connection.close()



def delete_all_logs():
    try:
        # Establish a connection to the PostgreSQL database
        connection = psycopg2.connect(
            user="postgres",     # The default username for PostgreSQL
            password="Rayman_45",  # The password you specified during Docker setup
            host="localhost",    # The host where PostgreSQL is running (in this case, the local machine)
            port="5432",         # The port PostgreSQL is listening on (default is 5432)
            database="postgres"  # The name of the database to connect to (default is "postgres")
        )

        # Create a cursor object
        cursor = connection.cursor()

        # SQL query to delete all rows from the authentication_logs table
        delete_query = '''
            DELETE FROM authentication_logs;
        '''

        # Execute the SQL query
        cursor.execute(delete_query)

        # Commit the transaction
        connection.commit()

        print("All logs deleted successfully")
    except (Exception, psycopg2.Error) as error:
        print("Error while deleting logs:", error)
        if connection:
            connection.rollback()
    finally:
        # Close the cursor and connection
        if cursor:
            cursor.close()
        if connection:
            connection.close()

def return_user_permissions():
    # Call get_last_n_events to get the latest events
    latest_events = get_last_n_events(100)  # Assuming you want to fetch the latest 100 events
    
    if latest_events:
        # Extract unique FingerPrintIDs
        fingerprint_ids = set(event['fingerprint_id'] for event in latest_events)

        # Convert set to list and return
        return list(fingerprint_ids)
    else:
        return []


#permissions = return_user_permissions()
#temp=check_files_in_table()
#print(temp)
#clear_finger_prints()             # Clear Post Gres Data Base
#create_log_table
delete_all_logs()           
#insert_log_entry(5,"Authorized","Ahmad")
#insert_log_entry(4,"Denied","Ahmad")
#insert_log_entry(1,"Authorized","Test1")
#insert_log_entry(5,"Authorized","Test1")
#insert_log_entry(3,"Denied","Test")
#insert_log_entry('X2','42','23',)            
#print(get_last_n_events(5))
#create_log_table()
#create_database()
#store_bmp_in_postgres('3_1.bmp')  # Store a Finger Print in Post Gres
#remove_bmp_from_postgres('3_1')   # Remove a Finger Print in Post Gres          
#Update_Local_Cache()              # Update the Local Cache
    
    




