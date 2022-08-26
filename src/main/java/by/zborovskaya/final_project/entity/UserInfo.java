package by.zborovskaya.final_project.entity;

import java.util.Objects;

public class UserInfo extends Entity {
    private User user;
    private String email;
    private String firstName;
    private String lastName;
    private int phone;
    private String address;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserInfo(User user, String email, String firstName, String lastName, int phone, String address) {
        this.user = user;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }
    public UserInfo(long userId, String email, String firstName, String lastName, int phone, String address) {
        this.user = new User();
        user.setUserId(userId);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    public UserInfo(String email, String firstName, String lastName, int phone, String address) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo)) return false;
        UserInfo userInfo = (UserInfo) o;
        return phone == userInfo.phone && Objects.equals(user, userInfo.user) && Objects.equals(email, userInfo.email) && Objects.equals(firstName, userInfo.firstName) && Objects.equals(lastName, userInfo.lastName) && Objects.equals(address, userInfo.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, email, firstName, lastName, phone, address);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "user=" + user +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone=" + phone +
                ", address='" + address + '\'' +
                '}';
    }
}
