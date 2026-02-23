package com.example.MealPlan.controller;


import com.example.MealPlan.model.Food;
import com.example.MealPlan.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService svc;

    public FoodController(FoodService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<Food> save(@RequestBody Food food) {
        Food saved = svc.save(food);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Food> getById(@PathVariable Long id) {
        return svc.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Food>> getAll(@RequestParam(required = false) String name) {
        if (name != null) {
            Optional<Food> f = svc.getByName(name);
            return f.map(food -> ResponseEntity.ok(List.of(food))).orElseGet(() -> ResponseEntity.ok(List.of()));
        }
        return ResponseEntity.ok(svc.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
