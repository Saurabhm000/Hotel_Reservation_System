import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Admin@123";

    // MAIN FUNCTION STARTS HERE
    public static void main(String[] args) throws Exception {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Boolean button = true;
        Scanner scanner = new Scanner(System.in);

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            

            while (button == true) {
                System.out.println();
                System.out.println("-----HOTEL RESERVATION SYSTEM-----");
                System.out.println();
                System.out.println("1 - New Reservation");
                System.out.println("2 - View Reservation");
                System.out.println("3 - Find Room No");
                System.out.println("4 - Update Reservation");
                System.out.println("5 - Delete Reservation");
                System.out.println("6 - Exit");
                System.out.println();
                System.out.print("Choose an Option : ");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        newReservation(con, stmt, scanner);
                        break;
                    case 2:
                        viewReservation(con, stmt, scanner);
                        break;
                    case 3:
                        findRoomno(con, stmt, scanner);
                        break;
                    case 4:
                        updateReservation(con, stmt, scanner);
                        break;
                    case 5:
                        deleteReservation(con, stmt, scanner);
                        break;
                    case 6:
                        scanner.close();
                        stmt.close();
                        con.close();
                        exit();
                        return;
                    default:
                        System.out.println("Invalid Choice Try Again !!!");
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("WARINING !! WRONG CHOICE ");
            scanner.nextLine();
        }

    }

    // MAKE NEW RESERVATION FUNCTION

    private static void newReservation(Connection con, Statement stmt, Scanner scanner) {
        System.out.print("Enter Name : ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Enter Room No : ");
        int roomno = scanner.nextInt();
        System.out.print("Enter Contact No : ");
        String contactno = scanner.next();

        if (!occupancyCheck(con, stmt, roomno)) {

            String query = "INSERT INTO RESERVATIONS(name,room_no,contact_no) " +
                    "VALUES ('" + name + "'," + roomno + ",'" + contactno + "')";

            try {
                int affectedrow = stmt.executeUpdate(query);
                if (affectedrow > 0) {
                    System.out.println();
                    System.out.println("Reservation Added Successfully!!");
                } else {
                    System.err.println("!! Reservation Add Failed !!");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } 
        else {
            System.out.println();
            System.out.println("-----------------------");
            System.out.println("Room Already Booked !!");
        }
    }

    // VIEW RESERVATION FUNCTION

    private static void viewReservation(Connection con, Statement stmt, Scanner scanner) {
        try {
            String query = "SELECT * FROM reservations";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int roomno = rs.getInt("room_no");
                String contactno = rs.getString("contact_no");
                String date = rs.getTimestamp("date").toString();

                System.out.println("ID - " + id + "| NAME - " + name + "| CONTACTNO - " + contactno + "| ROOM NO - "
                        + roomno + "| DATE - " + date);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // DELETE RESERVATION FUNCTION
    private static void deleteReservation(Connection con, Statement stmt, Scanner scanner) {

        System.out.print("Enter reservation Id to delete : ");
        int id = scanner.nextInt();

        String query = "DELETE FROM reservations where id = " + id;

        try {
            int affectedrow = stmt.executeUpdate(query);
            if (affectedrow > 0) {
                System.out.println();
                System.out.println("Reservation Deleted Successfully!!");
            } else {
                System.err.println("!! Reservation Deletion Failed !!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // UPDATE RESERVATION FUNCTION

    private static void updateReservation(Connection con, Statement stmt, Scanner scanner) {

        System.out.print("Enter reservation Id to update : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if ((reservationExists(con, stmt, id)) == true) {

            System.out.print("Enter new guest name : ");
            String name = scanner.nextLine();
            System.out.print("Enter new contact No : ");
            String contactno = scanner.next();

            String query = "UPDATE RESERVATIONS SET name = '" + name + "'," +
                    "contact_no = '" + contactno + "' " +
                    "WHERE ID = " + id;

            try {
                int affectedrow = stmt.executeUpdate(query);
                if (affectedrow > 0) {
                    System.out.println();
                    System.out.println("Reservation Updated Successfully!!");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.err.println("!! Reservation Updation Failed !!");
            System.out.println("Reservation not exist");
        }

    }

    // RESERVATION EXIST CHECK FUNCTION
    private static Boolean reservationExists(Connection con, Statement stmt, int id) {

        String query = "SELECT id FROM reservations where id = " + id;
        try {
            ResultSet n = stmt.executeQuery(query);
            return n.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // FIND ROOM NO FUNTION

    private static void findRoomno(Connection con, Statement stmt, Scanner scanner) throws SQLException {

        System.out.print("Enter Id : ");
        int id = scanner.nextInt();
        System.out.print("Enter name : ");
        scanner.nextLine();
        String name = scanner.nextLine();
        String query = "SELECT room_no FROM reservations " +
                "WHERE ID = " + id + " AND name = '" + name + "'";

        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            int room_no = rs.getInt("room_no");
            System.out.println();
            System.out.println("The Room no of " + name + " is : " + room_no);
        }

    }

    //ROOM OCCUPANCY CHECK FUNCTION

    private static Boolean occupancyCheck(Connection con, Statement stmt, int roomno) {
        try {
            String query = "SELECT id FROM reservations WHERE room_no = " + roomno;
            ResultSet rs = stmt.executeQuery(query);
            boolean checker = rs.next();
            return checker;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // EXIT FUNCTION
    private static void exit() throws InterruptedException {

        System.out.print("Exiting System");
        int i = 5;
        while (i != 0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("ThankYou For Using Hotel Reservation System!!!");

    }
}