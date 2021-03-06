package com.polytech.montpellier.lifiapp.Model.Discounts;

import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.Date;

/**
 * Created by Kevin on 02/05/2018.
 */

public class PercentageDiscount extends Discount {

    float percentage;

    public PercentageDiscount(int id, Product product, Date dateDebut, Date dateFin, Date dateCreation, float percentage,int fidelity) {
        super(id, product, dateDebut, dateFin, dateCreation, fidelity);
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    @Override
    public float oldPrice() {
        return this.product.getPrice();
    }

    @Override
    public float newPrice() {
        float oldPrice = this.oldPrice();
        return (oldPrice - oldPrice*getPercentage()/100);
    }


}
