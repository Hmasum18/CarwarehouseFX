/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.view.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.view.intentFX.SceneManager;
import github.hmasum18.carshowroomfrontend.view.util.SharedUIComponent;
import github.hmasum18.carshowroomfrontend.viewModel.ViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import res.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CarDataManipulationController implements Initializable, ObjectListenable {
    public static final String TAG = "CarDataManipulationController->";
    //views
    @FXML // fx:id="mPane"
    private AnchorPane mPane; // Value injected by FXMLLoader
    @FXML
    private JFXTextField mCarMakeTF, mCarModelTF, mCarRegTF, mYearMadeTF, mEngineSizeTF, mCarImagNameTF; // Value injected by FXMLLoader
    @FXML
    private JFXTextField mFuelTF, mSeatTF, mColorTF, mPriceTF, mStockTF; // Value injected by FXMLLoader
    @FXML
    private JFXComboBox<String> mTransmissionCB; // Value injected by FXMLLoader
    @FXML
    private JFXComboBox<String> mPaymentCB;
    @FXML
    private JFXButton mSubmitBtn, mCarImageChooseBtn; // Value injected by FXMLLoader
    @FXML
    private ImageView mCarIV; // Value injected by FXMLLoader

    //data
    private ViewModel mVB;
    private Car car;
    private String lastImageFolder;
    private String selectedImageName;
    private Meta.RequestType requestType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lastImageFolder = "C:\\Users\\Hasan Masum\\Desktop\\";
        mVB = ViewModel.getInstance();
        if (car == null) {
            requestType = Meta.RequestType.CREATE;
            car = new Car();
        }
        customizeView();
        bindListeners();
    }


    private void customizeView() {
        mTransmissionCB.getItems().addAll("Auto", "Manual");
        mPaymentCB.getItems().addAll("Cash", "Card", "Cash/Card");
        addValidator();
    }

    private void addValidator() {
        mCarMakeTF.getValidators().add(getTextValidator("Empty"));
        mCarModelTF.getValidators().add(getTextValidator("Empty"));

        if (requestType == Meta.RequestType.CREATE)
            mCarRegTF.getValidators().add(getTextValidator("Empty"));

        mYearMadeTF.getValidators().add(getNumberValidator("Number only"));
        mYearMadeTF.getValidators().add(getTextValidator("Empty"));

        mEngineSizeTF.getValidators().add(getNumberValidator("Number only")); //engine size a number
        mEngineSizeTF.getValidators().add(getTextValidator("Empty"));

        mTransmissionCB.getValidators().add(getTextValidator("Select a transmission type"));
        mFuelTF.getValidators().add(getTextValidator("Empty."));

        mSeatTF.getValidators().add(getNumberValidator("Number only")); //seat a number
        mSeatTF.getValidators().add(getTextValidator("Empty"));

        mColorTF.getValidators().add(getTextValidator("Empty"));

        mPriceTF.getValidators().add(getNumberValidator("Number only"));
        mPriceTF.getValidators().add(getTextValidator("Empty"));

        mPaymentCB.getValidators().add(getTextValidator("Select a transmission method"));

        mStockTF.getValidators().add(getNumberValidator("Number only")); //in stock a number
        mStockTF.getValidators().add(getTextValidator("Empty"));

        mCarImagNameTF.getValidators().add(getTextValidator("Choose an image"));
    }

    private NumberValidator getNumberValidator(String message) {
        NumberValidator numberValidator = new NumberValidator();
        ImageView icon = new ImageView(new Image(R.image.getInputStreamByName("warning.png")));
        icon.setFitWidth(15);
        icon.setFitHeight(15);
        numberValidator.setIcon(icon);
        numberValidator.setMessage(message);
        return numberValidator;
    }

    private RequiredFieldValidator getTextValidator(String message) {
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
        ImageView icon = new ImageView(new Image(R.image.getInputStreamByName("warning.png")));
        icon.setFitWidth(15);
        icon.setFitHeight(15);
        requiredFieldValidator.setIcon(icon);
        requiredFieldValidator.setMessage(message);
        return requiredFieldValidator;
    }


    private void initViews() {
        if (car != null) {
            requestType = Meta.RequestType.UPDATE;
            //init text fields
            // String carImageName = car.getImageName();
            // mCarImagNameTF.setText(carImageName.substring(0,carImageName.lastIndexOf('.')));

            mCarMakeTF.setText(car.getMake());
            mCarModelTF.setText(car.getModel());
            mCarRegTF.setText(car.getRegistration());
            mCarRegTF.setDisable(true);
            mYearMadeTF.setText(String.valueOf(car.getYear()));
            mEngineSizeTF.setText(String.valueOf(car.getEngineSize()));
            mFuelTF.setText(car.getFuelType());
            mColorTF.setText(car.getColor());
            mSeatTF.setText(String.valueOf(car.getSeatNumber()));
            mPriceTF.setText(String.valueOf(car.getPrice()));
            mStockTF.setText(String.valueOf(car.getQuantity()));
            mCarImagNameTF.setText(car.getImageName());

            //init comboBox
            mTransmissionCB.getSelectionModel().select(car.getTransmission());
            mPaymentCB.setValue("Cash"); //will be changed later

            //init imageView
            if (car.getImageDataBytes() != null)
                mCarIV.setImage(new Image(new ByteArrayInputStream(car.getImageDataBytes())));
        } else {
            requestType = Meta.RequestType.CREATE;
        }
    }

    private void bindListeners() {
        mCarImageChooseBtn.setOnAction(event -> {
            //choose an image from storage
            chooseImageFromFileChooser();
        });

        mSubmitBtn.setOnAction(event -> {
            //submit the data to server
            if(isDataIsValid()){
                getDataFromViews();
                if(isCarExist()&&requestType == Meta.RequestType.CREATE){
                    SharedUIComponent.showSnackBar(mPane,false,"Car with same registration already exist.");
                }else{
                    System.out.println(TAG + " submitting car data");
                    mVB.submitCar(car, requestType);
                    //update locally
                    ObjectListenable objectListenable = SceneManager.getInstance().getCurrentFXMLLoader().getController();
                    Meta meta = new Meta(Meta.Status.LOCAL, requestType, Meta.ContentType.CAR,
                            50, System.currentTimeMillis());
                    objectListenable.onObjectReceived(car, meta);
                    CarListViewController.popUpStage.close();
                }

            }else{
                System.out.println(TAG+" car data is not valid for submission");
            }
        });

        bindPropertyListeners();

        bindFocusPropertyListeners();
    }

    private boolean isCarExist() {
        System.out.println(TAG+" isExist ");
        boolean isExist = false;
        for (int i = 0; i < mVB.getStoredCarList().size(); i++) {
            System.out.println(TAG+" isCarExist: "+ mVB.getStoredCarList().get(i).getRegistration());
            System.out.println(TAG+" isCarExist: "+this.car.getRegistration());
            if(mVB.getStoredCarList().get(i).getRegistration().equals(this.car.getRegistration())){
                System.out.println(TAG+" isCarExist(): registration matches with "+car.getRegistration());
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    private void bindFocusPropertyListeners() {
        mYearMadeTF.focusedProperty().addListener((o,oldValue,newValue)->{
            if(!newValue)
                mYearMadeTF.validate();
        });

        mEngineSizeTF.focusedProperty().addListener((o,oldValue,newValue)->{
            if(!newValue)
                mEngineSizeTF.validate();
        });

        mSeatTF.focusedProperty().addListener((o,oldValue,newValue)->{
            if(!newValue)
                mSeatTF.validate();
        });

        mPriceTF.focusedProperty().addListener((o,oldValue,newValue)->{
            if(!newValue)
                mPriceTF.validate();
        });

        mStockTF.focusedProperty().addListener((o,oldValue,newValue)->{
            if(!newValue)
                mStockTF.validate();
        });

    }

    private void bindPropertyListeners() {
        textFieldPropertyListeners(mCarMakeTF);
        textFieldPropertyListeners(mCarModelTF);

        if (requestType == Meta.RequestType.CREATE)
            textFieldPropertyListeners(mCarRegTF);

        textFieldPropertyListeners(mYearMadeTF);
        textFieldPropertyListeners(mEngineSizeTF);

        comboBoxValuePropertyListener(mTransmissionCB);

        textFieldPropertyListeners(mFuelTF);
        textFieldPropertyListeners(mSeatTF);
        textFieldPropertyListeners(mColorTF);
        textFieldPropertyListeners(mPriceTF);

        comboBoxValuePropertyListener(mPaymentCB);
        textFieldPropertyListeners(mStockTF);
        textFieldPropertyListeners(mCarImagNameTF);
    }

    private void textFieldPropertyListeners(JFXTextField textField) {
        textField.textProperty().addListener((o,oldValue,newValue)->{
            if(!newValue.isBlank())
                textField.validate();
        });
    }

    private void comboBoxValuePropertyListener(JFXComboBox<String> comboBox) {
        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue != null) {
                    comboBox.validate();
                }
            }
        });
    }

    private void getDataFromViews() {
        car.setRegistration(mCarRegTF.getText());
        car.setModel(mCarModelTF.getText());
        car.setYear(Integer.parseInt(mYearMadeTF.getText()));
        car.setColor(mColorTF.getText());
        car.setMake(mCarMakeTF.getText());
        car.setEngineSize(Integer.parseInt(mEngineSizeTF.getText()));
        car.setTransmission(mTransmissionCB.getValue());
        car.setFuelType(mFuelTF.getText());
        car.setSeatNumber(Integer.parseInt(mSeatTF.getText()));
        car.setQuantity(Integer.parseInt(mStockTF.getText()));
        car.setPrice(Integer.parseInt(mPriceTF.getText()));
        try {
            car.setImageName(getCarImageName());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + " error naming the image");
        }

        System.out.println(car);
        ///car image byte is set already
    }

    private String getCarImageName() {
        String name = car.getMake() + " " + car.getModel();
        List<String> nameParts = Arrays.asList(name.split(" "));
        System.out.println(TAG + "getCarImageName(): " + nameParts);
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < nameParts.size() - 1; i++) {
            nameBuilder.append(nameParts.get(i)).append("-");
        }
        nameBuilder.append(nameParts.get(nameParts.size() - 1));
        int idx = selectedImageName.lastIndexOf(".");
        nameBuilder.append(".").append(selectedImageName.substring(idx + 1));

        return nameBuilder.toString();
    }

    private boolean isDataIsValid() {
        if(!validateTF(mCarMakeTF))
            return false;
        if(!validateTF(mCarModelTF))
            return false;
        if(!validateTF(mCarRegTF)&&requestType== Meta.RequestType.CREATE)
            return false;
        if(!validateTF(mYearMadeTF))
            return false;
        if(!validateTF(mEngineSizeTF))
            return false;
        if(!validateCB(mTransmissionCB))
            return false;
        if(!validateTF(mFuelTF))
            return false;
        if(!validateTF(mSeatTF))
            return false;
        if(!validateTF(mColorTF))
            return false;
        if(!validateTF(mPriceTF))
            return false;
        if(!validateCB(mPaymentCB))
            return false;
        if(!validateTF(mStockTF))
            return false;
        if(!validateTF(mCarImagNameTF))
            return false;


        return true;
    }

    private boolean validateTF(JFXTextField textField) {
        if (textField.getText().isBlank()) {
            textField.validate();
            return false;
        }
        return true;
    }

    private boolean validateCB(JFXComboBox<String> comboBox) {
        if(comboBox.getValue() == null){
            comboBox.validate();
            return false;
        }
        return true;
    }

    private void chooseImageFromFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(lastImageFolder));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(null);
        try {
            lastImageFolder = selectedFile.getAbsolutePath(); //save for later
            selectedImageName = selectedFile.getName();
            mCarImagNameTF.setText(selectedImageName);
            System.out.println(TAG + "chooseImageFromFileChooser(): " + lastImageFolder);
            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            byte[] bytes = fileInputStream.readAllBytes();
            System.out.println(TAG + " new image size : " + bytes.length);
            car.setImageDataBytes(bytes);
            mCarIV.setImage(new Image(new ByteArrayInputStream(bytes)));
            System.out.println(TAG + " user has selected image file from " + lastImageFolder);
        } catch (Exception e) {
            System.out.println(TAG + " user has not selected any image file");
            System.out.println(e);
            e.printStackTrace();
        }
    }


    @Override
    public void onObjectReceived(Object object, Meta meta) {

        if (object instanceof Car) {
            car = (Car) object;
            initViews();
        } else {
            System.out.println(TAG + "");
        }
    }
}
