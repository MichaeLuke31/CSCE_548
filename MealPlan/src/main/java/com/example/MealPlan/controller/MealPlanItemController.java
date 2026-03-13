package com.example.MealPlan.controller;

import com.example.MealPlan.dto.MealPlanItemDTO;
import com.example.MealPlan.model.MealPlanItem;
import com.example.MealPlan.model.Food;
import com.example.MealPlan.model.MealPlan;
import com.example.MealPlan.service.FoodService;
import com.example.MealPlan.service.MealPlanService;
import com.example.MealPlan.service.MealPlanItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller exposing MealPlanItem DTOs.
 * Accepts MealPlanItemDTO and returns MealPlanItemDTO.
 */
@RestController
@RequestMapping("/api/items")
public class MealPlanItemController {

    private final MealPlanItemService svc;
    private final FoodService foodService;
    private final MealPlanService mealPlanService;

    public MealPlanItemController(MealPlanItemService svc,
                                  FoodService foodService,
                                  MealPlanService mealPlanService) {
        this.svc = svc;
        this.foodService = foodService;
        this.mealPlanService = mealPlanService;
    }

    /**
     * Create or update a MealPlanItem.
     * Body: MealPlanItemDTO
     */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody MealPlanItemDTO dto) {
        if (dto == null) {
            return ResponseEntity.badRequest().body("request body required");
        }

        // Basic validation: need a food id and a mealPlanId for items
        if (dto.getFood() == null || dto.getFood().getId() == null) {
            return ResponseEntity.badRequest().body("food.id is required");
        }
        if (dto.getMealPlanId() == null) {
            return ResponseEntity.badRequest().body("mealPlanId is required");
        }

        // Convert DTO -> shallow entity (fields + ids set)
        MealPlanItem toSave = dto.toEntityReference();

        // Resolve Food to a managed entity
        Long foodId = dto.getFood().getId();
        Optional<Food> fOpt = foodService.getById(foodId);
        if (fOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("foodId " + foodId + " not found");
        }
        toSave.setFood(fOpt.get());

        // Resolve MealPlan to a managed entity
        Long mpId = dto.getMealPlanId();
        Optional<MealPlan> mpOpt = mealPlanService.getById(mpId);
        if (mpOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("mealPlanId " + mpId + " not found");
        }
        toSave.setMealPlan(mpOpt.get());

        // Persist. Service should implement safe-merge logic for updates.
        MealPlanItem saved = svc.save(toSave);

        // Return DTO for response
        MealPlanItemDTO resp = MealPlanItemDTO.fromEntity(saved);
        return ResponseEntity.ok(resp);
    }

    /**
     * GET single item as DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<MealPlanItemDTO> getById(@PathVariable Long id) {
        return svc.getById(id)
                .map(MealPlanItemDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET all items as DTOs
     */
    @GetMapping
    public ResponseEntity<List<MealPlanItemDTO>> getAll() {
        List<MealPlanItemDTO> list = svc.getAll()
                .stream()
                .map(MealPlanItemDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}