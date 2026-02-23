package com.example.MealPlan;


import com.example.MealPlan.model.*;
import com.example.MealPlan.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final FoodRepository foodRepo;
    private final NutrientsRepository nutrientRepo;
    private final MealPlanRepository mealPlanRepo;
    private final MealPlanItemRepository itemRepo;
    private final GroceryItemRepository groceryRepo;

    public DataLoader(FoodRepository foodRepo, NutrientsRepository nutrientRepo,
                      MealPlanRepository mealPlanRepo, MealPlanItemRepository itemRepo,
                      GroceryItemRepository groceryRepo) {
        this.foodRepo = foodRepo;
        this.nutrientRepo = nutrientRepo;
        this.mealPlanRepo = mealPlanRepo;
        this.itemRepo = itemRepo;
        this.groceryRepo = groceryRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (foodRepo.count() > 0) return; // avoid re-seeding

     // create nutrients but DO NOT save it directly
        Nutrients chickenN = new Nutrients();
        chickenN.setProteinG(31.0);
        chickenN.setCarbsG(0.0);
        chickenN.setFatG(3.6);
        chickenN.setFiberG(0.0);
        chickenN.setPctVitaminA(2.0);
        chickenN.setPctVitaminB12(1.0);
        chickenN.setPctVitaminB9(0.5);
        chickenN.setPctVitaminB6(0.8);
        chickenN.setPctVitaminC(0.0);
        chickenN.setPctVitaminD(0.0);

        // attach nutrients to food
        Food chicken = new Food();
        chicken.setName("Chicken Breast");
        chicken.setCostPerUnit(0.0025);
        chicken.setUnitType("gram");
        chicken.setCaloriesPer100g(165);
        chicken.setNutrients(chickenN);

        // Save only the food — cascade will persist nutrients
        foodRepo.save(chicken);

        // create a meal plan and add an item (cascade from MealPlan -> MealPlanItem if configured)
        MealPlan mp = new MealPlan();
        mp.setName("Bulking 2500");
        mp.setDescription("sample bulking plan");
        // create item
        MealPlanItem mpi = new MealPlanItem();
        mpi.setDayOfWeek(1);
        mpi.setMealType("dinner");
        mpi.setFood(chicken);
        mpi.setQuantityInGrams(200.0);
        // add to plan (setMealPlan happens in addItem)
        mp.addItem(mpi);
        mealPlanRepo.save(mp); // cascade will save mpi

        // grocery item
        GroceryItem g = new GroceryItem();
        g.setMealPlan(mp);
        g.setFood(chicken);
        g.setCost(12.0);
        g.setQuantity(2000.0);
        g.setUnit("gram");
        g.setPurchaseFrequency("weekly");
        groceryRepo.save(g);

        System.out.println("Seeded sample data.");
    }
}