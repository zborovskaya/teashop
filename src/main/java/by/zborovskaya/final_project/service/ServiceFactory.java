package by.zborovskaya.final_project.service;


import by.zborovskaya.final_project.service.impl.CategoryServiceImpl;
import by.zborovskaya.final_project.service.impl.ClientServiceImpl;
import by.zborovskaya.final_project.service.impl.ProductServiceImpl;
import by.zborovskaya.final_project.service.impl.UserServiceImpl;

public class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();


    private final UserService userService = new UserServiceImpl();
    private final ClientService clientService = new ClientServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();
    private final ProductService productService = new ProductServiceImpl();

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        return instance;
    }


    public UserService getUserService() {
        return userService;
    }
    public ClientService getClientService() {
        return clientService;
    }
    public CategoryService getCategoryService(){return categoryService;}

    public ProductService getProductService() {
        return productService;
    }
}
