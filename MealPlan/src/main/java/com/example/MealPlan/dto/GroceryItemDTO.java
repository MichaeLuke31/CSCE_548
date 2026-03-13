package com.example.MealPlan.dto;
import com.example.MealPlan.model.GroceryItem;
import com.example.MealPlan.model.Food;
import com.example.MealPlan.model.MealPlan;

public class GroceryItemDTO {
    private Long id;
    private FoodSummaryDTO food;
    private Long mealPlanId; // optional
    private Double cost;
    private Double quantity;
    private String unit;
    private String purchaseFrequency;

    public GroceryItemDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
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
	 public static GroceryItemDTO fromEntity(GroceryItem e) {
	        if (e == null) return null;
	        GroceryItemDTO d = new GroceryItemDTO();
	        d.setId(e.getId());
	        d.setFood(FoodSummaryDTO.fromEntity(e.getFood()));
	        if (e.getMealPlan() != null) d.setMealPlanId(e.getMealPlan().getId());
	        d.setCost(e.getCost());
	        d.setQuantity(e.getQuantity());
	        d.setUnit(e.getUnit());
	        d.setPurchaseFrequency(e.getPurchaseFrequency());
	        return d;
	    }

	    /**
	     * Convert DTO -> shallow entity reference. Service should resolve full entities as needed.
	     */
	    public GroceryItem toEntityReference() {
	        GroceryItem e = new GroceryItem();
	        e.setId(this.id);
	        if (this.food != null && this.food.getId() != null) {
	            Food f = new Food();
	            f.setId(this.food.getId());
	            e.setFood(f);
	        }
	        if (this.mealPlanId != null) {
	            MealPlan mp = new MealPlan();
	            mp.setId(this.mealPlanId);
	            e.setMealPlan(mp);
	        }
	        e.setCost(this.cost);
	        e.setQuantity(this.quantity);
	        e.setUnit(this.unit);
	        e.setPurchaseFrequency(this.purchaseFrequency);
	        return e;
	    }
}
