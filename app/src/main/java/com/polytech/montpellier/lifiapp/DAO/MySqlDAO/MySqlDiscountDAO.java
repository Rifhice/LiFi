package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;

import java.util.ArrayList;

/**
 * Created by Kevin on 30/04/2018.
 */

public class MySqlDiscountDAO extends DiscountDAO {
    @Override
    public Discount create(Discount obj) throws DAOException {
        return null;
    }

    @Override
    public Discount getById(int id) throws DAOException {
        return null;
    }

    @Override
    public int update(Discount obj) throws DAOException {
        return 0;
    }

    @Override
    public int delete(int id) throws DAOException {
        return 0;
    }

    @Override
    public ArrayList<Discount> getAll() throws DAOException {
        return null;
    }
}
