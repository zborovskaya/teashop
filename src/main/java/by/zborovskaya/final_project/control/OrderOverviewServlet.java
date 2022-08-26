package by.zborovskaya.final_project.control;

import by.zborovskaya.final_project.entity.*;
import by.zborovskaya.final_project.service.*;
import by.zborovskaya.final_project.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet("/order/Overview")
public class OrderOverviewServlet extends HttpServlet {
    private final String PRODUCTS = "products";
    private final OrderService orderService = OrderServiceImpl.getInstance();
    private final ProductService productService = ServiceFactory.getInstance().getProductService();
    private final UserService userService = ServiceFactory.getInstance().getUserService();
    private final ClientService clientService = ServiceFactory.getInstance().getClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendError(404);
        } else {
            String command = req.getParameter("command");
            long orderId = 0;
            if (command == null) {
                req.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(req, resp);
                return;
            }
            HttpSession httpSession = req.getSession();
            UserRole userRole = user.getUserRole();
            long idUser = user.getIdUser();
            Order order = null;
            List<CartItem> cartItemList = null;
            UserInfo userInfo = null;
            String path = null;
            List<Product> productsOrder = null;
            boolean commandNotForUser = false;
            try {
                switch (command) {
                    case "new":
                        if (userRole == UserRole.CLIENT) {
                            req.setAttribute("order", ((Order) httpSession.getAttribute("order")));
                            httpSession.removeAttribute("order");
                            path = "/client/orderOverview.jsp";
                        } else {
                            commandNotForUser = true;
                        }
                        break;
                    case "getDetails":
                        orderId = Long.valueOf(req.getParameter("orderId"));
                        order = orderService.getOrderById(orderId);
                        cartItemList = orderService.getCartItemsByOrderId(orderId);
                        order.setItems(cartItemList);
                        orderService.calculateTotalQuantity(order);
                        userInfo = clientService.findUserInfoById((int) order.getId_user());
                        req.setAttribute("order", order);
                        productsOrder = new ArrayList<>();
                        try {
                            for (CartItem item : order.getItems()) {
                                productsOrder.add(productService.findProductById(item.getProductId()));
                            }
                        } catch (ServiceException ex) {
                        }
                        req.setAttribute(PRODUCTS, productsOrder);
                        req.setAttribute("userInfo", userInfo);
                        path = "/orderDetailsPage.jsp";
                        break;
                    case "getOrdersForUser":
                        if (userRole == UserRole.CLIENT) {
                            List<Order> orderList = orderService.findOrdersByUserId(idUser);
                            req.setAttribute("orders", orderList);
                        } else {
                            List<Order> orderListAdmin = orderService.findAll();
                            for(int i =0; i<orderListAdmin.size(); i++){
                                Order orderSort = orderListAdmin.get(i);
                                String status = orderSort.getStatus();
                                if(status.equals("waiting")){
                                    orderListAdmin.remove(i);
                                    orderListAdmin.add(0,orderSort);
                                }
                            }
                            req.setAttribute("orders", orderListAdmin);
                        }
                        path = "/orders.jsp";
                        break;
                    case "getWordFile":
                        if (userRole == UserRole.ADMIN) {
                            orderId = Long.valueOf(req.getParameter("orderId"));
                            order = orderService.getOrderById(orderId);
                            cartItemList = orderService.getCartItemsByOrderId(orderId);
                            order.setItems(cartItemList);
                            orderService.calculateTotalQuantity(order);
                            userInfo = clientService.findUserInfoById((int) order.getId_user());
                            WordCreator.create(order, userInfo);
                            req.setAttribute("order", order);
                            productsOrder = new ArrayList<>();
                            try {
                                for (CartItem item : order.getItems()) {
                                    productsOrder.add(productService.findProductById(item.getProductId()));
                                }
                            } catch (ServiceException ex) {
                            }
                            req.setAttribute(PRODUCTS, productsOrder);
                            req.setAttribute("userInfo", userInfo);
                            path = "/orderDetailsPage.jsp";
                        }else{
                            commandNotForUser=true;
                        }
                        break;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            if (!commandNotForUser) {
                req.getRequestDispatcher("/WEB-INF/pages" + path).forward(req, resp);
            } else {
                resp.sendError(404);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        String orderId = req.getParameter("orderId");
        try {
            orderService.changeOrderStatus(Long.valueOf(orderId), status);
        } catch (ServiceException ex) {
        }
        resp.sendRedirect(req.getContextPath() + "/order/Overview?command=getOrdersForUser");
    }
}
