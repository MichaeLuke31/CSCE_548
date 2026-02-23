package com.example.MealPlan.testclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TestClient {

    private static final String BASE = "http://localhost:8080/api/foods";

    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        // 1️⃣ INSERT FOOD
        System.out.println("---- INSERT FOOD ----");

        String createJson = """
            {
              "name": "Console Test Food",
              "costPerUnit": 0.005,
              "unitType": "gram",
              "caloriesPer100g": 400
            }
            """;

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(createJson))
                .build();

        HttpResponse<String> createResponse =
                client.send(createRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + createResponse.statusCode());
        System.out.println(createResponse.body());

        // Extract ID from response (simple parsing)
        String body = createResponse.body();
        Long createdId = extractId(body);
        System.out.println("Created ID: " + createdId);

        // 2️⃣ GET ALL
        System.out.println("\n---- GET ALL AFTER INSERT ----");
        getAll(client);

        // 3️⃣ UPDATE FOOD
        System.out.println("\n---- UPDATE FOOD ----");

        String updateJson = """
            {
              "id": %d,
              "name": "Updated Console Food",
              "costPerUnit": 0.010,
              "unitType": "gram",
              "caloriesPer100g": 500
            }
            """.formatted(createdId);

        HttpRequest updateRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(updateJson))
                .build();

        HttpResponse<String> updateResponse =
                client.send(updateRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + updateResponse.statusCode());
        System.out.println(updateResponse.body());

        // 4️⃣ GET ALL AFTER UPDATE
        System.out.println("\n---- GET ALL AFTER UPDATE ----");
        getAll(client);

        // 5️⃣ DELETE FOOD
        System.out.println("\n---- DELETE FOOD ----");

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/" + createdId))
                .timeout(Duration.ofSeconds(5))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse =
                client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Delete Status: " + deleteResponse.statusCode());

        // 6️⃣ GET ALL AFTER DELETE
        System.out.println("\n---- GET ALL AFTER DELETE ----");
        getAll(client);

        System.out.println("\nTEST COMPLETE");
    }

    private static void getAll(HttpClient client) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println(response.body());
    }

    private static Long extractId(String json) {
        try {
            int index = json.indexOf("\"id\"");
            if (index == -1) return null;

            String sub = json.substring(index);
            String digits = sub.replaceAll("[^0-9]", " ").trim().split("\\s+")[0];
            return Long.parseLong(digits);
        } catch (Exception e) {
            return null;
        }
    }
}