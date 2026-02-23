package com.example.MealPlan.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "meal_plans")
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // "bulking", "carnivore", etc.
    private String description;

    // One-to-many relationship: a MealPlan has many MealPlanItems
    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealPlanItem> items = new ArrayList<>();

    public MealPlan() {}

    public void addItem(MealPlanItem item) {
        items.add(item);
        item.setMealPlan(this);
    }

    public void removeItem(MealPlanItem item) {
        items.remove(item);
        item.setMealPlan(null);
    }

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof MealPlan)) return false; MealPlan m = (MealPlan) o; return Objects.equals(id, m.id); }
    @Override public int hashCode() { return Objects.hash(id); }

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

	public List<MealPlanItem> getItems() {
		return items;
	}

	public void setItems(List<MealPlanItem> items) {
		this.items = items;
	}

    
}