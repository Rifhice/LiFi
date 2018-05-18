package com.polytech.montpellier.lifiapp.DAO.AbstractDAO;

import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Product;

/**
 * Created by Kevin on 30/04/2018.
 */

public abstract class ProductDAO extends DAO<Product> {

    public abstract void getProductDiscounts(Product product, ResponseHandler response) throws DAOException;
}
