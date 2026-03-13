package com.example.MealPlan.dto;
import com.example.MealPlan.model.MealPlan;
import com.example.MealPlan.model.MealPlanItem;

import java.util.List;
import java.util.stream.Collectors;

import java.io.Serializable;

public class MealPlanDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    // Optionally include items if you want; items must NOT include parent reference
    private List<MealPlanItemDTO> items;

    public MealPlanDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MealPlanItemDTO> getItems() {
		return items;
	}

	public void setItems(List<MealPlanItemDTO> items) {
		this.items = items;
	}
    
	public static MealPlanDTO fromEntity(MealPlan e, boolean includeItems) {
        if (e == null) return null;
        MealPlanDTO d = new MealPlanDTO();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setDescription(e.getDescription());
        if (includeItems && e.getItems() != null) {
            List<MealPlanItemDTO> itemDtos = e.getItems().stream()
                .map(MealPlanItemDTO::fromEntity)
                .collect(Collectors.toList());
            d.setItems(itemDtos);
        }
        return d;
    }

    /**
     * Convert to entity. Items are converted to shallow references; service layer should handle merging.
     */
    public MealPlan toEntityReference() {
        MealPlan e = new MealPlan();
        e.setId(this.id);
        e.setName(this.name);
        e.setDescription(this.description);
        if (this.items != null) {
            List<MealPlanItem> itemEntities = this.items.stream()
                .map(mdto -> mdto.toEntityReference())
                .collect(Collectors.toList());
            e.setItems(itemEntities);
        }
        return e;
    }
}