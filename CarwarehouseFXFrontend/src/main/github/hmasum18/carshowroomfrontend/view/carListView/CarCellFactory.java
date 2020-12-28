package github.hmasum18.carshowroomfrontend.view.carListView;

import github.hmasum18.carshowroomfrontend.model.Car;
import github.hmasum18.carshowroomfrontend.view.carListView.CarCell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


public class CarCellFactory implements Callback<ListView<Car>, ListCell<Car>> {
    @Override
    public ListCell<Car> call(ListView<Car> carListView) {
        return new CarCell();
    }
}
