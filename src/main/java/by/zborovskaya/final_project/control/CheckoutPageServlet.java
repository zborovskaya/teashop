package by.zborovskaya.final_project.control;

import by.zborovskaya.final_project.entity.*;
import by.zborovskaya.final_project.service.*;
import by.zborovskaya.final_project.service.impl.CartServiceImpl;
import by.zborovskaya.final_project.service.impl.OrderServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@WebServlet("/checkout")
public class CheckoutPageServlet extends HttpServlet {
    private static final String PRODUCTS = "products";
    private static final String ORDER = "order";
    private static final String ERRORS = "errors";
    private static final String NAME_VALUES = "nameValues";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String DELIVERY_ADDRESS = "deliveryAddress";
    private static final String PHONE = "phone";
    private static final String PAYMENT_METHODS = "paymentMethods";
    private static final String PAYMENT_METHOD = "paymentMethod";
    private static final String DELIVERY_DATE = "deliveryDate";
    private static final String INVALID_VALUE = "Invalid value";
    private CartService cartService;
    private final ProductService productService = ServiceFactory.getInstance().getProductService();
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
        orderService = OrderServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            response.sendError(404);
        }else {
            if(user.getUserRole() == UserRole.CLIENT) {
                setAttribute(request);
                request.getRequestDispatcher("/WEB-INF/pages/client/checkout.jsp").forward(request, response);
            }else {
                response.sendError(404);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Map<String, String> errors = new HashMap<>();
        Map<String, String> nameValues = new HashMap<>();
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);

        setRequiredValue(request, DELIVERY_ADDRESS, errors, nameValues, order::setDeliveryAddress);
        setPaymentMethod(request, errors, nameValues, order::setPaymentMethod);
        LocalDate date = LocalDate.now();
        order.setDeliveryDate(date);
        order.setId_user(((User)httpSession.getAttribute("user")).getIdUser());
        order.setStatus("waiting");
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            httpSession.setAttribute("order",order);
            cartService.clear(cart);
            response.sendRedirect(request.getContextPath() + "/order/Overview?command=new");
        } else {
            httpSession.setAttribute(ORDER, order);
            httpSession.setAttribute(ERRORS, errors);
            httpSession.setAttribute(NAME_VALUES, nameValues);
            response.sendRedirect(request.getContextPath() + "/checkout");
        }
    }

    private void setRequiredValue(HttpServletRequest request, String parameter, Map<String, String> errors,
                                  Map<String, String> nameValues, Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, INVALID_VALUE);
            nameValues.put(parameter, value);
        } else {
            consumer.accept(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors,
                                  Map<String, String> nameValues, Consumer<PaymentMethod> consumer) {
        String paymentMethod = request.getParameter(PAYMENT_METHOD);
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            errors.put(PAYMENT_METHOD, INVALID_VALUE);
            nameValues.put(PAYMENT_METHOD, paymentMethod);
        } else {
            consumer.accept(PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        }
    }

    private void setAttribute(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Map<Long, String> errors = (Map<Long, String>) httpSession.getAttribute(ERRORS);
        Map<Long, String> nameValues = (Map<Long, String>) httpSession.getAttribute(NAME_VALUES);
        Cart cart = cartService.getCart(request.getSession());
        Order order = null;
        if (errors != null) {
            request.setAttribute(ERRORS, errors);
            httpSession.removeAttribute(ERRORS);
            request.setAttribute(NAME_VALUES, nameValues);
            httpSession.removeAttribute(NAME_VALUES);
            order = (Order) httpSession.getAttribute(ORDER);
            httpSession.removeAttribute(ORDER);
        } else {
            order = orderService.getOrder(cart);
        }
        request.setAttribute(ORDER, order);
        List<Product> productsOrder = new ArrayList<>();
        try {
            for (CartItem item : order.getItems()) {
                productsOrder.add(productService.findProductById(item.getProductId()));
            }
        }catch (ServiceException ex){}
        request.setAttribute(PRODUCTS, productsOrder);
        request.setAttribute(PAYMENT_METHODS, orderService.getPaymentMethods());
    }
}
