package github.hmasum18.carshowroomfrontend.repository;

import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.carshowroom.RequestBuilder;
import github.hmasum18.carshowroomfrontend.carshowroom.ResponseParser;
import github.hmasum18.carshowroomfrontend.carshowroom.ServerHolder;
import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.model.LoginInfo;
import github.hmasum18.carshowroomfrontend.view.controller.ObjectListenable;
import github.hmasum18.carshowroomfrontend.view.intentFX.SceneManager;

import java.util.List;
import java.util.concurrent.*;

public class Repository {
    public static final String TAG = "Repository->";
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final ServerHolder serverHolder;
    private ResponseParser responseParser;
    private ObjectListenable currentController;


    public Repository(ServerHolder serverHolder) {
        this.serverHolder = serverHolder;
    }

    //make request
    public void login(LoginInfo loginInfo) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(TAG + " inside login call().");
                //build the request

                RequestBuilder requestBuilder = new RequestBuilder();
                requestBuilder.setData(loginInfo);
                Meta meta = new Meta(Meta.RequestType.POST, Meta.ContentType.LOGIN_INFO,
                        requestBuilder.getData().length(), System.currentTimeMillis());

                requestBuilder.setMeta(meta);
                //now send the request
                System.out.println(TAG + "login(): sending request");
                serverHolder.sendRequest(requestBuilder);
                System.out.println(TAG + " request sent from repository");
            }
        });
    }

    public void getCarList() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(TAG + " inside getCarList():call().");

                //building request
                RequestBuilder requestBuilder = new RequestBuilder();
                //set meta...data will be null
                // requestBuilder.setData(null);
                Meta meta = new Meta(Meta.RequestType.GET, Meta.ContentType.CAR_LIST,
                        0L, System.currentTimeMillis());
                requestBuilder.setMeta(meta);

                //now send the request
                System.out.println(TAG + "sending request");
                serverHolder.sendRequest(requestBuilder);
                System.out.println(TAG + " request sent from repository");
            }
        });
    }

    public void submitCar(Car car, Meta.RequestType requestType) {
        executorService.execute(() -> {
            System.out.println(TAG + " submitCar() ");
            //build the request

            RequestBuilder requestBuilder = new RequestBuilder();
            requestBuilder.setData(car);
            Meta meta = new Meta(requestType, Meta.ContentType.CAR,
                    requestBuilder.getData().getBytes().length, System.currentTimeMillis());
            requestBuilder.setMeta(meta);

            //now send the request
            System.out.println(TAG + "sending request of car data");
            serverHolder.sendRequest(requestBuilder);
            System.out.println(TAG + " car data sent from repository to server");
        });
    }

    public void deleteCar(String carReg) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(TAG + " inside deleteCar()");

                Car car = new Car();
                car.setRegistration(carReg);

                //build the request
                RequestBuilder requestBuilder = new RequestBuilder();
                requestBuilder.setData(car);

                Meta meta = new Meta(Meta.RequestType.DELETE, Meta.ContentType.CAR,
                        requestBuilder.getData().getBytes().length, System.currentTimeMillis());
                requestBuilder.setMeta(meta);

                //now send the request
                System.out.println(TAG + "sending request of car data deletion");
                serverHolder.sendRequest(requestBuilder);
                System.out.println(TAG + " car deletion data sent from repository to server");
            }
        });
    }

  /*  //local changes
    public void onLocalDataChange(Object object){
        if(object instanceof  Car){

        }
    }


    public void handleDataManipulation(Object carData) {
        currentController = SceneManager.getInstance().getCurrentFXMLLoader().getController();
        System.out.println(TAG + "handleDataManipulation(): handling car data manipulation");
        switch (responseParser.getMeta().getRequestType()) {
            case CREATE:
                notifyViewsDataChangeThroughNetwork((Car) carData,DataChangeType.CREATE);
                break;
            case UPDATE:
                notifyViewsDataChangeThroughNetwork((Car) carData,DataChangeType.UPDATE);
                break;
            case DELETE:
                notifyViewsDataChangeThroughNetwork((Car) carData,DataChangeType.DELETE);
                break;
        }
    }*/

/*    public void handleCarListChange(List<Car> carList) {
        currentController = SceneManager.getInstance().getCurrentFXMLLoader().getController();
        DataChangeType dataChangeType;
        ChangeVerdict changeVerdict;
        DataChanger dataChanger;
        if (carList != null) {
            //send to controller
            dataChangeType = DataChangeType.FETCH;
            changeVerdict = ChangeVerdict.SUCCESS;
            dataChanger = DataChanger.SERVER;
        } else {
            //send to controller with error
            dataChangeType = DataChangeType.FETCH;
            changeVerdict = ChangeVerdict.FAILED;
            dataChanger = DataChanger.SERVER;
        }
        DataChangeInfo dataChangeInfo = new DataChangeInfo(dataChangeType,changeVerdict,dataChanger,ChangedDataType.CAR_LIST);
        currentController.onObjectReceived(carList,dataChangeInfo);
    }

    private void notifyViewsDataChangeThroughNetwork(Car car, DataChangeType dataChangeType) {
        System.out.println(TAG + "handleCarCreation(): handling car created.");
        Meta meta = responseParser.getMeta();
        ChangeVerdict changeVerdict = ChangeVerdict.FAILED;
        DataChanger dataChanger = DataChanger.SERVER;
        if (meta.getStatus() == Meta.Status.OK) {
            //this client made the car so data is sent null
            if (car == null) {
                dataChangeType = DataChangeType.CREATE;
                changeVerdict = ChangeVerdict.SUCCESS;
                dataChanger = DataChanger.SELF;
            } else {
                dataChangeType = DataChangeType.CREATE;
                changeVerdict = ChangeVerdict.SUCCESS;
                dataChanger = DataChanger.OTHER;
            }
        } else {
            if (car == null) {
                dataChangeType = DataChangeType.CREATE;
                changeVerdict = ChangeVerdict.FAILED;
                dataChanger = DataChanger.OTHER;
            }
        }
        DataChangeInfo dataChangeInfo = new DataChangeInfo(dataChangeType,changeVerdict,dataChanger,ChangedDataType.CAR);
        currentController.onObjectReceived(car,dataChangeInfo);
    }*/


}
