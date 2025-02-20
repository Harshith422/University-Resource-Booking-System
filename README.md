# University Resource Booking System

## 📌 Overview  
The **University Resource Booking System** is a **Java-based application** designed to help university students and staff **reserve, manage, and cancel** bookings for university resources such as halls, classrooms, or labs. This system ensures **efficient resource allocation** while providing **a user-friendly experience** for managing reservations.

## 🚀 Features  
✅ **User Registration & Login** – Secure authentication system  
✅ **Resource Booking** – Reserve university halls based on capacity and availability  
✅ **View & Edit Bookings** – Modify existing reservations when needed  
✅ **Cancel Bookings** – Release a reserved resource for better availability  
✅ **Graphical User Interface (GUI)** – Implemented using **Java Swing**  
✅ **Database Integration** – Stores bookings using **JDBC with MySQL/PostgreSQL**  

## 🏗️ System Architecture  
The system consists of:  
- **Frontend**: Java Swing (GUI) for an interactive experience  
- **Backend**: Java with object-oriented programming principles  
- **Database**: MySQL/PostgreSQL for storing users and bookings  

## 📜 Project Structure  
```
/university-resource-booking
│   ├── CODE/
│   │   ├── Login.java             # User login module
│   │   ├── Register.java          # User registration module
│   │   ├── Booking.java           # Booking system logic
│   │   ├── EditBooking.java       # Modify existing bookings
│   │   ├── CancelBooking.java     # Cancel bookings
│   │   ├── HomeScreen.java        # Main dashboard for users
│── database/
│   ├── schema.sql                 # Database schema for resource booking
│── README.md                      # Project Documentation
```

## 🛠️ Installation & Setup  

### 1️⃣ Prerequisites  
- **Java (JDK 8+)**  
- **MySQL or PostgreSQL Database**  
- JDBC Driver installed  

### 2️⃣ Clone the Repository  
```bash
git clone https://github.com/Harshith422/university-resource-booking.git
cd university-resource-booking
```

### 3️⃣ Configure Database  
- Create a new database and execute `database/schema.sql`  
- Update database credentials in `DatabaseConnection.java`  
```java
String url = "jdbc:mysql://localhost:3306/university_booking";
String user = "root";
String password = "yourpassword";
```

### 4️⃣ Compile & Run the Project  
```bash
javac src/main/*.java
java src/main/HomeScreen
```

## 🔍 Key Modules  

### 🔑 **Registration & Login**  
- Users can register with an ID, name, and password  
- Secure login with credential validation  

### 📅 **Booking System**  
- Users can book **available university halls**  
- System automatically assigns a hall based on expected capacity  

### ✏️ **Edit & View Bookings**  
- Users can modify their bookings (hall, date, time, capacity)  
- View booking details  

### ❌ **Cancel Booking**  
- Users can cancel a booked resource, making it available for others  

## 📄 Future Enhancements  
🔹 **Admin Panel** – For managing university resources  
🔹 **Notification System** – Alerts for upcoming bookings  
🔹 **Multi-User Role Management** – Separate roles for students and faculty  
