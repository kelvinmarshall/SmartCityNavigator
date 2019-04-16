package dev.marshalll.smartcitytraveller.model;

public class Model_User {
    private  String username;
    private  String image;

    public Model_User() {
    }

    public Model_User(String username, String image) {
        this.username = username;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
