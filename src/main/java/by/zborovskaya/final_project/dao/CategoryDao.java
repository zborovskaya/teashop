package by.zborovskaya.final_project.dao;

import by.zborovskaya.final_project.entity.Category;
import java.util.List;

public interface CategoryDao {
    public List<Category> findAll() throws DaoException;

    public Category findEntityById(long id) throws DaoException;

    public boolean blockedCategoryById(long id) throws DaoException;

    public boolean create(Category category) throws DaoException;

    public boolean update(Category category) throws DaoException;

    public Category findCategoryByName(String categoryName) throws DaoException;

    public List<Category> findAllBlockedCategories() throws DaoException;

    public boolean unblockedCategoryById(long id) throws DaoException;
}
