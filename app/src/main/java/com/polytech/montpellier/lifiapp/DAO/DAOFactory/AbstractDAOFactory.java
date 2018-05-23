package com.polytech.montpellier.lifiapp.DAO.DAOFactory;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.LampDAO;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.ProductDAO;

/**
 * Created by Kevin on 30/04/2018.
 */

public abstract class AbstractDAOFactory {

    // ==================== //
    // ==== ATTRIBUTES ==== //
    // ==================== //
    public static final int DISTANT_DAO_FACTORY = 0;

    // ================= //
    // ==== METHODS ==== //
    // ================= //
    /**
     * Return a DAO factory according to the type given (default = MySQL)
     * 0 = MySQL DAO
     * @param type, int
     * @return AsbstractDAOFactory
     */
    public static AbstractDAOFactory getFactory(int type){
        switch(type) {
            case(DISTANT_DAO_FACTORY):
                return new DistantDAOFactory();
            default:
                return new DistantDAOFactory();
        }
    }

    // ==== DAOs ==== //
    public abstract LampDAO getLampDAO();
    public abstract DepartmentDAO getDepartmentDAO();
    public abstract DiscountDAO getDiscountDAO();
    public abstract ProductDAO getProductDAO();
}
