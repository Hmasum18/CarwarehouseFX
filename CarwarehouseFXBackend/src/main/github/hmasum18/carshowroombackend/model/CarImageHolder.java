/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroombackend.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class CarImageHolder {

    private final byte[] imageData;

    public CarImageHolder(byte[] bytes){
        this.imageData = bytes;
    }

    public CarImageHolder(ByteArrayInputStream byteArrayInputStream){
        this.imageData = byteArrayInputStream.readAllBytes();
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
