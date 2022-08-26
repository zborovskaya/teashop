package by.zborovskaya.final_project.dao;

import by.zborovskaya.final_project.entity.UserInfo;

import java.util.List;

public interface UserInfoDao {
    boolean	addClientData(UserInfo client, long userID) throws DaoException;
    boolean	updateClientData(UserInfo newUserInfo) throws DaoException;
    List<UserInfo> findAll() throws DaoException;
    UserInfo findUserInfoById(long idUser) throws DaoException;
}
