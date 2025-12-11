package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class LabseqCalculatorTest {
    @Test
    void testCalculate1() {
        given()
          .when().get("/labseq/1")
          .then()
             .statusCode(200)
             .body(is("1"));
    }
}