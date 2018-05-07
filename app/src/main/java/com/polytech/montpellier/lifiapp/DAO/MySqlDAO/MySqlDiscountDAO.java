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

        Helper.getInstance(this);
        String url = "http://81.64.139.113:1337/api/Discount";

        Helper.getInstance(this).GET(url, new ResponseHandler() {
            @Override
            public void onSuccess(Object object) {
                System.out.println("GET 1 DAO"+object.toString());

                JSONArray getJSON = new JSONArray();
                getJSON = (JSONArray) object;

                for (int i = 0; i < getJSON.length(); i++) {
                    try {
                        JSONObject jsonObj = getJSON.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

            }

            @Override
            public void onError(Object object) {

            }
        });
    }
}
