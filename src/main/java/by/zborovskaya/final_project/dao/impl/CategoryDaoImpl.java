package by.zborovskaya.final_project.dao.impl;

import by.zborovskaya.final_project.dao.CategoryDao;
import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.dao.pool.ConnectionPool;
import by.zborovskaya.final_project.dao.pool.ConnectionPoolException;
import by.zborovskaya.final_project.entity.Category;
import by.zborovskaya.final_project.entity.Product;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private static final Logger logger = LogManager.getLogger(CategoryDao.class);

    private final static ConnectionPool connectionPool = ConnectionPool.getInstance();

    private final static String CATEGORY_ID = "category_id";
    private final static String NAME_CATEGORY = "name_category";
    private final static String IS_ACTIVE = "is_active";

    private final static String FATAL_ERROR_RESULT_SET = "Fatal error closing resultSet";
    private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";

    private static final String SQL_SELECT_ALL_CATEGORIES = "SELECT category_id, name_category, is_active " +
            "FROM category " +
            "WHERE is_active = true";
    private static final String SQL_INSERT_NEW_CATEGORY = "INSERT INTO category(name_category, is_active) " +
            "VALUES (?, ?)";
    private static final String SQL_SELECT_CATEGORY_BY_NAME = "SELECT category_id, name_category, is_active " +
            "FROM category " +
            "WHERE name_category = (?)";
    private static final String SQL_SELECT_CATEGORY_BY_ID ="SELECT category_id, name_category, is_active " +
            "FROM category " +
            "WHERE category_id, = (?)";
    private static final String SQL_UPDATE_CATEGORY_NAME = "UPDATE category " +
            "SET name_category = (?) " +
            "WHERE category_id = (?)";
    private static final String SQL_BLOCKED_CATEGORY_BY_ID = "UPDATE category " +
            "SET is_active = false " +
            "WHERE category_id = (?)";
    private static final String SQL_SELECT_ALL_BLOCKED_CATEGORIES = "SELECT category_id, name_category, is_active " +
            "FROM category " +
            "WHERE is_active = false";
    private static final String SQL_UNBLOCKED_CATEGORY_BY_ID = "UPDATE category " +
            "SET is_active = true " +
            "WHERE category_id = (?)";
    @Override
    public List<Category> findAll() throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Category> categories = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_ALL_CATEGORIES);
            rs = st.executeQuery();

            categories = new ArrayList<Category>();
            while(rs.next()) {
                int category_id = rs.getInt(CATEGORY_ID);
                String name_category = rs.getString(NAME_CATEGORY);
                boolean isActive = rs.getBoolean(IS_ACTIVE);

                Category category = new Category(category_id, name_category,isActive);
                if(!categories.contains(category)){
                    categories.add(category);
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            logger.error(e.getMessage());
            throw new DaoException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }catch (SQLException e){
                    logger.log(Level.FATAL, FATAL_ERROR_RESULT_SET, e);
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
                try{
//                    connectionPool.destroy();
                }catch (Exception e){}

            }
        }

        return categories;
    }

    @Override
    public Category findEntityById(long id) throws DaoException {
        return null;
    }

    @Override
    public boolean blockedCategoryById(long id) throws DaoException {
        return false;
    }

    @Override
    public boolean create(Category category) throws DaoException {
        return false;
    }

    @Override
    public boolean update(Category category) throws DaoException {
        return false;
    }

    @Override
    public Category findCategoryByName(String categoryName) throws DaoException {
        return null;
    }

    @Override
    public List<Category> findAllBlockedCategories() throws DaoException {
        return null;
    }

    @Override
    public boolean unblockedCategoryById(long id) throws DaoException {
        return false;
    }

    public static void main(String[] args) {
        CategoryDao categoryDao = new CategoryDaoImpl();
        try{
            System.out.println(categoryDao.findAll());
        }catch (Exception e){}
    }
}
