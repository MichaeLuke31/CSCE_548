import java.sql.*;
import java.util.Scanner;

/**
 * Console viewer + basic CRUD tester for mealplanner DB. - Uses
 * DBConfig.getConnection() for DB access (DBConfig must be in same folder). -
 * No packages, no external frameworks.
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("=== MealPlanner Console (CRUD tester) ===");
		Scanner scanner = new Scanner(System.in);

		while (true) {
			printMainMenu();
			String choice = scanner.nextLine().trim();
			try {
				switch (choice) {
				case "1":
					showCounts();
					break;
				case "2":
					listUsers();
					break;
				case "3":
					addUser(scanner);
					break;
				case "4":
					editUser(scanner);
					break;
				case "5":
					deleteUser(scanner);
					break;

				case "10":
					listFoods();
					break;
				case "11":
					addFood(scanner);
					break;
				case "12":
					editFood(scanner);
					break;
				case "13":
					deleteFood(scanner);
					break;

				case "20":
					listMealPlans();
					break;
				case "21":
					addMealPlan(scanner);
					break;
				case "22":
					editMealPlan(scanner);
					break;
				case "23":
					deleteMealPlan(scanner);
					break;

				case "30":
					listMealPlanItems(scanner);
					break;
				case "31":
					addMealPlanItem(scanner);
					break;
				case "32":
					editMealPlanItem(scanner);
					break;
				case "33":
					deleteMealPlanItem(scanner);
					break;

				case "40":
					listGroceryItems(scanner);
					break;
				case "41":
					addGroceryItem(scanner);
					break;
				case "42":
					editGroceryItem(scanner);
					break;
				case "43":
					deleteGroceryItem(scanner);
					break;

				case "0":
					System.out.println("Exiting.");
					scanner.close();
					return;
				default:
					System.out.println("Unknown option.");
					break;
				}
			} catch (SQLException ex) {
				System.out.println("SQL error: " + ex.getMessage());
			}
			System.out.println();
		}
	}

	private static void printMainMenu() {
		System.out.println("Main Menu - choose an action:");
		System.out.println(" 1) Show row counts");
		System.out.println("Users:");
		System.out.println(" 2) List users  3) Add user  4) Edit user  5) Delete user");
		System.out.println("Foods:");
		System.out.println("10) List foods 11) Add food 12) Edit food 13) Delete food");
		System.out.println("Meal Plans:");
		System.out.println("20) List meal plans 21) Add meal plan 22) Edit meal plan 23) Delete meal plan");
		System.out.println("Meal Plan Items:");
		System.out.println("30) List items 31) Add item 32) Edit item 33) Delete item");
		System.out.println("Grocery Items:");
		System.out.println("40) List grocery items 41) Add grocery 42) Edit grocery 43) Delete grocery");
		System.out.println(" 0) Exit");
		System.out.print("Choice> ");
	}

	// -------------------------
	// Database helper
	// -------------------------
	private static Connection getConnection() throws SQLException {
		return DBConfig.getConnection();
	}

	// -------------------------
	// Option implementations (counts + listing)
	// -------------------------
	private static void showCounts() throws SQLException {
		String sql = "SELECT 'users' AS table_name, COUNT(*) AS row_count FROM users\n"
				+ "UNION ALL SELECT 'foods', COUNT(*) FROM foods\n"
				+ "UNION ALL SELECT 'meal_plans', COUNT(*) FROM meal_plans\n"
				+ "UNION ALL SELECT 'meal_plan_items', COUNT(*) FROM meal_plan_items\n"
				+ "UNION ALL SELECT 'grocery_items', COUNT(*) FROM grocery_items\n"
				+ "UNION ALL SELECT 'TOTAL', (SELECT COUNT(*) FROM users)+(SELECT COUNT(*) FROM foods)+(SELECT COUNT(*) FROM meal_plans)+(SELECT COUNT(*) FROM meal_plan_items)+(SELECT COUNT(*) FROM grocery_items)";
		try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
			System.out.printf("%-18s | %10s%n", "table_name", "row_count");
			System.out.println("-------------------+------------");
			while (rs.next()) {
				System.out.printf("%-18s | %10d%n", rs.getString(1), rs.getLong(2));
			}
		}
	}

	// ---------- USERS ----------
	private static void listUsers() throws SQLException {
		String sql = "SELECT id, username, daily_calories, daily_protein_g, daily_carbs_g, daily_fat_g, monthly_budget FROM users ORDER BY id";
		try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
			System.out.println("Users:");
			System.out.printf("%-4s %-15s %-6s %-6s %-6s %-6s %-8s%n", "id", "username", "kcal", "prot", "carb", "fat",
					"budget");
			while (rs.next()) {
				System.out.printf("%-4d %-15s %-6d %-6d %-6d %-6d $%-7.2f%n", rs.getInt("id"), rs.getString("username"),
						rs.getInt("daily_calories"), rs.getInt("daily_protein_g"), rs.getInt("daily_carbs_g"),
						rs.getInt("daily_fat_g"), rs.getDouble("monthly_budget"));
			}
		}
	}

	private static void addUser(Scanner sc) throws SQLException {
		System.out.print("username: ");
		String username = sc.nextLine().trim();
		int kcal = promptInt(sc, "daily_calories: ");
		int p = promptInt(sc, "daily_protein_g: ");
		int c = promptInt(sc, "daily_carbs_g: ");
		int f = promptInt(sc, "daily_fat_g: ");
		double budget = promptDouble(sc, "monthly_budget: ");

		String sql = "INSERT INTO users (username,daily_calories,daily_protein_g,daily_carbs_g,daily_fat_g,monthly_budget) VALUES (?,?,?,?,?,?)";
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, username);
			ps.setInt(2, kcal);
			ps.setInt(3, p);
			ps.setInt(4, c);
			ps.setInt(5, f);
			ps.setDouble(6, budget);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					System.out.println("Created user id: " + rs.getInt(1));
			}
		} catch (SQLException ex) {
			System.out.println("Insert failed: " + ex.getMessage());
		}
	}

	private static void editUser(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter user id to edit: ");
		// fetch existing
		String q = "SELECT username,daily_calories,daily_protein_g,daily_carbs_g,daily_fat_g,monthly_budget FROM users WHERE id=?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					System.out.println("User not found.");
					return;
				}
				String username = promptString(sc, "username (" + rs.getString("username") + ") : ",
						rs.getString("username"));
				int kcal = promptIntDefault(sc, "daily_calories (" + rs.getInt("daily_calories") + ") : ",
						rs.getInt("daily_calories"));
				int p = promptIntDefault(sc, "daily_protein_g (" + rs.getInt("daily_protein_g") + ") : ",
						rs.getInt("daily_protein_g"));
				int carb = promptIntDefault(sc, "daily_carbs_g (" + rs.getInt("daily_carbs_g") + ") : ",
						rs.getInt("daily_carbs_g"));
				int fat = promptIntDefault(sc, "daily_fat_g (" + rs.getInt("daily_fat_g") + ") : ",
						rs.getInt("daily_fat_g"));
				double budget = promptDoubleDefault(sc, "monthly_budget (" + rs.getDouble("monthly_budget") + ") : ",
						rs.getDouble("monthly_budget"));

				String upd = "UPDATE users SET username=?, daily_calories=?, daily_protein_g=?, daily_carbs_g=?, daily_fat_g=?, monthly_budget=? WHERE id=?";
				try (PreparedStatement ups = c.prepareStatement(upd)) {
					ups.setString(1, username);
					ups.setInt(2, kcal);
					ups.setInt(3, p);
					ups.setInt(4, carb);
					ups.setInt(5, fat);
					ups.setDouble(6, budget);
					ups.setInt(7, id);
					int changed = ups.executeUpdate();
					System.out.println("Rows updated: " + changed);
				}
			}
		}
	}

	private static void deleteUser(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter user id to delete: ");
		System.out.print("Are you sure? (y/N): ");
		String confirm = sc.nextLine().trim().toLowerCase();
		if (!"y".equals(confirm)) {
			System.out.println("Canceled.");
			return;
		}
		String sql = "DELETE FROM users WHERE id=?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setInt(1, id);
			System.out.println("Rows deleted: " + ps.executeUpdate());
		}
	}

	// ---------- FOODS ----------
	private static void listFoods() throws SQLException {
		String sql = "SELECT id, name, serving_description, unit_type, cost_per_unit, calories_per_100g FROM foods ORDER BY id";
		try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
			System.out.println("Foods:");
			System.out.printf("%-4s %-25s %-12s %-8s %-8s %-6s%n", "id", "name", "serving", "unit", "cost",
					"kcal/100g");
			while (rs.next()) {
				System.out.printf("%-4d %-25s %-12s %-8s %-8.4f %-6d%n", rs.getInt("id"), rs.getString("name"),
						nullable(rs.getString("serving_description")), rs.getString("unit_type"),
						rs.getDouble("cost_per_unit"), rs.getInt("calories_per_100g"));
			}
		}
	}

	private static void addFood(Scanner sc) throws SQLException {
		String name = promptString(sc, "name: ", "");
		String serving = promptString(sc, "serving_description: ", "");
		String unitType = promptString(sc, "unit_type (gram/serving/piece): ", "gram");
		double cost = promptDouble(sc, "cost_per_unit: ");
		int kcal = promptInt(sc, "calories_per_100g: ");
		String sql = "INSERT INTO foods (name,serving_description,cost_per_unit,unit_type,calories_per_100g) VALUES (?,?,?,?,?)";
		try (Connection c = getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, name);
			ps.setString(2, serving);
			ps.setDouble(3, cost);
			ps.setString(4, unitType);
			ps.setInt(5, kcal);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					System.out.println("Created food id: " + rs.getInt(1));
			}
		} catch (SQLException ex) {
			System.out.println("Insert failed: " + ex.getMessage());
		}
	}

	private static void editFood(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter food id to edit: ");
		String q = "SELECT name,serving_description,cost_per_unit,unit_type,calories_per_100g FROM foods WHERE id=?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					System.out.println("Food not found.");
					return;
				}
				String name = promptString(sc, "name (" + rs.getString("name") + ") : ", rs.getString("name"));
				String serv = promptString(sc,
						"serving_description (" + nullable(rs.getString("serving_description")) + ") : ",
						rs.getString("serving_description"));
				double cost = promptDoubleDefault(sc, "cost_per_unit (" + rs.getDouble("cost_per_unit") + ") : ",
						rs.getDouble("cost_per_unit"));
				String unit = promptString(sc, "unit_type (" + rs.getString("unit_type") + ") : ",
						rs.getString("unit_type"));
				int kcal = promptIntDefault(sc, "calories_per_100g (" + rs.getInt("calories_per_100g") + ") : ",
						rs.getInt("calories_per_100g"));

				String upd = "UPDATE foods SET name=?, serving_description=?, cost_per_unit=?, unit_type=?, calories_per_100g=? WHERE id=?";
				try (PreparedStatement ups = c.prepareStatement(upd)) {
					ups.setString(1, name);
					ups.setString(2, serv);
					ups.setDouble(3, cost);
					ups.setString(4, unit);
					ups.setInt(5, kcal);
					ups.setInt(6, id);
					System.out.println("Rows updated: " + ups.executeUpdate());
				}
			}
		}
	}

	private static void deleteFood(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter food id to delete: ");
		System.out.print("Are you sure? (y/N): ");
		String c = sc.nextLine().trim().toLowerCase();
		if (!"y".equals(c)) {
			System.out.println("Canceled.");
			return;
		}
		String sql = "DELETE FROM foods WHERE id=?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			System.out.println("Rows deleted: " + ps.executeUpdate());
		} catch (SQLException ex) {
			System.out.println("Delete failed: " + ex.getMessage());
		}
	}

	// ---------- MEAL PLANS ----------
	private static void listMealPlans() throws SQLException {
		String sql = "SELECT id, user_id, name, description FROM meal_plans ORDER BY id";
		try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
			System.out.println("Meal Plans:");
			System.out.printf("%-4s %-6s %-25s %-30s%n", "id", "user", "name", "description");
			while (rs.next()) {
				System.out.printf("%-4d %-6d %-25s %-30s%n", rs.getInt("id"), rs.getInt("user_id"),
						nullable(rs.getString("name")), nullable(rs.getString("description")));
			}
		}
	}

	private static void addMealPlan(Scanner sc) throws SQLException {
		int userId = promptInt(sc, "user_id: ");
		String name = promptString(sc, "name: ", "");
		String desc = promptString(sc, "description: ", "");
		String sql = "INSERT INTO meal_plans (user_id, name, description) VALUES (?,?,?)";
		try (Connection c = getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, userId);
			ps.setString(2, name);
			ps.setString(3, desc);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					System.out.println("Created meal_plan id: " + rs.getInt(1));
			}
		}
	}

	private static void editMealPlan(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter meal_plan id to edit: ");
		String q = "SELECT user_id,name,description FROM meal_plans WHERE id=?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					System.out.println("Not found.");
					return;
				}
				int uid = promptIntDefault(sc, "user_id (" + rs.getInt("user_id") + ") : ", rs.getInt("user_id"));
				String name = promptString(sc, "name (" + nullable(rs.getString("name")) + ") : ",
						rs.getString("name"));
				String desc = promptString(sc, "description (" + nullable(rs.getString("description")) + ") : ",
						rs.getString("description"));
				String upd = "UPDATE meal_plans SET user_id=?, name=?, description=? WHERE id=?";
				try (PreparedStatement ups = c.prepareStatement(upd)) {
					ups.setInt(1, uid);
					ups.setString(2, name);
					ups.setString(3, desc);
					ups.setInt(4, id);
					System.out.println("Rows updated: " + ups.executeUpdate());
				}
			}
		}
	}

	private static void deleteMealPlan(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter meal_plan id to delete: ");
		System.out.print("Are you sure? (y/N): ");
		String c = sc.nextLine().trim().toLowerCase();
		if (!"y".equals(c)) {
			System.out.println("Canceled.");
			return;
		}
		String sql = "DELETE FROM meal_plans WHERE id=?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			System.out.println("Rows deleted: " + ps.executeUpdate());
		}
	}

	// ---------- MEAL PLAN ITEMS ----------
	private static void listMealPlanItems(Scanner sc) throws SQLException {
		System.out.print("Enter meal_plan_id to list items for that plan (or press Enter to list all): ");
		String input = sc.nextLine().trim();
		if (input.isEmpty()) {
			String sql = "SELECT mpi.id, mpi.meal_plan_id, mpi.day_of_week, mpi.meal_type, f.name AS food_name, mpi.quantity_in_grams FROM meal_plan_items mpi JOIN foods f ON mpi.food_id=f.id ORDER BY mpi.meal_plan_id";
			try (Connection c = getConnection();
					Statement s = c.createStatement();
					ResultSet rs = s.executeQuery(sql)) {
				System.out.printf("%-4s %-8s %-4s %-10s %-25s %-8s%n", "id", "plan_id", "day", "meal", "food", "grams");
				while (rs.next()) {
					System.out.printf("%-4d %-8d %-4d %-10s %-25s %-8.2f%n", rs.getInt("id"), rs.getInt("meal_plan_id"),
							rs.getInt("day_of_week"), rs.getString("meal_type"), nullable(rs.getString("food_name")),
							rs.getDouble("quantity_in_grams"));
				}
			}
		} else {
			int mpid = Integer.parseInt(input);
			String sql = "SELECT id, day_of_week, meal_type, food_id, quantity_in_grams FROM meal_plan_items WHERE meal_plan_id=? ORDER BY day_of_week";
			try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setInt(1, mpid);
				try (ResultSet rs = ps.executeQuery()) {
					System.out.printf("%-4s %-4s %-10s %-6s %-8s%n", "id", "day", "meal", "foodId", "grams");
					while (rs.next()) {
						System.out.printf("%-4d %-4d %-10s %-6d %-8.2f%n", rs.getInt("id"), rs.getInt("day_of_week"),
								rs.getString("meal_type"), rs.getInt("food_id"), rs.getDouble("quantity_in_grams"));
					}
				}
			}
		}
	}

	private static void addMealPlanItem(Scanner sc) throws SQLException {
		int mealPlanId = promptInt(sc, "meal_plan_id: ");
		int day = promptInt(sc, "day_of_week (1-7): ");
		String mealType = promptString(sc, "meal_type (breakfast/lunch/dinner/snack): ", "breakfast");
		int foodId = promptInt(sc, "food_id: ");
		double grams = promptDouble(sc, "quantity_in_grams: ");
		String note = promptString(sc, "notes: ", "");
		String sql = "INSERT INTO meal_plan_items (meal_plan_id, day_of_week, meal_type, food_id, quantity_in_grams, notes) VALUES (?,?,?,?,?,?)";
		try (Connection c = getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, mealPlanId);
			ps.setInt(2, day);
			ps.setString(3, mealType);
			ps.setInt(4, foodId);
			ps.setDouble(5, grams);
			ps.setString(6, note);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					System.out.println("Created item id: " + rs.getInt(1));
			}
		}
	}

	private static void editMealPlanItem(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter meal_plan_item id to edit: ");
		String q = "SELECT meal_plan_id,day_of_week,meal_type,food_id,quantity_in_grams,notes FROM meal_plan_items WHERE id=?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					System.out.println("Not found.");
					return;
				}
				int mpid = promptIntDefault(sc, "meal_plan_id (" + rs.getInt("meal_plan_id") + ") : ",
						rs.getInt("meal_plan_id"));
				int day = promptIntDefault(sc, "day_of_week (" + rs.getInt("day_of_week") + ") : ",
						rs.getInt("day_of_week"));
				String mealType = promptString(sc, "meal_type (" + rs.getString("meal_type") + ") : ",
						rs.getString("meal_type"));
				int foodId = promptIntDefault(sc, "food_id (" + rs.getInt("food_id") + ") : ", rs.getInt("food_id"));
				double grams = promptDoubleDefault(sc,
						"quantity_in_grams (" + rs.getDouble("quantity_in_grams") + ") : ",
						rs.getDouble("quantity_in_grams"));
				String notes = promptString(sc, "notes (" + nullable(rs.getString("notes")) + ") : ",
						rs.getString("notes"));
				String upd = "UPDATE meal_plan_items SET meal_plan_id=?, day_of_week=?, meal_type=?, food_id=?, quantity_in_grams=?, notes=? WHERE id=?";
				try (PreparedStatement ups = c.prepareStatement(upd)) {
					ups.setInt(1, mpid);
					ups.setInt(2, day);
					ups.setString(3, mealType);
					ups.setInt(4, foodId);
					ups.setDouble(5, grams);
					ups.setString(6, notes);
					ups.setInt(7, id);
					System.out.println("Rows updated: " + ups.executeUpdate());
				}
			}
		}
	}

	private static void deleteMealPlanItem(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter meal_plan_item id to delete: ");
		System.out.print("Are you sure? (y/N): ");
		String c = sc.nextLine().trim().toLowerCase();
		if (!"y".equals(c)) {
			System.out.println("Canceled.");
			return;
		}
		String sql = "DELETE FROM meal_plan_items WHERE id=?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			System.out.println("Rows deleted: " + ps.executeUpdate());
		}
	}

	// ---------- GROCERY ITEMS ----------
	private static void listGroceryItems(Scanner sc) throws SQLException {
		System.out.print("Enter meal_plan_id to filter (or press Enter): ");
		String in = sc.nextLine().trim();
		if (in.isEmpty()) {
			String sql = "SELECT gi.id, gi.meal_plan_id, f.name AS food_name, gi.cost, gi.quantity, gi.unit, gi.purchase_frequency FROM grocery_items gi JOIN foods f ON gi.food_id=f.id ORDER BY gi.id";
			try (Connection c = getConnection();
					Statement s = c.createStatement();
					ResultSet rs = s.executeQuery(sql)) {
				System.out.printf("%-4s %-8s %-25s %-8s %-8s %-8s %-10s%n", "id", "plan_id", "food", "cost", "qty",
						"unit", "freq");
				while (rs.next()) {
					System.out.printf("%-4d %-8s %-25s $%-7.2f %-8.2f %-8s %-10s%n", rs.getInt("id"),
							rs.getObject("meal_plan_id") == null ? "NULL" : String.valueOf(rs.getInt("meal_plan_id")),
							nullable(rs.getString("food_name")), rs.getDouble("cost"), rs.getDouble("quantity"),
							nullable(rs.getString("unit")), nullable(rs.getString("purchase_frequency")));
				}
			}
		} else {
			int mpid = Integer.parseInt(in);
			String sql = "SELECT id, meal_plan_id, food_id, cost, quantity, unit, purchase_frequency FROM grocery_items WHERE meal_plan_id=? ORDER BY id";
			try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setInt(1, mpid);
				try (ResultSet rs = ps.executeQuery()) {
					System.out.printf("%-4s %-8s %-6s %-8s %-8s %-8s %-10s%n", "id", "plan_id", "foodId", "cost", "qty",
							"unit", "freq");
					while (rs.next()) {
						System.out.printf("%-4d %-8d %-6d $%-7.2f %-8.2f %-8s %-10s%n", rs.getInt("id"),
								rs.getInt("meal_plan_id"), rs.getInt("food_id"), rs.getDouble("cost"),
								rs.getDouble("quantity"), rs.getString("unit"), rs.getString("purchase_frequency"));
					}
				}
			}
		}
	}

	private static void addGroceryItem(Scanner sc) throws SQLException {
		String maybe = promptString(sc, "meal_plan_id (or blank for none): ", "");
		Integer mpid = maybe.isEmpty() ? null : Integer.parseInt(maybe);
		int foodId = promptInt(sc, "food_id: ");
		double cost = promptDouble(sc, "cost: ");
		double qty = promptDouble(sc, "quantity: ");
		String unit = promptString(sc, "unit: ", "gram");
		String freq = promptString(sc, "purchase_frequency (weekly/biweekly/monthly/once): ", "weekly");
		String sql = "INSERT INTO grocery_items (meal_plan_id, food_id, cost, quantity, unit, purchase_frequency) VALUES (?,?,?,?,?,?)";
		try (Connection c = getConnection();
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			if (mpid != null)
				ps.setInt(1, mpid);
			else
				ps.setNull(1, Types.INTEGER);
			ps.setInt(2, foodId);
			ps.setDouble(3, cost);
			ps.setDouble(4, qty);
			ps.setString(5, unit);
			ps.setString(6, freq);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					System.out.println("Created grocery id: " + rs.getInt(1));
			}
		}
	}

	private static void editGroceryItem(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter grocery_item id to edit: ");
		String q = "SELECT meal_plan_id,food_id,cost,quantity,unit,purchase_frequency FROM grocery_items WHERE id=?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(q)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					System.out.println("Not found.");
					return;
				}
				String mpDefault = rs.getObject("meal_plan_id") == null ? ""
						: String.valueOf(rs.getInt("meal_plan_id"));
				String mpStr = promptString(sc, "meal_plan_id (" + mpDefault + ") : ", mpDefault);
				Integer mpid = mpStr.isEmpty() ? null : Integer.parseInt(mpStr);
				int foodId = promptIntDefault(sc, "food_id (" + rs.getInt("food_id") + ") : ", rs.getInt("food_id"));
				double cost = promptDoubleDefault(sc, "cost (" + rs.getDouble("cost") + ") : ", rs.getDouble("cost"));
				double qty = promptDoubleDefault(sc, "quantity (" + rs.getDouble("quantity") + ") : ",
						rs.getDouble("quantity"));
				String unit = promptString(sc, "unit (" + nullable(rs.getString("unit")) + ") : ",
						rs.getString("unit"));
				String freq = promptString(sc,
						"purchase_frequency (" + nullable(rs.getString("purchase_frequency")) + ") : ",
						rs.getString("purchase_frequency"));

				String upd = "UPDATE grocery_items SET meal_plan_id=?, food_id=?, cost=?, quantity=?, unit=?, purchase_frequency=? WHERE id=?";
				try (PreparedStatement ups = c.prepareStatement(upd)) {
					if (mpid != null)
						ups.setInt(1, mpid);
					else
						ups.setNull(1, Types.INTEGER);
					ups.setInt(2, foodId);
					ups.setDouble(3, cost);
					ups.setDouble(4, qty);
					ups.setString(5, unit);
					ups.setString(6, freq);
					ups.setInt(7, id);
					System.out.println("Rows updated: " + ups.executeUpdate());
				}
			}
		}
	}

	private static void deleteGroceryItem(Scanner sc) throws SQLException {
		int id = promptInt(sc, "Enter grocery_item id to delete: ");
		System.out.print("Are you sure? (y/N): ");
		String c = sc.nextLine().trim().toLowerCase();
		if (!"y".equals(c)) {
			System.out.println("Canceled.");
			return;
		}
		String sql = "DELETE FROM grocery_items WHERE id=?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			System.out.println("Rows deleted: " + ps.executeUpdate());
		}
	}

	// -------------------------
	// Input helpers
	// -------------------------
	private static int promptInt(Scanner sc, String prompt) {
		while (true) {
			System.out.print(prompt);
			String s = sc.nextLine().trim();
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				System.out.println("Enter an integer.");
			}
		}
	}

	private static int promptIntDefault(Scanner sc, String prompt, int def) {
		System.out.print(prompt);
		String s = sc.nextLine().trim();
		if (s.isEmpty())
			return def;
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			System.out.println("Invalid, using default.");
			return def;
		}
	}

	private static double promptDouble(Scanner sc, String prompt) {
		while (true) {
			System.out.print(prompt);
			String s = sc.nextLine().trim();
			try {
				return Double.parseDouble(s);
			} catch (NumberFormatException e) {
				System.out.println("Enter a number.");
			}
		}
	}

	private static double promptDoubleDefault(Scanner sc, String prompt, double def) {
		System.out.print(prompt);
		String s = sc.nextLine().trim();
		if (s.isEmpty())
			return def;
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			System.out.println("Invalid, using default.");
			return def;
		}
	}

	private static String promptString(Scanner sc, String prompt, String def) {
		System.out.print(prompt);
		String s = sc.nextLine();
		if (s.trim().isEmpty())
			return def;
		return s.trim();
	}

	private static String nullable(String s) {
		return s == null ? "" : s;
	}
}
