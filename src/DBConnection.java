import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {

        String url = "jdbc:mysql://localhost:3306/online_banking";
        String username = "root";
        String password = "7861";

        try {

            Connection connection =
                    DriverManager.getConnection(url, username, password);

            System.out.println("Database connected successfully!");

            return connection;

        } catch (SQLException e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();

            return null;
        }
    }
}