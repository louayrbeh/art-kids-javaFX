package com.artkids.service;

public class AdminReservationApiService extends ReservationApiService {
    public AdminReservationApiService(ApiClient apiClient, MockDataService dataService,
                                      ChildService childService, ActivityService activityService) {
        super(apiClient, dataService, childService, activityService);
    }
}
