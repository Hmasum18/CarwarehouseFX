package github.hmasum18.carshowroomfrontend.view.controller;

import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.model.CarImageHolder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import res.R;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable, ObjectListenable {
    @FXML
    public ImageView mImageTest;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*ObservableList<String> name = FXCollections.observableArrayList();
        name.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {

            }
        });*/

        new Thread(new Runnable() {
            @Override
            public void run() {

                Image image = null;
                try {
                    byte[] bytes = R.image.getByteArrayByName("me.jpg");
                    CarImageHolder carImageHolder = new CarImageHolder(bytes);
                    //image = new Image(new ByteArrayInputStream(R.image.getByteArrayByName("me.jpg")));
                    mImageTest.setImage(carImageHolder.getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onObjectReceived(Object object, Meta dataChangeInfo) {

    }
}
