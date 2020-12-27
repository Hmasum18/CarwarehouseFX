/*
 * Copyright (c) 2020. Hasan Masum
 * Email : connectwithmasum@gmail.com
 * Github: https://github.com/Hmasum18
 * All rights reserved.
 */

package github.hmasum18.carshowroomfrontend.view.intentFX;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;

public class SceneManager {
    public static final String TAG = "SceneManager->";
    public static SceneManager instance;
    //fxml src address and its scene
    private final HashMap<String, Scene> sceneHashMap;
    private final HashMap<String, Parent> parentHashMap;
    private final HashMap<String,FXMLLoader> fxmlLoaderHashMap;
    private FXMLLoader currentFXMLLoader;
    private String currentSceneFxmlUrl;
    private boolean sceneTransitionAlive;


    private SceneManager(){
        sceneHashMap = new HashMap<>();
        parentHashMap = new HashMap<>();
        fxmlLoaderHashMap = new HashMap<>();
    }

    public static SceneManager getInstance() {
        if(instance == null)
            instance = new SceneManager();
        return instance;
    }

    public void addScene(String fxmlUrl, Scene scene){
        sceneHashMap.put(fxmlUrl,scene);
    }

    public void removeScene(String fxmlUrl){
        if(isSceneAlive(fxmlUrl))
            sceneHashMap.remove(fxmlUrl);
    }

    public Scene getScene(String fxmlUrl){
        return sceneHashMap.get(fxmlUrl);
    }

    public boolean isSceneAlive(String fxmlUrl){
        return sceneHashMap.get(fxmlUrl)!=null;
    }

    public String getCurrentSceneFxmlUrl() {
        return currentSceneFxmlUrl;
    }

    public void addFXMLLoader(String fxml, FXMLLoader fxmlLoader){
        fxmlLoaderHashMap.put(fxml,fxmlLoader);
    }

    public void removeFXMLLoader(String fxml){
        fxmlLoaderHashMap.remove(fxml);
    }

    public void setCurrentSceneFxmlUrl(String currentSceneFxmlUrl) {
        this.currentSceneFxmlUrl = currentSceneFxmlUrl;
    }

    public Scene getCurrentScene(){
        return sceneHashMap.get(currentSceneFxmlUrl);
    }


    public void addParent(String fxmlUrl, Parent parent){
        parentHashMap.put(fxmlUrl,parent);
    }

    public void removeParent(String fxmlUrl){
        if(isSceneAlive(fxmlUrl))
            parentHashMap.remove(fxmlUrl);
    }

    public Parent getParent(String fxmlUrl){
        return parentHashMap.get(fxmlUrl);
    }

    public Parent getCurrentParent(){
        return parentHashMap.get(currentSceneFxmlUrl);
    }

    public void setCurrentFXMLLoader(FXMLLoader currentFXMLLoader) {
        System.out.println(TAG+"fxmlLoaderReceived");
        this.currentFXMLLoader = currentFXMLLoader;
    }

    public FXMLLoader getCurrentFXMLLoader(){
        System.out.println(TAG+" getCurrentFXMLLoader(): currentFXML = "+currentSceneFxmlUrl);
        return fxmlLoaderHashMap.get(currentSceneFxmlUrl);
    }

    public void setSceneTransitionAlive(boolean sceneTransitionAlive) {
        this.sceneTransitionAlive = sceneTransitionAlive;
    }

    public boolean isSceneTransitionAlive() {
        return sceneTransitionAlive;
    }

}
