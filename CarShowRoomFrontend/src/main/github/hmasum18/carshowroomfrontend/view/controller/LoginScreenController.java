/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.view.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;

import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.model.LoginInfo;
import github.hmasum18.carshowroomfrontend.model.UserInfo;
import github.hmasum18.carshowroomfrontend.view.intentFX.IntentFX;
import github.hmasum18.carshowroomfrontend.view.util.SharedUIComponent;
import github.hmasum18.carshowroomfrontend.viewModel.ViewModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import res.R;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable, ObjectListenable {
    public static final String TAG = "LoginScreenController->";
    //views
    @FXML
    public JFXTextField mUsernameTF;
    @FXML
    public JFXPasswordField mPasswordTF;
    @FXML
    public JFXButton mLoginBTN;
    @FXML
    public JFXComboBox<String> mRoleCB;
    public JFXButton mViewerLoginBtn;

    //data sources
    private ViewModel mVB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mVB = ViewModel.getInstance();
        /*if(!Main.serverListener.isServerListenerAlive()){
            Main.serverListener = new ServerListener(Main.serverHolder);
        }*/

        initViews();
        bindListeners();
    }
    private void initViews() {
        //customize welcome level
        //mWelcomeLbl.setFont(R.font.getFontByName("BalooDa2-Bold.ttf",40));

        //define a validator
        RequiredFieldValidator validator = new RequiredFieldValidator();
        ImageView icon = new ImageView(new Image(R.image.getInputStreamByName("warning.png")));
        icon.setFitWidth(15);
        icon.setFitHeight(15);
        validator.setIcon(icon);
        validator.setMessage("This field can't be empty");

        //customize username text field
        mUsernameTF.setLabelFloat(true);
        mUsernameTF.setFocusColor(R.color.PRIMARY_COLOR.getColor());
        mUsernameTF.setUnFocusColor(R.color.SECONDARY_COLOR.getColor());
        mUsernameTF.getValidators().add(validator);


        //customize password text field
        mPasswordTF.setLabelFloat(true);
        mPasswordTF.setFocusColor(R.color.PRIMARY_COLOR.getColor());
        mPasswordTF.setUnFocusColor(R.color.SECONDARY_COLOR.getColor());
        mPasswordTF.getValidators().add(validator);

        mRoleCB.setLabelFloat(true);
        mRoleCB.setFocusColor(R.color.PRIMARY_COLOR.getColor());
        mRoleCB.setUnFocusColor(R.color.SECONDARY_COLOR.getColor());
        mRoleCB.getValidators().addAll(validator);
        mRoleCB.getItems().addAll("ADMIN", "MODERATOR");

        //customize login button
        mLoginBTN.getStyleClass().add("button-raised");
    }

    private void bindListeners() {
        mUsernameTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isBlank()){
                    //validate again to remove the previous error message
                    mUsernameTF.validate();
                }
            }
        });

        mPasswordTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(!newValue.isBlank()){
                    mPasswordTF.validate();
                }
            }
        });

        mRoleCB.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if(newValue!=null){
                    mRoleCB.validate();
                }
            }
        });

        mLoginBTN.setOnAction(event -> {
            String username = mUsernameTF.getText();
            String password = mPasswordTF.getText();
            String role = mRoleCB.getValue();
            System.out.println(role);
            if(isValid(username,password,role)){
                //send this data to serve
                LoginInfo loginInfo = new LoginInfo(username,password,LoginInfo.Role.valueOf(role));
                try {
                    mVB.login(loginInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        mViewerLoginBtn.setOnAction(event -> {
            LoginInfo loginInfo = new LoginInfo("viewer","viewer",LoginInfo.Role.VIEWER);
            try {
                mVB.login(loginInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    private boolean isValid(String username, String password,String role){
        if(username.isBlank()){
            mUsernameTF.getValidators().get(0).setMessage("username can't be empty");
            mUsernameTF.validate();
            return false;
        }
        else if(password.isBlank()){
            mPasswordTF.getValidators().get(0).setMessage("password can't be empty");
            mPasswordTF.validate();
            return false;
        }else if(role == null){
            RequiredFieldValidator validator = (RequiredFieldValidator) mRoleCB.getValidators().get(0);
            validator.setMessage("Please select a role");
            mRoleCB.validate();
            return false;
        }
        return true;
    }

    private void changeScreen(){
        System.out.println(TAG+"changeScreen()");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                IntentFX intentFX = new IntentFX(mLoginBTN,/*"home.fxml"*/"car_list_view.fxml",IntentFX.SLIDE_DOWN_TO_UP);
                try {
                    intentFX.startNewScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onObjectReceived(Object object, Meta meta) {
        if(object instanceof UserInfo){
            UserInfo userInfo = (UserInfo) object;
            ViewModel.getInstance().setUserInfo(userInfo);
            changeScreen();
        }else if(object==null){
            if(meta.getStatus()== Meta.Status.NOT_FOUND){
                SharedUIComponent.showSnackBar((Pane) mViewerLoginBtn.getParent(),false,"Login Failed.");
            }
        }else{
            if(meta.getStatus()== Meta.Status.NOT_FOUND){
                SharedUIComponent.showSnackBar((Pane) mViewerLoginBtn.getParent(),false,"Login Failed.");
            }
            System.out.println(TAG+" login failed ");
        }
    }
}
