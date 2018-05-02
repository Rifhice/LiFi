package com.polytech.montpellier.lifiapp.Model;

/**
 * Created by Kevin on 30/04/2018.
 */

public class Lamp {

    String name;
    Department department;


    public Lamp(String name, Department department) {
        this.department = department;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
