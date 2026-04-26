package com.qurkus;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class GreetingResourceTest {
    @Test
    @TestSecurity(user = "user", roles = "USER")
    void testMonthlyBudgetEndpoint() {
        given()
          .queryParam("month", "2026-04")
          .when().get("/budgets/monthly")
          .then()
             .statusCode(200)
             .body("budgetCents", org.hamcrest.CoreMatchers.is(20000));
    }

}