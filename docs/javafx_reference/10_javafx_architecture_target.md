# 10 - Architecture cible recommandee pour JavaFX

## Structure recommandee

```text
src/main/java/com/artkids/
├── MainApp.java
├── config/
├── model/
├── enums/
├── service/
├── controller/
│   ├── auth/
│   ├── frontoffice/
│   └── backoffice/
└── util/

src/main/resources/com/artkids/
├── view/
│   ├── auth/
│   ├── frontoffice/
│   └── backoffice/
├── css/
└── images/
```

## Role de chaque dossier

### `MainApp.java`

Point d'entree JavaFX.

Responsabilites :

- initialiser l'application
- charger la premiere scene
- initialiser les services globaux si necessaire

### `config/`

Configuration applicative.

Exemples :

- constantes globales
- chemins
- configuration API future
- session applicative

### `model/`

Contient les classes metier equivalentes aux entites Symfony :

- `User`
- `Child`
- `Category`
- `Activity`
- `Reservation`

### `enums/`

Contient les enums equivalents aux enums Symfony :

- `UserRole`
- `Sexe`
- `ActivityStatus`
- `ReservationStatus`

### `service/`

Contient la logique metier et les sources de donnees.

Services recommandes :

- `AuthService`
- `MockDataService`
- `ChildService`
- `CategoryService`
- `ActivityService`
- `ReservationService`
- `UserService`
- `ApiClient`

#### Role de `MockDataService`

Centraliser les donnees en memoire pour la premiere version desktop.

Il pourra contenir :

- utilisateurs mockes
- enfants mockes
- categories mockees
- activites mockees
- reservations mockees

#### Role de `ApiClient`

Preparer la future integration REST.

Exemples de methodes :

- `get()`
- `post()`
- `put()`
- `delete()`

### `controller/auth/`

Controleurs des vues d'authentification.

Exemples :

- `LoginController`
- `RegisterController` optionnel

### `controller/frontoffice/`

Controleurs de l'espace parent.

Exemples :

- `ParentDashboardController`
- `ParentChildController`
- `ParentChildFormController`
- `ParentActivityController`
- `ParentActivityDetailsController`
- `ParentReservationController`

### `controller/backoffice/`

Controleurs de l'espace admin.

Exemples :

- `AdminDashboardController`
- `AdminCategoryController`
- `AdminCategoryFormController`
- `AdminActivityController`
- `AdminActivityFormController`
- `AdminUserController`
- `AdminUserFormController`
- `AdminChildController`
- `AdminReservationController`

### `util/`

Outils techniques reutilisables.

Exemples :

- `SceneManager`
- `AlertUtils`
- `DateUtils`
- `SessionContext`

#### Role de `SceneManager`

Centraliser :

- le changement de scene
- le chargement FXML
- la navigation parent/admin
- la deconnexion

#### Role de `AlertUtils`

Uniformiser :

- messages d'information
- messages d'erreur
- boites de confirmation

#### Role de `DateUtils`

Mutualiser :

- calcul d'age
- formatage de dates
- comparaisons temporelles

### `resources/view/`

Contient les fichiers FXML.

Separations recommandees :

- `auth/`
- `frontoffice/`
- `backoffice/`

### `resources/css/`

Contient les feuilles de style JavaFX.

Fichiers recommandes :

- `theme.css`
- `components.css`
- `forms.css`
- `tables.css`
- `frontoffice.css`
- `backoffice.css`

### `resources/images/`

Contient :

- logo
- placeholders
- illustrations

## Principes d'architecture

### 1. Ne pas mettre la logique metier dans les controllers

Les controllers doivent :

- recuperer les donnees du formulaire
- appeler les services
- mettre a jour la vue

### 2. Laisser les services porter les regles metier

Exemples :

- controle de reservation
- verification des tranches d'age
- verification des capacites
- protection des suppressions

### 3. Garder la separation Parent / Admin

Comme dans Symfony :

- vues differentes
- controllers differents
- services communs quand c'est pertinent

### 4. Garder des modeles proches de Symfony

Le mapping conceptuel sera plus simple plus tard pour l'API.

### 5. Prevoir la migration vers API sans casser la premiere version mockee

Le bon schema est :

- version 1 : `MockDataService`
- version 2 : services bascules vers `ApiClient`

## Mapping fonctionnel conseille

- `AuthService` -> login mock puis API
- `ChildService` -> CRUD enfant et verification propriete
- `ActivityService` -> filtrage activites, disponibilite, details
- `ReservationService` -> creation, annulation, controle capacite
- `UserService` -> gestion admin des utilisateurs

## Conclusion

L'architecture JavaFX doit rester :

- simple
- modulaire
- proche du domaine Symfony
- facile a faire evoluer vers une consommation REST reelle
