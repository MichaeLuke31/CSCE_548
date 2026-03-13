package com.example.MealPlan.controller;

import com.example.MealPlan.dto.NutrientsDTO;
import com.example.MealPlan.model.Nutrients;
import com.example.MealPlan.service.NutrientsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * NutrientsController exposes NutrientsDTO over the REST API.
 * It maps DTO -> entity and entity -> DTO using the helpers on the DTO class.
 */
@RestController
@RequestMapping("/api/nutrients")
public class NutrientsController {

    private final NutrientsService svc;

    public NutrientsController(NutrientsService svc) {
        this.svc = svc;
    }

    /**
     * Create or update Nutrients.
     * Accepts NutrientsDTO in the body and returns the saved NutrientsDTO.
     */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody NutrientsDTO dto) {
        if (dto == null) {
            return ResponseEntity.badRequest().body("request body required");
        }

        // Convert DTO -> entity
        Nutrients toSave = dto.toEntity();

        // Service should implement safe merge behavior for updates if necessary.
        Nutrients saved = svc.save(toSave);

        // Map saved entity -> DTO for response
        NutrientsDTO resp = NutrientsDTO.fromEntity(saved);
        return ResponseEntity.ok(resp);
    }

    /**
     * GET a single Nutrients record as DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<NutrientsDTO> getById(@PathVariable Long id) {
        return svc.getById(id)
                .map(NutrientsDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET all Nutrients as DTO list
     */
    @GetMapping
    public ResponseEntity<List<NutrientsDTO>> getAll() {
        List<NutrientsDTO> list = svc.getAll()
                .stream()
                .map(NutrientsDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /**
     * DELETE nutrients by id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}