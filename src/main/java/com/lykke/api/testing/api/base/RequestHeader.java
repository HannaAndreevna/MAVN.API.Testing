package com.lykke.api.testing.api.base;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestHeader {

    public static RequestSpecification getHeader(String token) {
        // TODO: make it switchable from the configuration file
        return getGivenRequestSpecification(true)
                .header("Authorization", "Bearer " + token)
                .contentType(JSON)
                .when();
    }

    public static RequestSpecification getHeader() {
        // TODO: make it switchable from the configuration file
        return getGivenRequestSpecification(true)
                .contentType(JSON)
                .when();
    }

    public static RequestSpecification getHeaderWithKey(String key) {
        // TODO: make it switchable from the configuration file
        return getGivenRequestSpecification(true)
                .header("api-key", key)
                .contentType(JSON)
                .when();
    }

    private RequestSpecification getGivenRequestSpecification(boolean displayLogAll) {
        return displayLogAll
                ? given().log().all()
                : given();
    }
}
