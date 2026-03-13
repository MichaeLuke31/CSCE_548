package com.example.MealPlan.dto;
import com.example.MealPlan.model.Nutrients;
import java.io.Serializable;

public class NutrientsDTO implements Serializable {
    private Double proteinG;
    private Double carbsG;
    private Double fatG;
    private Double fiberG;
    private Double pctVitaminA;
    private Double pctVitaminB12;
    private Double pctVitaminB9;
    private Double pctVitaminB6;
    private Double pctVitaminC;
    private Double pctVitaminD;

    // constructors, getters, setters
    public NutrientsDTO() {}

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
	
	 // mapping helpers
    public static NutrientsDTO fromEntity(Nutrients e) {
        if (e == null) return null;
        NutrientsDTO d = new NutrientsDTO();
        d.setProteinG(e.getProteinG());
        d.setCarbsG(e.getCarbsG());
        d.setFatG(e.getFatG());
        d.setFiberG(e.getFiberG());
        d.setPctVitaminA(e.getPctVitaminA());
        d.setPctVitaminB12(e.getPctVitaminB12());
        d.setPctVitaminB9(e.getPctVitaminB9());
        d.setPctVitaminB6(e.getPctVitaminB6());
        d.setPctVitaminC(e.getPctVitaminC());
        d.setPctVitaminD(e.getPctVitaminD());
        return d;
    }

    public Nutrients toEntity() {
        Nutrients e = new Nutrients();
        e.setProteinG(this.proteinG);
        e.setCarbsG(this.carbsG);
        e.setFatG(this.fatG);
        e.setFiberG(this.fiberG);
        e.setPctVitaminA(this.pctVitaminA);
        e.setPctVitaminB12(this.pctVitaminB12);
        e.setPctVitaminB9(this.pctVitaminB9);
        e.setPctVitaminB6(this.pctVitaminB6);
        e.setPctVitaminC(this.pctVitaminC);
        e.setPctVitaminD(this.pctVitaminD);
        return e;
    }
    
}