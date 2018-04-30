package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.Model.Product;

import java.util.ArrayList;

/**
 * Created by Kevin on 30/04/2018.
 */

public class MySqlProductDAO extends ProductDAO {
    @Override
    public Product create(Product obj) throws DAOException {
        return null;
    }

    @Override
    public Product getById(int id) throws DAOException {
        return null;
    }

    @Override
    public int update(Product obj) throws DAOException {
        return 0;
    }

    @Override
    public int delete(int id) throws DAOException {
        return 0;
    }

    @Override
    public ArrayList<Product> getAll() throws DAOException {
        return null;
    }
}
