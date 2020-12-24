/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroombackend.model;

public class ClientInfo{
    private String clientIpPort;
    private UserInfo userInfo;

    public void setClientIpPort(String clientIpPort) {
        this.clientIpPort = clientIpPort;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getClientIpPort() {
        return clientIpPort;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "clientIpPort='" + clientIpPort + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
}
