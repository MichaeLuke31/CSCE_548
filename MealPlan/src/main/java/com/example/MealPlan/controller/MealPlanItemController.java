package com.example.MealPlan.controller;


import com.example.MealPlan.model.MealPlanItem;
import com.example.MealPlan.service.MealPlanItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class MealPlanItemController {

    private final MealPlanItemService svc;

    public MealPlanItemController(MealPlanItemService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<MealPlanItem> save(@RequestBody MealPlanItem item) {
        return ResponseEntity.ok(svc.save(item));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealPlanItem> getById(@PathVariable Long id) {
        return svc.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MealPlanItem>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
