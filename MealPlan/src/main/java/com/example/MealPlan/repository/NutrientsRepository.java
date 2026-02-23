package com.example.MealPlan.repository;

import com.example.MealPlan.model.Nutrients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutrientsRepository extends JpaRepository<Nutrients, Long> {}
