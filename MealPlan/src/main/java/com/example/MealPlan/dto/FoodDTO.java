package com.example.MealPlan.dto;
import com.example.MealPlan.model.Food;
import com.example.MealPlan.model.Nutrients;
import java.io.Serializable;

public class FoodDTO implements Serializable {
    private Long id;
    private String name;
    private Double costPerUnit;
    private String unitType;
    private Integer caloriesPer100g;
    private NutrientsDTO nutrients;

    public FoodDTO() {}

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

	public Double getCostPerUnit() {
		return costPerUnit;
	}

	public void setCostPerUnit(Double costPerUnit) {
		this.costPerUnit = costPerUnit;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public Integer getCaloriesPer100g() {
		return caloriesPer100g;
	}

	public void setCaloriesPer100g(Integer caloriesPer100g) {
		this.caloriesPer100g = caloriesPer100g;
	}

	public NutrientsDTO getNutrients() {
		return nutrients;
	}

	public void setNutrients(NutrientsDTO nutrients) {
		this.nutrients = nutrients;
	}

	// map from JPA entity -> DTO
    public static FoodDTO fromEntity(Food e) {
        if (e == null) return null;
        FoodDTO d = new FoodDTO();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setCostPerUnit(e.getCostPerUnit());
        d.setUnitType(e.getUnitType());
        d.setCaloriesPer100g(e.getCaloriesPer100g());
        d.setNutrients(NutrientsDTO.fromEntity(e.getNutrients()));
        return d;
    }

    // map from DTO -> JPA entity
    // note: for updates, the service layer should attach the correct existing entity or rely on repository.save
    public Food toEntity() {
        Food e = new Food();
        e.setId(this.id); // if null, JPA will treat as new
        e.setName(this.name);
        e.setCostPerUnit(this.costPerUnit);
        e.setUnitType(this.unitType);
        e.setCaloriesPer100g(this.caloriesPer100g);
        Nutrients n = (this.nutrients == null) ? null : this.nutrients.toEntity();
        e.setNutrients(n);
        return e;
    }
    
}