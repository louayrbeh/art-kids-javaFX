# 09 - Plan d'integration future JavaFX <-> Symfony

## Objectif

Dans une etape ulterieure, l'application JavaFX devra devenir un client desktop du backend Symfony.

Pour cette etape de documentation :

- JavaFX ne doit pas encore appeler Symfony
- Symfony reste l'application de reference
- les donnees JavaFX pourront etre mockees au debut

## Architecture cible

- Symfony garde la logique metier principale
- Symfony garde MySQL
- JavaFX devient client desktop
- JavaFX consomme des API REST Symfony
- JavaFX ne se connecte jamais directement a MySQL

## Pourquoi ne pas connecter JavaFX directement a MySQL

- centraliser la logique metier cote Symfony
- garder les validations au meme endroit
- conserver la securite et les autorisations par role
- simplifier la maintenance
- eviter la duplication de regles metier sensibles

## Couches a prevoir dans JavaFX

- `ApiClient` pour les appels HTTP
- `AuthService` pour login et session
- services metier orientes API :
  - `ChildApiService`
  - `CategoryApiService`
  - `ActivityApiService`
  - `ReservationApiService`
  - `UserApiService`

## Flux futur recommande

1. JavaFX envoie des identifiants a Symfony
2. Symfony authentifie
3. Symfony renvoie un token ou une session adaptee a l'API
4. JavaFX appelle les endpoints REST securises
5. Symfony renvoie du JSON
6. JavaFX rafraichit ses vues

## Futures API recommandees

### Authentification

- `POST /api/login`
- `GET /api/me`
- `POST /api/logout`

### Enfants

- `GET /api/children`
- `GET /api/children/{id}`
- `POST /api/children`
- `PUT /api/children/{id}`
- `DELETE /api/children/{id}`

### Categories

- `GET /api/categories`
- `GET /api/categories/{id}`
- `POST /api/categories`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`

### Activites

- `GET /api/activities`
- `GET /api/activities/{id}`
- `POST /api/activities`
- `PUT /api/activities/{id}`
- `DELETE /api/activities/{id}`

### Reservations

- `GET /api/reservations`
- `GET /api/reservations/{id}`
- `POST /api/reservations`
- `POST /api/reservations/{id}/cancel`
- `POST /api/reservations/{id}/confirm`

### Dashboard / statistiques

- `GET /api/parent/dashboard`
- `GET /api/admin/dashboard`

### Recommandations

- `GET /api/recommendations`
- `GET /api/children/{id}/recommendations`

### IA activite

- `POST /api/activities/generate-description`

## Ressources JSON a prevoir

### User

- id
- nom
- prenom
- fullName
- email
- telephone
- roles
- isActive
- createdAt

### Child

- id
- nom
- prenom
- fullName
- dateNaissance
- age
- sexe
- parentId

### Category

- id
- nom
- description
- image

### Activity

- id
- titre
- description
- image
- dateActivite
- heureDebut
- heureFin
- capaciteMax
- ageMin
- ageMax
- prix
- statut
- lieu
- category
- placesDisponibles
- estDisponible

### Reservation

- id
- dateReservation
- statut
- child
- activity

## Upload image futur

Pour les categories et activites :

- utiliser `multipart/form-data`
- laisser Symfony gerer les fichiers
- JavaFX ne stocke jamais un chemin MySQL direct

## IA future

Les appels IA doivent rester cote Symfony :

- generation description activite
- recommandations explicatives

JavaFX doit seulement appeler l'API Symfony qui expose le resultat.

## Important

Ce plan est une projection pour plus tard.

Pour la premiere version JavaFX :

- utiliser des donnees mockees
- ne pas appeler Symfony
- ne pas toucher a MySQL
