import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealPlanItemDAO {

	public MealPlanItem createMealPlanItem(MealPlanItem item) throws SQLException {
		String sql = "INSERT INTO meal_plan_items (meal_plan_id, day_of_week, meal_type, food_id, quantity_in_grams, notes) VALUES (?,?,?,?,?,?)";
		try (Connection c = DBConfig.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, item.getMealPlanId());
			ps.setInt(2, item.getDayOfWeek());
			ps.setString(3, item.getMealType());
			ps.setInt(4, item.getFoodId());
			ps.setDouble(5, item.getQuantityInGrams());
			ps.setString(6, item.getNotes());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					item.setId(rs.getInt(1));
			}
		}
		return item;
	}

	public MealPlanItem getMealPlanItemById(int id) throws SQLException {
		String sql = "SELECT * FROM meal_plan_items WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					MealPlanItem i = new MealPlanItem();
					i.setId(rs.getInt("id"));
					i.setMealPlanId(rs.getInt("meal_plan_id"));
					i.setDayOfWeek(rs.getInt("day_of_week"));
					i.setMealType(rs.getString("meal_type"));
					i.setFoodId(rs.getInt("food_id"));
					i.setQuantityInGrams(rs.getDouble("quantity_in_grams"));
					i.setNotes(rs.getString("notes"));
					return i;
				}
			}
		}
		return null;
	}

	public List<MealPlanItem> listMealPlanItems(int mealPlanId) throws SQLException {
		List<MealPlanItem> out = new ArrayList<>();
		String sql = "SELECT * FROM meal_plan_items WHERE meal_plan_id = ? ORDER BY day_of_week, meal_type";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, mealPlanId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					MealPlanItem i = new MealPlanItem();
					i.setId(rs.getInt("id"));
					i.setMealPlanId(rs.getInt("meal_plan_id"));
					i.setDayOfWeek(rs.getInt("day_of_week"));
					i.setMealType(rs.getString("meal_type"));
					i.setFoodId(rs.getInt("food_id"));
					i.setQuantityInGrams(rs.getDouble("quantity_in_grams"));
					i.setNotes(rs.getString("notes"));
					out.add(i);
				}
			}
		}
		return out;
	}

	public boolean updateMealPlanItem(MealPlanItem i) throws SQLException {
		String sql = "UPDATE meal_plan_items SET day_of_week=?, meal_type=?, food_id=?, quantity_in_grams=?, notes=? WHERE id=?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, i.getDayOfWeek());
			ps.setString(2, i.getMealType());
			ps.setInt(3, i.getFoodId());
			ps.setDouble(4, i.getQuantityInGrams());
			ps.setString(5, i.getNotes());
			ps.setInt(6, i.getId());
			return ps.executeUpdate() > 0;
		}
	}

	public boolean deleteMealPlanItem(int id) throws SQLException {
		String sql = "DELETE FROM meal_plan_items WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}
}
