/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.repository;

public class LiveData<T> {
    public interface DataChangeListenable<T>{
        void onDataChanged(T t);
    }

    DataChangeListenable<T> dataChangeListenable;

    public void postData(T data){
        dataChangeListenable.onDataChanged(data);
    }

    public void observe(DataChangeListenable<T> dataChangeListenable) {
        this.dataChangeListenable = dataChangeListenable;
    }
}
