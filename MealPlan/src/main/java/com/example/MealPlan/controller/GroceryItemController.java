package com.example.MealPlan.controller;

import com.example.MealPlan.dto.GroceryRequest;
import com.example.MealPlan.dto.GroceryDTO;
import com.example.MealPlan.model.GroceryItem;
import com.example.MealPlan.model.Food;
import com.example.MealPlan.model.MealPlan;
import com.example.MealPlan.service.FoodService;
import com.example.MealPlan.service.MealPlanService;
import com.example.MealPlan.service.GroceryItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/groceries")
public class GroceryItemController {

    private final GroceryItemService groceryService;
    private final FoodService foodService;
    private final MealPlanService mealPlanService;

    public GroceryItemController(GroceryItemService groceryService,
                                 FoodService foodService,
                                 MealPlanService mealPlanService) {
        this.groceryService = groceryService;
        this.foodService = foodService;
        this.mealPlanService = mealPlanService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody GroceryRequest req) {
        // validate required foodId
        if (req.foodId == null) {
            return ResponseEntity.badRequest().body("foodId is required");
        }

        Optional<Food> foodOpt = foodService.getById(req.foodId);
        if (foodOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("foodId " + req.foodId + " not found");
        }
        Food food = foodOpt.get();

        MealPlan mealPlan = null;
        if (req.mealPlanId != null) {
            Optional<MealPlan> mpOpt = mealPlanService.getById(req.mealPlanId);
            if (mpOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("mealPlanId " + req.mealPlanId + " not found");
            }
            mealPlan = mpOpt.get();
        }

        GroceryItem g = new GroceryItem();
        // if updating, set id so save() will update
        if (req.id != null) g.setId(req.id);

        g.setFood(food);
        g.setMealPlan(mealPlan);
        g.setCost(req.cost);
        g.setQuantity(req.quantity);
        g.setUnit(req.unit);
        g.setPurchaseFrequency(req.purchaseFrequency);

        GroceryItem saved = groceryService.save(g);

        // map saved to GroceryDTO for consistent response shape (frontend expects fields like foodId)
        GroceryDTO dto = new GroceryDTO();
        dto.id = saved.getId();
        dto.foodId = saved.getFood() != null ? saved.getFood().getId() : null;
        dto.foodName = saved.getFood() != null ? saved.getFood().getName() : null;
        dto.mealPlanId = saved.getMealPlan() != null ? saved.getMealPlan().getId() : null;
        dto.mealPlanName = saved.getMealPlan() != null ? saved.getMealPlan().getName() : null;
        dto.cost = saved.getCost();
        dto.quantity = saved.getQuantity();
        dto.unit = saved.getUnit();
        dto.purchaseFrequency = saved.getPurchaseFrequency();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroceryItem> getById(@PathVariable Long id) {
        return groceryService.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<GroceryItem>> getAll() {
        return ResponseEntity.ok(groceryService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	groceryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}