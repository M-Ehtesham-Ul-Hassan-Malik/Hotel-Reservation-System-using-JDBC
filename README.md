# Hotel Reservation System

This project is a simple Hotel Reservation System implemented in Java using JDBC for database interactions. It allows users to reserve rooms, view reservations, get room numbers for specific reservations, update reservations, and delete reservations.

## Table of Contents

- [What I Learned](#What I Learned)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Usage](#usage)
- [Database Schema](#database-schema)
- [License](#license)

## What I Learned

While working on this project, I learned how to:

- Establish a connection to a MySQL database using JDBC.
- Execute SQL queries to perform CRUD (Create, Read, Update, Delete) operations.
- Handle SQL exceptions and manage database resources efficiently.
- Use `PreparedStatement` to prevent SQL injection and improve query performance.
- Implement basic user interaction through the command-line interface.

## Features

- Reserve a room
- View all reservations
- Get room number for a specific reservation
- Update existing reservations
- Delete reservations

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server
- MySQL Connector/J (JDBC driver)

## Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/M-Ehtesham-Ul-Hassan-Malik/Hotel-Reservation-System-using-JDBC.git
   cd src
   ```

2. **Set up the MySQL database**
    - Start your MySQL server.
    - Create a database named `hotel`.
      ```sql
      CREATE DATABASE hotel;
      ```
    - Create the `reservations` table.
      ```sql
      USE hotel;
      CREATE TABLE reservations (
          reservation_id INT AUTO_INCREMENT PRIMARY KEY,
          guest_name VARCHAR(100) NOT NULL,
          room_no INT NOT NULL,
          contact_no VARCHAR(15) NOT NULL,
          reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      );
      ```

3. **Update the database configuration**
    - Open the `Main.java` file and update the `DB_URL`, `DB_USER`, and `DB_PASSWORD` with your MySQL credentials.

## Usage

1. **Compile and run the program**
   ```bash
   javac Main.java
   java Main
   ```

2. **Follow the on-screen instructions**
    - You will be presented with a menu to choose options such as reserving a room, viewing reservations, etc.
    - Enter the appropriate option number and provide the required details.

## Database Schema

- **reservations**
    - `reservation_id` (INT, Primary Key, Auto Increment)
    - `guest_name` (VARCHAR(100), NOT NULL)
    - `room_no` (INT, NOT NULL)
    - `contact_no` (VARCHAR(15), NOT NULL)
    - `reservation_date` (TIMESTAMP, Default CURRENT_TIMESTAMP)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
