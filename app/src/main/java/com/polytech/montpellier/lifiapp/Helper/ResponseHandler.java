package com.polytech.montpellier.lifiapp.Helper;

public interface ResponseHandler {
    void onSuccess(Object object);
    void onError(Object object);
}
