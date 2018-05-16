package com.polytech.montpellier.lifiapp.Model.Discounts;

import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.Date;

/**
 * Created by Kevin on 30/04/2018.
 */

public abstract class Discount {

    int id;
    Product product;
    Date dateDebut;
    Date dateFin;
    Date dateCreation;
    int fidelity;

    public Discount(int id, Product product, Date dateDebut, Date dateFin, Date dateCreation, int fidelity) {
        this.id = id;
        this.product = product;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dateCreation = dateCreation;
        this.fidelity = fidelity;
    }


    public int getId() {
        return this.id ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getFidelity(){return fidelity;}

    public void setFidelity(int fidelity){this.fidelity = fidelity;}

    public abstract float oldPrice();
    public abstract float newPrice();
}
