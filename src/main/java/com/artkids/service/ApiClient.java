package com.artkids.service;

import com.artkids.util.JsonUtils;
import com.artkids.util.LoadingUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class ApiClient {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);

    private final String baseUrl;
    private final HttpClient httpClient;
    private String bearerToken;

    public ApiClient(String baseUrl) {
        this.baseUrl = normalizeBaseUrl(baseUrl);
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .build();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public void clearBearerToken() {
        this.bearerToken = null;
    }

    public String get(String path) {
        return send("GET", path, null);
    }

    public String post(String path, String payload) {
        return send("POST", path, payload);
    }

    public String put(String path, String payload) {
        return send("PUT", path, payload);
    }

    public String delete(String path) {
        return send("DELETE", path, null);
    }

    private String send(String method, String path, String payload) {
        return LoadingUtils.runWithGlobalLoading(() -> sendNow(method, path, payload));
    }

    private String sendNow(String method, String path, String payload) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + normalizePath(path)))
                    .timeout(REQUEST_TIMEOUT)
                    .header("Accept", "application/json");

            if (bearerToken != null && !bearerToken.isBlank()) {
                builder.header("Authorization", "Bearer " + bearerToken);
            }

            if (payload == null || payload.isBlank()) {
                builder.method(method, HttpRequest.BodyPublishers.noBody());
            } else {
                builder.header("Content-Type", "application/json");
                builder.method(method, HttpRequest.BodyPublishers.ofString(payload));
            }

            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            return unwrapResponse(response.statusCode(), response.body());
        } catch (IOException exception) {
            throw new ApiException(0, "API Symfony indisponible. Verifiez que le serveur est demarre.", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ApiException(0, "Requete API interrompue.", exception);
        }
    }

    @SuppressWarnings("unchecked")
    private String unwrapResponse(int statusCode, String body) {
        Object parsed = JsonUtils.parse(body == null || body.isBlank() ? "{}" : body);
        if (!(parsed instanceof Map<?, ?> response)) {
            if (statusCode >= 200 && statusCode < 300) {
                return body == null ? "" : body;
            }
            throw new ApiException(statusCode, "Erreur API HTTP " + statusCode);
        }

        Object success = response.get("success");
        Object data = response.get("data");
        String message = JsonUtils.asString(response.get("message"));

        boolean ok = statusCode >= 200 && statusCode < 300 && Boolean.TRUE.equals(success);
        if (!ok) {
            throw new ApiException(statusCode, buildErrorMessage(message, response.get("errors")));
        }
        return JsonUtils.stringify(data);
    }

    private String buildErrorMessage(String message, Object errors) {
        if (errors instanceof Map<?, ?> map && !map.isEmpty()) {
            Object firstValue = map.values().iterator().next();
            String fieldMessage = JsonUtils.asString(firstValue);
            if (fieldMessage != null && !fieldMessage.isBlank()) {
                return fieldMessage;
            }
        }
        return message == null || message.isBlank() ? "Erreur API inconnue." : message;
    }

    private String normalizeBaseUrl(String value) {
        String normalized = value == null || value.isBlank() ? "http://127.0.0.1:8000/api" : value.trim();
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }

    private String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }
        return path.startsWith("/") ? path : "/" + path;
    }
}
