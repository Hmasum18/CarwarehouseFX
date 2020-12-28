/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.model;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class CarImageHolder {

    private final byte[] imageData;

    public CarImageHolder(byte[] bytes){
        this.imageData = bytes;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public Image getImage(){
        return new Image(new ByteArrayInputStream(imageData));
    }

    @Override
    public String toString() {
        return "CarImageHolder{" +
                "imageData=" + Arrays.toString(imageData) +
                '}';
    }
}
