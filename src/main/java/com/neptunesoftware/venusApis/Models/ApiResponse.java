package com.neptunesoftware.venusApis.Models;


import lombok.Data;

@Data
public class ApiResponse<T> {
    public T data;
    public Response response;

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<T>();
    }

    public static class ApiResponseBuilder<T> {
        private T data;
        private Response response;

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder<T> response(Response response) {
            this.response = response;
            return this;
        }

        public ApiResponse<T> build() {
            ApiResponse<T> obj = new ApiResponse<>();
            obj.data = this.data;
            obj.response = this.response;
            return obj;
        }
    }
}
