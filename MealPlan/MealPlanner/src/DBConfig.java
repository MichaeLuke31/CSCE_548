import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {
	// Default values; override with env vars if desired
	private static final String URL = System.getenv().getOrDefault("MEAL_DB_URL",
			"jdbc:mysql://localhost:3306/mealplanner?serverTimezone=UTC");
	private static final String USER = System.getenv().getOrDefault("MEAL_DB_USER", "root");
	private static final String PASS = System.getenv().getOrDefault("MEAL_DB_PASS", "password");

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}
}
