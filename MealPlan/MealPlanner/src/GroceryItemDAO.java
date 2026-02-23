import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroceryItemDAO {

	public GroceryItem createGroceryItem(GroceryItem g) throws SQLException {
		String sql = "INSERT INTO grocery_items (meal_plan_id, food_id, cost, quantity, unit, purchase_frequency) VALUES (?,?,?,?,?,?)";
		try (Connection c = DBConfig.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			if (g.getMealPlanId() != null)
				ps.setInt(1, g.getMealPlanId());
			else
				ps.setNull(1, Types.INTEGER);
			ps.setInt(2, g.getFoodId());
			ps.setDouble(3, g.getCost());
			ps.setDouble(4, g.getQuantity());
			ps.setString(5, g.getUnit());
			ps.setString(6, g.getPurchaseFrequency());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					g.setId(rs.getInt(1));
			}
		}
		return g;
	}

	public GroceryItem getGroceryItemById(int id) throws SQLException {
		String sql = "SELECT * FROM grocery_items WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					GroceryItem g = new GroceryItem();
					g.setId(rs.getInt("id"));
					int mpid = rs.getInt("meal_plan_id");
					if (!rs.wasNull())
						g.setMealPlanId(mpid);
					g.setFoodId(rs.getInt("food_id"));
					g.setCost(rs.getDouble("cost"));
					g.setQuantity(rs.getDouble("quantity"));
					g.setUnit(rs.getString("unit"));
					g.setPurchaseFrequency(rs.getString("purchase_frequency"));
					Timestamp ts = rs.getTimestamp("created_at");
					if (ts != null)
						g.setCreatedAt(ts.toInstant());
					return g;
				}
			}
		}
		return null;
	}

	public List<GroceryItem> listGroceryItemsForMealPlan(Integer mealPlanId) throws SQLException {
		List<GroceryItem> out = new ArrayList<>();
		String sql = (mealPlanId == null) ? "SELECT * FROM grocery_items WHERE meal_plan_id IS NULL"
				: "SELECT * FROM grocery_items WHERE meal_plan_id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			if (mealPlanId != null)
				ps.setInt(1, mealPlanId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					GroceryItem g = new GroceryItem();
					g.setId(rs.getInt("id"));
					int mpid = rs.getInt("meal_plan_id");
					if (!rs.wasNull())
						g.setMealPlanId(mpid);
					g.setFoodId(rs.getInt("food_id"));
					g.setCost(rs.getDouble("cost"));
					g.setQuantity(rs.getDouble("quantity"));
					g.setUnit(rs.getString("unit"));
					g.setPurchaseFrequency(rs.getString("purchase_frequency"));
					Timestamp ts = rs.getTimestamp("created_at");
					if (ts != null)
						g.setCreatedAt(ts.toInstant());
					out.add(g);
				}
			}
		}
		return out;
	}

	public boolean updateGroceryItem(GroceryItem g) throws SQLException {
		String sql = "UPDATE grocery_items SET meal_plan_id=?, food_id=?, cost=?, quantity=?, unit=?, purchase_frequency=? WHERE id=?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			if (g.getMealPlanId() != null)
				ps.setInt(1, g.getMealPlanId());
			else
				ps.setNull(1, Types.INTEGER);
			ps.setInt(2, g.getFoodId());
			ps.setDouble(3, g.getCost());
			ps.setDouble(4, g.getQuantity());
			ps.setString(5, g.getUnit());
			ps.setString(6, g.getPurchaseFrequency());
			ps.setInt(7, g.getId());
			return ps.executeUpdate() > 0;
		}
	}

	public boolean deleteGroceryItem(int id) throws SQLException {
		String sql = "DELETE FROM grocery_items WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}
}
