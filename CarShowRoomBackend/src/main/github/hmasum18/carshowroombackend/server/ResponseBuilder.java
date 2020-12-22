/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroombackend.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import github.hmasum18.carshowroombackend.model.Car;
import github.hmasum18.carshowroombackend.model.LoginInfo;
import github.hmasum18.carshowroombackend.model.Meta;
import github.hmasum18.carshowroombackend.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * this class build a formatted response before sending it to client
 */
public class ResponseBuilder {
    private String meta;
    private String data;
    private Gson gson = new Gson();

    public void setMeta(Meta meta) {
        this.meta = gson.toJson(meta);;
    }

    public void setData(String data){
        this.data = data;
    }

    public void setData(UserInfo userInfo) {
        this.data = gson.toJson(userInfo);
    }

    public void setData(LoginInfo loginInfo){
        this.data = gson.toJson(loginInfo);
    }

    public void setData(Car car) {
        this.data = gson.toJson(car);
    }

    public void setData(List<Car> carList){
        JsonArray jsonArray = (JsonArray) gson.toJsonTree(carList, new TypeToken<ArrayList<Car>>() {
        }.getType());
        this.data = gson.toJson(jsonArray);
    }

    public String getMeta() {
        return meta;
    }

    public String getData() {
        return data;
    }
}
