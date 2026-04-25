package com.chanakya.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private int status;

    // Constructors
    public ApiResponse(boolean success, String message, T data) {
        this(success, message, data, success ? 200 : 400);
    }

    public ApiResponse(boolean success, String message, T data, int status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Builder
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private boolean success;
        private String message;
        private T data;
        private int status;
        private LocalDateTime timestamp;

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }

        // ✅ Add this method
        public Builder<T> timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        // ✅ Add this method for String timestamp
        public Builder<T> timestamp(String timestamp) {
            this.timestamp = timestamp != null ? LocalDateTime.parse(timestamp) : LocalDateTime.now();
            return this;
        }

        public ApiResponse<T> build() {
            ApiResponse<T> response = new ApiResponse<>(success, message, data, status);
            if (timestamp != null) {
                response.setTimestamp(timestamp);
            }
            return response;
        }

    }
}