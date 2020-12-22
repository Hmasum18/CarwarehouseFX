package github.hmasum18.carshowroombackend.database;


import github.hmasum18.carshowroombackend.model.Car;
import github.hmasum18.carshowroombackend.model.CarImageHolder;
import res.R;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CarTableDao{
    public static final String TAG = "CarWareHouse->";
    private final ExecutorService executorService;
    private final Connection dbConnection;
    public CarTableDao(){
        executorService = SqliteConnectionBuilder.executorService;
        dbConnection = SqliteConnectionBuilder.getInstance().getConnection();
    }

    /**
     * insert a car in the carList
     * @param car is instance of car to be inserted.
     */
    public boolean insert(Car car) throws ExecutionException, InterruptedException {
        Future<Boolean> future = executorService.submit( () -> {
            String query = "INSERT INTO car " +
                    "VALUES (?,?,?,  ?,?,?,  ?,?,?,  ?,?,?)";
            try(PreparedStatement statement = dbConnection.prepareStatement(query)) {
                statement.setString(1,car.getRegistration());
                statement.setString(2,car.getModel());
                statement.setInt(3,car.getYear());
                statement.setString(4,car.getColor());
                statement.setString(5,car.getMake());
                statement.setInt(6,car.getEngineSize());
                statement.setString(7,car.getTransmission());
                statement.setString(8,car.getFuelType());
                statement.setInt(9,car.getSeatNumber());
                statement.setInt(10,car.getQuantity());
                statement.setInt(11,car.getPrice());
                statement.setString(12,car.getImageName());

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        });
        return future.get();
    }

    /**
     * update a car in the carList
     * @param car is instance of car to be inserted.
     */
    public boolean update(Car car) throws ExecutionException, InterruptedException {
        Future<Boolean> future = executorService.submit( () -> {
            String query = "UPDATE car " +
                    "SET " +
                    "model=?,"+ //1
                    "year = ?,"+ //2
                    "color = ?,"+ //3
                    "manufacturer = ?,"+ //4
                    "engineSize= ?,"+ //5
                    "transmission = ?,"+ //6
                    "fuelType = ?,"+ //7
                    "seat = ?,"+ //8
                    "price = ?,"+ //9
                    "quantity = ?,"+ //10
                    "imageUrl = ? "+ //11
                    "WHERE registration=?"; //12
            try(PreparedStatement statement = dbConnection.prepareStatement(query)) {
                //set
                statement.setString(1,car.getModel());
                statement.setInt(2,car.getYear());
                statement.setString(3,car.getColor());
                statement.setString(4,car.getMake());
                statement.setInt(5,car.getEngineSize());
                statement.setString(6,car.getTransmission());
                statement.setString(7,car.getFuelType());
                statement.setInt(8,car.getSeatNumber());
                statement.setInt(9,car.getPrice());
                statement.setInt(10,car.getQuantity());
                statement.setString(11,car.getImageName());
                //where
                statement.setString(12,car.getRegistration());

                statement.executeUpdate();
                System.out.println(TAG+" update: car data updated successfully");

                System.out.println(TAG+" update: saving new image file.");
                if(R.image.storeImage(car.getImageName(),car.getImageDataBytes()))
                    System.out.println(TAG+" stored new image file successfully.");
                else
                    System.out.println(TAG+" error saving new image file.");

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        });
        return future.get();
    }
    /**
     * delete car from carList if found.
     * @param carReg registration number of the car to be deleted if found.
     * @return the index of deleted car and -1 if the car doesn't exist
     */
    public boolean delete(String carReg) throws ExecutionException, InterruptedException {
        Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                String query = "DELETE FROM car WHERE registration = ?";
                try(PreparedStatement statement = dbConnection.prepareStatement(query)) {
                    statement.setString(1,carReg);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        });
        return future.get();
    }

    /**
     * find the car with registration number carReg from carList
     * @param carReg registration number of the car
     * @return return the instance of car if found and null is the car is not found
     */
    public Car query(String carReg) throws ExecutionException, InterruptedException {
        Future<Car> future =  executorService.submit(new Callable<Car>() {
            @Override
            public Car call() throws Exception {
                Car car = null;
                String query = "SELECT * FROM car WHERE registration = ?";
                try(PreparedStatement statement = SqliteConnectionBuilder.getInstance()
                        .getConnection().prepareStatement(query)) {
                    statement.setString(1,carReg);
                    ResultSet resultSet = statement.executeQuery();
                    while(resultSet.next()){
                        car = convertResultSetToCarInfo(resultSet);
                       // System.out.println(car.toString());
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return car;
            }
        });
        return future.get();
    }


    public List<Car> queryAll() throws ExecutionException, InterruptedException {
        Future<List<Car>> future =  executorService.submit(new Callable<List<Car>>() {
            @Override
            public List<Car> call() throws Exception {
                List<Car> carList = new ArrayList<>();
                String query = "SELECT * FROM car";
                try(PreparedStatement statement = dbConnection.prepareStatement(query)) {
                    ResultSet resultSet = statement.executeQuery();
                    while(resultSet.next()){
                        Car car = convertResultSetToCarInfo(resultSet);
                        carList.add(car);
                        // System.out.println(car.toString());
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return carList;
            }
        });
        return future.get();
    }

    private Car convertResultSetToCarInfo(ResultSet resultSet){
        Car car = null;
        try {
           // System.out.println(resultSet.getString(2));
            car = new Car();
            car.setRegistration(resultSet.getString("registration"));
            car.setModel(resultSet.getString("model"));
            car.setYear(resultSet.getInt("year"));
            car.setColor(resultSet.getString("color"));
            car.setMake(resultSet.getString("manufacturer"));
            car.setEngineSize(resultSet.getInt("engineSize"));
            car.setTransmission(resultSet.getString("transmission"));
            car.setFuelType(resultSet.getString("fuelType"));
            car.setSeatNumber(resultSet.getInt("seat"));
            car.setPrice(resultSet.getInt("price"));
            car.setQuantity(resultSet.getInt("quantity"));
            String imageName = resultSet.getString("imageUrl");
            System.out.println(TAG+ " queryAll()-> convertResultSetToCarInfo() imageName = "+ imageName);
            car.setImageName(imageName);
            loadCarImage(car,imageName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    private void loadCarImage(Car car , String imageName){
        InputStream inputStream = R.image.getInputStreamByName(imageName);
        if(inputStream != null){
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            try {
                byte[] bytes = bufferedInputStream.readAllBytes();
                car.setImageDataBytes(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

