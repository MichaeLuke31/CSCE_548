package com.example.MealPlan.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // cost per unit (e.g., per gram or per serving)
    private Double costPerUnit;

    // unitType: "gram","serving","piece"
    private String unitType;

    // calories per 100g (or per standard measure)
    private Integer caloriesPer100g;

    // One-to-one relationship to Nutrients (each Food has a Nutrients row)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nutrients_id")
    private Nutrients nutrients;

    public Food() {}

    // getters & setters, equals/hashCode...
    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof Food)) return false; Food f = (Food) o; return Objects.equals(id, f.id); }
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

	public Double getCostPerUnit() {
		return costPerUnit;
	}

	public void setCostPerUnit(Double costPerUnit) {
		this.costPerUnit = costPerUnit;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public Integer getCaloriesPer100g() {
		return caloriesPer100g;
	}

	public void setCaloriesPer100g(Integer caloriesPer100g) {
		this.caloriesPer100g = caloriesPer100g;
	}

	public Nutrients getNutrients() {
		return nutrients;
	}

	public void setNutrients(Nutrients nutrients) {
		this.nutrients = nutrients;
	}
    
}