import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealPlanDAO {

	public MealPlan createMealPlan(MealPlan mp) throws SQLException {
		String sql = "INSERT INTO meal_plans (user_id, name, description) VALUES (?,?,?)";
		try (Connection c = DBConfig.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, mp.getUserId());
			ps.setString(2, mp.getName());
			ps.setString(3, mp.getDescription());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					mp.setId(rs.getInt(1));
			}
		}
		return mp;
	}

	public MealPlan getMealPlanById(int id) throws SQLException {
		String sql = "SELECT * FROM meal_plans WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					MealPlan mp = new MealPlan();
					mp.setId(rs.getInt("id"));
					mp.setUserId(rs.getInt("user_id"));
					mp.setName(rs.getString("name"));
					mp.setDescription(rs.getString("description"));
					Timestamp ts = rs.getTimestamp("created_at");
					if (ts != null)
						mp.setCreatedAt(ts.toInstant());
					return mp;
				}
			}
		}
		return null;
	}

	public List<MealPlan> listMealPlansForUser(int userId) throws SQLException {
		List<MealPlan> out = new ArrayList<>();
		String sql = "SELECT * FROM meal_plans WHERE user_id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					MealPlan mp = new MealPlan();
					mp.setId(rs.getInt("id"));
					mp.setUserId(rs.getInt("user_id"));
					mp.setName(rs.getString("name"));
					mp.setDescription(rs.getString("description"));
					Timestamp ts = rs.getTimestamp("created_at");
					if (ts != null)
						mp.setCreatedAt(ts.toInstant());
					out.add(mp);
				}
			}
		}
		return out;
	}

	public boolean updateMealPlan(MealPlan mp) throws SQLException {
		String sql = "UPDATE meal_plans SET name=?, description=? WHERE id=?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, mp.getName());
			ps.setString(2, mp.getDescription());
			ps.setInt(3, mp.getId());
			return ps.executeUpdate() > 0;
		}
	}

	public boolean deleteMealPlan(int id) throws SQLException {
		String sql = "DELETE FROM meal_plans WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}
}
