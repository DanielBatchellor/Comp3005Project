import java.sql.*;
import java.util.Scanner;

public class Main {
    static String url = "jdbc:postgresql://localhost:5432/Health_and_fitness";
    static String user = "postgres";
    static String password = "Coding2Sell"; //left blank put in own password
    static Connection conn;
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you a Member(M), Trainer(T), Staff(S), or New to the gym(N): ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "M" -> memberLogin();
            case "T" -> trainerLogin();
            case "S" -> staffLogin();
            case "N" -> Members.userRegistration();
            default -> System.out.println("Nothing was chosen");
        }
    }

    /**
     * staffLogin
     * This function is the function for staff to login by typing an email address and password
     */
    private static void staffLogin() {
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.print("Enter email: ");
            String email = scan.nextLine();
            System.out.print("Enter password: ");
            String userPassword = scan.nextLine();

            Statement statement = conn.createStatement();
            statement.executeQuery("SELECT staff_id, first_name FROM Staff WHERE email = '"+email+"' AND password = '"+userPassword+"'");
            ResultSet resultSet = statement.getResultSet();

            //if the staff does not exist
            if (!resultSet.next()){
                System.out.println("Staff member does not exist");
                return;
            }

            System.out.println("Welcome "+resultSet.getString("first_name"));

            //ask the staff what they would like to do
            while(true){
                System.out.print("Would you like to go to Billing(B),  update the class schedule(S), manage room bookings(R), monitor equipment(E), or exit(type anything): ");
                String choice = scan.nextLine();
                switch (choice){
                    case "B" -> Staff.billing();
                    case "S" -> Staff.classScheduleUpdating();
                    case "R" -> Staff.roomBookingManagement();
                    case "E" -> Staff.equipmentMaintenanceMonitoring();
                    default -> {
                        System.out.println("Logging out");
                        return;
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * trainerLogin
     * This function is the function for staff to login by typing an email address and password
     */
    private static void trainerLogin() {
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.print("Enter email: ");
            String email = scan.nextLine();
            System.out.print("Enter password: ");
            String userPassword = scan.nextLine();

            Statement statement = conn.createStatement();
            statement.executeQuery("SELECT trainer_id, first_name FROM Trainers WHERE email = '"+email+"' AND password = '"+userPassword+"'");
            ResultSet resultSet = statement.getResultSet();

            //if the trainer does not exist
            if (!resultSet.next()){
                System.out.println("Trainer does not exist");
                return;
            }

            System.out.println("Welcome "+resultSet.getString("first_name"));

            //ask the trainer what they would like to do
            while(true){
                System.out.print("Would you like to view members profiles(M),  manage your schedule(S), or exit(type anything): ");
                String choice = scan.nextLine();
                switch (choice){
                    case "M" -> Trainers.memberProfileViewing();
                    case "S" -> Trainers.scheduleManagement(email);
                    default -> {
                        System.out.println("Logging out");
                        return;
                    }
                }
            }

        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * memberLogin
     * This function is the function for staff to login by typing an email address and password
     */
    private static void memberLogin() {
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.print("Enter email: ");
            String email = scan.nextLine();
            System.out.print("Enter password: ");
            String userPassword = scan.nextLine();
            Statement statement = conn.createStatement();
            statement.executeQuery("SELECT member_id, first_name FROM Members WHERE email = '"+email+"' AND password = '"+userPassword+"'");
            ResultSet resultSet = statement.getResultSet();

            //if the member does not exist
            if (!resultSet.next()){
                System.out.println("Member does not exist");
                return;
            }

            System.out.println("Welcome "+resultSet.getString("first_name"));

            //ask the member what they would like to do
            while(true){
                System.out.print("Would you like to go to profile management(P), dashboard display(D), schedule management(S), or exit(type anything): ");
                String choice = scan.nextLine();
                switch (choice){
                    case "P" -> Members.profileManagement(email);
                    case "D" -> Members.dashboardDisplay(email);
                    case "S" -> Members.scheduleManagement(email);
                    default -> {
                        System.out.println("Logging out");
                        return;
                    }
                }
            }

        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }


}
