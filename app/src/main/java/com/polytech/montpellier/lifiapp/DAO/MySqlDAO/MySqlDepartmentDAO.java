package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DepartmentDAO;
import com.polytech.montpellier.lifiapp.Model.Department;

import java.util.ArrayList;

/**
 * Created by Kevin on 30/04/2018.
 */

public class MySqlDepartmentDAO extends DepartmentDAO{
    @Override
    public Department create(Department obj) throws DAOException {
        return null;
    }

    @Override
    public Department getById(int id) throws DAOException {
        return null;
    }

    @Override
    public int update(Department obj) throws DAOException {
        return 0;
    }

    @Override
    public int delete(int id) throws DAOException {
        return 0;
    }

    @Override
    public ArrayList<Department> getAll() throws DAOException {
        return null;
    }
}
