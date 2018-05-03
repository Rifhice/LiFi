package com.polytech.montpellier.lifiapp.Helper;

import org.json.JSONObject;

public interface ResponseHandler {
    void onSuccess(Object object);
    void onError(Object object);
}
