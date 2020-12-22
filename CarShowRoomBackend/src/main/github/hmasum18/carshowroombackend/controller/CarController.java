package github.hmasum18.carshowroombackend.controller;

import github.hmasum18.carshowroombackend.database.CarTableDao;
import github.hmasum18.carshowroombackend.model.Car;
import github.hmasum18.carshowroombackend.model.CarImageHolder;
import res.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CarController {
    public static final String TAG = "Controller-Debug->";
    CarTableDao carTableDao;

    public CarController() {
        this.carTableDao = new CarTableDao();
    }

    /**
     * get a car by it's registration number.
     * @param carReg is the registration number of the car.
     * @return car if found and null otherwise
     */
    public Car searchByCarReg(String carReg) throws ExecutionException, InterruptedException {
       return carTableDao.query(carReg);
    }


    public List<Car> getAllCars() throws Exception {
        /*List<Car> carList = carTableDao.queryAll();
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            byte[] bytes;
            try {
                bytes = R.image.getByteArrayByName(car.getImageName());
                CarImageHolder carImageHolder = new CarImageHolder(bytes);
                car.setCarImageHolder(carImageHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return carTableDao.queryAll();
    }

    /**
     * add a car to the database
     * @param car is the car to be added
     * @return the index of the car if successful and -1 otherwise
     */
    public boolean addCar(Car car) throws Exception {
        return carTableDao.insert(car);
    }

    public boolean updateCar(Car car) throws Exception {
        return carTableDao.update(car);
    }

    /**
     * add a car to the database
     * @param carReg is the registration number of the car to be added
     * @return the index of the car if successful and -1 otherwise
     */
    public boolean deleteByCarReg(String carReg) throws Exception {
        return carTableDao.delete(carReg);
    }


}
