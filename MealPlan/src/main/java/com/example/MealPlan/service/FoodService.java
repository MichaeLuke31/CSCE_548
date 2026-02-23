package com.example.MealPlan.service;


import com.example.MealPlan.model.Food;
import com.example.MealPlan.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final FoodRepository repo;

    public FoodService(FoodRepository repo) {
        this.repo = repo;
    }

    public Food save(Food food) {
        return repo.save(food);
    }

    public Optional<Food> getById(Long id) {
        return repo.findById(id);
    }

    public Optional<Food> getByName(String name) {
        return repo.findByName(name);
    }

    public List<Food> getAll() {
        return repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}