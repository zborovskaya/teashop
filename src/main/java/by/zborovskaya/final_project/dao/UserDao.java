package by.zborovskaya.final_project.dao;

import by.zborovskaya.final_project.entity.User;

import java.util.List;

public interface UserDao {
    User authorization (String login, String password) throws DaoException;
    boolean	registration(User user) throws DaoException;
    User getUserID(long userID) throws DaoException;
    boolean changePassword(String oldPassword, String newPassword,
                           long userID) throws DaoException;
    public boolean updateUserState(long userId,boolean stateId) throws DaoException;
    public List<User> findAllClients() throws DaoException;
    public List<User> findAllAdmins() throws DaoException;
}
