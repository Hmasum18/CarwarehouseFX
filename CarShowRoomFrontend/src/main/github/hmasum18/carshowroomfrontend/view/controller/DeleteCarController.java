/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.view.controller;

import com.jfoenix.controls.JFXButton;
import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.view.intentFX.SceneManager;
import github.hmasum18.carshowroomfrontend.viewModel.ViewModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import res.R;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class DeleteCarController implements Initializable,ObjectListenable {

    public static final String TAG = "DeleteCarController->";
    public ImageView mCarIV;
    public Label mCarName;
    public JFXButton mKeepItBtn;
    public JFXButton mDeleteBtn;
    //views

    //data
    private Car car;
    private ViewModel mVB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(R.image.getInputStreamByName("delete_white.png"),13,13,true,false);
        mDeleteBtn.setGraphic(new ImageView(image));
        bindListeners();

        mVB = ViewModel.getInstance();
    }


    private void updateUI() {
        String carName = car.getMake()+" "+car.getModel();
        mCarName.setText(carName);
        if(car.getImageDataBytes()!=null){
            mCarIV.setImage(new Image(new ByteArrayInputStream(car.getImageDataBytes())));
        }
    }

    private void bindListeners() {
        mKeepItBtn.setOnAction(event -> {
            CarListViewController.popUpStage.close();
        });

        mDeleteBtn.setOnAction(event -> {
            System.out.println(TAG+" delete button clicked");
            mVB.removeCar(car.getRegistration());
            ObjectListenable objectListenable = SceneManager.getInstance().getCurrentFXMLLoader().getController();
            Meta meta = new Meta(Meta.Status.LOCAL, Meta.RequestType.DELETE, Meta.ContentType.CAR,
                    50,System.currentTimeMillis());
            objectListenable.onObjectReceived(car,meta);
            CarListViewController.popUpStage.close();
        });
    }

    @Override
    public void onObjectReceived(Object object, Meta meta) {
        if(object instanceof Car){
            car = (Car) object;
            updateUI();
        }else{
            System.out.println(TAG+" object received was not a car.");
        }
    }
}
