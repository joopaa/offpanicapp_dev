package com.panic.anhhoang.officerapp.config;

/**
 * Created by AnhHoang on 12/3/2015.
 */
public class AppConfig {
    /* Method: POST, Params: phone, pin*/
    public static String URL_SERVER_LOGIN = "http://quandh.com/panic_server/api/auth/login";
    /* Method: POST, Params: phone, name, password*/
    public static String URL_SERVER_REGISTER = "http://quandh.com/panic_server/api/users/register";
    /* Method: PUT, Params: user_id, pin*/
    public static String URL_SERVER_REGISTER_PIN = "http://quandh.com/panic_server/api/users/addPIN";
    /* Method: PUT, Params: order_id, officer_name*/
    public static String URL_SERVER_RESPONSE_HELP = "http://quandh.com/panic_server/api/orders/accept";
    /* Get all order*/
    public static String URL_SERVER_GET_REQUEST = "http://quandh.com/panic_server/api/orders";
    public static String URL_ADD_GCM_TOKEN = "http://quandh.com/panic_server/api/gcm/add";
}
