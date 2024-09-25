// app/src/main/java/com/faltenreich/diaguard/Order.java
package com.faltenreich.diaguard;

import java.util.List;

public class Order {
    private List<Medicine> cartList;
    private String address;
    private String dateTime;
    private String paymentMethod;
    private boolean isDelivered;
    private String orderId;

    // No-argument constructor
    public Order() {
    }

    public Order(List<Medicine> cartList, String address, String dateTime, String paymentMethod, boolean isDelivered, String orderId) {
        this.cartList = cartList;
        this.address = address;
        this.dateTime = dateTime;
        this.paymentMethod = paymentMethod;
        this.isDelivered = isDelivered;
        this.orderId = orderId;
    }

    // Getters and setters
    public List<Medicine> getCartList() {
        return cartList;
    }

    public void setCartList(List<Medicine> cartList) {
        this.cartList = cartList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}