package com.example.MealPlan.dto;

import com.example.MealPlan.model.Food;

public class FoodSummaryDTO {
    private Long id;
    private String name;
    private String unitType; // optional extra for UI text

    public FoodSummaryDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	
	 public static FoodSummaryDTO fromEntity(Food e) {
	        if (e == null) return null;
	        FoodSummaryDTO d = new FoodSummaryDTO();
	        d.setId(e.getId());
	        d.setName(e.getName());
	        d.setUnitType(e.getUnitType());
	        return d;
	    }

	    /**
	     * Produce a shallow Food entity with only id set. Service layer should
	     * fetch the real Food entity if it needs full data (recommended for updates).
	     */
	    public Food toEntityReference() {
	        if (this.id == null) return null;
	        Food f = new Food();
	        f.setId(this.id);
	        return f;
	    }
    
}
