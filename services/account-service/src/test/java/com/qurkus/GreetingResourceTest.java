package com.qurkus;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class GreetingResourceTest {
    @Test
    @TestSecurity(user = "user", roles = "USER")
    void testTotalBalanceEndpoint() {
        given()
          .when().get("/accounts/total-balance")
          .then()
             .statusCode(200)
             .body("totalBalanceCents", org.hamcrest.CoreMatchers.is(500000));
    }

}