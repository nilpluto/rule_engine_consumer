package com.example.consumer.service;

import org.springframework.stereotype.Service;

/**
 * Service responsible for handling user access based on rule evaluation.
 */
@Service
public class UserService {

    /**
     * Grants access to the user.
     */
    public void allowAccess() {
        System.out.println("✅ Access granted to the user.");
    }

    /**
     * Denies access to the user.
     */
    public void denyAccess() {
        System.out.println("⛔ Access denied to the user.");
    }
}
