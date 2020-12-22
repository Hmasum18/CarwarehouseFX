/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.carshowroom;

import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.model.UserInfo;
import github.hmasum18.carshowroomfrontend.repository.Repository;
import github.hmasum18.carshowroomfrontend.view.controller.LoginScreenController;
import github.hmasum18.carshowroomfrontend.view.controller.ObjectListenable;
import github.hmasum18.carshowroomfrontend.view.intentFX.SceneManager;
import github.hmasum18.carshowroomfrontend.viewModel.ViewModel;

import java.util.List;

public class ResponseManager {
    public static final String TAG = "ResponseManager->";
    private final ResponseParser responseParser;

    public ResponseManager(String meta,String data) {
        this.responseParser = new ResponseParser(meta,data);
    }

    /**
     * this is the only public method
     */
    public void manageResponse(){
        System.out.println(TAG+ " inside manageResponse()");
        Meta responseMeta = responseParser.getMeta();
        //currently active controller
        ObjectListenable controller = SceneManager.getInstance().getCurrentFXMLLoader().getController();
        switch (responseMeta.getContentType()){
            case LOGIN_INFO:
            case USER_INFO:
                System.out.println(TAG+"manageResponse(): notifying user_info");
                UserInfo userInfo = responseParser.getUserInfo();
                System.out.println(TAG+"manageResponse(): userInfo = "+ userInfo);
                if(controller!=null){
                    controller.onObjectReceived(userInfo,responseMeta);
                }else{
                    System.out.println(TAG+"manageResponse(): controller is null can't notify");
                }
                break;
            case CAR_LIST:
                List<Car> carList = responseParser.getCarList();
                if(carList!=null)
                    System.out.println(TAG+"manageResponse(): carListSize = "+ carList.size());
                else
                    System.out.println(TAG+"manageResponse(): carList is null");
                if(controller!=null){
                    System.out.println(TAG+"manageResponse(): notifying carList to controller");
                    controller.onObjectReceived(carList,responseMeta);
                    System.out.println(TAG+"manageResponse(): data sent to controller successfully");
                }
                else
                    System.out.println(TAG+"manageResponse(): controller is null can't notify");
                break;
            case CAR:
                if(controller!=null){
                    controller.onObjectReceived(responseParser.getCar(),responseMeta);
                }else{
                    System.out.println(TAG+"manageResponse(): controller is null can't notify");
                }
                break;
        }
    }
}
