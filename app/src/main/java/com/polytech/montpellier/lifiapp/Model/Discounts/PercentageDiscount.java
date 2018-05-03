package com.polytech.montpellier.lifiapp.Model.Discounts;

import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.Date;

/**
 * Created by Kevin on 02/05/2018.
 */

public abstract class PercentageDiscount extends Discount {

    float percentage;

    public PercentageDiscount(Product product, Date dateDebut, Date dateFin, Date dateCreation, float percentage) {
        super(product, dateDebut, dateFin, dateCreation);
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
        return 0;
    }

    @Override
    public float newPrice() {
        return 0;
    }
}