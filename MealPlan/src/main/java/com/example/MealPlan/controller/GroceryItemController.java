package com.example.MealPlan.controller;

import com.example.MealPlan.dto.GroceryItemDTO;
import com.example.MealPlan.dto.FoodSummaryDTO;
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
import java.util.stream.Collectors;

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

    /**
     * Create or update a grocery item.
     * Accepts GroceryItemDTO and returns GroceryItemDTO.
     */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody GroceryItemDTO dto) {
        // Basic validation: require food reference in DTO
        if (dto.getFood() == null || dto.getFood().getId() == null) {
            return ResponseEntity.badRequest().body("food.id is required");
        }

        // Convert DTO -> shallow entity (sets simple fields and id if present)
        GroceryItem toSave = dto.toEntityReference();

        // Resolve and attach a managed Food entity (prevent detached/shallow issues)
        Long foodId = dto.getFood().getId();
        Optional<Food> foodOpt = foodService.getById(foodId);
        if (foodOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("foodId " + foodId + " not found");
        }
        toSave.setFood(foodOpt.get());

        // If DTO supplied a mealPlanId (or nested mealPlan summary), resolve it
        if (dto.getMealPlanId() != null) {
            Optional<MealPlan> mpOpt = mealPlanService.getById(dto.getMealPlanId());
            if (mpOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("mealPlanId " + dto.getMealPlanId() + " not found");
            }
            toSave.setMealPlan(mpOpt.get());
        } else {
            toSave.setMealPlan(null);
        }

        // Save via service (service may perform merge logic for updates)
        GroceryItem saved = groceryService.save(toSave);

        // Return stable DTO shape to client
        GroceryItemDTO resp = GroceryItemDTO.fromEntity(saved);
        return ResponseEntity.ok(resp);
    }

    /**
     * GET single grocery item as DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<GroceryItemDTO> getById(@PathVariable Long id) {
        return groceryService.getById(id)
                .map(GroceryItemDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET all grocery items as DTO list
     */
    @GetMapping
    public ResponseEntity<List<GroceryItemDTO>> getAll() {
        List<GroceryItemDTO> list = groceryService.getAll()
                .stream()
                .map(GroceryItemDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        groceryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}