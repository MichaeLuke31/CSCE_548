# Deployment Instructions

This guide explains how to run the **MealPlan** application locally from the provided ZIP file.  
It includes prerequisites, setup/configuration, running the application, and basic troubleshooting.

---

## Prerequisites

Make sure the following software is installed:

- **Eclipse IDE** 2025-12 or later  
- **Spring Tools (Spring Tool Suite)** 5.1.0 release  
- **MySQL Server** 9.6 or later  
- **MySQL Workbench** 8.0 or later *(optional GUI)*  
- **Java JDK** 17 or later  
- **Any web browser**

---

## Step 1: Create a MySQL Instance

1. Open **MySQL** and create a **local instance server**.
2. Create a user and password.

⚠️ **Important:**  
Do **not share** your database credentials.

---

## Step 2: Download the Project

1. Download the **`MealPlan` ZIP file** from the GitHub repository.
2. Unzip the project into your **Eclipse workspace folder**.

Default location on Windows:

C:\Users<your-username>\eclipse-workspace


---

## Step 3: Configure the Database Connection

1. In Eclipse, locate the **MealPlan project**.
2. Navigate to:

src > main > resources


3. Open the file:

application.properties


4. Update the following properties with your database credentials:

```properties
spring.datasource.url=jdbc:<enter database URL here>
spring.datasource.username=<enter user here>
spring.datasource.password=<enter password here>

## Step 4: Run the Application
1. Navigate to:

src > main > java > com > example > MealPlan

2. Open the file:

MealPlanApplication.java

3. Run the application.

If the application starts successfully, you should see a message similar to:

Started in X.XXX seconds in the Eclipse console.

IF Application fails to connect to the database check the following:

- spring.datasource.url is correct
- Database username and password are correct
- MySQL server is running
- The database user has the correct permissions

## Step 5: Access the Application
Open a browser and go to:

http://localhost:8080/

You should now see the Meal Plan application frontend.

## Step 6 (Optional): Populate Database Tables
You can populate the food and nutrients tables by:

Writing SQL manually

Using MySQL Workbench

Asking an AI assistant to generate realistic SQL seed data

This step helps populate the application with realistic food and nutrient values.
