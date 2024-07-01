import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.Scanner;

public class Main {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin@123";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            while (true) {
                System.out.println();
                System.out.println("HOTEL RESERVATION SYSTEM");
                System.out.println("1. Reserve a room.");
                System.out.println("2. View Reservations.");
                System.out.println("3. Get Room No.");
                System.out.println("4. Update Reservations.");
                System.out.println("5. Delete Reservations.");
                System.out.println("6. Exit.");
                System.out.println("Please choose an option:");

                int choices = sc.nextInt();
                switch (choices) {
                    case 1:
                        reserveRoom(connection, sc);
                        break;

                    case 2:
                        viewReservation(connection, sc);
                        break;

                    case 3:
                        getRoomNo(connection, sc);
                        break;

                    case 4:
                        updateReservation(connection, sc);
                        break;

                    case 5:
                        deleteReservation(connection, sc);
                        break;

                    case 6:
                        exit();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection connection, Scanner sc) {
        System.out.println("Enter guest name: ");
        String guest_name = sc.next();
        System.out.println("Enter room number: ");
        int room_no = sc.nextInt();
        if (roomIsReserved(connection, room_no)) {
            System.out.println("Room already reserved!");
            return;
        }
        System.out.println("Enter contact number: ");
        String contact_no = sc.next();

        String sql = "INSERT INTO reservations (guest_name, room_no, contact_no) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, guest_name);
            statement.setInt(2, room_no);
            statement.setString(3, contact_no);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Reservation was successful.");
            } else {
                System.out.println("Reservation failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservation(Connection connection, Scanner sc) {
        String sql = "SELECT reservation_id, guest_name, room_no, contact_no, reservation_date FROM reservations";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println();
            System.out.println("+--------------+--------------------------+---------+------------+------------------+");
            System.out.println("|Reservation ID|        Guest Name        | Room No | Contact No | Reservation Date |");
            System.out.println("+--------------+--------------------------+---------+------------+------------------+");

            while (resultSet.next()) {
                int reservation_id = resultSet.getInt("reservation_id");
                String guest_name = resultSet.getString("guest_name");
                int room_no = resultSet.getInt("room_no");
                String contact_no = resultSet.getString("contact_no");
                String reservation_date = resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-25s | %-7d | %-10s | %-16s |\n",
                        reservation_id, guest_name, room_no, contact_no, reservation_date);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getRoomNo(Connection connection, Scanner sc) {
        System.out.println("Enter reservation id: ");
        int reservation_id = sc.nextInt();
        System.out.println("Enter guest name: ");
        String guest_name = sc.next();

        String sql = "SELECT room_no FROM reservations WHERE reservation_id = ? AND guest_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservation_id);
            statement.setString(2, guest_name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int room_no = resultSet.getInt("room_no");
                    System.out.println("Reservation ID: " + reservation_id + "\nGuest: " + guest_name + "\nRoom Number: " + room_no);
                } else {
                    System.out.println("Reservation not found for the given ID and Guest name.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateReservation(Connection connection, Scanner sc) {
        System.out.println("Enter Reservation ID to update: ");
        int reservation_id = sc.nextInt();

        if (!reservationExists(connection, reservation_id)) {
            System.out.println("Reservation not found!");
            return;
        }

        System.out.println("Enter new guest name: ");
        String guest_name = sc.next();
        System.out.println("Enter new contact number: ");
        String contact_no = sc.next();
        System.out.println("Enter new room number: ");
        int room_no = sc.nextInt();

        String sql = "UPDATE reservations SET guest_name = ?, contact_no = ?, room_no = ? WHERE reservation_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, guest_name);
            statement.setString(2, contact_no);
            statement.setInt(3, room_no);
            statement.setInt(4, reservation_id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Reservation updated successfully for the given Reservation ID.");
            } else {
                System.out.println("Reservation not updated!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner sc) {
        System.out.println("Enter Reservation ID to delete: ");
        int reservation_id = sc.nextInt();

        if (!reservationExists(connection, reservation_id)) {
            System.out.println("Reservation not found!");
            return;
        }

        String sql = "DELETE FROM reservations WHERE reservation_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservation_id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Reservation deleted successfully for the given Reservation ID.");
            } else {
                System.out.println("Reservation not deleted!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservation_id) {
        String sql = "SELECT 1 FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservation_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean roomIsReserved(Connection connection, int room_no) {
        String sql = "SELECT 1 FROM reservations WHERE room_no = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, room_no);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while (i != 0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank You For Using Hotel Reservation System!!!");
    }
}
