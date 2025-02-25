package com.onlineshop.project1.entity;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private String productName;
    private String supplierName;
    private BigDecimal productPrice;


    public Product(){}
    public Product(int productId, String productName, String supplierName, BigDecimal productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.supplierName = supplierName;
        this.productPrice = productPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
}
