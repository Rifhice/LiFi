package com.polytech.montpellier.lifiapp.DAO.AbstractDAO;

import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Department;

/**
 * Created by Kevin on 30/04/2018.
 */

public abstract class DepartmentDAO extends DAO<Department> {

    public abstract void getAllProducts(Department dep, ResponseHandler res);

}
