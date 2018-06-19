package com.lehoangduy.smartworld.Model;

/**
 * Created by Admin on 12/12/2016.
 */

public class Config {

    public static final String DATA_PL = "http://hostfree321.esy.es/getdienthoai.php?MaLoai=";
    public static final String DETAIL_URL = "http://hostfree321.esy.es/getdetailsp.php?Id=";
    public static final String DATA_URL = "http://hostfree321.esy.es/getcustomer.php?Mail=";
    public static final String JSON_ARRAY = "result";

    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";


}
