package com.example.MealPlan.service;


import com.example.MealPlan.model.Nutrients;
import com.example.MealPlan.repository.NutrientsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NutrientsService {

    private final NutrientsRepository repo;

    public NutrientsService(NutrientsRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Nutrients save(Nutrients nutrients) {
        return repo.save(nutrients);
    }

    public Optional<Nutrients> getById(Long id) {
        return repo.findById(id);
    }

    public List<Nutrients> getAll() {
        return repo.findAll();
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
