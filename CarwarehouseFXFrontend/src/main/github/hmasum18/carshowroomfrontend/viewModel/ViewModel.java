/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.viewModel;

import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.carshowroom.ServerHolder;
import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.model.LoginInfo;
import github.hmasum18.carshowroomfrontend.model.UserInfo;
import github.hmasum18.carshowroomfrontend.repository.Repository;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

public class ViewModel {
    public static final String TAG = "ViewModel->";
    private static ViewModel instance;
    private Repository repository;
    private ServerHolder serverHolder;
    private List<Car> carList;
    private UserInfo userInfo;
    private ViewModel(){
        carList = new ArrayList<>();
    }

    public static ViewModel getInstance() {
        if(instance == null)
            instance = new ViewModel();
        return instance;
    }

   // public void addCars(Li)

    /**
     * will be only called from main just once
     * @param serverHolder
     */
    public void setServerHolder(ServerHolder serverHolder) {
        this.serverHolder = serverHolder;
        repository = new Repository(serverHolder);

    }

    public Repository getRepository() {
        return repository;
    }

    public void storeCarList(List<Car> carList) {
        this.carList = carList;
    }

    public List<Car> getStoredCarList(){
        return carList;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void login(LoginInfo loginInfo) throws Exception {
        repository.login(loginInfo);
    }

    public void getCarList(){
        repository.getCarList();
    }

    public void submitCar(Car car, Meta.RequestType requestType){
        System.out.println(TAG + " submitting car data.");
        repository.submitCar(car,requestType);
    }

    public void removeCar(String carReg){
        repository.deleteCar(carReg);
    }
}
