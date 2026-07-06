package com.artkids.service;

import com.artkids.enums.ActivityStatus;
import com.artkids.enums.ReservationStatus;
import com.artkids.enums.Sexe;
import com.artkids.enums.UserRole;
import com.artkids.model.Activity;
import com.artkids.model.Category;
import com.artkids.model.Child;
import com.artkids.model.Reservation;
import com.artkids.model.User;
import com.artkids.util.JsonUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ApiMappers {
    private ApiMappers() {
    }

    public static User user(Object value) {
        Map<String, Object> map = object(value);
        User user = new User();
        user.setId(JsonUtils.asInt(map.get("id")));
        user.setNom(JsonUtils.asString(map.get("nom")));
        user.setPrenom(JsonUtils.asString(map.get("prenom")));
        user.setEmail(JsonUtils.asString(map.get("email")));
        user.setTelephone(JsonUtils.asString(map.get("telephone")));
        user.setRole(userRole(map.get("roles")));
        user.setActive(map.containsKey("isActive") ? JsonUtils.asBoolean(map.get("isActive")) : true);
        user.setCreatedAt(dateTime(map.get("createdAt")));
        user.setUpdatedAt(dateTime(map.get("updatedAt")));
        return user;
    }

    public static Child child(Object value) {
        Map<String, Object> map = object(value);
        Child child = new Child();
        child.setId(JsonUtils.asInt(map.get("id")));
        child.setNom(JsonUtils.asString(map.get("nom")));
        child.setPrenom(JsonUtils.asString(map.get("prenom")));
        child.setDateNaissance(date(map.get("dateNaissance")));
        child.setSexe(enumValue(Sexe.class, map.get("sexe")));
        child.setParentId(nestedId(map.get("parent")));
        child.setCreatedAt(dateTime(map.get("createdAt")));
        child.setUpdatedAt(dateTime(map.get("updatedAt")));
        return child;
    }

    public static Category category(Object value) {
        Map<String, Object> map = object(value);
        Category category = new Category();
        category.setId(JsonUtils.asInt(map.get("id")));
        category.setNom(JsonUtils.asString(map.get("nom")));
        category.setDescription(JsonUtils.asString(map.get("description")));
        String image = JsonUtils.asString(map.get("image"));
        category.setImage(image == null ? JsonUtils.asString(map.get("imageUrl")) : image);
        category.setCreatedAt(dateTime(map.get("createdAt")));
        category.setUpdatedAt(dateTime(map.get("updatedAt")));
        return category;
    }

    public static Activity activity(Object value) {
        Map<String, Object> map = object(value);
        Activity activity = new Activity();
        activity.setId(JsonUtils.asInt(map.get("id")));
        activity.setTitre(JsonUtils.asString(map.get("titre")));
        activity.setDescription(JsonUtils.asString(map.get("description")));
        String image = JsonUtils.asString(map.get("image"));
        activity.setImage(image == null ? JsonUtils.asString(map.get("imageUrl")) : image);
        activity.setDateActivite(date(map.get("dateActivite")));
        activity.setHeureDebut(time(map.get("heureDebut")));
        activity.setHeureFin(time(map.get("heureFin")));
        activity.setCapaciteMax(JsonUtils.asInt(map.get("capaciteMax")));
        activity.setPlacesDisponibles(map.containsKey("placesDisponibles")
                ? JsonUtils.asInt(map.get("placesDisponibles"))
                : -1);
        activity.setAgeMin(JsonUtils.asInt(map.get("ageMin")));
        activity.setAgeMax(JsonUtils.asInt(map.get("ageMax")));
        activity.setPrix(decimal(map.get("prix")));
        activity.setStatut(enumValue(ActivityStatus.class, map.get("statut")));
        activity.setLieu(JsonUtils.asString(map.get("lieu")));
        activity.setCategoryId(nestedId(map.get("category")));
        if (activity.getCategoryId() == 0) {
            activity.setCategoryId(JsonUtils.asInt(map.get("categoryId")));
        }
        activity.setCreatedAt(dateTime(map.get("createdAt")));
        activity.setUpdatedAt(dateTime(map.get("updatedAt")));
        return activity;
    }

    public static Reservation reservation(Object value) {
        Map<String, Object> map = object(value);
        Reservation reservation = new Reservation();
        reservation.setId(JsonUtils.asInt(map.get("id")));
        reservation.setDateReservation(dateTime(map.get("dateReservation")));
        reservation.setStatut(enumValue(ReservationStatus.class, map.get("statut")));
        reservation.setChildId(nestedId(map.get("child")));
        reservation.setActivityId(nestedId(map.get("activity")));
        reservation.setCreatedAt(dateTime(map.get("createdAt")));
        reservation.setUpdatedAt(dateTime(map.get("updatedAt")));
        return reservation;
    }

    public static Map<String, Object> childPayload(Child child) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("nom", child.getNom());
        payload.put("prenom", child.getPrenom());
        payload.put("dateNaissance", child.getDateNaissance() == null ? null : child.getDateNaissance().toString());
        payload.put("sexe", child.getSexe() == null ? null : child.getSexe().name());
        return payload;
    }

    public static Map<String, Object> categoryPayload(Category category) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("nom", category.getNom());
        payload.put("description", category.getDescription());
        payload.put("image", category.getImage());
        return payload;
    }

    public static Map<String, Object> activityPayload(Activity activity) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("titre", activity.getTitre());
        payload.put("description", activity.getDescription());
        payload.put("categoryId", activity.getCategoryId());
        payload.put("dateActivite", activity.getDateActivite() == null ? null : activity.getDateActivite().toString());
        payload.put("heureDebut", activity.getHeureDebut() == null ? null : activity.getHeureDebut().toString());
        payload.put("heureFin", activity.getHeureFin() == null ? null : activity.getHeureFin().toString());
        payload.put("capaciteMax", activity.getCapaciteMax());
        payload.put("ageMin", activity.getAgeMin());
        payload.put("ageMax", activity.getAgeMax());
        payload.put("prix", activity.getPrix() == null ? "0.00" : activity.getPrix().toPlainString());
        payload.put("statut", activity.getStatut() == null ? null : activity.getStatut().name());
        payload.put("lieu", activity.getLieu());
        payload.put("image", activity.getImage());
        return payload;
    }

    public static Map<String, Object> userPayload(User user) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("nom", user.getNom());
        payload.put("prenom", user.getPrenom());
        payload.put("email", user.getEmail());
        payload.put("telephone", user.getTelephone());
        payload.put("password", user.getPassword());
        payload.put("roles", List.of(user.isAdmin() ? "ROLE_ADMIN" : "ROLE_PARENT"));
        payload.put("isActive", user.isActive());
        return payload;
    }

    public static Map<String, Object> registerPayload(String nom, String prenom, String email,
                                                      String telephone, String password) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("nom", nom);
        payload.put("prenom", prenom);
        payload.put("email", email);
        payload.put("telephone", telephone);
        payload.put("password", password);
        return payload;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> array(String json) {
        Object parsed = JsonUtils.parse(json);
        if (parsed instanceof List<?> list) {
            return (List<Object>) list;
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> object(Object value) {
        if (value instanceof String json) {
            Object parsed = JsonUtils.parse(json);
            if (parsed instanceof Map<?, ?> map) {
                return (Map<String, Object>) map;
            }
        }
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return new LinkedHashMap<>();
    }

    private static UserRole userRole(Object rolesValue) {
        if (rolesValue instanceof List<?> roles) {
            if (roles.stream().anyMatch(role -> "ROLE_ADMIN".equals(String.valueOf(role)))) {
                return UserRole.ADMIN;
            }
        }
        return UserRole.PARENT;
    }

    private static int nestedId(Object nested) {
        if (nested instanceof Map<?, ?> map) {
            return JsonUtils.asInt(map.get("id"));
        }
        return JsonUtils.asInt(nested);
    }

    private static LocalDate date(Object value) {
        String text = JsonUtils.asString(value);
        return text == null || text.isBlank() ? null : LocalDate.parse(text.substring(0, 10));
    }

    private static LocalTime time(Object value) {
        String text = JsonUtils.asString(value);
        return text == null || text.isBlank() ? null : LocalTime.parse(text.substring(0, 5));
    }

    private static LocalDateTime dateTime(Object value) {
        String text = JsonUtils.asString(value);
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(text).toLocalDateTime();
        } catch (RuntimeException ignored) {
            return LocalDateTime.parse(text);
        }
    }

    private static BigDecimal decimal(Object value) {
        String text = JsonUtils.asString(value);
        return text == null || text.isBlank() ? BigDecimal.ZERO : new BigDecimal(text);
    }

    private static <E extends Enum<E>> E enumValue(Class<E> enumType, Object value) {
        String text = JsonUtils.asString(value);
        return text == null || text.isBlank() ? null : Enum.valueOf(enumType, text);
    }
}
