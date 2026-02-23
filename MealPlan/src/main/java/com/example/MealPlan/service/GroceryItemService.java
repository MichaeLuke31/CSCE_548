package com.example.MealPlan.service;


import com.example.MealPlan.model.GroceryItem;
import com.example.MealPlan.repository.GroceryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GroceryItemService {

    private final GroceryItemRepository repo;

    public GroceryItemService(GroceryItemRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public GroceryItem save(GroceryItem item) {
        return repo.save(item);
    }

    public Optional<GroceryItem> getById(Long id) {
        return repo.findById(id);
    }

    public List<GroceryItem> getAll() {
        return repo.findAll();
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
