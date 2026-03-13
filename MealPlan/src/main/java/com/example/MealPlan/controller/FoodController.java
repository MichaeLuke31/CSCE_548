package com.example.MealPlan.controller;

import com.example.MealPlan.dto.FoodDTO;
import com.example.MealPlan.model.Food;
import com.example.MealPlan.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService svc;

    public FoodController(FoodService svc) { this.svc = svc; }

    // Create or update: accepts FoodDTO, returns saved FoodDTO
    @PostMapping
    public ResponseEntity<FoodDTO> save(@RequestBody FoodDTO foodDto) {
        // Map DTO -> entity
        Food toSave = foodDto.toEntity();
        Food saved = svc.save(toSave);
        // Map entity -> DTO for response
        FoodDTO resp = FoodDTO.fromEntity(saved);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDTO> getById(@PathVariable Long id) {
        Optional<Food> opt = svc.getById(id);
        return opt.map(f -> ResponseEntity.ok(FoodDTO.fromEntity(f)))
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FoodDTO>> getAll(@RequestParam(required = false) String name) {
        if (name != null) {
            Optional<Food> f = svc.getByName(name);
            List<FoodDTO> list = f.map(food -> List.of(FoodDTO.fromEntity(food))).orElseGet(List::of);
            return ResponseEntity.ok(list);
        }
        // map all entities to DTOs
        List<FoodDTO> dtoList = svc.getAll().stream().map(FoodDTO::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}