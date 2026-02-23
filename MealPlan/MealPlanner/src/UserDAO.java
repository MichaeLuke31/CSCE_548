import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

	public User createUser(User u) throws SQLException {
		String sql = "INSERT INTO users (username, daily_calories, daily_protein_g, daily_carbs_g, daily_fat_g, monthly_budget) VALUES (?,?,?,?,?,?)";
		try (Connection c = DBConfig.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, u.getUsername());
			ps.setInt(2, u.getDailyCalories());
			ps.setInt(3, u.getDailyProteinG());
			ps.setInt(4, u.getDailyCarbsG());
			ps.setInt(5, u.getDailyFatG());
			ps.setDouble(6, u.getMonthlyBudget());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					u.setId(rs.getInt(1));
			}
		}
		return u;
	}

	public User getUserById(int id) throws SQLException {
		String sql = "SELECT * FROM users WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					User u = new User();
					u.setId(rs.getInt("id"));
					u.setUsername(rs.getString("username"));
					u.setDailyCalories(rs.getInt("daily_calories"));
					u.setDailyProteinG(rs.getInt("daily_protein_g"));
					u.setDailyCarbsG(rs.getInt("daily_carbs_g"));
					u.setDailyFatG(rs.getInt("daily_fat_g"));
					u.setMonthlyBudget(rs.getDouble("monthly_budget"));
					Timestamp ts = rs.getTimestamp("created_at");
					if (ts != null)
						u.setCreatedAt(ts.toInstant());
					return u;
				}
			}
		}
		return null;
	}

	public List<User> listUsers() throws SQLException {
		List<User> out = new ArrayList<>();
		String sql = "SELECT * FROM users";
		try (Connection c = DBConfig.getConnection();
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery(sql)) {
			while (rs.next()) {
				User u = new User();
				u.setId(rs.getInt("id"));
				u.setUsername(rs.getString("username"));
				u.setDailyCalories(rs.getInt("daily_calories"));
				u.setDailyProteinG(rs.getInt("daily_protein_g"));
				u.setDailyCarbsG(rs.getInt("daily_carbs_g"));
				u.setDailyFatG(rs.getInt("daily_fat_g"));
				u.setMonthlyBudget(rs.getDouble("monthly_budget"));
				Timestamp ts = rs.getTimestamp("created_at");
				if (ts != null)
					u.setCreatedAt(ts.toInstant());
				out.add(u);
			}
		}
		return out;
	}

	public boolean updateUser(User u) throws SQLException {
		String sql = "UPDATE users SET username=?, daily_calories=?, daily_protein_g=?, daily_carbs_g=?, daily_fat_g=?, monthly_budget=? WHERE id=?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, u.getUsername());
			ps.setInt(2, u.getDailyCalories());
			ps.setInt(3, u.getDailyProteinG());
			ps.setInt(4, u.getDailyCarbsG());
			ps.setInt(5, u.getDailyFatG());
			ps.setDouble(6, u.getMonthlyBudget());
			ps.setInt(7, u.getId());
			return ps.executeUpdate() > 0;
		}
	}

	public boolean deleteUser(int id) throws SQLException {
		String sql = "DELETE FROM users WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}
}
