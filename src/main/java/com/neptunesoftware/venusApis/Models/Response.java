package com.neptunesoftware.venusApis.Models;


public class Response {
    private final String code;
    private final String message;

    // Add this constructor
    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getters (no setters needed for immutable approach)
    public String getCode() { return code; }
    public String getMessage() { return message; }
}