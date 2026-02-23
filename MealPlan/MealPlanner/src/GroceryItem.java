import java.time.Instant;
import java.util.Objects;

public class GroceryItem {
	private Integer id;
	private Integer mealPlanId; // nullable
	private Integer foodId;
	private double cost;
	private double quantity;
	private String unit;
	private String purchaseFrequency;
	private Instant createdAt;

	public GroceryItem() {
	}

	// getters & setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMealPlanId() {
		return mealPlanId;
	}

	public void setMealPlanId(Integer mealPlanId) {
		this.mealPlanId = mealPlanId;
	}

	public Integer getFoodId() {
		return foodId;
	}

	public void setFoodId(Integer foodId) {
		this.foodId = foodId;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPurchaseFrequency() {
		return purchaseFrequency;
	}

	public void setPurchaseFrequency(String purchaseFrequency) {
		this.purchaseFrequency = purchaseFrequency;
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
		if (!(o instanceof GroceryItem))
			return false;
		GroceryItem g = (GroceryItem) o;
		return Objects.equals(id, g.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "GroceryItem{" + "id=" + id + ", foodId=" + foodId + '}';
	}
}
