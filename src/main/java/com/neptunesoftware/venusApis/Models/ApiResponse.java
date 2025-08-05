package com.neptunesoftware.venusApis.Models;


import lombok.Data;

@Data
public class ApiResponse<T> {
    public T data;
    public Responses responses;

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<T>();
    }

    public static class ApiResponseBuilder<T> {
        private T data;
        private Responses responses;

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder<T> response(Responses responses) {
            this.responses = responses;
            return this;
        }

        public ApiResponse<T> build() {
            ApiResponse<T> obj = new ApiResponse<>();
            obj.data = this.data;
            obj.responses = this.responses;
            return obj;
        }
    }
}
