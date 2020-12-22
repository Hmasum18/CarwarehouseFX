package github.hmasum18.carshowroomfrontend.view.controller;

import com.jfoenix.controls.JFXButton;
import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.model.LoginInfo;
import github.hmasum18.carshowroomfrontend.model.UserInfo;
import github.hmasum18.carshowroomfrontend.view.carListView.CarCell;
import github.hmasum18.carshowroomfrontend.view.intentFX.IntentFX;
import github.hmasum18.carshowroomfrontend.view.intentFX.SceneManager;
import github.hmasum18.carshowroomfrontend.view.util.SharedUIComponent;
import github.hmasum18.carshowroomfrontend.viewModel.ViewModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import res.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class CarListViewController implements Initializable, ObjectListenable {
    public static final String TAG = "CarListViewController->";
    //views
    public static Stage popUpStage;
    public JFXButton mAddFab;
    public TextField mModelSearchTF;
    public TextField mCarMakeSearchTF;
    public TextField mRegiSearchTF;
    public AnchorPane mRootPane;
    public JFXButton mLogoutBtn;
    //  public JFXSpinner mLoadingSpinner;
    //data source
    private ViewModel mVB;
    //data
    private Meta meta;
    private List<Car> allCars;
    private List<Car> filteredCars;
    private UserInfo userInfo;

    //views
    @FXML
    public ListView<Car> mCarListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(TAG + " initialize(): ");
        popUpStage = new Stage();
        popUpStage.getIcons().add(new Image(R.image.getInputStreamByName("icon.png")));
        popUpStage.setResizable(false);

        System.out.println(TAG + " calling data from server");
        //fetch data from server
        mVB = ViewModel.getInstance();
        userInfo = mVB.getUserInfo();
        System.out.println(TAG + "initialize(): requesting for car data");
        mVB.getCarList(); //reqeust for all car data
        customizeView();
        bindListeners();
    }

    public void customizeView() {
        Image image = new Image(R.image.getInputStreamByName("plus.png"), 18, 18, false, true);
        mAddFab.setGraphic(new ImageView(image));
        mAddFab.setText("");

        // mCarListView.getScene().getStylesheets().add(R.css.getPathByName("car_list_view.css"));
        System.out.println(TAG + "style sheets size:  " + mCarListView.getStylesheets().size());
        //init table sell
        //mCarListView.setFocusTraversable(false);
        mCarListView.setCellFactory(carListView -> new CarCell());

        if(userInfo.getRole()== LoginInfo.Role.VIEWER)
        {
            mAddFab.setVisible(false);
        }
    }

    private void bindListeners() {
        if(userInfo.getRole()!= LoginInfo.Role.VIEWER)
        mAddFab.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(R.fxml.getURLByName("car_card_edit.fxml"));
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AnchorPane anchorPane = fxmlLoader.getRoot();
            Scene scene = new Scene(anchorPane);

            CarListViewController.popUpStage.setTitle("Add new car");
            CarListViewController.popUpStage.setScene(scene);
            CarListViewController.popUpStage.show();
        });

        mLogoutBtn.setOnAction(event -> {
            System.out.println(TAG+" logging out user");
            IntentFX intentFX = new IntentFX(mLogoutBtn,"login_screen.fxml");
            try {
               // Main.serverListener.stopServerListener();
                mVB.setUserInfo(null);
                intentFX.startNewScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mRegiSearchTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        filteredCars = new ArrayList<>();
                        filterCarListByRegistration(newValue);
                        mCarListView.getItems().setAll(filteredCars);
                    }
                });
            }
        });

        mCarMakeSearchTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        filteredCars = new ArrayList<>();
                        filterCarListByMakeAndModel(newValue,mModelSearchTF.getText());
                        mCarListView.getItems().setAll(filteredCars);
                    }
                });
            }
        });

        mModelSearchTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        filteredCars = new ArrayList<>();
                        filterCarListByMakeAndModel(mCarMakeSearchTF.getText(),newValue);
                        mCarListView.getItems().setAll(filteredCars);
                    }
                });

            }
        });
    }

    private void filterCarListByMakeAndModel(String carMake, String carModel){
        mRegiSearchTF.setText("");
        carMake = carMake.toLowerCase();
        carModel = carModel.toLowerCase();
        for (Car c : allCars) {
            String cMake = c.getMake().toLowerCase();
            String cModel = c.getModel().toLowerCase();
            if(cMake.contains(carMake) && cModel.contains(carModel))
                filteredCars.add(c);
        }
    }

    private void filterCarListByRegistration(String regi){
        mCarMakeSearchTF.setText("");
        mModelSearchTF.setText("");
        regi = regi.toLowerCase();
        for (Car c : allCars) {
            String cRegi = c.getRegistration().toLowerCase();
          //  System.out.println(TAG+cRegi);
           // System.out.println(TAG+" regi: "+ regi);
            if(cRegi.contains(regi))
                filteredCars.add(c);
        }
    }


    @Override
    public void onObjectReceived(Object object,Meta meta) {
        this.meta = meta;
        System.out.println(TAG + " inside onObjectReceived()");
        if(meta.getStatus()== Meta.Status.LOCAL){
            handleLocalDataChange(object,meta);
        }else{
            if (object instanceof List) {
                handleCarListChange((List<Car>) object);
            } else if (object instanceof Car) { //for update , delete, insert
                handleDataManipulation(object);
            } else if (object == null) {
                System.out.println(TAG + "onObjectReceive(): received object is null");
                if (meta.getContentType() == Meta.ContentType.CAR)
                    handleDataManipulation(null);
            }
            else {
                System.out.println(TAG + " error fetching data.");
            }
        }
    }

    private void handleLocalDataChange(Object object,Meta meta) {
        if(object instanceof Car){
            Car car = (Car) object;
            switch (meta.getRequestType()){
                case CREATE:
                    //a car was added in server
                    allCars.add(car);
                    break;
                case UPDATE:
                    for (int i = 0; i < allCars.size(); i++) {
                        if (allCars.get(i).getRegistration().equals(car.getRegistration())) {
                            allCars.set(i,car);
                            break;
                        }
                    }
                    break;
                case DELETE:
                    for (int i = 0; i < allCars.size(); i++) {
                        if (allCars.get(i).getRegistration().equals(car.getRegistration())) {
                            allCars.remove(i);
                            break;
                        }
                    }
                    break;
            }
            //update the listView cars
            Platform.runLater(()->{
                mVB.storeCarList(allCars);

                //mCarListView.getItems().setAll(allCars);
                filteredCars = new ArrayList<>();
                filterCarListByMakeAndModel(mCarMakeSearchTF.getText(),mModelSearchTF.getText());
                mCarListView.getItems().setAll(filteredCars);
            });
        }
    }

    private void handleDataManipulation(Object carData) {
        System.out.println(TAG + "handleDataManipulation(): handling car data manipulation");
        switch (meta.getRequestType()) {
            case CREATE:
                handleCarCreation((Car) carData);
                break;
            case UPDATE:
                handleUpdateCar((Car) carData);
                break;
            case DELETE:
                handleDeleteCar((Car) carData);
                break;
        }
    }

    private void handleCarCreation(Car car) {
        System.out.println(TAG + "handleCarCreation(): handling car delete.");
        boolean status = false;
        String snackBarMessage = null;
        if (meta.getStatus() == Meta.Status.OK) {
            //this client made the car so data is sent null
            if (car == null) {
                status = true;
                snackBarMessage = "Car created successfully.";
            } else {
                //a car was added in server
                allCars.add(car);
                //update the listView cars
                Platform.runLater(()->{
                    //mCarListView.getItems().setAll(allCars);
                    filteredCars = new ArrayList<>();
                    filterCarListByMakeAndModel(mCarMakeSearchTF.getText(),mModelSearchTF.getText());
                    mCarListView.getItems().setAll(filteredCars);
                });
                status = true;
                snackBarMessage = car.getMake()+" "+car.getModel()+" was deleted from server";
            }
        } else {
            if (car == null){
                status = false;
                snackBarMessage = "Failed car deletion.";
            }
        }

        if(snackBarMessage!=null)
            SharedUIComponent.showSnackBar(mRootPane,status,snackBarMessage);
        else
            System.out.println(TAG+"handleCarCreation(): there is some error");
    }

    private void handleDeleteCar(Car car) {
        System.out.println(TAG + "handleDeleteCar(): handling car delete.");
        boolean status = false;
        String snackBarMessage = null;
        if (meta.getStatus() == Meta.Status.OK) {
            //this client made the car so data is sent null
            if (car == null) {
                status = true;
                snackBarMessage = "Car deleted successfully.";
            } else {
                //other client updated
                Car deletedCar = null;
                for (int i = 0; i < allCars.size(); i++) {
                    if (allCars.get(i).getRegistration().equals(car.getRegistration())) {
                        deletedCar = allCars.get(i);
                        allCars.remove(i);
                        break;
                    }
                }
                //update the listView cars
                Platform.runLater(()->{
                   // mCarListView.getItems().setAll(allCars);
                    filteredCars = new ArrayList<>();
                    filterCarListByMakeAndModel(mCarMakeSearchTF.getText(),mModelSearchTF.getText());
                    mCarListView.getItems().setAll(filteredCars);
                });
                status = true;
                if(deletedCar!=null)
                    snackBarMessage = deletedCar.getMake()+" "+deletedCar.getModel()+" was deleted from server";
                else
                    snackBarMessage = deletedCar.getMake()+" "+deletedCar.getModel()+" was already removed.";
            }
        } else {
            if (car == null){
                status = false;
                snackBarMessage = "Failed car deletion.";
            }

        }

        if(snackBarMessage!=null)
            SharedUIComponent.showSnackBar(mRootPane,status,snackBarMessage);
        else
            System.out.println(TAG+"handleDeleteCar(): there is some error");
    }

    private void handleUpdateCar(Car car) {
        System.out.println(TAG + "handleUpdateCar(): handling car update.");
        boolean status = false;
        String snackBarMessage = null;
        if (meta.getStatus() == Meta.Status.OK) {
            //this client made the car so data is sent null
            if (car == null) {
                status = true;
                snackBarMessage = "Car updated successfully.";
            } else {
                //other client updated
                for (int i = 0; i < allCars.size(); i++) {
                    if (allCars.get(i).getRegistration().equals(car.getRegistration())) {
                        allCars.set(i,car);
                        break;
                    }
                }
                //update the listView car
                Platform.runLater(()->{
                    //mCarListView.getItems().setAll(allCars);
                    filteredCars = new ArrayList<>();
                    filterCarListByMakeAndModel(mCarMakeSearchTF.getText(),mModelSearchTF.getText());
                    mCarListView.getItems().setAll(filteredCars);
                });
                status = true;
                snackBarMessage = car.getMake()+" "+car.getModel()+" was updated in server";
            }
        } else {
            if (car == null){
                status = false;
                snackBarMessage = "Car was not updated.";
            }
        }
        if(snackBarMessage!=null)
            SharedUIComponent.showSnackBar(mRootPane,status,snackBarMessage);
    }


    private void handleCarListChange(List<Car> carList) {
        System.out.println(TAG + "carList data received : size of car list is " + carList.size());
        for (Car c :
                carList) {
            System.out.println(TAG + c);
        }
        System.out.println(TAG + " waiting for finishing scene transition");
        while (SceneManager.getInstance().isSceneTransitionAlive()) {
            System.out.print(".");
        }
        System.out.println();

        Platform.runLater(() -> {
            System.out.println(TAG + "updating UI");
            //set the data to listView to show them
           // mCarListView.getItems().setAll(carList);
            allCars = carList;
            mVB.storeCarList(carList);

            filteredCars = new ArrayList<>();
            filterCarListByMakeAndModel(mCarMakeSearchTF.getText(),mModelSearchTF.getText());
            mCarListView.getItems().setAll(filteredCars);
            // mLoadingSpinner.setVisible(false);
            // mLoadingSpinner.setDisable(true);
            System.out.println(TAG + " UI updated");
            SharedUIComponent.showSnackBar(mRootPane,true, "Login Successful");
        });

    }

    /*@Override
    public void onObjectReceived(Object object, DataChangeInfo dataChangeInfo) {
        switch (dataChangeInfo.getChangedDataType()){
            case CAR:
                handleCarDataManipulation((Car) object,dataChangeInfo);
                break;
            case CAR_LIST:
                handleCarListChange((List<Car>) object);
                break;
        }
    }

    private void handleCarDataManipulation(Car car,DataChangeInfo dataChangeInfo) {
        switch (dataChangeInfo.getDataChangeType()){
            case CREATE:
                if(dataChangeInfo.getChangeVerdict()==ChangeVerdict.SUCCESS){
                    if()
                }else{

                }
                break;
            case UPDATE:
                break;
            case DELETE:
                break;
        }
    }*/
}
