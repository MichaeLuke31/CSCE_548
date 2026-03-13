package com.example.MealPlan.controller;

import com.example.MealPlan.dto.MealPlanDTO;
import com.example.MealPlan.dto.MealPlanItemDTO;
import com.example.MealPlan.model.MealPlan;
import com.example.MealPlan.model.MealPlanItem;
import com.example.MealPlan.model.Food;
import com.example.MealPlan.service.MealPlanService;
import com.example.MealPlan.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller that exposes MealPlan DTOs over the API.
 * It resolves nested Food references for items before saving.
 */
@RestController
@RequestMapping("/api/mealplans")
public class MealPlanController {

    private final MealPlanService svc;
    private final FoodService foodService;

    public MealPlanController(MealPlanService svc, FoodService foodService) {
        this.svc = svc;
        this.foodService = foodService;
    }

    /**
     * Create or update a meal plan.
     * Accepts MealPlanDTO and returns MealPlanDTO (with items included).
     */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody MealPlanDTO dto) {
        // Basic validation
        if (dto == null) {
            return ResponseEntity.badRequest().body("mealPlan payload required");
        }

        // Convert DTO -> shallow entity (items will be shallow too)
        MealPlan toSave = dto.toEntityReference();

        // If DTO provided items, resolve each item's food reference to a managed Food entity
        if (dto.getItems() != null) {
            for (MealPlanItemDTO itemDto : dto.getItems()) {
                if (itemDto.getFood() != null && itemDto.getFood().getId() != null) {
                    Long foodId = itemDto.getFood().getId();
                    Optional<Food> foodOpt = foodService.getById(foodId);
                    if (foodOpt.isEmpty()) {
                        return ResponseEntity.badRequest().body("foodId " + foodId + " not found");
                    }
                    // Find the corresponding shallow item entity in toSave and attach the managed food
                    // (We rely on ordering/IDs; safest approach: match by item id if present, otherwise by DTO index)
                    MealPlanItem matching = null;
                    if (itemDto.getId() != null) {
                        // try to find by id
                        matching = toSave.getItems().stream()
                                .filter(mi -> mi.getId() != null && mi.getId().equals(itemDto.getId()))
                                .findFirst().orElse(null);
                    }
                    if (matching == null) {
                        // fallback to first item that doesn't have a food attached yet (index-based)
                        matching = toSave.getItems().stream()
                                .filter(mi -> mi.getFood() == null || mi.getFood().getId() == null)
                                .findFirst().orElse(null);
                    }
                    if (matching != null) {
                        matching.setFood(foodOpt.get());
                    }
                } else {
                    // If no food provided for an item, that's probably invalid in your domain
                    // If it's allowed, remove this check. Otherwise return a bad request.
                    return ResponseEntity.badRequest().body("each MealPlanItem must include a food.id");
                }
            }
        }

        // Ensure each item has a parent reference to toSave before saving
        if (toSave.getItems() != null) {
            for (MealPlanItem mi : toSave.getItems()) {
                mi.setMealPlan(toSave);
            }
        }

        // Persist (service should handle merging for updates)
        MealPlan saved = svc.save(toSave);

        // Return DTO to client (include items)
        MealPlanDTO resp = MealPlanDTO.fromEntity(saved, true);
        return ResponseEntity.ok(resp);
    }

    /**
     * GET single plan as DTO (includes items).
     */
    @GetMapping("/{id}")
    public ResponseEntity<MealPlanDTO> getById(@PathVariable Long id) {
        return svc.getById(id)
                .map(mp -> MealPlanDTO.fromEntity(mp, true))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET list of plans as DTOs (items not included by default here to keep payload small).
     * If you want items included for all, change mapping call to fromEntity(e, true).
     */
    @GetMapping
    public ResponseEntity<List<MealPlanDTO>> getAll(@RequestParam(required = false) String name) {
        if (name != null) {
            Optional<MealPlan> m = svc.getByName(name);
            List<MealPlanDTO> list = m.map(mp -> List.of(MealPlanDTO.fromEntity(mp, false))).orElseGet(List::of);
            return ResponseEntity.ok(list);
        }
        List<MealPlanDTO> dtoList = svc.getAll()
                .stream()
                .map(mp -> MealPlanDTO.fromEntity(mp, false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}