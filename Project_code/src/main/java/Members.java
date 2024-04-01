import java.sql.*;
import java.util.Scanner;

public class Members {
    static String url = "jdbc:postgresql://localhost:5432/Health_and_fitness";
    static String user = "postgres";
    static String password = "Coding2Sell"; //left blank put in own password
    static Connection conn;

    /**
     * userRegistration
     * create a new member for the gym
     */
    public static void userRegistration(){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);

            //ask required information
            Scanner scan = new Scanner(System.in);
            System.out.print("Enter first name: ");
            String first_name = scan.nextLine();
            System.out.print("Enter Last Name: ");
            String last_name = scan.nextLine();
            System.out.print("Enter email: ");
            String email = scan.nextLine();
            System.out.print("Enter password: ");
            String password = scan.nextLine();
            System.out.print("Enter a weight goal in kg: ");
            String weight = scan.nextLine();
            System.out.print("Enter a time goal in the format 'yyyy-mm-dd hh:mm:ss': ");
            String time = scan.nextLine();
            System.out.print("Enter your blood pressure: ");
            String blood = scan.nextLine();
            System.out.print("Enter your physical ability: ");
            String ability = scan.nextLine();


            Statement statement = conn.createStatement();

            //if the email all ready exists
            if(checkIfAlreadyRes(statement, email)){
                System.out.println("A member already exists with this email");
                return;
            }

            //insert into the members table and make a bill
            statement.executeUpdate("INSERT INTO Members(first_name, last_name, email, password, weight_goal, time_goal, blood_pressure, physical_ability) VALUES" +
                    "('"+first_name+"', '"+last_name+"', '"+email+"', '"+password+"', "+weight+", '"+time+"', '"+blood+"', '"+ability+"')");

            System.out.println("The registration was a success");
            ResultSet resultSet = statement.executeQuery("SELECT member_id FROM Members WHERE email = '"+email+"'");
            resultSet.next();
            statement.executeUpdate("INSERT INTO Bills (member_id, price, name_of_bill, is_paid) VALUES ("+resultSet.getInt("member_id")+", 70.00, 'annual fee', FALSE)");
            System.out.println("Bill has been added");

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * profileManagement
     * allows the user to update their profile
     * @param email - the email of the member
     */
    public static void profileManagement(String email){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            Statement statement = conn.createStatement();
            Scanner scan = new Scanner(System.in);
            System.out.print("What would you like to update; PERSONAL, GOALS, or METRICS: ");
            String choice = scan.nextLine();
            switch (choice) {
                case "PERSONAL" -> updatePersonal(statement, email);
                case "GOALS" -> updateGoals(statement, email);
                case "METRICS" -> updateMetrics(statement, email);
                default -> System.out.println("Nothing chosen");
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * updateGoals
     * allows the user to update their fitness goals
     * @param statement - allows to update the database
     * @param email - the email of the member
     * @throws SQLException - if statement does not work
     */
    private static void updateGoals(Statement statement, String email) throws SQLException {
        System.out.print("Do you want to update your time goal(T) or your weight goal(G): ");
        Scanner scan = new Scanner(System.in);
        String choice = scan.nextLine();
        if (choice.equals("T")){
            System.out.print("Enter time in format (yyyy-mm-dd hh:mm:ss): ");
            String time = scan.nextLine();
            statement.executeUpdate("UPDATE Members SET time_goal = "+time+" WHERE email = '"+email+"'");
            System.out.println("Time goal updated");
        }else if(choice.equals("G")){
            System.out.print("Enter weight in Kg: ");
            String weight = scan.nextLine();
            statement.executeUpdate("UPDATE Members SET weight_goal = "+weight+" WHERE email = '"+email+"'");
            System.out.println("Weight goal updated");
        }else{
            System.out.println("Nothing chosen");
        }
    }

    /**
     * updateMetrics
     * allows the user to update their health values
     * @param statement - allows to update the database
     * @param email - the email of the member
     * @throws SQLException - if statement does not work
     */
    private static void updateMetrics(Statement statement, String email) throws SQLException {
        System.out.println("Do you want to update your blood pressure(B) or physical ability(P): ");
        Scanner scan = new Scanner(System.in);
        String choice = scan.nextLine();
        if (choice.equals("B")){
            System.out.print("Enter your blood pressure (num1/num2): ");
            String bp = scan.nextLine();
            statement.executeUpdate("UPDATE Members SET blood_pressure = '"+bp+"' WHERE email = '"+email+"'");
        }else if (choice.equals("P")){
            System.out.print("Enter your physical ability: ");
            String pa = scan.nextLine();
            statement.executeUpdate("UPDATE Members SET physical_ability = '"+pa+"' WHERE email = '"+email+"'");
        }else{
            System.out.println("Nothing chosen");
        }
    }

    /**
     * updatePersonal
     * allows the user to change their name, email, or password
     * @param statement - allows to update the database
     * @param email - the email of the member
     * @throws SQLException - if statement does not work
     */
    private static void updatePersonal(Statement statement, String email) throws SQLException {
        Scanner scan = new Scanner(System.in);

        while(true){
            System.out.print("Do you want to update your name(N), Email(E), password(P), or anything else to exit: ");
            String choice = scan.nextLine();
            switch (choice) {
                case "N" -> {
                    System.out.print("Enter new first name: ");
                    String fname = scan.nextLine();
                    System.out.print("Enter new last name: ");
                    String lname = scan.nextLine();
                    statement.executeUpdate("UPDATE Members SET first_name = '" + fname + "' last_name = '" + lname + "' WHERE email = '" + email + "'");
                }
                case "E" -> {
                    System.out.print("Enter new email: ");
                    String newEmail = scan.nextLine();
                    statement.executeQuery("SELECT email FROM Members WHERE email = '" + newEmail + "'");
                    ResultSet r = statement.getResultSet();
                    if (!r.next()) {
                        statement.executeQuery("SELECT member_id FROM Members WHERE email = '" + email + "'");
                        ResultSet resultSet = statement.getResultSet();
                        resultSet.next();
                        statement.executeUpdate("UPDATE Members SET email = '" + newEmail + "' WHERE member_id = " + resultSet.getInt("member_id"));
                    } else {
                        System.out.println("This email is already being used");
                    }
                }
                case "P" -> {
                    System.out.print("Enter new password: ");
                    String newPassword = scan.nextLine();
                    statement.executeUpdate("UPDATE Members SET password = '" + newPassword + "' WHERE email = '" + email + "'");
                }
                default -> {
                    System.out.println("Exiting from update information");
                    return;
                }
            }
        }
    }

    /**
     * dashboardDisplay
     * show the goals and health of the member
     * @param email - the email of the member
     */
    public static void dashboardDisplay(String email){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT weight_goal, time_goal, blood_pressure, physical_ability FROM Members WHERE email = '"+email+"'");
            resultSet.next();
            System.out.println("Weight goal: "+resultSet.getString("weight_goal"));
            System.out.println("Time goal: "+resultSet.getString("time_goal"));
            System.out.println("Blood pressure: "+resultSet.getString("blood_pressure"));
            System.out.println("Physical ability: "+resultSet.getString("physical_ability"));
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * scheduleManagement
     * add personal training exercises for members
     * @param email - the email of the member
     */
    public static void scheduleManagement(String email){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            Scanner scan = new Scanner(System.in);
            System.out.print("Input the trainer first name: ");
            String fname = scan.nextLine();
            System.out.print("Input the trainer last name: ");
            String lname = scan.nextLine();
            Statement statement = conn.createStatement();
            statement.executeQuery("SELECT * FROM Trainers WHERE first_name = '"+fname+"' AND last_name = '"+lname+"'");
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            statement.executeQuery("SELECT * FROM Avaiblitiy WHERE trainer_id = "+resultSet.getInt("trainer_id"));
            ResultSet resultSet1 = statement.getResultSet();

            //show the avaiblities
            while(resultSet1.next()){
                System.out.println("id: "+resultSet1.getInt("avaiblitiy_id")+", Trainer: "+fname+" "+lname+", avaiblitiy start: "+resultSet1.getTimestamp("avaiblitiy_date_start")+", avaiblitiy start: "+resultSet1.getTimestamp("avaiblitiy_date_end"));
            }


            System.out.println("Choose an avaiblitiy");
            String id = scan.nextLine();
            Statement statement2 = conn.createStatement();
            statement2.executeQuery("SELECT * FROM Avaiblitiy WHERE avaiblitiy_id = "+id);
            ResultSet resultSet2 = statement2.getResultSet();
            resultSet2.next();
            Statement statement1 = conn.createStatement();
            resultSet = statement1.executeQuery("SELECT * FROM Trainers WHERE first_name = '"+fname+"' AND last_name = '"+lname+"'");
            resultSet.next();
            boolean group = checkIfGroup(resultSet2, statement);

            if (group){ //ask the user if they would like for a group training exercise if no then exit, if yes then add them in then exit
                System.out.print("This is a group time slot would you like to join answer yes or no: ");
                String question = scan.nextLine();
                if (question.equals("yes")) {
                    statement.executeQuery("SELECT member_id FROM Members WHERE email = '" + email + "'");
                    ResultSet resultSet3 = statement.getResultSet();
                    resultSet3.next();
                    int mem = resultSet3.getInt("member_id");
                    statement.executeUpdate("INSERT INTO Exercise (trainer_id, member_id, exercise_date, exercise_type) VALUES (" + resultSet.getInt("trainer_id") + ", " + mem + ", '" + resultSet2.getTimestamp("avaiblitiy_date_start") + "', 'Group')");
                    System.out.println("Exercise added");
                    statement.executeUpdate("INSERT INTO Bills (member_id, price, name_of_bill, is_paid) VALUES (" + mem + ", 60.00, 'Group Workout', FALSE)");
                    System.out.println("Bill has been added");
                }

            }else{//ask if they would like a personal or group training, if personal add the exercise and remove the avaibility if group add the exercise and keep the avaibility

                System.out.print("Would you like for a personal(P) or group(G) train: ");
                String choice = scan.nextLine();
                statement.executeQuery("SELECT member_id FROM Members WHERE email = '"+email+"'");
                ResultSet resultSet3 = statement.getResultSet();
                resultSet3.next();
                int mem = resultSet3.getInt("member_id");
                if (choice.equals("P")){
                    statement.executeUpdate("INSERT INTO Exercise (trainer_id, member_id, exercise_date, exercise_type) VALUES ("+resultSet.getInt("trainer_id")+", "+mem+", '"+resultSet2.getTimestamp("avaiblitiy_date_start")+"', 'Personal')");
                    statement.executeUpdate("INSERT INTO Bills (member_id, price, name_of_bill, is_paid) VALUES ("+mem+", 60.00, 'Personal Workout', FALSE)");
                    System.out.println("Exercise added");
                    System.out.println("Bill has been added");
                    statement.executeUpdate("DELETE FROM Avaiblitiy WHERE avaiblitiy_id = "+id);
                }else if (choice.equals("G")){
                    statement.executeUpdate("INSERT INTO Exercise (trainer_id, member_id, exercise_date, exercise_type) VALUES ("+resultSet.getInt("trainer_id")+", "+mem+", '"+resultSet2.getTimestamp("avaiblitiy_date_start")+"', 'Group')");
                    statement.executeUpdate("INSERT INTO Bills (member_id, price, name_of_bill, is_paid) VALUES ("+mem+", 60.00, 'Group Workout', FALSE)");
                    System.out.println("Exercise added");
                    System.out.println("Bill has been added");
                }else{
                    System.out.println("Nothing was chosen exiting");
                }

            }

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * checkIfGroup
     * checks if an exercise is a group exercise
     * @param resultSet2 - information in the database
     * @param statement - to find information in the database
     * @return a boolean
     * @throws SQLException - if resultSet2 or statement does not work
     */
    private static boolean checkIfGroup(ResultSet resultSet2, Statement statement) throws SQLException {
        statement.executeQuery("SELECT * FROM Exercise WHERE trainer_id = "+resultSet2.getInt("trainer_id")+" AND exercise_date = '"+resultSet2.getTimestamp("avaiblitiy_date_start")+"'");
        ResultSet resultSet = statement.getResultSet();
        return resultSet.next();
    }

    /**
     * checkIfAlreadyRes
     * checks to see if there is a member with the same email
     * @param statement - to find information in the database
     * @param email - the email of the person who is registering
     * @return a boolean
     * @throws SQLException - if statement does not work
     */
    private static boolean checkIfAlreadyRes(Statement statement, String email) throws SQLException {
        statement.executeQuery("SELECT member_id FROM Members WHERE email = '"+email+"'");
        ResultSet resultSet = statement.getResultSet();
        return resultSet.next();
    }
}
