package by.zborovskaya.final_project.dao;

import by.zborovskaya.final_project.entity.CartItem;
import by.zborovskaya.final_project.entity.Order;

import java.util.List;

public interface OrderDao {
    List<Order> findAll() throws DaoException;
    Order getOrderById(Long id) throws DaoException;
    long save(Order objectDao) throws DaoException;
    List<CartItem>  getCartItemsByOrderId(long id) throws DaoException;
    List<Order> findOrdersByUserId (long id) throws DaoException;
    void changeOrderStatus (long orderId, String status) throws DaoException;
}
