package by.zborovskaya.final_project.control.listener;


import by.zborovskaya.final_project.dao.pool.ConnectionPool;
import by.zborovskaya.final_project.dao.pool.ConnectionPoolException;
import by.zborovskaya.final_project.entity.Category;
import by.zborovskaya.final_project.service.ServiceException;
import by.zborovskaya.final_project.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

public class ContextListener implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger(ContextListener.class);
    private static final String INIT_POOL_ERROR ="Error initializing pool data";
    private static final String CLOSING_POOL_ERROR ="Error closing connection pool";
    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            ConnectionPool.getInstance().init();
            List<Category> categoryList = serviceFactory.getCategoryService().findAll();
            servletContextEvent.getServletContext().setAttribute("categoryList",categoryList);

        } catch (ConnectionPoolException | ServiceException e) {
            logger.error(INIT_POOL_ERROR);
            throw new ListenerException(INIT_POOL_ERROR, e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            ConnectionPool.getInstance().destroy();
        } catch (ConnectionPoolException e) {
            logger.error(CLOSING_POOL_ERROR);
            throw new ListenerException(CLOSING_POOL_ERROR,e);
        }
    }
}
