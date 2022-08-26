package by.zborovskaya.final_project.dao;

import by.zborovskaya.final_project.entity.Product;

import java.util.List;

public interface ProductDao {
    public List<Product> findAll() throws DaoException;
    public List<Product> findProductsByCategory(long categoryId) throws DaoException;
    public boolean blockedProductById(long id) throws DaoException;
    public boolean unBlockedProductById(long id) throws DaoException;
    public boolean create(Product product) throws DaoException;
    public boolean update(Product product) throws DaoException;
    public Product findProductById(long id) throws DaoException;
    public List<Product> findBlockedProducts() throws DaoException;
    public List<Product> searchByName(String pattern) throws DaoException;
    public List<Product> searchByNameAndCategory(String pattern,long categoryId) throws DaoException;
}
