import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO {

	public Food createFood(Food f) throws SQLException {
		String sql = "INSERT INTO foods (name, serving_description, cost_per_unit, unit_type, calories_per_100g, protein_per_100g, carbs_per_100g, fat_per_100g, vitamins) VALUES (?,?,?,?,?,?,?,?,?)";
		try (Connection c = DBConfig.getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, f.getName());
			ps.setString(2, f.getServingDescription());
			ps.setDouble(3, f.getCostPerUnit());
			ps.setString(4, f.getUnitType());
			ps.setInt(5, f.getCaloriesPer100g());
			ps.setDouble(6, f.getProteinPer100g());
			ps.setDouble(7, f.getCarbsPer100g());
			ps.setDouble(8, f.getFatPer100g());
			ps.setString(9, f.getVitamins());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					f.setId(rs.getInt(1));
			}
		}
		return f;
	}

	public Food getFoodById(int id) throws SQLException {
		String sql = "SELECT * FROM foods WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Food f = new Food();
					f.setId(rs.getInt("id"));
					f.setName(rs.getString("name"));
					f.setServingDescription(rs.getString("serving_description"));
					f.setCostPerUnit(rs.getDouble("cost_per_unit"));
					f.setUnitType(rs.getString("unit_type"));
					f.setCaloriesPer100g(rs.getInt("calories_per_100g"));
					f.setProteinPer100g(rs.getDouble("protein_per_100g"));
					f.setCarbsPer100g(rs.getDouble("carbs_per_100g"));
					f.setFatPer100g(rs.getDouble("fat_per_100g"));
					f.setVitamins(rs.getString("vitamins"));
					Timestamp ts = rs.getTimestamp("created_at");
					if (ts != null)
						f.setCreatedAt(ts.toInstant());
					return f;
				}
			}
		}
		return null;
	}

	public List<Food> listFoods() throws SQLException {
		List<Food> out = new ArrayList<>();
		String sql = "SELECT * FROM foods";
		try (Connection c = DBConfig.getConnection();
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery(sql)) {
			while (rs.next()) {
				Food f = new Food();
				f.setId(rs.getInt("id"));
				f.setName(rs.getString("name"));
				f.setServingDescription(rs.getString("serving_description"));
				f.setCostPerUnit(rs.getDouble("cost_per_unit"));
				f.setUnitType(rs.getString("unit_type"));
				f.setCaloriesPer100g(rs.getInt("calories_per_100g"));
				f.setProteinPer100g(rs.getDouble("protein_per_100g"));
				f.setCarbsPer100g(rs.getDouble("carbs_per_100g"));
				f.setFatPer100g(rs.getDouble("fat_per_100g"));
				f.setVitamins(rs.getString("vitamins"));
				Timestamp ts = rs.getTimestamp("created_at");
				if (ts != null)
					f.setCreatedAt(ts.toInstant());
				out.add(f);
			}
		}
		return out;
	}

	public boolean updateFood(Food f) throws SQLException {
		String sql = "UPDATE foods SET name=?, serving_description=?, cost_per_unit=?, unit_type=?, calories_per_100g=?, protein_per_100g=?, carbs_per_100g=?, fat_per_100g=?, vitamins=? WHERE id=?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, f.getName());
			ps.setString(2, f.getServingDescription());
			ps.setDouble(3, f.getCostPerUnit());
			ps.setString(4, f.getUnitType());
			ps.setInt(5, f.getCaloriesPer100g());
			ps.setDouble(6, f.getProteinPer100g());
			ps.setDouble(7, f.getCarbsPer100g());
			ps.setDouble(8, f.getFatPer100g());
			ps.setString(9, f.getVitamins());
			ps.setInt(10, f.getId());
			return ps.executeUpdate() > 0;
		}
	}

	public boolean deleteFood(int id) throws SQLException {
		String sql = "DELETE FROM foods WHERE id = ?";
		try (Connection c = DBConfig.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}

	// helper: approximate calories from grams (food stores calories per 100g)
	public static double approximateCaloriesForItem(Food food, double grams) {
		return (food.getCaloriesPer100g() * grams) / 100.0;
	}
}
