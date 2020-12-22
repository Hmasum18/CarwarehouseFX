package github.hmasum18.carshowroomfrontend.model;


import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class Car {
    public static final String TAG = "Car->";
    private String registration; //1
    private String model; //2
    private int year; //3
    private String color; //4
    private String make; //5
    private int engineSize; //6
    private String transmission; //7
    private String fuelType; //8
    private int seatNumber; //9
    private int quantity;//10
    private int price; //11
    private String imageName; //12
    private byte[] imageDataBytes;

    public Car() {
    }

    public Car(String registration, String model, int year,
               String color, String make, int engineSize,
               String transmission, String fuelType, int seatNumber,
               int quantity, int price, String imageName) {
        this.registration = registration;
        this.model = model;
        this.year = year;
        this.color = color;
        this.make = make;
        this.engineSize = engineSize;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.seatNumber = seatNumber;
        this.quantity = quantity;
        this.price = price;
        this.imageName = imageName;
    }

    public String getRegistration() {
        return registration;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getMake() {
        return make;
    }

    public int getEngineSize() {
        return engineSize;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageName() {
        return imageName;
    }

    public byte[] getImageDataBytes() {
        return imageDataBytes;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setEngineSize(int engineSize) {
        this.engineSize = engineSize;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageDataBytes(byte[] imageDataBytes) {
        this.imageDataBytes = imageDataBytes;
    }



    @Override
    public String toString() {
        return "Car{\n" +
                "   registration='" + registration + "\',\n" +
                "   model='" + model + "\',\n" +
                "   year=" + year + ",\n" +
                "   color='" + color + "\',\n" +
                "   make='" + make + "\',\n" +
                "   engineSize=" + engineSize + ",\n" +
                "   transmission='" + transmission + "\',\n" +
                "   fuelType='" + fuelType + "\',\n" +
                "   seatNumber=" + seatNumber + ",\n" +
                "   price=" + price + ",\n" +
                "   imageUrl='" + imageName + "',\n" +
               // "  imageData [" + imageDataBytes.length + "]\n" +
                '}';
    }
}
