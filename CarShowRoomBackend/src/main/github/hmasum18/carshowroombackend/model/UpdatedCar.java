/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroombackend.model;

import github.hmasum18.carshowroombackend.database.CarTableDao;

import java.util.concurrent.ExecutionException;

public class UpdatedCar{
    private Car oldCar;
    private Car newCar;

    public UpdatedCar(Car newCar){
        this.newCar = newCar;
        try {
            oldCar = new CarTableDao().query(newCar.getRegistration());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            this.oldCar = newCar;
        }
    }
}
