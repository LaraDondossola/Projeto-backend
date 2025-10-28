package com.example.cuidar.models;

public enum Role {
    ADMIN("ADMIN"),
    PROFISSIONAL("PROFISSIONAL"),
    CLIENTE("CLIENTE");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
