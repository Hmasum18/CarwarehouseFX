/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroombackend.database;
import github.hmasum18.carshowroombackend.model.LoginInfo;
import github.hmasum18.carshowroombackend.model.UserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class UserTableDao {
    private final ExecutorService executorService;
    private final Connection dbConnection;

    public UserTableDao() {
        executorService = SqliteConnectionBuilder.executorService;
        dbConnection = SqliteConnectionBuilder.getInstance().getConnection();
    }

    /**
     *
     * @param userInfo is the userInfo to be inserted
     * @return true if interested successfully false otherwise
     * @throws Exception is there is some error
     */
    public boolean insert(UserInfo userInfo) throws Exception {
        Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                String query = "INSERT INTO user " +
                        "VALUES (?,?,?, ?,?,?)";
                try(PreparedStatement statement = dbConnection.prepareStatement(query)) {

                    statement.setString(1,userInfo.getUserName());
                    statement.setString(2,userInfo.getFirstName());
                    statement.setString(3,userInfo.getLastName());
                    statement.setString(4,userInfo.getPassword());
                    statement.setString(5,userInfo.getProfilePicUrl());
                    statement.setString(6,userInfo.getRole().name());

                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        });

        return future.get();
    }

    /**
     *
     * @param userName is the userName of the user to be deleted.
     * @return true if interested successfully false otherwise
     * @throws Exception is there is some error
     */
    public boolean delete(String userName) throws Exception{
        Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                String query = "DELETE FROM user WHERE username = ?";
                try(PreparedStatement statement = dbConnection.prepareStatement(query)) {
                    statement.setString(1,userName);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        });
        return future.get();
    }


    /**
     * search for UserInfo in user table
     * @return userInfo is found . null is not not found
     * @throws Exception if there is some error
     */
    public UserInfo query(LoginInfo loginInfo) throws Exception {
        Future<UserInfo> future = executorService.submit(new Callable<UserInfo>() {
            @Override
            public UserInfo call() throws Exception {
                UserInfo userInfo = null;
                String query = "SELECT * FROM user WHERE username = ? AND password = ? AND role = ?";
                try(PreparedStatement statement = SqliteConnectionBuilder.getInstance()
                        .getConnection().prepareStatement(query)) {
                    statement.setString(1,loginInfo.getUserName());
                    statement.setString(2,loginInfo.getPassword());
                    statement.setString(3,loginInfo.getRole().name());

                    ResultSet resultSet = statement.executeQuery();
                    while(resultSet.next()){
                        userInfo = convertResultSetToUserInfo(resultSet);
                        // System.out.println(car.toString());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return userInfo;
            }
        });
        return future.get();
    }

    private UserInfo convertResultSetToUserInfo(ResultSet resultSet) {
        UserInfo userInfo = null;
        try {
            userInfo = new UserInfo(
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("username"),
                    "dummy",
                    LoginInfo.Role.valueOf(resultSet.getString("role"))
            );

        }catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }
}
