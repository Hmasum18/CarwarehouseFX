/*
 * Copyright (c) 2020. Hasan Masum
 * Email : connectwithmasum@gmail.com
 * Github: https://github.com/Hmasum18
 * All rights reserved.
 */

package github.hmasum18.carshowroomfrontend.view.intentFX;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import res.R;

import java.io.IOException;
import java.net.URL;

//import res.R;

public class IntentFX {
    public static final String TAG = "IntentFX->";
    public static final int NO_ANIMATION = 0;
    public static final int SLIDE_DOWN_TO_UP = 1;
    public static final int SLIDE_TOP_TO_DOWN = 2;
    public static final int SLIDE_RIGHT_TO_LEFT = 3;
    public static final int SLIDE_LEFT_TO_RIGHT = 4;

    public static final int ANIMATE_CURRENT = 5;
    public static final int ANIMATE_NEW = 6;

    //button or other that extends node which we use to change screen
    private final Stage stage;
    //this fxml location must be from src
    private final String newFXML;
    private int animationType = 0;
    private int animateWho = ANIMATE_NEW;

    public IntentFX(Stage stage, String fxml) {
        this.stage = stage;
        this.newFXML = fxml;
    }

    public IntentFX(Scene scene, String fxml) {
        this.stage = (Stage) scene.getWindow();
        this.newFXML = fxml;
    }

    public IntentFX(Node node, String fxml) {
        this.stage = (Stage) node.getScene().getWindow();
        this.newFXML = fxml;
    }

    public IntentFX(Node node, String fxml, int animationType) {
        this.stage = (Stage) node.getScene().getWindow();
        this.newFXML = fxml;
        this.animationType = animationType;
    }

    public IntentFX(Node node, String fxml, int animationType, int animateWho) {
        this.stage = (Stage) node.getScene().getWindow();
        this.newFXML = fxml;
        this.animationType = animationType;
        this.animateWho = animateWho;
    }

    private Parent getParentFromNewFxml() throws IOException {
        URL url = R.fxml.getURLByName(newFXML);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent parent = fxmlLoader.load();

        //set the fxml loader so that we can get the controller later.
        //SceneManager.getInstance().setCurrentFXMLLoader(fxmlLoader);
        SceneManager.getInstance().addFXMLLoader(newFXML,fxmlLoader);
        return parent;
    }

    /**
     * change the scene of the stage by replacing the previous one
     * remove the previous scene if exist from SceneManager Hash
     *
     * @throws IOException if the fxml is not found
     */
    public void startNewScene() throws IOException {
        Parent newRoot = getParentFromNewFxml();
        //Scene scene = new Scene(parent);

        //remove the previous scene if exist
        if (SceneManager.getInstance().isSceneAlive(newFXML)) {
            SceneManager.getInstance().removeScene(newFXML);
            //SceneManager.getInstance().removeParent(newFXML);
        }
        //add the new parent and scene instead for the fxml file
        SceneManager.getInstance().addParent(newFXML, newRoot);
        //SceneManager.getInstance().addScene(newFXML,scene);

        //if it is not the 1st screen and if user wants any animation
        //call the animate() which will set the scene of the stage
        //after completing the animation
        //and will also update the currentSceneFxml name
        if (SceneManager.getInstance().getCurrentSceneFxmlUrl() != null && animationType != NO_ANIMATION) {
            animate();
           // animateNewScene();
        } else {
            //if no animation is specified
            //then update the current fxml file name and update the stage.
            SceneManager.getInstance().setCurrentSceneFxmlUrl(newFXML);
            Scene newScene = new Scene(newRoot);
            SceneManager.getInstance().addScene(newFXML,newScene);
            stage.setScene(newScene);
        }
    }

    /**
     * start the previous scene if exist else start a new scene of the fxml file
     *
     * @throws IOException is the fxml file was not found
     */
    public void startScene() throws IOException {
        //if there is a instance of a scene of the new fxml file load it
        if (SceneManager.getInstance().isSceneAlive(newFXML)) {
            if (animationType != NO_ANIMATION) {
                //call the animate() which will set the scene of the stage
                //after completing the animation
                //and will also update the currentSceneFxml name
               // animateNewScene();
                 animate();
            } else {
                System.out.println(TAG + " no animation for new scene.. hmm ok done");
                SceneManager.getInstance().setCurrentSceneFxmlUrl(newFXML);
                Parent newRoot = getParentFromNewFxml();
                Scene newScene = new Scene(newRoot);
                stage.setScene(newScene);
            }
        } else {
            System.out.println(TAG+" startScene():  scene doesn't exist . starting brand new scene");
            startNewScene();
        }
    }

    /*    */

    /**
     * animate the scene changing.
     * every scene must have a pane to achieve the animation
     */
    private void animate() {
        /*//System.out.println();
        //get the old current scene in the stage;
        Scene currentScene = SceneManager.getInstance().getCurrentScene();

        //get the fxml parent of new Scene
        Parent newRoot = SceneManager.getInstance().getParent(newFXML);
        Parent currentRoot = currentScene.getRoot();//SceneManager.getInstance().getCurrentParent();
        //System.out.println(TAG+"animate():current : "+currentRoot);
       // System.out.println(TAG+"animate(): "+currentRoot.getStyleClass().size());
        if(currentRoot.getStyleClass().size()>1){
            for (int i = 0; i <currentRoot.getStyleClass().size() ; i++) {
                System.out.println("removing: "+currentRoot.getStyleClass().get(i));
                currentRoot.getStyleClass().remove(i);
            }
        }
        System.out.println(TAG+"animate():new: "+newRoot);

        //update the name of current fxml with new fxml file
        SceneManager.getInstance().setCurrentSceneFxmlUrl(newFXML);

        //every scene must have a rootPane to achieve the animation
        //get the current root Pane of the fxml file of current scene
        //add whole fxml files parent as a child of this pane
        Pane currentRootPane = (Pane) currentScene.getRoot();
        int idx = currentRootPane.getChildren().size();
        //System.out.println("total child : "+idx);
        currentRootPane.getChildren().add(idx,newRoot);
        //System.out.println("total child now : "+currentRootPane.getChildren().size());

        //determine from which direction this new parent of newFxml should animate
        newRoot.translateYProperty().set(currentScene.getHeight());
        System.out.println("translateY: "+newRoot.translateYProperty().get());

        //create timeline, keyValue, keyFrame to animate the newRoot
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(newRoot.translateYProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        SceneManager.getInstance().setSceneTransitionAlive(true);
        timeline.play();
        System.out.println(TAG+"animate(): Start of animation");
        timeline.setOnFinished(t -> {
            Platform.runLater(()->{
                //remove the animated fxml which was added as a child of current scene root(Parent)
                currentRootPane.getChildren().remove(idxnewRoot.getStyleableNode());
                //System.out.println("translateY now: "+newRoot.translateYProperty().get());

                //update the scene with the newRoot replacing the old one
                //System.out.println(TAG+"animate():newRoot styleClass: "+newRoot.getStyleClass().size());
                if(newRoot.getStyleClass().size()>=1){
                    for (int i = 0; i <currentRoot.getStyleClass().size() ; i++) {
                        //System.out.println("removing: "+currentRoot.getStyleClass().get(i));
                        newRoot.getStyleClass().remove(i);
                    }
                }
                Scene scene = new Scene(newRoot);
                if(SceneManager.getInstance().isSceneAlive(newFXML)){
                    SceneManager.getInstance().removeScene(newFXML);
                }
                SceneManager.getInstance().addScene(newFXML,scene);
                stage.setScene(scene);
                SceneManager.getInstance().setSceneTransitionAlive(false);
                System.out.println(TAG+"animate(): End of animation");
            });
        });*/
        if(animateWho == ANIMATE_CURRENT)
            animateCurrentScene();
        else
            animateNewScene();
    }
    private void animateCurrentScene() {
        //get the old current scene in the stage;
        Scene currentScene = stage.getScene();

        //get the fxml parent of new Scene
        Pane newRoot = (Pane) SceneManager.getInstance().getParent(newFXML);
        Pane currentRoot = (Pane) currentScene.getRoot();//SceneManager.getInstance().getCurrentParent();

        //update the name of current fxml with new fxml file
        Scene scene;
        SceneManager.getInstance().setCurrentSceneFxmlUrl(newFXML);
        if (SceneManager.getInstance().isSceneAlive(newFXML)) {
            System.out.println(TAG+ newFXML+ " is alive");
            scene = SceneManager.getInstance().getScene(newFXML);
        }else{
            System.out.println(TAG+ newFXML+ " is not alive starting a new scene.");
            scene = new Scene(newRoot);
            SceneManager.getInstance().addScene(newFXML, scene);
        }
        stage.setScene(scene);

        //every scene must have a rootPane to achieve the animation
        //get the current root Pane of the fxml file of current scene
        //add whole fxml files parent as a child of this pane
        int idx = newRoot.getChildren().size();
        //System.out.println("total child : "+idx);
        newRoot.getChildren().add(idx, currentRoot);


        //create timeline, keyValue, keyFrame to animate the newRoot
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(currentRoot.translateYProperty(), stage.getScene().getHeight(), Interpolator.EASE_OUT);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        SceneManager.getInstance().setSceneTransitionAlive(true);
        timeline.play();
        System.out.println(TAG + "animate(): Start of animation");
        timeline.setOnFinished(t -> {
            Platform.runLater(() -> {
                //remove the animated fxml which was added as a child of current scene root(Parent)
                 newRoot.getChildren().remove(idx);

                //update the scene with the newRoot replacing the old one
                if(currentRoot.getStyleClass().size()>=1){
                    for (int i = 0; i <currentRoot.getStyleClass().size() ; i++) {
                        System.out.println(TAG+"animateCurrentScene: "+"removing: "+currentRoot.getStyleClass().get(i));
                        currentRoot.getStyleClass().remove(i);
                    }
                }
                SceneManager.getInstance().setSceneTransitionAlive(false);
                System.out.println(TAG + "animate(): End of animation");
            });
        });
    }

    private void animateNewScene() {
        //get the old current scene in the stage;
        Scene currentScene = SceneManager.getInstance().getCurrentScene();

        //get the fxml parent of new Scene
        Parent newRoot = SceneManager.getInstance().getParent(newFXML);
        Parent currentRoot = currentScene.getRoot();//SceneManager.getInstance().getCurrentParent();
        //System.out.println(TAG+"animate():current : "+currentRoot);
        // System.out.println(TAG+"animate(): "+currentRoot.getStyleClass().size());
       /* if(currentRoot.getStyleClass().size()>1){
            for (int i = 0; i <currentRoot.getStyleClass().size() ; i++) {
                System.out.println("removing: "+currentRoot.getStyleClass().get(i));
                currentRoot.getStyleClass().remove(i);
            }
        }*/
        System.out.println(TAG + "animate():new: " + newRoot);

        //update the name of current fxml with new fxml file
        SceneManager.getInstance().setCurrentSceneFxmlUrl(newFXML);

        //every scene must have a rootPane to achieve the animation
        //get the current root Pane of the fxml file of current scene
        //add whole fxml files parent as a child of this pane
        Pane currentRootPane = (Pane) currentScene.getRoot();
        int idx = currentRootPane.getChildren().size();
        //System.out.println("total child : "+idx);
        currentRootPane.getChildren().add(idx, newRoot);
        //System.out.println("total child now : "+currentRootPane.getChildren().size());

        //determine from which direction this new parent of newFxml should animate
        newRoot.translateYProperty().set(currentScene.getHeight());
        System.out.println("translateY: " + newRoot.translateYProperty().get());

        //create timeline, keyValue, keyFrame to animate the newRoot
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(newRoot.translateYProperty(), 0, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        SceneManager.getInstance().setSceneTransitionAlive(true);
        timeline.play();
        System.out.println(TAG + "animate(): Start of animation");
        timeline.setOnFinished(t -> {
            Platform.runLater(() -> {
                //remove the animated fxml which was added as a child of current scene root(Parent)
                currentRootPane.getChildren().remove(idx/*newRoot.getStyleableNode()*/);
                //System.out.println("translateY now: "+newRoot.translateYProperty().get());

                //update the scene with the newRoot replacing the old one
                //System.out.println(TAG+"animate():newRoot styleClass: "+newRoot.getStyleClass().size());
                if (newRoot.getStyleClass().size() >= 1) {
                    for (int i = 0; i < currentRoot.getStyleClass().size(); i++) {
                        //System.out.println("removing: "+currentRoot.getStyleClass().get(i));
                        newRoot.getStyleClass().remove(i);
                    }
                }
                Scene scene ;//= new Scene(newRoot);
                if (SceneManager.getInstance().isSceneAlive(newFXML)) {
                    //SceneManager.getInstance().removeScene(newFXML);
                    scene = SceneManager.getInstance().getScene(newFXML);
                }else{
                    scene = new Scene(newRoot);
                }
                SceneManager.getInstance().addScene(newFXML, scene);
                stage.setScene(scene);
                SceneManager.getInstance().setSceneTransitionAlive(false);
                System.out.println(TAG + "animate(): End of animation");
            });
        });
    }
}
