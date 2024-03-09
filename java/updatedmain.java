package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;
@SpringBootApplication
class CustomerDetails {
    private String name;
    private String destination;
    private String bloodGroup;
    private String phoneNumber;
    private boolean reservationStatus;

    public CustomerDetails(String name, String destination, String bloodGroup, String phoneNumber) {
        this.name = name;
        this.destination = destination;
        this.bloodGroup = bloodGroup;
        this.phoneNumber = phoneNumber;
        this.reservationStatus = true;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(boolean status) {
        reservationStatus = status;
    }
}

class Reservation {
    private static final String INSERT_RESERVATION_QUERY = "INSERT INTO reservations (name, destination, blood_group, phone_number, reservation_status) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_RESERVATION_STATUS_QUERY = "UPDATE reservations SET reservation_status = ? WHERE name = ? AND phone_number = ?";
    private static final String SELECT_RESERVATIONS_QUERY = "SELECT * FROM reservations";

    public static void createReservation(CustomerDetails customer) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_RESERVATION_QUERY)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getDestination());
            stmt.setString(3, customer.getBloodGroup());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setBoolean(5, customer.getReservationStatus());
            stmt.executeUpdate();
        }
    }

    public static void displayCustomerDetails() throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_RESERVATIONS_QUERY)) {
            while (rs.next()) {
                String name = rs.getString("name");
                String destination = rs.getString("destination");
                String bloodGroup = rs.getString("blood_group");
                String phoneNumber = rs.getString("phone_number");
                boolean reservationStatus = rs.getBoolean("reservation_status");

                System.out.println("Name: " + name);
                System.out.println("Destination: " + destination);
                System.out.println("Blood Group: " + bloodGroup);
                System.out.println("Phone Number: " + phoneNumber);
                System.out.println("Reservation Status: " + (reservationStatus ? "success" : "Cancelled"));
                System.out.println();
            }
        }
    }

    public static void cancelReservation(String name, String phoneNumber) throws SQLException {
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_RESERVATION_STATUS_QUERY)) {
            stmt.setBoolean(1, false);
            stmt.setString(2, name);
            stmt.setString(3, phoneNumber);
            stmt.executeUpdate();
        }
    }
}

class Cancellation {
    public void cancelReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter customer name:");
        String name = scanner.nextLine();
        System.out.println("Enter phone number:");
        String phoneNumber = scanner.nextLine();

        try {
            Reservation.cancelReservation(name, phoneNumber);
            System.out.println("Reservation for " + name + " cancelled successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Reservation not found for the provided name and phone number.");
        }
    }
}

class DatabaseUtils {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus_reservation_system";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create Reservation");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. Display Customer Details");
            System.out.println("4. Exit");
            System.out.println("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter customer name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter your desired travel destination:");
                    String destination = scanner.nextLine();
                    System.out.println("Enter blood group:");
                    String bloodGroup = scanner.nextLine();
                    System.out.println("Enter phone number:");
                    String phoneNumber = scanner.nextLine();
                    CustomerDetails customer = new CustomerDetails(name, destination, bloodGroup, phoneNumber);
                    try {
                        Reservation.createReservation(customer);
                        System.out.println("Reservation created successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Cancellation cancellation = new Cancellation();
                    cancellation.cancelReservation();
                    break;
                case 3:
                    try {
                        Reservation.displayCustomerDetails();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter again.");
            }
        }
    }
}
