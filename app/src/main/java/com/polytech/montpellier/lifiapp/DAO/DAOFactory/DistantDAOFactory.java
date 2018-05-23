package com.polytech.montpellier.lifiapp.DAO.DAOFactory;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;
import com.polytech.montpellier.lifiapp.DAO.DistantDAO.DistantDepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.DistantDAO.DistantDiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.DistantDAO.DistantLampDAO;
import com.polytech.montpellier.lifiapp.DAO.DistantDAO.DistantProductDAO;

/**
 * Created by Kevin on 30/04/2018.
 */

public class DistantDAOFactory extends AbstractDAOFactory{

    @Override
    public LampDAO getLampDAO() {
        return new DistantLampDAO();
    }

    @Override
    public DepartmentDAO getDepartmentDAO() {
        return new DistantDepartmentDAO();
    }

    @Override
    public DiscountDAO getDiscountDAO() {
        return new DistantDiscountDAO();
    }

    @Override
    public ProductDAO getProductDAO() {
        return new DistantProductDAO();
    }


}