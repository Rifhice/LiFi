package com.polytech.montpellier.lifiapp.Model.Discounts;

import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.Date;

/**
 * Created by Kevin on 02/05/2018.
 */

public class RegularPercentageDiscount extends PercentageDiscount {

    public RegularPercentageDiscount(Product product, Date dateDebut, Date dateFin, Date dateCreation, float percentage) {
        super(product, dateDebut, dateFin, dateCreation, percentage);
    }

    @Override
    public float oldPrice() {
        return this.product.getPrice();
    }

    @Override
    public float newPrice() {
        float oldPrice = this.oldPrice();
        return (oldPrice - oldPrice*getPercentage());
    }

}
