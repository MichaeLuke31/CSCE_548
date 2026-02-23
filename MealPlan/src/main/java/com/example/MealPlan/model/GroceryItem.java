package com.example.MealPlan.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "grocery_items")
public class GroceryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // optionally tied to a meal plan (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id")
    private MealPlan mealPlan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    private Double cost;
    private Double quantity; // in grams or unit according to Food.unitType
    private String unit;
    private String purchaseFrequency; // weekly, biweekly, monthly, once

    public GroceryItem() {}

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof GroceryItem)) return false; GroceryItem g = (GroceryItem) o; return Objects.equals(id, g.id); }
    @Override public int hashCode() { return Objects.hash(id); }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MealPlan getMealPlan() {
		return mealPlan;
	}

	public void setMealPlan(MealPlan mealPlan) {
		this.mealPlan = mealPlan;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
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

    
}