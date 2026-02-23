CREATE DATABASE IF NOT EXISTS mealplanner CHARACTER SET = 'utf8mb4' COLLATE = 'utf8mb4_unicode_ci';
USE mealplanner;

-- Class 1: Users
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  daily_calories INT NOT NULL,
  daily_protein_g INT DEFAULT 0,
  daily_carbs_g INT DEFAULT 0,
  daily_fat_g INT DEFAULT 0,
  monthly_budget DECIMAL(10,2) DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Class 2: Food
CREATE TABLE foods (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL UNIQUE,
  serving_description VARCHAR(100),
  cost_per_unit DECIMAL(10,4) NOT NULL, -- cost per 1 gram or per unit depending on unit_type
  unit_type ENUM('gram','ml','piece','serving') DEFAULT 'gram',
  calories_per_100g INT DEFAULT 0,
  protein_per_100g DECIMAL(6,2) DEFAULT 0,
  carbs_per_100g DECIMAL(6,2) DEFAULT 0,
  fat_per_100g DECIMAL(6,2) DEFAULT 0,
  vitamins TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Class 3: MealPlan
CREATE TABLE meal_plans (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  name VARCHAR(150) NOT NULL,
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_mealplans_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Class 4: MealPlanItem
CREATE TABLE meal_plan_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  meal_plan_id INT NOT NULL,
  day_of_week TINYINT NOT NULL CHECK (day_of_week BETWEEN 1 AND 7),
  meal_type ENUM('breakfast','lunch','dinner','snack') NOT NULL,
  food_id INT NOT NULL,
  quantity_in_grams DECIMAL(8,2) NOT NULL,
  notes VARCHAR(255),
  CONSTRAINT fk_items_mealplan FOREIGN KEY (meal_plan_id) REFERENCES meal_plans(id) ON DELETE CASCADE,
  CONSTRAINT fk_items_food FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Class 5: GroceryItem
CREATE TABLE grocery_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  meal_plan_id INT NULL, -- can be null if grocery is not tied to a specific plan
  food_id INT NOT NULL,
  cost DECIMAL(10,2) NOT NULL,
  quantity DECIMAL(10,3) NOT NULL,
  unit VARCHAR(50) NOT NULL,
  purchase_frequency ENUM('weekly','biweekly','monthly','once') DEFAULT 'weekly',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_grocery_mealplan FOREIGN KEY (meal_plan_id) REFERENCES meal_plans(id) ON DELETE SET NULL,
  CONSTRAINT fk_grocery_food FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE RESTRICT
) ENGINE=InnoDB;