/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroombackend.server;

import github.hmasum18.carshowroombackend.controller.CarController;
import github.hmasum18.carshowroombackend.controller.UserController;
import github.hmasum18.carshowroombackend.model.*;

import java.util.List;

/**
 * This class analyze a client request
 * then serve the client accordingly
 * this class also notify other client is any data is manipulated(created,updated,deleted)
 */
public class RequestManger {
    public static final String TAG = "RequestManger->";

    //database variables
    //controller to get user data from database
    private final UserController userController;
    //controller to get car data from database
    private final CarController carController;

    //client variables
    //client that send the request
    private final ClientHolder clientHolder;
    //request that has been received from client
    private final RequestParser requestParser;
    //response that is to be sent to client
    private final ResponseBuilder responseBuilder;

    public RequestManger(ClientHolder clientHolder,String meta, String data) {
        this.userController = new UserController();
        this.carController = new CarController();

        this.clientHolder = clientHolder;
        this.requestParser = new RequestParser(meta, data);
        this.responseBuilder = new ResponseBuilder();
    }

    //entry (only public method)
    /**
     * check is the user data is valid
     * and check what kind data user has requested for
     * then handle the request accordingly
     */
    public void manageRequest() {
        System.out.println(TAG + " manageRequest(): building responseBuilder");
        Meta meta = requestParser.getMeta();
        if (requestParser.isDataIsValid())
            switch (meta.getContentType()) {
                case CAR:
                    handleCarDataManipulation();
                    break;
                case CAR_LIST:
                    if (meta.getRequestType() == Meta.RequestType.GET)
                        handleCarListRequest();
                    break;
                case USER_INFO:
                    //will be done later
                    break;
                case LOGIN_INFO:
                    System.out.println(TAG + " checking if the received login data is valid");
                    handleUserLogin();
                    break;
                case ALL_CLIENT_INFO:
                    UserInfo userInfo = ClientManager.getInstance().getUserInfo(clientHolder.getClientName());
                    if(userInfo.getRole() == LoginInfo.Role.ADMIN){
                        if(meta.getRequestType()== Meta.RequestType.GET){
                            ResponseBuilder responseBuilder = new ResponseBuilder();
                            //responseBuilder.setData(ClientManager.getInstance().getAllAuthenticatedClientInfo());
                        }
                    }else{
                        System.out.println(TAG+clientHolder.getClientName()+ " is not an admin to send the client info list");
                    }
                    break;
            }
        else {
            System.out.println(TAG + " received data is not valid");
        }
    }

    /**
     * authenticate a user while login
     */
    private void handleUserLogin() {
        LoginInfo loginInfo = requestParser.getLoginInfo();
        System.out.println(TAG + " handleUserLogin(): " + loginInfo);
        try {
            UserInfo userInfo = userController.login(loginInfo);
            System.out.println(TAG + " handleUserLogin(): userInfo = " + userInfo);
            if (userInfo == null) {
                throw new NullPointerException(" user doesn't exist ");
            }
            //build the response
            responseBuilder.setData(userInfo);
            System.out.println(TAG + " handleUserLogin(): userInfoJson = " + responseBuilder.getData());
            Meta meta = new Meta(Meta.Status.OK, Meta.ContentType.USER_INFO,
                     responseBuilder.getData().getBytes().length, System.currentTimeMillis());
            responseBuilder.setMeta(meta);
            //send it to client
            clientHolder.sendResponse(responseBuilder);

            //add client info to client Manager for admin
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setClientIpPort(clientHolder.getClientName());
            clientInfo.setUserInfo(userInfo);
            ClientManager.getInstance().addClientInfo(clientHolder.getClientName(),clientInfo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + " handleUserLogin() :  " + "user doesn't exist");
            sendErrorResponse(Meta.Status.NOT_FOUND);
        }
    }

    /**
     * send the error to client without any data
     * @param status is error status
     */
    private void sendErrorResponse(Meta.Status status){
        System.out.println(TAG+ " sendErrorResponse(): sending error response to client ");
        //data will be string "null"
        Meta meta = new Meta(status, requestParser.getMeta().getContentType()
                , 0L, System.currentTimeMillis());
        responseBuilder.setMeta(meta);
        clientHolder.sendResponse(responseBuilder);
    }


    /**
     * send the car list to a client
     */
    private void handleCarListRequest() {
        try {
            List<Car> carList = carController.getAllCars();
            if (carList == null) {
                System.out.println(TAG + " handleUserLogin() :  " + "carlList doesn't exist");
                throw new NullPointerException("carList doesn't exist");
            }
            //building response
            responseBuilder.setData(carList);
            Meta meta = new Meta(Meta.Status.OK, Meta.ContentType.CAR_LIST,
                    responseBuilder.getData().getBytes().length, System.currentTimeMillis());
            responseBuilder.setMeta(meta);
            //send it to client
            clientHolder.sendResponse(responseBuilder);
            System.out.println(TAG + " car list data sent");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(Meta.Status.NOT_FOUND);
        }
    }


    /**
     * this method handle creation of car data
     * also update and deletion of car
     */
    private void handleCarDataManipulation() {
        System.out.println(TAG+" handleCarDataManipulation() handling data manipulation request from <= "+clientHolder.getClientName());
        Meta meta = requestParser.getMeta();
        switch (meta.getRequestType()) {
            case CREATE:
                addNewCar();
                break;
            case UPDATE:
                System.out.println(TAG+"handleCarDataManipulation(): user requested for car update");
                updateCar();
                break;
            case DELETE:
                deleteCar();
                break;
        }
    }

    private void addNewCar() {
        try {
            System.out.println(TAG+"addNewCar(): adding new car to database requested by <= "+clientHolder.getClientName());
            if(carController.addCar(requestParser.getCar())){
                notifyClients();
            }else{
                sendErrorResponse(Meta.Status.FAILED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCar() {
        //add update logic
        try {
            //update database
            System.out.println(TAG+"updateCar(): updating car to database requested by <= "+clientHolder.getClientName());
            if(carController.updateCar(requestParser.getCar()))
                notifyClients();
            else
                sendErrorResponse(Meta.Status.FAILED);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCar() {
        try {
            //delete from data base
            System.out.println(TAG+"deleteCar(): deleting car from database requested by <= "+clientHolder.getClientName());
            if(carController.deleteByCarReg(requestParser.getCar().getRegistration()) )
                notifyClients();
            else
                sendErrorResponse(Meta.Status.FAILED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * send a response to client who requested ( data is string "null")
     * then notify the changes to all the client
     */
    private void notifyClients() {
        Meta requestMeta = requestParser.getMeta();

        //send response back to client
        System.out.println(TAG+" notifyClients() : sending response back to requested client");;
        Meta selfMeta = new Meta(Meta.Status.OK,requestMeta.getRequestType(), Meta.ContentType.CAR,
                0, System.currentTimeMillis());
        responseBuilder.setMeta(selfMeta);
        clientHolder.sendResponse(responseBuilder);

        //notify other client
        System.out.println(TAG+" notifyClients() : notifying change to all other clients");;
        ResponseBuilder notifyingResponse = new ResponseBuilder();
        Meta notifyMeta = new Meta(Meta.Status.OK,requestMeta.getRequestType(),requestMeta.getContentType()
                ,requestMeta.getContentLength(),System.currentTimeMillis());
        notifyingResponse.setMeta(notifyMeta);
        notifyingResponse.setData(requestParser.getDataAsString());
        ClientManager.getInstance().notifyAllOtherClient(notifyingResponse, clientHolder.getClientName());
    }


}
