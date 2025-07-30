import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystem {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/studentdb";
        String user = "root"; // 🔁 Replace with your MySQL username
        String password = "22048"; // 🔁 Replace with your MySQL password
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    try (Connection conn = getConnection()) {
                        System.out.print("Enter ID: ");
                        int id = sc.nextInt();
                        sc.nextLine(); // clear buffer
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Age: ");
                        int age = sc.nextInt();
                        sc.nextLine(); // clear buffer
                        System.out.print("Enter Course: ");
                        String course = sc.nextLine();

                        String sql = "INSERT INTO students (id, name, age, course) VALUES (?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, id);
                        stmt.setString(2, name);
                        stmt.setInt(3, age);
                        stmt.setString(4, course);
                        stmt.executeUpdate();
                        System.out.println("✅ Student added to database!");
                    } catch (SQLException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    try (Connection conn = getConnection()) {
                        String sql = "SELECT * FROM students";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery();
                        System.out.println("\n📋 List of Students:");
                        while (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id"));
                            System.out.println("Name: " + rs.getString("name"));
                            System.out.println("Age: " + rs.getInt("age"));
                            System.out.println("Course: " + rs.getString("course"));
                            System.out.println("---------------------------");
                        }
                    } catch (SQLException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Enter Student ID to search: ");
                    int searchId = sc.nextInt();
                    try (Connection conn = getConnection()) {
                        String sql = "SELECT * FROM students WHERE id = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, searchId);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            System.out.println("ID: " + rs.getInt("id"));
                            System.out.println("Name: " + rs.getString("name"));
                            System.out.println("Age: " + rs.getInt("age"));
                            System.out.println("Course: " + rs.getString("course"));
                        } else {
                            System.out.println("❌ Student not found.");
                        }
                    } catch (SQLException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter Student ID to delete: ");
                    int deleteId = sc.nextInt();
                    try (Connection conn = getConnection()) {
                        String sql = "DELETE FROM students WHERE id = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, deleteId);
                        int rows = stmt.executeUpdate();
                        if (rows > 0) {
                            System.out.println("🗑️ Student deleted successfully.");
                        } else {
                            System.out.println("❌ Student not found.");
                        }
                    } catch (SQLException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.println("👋 Exiting system. Thank you!");
                    break;

                default:
                    System.out.println("⚠️ Invalid choice. Try again.");
            }

        } while (choice != 5);

        sc.close();
    }
}
