package com.example.MealPlan.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "nutrients")
public class Nutrients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // grams
    private Double proteinG;
    private Double carbsG;
    private Double fatG;
    private Double fiberG;

    // percent daily value (0-100)
    private Double pctVitaminA;
    private Double pctVitaminB12;
    private Double pctVitaminB9; // folate
    private Double pctVitaminB6;
    private Double pctVitaminC;
    private Double pctVitaminD;

    public Nutrients() {}


    // equals/hashCode based on id
    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof Nutrients)) return false; Nutrients n = (Nutrients) o; return Objects.equals(id, n.id); }
    @Override public int hashCode() { return Objects.hash(id); }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getProteinG() {
		return proteinG;
	}

	public void setProteinG(Double proteinG) {
		this.proteinG = proteinG;
	}

	public Double getCarbsG() {
		return carbsG;
	}

	public void setCarbsG(Double carbsG) {
		this.carbsG = carbsG;
	}

	public Double getFatG() {
		return fatG;
	}

	public void setFatG(Double fatG) {
		this.fatG = fatG;
	}

	public Double getFiberG() {
		return fiberG;
	}

	public void setFiberG(Double fiberG) {
		this.fiberG = fiberG;
	}

	public Double getPctVitaminA() {
		return pctVitaminA;
	}

	public void setPctVitaminA(Double pctVitaminA) {
		this.pctVitaminA = pctVitaminA;
	}

	public Double getPctVitaminB12() {
		return pctVitaminB12;
	}

	public void setPctVitaminB12(Double pctVitaminB12) {
		this.pctVitaminB12 = pctVitaminB12;
	}

	public Double getPctVitaminB9() {
		return pctVitaminB9;
	}

	public void setPctVitaminB9(Double pctVitaminB9) {
		this.pctVitaminB9 = pctVitaminB9;
	}

	public Double getPctVitaminB6() {
		return pctVitaminB6;
	}

	public void setPctVitaminB6(Double pctVitaminB6) {
		this.pctVitaminB6 = pctVitaminB6;
	}

	public Double getPctVitaminC() {
		return pctVitaminC;
	}

	public void setPctVitaminC(Double pctVitaminC) {
		this.pctVitaminC = pctVitaminC;
	}

	public Double getPctVitaminD() {
		return pctVitaminD;
	}

	public void setPctVitaminD(Double pctVitaminD) {
		this.pctVitaminD = pctVitaminD;
	}
    
    
}
