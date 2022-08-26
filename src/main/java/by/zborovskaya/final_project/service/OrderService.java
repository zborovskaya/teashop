package by.zborovskaya.final_project.service;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.entity.Cart;
import by.zborovskaya.final_project.entity.CartItem;
import by.zborovskaya.final_project.entity.Order;
import by.zborovskaya.final_project.entity.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order getOrder(Cart cart);

    List<PaymentMethod> getPaymentMethods();

    void placeOrder(Order order);

    Order getOrderById(Long id) throws ServiceException;

    List<CartItem>  getCartItemsByOrderId(long id) throws ServiceException;

    List<Order> findOrdersByUserId (long id) throws ServiceException;

    List<Order> findAll() throws ServiceException;

    void calculateTotalCost(Order order) throws ServiceException;

    void calculateTotalQuantity( Order order) throws ServiceException;
    void changeOrderStatus (long orderId, String status) throws ServiceException;
}
