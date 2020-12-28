/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.view.util;

import com.jfoenix.controls.JFXSnackbar;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import res.R;

import java.io.IOException;

public class SharedUIComponent {

    public static final String TAG = "SharedUIComponent->";

    public static void showSnackBar(Pane mRootPane, boolean status, String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Image image;
                String style;
                if (status) {
                    image = new Image(R.image.getInputStreamByName("checked.png"), 25, 25, true, true);
                    style = "-fx-background-color: #4caf50; -fx-background-radius:3px;";
                } else {
                    image = new Image(R.image.getInputStreamByName("warning_white.png"), 25, 25, true, true);
                    style = "-fx-background-color: #f44336; -fx-background-radius:3px;";
                }
                JFXSnackbar jfxSnackbar = new JFXSnackbar(mRootPane);
                try {
                    AnchorPane anchorPane = FXMLLoader.load(R.fxml.getURLByName("snakbar_toast.fxml"));
                    anchorPane.setStyle(style);
                    System.out.println(TAG + anchorPane.getChildren());
                    ImageView icon = (ImageView) anchorPane.getChildren().get(0);
                    icon.setImage(image);
                    Label label = (Label) anchorPane.getChildren().get(1);
                    label.setText(message);
                    JFXSnackbar.SnackbarEvent event = new JFXSnackbar.SnackbarEvent(anchorPane, Duration.seconds(1.5));
                    jfxSnackbar.enqueue(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
