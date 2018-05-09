package com.polytech.montpellier.lifiapp.DAO.AbstractDAO;

import com.polytech.montpellier.lifiapp.Helper.ResponseHandler;

import java.util.ArrayList;

/**
 * Created by Kevin on 30/04/2018.
 */
public abstract class DAO <T> {

    // ====================== //
    // ==== ATTRIBUTES ==== //
    // ====================== //

    // ====================== //
    // ==== CONSTRUCTORS ==== //
    // ====================== //
    public DAO() {

    }

    // ================= //
    // ==== METHODS ==== //
    // ================= //
    /**
     * Create a new object and return it.
     * @param obj
     * @return T, the obj created or null if an error occurred.
     */
    public abstract void create(T obj, String token, ResponseHandler response) throws DAOException;

    /**
     * Get an existing object by his id.
     * @param id, int
     * @return T
     */
    public abstract void getById(int id,ResponseHandler response) throws DAOException;

    /**
     * Modify an existing object.
     * @param obj
     * @return int, number of lines updated
     */
    public abstract void update(T obj, String token, ResponseHandler response) throws DAOException;

    /**
     * Delete the object with id equals to the given one.
     * @param id, int
     * @return int, the number of rows deleted.
     */
    public abstract void delete(int id, String token, ResponseHandler response) throws DAOException;

    /**
     * Get all the existing objects.
     * @return ArrayList<T>, an ArrayList of all the objects.
     */
    public abstract void getAll(ResponseHandler response) throws DAOException;
}
