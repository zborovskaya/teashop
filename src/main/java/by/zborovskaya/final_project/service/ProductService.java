package by.zborovskaya.final_project.service;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.entity.Product;

import java.util.List;

public interface ProductService {
    public List<Product> findAll() throws ServiceException;
    public List<Product> findProductsByCategory(long categoryId) throws ServiceException;
    public boolean blockedProductById(long id) throws ServiceException;
    public boolean unBlockedProductById(long id) throws ServiceException;

    public boolean create(Product product) throws ServiceException;

    public boolean update(Product product) throws ServiceException;

    public Product findProductById(long id) throws ServiceException;
    public List<Product> findBlockedProducts() throws ServiceException;
    public List<Product> searchByName(String pattern) throws ServiceException;
    public List<Product> searchByNameAndCategory(String pattern,long categoryId) throws ServiceException;
}
