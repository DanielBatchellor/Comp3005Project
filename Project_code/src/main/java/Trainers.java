import java.sql.*;
import java.util.Scanner;

public class Trainers {
    static String url = "jdbc:postgresql://localhost:5432/Health_and_fitness";
    static String user = "postgres";
    static String password = "Coding2Sell"; //left blank put in own password
    static Connection conn;

    /**
     * scheduleManagement
     * allows trainers to add or remove avaiblities
     * @param email - the email of the trainer
     */
    public static void scheduleManagement(String email){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            Scanner scan = new Scanner(System.in);
            System.out.print("Would you like to add(A) or remove(R) an avaiblitiy");
            String choice = scan.nextLine();
            switch (choice){
                case "A" -> addTime(email);
                case "R" -> removeTime(email);
                default -> System.out.println("Nothing was chosen");
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * removeTime
     * allows the trainer to remove avaiblities
     * @param email - the email of the trainer
     * @throws SQLException - if it does not connect to the database
     */
    private static void removeTime(String email) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Avaiblitiy WHERE trainer_id IN (SELECT trainer_id FROM Trainers WHERE email = '"+email+"')");
        while(resultSet.next()){
            System.out.println("id: "+resultSet.getInt("avaiblitiy_id")+", start date: "+resultSet.getTimestamp("avaiblitiy_date_start")+", end date: "+resultSet.getTimestamp("avaiblitiy_date_end"));
        }
        System.out.print("Choose the id of the time that you want to get rid of");
        Scanner scan = new Scanner(System.in);
        String time = scan.nextLine();
        statement.executeUpdate("DELETE FROM Avaiblitiy WHERE avaiblitiy_id = "+time);
        System.out.println("Time deleted");
    }

    /**
     * addTime
     * allows trainers to add availbities
     * @param email - the email of the trainer
     * @throws SQLException - if it does not connect to the database
     */
    private static void addTime(String email) throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Add a start time in the format(yyyy-mm-dd hh:mm:ss): ");
        String start = scan.nextLine();
        System.out.print("Add an end time in the format(yyyy-mm-dd hh:mm:ss): ");
        String end = scan.nextLine();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT trainer_id FROM Trainers WHERE email = '"+email+"'");
        resultSet.next();
        statement.executeUpdate("INSERT INTO Avaiblitiy (avaiblitiy_date_start, avaiblitiy_date_end, trainer_id) VALUES ('"+start+"', '"+end+"', "+resultSet.getInt("trainer_id"));
        System.out.println("Avaiblitiy added");
    }

    /**
     * memberProfileViewing
     * allows trainers to look for members by searching the member's name
     */
    public static void memberProfileViewing(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the members first name: ");
        String fname = scan.nextLine();
        System.out.print("Enter the members last name: ");
        String lname = scan.nextLine();
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);

            Statement statement = conn.createStatement();
            statement.executeQuery("SELECT * FROM Members WHERE first_name = '"+fname+"' AND last_name = '"+lname+"'");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                System.out.println("name: "+resultSet.getString("first_name")+" "+resultSet.getString("last_name")
                        +"\nweight goal: "+resultSet.getFloat("weight_goal")+"\ntime goal: "+resultSet.getTimestamp("time_goal")
                        +"\nblood pressure: "+resultSet.getString("blood_pressure")+"\nphysical ability: "+resultSet.getString("physical_ability"));
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
