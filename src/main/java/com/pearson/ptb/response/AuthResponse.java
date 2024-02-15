package com.pearson.ptb.response;

public class AuthResponse {
    private boolean success;
    private String jsonResponse;

    public AuthResponse(boolean success, String jsonResponse) {
        this.success = success;
        this.jsonResponse = jsonResponse;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }
}
