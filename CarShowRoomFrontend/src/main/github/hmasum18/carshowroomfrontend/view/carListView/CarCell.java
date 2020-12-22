
/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.view.carListView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.model.LoginInfo;
import github.hmasum18.carshowroomfrontend.model.UserInfo;
import github.hmasum18.carshowroomfrontend.view.controller.CarListViewController;
import github.hmasum18.carshowroomfrontend.view.controller.ObjectListenable;
import github.hmasum18.carshowroomfrontend.view.intentFX.SceneManager;
import github.hmasum18.carshowroomfrontend.view.util.SharedUIComponent;
import github.hmasum18.carshowroomfrontend.viewModel.ViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import res.R;

import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CarCell extends ListCell<Car> {
    public static final String TAG = "CarCell->";
    private Car car;

    @FXML // fx:id="mPane"
    private AnchorPane mPane; // Value injected by FXMLLoader

    @FXML // fx:id="mCarIV"
    private ImageView mCarIV; // Value injected by FXMLLoader

    @FXML // fx:id="mNameLbl"
    private Label mNameLbl; // Value injected by FXMLLoader

    @FXML
    private Label mRegistrationTF;

    @FXML // fx:id="mEditIV"
    private ImageView mEditIV; // Value injected by FXMLLoader

    @FXML // fx:id="mDeleteIV"
    private ImageView mDeleteIV; // Value injected by FXMLLoader

    @FXML // fx:id="mYearMadeLbl"
    private Label mYearMadeLbl; // Value injected by FXMLLoader

    @FXML // fx:id="mEngineSizeLbl"
    private Label mEngineSizeLbl; // Value injected by FXMLLoader

    @FXML // fx:id="mTransmissionLbl"
    private Label mTransmissionLbl; // Value injected by FXMLLoader

    @FXML // fx:id="mFuelLbl"
    private Label mFuelLbl; // Value injected by FXMLLoader

    @FXML // fx:id="mSeatLbl"
    private Label mSeatLbl; // Value injected by FXMLLoader

    @FXML // fx:id="mColorLbl"
    private Label mColorLbl; // Value injected by FXMLLoader

    @FXML // fx:id="mPriceLbl"
    private Label mPriceLbl; // Value injected by FXMLLoader

    @FXML // fx:id="mPaymentLbl"
    private Label mPaymentLbl; // Value injected by FXMLLoader

    @FXML // fx:id="mInStockLbl"
    private Label mInStockLbl; // Value injected by FXMLLoader

    @FXML
    private JFXButton mCartBtn;


    //data
    private UserInfo userInfo;

    public CarCell() {
        try {
            userInfo = ViewModel.getInstance().getUserInfo();
            String fxmlFileName = userInfo.getRole() == LoginInfo.Role.VIEWER? "car_card.fxml":"car_card_editable.fxml";

            FXMLLoader loader = new FXMLLoader(R.fxml.getURLByName(fxmlFileName));
            //loader.setRoot(this);
            loader.setController(this);
            loader.load();
            //getStyleClass().add(R.css.getByName("shared.css").toExternalForm());
            bindListeners();
            // System.out.println(TAG+fxmlFileName +"loaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Background createBackground(String colorCode, double cornerRadius) {
        CornerRadii cornerRadii = new CornerRadii(cornerRadius);
        Insets insets = new Insets(5, 5, 5, 5);
        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf(colorCode), cornerRadii,/*Insets.EMPTY*/insets);
        return new Background(backgroundFill);
    }

    private void bindListeners() {
        if(userInfo.getRole() != LoginInfo.Role.VIEWER){
            addRippler(mEditIV, "#3498db", 3, 0, 5, 0);
            mEditIV.setOnMouseClicked(mouseEvent -> {
                System.out.println(TAG + " edit button is clicked.");
                try {
                    makePopUpStage("car_card_edit.fxml", car, "Edit");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            addRippler(mDeleteIV, "#EE384B", 2, 0, 35, 0);
            mDeleteIV.setOnMouseClicked(mouseEvent -> {
                System.out.println(TAG + " delete button is clicked.");
                try {
                    makePopUpStage("delete_car.fxml", car, "Delete");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }else{
            mCartBtn.setOnAction(event -> {
                System.out.println(TAG+" add to cart button clicked.");
                if(car.getQuantity()>0){
                    car.setQuantity(car.getQuantity()-1);
                    SharedUIComponent.showSnackBar(mPane,true,"Car was added to your cart");
                    //update locally
                    ObjectListenable objectListenable = SceneManager.getInstance().getCurrentFXMLLoader().getController();
                    Meta meta = new Meta(Meta.Status.LOCAL, Meta.RequestType.UPDATE, Meta.ContentType.CAR,
                            50, System.currentTimeMillis());
                    objectListenable.onObjectReceived(car, meta);
                    //update in server
                    ViewModel.getInstance().submitCar(car, Meta.RequestType.UPDATE);
                }
                else
                    SharedUIComponent.showSnackBar(mPane,false, "Car is out of stock");

            });
        }

    }

    @Override
    protected void updateItem(Car car, boolean empty) {
        super.updateItem(car, empty);
        if (empty) {
            //System.out.println(TAG+" updateItem : car is empty");
            super.setText(null);
            super.setGraphic(null);
            super.setBackground(null);
        } else {
            //System.out.println(TAG+" updateItem : car is = "+car);
            // mTestLabel.setText(car.toString());
            super.setStyle("-fx-padding: 5px");
            this.car = car;
            updateUI(car);
            super.setGraphic(mPane);
            super.setBackground(createBackground("#fff59d", 10));
        }
    }

    private void updateUI(Car car) {

        if(userInfo.getRole()== LoginInfo.Role.VIEWER){
            mRegistrationTF.setText(car.getRegistration());
        }

        if (car.getImageDataBytes() != null) {
            Image image = new Image(new ByteArrayInputStream(car.getImageDataBytes())/*,220,230,false,true*/);
            mCarIV.setImage(image);
        } else {
            System.out.println(TAG + "updateUI(): car image was not sent from the server");
        }

        String carName = car.getMake() + " " + car.getModel();
        mNameLbl.setText(carName);
        mYearMadeLbl.setText(String.valueOf(car.getYear()));
        mEngineSizeLbl.setText(car.getEngineSize() + "cc");
        mTransmissionLbl.setText(car.getTransmission());
        mFuelLbl.setText(car.getFuelType());
        mSeatLbl.setText(String.valueOf(car.getSeatNumber()));
        mColorLbl.setText(car.getColor());
        mPriceLbl.setText(car.getPrice() + "$");
        mPaymentLbl.setText("cash/card");
        mInStockLbl.setText(String.valueOf(car.getQuantity()));
    }

    private void makePopUpStage(String fxml, Object data, String stageName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(R.fxml.getURLByName(fxml));
        fxmlLoader.load();
        ObjectListenable controller = fxmlLoader.getController();
        controller.onObjectReceived(data, null);

        AnchorPane anchorPane = fxmlLoader.getRoot();
        Scene scene = new Scene(anchorPane);

        CarListViewController.popUpStage.setTitle(stageName + " " + car.getMake() + " " + car.getModel());
        CarListViewController.popUpStage.setScene(scene);
        CarListViewController.popUpStage.show();
    }

    private void addRippler(Node node, String colorCode, double top, double bottom, double right, double left) {
        AnchorPane anchorPane = (AnchorPane) node.getParent();
        JFXRippler rippler = new JFXRippler(node);
        AnchorPane.setRightAnchor(rippler, right);
        AnchorPane.setTopAnchor(rippler, top);
        rippler.setRipplerFill(Paint.valueOf(colorCode));
        anchorPane.getChildren().add(rippler);
    }
}
