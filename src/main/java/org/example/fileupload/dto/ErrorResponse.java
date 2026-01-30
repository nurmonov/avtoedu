package org.example.fileupload.dto;


public class ErrorResponse {
    private int status;
    private String error;
    private String message;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    // getter va setter lar (yoki Lombok @Data ishlatish mumkin)
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
}
