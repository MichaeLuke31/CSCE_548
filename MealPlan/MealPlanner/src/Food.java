import java.time.Instant;
import java.util.Objects;

public class Food {
	private Integer id;
	private String name;
	private String servingDescription;
	private double costPerUnit;
	private String unitType;
	private int caloriesPer100g;
	private double proteinPer100g;
	private double carbsPer100g;
	private double fatPer100g;
	private String vitamins;
	private Instant createdAt;

	public Food() {
	}

	// getters & setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServingDescription() {
		return servingDescription;
	}

	public void setServingDescription(String servingDescription) {
		this.servingDescription = servingDescription;
	}

	public double getCostPerUnit() {
		return costPerUnit;
	}

	public void setCostPerUnit(double costPerUnit) {
		this.costPerUnit = costPerUnit;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public int getCaloriesPer100g() {
		return caloriesPer100g;
	}

	public void setCaloriesPer100g(int caloriesPer100g) {
		this.caloriesPer100g = caloriesPer100g;
	}

	public double getProteinPer100g() {
		return proteinPer100g;
	}

	public void setProteinPer100g(double proteinPer100g) {
		this.proteinPer100g = proteinPer100g;
	}

	public double getCarbsPer100g() {
		return carbsPer100g;
	}

	public void setCarbsPer100g(double carbsPer100g) {
		this.carbsPer100g = carbsPer100g;
	}

	public double getFatPer100g() {
		return fatPer100g;
	}

	public void setFatPer100g(double fatPer100g) {
		this.fatPer100g = fatPer100g;
	}

	public String getVitamins() {
		return vitamins;
	}

	public void setVitamins(String vitamins) {
		this.vitamins = vitamins;
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
		if (!(o instanceof Food))
			return false;
		Food that = (Food) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Food{" + "id=" + id + ", name='" + name + '\'' + '}';
	}
}
