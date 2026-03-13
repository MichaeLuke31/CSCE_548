package com.example.MealPlan.dto;
import com.example.MealPlan.model.Food;
import com.example.MealPlan.model.MealPlanItem;
import com.example.MealPlan.model.MealPlan;

public class MealPlanItemDTO {
    private Long id;
    private Integer dayOfWeek;  
    private String mealType;        // "Breakfast", "Lunch", ...
    private Double quantityInGrams;
    // Reference to food: include a summary or id only
    private FoodSummaryDTO food;
    // link to parent plan as ID (not the full object) to avoid cycles
    private Long mealPlanId;

    public MealPlanItemDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public Double getQuantityInGrams() {
		return quantityInGrams;
	}

	public void setQuantityInGrams(Double quantityInGrams) {
		this.quantityInGrams = quantityInGrams;
	}

	public FoodSummaryDTO getFood() {
		return food;
	}

	public void setFood(FoodSummaryDTO food) {
		this.food = food;
	}

	public Long getMealPlanId() {
		return mealPlanId;
	}

	public void setMealPlanId(Long mealPlanId) {
		this.mealPlanId = mealPlanId;
	}
    

    public static MealPlanItemDTO fromEntity(MealPlanItem e) {
        if (e == null) return null;
        MealPlanItemDTO d = new MealPlanItemDTO();
        d.setId(e.getId());
        d.setDayOfWeek(e.getDayOfWeek());
        d.setMealType(e.getMealType());
        d.setQuantityInGrams(e.getQuantityInGrams());
        // food may be null or full object
        d.setFood(FoodSummaryDTO.fromEntity(e.getFood()));
        // parent link: set id if present
        if (e.getMealPlan() != null) d.setMealPlanId(e.getMealPlan().getId());
        return d;
    }

    /**
     * Convert DTO -> shallow entity. For relationships we set only references by id
     * (Food and MealPlan) to avoid loading full objects here.
     *
     * The service layer should decide whether to replace these shallow references
     * with real managed entities (e.g., repo.findById(foodId)) before saving.
     */
    public MealPlanItem toEntityReference() {
        MealPlanItem e = new MealPlanItem();
        e.setId(this.id);
        e.setDayOfWeek(this.dayOfWeek);
        e.setMealType(this.mealType);
        e.setQuantityInGrams(this.quantityInGrams);
        if (this.food != null && this.food.getId() != null) {
            Food fRef = new Food();
            fRef.setId(this.food.getId());
            e.setFood(fRef);
        }
        if (this.mealPlanId != null) {
            MealPlan mpRef = new MealPlan();
            mpRef.setId(this.mealPlanId);
            e.setMealPlan(mpRef);
        }
        return e;
    }
}
