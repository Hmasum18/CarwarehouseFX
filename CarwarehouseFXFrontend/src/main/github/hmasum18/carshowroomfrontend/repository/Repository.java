package github.hmasum18.carshowroomfrontend.repository;

import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.carshowroom.RequestBuilder;
import github.hmasum18.carshowroomfrontend.carshowroom.ResponseParser;
import github.hmasum18.carshowroomfrontend.carshowroom.ServerHolder;
import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.model.LoginInfo;
import github.hmasum18.carshowroomfrontend.model.UserInfo;
import github.hmasum18.carshowroomfrontend.view.controller.ObjectListenable;

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

    //get response
    LiveData<UserInfo> userInfoLiveData = new LiveData<>();
    public LiveData<UserInfo> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    LiveData<Car> carLiveData = new LiveData<>();
    public LiveData<Car> getCarLiveData(){
        return carLiveData;
    }

    LiveData<List<Car>> carListLiveData = new LiveData<>();
    public LiveData<List<Car>> getCarListLiveData() {
        return carListLiveData;
    }

    public void onDataReceived(Meta meta, Object object){
        switch (meta.getStatus()){
            case OK:
                handleSuccessResponse(meta);
                break;
            case FAILED: //for post,create,delete req
                handleFailedResponse(meta);
                break;
            case NOT_FOUND: //for get request
                handleNotFoundResponse(meta);
                break;
        }
    }

    private void handleSuccessResponse(Meta meta){
        switch (meta.getContentType()){
            case USER_INFO:
                userInfoLiveData.postData(responseParser.getUserInfo());
                break;
            case CAR:
                carLiveData.postData(responseParser.getCar());
                break;
            case CAR_LIST:
                carListLiveData.postData(responseParser.getCarList());
                break;
        }
    }

    private void handleFailedResponse(Meta meta) {

    }

    private void handleNotFoundResponse(Meta meta) {

    }

}
