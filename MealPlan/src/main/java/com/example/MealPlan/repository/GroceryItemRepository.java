package com.example.MealPlan.repository;


import com.example.MealPlan.model.GroceryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroceryItemRepository extends JpaRepository<GroceryItem, Long> {
    List<GroceryItem> findByMealPlanId(Long mealPlanId);
}
