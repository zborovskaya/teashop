package by.zborovskaya.final_project.service.impl;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.dao.DaoFactory;
import by.zborovskaya.final_project.dao.UserDao;
import by.zborovskaya.final_project.dao.impl.UserDaoImpl;
import by.zborovskaya.final_project.entity.User;
import by.zborovskaya.final_project.service.ServiceException;
import by.zborovskaya.final_project.service.UserService;
import by.zborovskaya.final_project.service.validation.PasswordChangeValidator;
import by.zborovskaya.final_project.service.validation.SignUpValidator;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public List<User> findAllClients() throws ServiceException {
        DaoFactory daoFactory=DaoFactory.getInstance();
        UserDao userDao = daoFactory.getUserDao();
        List<User> userList;
        try {
            userList = userDao.findAllClients();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return userList;
    }

    @Override
    public void changeUserState(boolean state, long id) throws ServiceException {
        DaoFactory daoFactory=DaoFactory.getInstance();
        UserDao userDao = daoFactory.getUserDao();
        try {
            userDao.updateUserState(id,state);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findAllAdmins() throws ServiceException {
        DaoFactory daoFactory=DaoFactory.getInstance();
        UserDao userDao = daoFactory.getUserDao();
        List<User> userList;
        try {
            userList = userDao.findAllAdmins();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return userList;
    }

    private final static String EMPTY_STRING = "";
    private final static String INPUT_ALL_DATA = "Input all data";
    private final static String INVALID_LOGIN = "Invalid login. Login may include letters from A to Z in any register," +
            " numbers from 0 to 9. Login length must be from 3 to 50 signs";
    private final static String INVALID_PASSWORD = "Invalid password." +
            " The password must contain upper and lower case letters from a to z and numbers from 0 to 9." +
            " Password length must be from 3 to 50 signs";
    private final static String CONFIRM_PASSWORD = "New and confirmed passwords are not the same";

    @Override
    public User authorization(String login, String password) throws ServiceException {
        if(login == null || EMPTY_STRING.equals(login) || password == null || EMPTY_STRING.equals(password)) {
            throw new ServiceException(INPUT_ALL_DATA);
        }

        DaoFactory daoFactory=DaoFactory.getInstance();
        UserDao userDao = daoFactory.getUserDao();

        User user = null;
        try {
            user = userDao.authorization(login, password);
        }catch(DaoException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public boolean registration(User user) throws ServiceException{
        String login = user.getLogin();
        String password = user.getPassword();

        //TODO ServiceException
        if(login == null || EMPTY_STRING.equals(login) || password == null || EMPTY_STRING.equals(password)) {
            throw new ServiceException(INPUT_ALL_DATA);
        }

        if(!SignUpValidator.isLoginValid(login)){
            throw new ServiceException(INVALID_LOGIN);
        }
        if(!SignUpValidator.isPasswordValid(password)){
            throw new ServiceException(INVALID_PASSWORD);
        }

        DaoFactory daoFactory=DaoFactory.getInstance();
        UserDao userDao = daoFactory.getUserDao();

        boolean isUserRegistrated = false;
        try {
            //TODO userDao.registration return idUser
            isUserRegistrated = userDao.registration(user);
            //TODO User.setId
        }catch(DaoException e) {
            throw new ServiceException(e);
        }

        return isUserRegistrated;
    }

    @Override
    public User getUserID(int userID) throws ServiceException {

        DaoFactory daoFactory=DaoFactory.getInstance();
        UserDao userDao = daoFactory.getUserDao();

        User user = null;
        try{
            user = userDao.getUserID(userID);
        }catch(DaoException e) {
            throw new ServiceException(e);
        }

        return user;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword,String confirmedPassword, int userID)
            throws ServiceException {
        if(null == oldPassword || EMPTY_STRING.equals(oldPassword) ||
                null == newPassword || EMPTY_STRING.equals(newPassword) ||
                null == confirmedPassword || EMPTY_STRING.equals(confirmedPassword)){
            throw new ServiceException(INPUT_ALL_DATA);
        }

        if(!PasswordChangeValidator.isPasswordConfirmed(newPassword, confirmedPassword)){
            throw new ServiceException(CONFIRM_PASSWORD);
        }
        if(!PasswordChangeValidator.isPasswordValid(newPassword)){
            throw new ServiceException(INVALID_PASSWORD);
        }


        DaoFactory daoFactory=DaoFactory.getInstance();
        UserDao userDao = daoFactory.getUserDao();

        boolean isPasswordChanged = false;
        try{
            isPasswordChanged = userDao.changePassword(oldPassword, newPassword, userID);
        } catch(DaoException e) {
            throw new ServiceException(e);
        }

        return isPasswordChanged;
    }

    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();
        try {
//            System.out.println(userService.authorization("user6", "77777777777709"));
            System.out.println(userService.findAllClients().toString());
        }catch (ServiceException ex){

        }
    }
}
