package by.zborovskaya.final_project.control;

import by.zborovskaya.final_project.entity.Cart;
import by.zborovskaya.final_project.entity.Product;
import by.zborovskaya.final_project.entity.User;
import by.zborovskaya.final_project.entity.UserRole;
import by.zborovskaya.final_project.service.*;
import by.zborovskaya.final_project.service.impl.CartServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {
    private static final String PRODUCT_ADDED = "?message=Product added to cart";
    private static final String QUANTITY = "quantity";
    private static final String EQUALS = "=";
    private static final String AMPERSAND = "&";
    private static final String PRODUCT_ID = "productId";
    private static final String ERROR = "error";
    private static final CartService cartService = CartServiceImpl.getInstance();
    private static final String TIME_PATTERN = "HH:mm";
    private final ProductService productService = ServiceFactory.getInstance().getProductService();
    private static final String ABSOLUTE_PATH = "F:/products/";
    private static final String PICTURE_PATH = "picture_path";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();
        User user  =(User) httpSession.getAttribute("user");
        if(user !=null) {
            UserRole userRole = user.getUserRole();
            String command = req.getParameter("command");
            String pathJSP = null;
            boolean notUserCommand = false;
            if (command == null) {
                pathJSP = "/admin/addProduct.jsp";
            } else {
                try {
                    switch (command) {
                        case "update":
                            if (userRole==UserRole.ADMIN) {
                                long productId = Long.valueOf(req.getParameter("productId"));
                                Product product = productService.findProductById(productId);
                                req.setAttribute("product", product);
                                pathJSP = "/admin/updateProduct.jsp";
                            }else{
                                notUserCommand = true;
                            }
                            break;
                        case "add":
                            if (userRole==UserRole.ADMIN) {
                                pathJSP = "/admin/addProduct.jsp";
                            }else{
                                notUserCommand = true;
                            }
                            break;
                        case "restore":
                            if (userRole==UserRole.ADMIN) {
                            List<Product> productList = productService.findBlockedProducts();
                            req.setAttribute("productList", productList);
                            pathJSP = "/admin/restoreProduct.jsp";
                            }else{
                                notUserCommand = true;
                            }
                            break;
                    }
                } catch (ServiceException ex) {
                }
            }
            if(!notUserCommand){
                req.getRequestDispatcher("/WEB-INF/pages" + pathJSP).forward(req, resp);
            }else {
                resp.sendError(404);
            }
        }else {
            resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");
        String error = new String();
        String path = null;
        try {
            switch (command) {
                case "blocked":
                    long productId = Long.valueOf(req.getParameter("productId"));
                    productService.blockedProductById(productId);
                    path = "/main";
                    break;
                case "update":
                    long id = Long.valueOf(req.getParameter("productId"));
                    if (!update(req, resp, id)) return;
                    path = "/products?productId=" + id + "&command=update&success=Product updated successfully";
                    break;
                case "add_new_product":
                    if (!addProduct(req, resp)) return;
                    path = "/products?command=add&success=Product added successfully";
                    break;
                case "unBlocked":
                    productService.unBlockedProductById(Long.valueOf(req.getParameter("productId")));
                    path = "/products?command=restore";
                    break;
                case "addToCart":
                    if (!addToCart(req, resp)) return;
                    path = "/main" + PRODUCT_ADDED;
                    break;
                case "getXml":
                    Product product = productService.findProductById(Long.valueOf(req.getParameter("productId")));
                    try {
                        XmlWorker.serializeToXML(product);
                    }catch (Exception ex){
                        error="?error=xmlException";
                    }
                    path = "/main"+error;
                    break;
                case "useXml":
                    try {
                        productService.create(XmlWorker.deserializeFromXML(uploadFile(req,resp)));
                    }catch (Exception ex){
                        error="?error=xmlException";
                    }
                    path = "/main"+error;
                    break;
            }
        } catch (ServiceException ex) {
        }
        resp.sendRedirect(req.getContextPath() + path);
    }

    private boolean addProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String category_idString = req.getParameter("product_category");
        String name = req.getParameter("product_name");
        String composition = req.getParameter("product_composition");
        String weightString = req.getParameter("weight");
        BigDecimal weight = BigDecimal.valueOf(Double.parseDouble(weightString));
        if (weight.compareTo(BigDecimal.ZERO) <= 0) {
            resp.sendRedirect(req.getContextPath() + "/products?command=add&invalid_weight=invalid_weight");
            return false;
        }
        String timeCooking = req.getParameter("product_time");
        String temperatureString = req.getParameter("temperature");
        int temperature = Integer.valueOf(temperatureString);
        if (temperature > 100 || temperature < 0) {
            resp.sendRedirect(req.getContextPath() + "/products?command=add&invalid_temperature=invalid_temperature");
            return false;
        }
        String priceString = req.getParameter("price");
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceString));
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            resp.sendRedirect(req.getContextPath() + "/products?command=add&invalid_weight=invalid_weight");
            return false;
        }
        Product product = new Product(Long.valueOf(category_idString), name, null, composition,
                weight, temperature, LocalTime.parse(timeCooking, DateTimeFormatter.ofPattern(TIME_PATTERN)),
                price, true);
        try {
            productService.create(product);
        } catch (ServiceException ex) {

        }
        return true;
    }

    private boolean update(HttpServletRequest req, HttpServletResponse resp, long id) throws IOException {
        String category_idString = req.getParameter("product_category");
        String name = req.getParameter("product_name");
        String composition = req.getParameter("product_composition");
        String weightString = req.getParameter("weight");
        BigDecimal weight = BigDecimal.valueOf(Double.parseDouble(weightString));
        if (weight.compareTo(BigDecimal.ZERO) <= 0) {
            resp.sendRedirect(req.getContextPath() + "/products?productId=" + id + "&command=update&invalid_weight=invalid_weight");
            return false;
        }
        String timeCooking = req.getParameter("product_time");
        String temperatureString = req.getParameter("temperature");
        int temperature = Integer.valueOf(temperatureString);
        if (temperature > 100 || temperature < 0) {
            resp.sendRedirect(req.getContextPath() + "/products?productId=" + id + "&command=update&invalid_temperature=invalid_temperature");
            return false;
        }
        String priceString = req.getParameter("price");
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceString));
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            resp.sendRedirect(req.getContextPath() + "/products?productId=" + id + "&command=update&invalid_weight=invalid_weight");
            return false;
        }
        Product product = new Product(Long.valueOf(category_idString), name, null, composition,
                weight, temperature, LocalTime.parse(timeCooking, DateTimeFormatter.ofPattern(TIME_PATTERN)),
                price, true);
        product.setId(id);
        try {
            productService.update(product);
        } catch (ServiceException ex) {

        }
        return true;
    }

    private boolean addToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String quantityString = request.getParameter(QUANTITY);
        String productIdString = request.getParameter(PRODUCT_ID);
        Long productId = Long.valueOf(productIdString);
        request.getLocale();
        Cart cart = cartService.getCart(request.getSession());
        try {
            cartService.add(cart, productId, quantityString, NumberFormat.getInstance(request.getLocale()));
//            response.sendRedirect(request.getContextPath() + "/products" + PRODUCT_ADDED);
        } catch (QuantityException ex) {
            String category_id =request.getParameter("category_id");
            String parameter = "";
            if(!category_id.isEmpty()){
                parameter="&command=find_all_products_by_category&category_id="+category_id;
            }
            response.sendRedirect(request.getContextPath() + "/main?" + PRODUCT_ID + EQUALS + productId
                    + AMPERSAND + QUANTITY + EQUALS + quantityString
                    + AMPERSAND + ERROR + EQUALS + ex.getMessage()+parameter);
            return false;
        }
        return true;
    }

    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String path =null;
        try (InputStream inputStream = request.getPart(PICTURE_PATH).getInputStream()) {
            String submittedFileName = request.getPart(PICTURE_PATH).getSubmittedFileName();
            path = ABSOLUTE_PATH + submittedFileName;
        } catch (IOException | ServletException e) {
        }
        return path;

    }
}
