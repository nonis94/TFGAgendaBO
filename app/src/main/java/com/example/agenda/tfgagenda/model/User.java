package com.example.agenda.tfgagenda.model;

/**
 * Created by Nonis123 on 12/04/2018.
 */

public class User {

    private int id;
    private String name;
    private String email;
    private String password;

    public int getId(){
        return id;
    }
    public void setId(int newId){
        this.id=newId;
    }
    public String getName(){
        return name;
    }
    public void setName(String newName){
        this.name=newName;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String newEmail){
        this.email=newEmail;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String newPassword){
        this.password=newPassword;
    }
}
