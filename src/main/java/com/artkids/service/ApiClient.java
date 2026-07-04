package com.artkids.service;

public class ApiClient {
    private final String baseUrl;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String get(String path) {
        // TODO: future integration with Symfony REST API
        throw new UnsupportedOperationException("API non disponible dans la version mockee.");
    }

    public String post(String path, String payload) {
        // TODO: future integration with Symfony REST API
        throw new UnsupportedOperationException("API non disponible dans la version mockee.");
    }

    public String put(String path, String payload) {
        // TODO: future integration with Symfony REST API
        throw new UnsupportedOperationException("API non disponible dans la version mockee.");
    }

    public String delete(String path) {
        // TODO: future integration with Symfony REST API
        throw new UnsupportedOperationException("API non disponible dans la version mockee.");
    }
}
