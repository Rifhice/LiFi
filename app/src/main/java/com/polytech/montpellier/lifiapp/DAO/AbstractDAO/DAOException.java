package com.polytech.montpellier.lifiapp.DAO.AbstractDAO;

/**
 * Created by Kevin on 30/04/2018.
 */

public class DAOException extends RuntimeException {
    // ====================== //
    // ==== CONSTRUCTORS ==== //
    // ====================== //
    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
