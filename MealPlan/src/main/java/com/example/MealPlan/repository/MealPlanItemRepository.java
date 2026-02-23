package com.example.MealPlan.repository;


import com.example.MealPlan.model.MealPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {
    List<MealPlanItem> findByMealPlanIdOrderByDayOfWeek(Long mealPlanId);
}
