package by.zborovskaya.final_project.service.impl;

import by.zborovskaya.final_project.dao.CategoryDao;
import by.zborovskaya.final_project.dao.DaoException;
import by.zborovskaya.final_project.dao.DaoFactory;
import by.zborovskaya.final_project.entity.Category;
import by.zborovskaya.final_project.service.CategoryService;
import by.zborovskaya.final_project.service.ServiceException;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<Category> findAll() throws ServiceException {
        DaoFactory daoFactory=DaoFactory.getInstance();
        CategoryDao categoryDao = daoFactory.getCategoryDao();
        List<Category> categoryList;
        try {
            categoryList = categoryDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return categoryList;
    }

    @Override
    public Category findEntityById(long id) throws ServiceException {
        return null;
    }

    @Override
    public boolean blockedCategoryById(long id) throws ServiceException {
        return false;
    }

    @Override
    public boolean create(Category category) throws ServiceException {
        return false;
    }

    @Override
    public boolean update(Category category) throws ServiceException {
        return false;
    }

    @Override
    public Category findCategoryByName(String categoryName) throws ServiceException {
        return null;
    }

    @Override
    public List<Category> findAllBlockedCategories() throws ServiceException {
        return null;
    }

    @Override
    public boolean unblockedCategoryById(long id) throws ServiceException {
        return false;
    }
}
