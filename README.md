Vehicle Rental System

A Java desktop application for managing customers, vehicles, and rentals.
This program requires MySQL to store and retrieve data.

📦 Requirements

Java 17 or later

MySQL Server 8.x

MySQL Workbench (optional but recommended)

📌 1. Install MySQL

Download MySQL Installer:
https://dev.mysql.com/downloads/installer/

Install:

MySQL Server

MySQL Workbench

Remember the root password you set.

📌 2. Create Database

Open MySQL Workbench → New SQL Tab → run:

CREATE DATABASE rentaldb;
USE rentaldb;

📌 3. Create Tables
customer
CREATE TABLE customer (
    id INT PRIMARY KEY,
    type VARCHAR(20),
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100)
);

vehicle
CREATE TABLE vehicle (
    id INT PRIMARY KEY,
    type VARCHAR(20),
    brand VARCHAR(50),
    model VARCHAR(50),
    plate_number VARCHAR(20),
    price_per_day DECIMAL(10,2),
    available INT,
    image_path VARCHAR(255),
    seats INT,
    cc INT,
    capacity INT
);

rental
CREATE TABLE rental (
    rental_id INT PRIMARY KEY,
    customer_id INT,
    vehicle_id INT,
    rental_date DATE,
    return_date DATE,
    total_price DECIMAL(10,2),
    expected_return_date DATE
);

📌 4. Insert Sample Data
customer
INSERT INTO customer VALUES
(1,'REGULAR','John Tan','0123456789','john@mail.com'),
(2,'REGULAR','John Pork','0123334444','pork@gmail.com'),
(3,'PREMIUM','Jake','91231320','jake@hotmail.com'),
(4,'REGULAR','henry','0122921170','henry@mail.com');

rental
INSERT INTO rental VALUES
(1,4,1,'2025-12-12','2025-12-12',450,'2025-12-15'),
(2,4,2,'2025-12-12','2025-12-12',2400,'2025-12-16'),
(3,4,7,'2025-12-12','2025-12-12',600,'2025-12-15');

vehicle

⚠ Image files must be placed in:

C:\Users\<your_username>\Documents\Code\Java\VehicleRentalSystem\images\


Required images:

car.jpg

bike.jpg

van.jpg

supra.png

ae86.jpg

atto3.jpg

y15zr.jpg

nv200.JPG

Insert vehicles (replace <your_username>):

INSERT INTO vehicle VALUES
(1,'CAR','Toyota','Vios','ABC123',150,1,'C:\\Users\\<your_username>\\Documents\\Code\\Java\\VehicleRentalSystem\\images\\car.jpg',5,NULL,NULL),
(2,'BIKE','Honda','Goldwing Tour','WHS3929',600,1,'C:\\Users\\<your_username>\\Documents\\Code\\Java\\VehicleRentalSystem\\images\\bike.jpg',NULL,1833,NULL),
(3,'VAN','Volvo','A10','ABC3344',110,1,'C:\\Users\\<your_username>\\Documents\\Code\\Java\\VehicleRentalSystem\\images\\van.jpg',NULL,NULL,1200),
(4,'CAR','Toyota','Supra','AYE6767',350,1,'C:\\Users\\<your_username>\\Documents\\Code\\Java\\VehicleRentalSystem\\images\\supra.png',2,NULL,NULL),
(5,'CAR','Toyota','Sprinter Trueno AE86','ABC1954',200,1,'C:\\Users\\<your_username>\\Documents\\Code\\Java\\VehicleRentalSystem\\images\\ae86.jpg',4,NULL,NULL),
(6,'CAR','BYD','ATTO 3','JKM9934',500,1,'C:\\Users\\<your_username>\\Documents\\Code\\Java\\VehicleRentalSystem\\images\\atto3.jpg',5,NULL,NULL),
(7,'BIKE','Yamaha','Y15ZR','JJ3344',200,1,'C:\\Users\\<your_username>\\Documents\\Code\\Java\\VehicleRentalSystem\\images\\y15zr.jpg',NULL,150,NULL),
(8,'VAN','Nissan','NV200','JSK5976',700,1,'C:\\Users\\<your_username>\\Documents\\Code\\Java\\VehicleRentalSystem\\images\\nv200.JPG',NULL,NULL,740);

📌 5. Database Configuration

Your app must point to this database:

url=jdbc:mysql://localhost:3306/rentaldb
user=root
password=YOUR_PASSWORD

📌 6. Running the Application

Run the JAR using:

java -jar VehicleRentalSystem.jar


Make sure:

MySQL server is running

Tables and images are set up correctly
