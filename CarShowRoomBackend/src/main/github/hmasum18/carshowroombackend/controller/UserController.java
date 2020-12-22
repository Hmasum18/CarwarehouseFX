/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroombackend.controller;

import github.hmasum18.carshowroombackend.database.UserTableDao;
import github.hmasum18.carshowroombackend.model.LoginInfo;
import github.hmasum18.carshowroombackend.model.UserInfo;

public class UserController {
    private UserTableDao userTableDao;

    public UserController() {
        this.userTableDao = new UserTableDao();
    }

    /**
     * @return ther userInfo the user exist.. null otherwise
     * @throws Exception if there is some error
     */
    public UserInfo login(LoginInfo loginInfo) throws Exception {
        return userTableDao.query(loginInfo);
    }


    /**
     * @param userInfo is the profile information
     * @return true if the user added successfully
     * @throws Exception if there is some error
     */
    public boolean addUser(UserInfo userInfo) throws Exception {
        return userTableDao.insert(userInfo);
    }

    /**
     * @param username is the username of the user to be removed
     * @return true if the user removed successfully
     * @throws Exception if there is some error
     */
    public boolean removeUser(String username) throws Exception {
        return userTableDao.delete(username);
    }
}
