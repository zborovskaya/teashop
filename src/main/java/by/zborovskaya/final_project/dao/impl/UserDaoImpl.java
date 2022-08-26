package by.zborovskaya.final_project.dao.impl;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.dao.UserDao;
import by.zborovskaya.final_project.dao.pool.ConnectionPool;
import by.zborovskaya.final_project.dao.pool.ConnectionPoolException;
import by.zborovskaya.final_project.entity.User;
import by.zborovskaya.final_project.entity.UserRole;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

    private final static ConnectionPool connectionPool = ConnectionPool.getInstance();

    private final static String UPDATE_PASSWORD = "UPDATE `users`SET `password` = ? WHERE user_id = ? ";
    private final static String SELECT_BY_LOGIN = "SELECT user_id,`login`," +
            "`password`,`is_active`,`role_id` FROM users WHERE `login` = ?";
    private final static String COUNT_SUCH_LOGIN = "SELECT COUNT(user_id) AS loginEquals FROM `users` WHERE `login` = ? ";
    private final static String USER_LOGIN = "login";
    private final static String USER_PASSWORD = "password";
    private final static String USER_ID = "user_id";
    private final static String USER_ROLE = "role_id";
    private final static String USER_ACTIVE = "is_active";
    private final static String LOGIN_EQUALS = "loginEquals";
    private final static String SELECT_USER_BY_ID = "SELECT * FROM `users` WHERE `user_id`=?";
    private final static String REGISTRSATE_USER = "INSERT INTO `users`(`login`,`password`,`is_active`,`role_id`)" +
            " VALUES(?,?,?,?)";
    private static final String SQL_UPDATE_USER_STATE_BY_ID = "UPDATE users " +
            "SET is_active = (?) WHERE user_id = (?)";
    private final static String FATAL_ERROR_RESULT_SET = "Fatal error closing resultSet";
    private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";
    private static final String SQL_SELECT_ALL_CLIENTS = "SELECT user_id,login,password,is_active," +
            " user_role.role_name FROM users " +
            "JOIN user_role ON user_role.role_id = users.role_id " +
            "WHERE role_name = 'client'";
    private static final String SQL_SELECT_ALL_ADMINS = "SELECT user_id,login,password,is_active," +
            " user_role.role_name FROM users " +
            "JOIN user_role ON user_role.role_id = users.role_id " +
            "WHERE role_name = 'admin'";


    @Override
    public User authorization(String login, String password) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        User user = null;

        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();
            st = con.prepareStatement(SELECT_BY_LOGIN);
            st.setString(1, login);
            rs = st.executeQuery();

            while (rs.next()) {
                String userPassword = rs.getString(USER_PASSWORD);
                boolean userActive = rs.getBoolean(USER_ACTIVE);
                String userRole = null;
                long userID = rs.getInt(USER_ID);
                if (rs.getInt(USER_ROLE) == 2) {
                    userRole = UserRole.ADMIN.getValue();
                } else {
                    userRole = UserRole.CLIENT.getValue();
                }

                if (userPassword.equals(password) && userActive) {
                    user = new User(userID, userRole.toUpperCase(), login, password, userActive);
                } else {
                    return null;
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
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

        return user;
    }

    @Override
    public boolean registration(User user) throws DaoException {

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(COUNT_SUCH_LOGIN);
            st.setString(1, user.getLogin());
            rs = st.executeQuery();
            while (rs.next()) {
                int usersWithSuchLogin = rs.getInt(LOGIN_EQUALS);
                if (usersWithSuchLogin != 0) {
                    return false;
                }
                break;
            }

            st = con.prepareStatement(REGISTRSATE_USER);
            st.setString(1, user.getLogin());
            st.setString(2, user.getPassword());
            st.setBoolean(3, user.isActive());
            UserRole userRole = user.getUserRole();
            if (userRole == UserRole.CLIENT) {
                st.setInt(4, 1);
            } else {
                st.setInt(4, 2);
            }
            st.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
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

        return true;
    }

    @Override
    public User getUserID(long userID) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        User user = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SELECT_USER_BY_ID);
            st.setLong(1, userID);
            rs = st.executeQuery();

            while (rs.next()) {
                String userPassword = rs.getString(USER_PASSWORD);
                String userLogin = rs.getString(USER_LOGIN);
                boolean userActive = rs.getBoolean(USER_ACTIVE);
                String userRole = null;
                if (rs.getInt(USER_ROLE) == 2) {
                    userRole = UserRole.ADMIN.getValue();
                } else {
                    userRole = UserRole.CLIENT.getValue();
                }
                if (userActive) {
                    user = new User(userID, userRole.toUpperCase(), userLogin, userPassword, userActive);
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
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
        return user;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword, long userID) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

//        boolean isPasswordChanged = false;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();
            st = con.prepareStatement(SELECT_USER_BY_ID);
            st.setLong(1, userID);
            rs = st.executeQuery();

            if (rs.next()) {
                String currentPassword = rs.getString(USER_PASSWORD);
                if (!currentPassword.equals(oldPassword)) {
                    return false;
                }
            }

            st = con.prepareStatement(UPDATE_PASSWORD);
            st.setString(1, newPassword);
            st.setLong(2, userID);
            st.executeUpdate();

//            isPasswordChanged = true;

        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
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
        return true;
    }

    @Override
    public boolean updateUserState(long userId, boolean stateId) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

//        boolean isPasswordChanged = false;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();
            st = con.prepareStatement(SQL_UPDATE_USER_STATE_BY_ID);
            st.setBoolean(1, stateId);
            st.setLong(2, userId);
            st.executeUpdate();
        } catch (SQLException | ConnectionPoolException e) {
            logger.log(Level.ERROR, "Exception while update user state method ");
            throw new DaoException("Exception while update user state method ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
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
            return true;
        }
    }

    @Override
    public List<User> findAllClients() throws DaoException {
        List<User> userList = new ArrayList<>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_ALL_CLIENTS);
            rs = st.executeQuery();
            while (rs.next()) {
                long id = rs.getInt(USER_ID);
                String userPassword = rs.getString(USER_PASSWORD);
                String userLogin = rs.getString(USER_LOGIN);
                boolean userActive = rs.getBoolean(USER_ACTIVE);
                String userRole = rs.getString("role_name");
                User user = new User(id, userRole.toUpperCase(), userLogin, userPassword, userActive);

                if (!userList.contains(user)) {
                    userList.add(user);
                }

            }

            logger.log(Level.INFO, "List: " + userList);
        } catch (SQLException | ConnectionPoolException e) {
            logger.log(Level.ERROR, "Exception while find all clients method ");
            throw new DaoException("Exception in a findAllAdmins method", e);
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
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
            return userList;
        }

    }

    @Override
    public List<User> findAllAdmins() throws DaoException {
        List<User> userList = new ArrayList<>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_ALL_ADMINS);
            rs = st.executeQuery();
            while (rs.next()) {
                long id = rs.getInt(USER_ID);
                String userPassword = rs.getString(USER_PASSWORD);
                String userLogin = rs.getString(USER_LOGIN);
                boolean userActive = rs.getBoolean(USER_ACTIVE);
                String userRole = rs.getString("role_name");
                User user = new User(id, userRole.toUpperCase(), userLogin, userPassword, userActive);

                if (!userList.contains(user)) {
                    userList.add(user);
                }

            }

            logger.log(Level.INFO, "List: " + userList);
        } catch (SQLException | ConnectionPoolException e) {
            logger.log(Level.ERROR, "Exception while find all clients method ");
            throw new DaoException("Exception in a findAllAdmins method", e);
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
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
            return userList;
        }
    }


    public static void main(String[] args) {
//        вход пользователя
//        User user=null;
//        try {
//            UserDaoImpl userDaoImpl = new UserDaoImpl();
//            user=userDaoImpl.authorization("Anna12", "63aa44c55315c3a152719a88d0064922");
//        }catch (DaoException ex){}
//        System.out.println(user.toString());

//        регистрация пользователя
//        User user=new User("CLIENT","user5","jxjzgjfh",true);
//        try {
//            UserDaoImpl userDaoImpl = new UserDaoImpl();
//            System.out.println(userDaoImpl.registration(user));
//        }catch (Exception ex){}

//        получить пользователя по id
//        User user=null;
//        try {
//            UserDaoImpl userDaoImpl = new UserDaoImpl();
//            user=userDaoImpl.getUserID(2);
//        }catch (DaoException ex){}
//        System.out.println(user.toString());

        //сменить пароль
//        try {
//            UserDaoImpl userDaoImpl = new UserDaoImpl();
//            System.out.println(userDaoImpl.changePassword("777777777777777",
//                    "77777777777709",2L));
//        }catch (DaoException ex){}

        //блокировка пользователя
//        try {
//            UserDaoImpl userDaoImpl = new UserDaoImpl();
//            System.out.println(userDaoImpl.updateUserState(3,false));
//        }catch (DaoException ex){}

        //найти всех клинтов
//        try {
//            UserDaoImpl userDaoImpl = new UserDaoImpl();
//            System.out.println(userDaoImpl.findAllClients().toString());
//        }catch (DaoException ex){}

        // найти всех админов
        try {
            UserDaoImpl userDaoImpl = new UserDaoImpl();
            System.out.println(userDaoImpl.findAllAdmins().toString());
        }catch (DaoException ex){}
    }
}
