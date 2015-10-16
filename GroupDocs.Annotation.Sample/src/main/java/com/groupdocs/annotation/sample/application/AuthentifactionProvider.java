package com.groupdocs.annotation.sample.application;

import com.groupdocs.annotation.contracts.IAnnotationDataLayer;
import com.groupdocs.annotation.contracts.dataobjects.UserDataObject;
import com.groupdocs.annotation.datalayer.sample.JsonDataSaver;
import java.util.ArrayList;
import javax.servlet.http.Cookie;

public final class AuthentifactionProvider {
    static JsonDataSaver storageManager;
    
    public static void setStorageManager(IAnnotationDataLayer dataLayer){
        storageManager = (JsonDataSaver)dataLayer;
    }
    
    public static Cookie logIn(String id) {
        Cookie cookie = new Cookie("id", id);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600*24);
        return cookie;
    }

    public static Cookie logOut() {
        Cookie cookie = new Cookie("id", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        return cookie;
    }

    public static String getUserId(Cookie[] cookies) {
        String id = "00000000-0000-0000-0000-000000000001";
        for (Cookie tmpCookie : cookies) {
            if (tmpCookie.getName().equals("id")) {
                id = tmpCookie.getValue();
            }
        }
        return id;
    }
    
    public static boolean validate(String login, String password) throws Exception{
        ArrayList<UserDataObject> usersList = storageManager.getUsers();
        for(UserDataObject user : usersList){
            if(user.getLogin().equals(login) && user.getPassword().equals(password))
                return true;
        }
        return false;
    }

}
