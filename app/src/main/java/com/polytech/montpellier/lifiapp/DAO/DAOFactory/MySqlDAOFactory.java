package com.polytech.montpellier.lifiapp.DAO.DAOFactory;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.MySqlDAO.MySqlDepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.MySqlDAO.MySqlDiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.MySqlDAO.MySqlLampDAO;
import com.polytech.montpellier.lifiapp.DAO.MySqlDAO.MySqlProductDAO;

/**
 * Created by Kevin on 30/04/2018.
 */

public class MySqlDAOFactory extends AbstractDAOFactory{

    @Override
    public LampDAO getLampDAO() {
        return new MySqlLampDAO();
    }

    @Override
    public DepartmentDAO getDepartmentDAO() {
        return new MySqlDepartmentDAO();
    }

    @Override
    public DiscountDAO getDiscountDAO() {
        return new MySqlDiscountDAO();
    }

    @Override
    public ProductDAO getProductDAO() {
        return new MySqlProductDAO();
    }


}