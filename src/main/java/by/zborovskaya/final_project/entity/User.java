package by.zborovskaya.final_project.entity;

import java.util.Objects;

public class User extends Entity{
    private UserRole userRole;
    private long userId;
    private String login;
    private String password;
    private boolean isActive;

    public User(){}

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public User(long userId, String role, String login, String password, boolean isActive) {
        this.userId = userId;
        this.userRole = UserRole.valueOf(role);
        this.login = login;
        this.password = password;
        this.isActive = isActive;
    }
    public User(String role, String login, String password, boolean isActive) {
        this.userRole = UserRole.valueOf(role);
        this.login = login;
        this.password = password;
        this.isActive = isActive;
    }


    public String getLogin() {
        return login;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getIdUser() {
        return userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userRole=" + userRole +
                ", idUser=" + userId +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId && isActive == user.isActive && userRole == user.userRole && Objects.equals(login, user.login) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRole, userId, login, password, isActive);
    }
}
