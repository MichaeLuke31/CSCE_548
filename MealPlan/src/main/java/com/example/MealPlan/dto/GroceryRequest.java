package com.example.MealPlan.dto;

public class GroceryRequest {
    public Long id;
    public Long foodId;
    public Long mealPlanId; // optional
    public Double cost;
    public Double quantity;
    public String unit;
    public String purchaseFrequency;
}
