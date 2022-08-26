package by.zborovskaya.final_project.dao.impl;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.dao.OrderDao;
import by.zborovskaya.final_project.dao.pool.ConnectionPool;
import by.zborovskaya.final_project.dao.pool.ConnectionPoolException;
import by.zborovskaya.final_project.entity.CartItem;
import by.zborovskaya.final_project.entity.Order;
import by.zborovskaya.final_project.entity.PaymentMethod;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static final Logger logger = LogManager.getLogger(OrderDaoImpl.class);
    private final static ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String ORDER_ID="order_id";
    private static final String ORDER_DATE="order_date";
    private static final String PAYMENT_METHOD="payment_method";
    private static final String USER_ID="user_id";
    private static final String PRODUCT_ID="product_id";
    private static final String ADDRESS="address";
    private static final String TOTAL_COST="total_cost";
    private static final String QUANTITY="quantity";
    private static final String STATUS="order_status";

    private final static String FATAL_ERROR_RESULT_SET = "Fatal error closing resultSet";
    private final static String FATAL_ERROR_PREPARED_STATEMENT = "Fatal error closing preparedStatement";


    private static final String SQL_SELECT_ALL_ORDERS = "SELECT orders.order_id, order_date, payment_method, " +
            "user_id, address," +
            "total_cost, order_status FROM orders ";

    private static final String SQL_SELECT_ORDERS_BY_USER_ID = "SELECT orders.order_id, order_date, payment_method, " +
            "user_id, address," +
            "total_cost, order_status FROM orders "+
            "WHERE user_id = (?)";

    private static final String SQL_SELECT_ALL_ORDERS_PRODUCTS_BY_ORDER_ID = "SELECT order_id, product_id, quantity " +
            "FROM orders_products " +
            "WHERE order_id = (?)";

    private static final String SQL_SELECT_ORDERS_BY_ID = "SELECT order_id, order_date, payment_method, " +
            "user_id, address," +
            "total_cost,order_status  FROM orders " +
            "WHERE order_id = (?)";

    private static final String SQL_SELECT_ORDERS_PRODUCTS_BY_ID = "SELECT order_id, product_id, quantity " +
            "FROM orders_products " +
            "WHERE order_id = (?)";

    private static final String SQL_INSERT_NEW_ORDERS_ITEM = "" +
            "INSERT INTO orders(order_date, payment_method," +
            "user_id, address," +
            "total_cost,order_status )" +
            "VALUES (?, ?, ?, ?, ?,?)";

    private static final String SQL_INSERT_NEW_ORDERS_PRODUCTS_ITEM = "" +
            "INSERT INTO orders_products(order_id, product_id, quantity)" +
            "VALUES (?, ?, ?)";

    private static final String SQL_UPDATE_ORDER_STATUS = "UPDATE orders " +
            "SET order_status = (?) " +
            "WHERE order_id = (?)";

    @Override
    public Order getOrderById(Long id) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Order order = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_ORDERS_BY_ID);
            st.setLong(1, id);
            rs = st.executeQuery();

            while (rs.next()) {
                long id_order = rs.getLong(ORDER_ID);
                long user_id = rs.getLong(USER_ID);
                String paymentMethod = rs.getString(PAYMENT_METHOD);
                String address = rs.getString(ADDRESS);
                BigDecimal totalCost = rs.getBigDecimal(TOTAL_COST);
                LocalDate order_date = rs.getDate(ORDER_DATE).toLocalDate();
                String status = rs.getString(STATUS);

                order = new Order(id_order,user_id,totalCost,address,order_date,
                        PaymentMethod.valueOf(paymentMethod.toUpperCase()));
                order.setStatus(status);
            }

            List<CartItem> itemList = new ArrayList<>();

            st = con.prepareStatement(SQL_SELECT_ORDERS_PRODUCTS_BY_ID);
            st.setLong(1, id);
            rs = st.executeQuery();
            while (rs.next()) {
                long product_id = rs.getLong(PRODUCT_ID);
                int quantity = rs.getInt(QUANTITY);
                CartItem cartItem = new CartItem(product_id,quantity);
                itemList.add(cartItem);
            }
            order.setItems(itemList);
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
        return order;
    }

    @Override
    public long save(Order order) throws DaoException{
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        long idOrder = 0l;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_INSERT_NEW_ORDERS_ITEM,Statement.RETURN_GENERATED_KEYS);
            st.setDate(1, Date.valueOf(order.getDeliveryDate()));
            st.setString(2, order.getPaymentMethod().toString());
            st.setLong(3, order.getId_user());
            st.setString(4, order.getDeliveryAddress());
            st.setBigDecimal(5, order.getTotalCost());
            st.setString(6,order.getStatus());

            st.executeUpdate();

            try(ResultSet resultSet = st.getGeneratedKeys()) {
                resultSet.next();
                idOrder = resultSet.getLong(1);
            }
            List<CartItem> cartItemList = order.getItems();
            for(CartItem cartItem : cartItemList){
                st = con.prepareStatement(SQL_INSERT_NEW_ORDERS_PRODUCTS_ITEM);
                st.setLong(1,idOrder);
                st.setLong(2,cartItem.getProductId());
                st.setInt(3,cartItem.getQuantity());
                st.executeUpdate();
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
//                try{
//                    connectionPool.destroy();
//                }catch (Exception e){}
            }
        }
        return idOrder;
    }

    @Override
    public List<Order> findAll() throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Order> orders = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_ALL_ORDERS);
            rs = st.executeQuery();

            orders = new ArrayList<>();
            while(rs.next()) {
                long id_order = rs.getLong(ORDER_ID);
                long user_id = rs.getLong(USER_ID);
                String paymentMethod = rs.getString(PAYMENT_METHOD);
                String address = rs.getString(ADDRESS);
                BigDecimal totalCost = rs.getBigDecimal(TOTAL_COST);
                LocalDate order_date = rs.getDate(ORDER_DATE).toLocalDate();
                String status = rs.getString(STATUS);

                Order order = new Order(id_order,user_id,totalCost,address,order_date,
                        PaymentMethod.valueOf(paymentMethod.toUpperCase()));
                order.setStatus(status);
                if(!orders.contains(order)){
                    orders.add(order);
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
        return orders;
    }

    @Override
    public void changeOrderStatus(long orderId, String status) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_UPDATE_ORDER_STATUS);
            st.setString(1, status);
            st.setLong(2, orderId);
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
    }

    @Override
    public List<CartItem> getCartItemsByOrderId(long id) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<CartItem> cartItemsList = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_ALL_ORDERS_PRODUCTS_BY_ORDER_ID);
            st.setLong(1, id);
            rs = st.executeQuery();

            cartItemsList = new ArrayList<>();

            while (rs.next()) {
                long id_order = rs.getLong(ORDER_ID);
                long product_id = rs.getLong(PRODUCT_ID);
                int quantity = rs.getInt(QUANTITY);
                CartItem cartItem =  new CartItem(product_id,quantity);
                if(!cartItemsList.contains(cartItem)){
                    cartItemsList.add(cartItem);
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
        return cartItemsList;
    }

    @Override
    public List<Order> findOrdersByUserId(long id) throws DaoException {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Order> orders = null;
        try {
//            connectionPool.init();
            con = connectionPool.takeConnection();

            st = con.prepareStatement(SQL_SELECT_ORDERS_BY_USER_ID);
            st.setLong(1, id);
            rs = st.executeQuery();

            orders = new ArrayList<>();
            while(rs.next()) {
                long id_order = rs.getLong(ORDER_ID);
                long user_id = rs.getLong(USER_ID);
                String paymentMethod = rs.getString(PAYMENT_METHOD);
                String address = rs.getString(ADDRESS);
                BigDecimal totalCost = rs.getBigDecimal(TOTAL_COST);
                LocalDate order_date = rs.getDate(ORDER_DATE).toLocalDate();
                String status = rs.getString(STATUS);

                Order order = new Order(id_order,user_id,totalCost,address,order_date,
                        PaymentMethod.valueOf(paymentMethod.toUpperCase()));
                order.setStatus(status);
                if(!orders.contains(order)){
                    orders.add(order);
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
        return orders;
    }
}
