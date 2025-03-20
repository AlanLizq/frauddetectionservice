package com.frauddetectservice.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int status;
    private boolean success;
    private T data;
    private String error;

    // 成功响应
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setSuccess(true);
        response.setData(data);
        response.setError(null);
        return response;
    }

    // 失败响应
    public static <T> ApiResponse<T> error(int status, String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setSuccess(false);
        response.setData(null);
        response.setError(error);
        return response;
    }
}
