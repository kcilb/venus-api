package com.neptunesoftware.venusApis.Util;


import com.neptunesoftware.venusApis.Models.Response;


public class StaticRefs {
    public static Response success() {
        return new Response("0", "Operation successful");
    }
    public static Response noRecords() {
        return new Response("-11", "No records found");
    }


    public static Response customMessage(String code, String message) {
        return new Response(code, message);
    }

    public static Response serverError() {
        return new Response("-99", "An error occurred while processing request");
    }

}
