package com.polytech.montpellier.lifiapp.Model.Discounts;

import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.Date;

/**
 * Created by Kevin on 02/05/2018.
 */

public class FidelityPercentageDiscount extends PercentageDiscount {

    public FidelityPercentageDiscount(Product product, Date dateDebut, Date dateFin, Date dateCreation, float percentage) {
        super(product, dateDebut, dateFin, dateCreation, percentage);
    }

}
