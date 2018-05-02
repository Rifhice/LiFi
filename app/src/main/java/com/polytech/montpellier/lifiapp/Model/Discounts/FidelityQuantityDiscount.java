package com.polytech.montpellier.lifiapp.Model.Discounts;

import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.Date;

/**
 * Created by Kevin on 02/05/2018.
 */

public class FidelityQuantityDiscount extends QuantityDiscount {

    public FidelityQuantityDiscount(Product product, Date dateDebut, Date dateFin, Date dateCreation, int bought, int free) {
        super(product, dateDebut, dateFin, dateCreation, bought, free);
    }
    
}
