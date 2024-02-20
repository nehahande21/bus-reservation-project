import java.util.ArrayList;
import java.util.Scanner;

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
    private ArrayList<CustomerDetails> reservations;
    private String[] destinations = {"Bangalore", "Goa", "Mysore", "Kerala", "Tamil Nadu", "Mumbai", "Maharashtra", "Goa", "Kasargod", "Telangana"};

    public Reservation() {
        reservations = new ArrayList<>();
    }

    public void createReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter customer name:");
        String name = scanner.nextLine();
        System.out.println("Select travel destination:");

        for (int i = 0; i < destinations.length; i++) {
            System.out.println((i + 1) + ". " + destinations[i]);
        }

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String destination = destinations[choice - 1];

        System.out.println("Enter blood group:");
        String bloodGroup = scanner.nextLine();
        System.out.println("Enter phone number:");
        String phoneNumber = scanner.nextLine();

        CustomerDetails customer = new CustomerDetails(name, destination, bloodGroup, phoneNumber);
        reservations.add(customer);

        System.out.println("Reservation created successfully.");
    }

    public void displayCustomerDetails() {
        System.out.println("Customer Details:");
        for (CustomerDetails customer : reservations) {
            System.out.println("Name: " + customer.getName());
            System.out.println("Destination: " + customer.getDestination());
            System.out.println("Blood Group: " + customer.getBloodGroup());
            System.out.println("Phone Number: " + customer.getPhoneNumber());
            System.out.println("Reservation Status: " + (customer.getReservationStatus() ? "success" : "Cancelled"));
            System.out.println();
        }
    }

    public void cancelReservation(String name, String phoneNumber) {
        for (CustomerDetails customer : reservations) {
            if (customer.getName().equals(name) && customer.getPhoneNumber().equals(phoneNumber)) {
                customer.setReservationStatus(false);
                System.out.println("Reservation for " + name + " cancelled successfully.");
                return;
            }
        }
        System.out.println("Reservation not found for the provided name and phone number.");
    }
}

class Cancellation {
    private Reservation reservation;

    public Cancellation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void cancelReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter customer name:");
        String name = scanner.nextLine();
        System.out.println("Enter phone number:");
        String phoneNumber = scanner.nextLine();

        reservation.cancelReservation(name, phoneNumber);
    }
}

class Maintenance {
    private String[] buses = {"Bangalore", "Goa", "Mysore", "Kerala", "Tamil Nadu", "Mumbai", "Maharashtra", "Goa", "Kasargod", "Telangana"};
    private boolean[] busAvailability = new boolean[buses.length];

    public Maintenance() {
        for (int i = 0; i < busAvailability.length; i++) {
            busAvailability[i] = true;
        }
    }

    public String[] getBuses() {
        return buses;
    }

    public void displayBusDestinations() {
        System.out.println("Bus Destinations:");
        for (int i = 0; i < buses.length; i++) {
            String availability = busAvailability[i] ? "Available" : "Not Available";
            System.out.println("Bus " + (i + 1) + " to " + buses[i] + " - " + availability);
        }
    }

    // Method to check if a specific bus is available
    public boolean isBusAvailable(int busNumber) {
        if (busNumber >= 1 && busNumber <= busAvailability.length) {
            return busAvailability[busNumber - 1];
        }
        return false;
    }
    public void updateBusAvailability(int busNumber, boolean availability) {
        if (busNumber >= 1 && busNumber <= busAvailability.length) {
            busAvailability[busNumber - 1] = availability;
        }
    }
}

class Employee {
    private Reservation reservation;
    private Cancellation cancellation;

    public Employee(Reservation reservation, Cancellation cancellation) {
        this.reservation = reservation;
        this.cancellation = cancellation;
    }

    public void createReservation() {
        reservation.createReservation();
    }

    public void cancelReservation() {
        cancellation.cancelReservation();
    }
}

public class Main {
    public static void updateBusAvailability(Scanner scanner, Maintenance maintenance) {
        System.out.println("Enter the bus number you want to update: ");
        int busNumber = scanner.nextInt();
        if (busNumber >= 1 && busNumber <= maintenance.getBuses().length) {
            System.out.println("Enter the availability (true for Available /false for Not Available) for Bus " + busNumber + ": ");
            boolean availability = scanner.nextBoolean();
            maintenance.updateBusAvailability(busNumber, availability);
            System.out.println("Bus " + busNumber + " availability updated.");
            maintenance.displayBusDestinations();
        } else {
            System.out.println("Invalid bus number.");
        }
    }

    public static void main(String[] args) {
        Reservation reservation = new Reservation();
        Cancellation cancellation = new Cancellation(reservation);
        Employee employee = new Employee(reservation, cancellation);
        Maintenance maintenance = new Maintenance();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create Reservation");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. Display Customer Details");
            System.out.println("4. Display Bus Destinations and their availability"); 
            System.out.println("5. Update Bus Availability");
            System.out.println("6. Exit");
            System.out.println("Enter your choice:");

            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    employee.createReservation();
                    break;
                case 2:
                    employee.cancelReservation();
                    break;
                case 3:
                    reservation.displayCustomerDetails();
                    break;
                case 4:
                    maintenance.displayBusDestinations();
                    break;
                case 5:
                    updateBusAvailability(scanner, maintenance);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter again.");
            }
        }
    }
}


