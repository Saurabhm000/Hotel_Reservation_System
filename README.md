# Hotel_Reservation_System
This is a console-based Hotel Reservation System developed in Java using MySQL for backend data storage. It allows hotel staff to perform basic reservation operations like adding new guests, viewing bookings, updating guest information, and deleting reservations. 

**📘 About the Project**

This Hotel Reservation System simplifies the booking process by enabling the hotel management to:
	•	Create new room reservations.
	•	View all current reservations.
	•	Search for a guest’s room number.
	•	Update existing reservation details.
	•	Delete bookings when no longer needed.

The system checks for room availability before making a new booking, ensuring no duplicate room reservations. It operates through a menu-driven command-line interface and stores all data in a MySQL table named reservations.

⸻

**🔧 Technologies Used**
	•	Language: Java
	•	Database: MySQL
	•	Database Driver: MySQL JDBC (Connector/J)
	•	Tools: IntelliJ IDEA / VS Code / Eclipse
	•	JDBC URL: jdbc:mysql://localhost:3306/hotel_db

**🗃️ Database Table Schema**
CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    room_no INT,
    contact_no VARCHAR(20),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


**📝 How to Run the Project**
	1.	Install MySQL and create a database named hotel_db.
	2.	Create the reservations table using the SQL above.
	3.	Set your MySQL credentials (username and password) in the Java code.
	4.	Compile and run the Main.java file using any Java IDE or terminal.
