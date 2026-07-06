package com.artkids.config;

import com.artkids.model.User;
import com.artkids.service.AdminActivityApiService;
import com.artkids.service.AdminCategoryApiService;
import com.artkids.service.AdminChildApiService;
import com.artkids.service.AdminReservationApiService;
import com.artkids.service.AdminUserApiService;
import com.artkids.service.ApiClient;
import com.artkids.service.AuthApiService;
import com.artkids.service.MockDataService;
import com.artkids.service.ActivityService;
import com.artkids.service.AuthService;
import com.artkids.service.CategoryService;
import com.artkids.service.ChildService;
import com.artkids.service.ReservationService;
import com.artkids.service.UserService;

public final class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();

    private final MockDataService mockDataService = new MockDataService();
    private final ApiClient apiClient = new ApiClient(
            System.getProperty("artkids.api.baseUrl", "http://127.0.0.1:8000/api")
    );
    private final CategoryService categoryService = new AdminCategoryApiService(apiClient, mockDataService);
    private final ActivityService activityService = new AdminActivityApiService(apiClient, mockDataService);
    private final UserService userService = new AdminUserApiService(apiClient, mockDataService);
    private final ChildService childService = new AdminChildApiService(apiClient, mockDataService);
    private final ReservationService reservationService =
            new AdminReservationApiService(apiClient, mockDataService, childService, activityService);
    private final AuthService authService = new AuthApiService(apiClient, mockDataService);

    private User currentUser;
    private String bearerToken;
    private int tokenExpiresIn;
    private boolean initialized;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public void initialize() {
        if (initialized) {
            return;
        }
        mockDataService.initialize();
        initialized = true;
    }

    public MockDataService getMockDataService() {
        return mockDataService;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public ChildService getChildService() {
        return childService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public ActivityService getActivityService() {
        return activityService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public UserService getUserService() {
        return userService;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setApiSession(String bearerToken, int tokenExpiresIn, User currentUser) {
        this.bearerToken = bearerToken;
        this.tokenExpiresIn = tokenExpiresIn;
        this.currentUser = currentUser;
        apiClient.setBearerToken(bearerToken);
    }

    public int getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public void clearSession() {
        currentUser = null;
        bearerToken = null;
        tokenExpiresIn = 0;
        apiClient.clearBearerToken();
    }
}
