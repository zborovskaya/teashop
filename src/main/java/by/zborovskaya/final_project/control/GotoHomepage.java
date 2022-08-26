package by.zborovskaya.final_project.control;

import by.zborovskaya.final_project.entity.Product;
import by.zborovskaya.final_project.service.ProductService;
import by.zborovskaya.final_project.service.ServiceException;
import by.zborovskaya.final_project.service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/main")
public class GotoHomepage extends HttpServlet {
    private final ProductService productService = ServiceFactory.getInstance().getProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");
        String search = req.getParameter("search");
        if (command == null) {
            if (search == null) {
                try {
                    List<Product> productList = productService.findAll();
                    req.setAttribute("productList", productList);
                } catch (ServiceException ex) {
                }
            } else {
                try {
                    List<Product> productList = productService.searchByName(search.trim());
                    req.setAttribute("productList", productList);
                } catch (ServiceException ex) {
                }
            }
        } else {
            if (command.equals("find_all_products_by_category")) {
                String categoryIdString = req.getParameter("category_id");
                try {
                    List<Product> productList = productService.findProductsByCategory(Long.valueOf(categoryIdString));
                    req.setAttribute("productList", productList);
                    req.setAttribute("category_id", categoryIdString);
                } catch (ServiceException ex) {
                }
            }
        }
        req.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(req, resp);
        return;
    }
}
