package by.zborovskaya.final_project.service;


import by.zborovskaya.final_project.entity.Cart;

import javax.servlet.http.HttpSession;
import java.text.NumberFormat;

public interface CartService {
    Cart getCart(HttpSession session);

    void add(Cart cart, Long productId, String quantity, NumberFormat format) throws QuantityException;

    void update(Cart cart, Long productId, String quantity, NumberFormat format) throws QuantityException;

    void delete(Cart cart, Long productId);

    void clear(Cart cart);
}
