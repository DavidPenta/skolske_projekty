package com.musicverse.client.objects;

public class User {
    private String nickName;
    private String email;
    private int role;
    private int id;
    private String createdAt;

    public User(String nickName, String email, int role, int id, String createdAt){
        this.nickName = nickName;
        this.email = email;
        this.role = role;
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
