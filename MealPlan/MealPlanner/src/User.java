import java.time.Instant;
import java.util.Objects;

public class User {
	private Integer id;
	private String username;
	private int dailyCalories;
	private int dailyProteinG;
	private int dailyCarbsG;
	private int dailyFatG;
	private double monthlyBudget;
	private Instant createdAt;

	public User() {
	}

	public User(Integer id, String username, int dailyCalories, int dailyProteinG, int dailyCarbsG, int dailyFatG,
			double monthlyBudget) {
		this.id = id;
		this.username = username;
		this.dailyCalories = dailyCalories;
		this.dailyProteinG = dailyProteinG;
		this.dailyCarbsG = dailyCarbsG;
		this.dailyFatG = dailyFatG;
		this.monthlyBudget = monthlyBudget;
	}

	// getters & setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getDailyCalories() {
		return dailyCalories;
	}

	public void setDailyCalories(int dailyCalories) {
		this.dailyCalories = dailyCalories;
	}

	public int getDailyProteinG() {
		return dailyProteinG;
	}

	public void setDailyProteinG(int dailyProteinG) {
		this.dailyProteinG = dailyProteinG;
	}

	public int getDailyCarbsG() {
		return dailyCarbsG;
	}

	public void setDailyCarbsG(int dailyCarbsG) {
		this.dailyCarbsG = dailyCarbsG;
	}

	public int getDailyFatG() {
		return dailyFatG;
	}

	public void setDailyFatG(int dailyFatG) {
		this.dailyFatG = dailyFatG;
	}

	public double getMonthlyBudget() {
		return monthlyBudget;
	}

	public void setMonthlyBudget(double monthlyBudget) {
		this.monthlyBudget = monthlyBudget;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;
		User u = (User) o;
		return Objects.equals(id, u.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", username='" + username + '\'' + '}';
	}
}
