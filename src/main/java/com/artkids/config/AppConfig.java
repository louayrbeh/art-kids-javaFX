package com.artkids.config;

import com.artkids.model.User;
import com.artkids.service.ActivityService;
import com.artkids.service.ApiClient;
import com.artkids.service.AuthService;
import com.artkids.service.CategoryService;
import com.artkids.service.ChildService;
import com.artkids.service.MockDataService;
import com.artkids.service.ReservationService;
import com.artkids.service.UserService;

public final class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();

    private final MockDataService mockDataService = new MockDataService();
    private final ApiClient apiClient = new ApiClient("http://localhost:8000/api");
    private final CategoryService categoryService = new CategoryService(mockDataService);
    private final ActivityService activityService = new ActivityService(mockDataService);
    private final UserService userService = new UserService(mockDataService);
    private final ChildService childService = new ChildService(mockDataService);
    private final ReservationService reservationService =
            new ReservationService(mockDataService, childService, activityService);
    private final AuthService authService = new AuthService(mockDataService);

    private User currentUser;
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
        activityService.refreshStatuses();
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

    public void clearSession() {
        currentUser = null;
    }
}
