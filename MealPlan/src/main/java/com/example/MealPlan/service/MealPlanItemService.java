package com.example.MealPlan.service;


import com.example.MealPlan.model.MealPlanItem;
import com.example.MealPlan.repository.MealPlanItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MealPlanItemService {

    private final MealPlanItemRepository repo;

    public MealPlanItemService(MealPlanItemRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public MealPlanItem save(MealPlanItem item) {
        return repo.save(item);
    }

    public Optional<MealPlanItem> getById(Long id) {
        return repo.findById(id);
    }

    public List<MealPlanItem> getAll() {
        return repo.findAll();
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}