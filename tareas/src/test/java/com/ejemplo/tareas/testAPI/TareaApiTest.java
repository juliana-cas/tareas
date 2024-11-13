package com.ejemplo.tareas.testAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TareaApiTest {

    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = "http://localhost:8080/api/tareas";
    }

    @Test
    public void testListarTareas() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("$", is(not(empty())));
    }

    @Test
    public void testAgregarTarea() {
        String nuevaTareaJson = "{\"descripcion\": \"Nueva tarea\"}";

        given()
                .contentType(ContentType.JSON)
                .body(nuevaTareaJson)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body(equalTo("Tarea agregada con éxito."));
    }


    @Test
    public void testEliminarTareaNoExistente() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testContarTareas() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/contar")
                .then()
                .statusCode(200)
                .body(equalTo("1"));
    }
}