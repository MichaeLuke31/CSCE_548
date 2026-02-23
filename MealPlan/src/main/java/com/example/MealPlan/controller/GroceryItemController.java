package com.example.MealPlan.controller;


import com.example.MealPlan.model.GroceryItem;
import com.example.MealPlan.service.GroceryItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groceries")
public class GroceryItemController {

    private final GroceryItemService svc;

    public GroceryItemController(GroceryItemService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<GroceryItem> save(@RequestBody GroceryItem g) {
        return ResponseEntity.ok(svc.save(g));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroceryItem> getById(@PathVariable Long id) {
        return svc.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<GroceryItem>> getAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}