package com.aymane.ecom.multivendor.domain;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    CUSTOMER("ROLE_CUSTOMER"),
    SELLER("ROLE_SELLER");

    final String role;

    UserRole(String seller) {
        this.role = seller;
    }
}
