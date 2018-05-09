package com.polytech.montpellier.lifiapp.Model;

/**
 * Created by Kevin on 30/04/2018.
 */

public class Department {

    int id;
    String name;

    public Department(int id){
        this.id = id;
    }

    public Department(int id, String name){
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
