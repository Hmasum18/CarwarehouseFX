package github.hmasum18.carshowroomfrontend.view;

import github.hmasum18.carshowroomfrontend.carshowroom.ServerHolder;
import github.hmasum18.carshowroomfrontend.carshowroom.ServerListener;
import github.hmasum18.carshowroomfrontend.view.intentFX.IntentFX;
import github.hmasum18.carshowroomfrontend.viewModel.ViewModel;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import res.R;

import java.io.IOException;

public class Main extends Application {

    public static ServerListener serverListener;
    public static ServerHolder serverHolder;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(R.image.getInputStreamByName("icon.png")));
        try {
            serverHolder = new ServerHolder("127.0.0.1",3333);
            //start a new listener(a thread)
            serverListener = new ServerListener(serverHolder);
            serverListener.start();

            //init view model with server holder
            ViewModel viewModel = ViewModel.getInstance();
            viewModel.setServerHolder(serverHolder);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //IntentFX intentFX = new IntentFX(primaryStage,"car_list_view.fxml");
        IntentFX intentFX = new IntentFX(primaryStage,"login_screen.fxml");
        try {
            intentFX.startNewScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.show();
    }
}
