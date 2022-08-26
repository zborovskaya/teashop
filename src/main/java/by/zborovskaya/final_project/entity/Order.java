package by.zborovskaya.final_project.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {
    private long id;
    private long id_user;
    private List<CartItem> items;
    private BigDecimal subTotalCost;
    private BigDecimal deliveryCost;
    private BigDecimal totalCost;
    private int totalQuantity;

    private String deliveryAddress;
    private LocalDate deliveryDate;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    private PaymentMethod paymentMethod;

    public Order(long id, long id_user, BigDecimal totalCost,
                 String deliveryAddress, LocalDate deliveryDate,
                 PaymentMethod paymentMethod) {
        this.id = id;
        this.id_user = id_user;
        this.totalCost = totalCost;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDate = deliveryDate;
        this.paymentMethod = paymentMethod;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getSubTotalCost() {
        return subTotalCost;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void setSubTotalCost(BigDecimal subTotalCost) {
        this.subTotalCost = subTotalCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }


    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return totalQuantity == order.totalQuantity
                && Objects.equals(items, order.items) && Objects.equals(subTotalCost, order.subTotalCost)
                && Objects.equals(deliveryCost, order.deliveryCost) && Objects.equals(totalCost, order.totalCost)
                && Objects.equals(deliveryAddress, order.deliveryAddress)
                && Objects.equals(deliveryDate, order.deliveryDate) && paymentMethod == order.paymentMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, items, subTotalCost, deliveryCost, totalCost, totalQuantity, deliveryAddress, deliveryDate, paymentMethod);
    }
}
