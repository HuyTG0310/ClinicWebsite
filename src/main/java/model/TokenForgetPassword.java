/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author Tai Loi
 */
public class TokenForgetPassword {
    private int id, userId;
    private boolean isUsed;
    private String token;
    public LocalDateTime expriryTime;

    public TokenForgetPassword(int id, int userId, boolean isUsed, String token, LocalDateTime expriryTime) {
        this.id = id;
        this.userId = userId;
        this.isUsed = isUsed;
        this.token = token;
        this.expriryTime = expriryTime;
    }
    public TokenForgetPassword(){
        
    }
    public TokenForgetPassword(int userId, boolean isUsed, String token, LocalDateTime expriryTime) {
        this.userId = userId;
        this.isUsed = isUsed;
        this.token = token;
        this.expriryTime = expriryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpriryTime() {
        return expriryTime;
    }

    public void setExpriryTime(LocalDateTime expriryTime) {
        this.expriryTime = expriryTime;
    }

    @Override
    public String toString() {
        return "TokenForgetPassword{" + "id=" + id + ", userId=" + userId + ", isUsed=" + isUsed + ", token=" + token + ", expriryTime=" + expriryTime + '}';
    }
}
