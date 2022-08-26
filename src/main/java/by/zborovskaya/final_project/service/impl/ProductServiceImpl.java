package by.zborovskaya.final_project.service.impl;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.dao.DaoFactory;
import by.zborovskaya.final_project.dao.ProductDao;
import by.zborovskaya.final_project.entity.Product;
import by.zborovskaya.final_project.service.ProductService;
import by.zborovskaya.final_project.service.ServiceException;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final static ProductDao productDao= DaoFactory.getInstance().getProductDao();
    @Override
    public List<Product> findAll() throws ServiceException {

        List<Product> products;
        try {
            products = productDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return products;
    }

    @Override
    public List<Product> findProductsByCategory(long categoryId) throws ServiceException {
        List<Product> products;
        try {
            products = productDao.findProductsByCategory(categoryId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return products;
    }

    @Override
    public boolean blockedProductById(long id) throws ServiceException {
       boolean isBlocked = false;
        try {
            isBlocked = productDao.blockedProductById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return isBlocked;

    }

    @Override
    public boolean unBlockedProductById(long id) throws ServiceException {
        boolean isUnBlocked = false;
        try {
            isUnBlocked = productDao.unBlockedProductById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return isUnBlocked;
    }

    @Override
    public List<Product> searchByName(String pattern) throws ServiceException {
        List<Product> products;
        try {
            products = productDao.searchByName(pattern);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return products;
    }

    @Override
    public List<Product> searchByNameAndCategory(String pattern, long categoryId) throws ServiceException {
        List<Product> products;
        try {
            products = productDao.searchByNameAndCategory(pattern,categoryId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return products;
    }

    @Override
    public boolean create(Product product) throws ServiceException {
        boolean created = false;
        try {
            created = productDao.create(product);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return created;
    }

    @Override
    public boolean update(Product product) throws ServiceException {
        boolean updated = false;
        try {
            updated = productDao.update(product);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return updated;
    }

    @Override
    public Product findProductById(long id) throws ServiceException {
        Product product = null;
        try{
            product = productDao.findProductById(id);
        }catch(DaoException e) {
            throw new ServiceException(e);
        }

        return product;
    }

    @Override
    public List<Product> findBlockedProducts() throws ServiceException {
        List<Product> products;
        try {
            products = productDao.findBlockedProducts();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return products;
    }
}
