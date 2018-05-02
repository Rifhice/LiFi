package com.polytech.montpellier.lifiapp.Model.Discounts;

import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.Date;

/**
 * Created by Kevin on 02/05/2018.
 */

public abstract class QuantityDiscount extends Discount {

    int bought;
    int free;

    public QuantityDiscount(Product product, Date dateDebut, Date dateFin, Date dateCreation, int bought, int free) {
        super(product, dateDebut, dateFin, dateCreation);
        this.bought = bought;
        this.free = free;
    }

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    @Override
    public float oldPrice() {
        return 0;
    }

    @Override
    public float newPrice() {
        return 0;
    }
}
