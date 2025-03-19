package com.frauddetectservice.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int status;      // 状态码（例如 200、400、500）
    private boolean success; // 请求是否成功
    private T data;         // 返回的数据
    private String error;   // 错误信息

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
