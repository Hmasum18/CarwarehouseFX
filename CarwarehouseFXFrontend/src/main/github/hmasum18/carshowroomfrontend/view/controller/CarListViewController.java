package github.hmasum18.carshowroomfrontend.view.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.events.JFXDrawerEvent;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
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
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    public ListView<Car> mCarListView;
    public JFXHamburger mHam;
    public JFXDrawer mDrawer;
    //public JFXSpinner mLoadingSpinner;

    private boolean status = false;
    private String snackBarMessage = null;

    //data source
    private ViewModel mVB;
    //data
    private Meta meta;
    private List<Car> allCars;
    private List<Car> filteredCars;
    private UserInfo userInfo;


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

    /**
     * customize the views when the scene is created
     */
    public void customizeView() {
        setUpNavigationDrawer();

        Image image = new Image(R.image.getInputStreamByName("plus.png"), 18, 18, false, true);
        mAddFab.setGraphic(new ImageView(image));
        mAddFab.setText("");

        // mCarListView.getScene().getStylesheets().add(R.css.getPathByName("car_list_view.css"));
        System.out.println(TAG + "style sheets size:  " + mCarListView.getStylesheets().size());
        //init table sell
        //mCarListView.setFocusTraversable(false);
        mCarListView.setCellFactory(carListView -> new CarCell());

        if (userInfo.getRole() == LoginInfo.Role.VIEWER) {
            mAddFab.setVisible(false);
        }
    }

    private void setUpNavigationDrawer(){
        //set up hamburger
        AnchorPane mDrawerPane;
        try {
            mDrawerPane = FXMLLoader.load(R.fxml.getURLByName("drawer_fxml.fxml"));
            mDrawer.setSidePane(mDrawerPane);
            for (Node node : mDrawerPane.getChildren()) {
                System.out.println(TAG+" mDrawerPane node: "+node.getAccessibleText());
                if(node.getAccessibleText()!=null){
                   bindDrawerViews(node);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(mHam);
        transition.setRate(-1);
        mHam.addEventHandler(MouseEvent.MOUSE_PRESSED,(e)->{
            transition.setRate(transition.getRate()*-1);
            transition.play();
            if(mDrawer.isClosed())
                mDrawer.open();
            else
                mDrawer.close();
        } );
    }

    private void bindDrawerViews(Node node){
        Label label;
        switch (node.getAccessibleText()){
            case "drawerLogoutBtn":
                node.addEventHandler(MouseEvent.MOUSE_CLICKED,(event)->{
                    System.out.println(TAG + " logging out user");
                    IntentFX intentFX = new IntentFX(mDrawer, "login_screen.fxml"
                            ,IntentFX.SLIDE_DOWN_TO_UP,IntentFX.ANIMATE_CURRENT);
                    try {
                        // Main.serverListener.stopServerListener();
                        mVB.setUserInfo(null);
                        //if(mDrawer.isOpened())
                           // mDrawer.close();
                        intentFX.startScene();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "drawerFullName":
                label = (Label) node;
                label.setText(userInfo.getFirstName()+" "+userInfo.getLastName());
                break;
            case "drawerUserName":
                label = (Label) node;
                label.setText("#"+userInfo.getUserName());
                break;
            case "drawerUserRole":
                label = (Label) node;
                label.setText("Role: "+userInfo.getRole().name());
                break;
        }

    }

    private void bindListeners() {
        if (userInfo.getRole() != LoginInfo.Role.VIEWER)
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

       /* mLogoutBtn.setOnAction(event -> {
            System.out.println(TAG + " logging out user");
            IntentFX intentFX = new IntentFX(mLogoutBtn, "login_screen.fxml");
            try {
                // Main.serverListener.stopServerListener();
                mVB.setUserInfo(null);
                intentFX.startNewScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/

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
                        filterCarListByMakeAndModel(newValue, mModelSearchTF.getText());
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
                        filterCarListByMakeAndModel(mCarMakeSearchTF.getText(), newValue);
                        mCarListView.getItems().setAll(filteredCars);
                    }
                });

            }
        });
    }

    @Override
    public void onObjectReceived(Object object, Meta meta) {
        this.meta = meta;
        System.out.println(TAG + " inside onObjectReceived()");
        if (meta.getStatus() == Meta.Status.LOCAL) {
            handleLocalDataChange(object, meta);
        } else {
            if (object instanceof List) {
                handleCarListChange((List<Car>) object);
            } else if (object instanceof Car) { //for update , delete, insert
                handleDataManipulation(object);
            } else if (object == null) {
                System.out.println(TAG + "onObjectReceive(): received object is null");
                if (meta.getContentType() == Meta.ContentType.CAR)
                    handleDataManipulation(null);
            } else {
                System.out.println(TAG + " error fetching data.");
            }
        }
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

        allCars = carList;
        mVB.storeCarList(carList);

        Platform.runLater(() -> {
            System.out.println(TAG + "updating UI");
            //set the data to listView to show them

            updateCarListUI();
            status = true;
            snackBarMessage = "Login Successful";
            System.out.println(TAG + " UI updated");
        });

    }

    private void updateCarListUI() {
        filteredCars = new ArrayList<>();
        if (!mRegiSearchTF.getText().equals(""))
            filterCarListByRegistration(mRegiSearchTF.getText());
        else
            filterCarListByMakeAndModel(mCarMakeSearchTF.getText(), mModelSearchTF.getText());
        mCarListView.getItems().setAll(filteredCars);

        if (snackBarMessage != null && userInfo.getRole() != LoginInfo.Role.VIEWER)
            SharedUIComponent.showSnackBar(mRootPane, status, snackBarMessage);
        else
            System.out.println(TAG + "handleCarCreation(): there is some error");
    }

    private void handleLocalDataChange(Object object, Meta meta) {
        if (object instanceof Car) {
            Car car = (Car) object;
            switch (meta.getRequestType()) {
                case CREATE:
                    //a car was added in server
                    allCars.add(car);
                    break;
                case UPDATE:
                    for (int i = 0; i < allCars.size(); i++) {
                        if (allCars.get(i).getRegistration().equals(car.getRegistration())) {
                            allCars.set(i, car);
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
            Platform.runLater(() -> {
                mVB.storeCarList(allCars);

                //mCarListView.getItems().setAll(allCars);
                filteredCars = new ArrayList<>();
                filterCarListByMakeAndModel(mCarMakeSearchTF.getText(), mModelSearchTF.getText());
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

        Platform.runLater(this::updateCarListUI);
    }

    private void handleCarCreation(Car car) {
        System.out.println(TAG + "handleCarCreation(): handling car delete.");
        status = false;
        snackBarMessage = null;
        if (meta.getStatus() == Meta.Status.OK) {
            //this client made the car so data is sent null
            if (car == null) {
                status = true;
                snackBarMessage = "Car created successfully.";
            } else {
                //a car was added in server
                allCars.add(car);
                status = true;
                snackBarMessage = car.getMake() + " " + car.getModel() + " was deleted from server";
            }
        } else {
            if (car == null) {
                status = false;
                snackBarMessage = "Failed car deletion.";
            }
        }

      /*  if(snackBarMessage!=null)
            SharedUIComponent.showSnackBar(mRootPane,status,snackBarMessage);
        else
            System.out.println(TAG+"handleCarCreation(): there is some error");*/
    }

    private void handleUpdateCar(Car car) {
        System.out.println(TAG + "handleUpdateCar(): handling car update.");
        status = false;
        snackBarMessage = null;
        if (meta.getStatus() == Meta.Status.OK) {
            //this client made the car so data is sent null
            if (car == null) {
                status = true;
                snackBarMessage = "Car updated successfully.";
            } else {
                //other client updated
                for (int i = 0; i < allCars.size(); i++) {
                    if (allCars.get(i).getRegistration().equals(car.getRegistration())) {
                        allCars.set(i, car);
                        break;
                    }
                }
                status = true;
                snackBarMessage = car.getMake() + " " + car.getModel() + " was updated in server";
            }
        } else {
            if (car == null) {
                status = false;
                snackBarMessage = "Car was not updated.";
            }
        }
        /*if(snackBarMessage!=null)
            SharedUIComponent.showSnackBar(mRootPane,status,snackBarMessage);*/
    }

    private void handleDeleteCar(Car car) {
        System.out.println(TAG + "handleDeleteCar(): handling car delete.");
        status = false;
        snackBarMessage = null;
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
                status = true;
                if (deletedCar != null)
                    snackBarMessage = deletedCar.getMake() + " " + deletedCar.getModel() + " was deleted from server";
                else
                    snackBarMessage = deletedCar.getMake() + " " + deletedCar.getModel() + " was already removed.";
            }
        } else {
            if (car == null) {
                status = false;
                snackBarMessage = "Failed car deletion.";
            }

        }

        /*if(snackBarMessage!=null)
            SharedUIComponent.showSnackBar(mRootPane,status,snackBarMessage);
        else
            System.out.println(TAG+"handleDeleteCar(): there is some error");*/
    }

    private void filterCarListByMakeAndModel(String carMake, String carModel) {
        mRegiSearchTF.setText("");
        carMake = carMake.toLowerCase();
        carModel = carModel.toLowerCase();
        for (Car c : allCars) {
            String cMake = c.getMake().toLowerCase();
            String cModel = c.getModel().toLowerCase();
            if (cMake.contains(carMake) && cModel.contains(carModel))
                filteredCars.add(c);
        }
    }

    private void filterCarListByRegistration(String regi) {
        mCarMakeSearchTF.setText("");
        mModelSearchTF.setText("");
        regi = regi.toLowerCase();
        for (Car c : allCars) {
            String cRegi = c.getRegistration().toLowerCase();
            //  System.out.println(TAG+cRegi);
            // System.out.println(TAG+" regi: "+ regi);
            if (cRegi.contains(regi))
                filteredCars.add(c);
        }
    }

}
