package by.zborovskaya.final_project.service;

import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.entity.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll() throws ServiceException;

    public Category findEntityById(long id) throws ServiceException;

    public boolean blockedCategoryById(long id) throws ServiceException;

    public boolean create(Category category) throws ServiceException;

    public boolean update(Category category) throws ServiceException;

    public Category findCategoryByName(String categoryName) throws ServiceException;

    public List<Category> findAllBlockedCategories() throws ServiceException;

    public boolean unblockedCategoryById(long id) throws ServiceException;
}
