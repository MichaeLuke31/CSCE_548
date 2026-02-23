package com.example.MealPlan.controller;


import com.example.MealPlan.model.Nutrients;
import com.example.MealPlan.service.NutrientsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrients")
public class NutrientsController {

    private final NutrientsService svc;

    public NutrientsController(NutrientsService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<Nutrients> save(@RequestBody Nutrients n) {
        return ResponseEntity.ok(svc.save(n));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nutrients> getById(@PathVariable Long id) {
        return svc.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Nutrients>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
