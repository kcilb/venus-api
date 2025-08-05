package com.neptunesoftware.venusApis.Models;


import lombok.AllArgsConstructor;
import lombok.Data;


public class Responses {
    private final String code;
    private final String message;

    // Add this constructor
    public Responses(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getters (no setters needed for immutable approach)
    public String getCode() { return code; }
    public String getMessage() { return message; }
}