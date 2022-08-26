package by.zborovskaya.final_project.dao.impl;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.dao.ProductDao;
import by.zborovskaya.final_project.dao.pool.ConnectionPool;
import by.zborovskaya.final_project.dao.pool.ConnectionPoolException;
import by.zborovskaya.final_project.entity.Product;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private static final Logger logger = LogManager.getLogger(ProductDaoImpl.class);
    private final static ConnectionPool connectionPool = ConnectionPool.getInstance();

    private final static String PRODUCT_ID = "product_id";
    private final static String NAME_PRODUCT = "name_product";
    private final static String PICTURE_PATH = "picture_path";
    private final static String COMPOSITION = "composition";
    private final static String WEIGHT = "weight";
    private final static String BREWING_TIME = "brewing_time";
    private final static String WATER_TEMPERATURE = "water_temperature";
    private final static String PRICE = "price";
    private final static String IS_ACTIVE = "is_active";
    private final static String CATEGORY_ID = "category_id";

    private final static String FATAL_ERROR_RESULT_SET = "Fatal error closing resultSet";
    private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";

    private static final String SQL_SELECT_ALL_PRODUCTS = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE is_active = true";
    private static final String SQL_SELECT_PRODUCTS_BY_CATEGORY = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE category_id = (?) AND is_active = true";
    private static final String SEARCH_PRODUCTS_BY_NAME = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE name_product LIKE CONCAT( '%',?,'%') AND is_active = true";
    private static final String SEARCH_PRODUCTS_BY_NAME_AND_CATEGORY = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE name_product LIKE CONCAT( '%',?,'%') AND category_id = (?) AND is_active = true";
    private static final String SQL_SELECT_BLOCKED_PRODUCTS = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE is_active = false";
    private static final String SQL_SELECT_PRODUCTS_BY_ID = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE product_id = (?)";
    private static final String SQL_INSERT_NEW_PRODUCTS_ITEM = "" +
            "INSERT INTO products(name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_PRODUCTS_ITEM = "UPDATE products " +
            "SET is_active = false " +
            "WHERE product_id = (?)";
    private static final String SQL_RESTORE_PRODUCTS_ITEM = "UPDATE products " +
            "SET is_active = true " +
            "WHERE product_id = (?)";
    private static final String SQL_DELETE_PRODUCTS_BY_CATEGORY_ID = "UPDATE products " +
            "SET is_active = false " +
            "WHERE category_id = (?)";
    private static final String SQL_UPDATE_PRODUCTS = "UPDATE products SET name_product = (?), picture_path = (?)," +
            " composition = (?), weight = (?), brewing_time = (?), water_temperature = (?), " +
            "price = (?), category_id = (?) " +
            "WHERE product_id = (?)";
    private static final String SQL_UPDATE_IMAGE_PATH_BY_NAME = "UPDATE products " +
            "SET picture_path = (?) WHERE product_id = (?)";
    private static final String SQL_SELECT_PRODUCTS_BY_NAME = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE name_product = (?)";
    private static final String SQL_FIND_PRODUCTS_SUBLIST_BY_SECTION_ID = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE category_id = (?) AND is_active = true " +
            "LIMIT ? OFFSET ?";
    private static final String SQL_SELECT_ALL_MENU_ROW_COUNT = "SELECT COUNT(*) FROM products " +
            "WHERE is_active = true";
    private static final String SQL_SELECT_PRODUCTS_SUBLIST ="SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE is_active = true " +
            "LIMIT ? OFFSET ?";
    private static final String SQL_SELECT_ALL_SORTED_PRODUCTS = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE is_active = true " +
            "ORDER BY price" +
            "LIMIT ? OFFSET ?";
    private static final String SQL_SELECT_SORTED_CATEGORY_PRODUCTS = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products " +
            "WHERE category_id = (?) AND is_active = true " +
            "ORDER BY price" +
            "LIMIT ? OFFSET ?";
    private static final String SQL_SELECT_PRODUCTS_ROW_COUNT_BY_SECTION_ID = "SELECT COUNT(*) " +
            "FROM products WHERE category_id = (?) AND is_active = true";
    private static final String SQL_SELECT_ALL_BLOCKED_UNBLOCKED_PRODUCTS = "SELECT product_id, name_product, picture_path, " +
            "composition, weight," +
            "brewing_time, water_temperature, price, is_active, category_id FROM products";
    private static final String SQL_UPDATE_USER_STATE_BY_ID = "UPDATE users " +
            "SET is_active = (?) WHERE user_id = (?)";
    private static final String SQL_UPDATE_PRODUCTS_STATE_BY_ID = "UPDATE product " +
            "SET is_active = (?) " +
            "WHERE product_id= (?)";

    @Override
    public List<Product> searchByName(String pattern) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Product> products = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SEARCH_PRODUCTS_BY_NAME);
            st.setString(1, pattern);
            rs = st.executeQuery();

            products = new ArrayList<Product>();
            while(rs.next()) {
                int id_product = rs.getInt(PRODUCT_ID);
                int category_id = rs.getInt(CATEGORY_ID);
                String name_product = rs.getString(NAME_PRODUCT);
                String picture_path = rs.getString(PICTURE_PATH);
                String composition = rs.getString(COMPOSITION);
                BigDecimal weight = rs.getBigDecimal(WEIGHT);
                LocalTime brewing_time = rs.getTime(BREWING_TIME).toLocalTime();
                int water_temperature = rs.getInt(WATER_TEMPERATURE);
                BigDecimal price = rs.getBigDecimal(PRICE);
                boolean isActive = rs.getBoolean(IS_ACTIVE);

                Product product = new Product(id_product, category_id,name_product,picture_path,
                        composition, weight, water_temperature,brewing_time, price,isActive);

                if(!products.contains(product)){
                    products.add(product);
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
//                try{
//                    connectionPool.destroy();
//                }catch (Exception e){}
            }
        }
        return products;
    }

    @Override
    public List<Product> searchByNameAndCategory(String pattern,long categoryId) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Product> products = null;
        try {
            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SEARCH_PRODUCTS_BY_NAME_AND_CATEGORY);
            st.setString(1, pattern);
            st.setLong(2, categoryId);
            rs = st.executeQuery();

            products = new ArrayList<Product>();
            while(rs.next()) {
                int id_product = rs.getInt(PRODUCT_ID);
                int category_id = rs.getInt(CATEGORY_ID);
                String name_product = rs.getString(NAME_PRODUCT);
                String picture_path = rs.getString(PICTURE_PATH);
                String composition = rs.getString(COMPOSITION);
                BigDecimal weight = rs.getBigDecimal(WEIGHT);
                LocalTime brewing_time = rs.getTime(BREWING_TIME).toLocalTime();
                int water_temperature = rs.getInt(WATER_TEMPERATURE);
                BigDecimal price = rs.getBigDecimal(PRICE);
                boolean isActive = rs.getBoolean(IS_ACTIVE);

                Product product = new Product(id_product, category_id,name_product,picture_path,
                        composition, weight, water_temperature,brewing_time, price,isActive);

                if(!products.contains(product)){
                    products.add(product);
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
                    connectionPool.destroy();
                }catch (Exception e){}
            }
        }
        return products;
    }

    @Override
    public List<Product> findAll() throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Product> products = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_ALL_PRODUCTS);
            rs = st.executeQuery();

            products = new ArrayList<Product>();
            while(rs.next()) {
                int id_product = rs.getInt(PRODUCT_ID);
                int category_id = rs.getInt(CATEGORY_ID);
                String name_product = rs.getString(NAME_PRODUCT);
                String picture_path = rs.getString(PICTURE_PATH);
                String composition = rs.getString(COMPOSITION);
                BigDecimal weight = rs.getBigDecimal(WEIGHT);
                LocalTime brewing_time = rs.getTime(BREWING_TIME).toLocalTime();
                int water_temperature = rs.getInt(WATER_TEMPERATURE);
                BigDecimal price = rs.getBigDecimal(PRICE);
                boolean isActive = rs.getBoolean(IS_ACTIVE);

                Product product = new Product(id_product, category_id,name_product,picture_path,
                        composition, weight, water_temperature,brewing_time, price,isActive);

                if(!products.contains(product)){
                    products.add(product);
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
//                try{
//                    connectionPool.destroy();
//                }catch (Exception e){}

            }
        }

        return products;
    }

    @Override
    public List<Product> findProductsByCategory(long categoryId) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Product> products = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_PRODUCTS_BY_CATEGORY);
            st.setLong(1, categoryId);
            rs = st.executeQuery();

            products = new ArrayList<Product>();
            while(rs.next()) {
                int id_product = rs.getInt(PRODUCT_ID);
                int category_id = rs.getInt(CATEGORY_ID);
                String name_product = rs.getString(NAME_PRODUCT);
                String picture_path = rs.getString(PICTURE_PATH);
                String composition = rs.getString(COMPOSITION);
                BigDecimal weight = rs.getBigDecimal(WEIGHT);
                LocalTime brewing_time = rs.getTime(BREWING_TIME).toLocalTime();
                int water_temperature = rs.getInt(WATER_TEMPERATURE);
                BigDecimal price = rs.getBigDecimal(PRICE);
                boolean isActive = rs.getBoolean(IS_ACTIVE);

                Product product = new Product(id_product, category_id,name_product,picture_path,
                        composition, weight, water_temperature,brewing_time, price,isActive);

                if(!products.contains(product)){
                    products.add(product);
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
//                try{
//                    connectionPool.destroy();
//                }catch (Exception e){}

            }
        }
        return products;
    }

    @Override
    public List<Product> findBlockedProducts() throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Product> products = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_BLOCKED_PRODUCTS);
            rs = st.executeQuery();

            products = new ArrayList<>();
            while(rs.next()) {
                int id_product = rs.getInt(PRODUCT_ID);
                int category_id = rs.getInt(CATEGORY_ID);
                String name_product = rs.getString(NAME_PRODUCT);
                String picture_path = rs.getString(PICTURE_PATH);
                String composition = rs.getString(COMPOSITION);
                BigDecimal weight = rs.getBigDecimal(WEIGHT);
                LocalTime brewing_time = rs.getTime(BREWING_TIME).toLocalTime();
                int water_temperature = rs.getInt(WATER_TEMPERATURE);
                BigDecimal price = rs.getBigDecimal(PRICE);
                boolean isActive = rs.getBoolean(IS_ACTIVE);

                Product product = new Product(id_product, category_id,name_product,picture_path,
                        composition, weight, water_temperature,brewing_time, price,isActive);

                if(!products.contains(product)){
                    products.add(product);
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

        return products;
    }

    @Override
    public boolean blockedProductById(long id) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_DELETE_PRODUCTS_ITEM);
            st.setLong(1, id);
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
//                try{
//                    connectionPool.destroy();
//                }catch (Exception e){}
            }
        }

        return true;
    }
    @Override
    public boolean unBlockedProductById(long id) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_RESTORE_PRODUCTS_ITEM);
            st.setLong(1, id);
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
//                try{
//                    connectionPool.destroy();
//                }catch (Exception e){}
            }
        }
        return true;
    }

    @Override
    public boolean create(Product product) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_INSERT_NEW_PRODUCTS_ITEM);
            st.setString(1, product.getName());
            st.setString(2, product.getPicturePath());
            st.setString(3, product.getComposition());
            st.setBigDecimal(4, product.getWeight());
            st.setTime(5, Time.valueOf(product.getBrewing_time()));
            st.setInt(6, product.getWater_temperature());
            st.setBigDecimal(7, product.getPrice());
            st.setBoolean(8,product.isActive());
            st.setLong(9, product.getCategory_id());
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
//                try{
//                    connectionPool.destroy();
//                }catch (Exception e){}
            }
        }

        return true;
    }

    @Override
    public boolean update(Product product) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();
            st = con.prepareStatement(SQL_UPDATE_PRODUCTS);
            st.setString(1, product.getName());
            st.setString(2, product.getPicturePath());
            st.setString(3, product.getComposition());
            st.setBigDecimal(4, product.getWeight());
            st.setTime(5, Time.valueOf(product.getBrewing_time()));
            st.setInt(6, product.getWater_temperature());
            st.setBigDecimal(7, product.getPrice());
            st.setLong(8, product.getCategory_id());
            st.setLong(9, product.getId());
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
                try {
//                    connectionPool.destroy();
                } catch (Exception e) {
                }

            }
        }
        return true;
    }

    @Override
    public Product findProductById(long id) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Product product = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_PRODUCTS_BY_ID);
            st.setLong(1, id);
            rs = st.executeQuery();

            while (rs.next()) {
                int id_product = rs.getInt(PRODUCT_ID);
                int category_id = rs.getInt(CATEGORY_ID);
                String name_product = rs.getString(NAME_PRODUCT);
                String picture_path = rs.getString(PICTURE_PATH);
                String composition = rs.getString(COMPOSITION);
                BigDecimal weight = rs.getBigDecimal(WEIGHT);
                LocalTime brewing_time = rs.getTime(BREWING_TIME).toLocalTime();
                int water_temperature = rs.getInt(WATER_TEMPERATURE);
                BigDecimal price = rs.getBigDecimal(PRICE);
                boolean isActive = rs.getBoolean(IS_ACTIVE);

                product = new Product(id_product, category_id,name_product,picture_path,
                        composition, weight, water_temperature,brewing_time, price,isActive);
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
//                try {
//                    connectionPool.destroy();
//                } catch (Exception e) {
//                }
            }
        }
        return product;
    }

    public static void main(String[] args) {
        ProductDao productDao = new ProductDaoImpl();
//        Product product = new Product(3,2,"1001 Ночь","http://bestea.by/image/cachewebp/catalog/product/green/5-700x700.webp",
//                "чай черный индийский, чай зеленый китайский Сенча, кусочки ананаса",
//                BigDecimal.valueOf(25),95, LocalTime.of(0,3),
//                BigDecimal.valueOf(2.90),true);
        try{
//            System.out.println(productDao.findAll());
//            productDao.update(product);
//            productDao.create(product);
//            productDao.unBlockedProductById(4L);
//            System.out.println(productDao.searchByName("печенье"));
            System.out.println(productDao.searchByNameAndCategory("печенье",1));
//            System.out.println( productDao.findProductById(1L));
        }catch (Exception e){}
    }
}
