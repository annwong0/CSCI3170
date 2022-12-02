package src;

import java.sql.*;
import java.util.Scanner;

public class Manager {

    public static String DB_URL = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/Group53?autoReconnect=true&useSSL=false";
    public static String USER = "Group53";
    public static String PASS = "CSCI3170";
    static Connection conn = null;

    public static void menu() throws SQLException {
        String opening = "What kinds of operation would you like to perform?\n" +
                "1. List all salepersons\n" +
                "2. Count the no. of sales record of each salesperson under a specific range on years of experience\n" +
                "3. Show the total sales value of each manufacturer\n" +
                "4. Show the N most popular part\n" +
                "5. Return to the main menu\n" +
                "Enter Your Choice: ";

        while (true) {
            System.out.print(opening);
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();

            if (choice == 1) {
                listAllSalespersons();
            } else if (choice == 2) {
                countSalesRecords();
            } else if (choice == 3) {
                showTotalSalesValue();
            } else if (choice == 4) {
                showPopularPart();
            } else if (choice == 5) {
                break;
            }
        }
    }
    public static void listAllSalespersons() throws SQLException {
        Statement stmt = null;
        String string = "Choose ordering:\n" +
                "1. By ascending order\n" +
                "2. By descending order\n" +
                "Choose the list ordering: ";

        try {
            System.out.print(string);
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();

            String query = "SELECT s_id, s_name, s_phone_number, s_experience\n" +
                    "FROM salesperson\n" +
                    "ORDER BY s_experience ";
            String order = "";

            if (choice == 1) {
                order = "ASC";
            } else if (choice == 2) {
                order = "DESC";
            }

            String sql = query + order;
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("| ID | Name | Mobile Phone | Years of Experience |");
            while (rs.next()) {
                System.out.print("| " + rs.getInt(1) + " ");
                System.out.print("| " + rs.getString(2) + " ");
                System.out.print("| " + rs.getInt(3) + " ");
                System.out.print("| " + rs.getInt(3) + " ");
                System.out.print("|");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void countSalesRecords() throws SQLException {
        Statement stmt = null;

        try {
            System.out.println("Type in the lower bound for years of experience: ");
            Scanner input1 = new Scanner(System.in);
            int lower = input1.nextInt();

            System.out.println("Type in the upper bound for years of experience: ");
            Scanner input2 = new Scanner(System.in);
            int upper = input2.nextInt();

            String sql = "SELECT s.s_id, s.s_name, s.s_experience, COUNT(t.s_id)\n" +
                    "FROM salesperson s LEFT JOIN\n" +
                    "transaction t ON t.s_id = s.s_id\n" +
                    "WHERE s.s_experience >= " + lower + "AND s.s_experience <= " + upper + "\n" +
                    "GROUP BY s.s_id\n" +
                    "ORDER BY s.s_id DESC";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("| ID | Name | Years of Experience | Number of Transaction |");
            while (rs.next()) {
                System.out.print("| " + rs.getInt(1) + " ");
                System.out.print("| " + rs.getString(2) + " ");
                System.out.print("| " + rs.getInt(3) + " ");
                System.out.print("| " + rs.getInt(4) + " ");
                System.out.println(("|"));
            }
            System.out.println("End of Query");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showTotalSalesValue() throws SQLException {
        Statement stmt = null;
        String sql = "SELECT m.mid, m.mname, SUM(p.pprice) AS [Total Sales Value]\n" +
                "FROM manufacturer m LEFT JOIN\n" +
                "part p on p.mid = m.mid\n" +
                "GROUP BY m.mid\n" +
                "ORDER BY SUM(p.pprice) DESC";
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("| Manufacturer ID | Manufacturer Name | Total Sales Value |");
        while (rs.next()) {
            System.out.print("| " + rs.getInt(1) + " ");
            System.out.print("| " + rs.getString(1) + " ");
            System.out.print("| " + rs.getInt(1) + " ");
            System.out.println("|");
        }
        System.out.println("End of Query");
    }

    public static void showPopularPart(){
        Statement stmt = null;

        try {
            System.out.print("Type in the number of parts ");
            Scanner input = new Scanner(System.in);
            int number = input.nextInt();

            String sql = "SELECT p.pid, p.pname, COUNT(t.p_id) AS [No. of Transaction]\n" +
                    "FROM part p LEFT JOIN\n" +
                    "transaction t on t.p_id = p.pid\n" +
                    "GROUP BY p.pid\n" +
                    "ORDER BY COUNT(t.p_id) DESC\n" +
                    "LIMIT " + number;
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("| Part ID | Part Name | No. of Transaction |");
            while (rs.next()) {
                System.out.print("| " + rs.getInt(1) + " ");
                System.out.print("| " + rs.getString(2) + " ");
                System.out.print("| " + rs.getInt(3) + " ");
                System.out.println("|");
            }
            System.out.println("End of Query");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            menu();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
