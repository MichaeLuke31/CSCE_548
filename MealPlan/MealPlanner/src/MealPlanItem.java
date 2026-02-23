import java.util.Objects;

public class MealPlanItem {
	private Integer id;
	private Integer mealPlanId;
	private int dayOfWeek; // 1-7
	private String mealType;
	private Integer foodId;
	private double quantityInGrams;
	private String notes;

	public MealPlanItem() {
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

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public Integer getFoodId() {
		return foodId;
	}

	public void setFoodId(Integer foodId) {
		this.foodId = foodId;
	}

	public double getQuantityInGrams() {
		return quantityInGrams;
	}

	public void setQuantityInGrams(double quantityInGrams) {
		this.quantityInGrams = quantityInGrams;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MealPlanItem))
			return false;
		MealPlanItem m = (MealPlanItem) o;
		return Objects.equals(id, m.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "MealPlanItem{" + "id=" + id + ", mealPlanId=" + mealPlanId + '}';
	}
}
