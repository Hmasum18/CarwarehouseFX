/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.carshowroom;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.model.UserInfo;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.List;

public class ResponseParser {
    public static final String TAG = "ResponseParser->";
    private Meta meta;
    private String data;
    private final Gson gson = new Gson();

    public ResponseParser(String meta, String data) {
        this.meta = gson.fromJson(meta,Meta.class);
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public UserInfo getUserInfo(){
        UserInfo userInfo = null;
        if(meta.getContentType() == Meta.ContentType.USER_INFO){
            userInfo = gson.fromJson(data,UserInfo.class);
        }
        return userInfo;
    }

    public Car getCar(){
        Car car = null;
        if(meta.getContentType() == Meta.ContentType.CAR){
            car = gson.fromJson(data,Car.class);
        }
        return car;
    }

    public List<Car> getCarList(){
        List<Car> carList = null;
        if(meta.getContentType() == Meta.ContentType.CAR_LIST){
            System.out.println(TAG+" getCarList(): converting data to car list");
            carList = gson.fromJson(data,new TypeToken<ArrayList<Car>>() {
            }.getType());
            if(carList!=null)
                System.out.println(TAG+" getCarList(): received carList size "+carList.size());
            else
                System.out.println(TAG+" getCarList(): received carList is null");
        }
        return carList;
    }

    public void getClientInfoList(){

    }
}
