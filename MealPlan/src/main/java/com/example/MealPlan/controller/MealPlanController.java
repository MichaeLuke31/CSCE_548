package com.example.MealPlan.controller;


import com.example.MealPlan.model.MealPlan;
import com.example.MealPlan.service.MealPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mealplans")
public class MealPlanController {

    private final MealPlanService svc;

    public MealPlanController(MealPlanService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<MealPlan> save(@RequestBody MealPlan mp) {
        return ResponseEntity.ok(svc.save(mp));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealPlan> getById(@PathVariable Long id) {
        return svc.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MealPlan>> getAll(@RequestParam(required = false) String name) {
        if (name != null) {
            Optional<MealPlan> m = svc.getByName(name);
            return m.map(mp -> ResponseEntity.ok(List.of(mp))).orElseGet(() -> ResponseEntity.ok(List.of()));
        }
        return ResponseEntity.ok(svc.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}