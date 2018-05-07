package com.polytech.montpellier.lifiapp.DAO.MySqlDAO;

import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DAOException;
import com.polytech.montpellier.lifiapp.DAO.AbstractDAO.DiscountDAO;
import com.polytech.montpellier.lifiapp.Helper.Helper;
import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;
import com.polytech.montpellier.lifiapp.Model.Discounts.Discount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kevin on 30/04/2018.
 */

public class MySqlDiscountDAO extends DiscountDAO {

    @Override
    public void create(Discount obj,String token, ResponseHandler response) throws DAOException {

    }

    @Override
    public void getById(int id, ResponseHandler response) throws DAOException {

    }

    @Override
    public void update(Discount obj,String token, ResponseHandler response) throws DAOException {

    }

    @Override
    public void delete(int id,String token, ResponseHandler response) throws DAOException {

    }

    @Override
    public void getAll(ResponseHandler response) throws DAOException {

    }
}
