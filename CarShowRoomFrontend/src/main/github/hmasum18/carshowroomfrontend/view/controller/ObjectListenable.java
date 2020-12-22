/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.view.controller;

import github.hmasum18.carshowroomfrontend.carshowroom.Meta;
import github.hmasum18.carshowroomfrontend.repository.*;

public interface ObjectListenable {
   //void onObjectReceived(Object object, DataChangeInfo dataChangeInfo);
   void onObjectReceived(Object object, Meta meta);
}
