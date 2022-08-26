package by.zborovskaya.final_project.service.impl;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.dao.DaoFactory;
import by.zborovskaya.final_project.dao.OrderDao;
import by.zborovskaya.final_project.entity.*;
import by.zborovskaya.final_project.service.OrderService;
import by.zborovskaya.final_project.service.ProductService;
import by.zborovskaya.final_project.service.ServiceException;
import by.zborovskaya.final_project.service.ServiceFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private static final OrderService instance = new OrderServiceImpl();
    private static final OrderDao orderDao = DaoFactory.getInstance().getOrderDao();
    private final ProductService productService = ServiceFactory.getInstance().getProductService();

    private OrderServiceImpl() {
    }

    @Override
    public List<Order> findAll() throws ServiceException {
        List<Order> orders;
        try {
            orders = orderDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return orders;
    }

    @Override
    public void changeOrderStatus(long orderId, String status) throws ServiceException {
        try {
            orderDao.changeOrderStatus(orderId,status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    public static OrderService getInstance() {
        return instance;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems()
                .stream().map(item -> {
                    try {
                        return item.clone();
                    } catch (CloneNotSupportedException ex) {
                        throw new RuntimeException(ex);
                    }
                }).collect(Collectors.toList()));
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setTotalCost(cart.getTotalCost());
        return order;
    }

    @Override
    public void placeOrder(Order order){
    try{
        long id =orderDao.save(order);
        order.setId(id);
    }catch(DaoException ex){}
    }

    @Override
    public Order getOrderById(Long id) throws ServiceException {
        Order order= null;
        try{
            order = orderDao.getOrderById(id);
        }catch(DaoException e) {
            throw new ServiceException(e);
        }

        return order;
    }

    @Override
    public List<CartItem> getCartItemsByOrderId(long id) throws ServiceException {
        List<CartItem> cartItemList;
        try {
            cartItemList = orderDao.getCartItemsByOrderId(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return cartItemList;
    }

    @Override
    public List<Order> findOrdersByUserId(long id) throws ServiceException {
        List<Order> orders;
        try {
            orders = orderDao.findOrdersByUserId(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return orders;
    }

    @Override
    public void calculateTotalCost(Order order) throws ServiceException {
        if (order.getItems().size() == 0) {
            order.setTotalCost(null);
        } else {
            List<CartItem> cartItemList = order.getItems();
            BigDecimal cost = BigDecimal.valueOf(0);
            for (CartItem item : cartItemList) {
                try {
                    cost = cost.add( productService.findProductById(item.getProductId()).getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity())));
                }catch (ServiceException ex){}
            }
            order.setTotalCost(cost);
        }
    }

    @Override
    public void calculateTotalQuantity(Order order) throws ServiceException {
        if (order.getItems().size() == 0) {
            order.setTotalQuantity(0);
        } else {
            order.setTotalQuantity(order.getItems()
                    .stream()
                    .map(CartItem::getQuantity)
                    .collect(Collectors.summingInt(q -> q.intValue())));
        }
    }
}
