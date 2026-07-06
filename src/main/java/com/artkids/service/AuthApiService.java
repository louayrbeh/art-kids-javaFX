package com.artkids.service;

import com.artkids.config.AppConfig;
import com.artkids.model.User;
import com.artkids.util.JsonUtils;
import com.artkids.util.ValidationUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthApiService extends AuthService {
    private final ApiClient apiClient;

    public AuthApiService(ApiClient apiClient, MockDataService dataService) {
        super(dataService);
        this.apiClient = apiClient;
    }

    @Override
    public User login(String email, String password) {
        ValidationUtils.validateRequired(email, "Email");
        ValidationUtils.validateRequired(password, "Mot de passe");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", email.trim());
        payload.put("password", password);

        Map<String, Object> data = JsonUtils.parseObject(apiClient.post("/login", JsonUtils.stringify(payload)));
        String token = JsonUtils.asString(data.get("token"));
        int expiresIn = JsonUtils.asInt(data.get("expiresIn"));
        User user = ApiMappers.user(data.get("user"));
        AppConfig.getInstance().setApiSession(token, expiresIn, user);
        return user;
    }

    public User getMe() {
        User user = ApiMappers.user(JsonUtils.parseObject(apiClient.get("/me")));
        AppConfig.getInstance().setCurrentUser(user);
        return user;
    }

    @Override
    public void logout() {
        try {
            if (AppConfig.getInstance().getBearerToken() != null) {
                apiClient.post("/logout", "{}");
            }
        } catch (ApiException ignored) {
            // Stateless logout is local-first on the JavaFX side.
        } finally {
            AppConfig.getInstance().clearSession();
        }
    }
}
