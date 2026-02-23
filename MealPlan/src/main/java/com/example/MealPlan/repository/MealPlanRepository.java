package com.example.MealPlan.repository;

import com.example.MealPlan.model.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
	Optional<MealPlan> findByName(String name);
}
