package by.zborovskaya.final_project.service;

import by.zborovskaya.final_project.entity.UserInfo;
import by.zborovskaya.final_project.service.validation.exception.ValidatorException;

import java.util.List;

public interface ClientService {
    boolean	addClientData(UserInfo client, long userID) throws ServiceException, ValidatorException;
    boolean	updateClientData(UserInfo newClient) throws ServiceException, ValidatorException ;
    List<UserInfo> findAll() throws ServiceException;
    UserInfo findUserInfoById(int idClient) throws ServiceException;
}
