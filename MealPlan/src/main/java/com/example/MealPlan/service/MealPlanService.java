package com.example.MealPlan.service;


import com.example.MealPlan.model.MealPlan;
import com.example.MealPlan.repository.MealPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MealPlanService {

    private final MealPlanRepository repo;

    public MealPlanService(MealPlanRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public MealPlan save(MealPlan mealPlan) {
        return repo.save(mealPlan);
    }

    public Optional<MealPlan> getById(Long id) {
        return repo.findById(id);
    }

    public Optional<MealPlan> getByName(String name) {
        return repo.findByName(name);
    }

    public List<MealPlan> getAll() {
        return repo.findAll();
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}