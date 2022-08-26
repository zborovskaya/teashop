package by.zborovskaya.final_project.service.impl;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.dao.DaoFactory;
import by.zborovskaya.final_project.dao.UserInfoDao;
import by.zborovskaya.final_project.entity.UserInfo;
import by.zborovskaya.final_project.service.ClientService;
import by.zborovskaya.final_project.service.ServiceException;
import by.zborovskaya.final_project.service.validation.ClientInfoValidator;
import by.zborovskaya.final_project.service.validation.exception.ValidatorException;

import java.util.List;

public class ClientServiceImpl implements ClientService {

    private final static String INVALID_NAME = "Invalid name. The name must start with a capital letter";
    private final static String INVALID_PHONE = "Invalid phone";
    private final static String INVALID_EMAIL = "Invalid email";

    @Override
    public boolean addClientData(UserInfo client, long userID) throws ServiceException, ValidatorException {
        String firstName = client.getFirstName();
        String lastName = client.getLastName();
        int phone = client.getPhone();
        String email= client.getEmail();
        String address = client.getAddress();

        if(!ClientInfoValidator.isFirstNameValid(firstName)){
            throw new ValidatorException(INVALID_NAME);
        }

        if(!ClientInfoValidator.isLastNameValid(lastName)){
            throw new ValidatorException(INVALID_NAME);
        }

        if(!ClientInfoValidator.isPhoneValid(String.valueOf(phone))){
            throw new ValidatorException(INVALID_PHONE);
        }
        if(!ClientInfoValidator.isEmailValid(email)){
            throw new ValidatorException(INVALID_EMAIL);
        }

        DaoFactory daoFactory=DaoFactory.getInstance();
        UserInfoDao clientDao = daoFactory.getUserInfoDao();
        boolean isClientDataAdded = false;
        try {
            isClientDataAdded = clientDao.addClientData(client,userID);
        }catch(DaoException e) {
            throw new ServiceException(e);
        }

        return isClientDataAdded;
    }

    @Override
    public boolean updateClientData(UserInfo newClient) throws ServiceException, ValidatorException {
        String firstName = newClient.getFirstName();
        String lastName = newClient.getLastName();
        int phone = newClient.getPhone();
        String email= newClient.getEmail();

        if(!ClientInfoValidator.isFirstNameValid(firstName)){
            throw new ValidatorException(INVALID_NAME);
        }

        if(!ClientInfoValidator.isLastNameValid(lastName)){
            throw new ValidatorException(INVALID_NAME);
        }

        if(!ClientInfoValidator.isPhoneValid(String.valueOf(phone))){
            throw new ValidatorException(INVALID_PHONE);
        }
        if(!ClientInfoValidator.isEmailValid(email)){
            throw new ValidatorException(INVALID_EMAIL);
        }

        DaoFactory daoFactory=DaoFactory.getInstance();
        UserInfoDao userInfoDao = daoFactory.getUserInfoDao();
        boolean isClientDataUpdated = false;
        try {
            isClientDataUpdated = userInfoDao.updateClientData(newClient);
        }catch(DaoException e) {
            throw new ServiceException(e);
        }

        return isClientDataUpdated;
    }

    @Override
    public List<UserInfo> findAll() throws ServiceException {
        return null;
    }

    @Override
    public UserInfo findUserInfoById(int idClient) throws ServiceException {
        DaoFactory daoFactory=DaoFactory.getInstance();
        UserInfoDao userInfoDao = daoFactory.getUserInfoDao();
        UserInfo userInfo = null;
        try {
            userInfo = userInfoDao.findUserInfoById(idClient);
        }catch(DaoException e) {
            throw new ServiceException(e);
        }
        return userInfo;
    }

    public static void main(String[] args) {
        ClientService clientService = new ClientServiceImpl();
        try {
            UserInfo userInfo = clientService.findUserInfoById(3);
            System.out.println(userInfo);
        }catch (Exception ex){}
    }
}
