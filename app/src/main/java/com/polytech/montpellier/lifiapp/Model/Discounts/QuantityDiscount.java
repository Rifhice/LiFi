package com.polytech.montpellier.lifiapp.Model.Discounts;

import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.Date;

/**
 * Created by Kevin on 02/05/2018.
 */

public class QuantityDiscount extends Discount {

    int bought;
    int free;

    public QuantityDiscount(int id, Product product, Date dateDebut, Date dateFin, Date dateCreation, int bought, int free,int fidelity) {
        super(id,product, dateDebut, dateFin, dateCreation,fidelity);
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
        return this.product.getPrice()*(this.bought +this.free);
    }

    @Override
    public float newPrice() {
        return this.product.getPrice()*(this.bought);
    }
}
