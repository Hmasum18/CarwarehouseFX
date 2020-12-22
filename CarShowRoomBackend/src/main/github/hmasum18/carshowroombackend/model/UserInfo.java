/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroombackend.model;

public class UserInfo extends LoginInfo{
    private String firstName;
    private String lastName;
    private String profilePicUrl;

    public UserInfo(String firstName, String lastName, String username, String password,Role role) {
        super(username,password,role);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserInfo(String firstName, String lastName, String username,
                    String password, String profilePicUrl, Role role) {
        super(username,password,role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicUrl = profilePicUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                "userName='" + getUserName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role=" + getRole() +
                '}';
    }
}
