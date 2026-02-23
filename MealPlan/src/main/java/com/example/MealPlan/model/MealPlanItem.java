package com.example.MealPlan.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "meal_plan_items",
       uniqueConstraints = @UniqueConstraint(columnNames = {"meal_plan_id", "day_of_week", "meal_type", "food_id"}))
public class MealPlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1..7
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "meal_type", nullable = false)
    private String mealType; // breakfast/lunch/dinner/snack

    private Double quantityInGrams;

    // Item belongs to a MealPlan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id")
    private MealPlan mealPlan;

    // Item refers to a Food
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    public MealPlanItem() {}

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof MealPlanItem)) return false; MealPlanItem m = (MealPlanItem) o; return Objects.equals(id, m.id); }
    @Override public int hashCode() { return Objects.hash(id); }

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

}