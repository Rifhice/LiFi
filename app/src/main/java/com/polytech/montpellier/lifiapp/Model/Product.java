package com.polytech.montpellier.lifiapp.Model;

/**
 * Created by Kevin on 30/04/2018.
 */

public class Product {

    String name;
    String description;
    float price;
    String brand;
    Department department;

    public Product(String name, String description, float price, String brand, Department department) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
