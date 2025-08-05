package com.neptunesoftware.venusApis.Util;


import com.neptunesoftware.venusApis.Models.Responses;


public class StaticRefs {
    public static Responses success() {
        return new Responses("0", "Operation successful");
    }

    public static Responses customMessage(String code, String message) {
        return new Responses(code, message);
    }

    public static Responses serverError() {
        return new Responses("-99", "An error occurred while processing request");
    }

}
