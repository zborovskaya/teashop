package by.zborovskaya.final_project.service.impl;

import by.zborovskaya.final_project.entity.Cart;
import by.zborovskaya.final_project.entity.CartItem;
import by.zborovskaya.final_project.service.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class CartServiceImpl implements CartService {
    private final ProductService productService = ServiceFactory.getInstance().getProductService();
    private static final String INCORRECT_QUANTITY = "Incorrect quantity";
    private static final String OUT_OF_STOCK = "Out of stock, available ";
    private static final String CART_SESSION_ATTRIBUTE = CartServiceImpl.class.getName() + ".cart";
    private static volatile CartService instance;

    public static CartService getInstance() {
        CartService result = instance;
        if (result != null) {
            return result;
        }
        synchronized (CartService.class) {
            if (instance == null) {
                instance = new CartServiceImpl();
            }
            return instance;
        }
    }

    private CartServiceImpl() {
    }

    @Override
    public synchronized Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public synchronized void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(item -> productId.equals(item.getProductId()));
        recalculateCart(cart);
    }

    @Override
    public synchronized void add(Cart cart, Long productId, String quantityString, NumberFormat format)
            throws QuantityException {
        int quantity = parseQuantityToInt(quantityString, format);
        List<CartItem> cartItemList = cart.getItems();
        boolean existId = false;
        for (CartItem item : cartItemList) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity + item.getQuantity());
                existId = true;
            }
        }
        if (!existId) {
            cartItemList.add(new CartItem(productId, quantity));
        }
        recalculateCart(cart);
    }

    @Override
    public void update(Cart cart, Long productId, String quantityString, NumberFormat format) throws QuantityException {
        int quantity = parseQuantityToInt(quantityString, format);
        List<CartItem> cartItemList = cart.getItems();
        for (CartItem item : cartItemList) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
            }
        }
        recalculateCart(cart);
    }

    private void recalculateCart(Cart cart) {
        if (cart.getItems().size() == 0) {
            cart.setTotalQuantity(0);
            cart.setTotalCost(null);
        } else {
            List<CartItem> cartItemList = cart.getItems();
            BigDecimal cost = BigDecimal.valueOf(0);
            for (CartItem item : cartItemList) {
                try {
                    cost = cost.add( productService.findProductById(item.getProductId()).getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity())));
                }catch (ServiceException ex){}
            }
            cart.setTotalCost(cost);
            cart.setTotalQuantity(cart.getItems()
                    .stream()
                    .map(CartItem::getQuantity)
                    .collect(Collectors.summingInt(q -> q.intValue())));
        }
    }

    private int parseQuantityToInt(String quantityString, NumberFormat format) throws QuantityException {
        int quantity;
        if (!QuantityValidator.isNumber(quantityString)) {
            throw new QuantityException(INCORRECT_QUANTITY);
        }
        try {
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException ex) {
            throw new QuantityException(INCORRECT_QUANTITY);
        }
        if (quantity < 1) {
            throw new QuantityException(INCORRECT_QUANTITY);
        }
        return quantity;
    }

    @Override
    public void clear(Cart cart) {
        List<CartItem> cartItemList = cart.getItems();
        cartItemList.removeAll(cartItemList);
        recalculateCart(cart);
    }
}
