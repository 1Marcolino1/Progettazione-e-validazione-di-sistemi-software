package it.univr.track.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ShipmentControllerTest {

    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void createShipmentTest(){
        String body = "{name : Test}";
        given()
        .contentType("application/json")
        .body(body)
        .when()
        .post("/shipment")
        .then()
        .statusCode(200)
        .body(Matchers.is("true"));

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void readExistingShipment() {
        Long shipmentId =
                given().post("/shipment")
                        .then().extract().as(Boolean.class) ? 1L : null;

        Assertions.assertNotNull(shipmentId);
        given()
                .when()
                .get("/shipment/{id}", shipmentId)
                .then()
                .statusCode(200)
                .body("id", equalTo(shipmentId.intValue()));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void readNonExistingShipment() {
        given()
                .when()
                .get("/shipment/{id}", 9999)
                .then()
                .statusCode(404);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void editExistingShipment() {
        given().post("/shipment");

        String body = "{ \"id\": 1, \"name\": \"Test\", \"isTracked\": false }";

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/shipment")
                .then()
                .statusCode(200)
                .body(equalTo("true"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteExistingShipmentTest() {
        String body = "{\"name\": \"Test\"}";
        given()
            .contentType("application/json")
            .body(body)
            .when()
            .post("/shipment");
        given()
            .param("id", 1)
            .when()
            .delete("/api/shipment")
            .then()
            .statusCode(200);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteShipmentNoneExistTest() {
        given()
                .param("id", 999)
                .when()
                .delete("/api/shipment")
                .then()
                .statusCode(404);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteShipmentWithDevicesForceAndNotForceTest() {
        String body = "{\"name\": \"Test\"}";
        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/shipment");
        given()
                .contentType("application/json")
                .when()
                .post("/api/device");

        given()
                .contentType("application/json")
                .post("/api/shipment/allocate?shipmentId=1&deviceId=1")
                .then()
                .statusCode(200);


        given()
                .param("id", 1)
                .when()
                .delete("/api/shipment")
                .then()
                .statusCode(409);

        given()
                .param("id", 1)
                .param("forceDeletion", true)
                .when()
                .delete("/api/shipment")
                .then()
                .statusCode(200);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void readShipmentsTest() {
        String body = "{\"name\": \"Test\"}";
        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/shipment");

        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/shipment");

        given()
                .contentType("application/json")
                .when()
                .get("/api/shipments")
                .then()
                .statusCode(200)
                .body("$", notNullValue())
                .body("$", instanceOf(java.util.List.class))
                .body("$.size()", equalTo(2));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void allocateExistingDeviceToExistingShipment() {
        String body = "{\"name\": \"Test\"}";
        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/shipment");
        given()
                .contentType("application/json")
                .when()
                .post("/api/device");

        given()
                .contentType("application/json")
                .post("/api/shipment/allocate?shipmentId=1&deviceId=1")
                .then()
                .statusCode(200);


    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deallocateExistingDeviceToExistingShipment() {
        String body = "{\"name\": \"Test\"}";
        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/shipment");
        given()
                .contentType("application/json")
                .when()
                .post("/api/device");

        given()
                .contentType("application/json")
                .post("/api/shipment/allocate?shipmentId=1&deviceId=1")
                .then()
                .statusCode(200);


        given()
                .contentType("application/json")
                .post("/api/shipment/deallocate?shipmentId=1&deviceId=1")
                .then()
                .statusCode(200);


    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void toggleTrackingStatus()
    {
        String body = "{\"name\": \"Test\"}";
        given()
                .contentType("application/json")
                .body(body)
                .when()
                .post("/shipment");
        given()
        .contentType("application/json")
                .when()
                .post("/api/shipment/tracking?id=1&tracked=true")
                .then()
                .body(equalTo("true"))
                .statusCode(200);


    }
}
