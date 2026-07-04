package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.model.User;
import com.artkids.util.ValidationUtils;

public class AuthService {
    private final MockDataService dataService;

    public AuthService(MockDataService dataService) {
        this.dataService = dataService;
    }

    public User login(String email, String password) {
        ValidationUtils.validateRequired(email, "Email");
        ValidationUtils.validateRequired(password, "Mot de passe");

        return dataService.getUsers().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst()
                .map(user -> {
                    if (!user.isActive()) {
                        throw new IllegalArgumentException("Ce compte est desactive.");
                    }
                    if (!user.getPassword().equals(password)) {
                        throw new IllegalArgumentException("Email ou mot de passe incorrect.");
                    }
                    AppConfig.getInstance().setCurrentUser(user);
                    return user;
                })
                .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect."));
    }

    public void logout() {
        AppConfig.getInstance().clearSession();
    }
}
