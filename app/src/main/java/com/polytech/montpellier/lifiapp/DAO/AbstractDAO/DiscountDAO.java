package com.polytech.montpellier.lifiapp.DAO.AbstractDAO;

import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;

import java.util.Date;

/**
 * Created by Kevin on 30/04/2018.
 */

public abstract class DiscountDAO extends DAO<Discount> {

    public abstract void getAllByDate(Date date, ResponseHandler response) throws DAOException;
}
