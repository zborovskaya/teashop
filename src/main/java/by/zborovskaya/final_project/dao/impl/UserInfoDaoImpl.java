package by.zborovskaya.final_project.dao.impl;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.entity.UserInfo;
import by.zborovskaya.final_project.dao.UserInfoDao;
import by.zborovskaya.final_project.dao.pool.ConnectionPool;
import by.zborovskaya.final_project.dao.pool.ConnectionPoolException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserInfoDaoImpl implements UserInfoDao {
    private static final Logger logger = LogManager.getLogger(UserInfoDaoImpl.class);

    private final static ConnectionPool connectionPool = ConnectionPool.getInstance();

    private final static String INSERT_USERINFO = "INSERT INTO `user_info`(`user_id`,`email`,`first_name`," +
            "`last_name`,`phone`,`address`)VALUES(?, ?, ?, ?,?,?);";
    private static final String SQL_UPDATE_USERINFO = "UPDATE user_info SET email = (?), first_name = (?)," +
            " last_name = (?), phone = (?), address = (?) WHERE user_id = (?)";
    private static final String SQL_SELECT_USERINFO = "SELECT email, first_name, last_name, " +
            "phone, address FROM user_info " +
            "WHERE user_id = (?)";
    private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";


    @Override
    public boolean addClientData(UserInfo userInfo, long userID) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;

        boolean isClientRegistrated = false;

        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(INSERT_USERINFO);
            st.setLong(1, userID);
            st.setString(2,userInfo.getEmail());
            st.setString(3, userInfo.getFirstName());
            st.setString(4, userInfo.getLastName());
            st.setInt(5, userInfo.getPhone());
            st.setString(6, userInfo.getAddress());
            st.executeUpdate();
            isClientRegistrated = true;
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
                }
            }
            if(con != null){
                connectionPool.releaseConnection(con);
            }
        }
        return isClientRegistrated;
    }

    @Override
    public boolean updateClientData(UserInfo newUserInfo) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

//        boolean  updateClientData = false;
        try{
//            connectionPool.init();
            con = connectionPool.takeConnection();
            st = con.prepareStatement(SQL_UPDATE_USERINFO);
            st.setString(1, newUserInfo.getEmail());
            st.setString(2, newUserInfo.getFirstName());
            st.setString(3, newUserInfo.getLastName());
            st.setInt(4,newUserInfo.getPhone());
            st.setString(5, newUserInfo.getAddress());
            st.setLong(6, newUserInfo.getUser().getIdUser());
            st.executeUpdate();

//            isPasswordChanged = true;

        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }catch (SQLException e){
                }
            }
            if (st != null) {
                try {
                    st.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
                }
            }
            if(con != null){
                connectionPool.releaseConnection(con);
            }
        }
        return true;
    }

    @Override
    public List<UserInfo> findAll() throws DaoException {
        return null;
    }

    @Override
    public UserInfo findUserInfoById(long idUser) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        UserInfo userInfo = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_USERINFO);
            st.setLong(1, idUser);
            rs = st.executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                String firstName = rs.getString("first_name");
                String lastName =rs.getString("last_name");
                int phone = rs.getInt("phone");
                String address = rs.getString("address");
                userInfo = new UserInfo(email,firstName,lastName,phone,address);
            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    logger.log(Level.FATAL, FATAL_ERROR_PREPARED_STATEMENT, e);
                }
            }
            if (con != null) {
                connectionPool.releaseConnection(con);
            }
        }
        return userInfo;
    }

    public static void main(String[] args){
        //регистрация клиента
//        UserInfo client=new UserInfo("kate@gmail.com","Катя","Стасюк",
//                234568794,"Брест");
//        try {
//            UserInfoDaoImpl clientDaoImpl = new UserInfoDaoImpl();
//            System.out.println(clientDaoImpl.addClientData(client,3));
//        }catch (Exception ex){}

        //обновление информации о клиенте
//        UserInfo client=new UserInfo(3,"kate@gmail.com","Катя","Стасюк",
//                234568794,"Минск");
//
//        try {
//            UserInfoDaoImpl clientDaoImpl = new UserInfoDaoImpl();
//            System.out.println(clientDaoImpl.updateClientData(client));
//        }catch (Exception ex){}

        //получить данные о пользователе
//        try {
//            UserInfoDaoImpl clientDaoImpl = new UserInfoDaoImpl();
//            System.out.println(clientDaoImpl.findUserInfoById(3).toString());
//        }catch (Exception ex){}

    }
}
