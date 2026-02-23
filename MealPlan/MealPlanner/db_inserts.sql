-- test-data.sql
USE mealplanner;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE grocery_items;
TRUNCATE TABLE meal_plan_items;
TRUNCATE TABLE meal_plans;
TRUNCATE TABLE foods;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

-- Users
INSERT INTO users (username, daily_calories, daily_protein_g, daily_carbs_g, daily_fat_g, monthly_budget)
VALUES
('alice', 2500, 180, 300, 70, 500.00),
('bob', 2000, 120, 250, 60, 350.00),
('user1', 2200, 140, 260, 60, 300),
('user2', 2400, 160, 280, 70, 400),
('user3', 1800, 120, 200, 50, 250),
('user4', 2600, 190, 320, 80, 450),
('user5', 2000, 130, 240, 55, 320),
('user6', 2300, 150, 270, 65, 350),
('user7', 2100, 135, 250, 60, 330),
('user8', 2800, 200, 350, 85, 500);

-- Foods
INSERT INTO foods (name, serving_description, cost_per_unit, unit_type, calories_per_100g, protein_per_100g, carbs_per_100g, fat_per_100g, vitamins)
VALUES
('Chicken Breast', 'per 100g', 0.0025, 'gram', 165, 31.0, 0.0, 3.6, 'B3,B6'),
('Brown Rice', 'per 100g cooked', 0.0010, 'gram', 111, 2.6, 23.0, 0.9, 'B1,B3'),
('Olive Oil', 'per 1g', 0.01, 'gram', 884, 0.0, 0.0, 100.0, 'Vitamin E'),
('Whey Protein', 'per scoop (30g)', 0.03, 'serving', 120, 24.0, 3.0, 1.5, 'B12'),
('Apple', 'per 100g', 0.0008, 'gram', 52, 0.3, 14.0, 0.2, 'Vitamin C'),
('Eggs', 'per 100g', 0.0020, 'gram', 155, 13, 1.1, 11, 'A,D,B12'),
('Oatmeal', 'per 100g', 0.0012, 'gram', 68, 2.4, 12, 1.4, 'B1'),
('Greek Yogurt', 'per 100g', 0.0018, 'gram', 59, 10, 3.6, 0.4, 'Calcium'),
('Salmon', 'per 100g', 0.0045, 'gram', 208, 20, 0, 13, 'D,B12'),
('Broccoli', 'per 100g', 0.0009, 'gram', 34, 2.8, 7, 0.4, 'C,K'),
('Banana', 'per 100g', 0.0007, 'gram', 89, 1.1, 23, 0.3, 'B6'),
('Peanut Butter', 'per 100g', 0.0028, 'gram', 588, 25, 20, 50, 'E'),
('Pasta', 'per 100g cooked', 0.0011, 'gram', 131, 5, 25, 1.1, 'B1');

-- MealPlan for Alice
INSERT INTO meal_plans (user_id, name, description) VALUES
((SELECT id FROM users WHERE username='alice'), 'Bulking 2500', 'Weekly bulking plan aimed at 2500 kcal/day');

INSERT INTO meal_plans (user_id, name, description)
SELECT id, 'Maintenance Plan', 'Balanced weekly plan'
FROM users;

INSERT INTO meal_plans (user_id, name, description)
SELECT id, 'Cutting Plan', 'Lower calorie weekly plan'
FROM users;

-- MealPlanItems (a few examples)
INSERT INTO meal_plan_items (meal_plan_id, day_of_week, meal_type, food_id, quantity_in_grams, notes)
VALUES

((SELECT mp.id FROM meal_plans mp JOIN users u ON mp.user_id=u.id WHERE u.username='alice' AND mp.name='Bulking 2500'), 1, 'breakfast', (SELECT id FROM foods WHERE name='Whey Protein'), 30, 'post-workout'),
((SELECT mp.id FROM meal_plans mp JOIN users u ON mp.user_id=u.id WHERE u.username='alice' AND mp.name='Bulking 2500'), 1, 'dinner', (SELECT id FROM foods WHERE name='Chicken Breast'), 200, 'with rice'),
((SELECT mp.id FROM meal_plans mp JOIN users u ON mp.user_id=u.id WHERE u.username='alice' AND mp.name='Bulking 2500'), 1, 'dinner', (SELECT id FROM foods WHERE name='Brown Rice'), 150, 'cooked'),
((SELECT mp.id FROM meal_plans mp JOIN users u ON mp.user_id=u.id WHERE u.username='alice' AND mp.name='Bulking 2500'), 1, 'dinner', (SELECT id FROM foods WHERE name='Olive Oil'), 10, 'cooking oil');

INSERT INTO meal_plan_items (meal_plan_id, day_of_week, meal_type, food_id, quantity_in_grams, notes)
SELECT
  mp.id,
  d.day_of_week,
  m.meal_type,
  f.id,
  150,
  'auto-generated'
FROM meal_plans mp
JOIN (SELECT 1 AS day_of_week UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) d
JOIN (SELECT 'breakfast' AS meal_type UNION SELECT 'lunch' UNION SELECT 'dinner') m
JOIN foods f
WHERE f.name IN ('Chicken Breast', 'Brown Rice', 'Eggs');

-- Grocery Items (for the plan)
INSERT INTO grocery_items (meal_plan_id, food_id, cost, quantity, unit, purchase_frequency)
VALUES
(
  (SELECT id FROM meal_plans WHERE name='Bulking 2500'),
  (SELECT id FROM foods WHERE name='Chicken Breast'),
  12.00, 2000, 'gram', 'weekly'
),
(
  (SELECT id FROM meal_plans WHERE name='Bulking 2500'),
  (SELECT id FROM foods WHERE name='Brown Rice'),
  3.50, 2000, 'gram', 'weekly'
),
(
  (SELECT id FROM meal_plans WHERE name='Bulking 2500'),
  (SELECT id FROM foods WHERE name='Whey Protein'),
  25.00, 900, 'gram', 'monthly'
);

INSERT INTO grocery_items (meal_plan_id, food_id, cost, quantity, unit, purchase_frequency)
SELECT
  mp.id,
  f.id,
  10.00,
  1000,
  'gram',
  'weekly'
FROM meal_plans mp
JOIN foods f
WHERE f.name IN ('Chicken Breast', 'Brown Rice', 'Oatmeal');
