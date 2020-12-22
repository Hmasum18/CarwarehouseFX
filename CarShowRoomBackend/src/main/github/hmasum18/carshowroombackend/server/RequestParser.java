/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroombackend.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import github.hmasum18.carshowroombackend.model.Car;
import github.hmasum18.carshowroombackend.model.LoginInfo;
import github.hmasum18.carshowroombackend.model.Meta;
import github.hmasum18.carshowroombackend.model.UserInfo;

/**
 * As the name suggest this class parse the request and data sent by a client
 */
public class RequestParser {
    public static final String TAG = "RequestParser->";
    private Meta meta;
    private String data;
    private Gson gson = new Gson();

    public RequestParser(String meta, String data) {
        this.meta = gson.fromJson(meta,Meta.class);
        this.data = data;
        System.out.println(TAG+" constructor : parsed meta class : "+this.meta);
        System.out.println(TAG+" constructor: dataSize : "+data.length());
    }

    public boolean isDataIsValid(){
        if( (data.equals("null")&&meta.getContentLength() == 0) || (data.length() == meta.getContentLength()) )
            return true;
        else
            return false;
    }

    public Meta getMeta() {
        return meta;
    }
    public String getDataAsString(){return data;}

    /**
     * when user login
     * @return
     */
    public LoginInfo getLoginInfo() {
        LoginInfo loginInfo = null;
        if(meta.getContentType() == Meta.ContentType.LOGIN_INFO){
            loginInfo = gson.fromJson(data,LoginInfo.class);
        }
        return loginInfo;
    }

    /**
     * when a new user is registered
     * @return
     */
    public UserInfo getUserInfo(){
        UserInfo userInfo = null;
        if(meta.getContentType() == Meta.ContentType.USER_INFO){
            userInfo = gson.fromJson(data,UserInfo.class);
        }
        return userInfo;
    }

    /**
     * update and delete car data
     * @return
     */
    public Car getCar(){
        Car car = null;
        if(meta.getContentType() == Meta.ContentType.CAR){
            car = gson.fromJson(data,Car.class);
        }
        return car;
    }
}
