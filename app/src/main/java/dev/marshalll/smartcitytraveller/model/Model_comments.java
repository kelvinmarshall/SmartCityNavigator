package dev.marshalll.smartcitytraveller.model;

public class Model_comments {
    private String message;
    private String  userId;

    public Model_comments() {
    }

    public Model_comments(String message, String userId) {
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
