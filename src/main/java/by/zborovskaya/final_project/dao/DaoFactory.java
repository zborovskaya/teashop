package by.zborovskaya.final_project.dao;

import by.zborovskaya.final_project.dao.impl.*;

public class DaoFactory {
    private static final DaoFactory instance = new DaoFactory();

    private final UserDao userDao = new UserDaoImpl();
    private final UserInfoDao userInfoDao = new UserInfoDaoImpl();
    private final ProductDao productDao = new ProductDaoImpl();
    private final CategoryDao categoryDao = new CategoryDaoImpl();
    private final OrderDao orderDao = new OrderDaoImpl();


    private DaoFactory() {}

    public static DaoFactory getInstance() {
        return instance;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }
}
