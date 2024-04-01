import java.sql.*;
import java.util.Scanner;

public class Staff {
    static String url = "jdbc:postgresql://localhost:5432/Health_and_fitness";
    static String user = "postgres";
    static String password = "Coding2Sell"; //left blank put in own password
    static Connection conn;

    /**
     * roomBookingManagement
     * allows staff to book rooms for classes
     */
    public static void roomBookingManagement(){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);

            Statement statement = conn.createStatement();
            Scanner scanner = new Scanner(System.in);

            System.out.print("Would you like to Delete a booking(D) or Add a booking(A): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "D" -> deleteBooking(statement);
                case "A" -> addBooking(statement);
                default -> {
                    System.out.println("Nothing chosen");
                }
            }

        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * deleteBooking
     * allows staff to delete bookings
     * @param statement - to delete bookings from the database
     * @throws SQLException - if statement does not work
     */
    private static void deleteBooking(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Classes");
        while(resultSet.next()){
            System.out.println("id: "+resultSet.getInt("class_id")+", class: "+resultSet.getString("name_of_class")+", location: "+resultSet.getString("location")+", start date: "+resultSet.getTimestamp("date_start")+", end date: "+resultSet.getTimestamp("date_end"));
        }

        System.out.print("choose an id: ");
        Scanner scan = new Scanner(System.in);
        String id = scan.nextLine();

        statement.executeUpdate("DELETE FROM BookedRooms WHERE class_id="+id);
        statement.executeUpdate("DELETE FROM Classes WHERE class_id="+id);
        System.out.println("class was properly deleted");
    }

    /**
     * addBooking
     * allows staff to add bookings
     * @param statement - to add bookings to the database
     * @throws SQLException - if statement does not work
     */
    private static void addBooking(Statement statement) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the class: ");
        String name = scanner.nextLine();
        System.out.print("Enter the name of the room: ");
        String location = scanner.nextLine();
        System.out.print("Enter the start time: ");
        String sTime = scanner.nextLine();
        System.out.print("Enter the end time: ");
        String eTime = scanner.nextLine();

        statement.executeUpdate("INSERT INTO Classes (name_of_class, location, date_start, date_end) VALUES ('"+name+"', '"+location+"', '"+sTime+"', '"+eTime+"')");
        ResultSet resultSet = statement.executeQuery("SELECT class_id FROM Classes");
        while (resultSet.next()){
            if (resultSet.isLast()){
                statement.executeUpdate("INSERT INTO BookedRooms (name_of_room, class_id, date_start, date_end) VALUES ('"+location+"', "+resultSet.getInt("class_id")+", '"+sTime+"', '"+eTime+"')");
            }
        }
    }

    /**
     * equipmentMaintenanceMonitoring
     * allows staff to monitor equipment
     */
    public static void equipmentMaintenanceMonitoring(){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);

            Statement statement = conn.createStatement();
            statement.executeQuery("SELECT * FROM ExerciseEquipment");

            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                System.out.println("id: "+resultSet.getInt("equipment_id")+", name: "+resultSet.getString("equipment_name")+", working: "+resultSet.getBoolean("working"));
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * classScheduleUpdating
     * allows staff to update information on a class
     */
    public static void classScheduleUpdating(){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Classes");

            while(resultSet.next()){
                System.out.println("id: "+resultSet.getInt("class_id")+", class: "+resultSet.getString("name_of_class")+", location: "+resultSet.getString("location")+", start date: "+resultSet.getTimestamp("date_start")+", end date: "+resultSet.getTimestamp("date_end"));
            }

            Scanner scan = new Scanner(System.in);
            System.out.print("Choose an id: ");
            String id = scan.nextLine();
            while(true){
                System.out.print("do you want to change the name of the class(C), the location(L), or the time(T). anything else to exit: ");
                String choice = scan.nextLine();
                switch (choice){
                    case "C":
                        System.out.print("Enter name: ");
                        String name = scan.nextLine();
                        statement.executeUpdate("UPDATE Classes SET name_of_class = '"+name+"' WHERE class_id="+id);
                        System.out.println("Class updated");
                        break;
                    case "L":
                        System.out.print("Enter location: ");
                        String location = scan.nextLine();
                        statement.executeUpdate("UPDATE Classes SET location = '"+location+"' WHERE class_id="+id);
                        statement.executeUpdate("UPDATE BookedRooms SET name_of_room = '"+location+"' WHERE class_id="+id);
                        System.out.println("Class updated");
                        break;
                    case "T":
                        System.out.print("Enter start time (yyyy-mm-dd hh:mm:ss): ");
                        String sTime = scan.nextLine();
                        System.out.print("Enter end time (yyyy-mm-dd hh:mm:ss): ");
                        String eTime = scan.nextLine();
                        statement.executeUpdate("UPDATE Classes SET date_start = '"+sTime+"', date_end = '"+eTime+"' WHERE class_id="+id);
                        statement.executeUpdate("UPDATE BookedRooms SET date_start = '"+sTime+"', date_end = '"+eTime+"' WHERE class_id="+id);
                        System.out.println("Class updated");
                        break;
                    default: return;
                }
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * billing
     * allows staff to add, update, or remove bills
     */
    public static void billing(){
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);

            Statement statement = conn.createStatement();
            Scanner scanner = new Scanner(System.in);
            while(true) {
                System.out.print("Would you like to Cancel a bill(C), Make a new Bill(M), Update a Bill(U) or exit(type anything): ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "C" -> cancelBill(statement);
                    case "M" -> newBill(statement);
                    case "U" -> updateBill(statement);
                    default -> {
                        return;
                    }
                }
            }
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * updateBill
     * updates the bill that the staff chooses based on what the staff wants to be updated
     * @param statement - to update bills in the database
     * @throws SQLException - if statement does not work
     */
    private static void updateBill(Statement statement) throws SQLException {
        statement.executeQuery("SELECT * FROM Bills");
        ResultSet resultSet = statement.getResultSet();
        Scanner scan = new Scanner(System.in);
        while(resultSet.next()){
            System.out.println("id: "+resultSet.getInt("bill_id")+", member id: "+resultSet.getInt("member_id")+", price: "+resultSet.getFloat("price")+", name of the bill: "+resultSet.getString("name_of_bill")+", has the bill been paid: "+resultSet.getBoolean("is_paid"));
        }

        System.out.print("Enter the id: ");
        String id = scan.nextLine();
        while(true) {
            System.out.print("How do you want to update the bill price(P), name(N), or if it has been paid(I). Anything else to exit: ");
            String choice = scan.nextLine();
            switch(choice){
                case "P":
                    System.out.print("Enter new price: ");
                    String newPrice = scan.nextLine();
                    statement.executeUpdate("UPDATE Bills SET price = "+newPrice+" WHERE bill_id="+id);
                    System.out.println("Bill updated");
                    break;
                case "N":
                    System.out.print("Enter new name: ");
                    String newName = scan.nextLine();
                    statement.executeUpdate("UPDATE Bills SET name_of_bill = '"+newName+"' WHERE bill_id="+id);
                    break;
                case "I":
                    System.out.print("Enter T if paid or F if not: ");
                    if (scan.nextLine().equals("T")){
                        statement.executeUpdate("UPDATE Bills SET is_paid = TRUE WHERE bill_id="+id);
                    }else{
                        statement.executeUpdate("UPDATE Bills SET is_paid = FALSE WHERE bill_id="+id);
                    }
                default:
                    System.out.println("Nothing chosen");
                    return;
            }
        }
    }

    /**
     * newBill
     * adds a new bill to the database
     * @param statement - to add new bills in the database
     * @throws SQLException - if statement does not work
     */
    private static void newBill(Statement statement) throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.println("New Bill being made");
        System.out.print("Enter member id: ");
        String memId = scan.nextLine();
        System.out.print("Enter price of bill: ");
        String price = scan.nextLine();
        System.out.print("Enter the name of the bill: ");
        String billName = scan.nextLine();
        System.out.print("is the bill paid true(TRUE) or False(FALSE): ");
        String isPaid = scan.nextLine();
        boolean b = isPaid.equals("TRUE");

        statement.executeUpdate("INSERT INTO Bills (member_id, price, name_of_bill, is_paid) VALUES ("+memId+", "+price+", '"+billName+"', "+b+")");
        System.out.println("Bill has been added");
    }

    /**
     * cancelBill
     * deletes bills from the database
     * @param statement - to delete bills from the database
     * @throws SQLException - if the statement does not work
     */
    private static void cancelBill(Statement statement) throws SQLException {
        statement.executeQuery("SELECT * FROM Bills");
        ResultSet resultSet = statement.getResultSet();

        while(resultSet.next()){
            System.out.println("id: "+resultSet.getInt("bill_id")+", member id: "+resultSet.getInt("member_id")+", price: "+resultSet.getFloat("price")+", name of the bill: "+resultSet.getString("name_of_bill")+", has the bill been paid(1 for yes, 0 for no): "+resultSet.getBoolean("is_paid"));
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the id of the bill you want to cancel: ");
        String id = scanner.nextLine();
        statement.executeUpdate("DELETE FROM Bills WHERE bill_id = "+id);
        System.out.println("Bill canceled");
    }
}
