# 08 - Reference des ecrans JavaFX a reproduire

## Auth

### `login.fxml`

#### Controller JavaFX recommande

- `LoginController.java`

#### Objectif

Permettre la connexion mockee ou future connexion API d'un parent ou d'un admin.

#### Composants UI necessaires

- champ email
- champ mot de passe
- bouton se connecter
- message d'erreur
- eventuellement boutons de demo

#### Boutons

- Se connecter

#### Validations

- email obligatoire
- mot de passe obligatoire

#### Donnees mockees necessaires

- compte admin mock
- compte parent mock

### `register.fxml` optionnel

#### Controller JavaFX recommande

- `RegisterController.java`

#### Objectif

Reproduire plus tard l'inscription parent publique.

#### Champs

- nom
- prenom
- email
- telephone
- mot de passe
- confirmation mot de passe

#### Validations

- champs obligatoires
- email valide
- mots de passe identiques

## Front-Office Parent

### `parent_dashboard.fxml`

#### Controller JavaFX recommande

- `ParentDashboardController.java`

#### Objectif

Afficher la vue globale de l'espace parent.

#### Composants UI necessaires

- cartes statistiques
- bloc parent connecte
- liste des enfants
- liste des prochaines reservations
- liste des recommandations
- historique recent
- boutons d'action rapide

#### Donnees mockees necessaires

- utilisateur parent connecte
- enfants du parent
- reservations du parent
- recommandations par enfant

### `parent_children.fxml`

#### Controller JavaFX recommande

- `ParentChildController.java`

#### Objectif

Lister les enfants du parent connecte.

#### Composants UI necessaires

- tableau enfants
- colonnes : nom complet, date naissance, age, sexe, reservations
- boutons ajouter, voir, modifier, supprimer

#### Validations metier

- ne montrer que les enfants du parent courant

### `parent_child_form.fxml`

#### Controller JavaFX recommande

- `ParentChildFormController.java`

#### Objectif

Ajouter ou modifier un enfant.

#### Champs

- nom
- prenom
- date de naissance
- sexe

#### Boutons

- Enregistrer
- Annuler / Retour

#### Validations

- nom obligatoire
- prenom obligatoire
- date de naissance obligatoire
- date de naissance non future
- sexe obligatoire

### `parent_child_details.fxml` optionnel

#### Controller JavaFX recommande

- `ParentChildDetailsController.java`

#### Objectif

Afficher les informations d'un enfant, son historique et ses recommandations.

#### Composants

- infos enfant
- liste reservations
- cartes recommandations

### `parent_activities.fxml`

#### Controller JavaFX recommande

- `ParentActivityController.java`

#### Objectif

Afficher les activites ouvertes et filtrables.

#### Composants UI necessaires

- filtre texte titre
- filtre categorie
- filtre age
- tableau ou cartes activites
- bouton voir details

#### Donnees mockees necessaires

- categories
- activites ouvertes, futures et reservables

### `parent_activity_details.fxml`

#### Controller JavaFX recommande

- `ParentActivityDetailsController.java`

#### Objectif

Afficher le detail d'une activite et permettre la reservation.

#### Composants UI necessaires

- image
- titre
- categorie
- description
- date
- horaire
- lieu
- prix
- age min/max
- places restantes
- statut
- select enfant
- liste compatibilite enfants
- suggestions proches

#### Boutons

- Reserver
- Retour aux activites

#### Validations

- enfant obligatoire
- enfant compatible
- activite reservable

### `parent_reservations.fxml`

#### Controller JavaFX recommande

- `ParentReservationController.java`

#### Objectif

Afficher les reservations du parent et permettre l'annulation si autorisee.

#### Composants UI necessaires

- tableau reservations
- colonnes : enfant, activite, categorie, date activite, date reservation, statut
- bouton voir detail optionnel
- bouton annuler

#### Validations

- annulation seulement si autorisee par la logique metier

### `parent_reservation_details.fxml` optionnel

#### Controller JavaFX recommande

- `ParentReservationDetailsController.java`

#### Objectif

Afficher le detail d'une reservation unique.

#### Information non trouvee dans le code actuel

- Le template detail reservation n'a pas ete audite champ par champ ici.

## Back-Office Admin

### `admin_dashboard.fxml`

#### Controller JavaFX recommande

- `AdminDashboardController.java`

#### Objectif

Afficher les statistiques globales et les listes de synthese.

#### Composants UI necessaires

- cartes statistiques
- panneaux resume
- listes ou tableaux dernieres reservations
- listes ou tableaux prochaines activites
- zone reservee pour futurs graphiques desktop

### `admin_categories.fxml`

#### Controller JavaFX recommande

- `AdminCategoryController.java`

#### Objectif

Lister les categories et permettre les actions CRUD principales.

#### Composants UI necessaires

- tableau categories
- colonnes : image, nom, description, nombre d'activites
- boutons creer, voir, modifier, supprimer

### `admin_category_form.fxml`

#### Controller JavaFX recommande

- `AdminCategoryFormController.java`

#### Objectif

Ajouter ou modifier une categorie.

#### Champs

- nom
- description
- image

#### Validations

- nom obligatoire
- nom unique en logique mock si souhaite

### `admin_activities.fxml`

#### Controller JavaFX recommande

- `AdminActivityController.java`

#### Objectif

Lister les activites du catalogue.

#### Composants UI necessaires

- tableau activites
- image
- titre
- categorie
- date
- capacite
- statut
- actions CRUD

### `admin_activity_form.fxml`

#### Controller JavaFX recommande

- `AdminActivityFormController.java`

#### Objectif

Ajouter ou modifier une activite.

#### Champs

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
- categorie

#### Fonctions optionnelles

- bouton "Generer une description avec IA" mock

#### Validations

- titre obligatoire
- description obligatoire
- categorie obligatoire
- date future
- heure fin > heure debut
- capacite > 0
- age max >= age min

### `admin_users.fxml`

#### Controller JavaFX recommande

- `AdminUserController.java`

#### Objectif

Lister et administrer les comptes utilisateurs.

#### Composants UI necessaires

- tableau utilisateurs
- nom complet
- email
- telephone
- role
- statut
- date creation
- actions edit / toggle / delete

### `admin_user_form.fxml`

#### Controller JavaFX recommande

- `AdminUserFormController.java`

#### Objectif

Ajouter ou modifier un utilisateur.

#### Champs

- nom
- prenom
- email
- telephone
- role
- mot de passe
- actif

#### Validations

- email obligatoire
- email valide
- mot de passe obligatoire a la creation
- role obligatoire

### `admin_children.fxml`

#### Controller JavaFX recommande

- `AdminChildController.java`

#### Objectif

Lister les enfants avec leur parent associe.

#### Composants UI necessaires

- tableau enfants
- nom complet
- parent
- date de naissance
- age
- sexe

### `admin_child_details.fxml` optionnel

#### Controller JavaFX recommande

- `AdminChildDetailsController.java`

#### Objectif

Afficher le detail d'un enfant en lecture seule.

### `admin_reservations.fxml`

#### Controller JavaFX recommande

- `AdminReservationController.java`

#### Objectif

Lister les reservations et permettre confirmation / annulation.

#### Composants UI necessaires

- tableau reservations
- parent
- enfant
- activite
- statut
- date reservation
- boutons confirmer / annuler / voir

#### Validations

- empecher confirmation ou annulation invalides selon regles metier

## Services mockes necessaires pour JavaFX

- `AuthService`
- `MockDataService`
- `ChildService`
- `CategoryService`
- `ActivityService`
- `ReservationService`
- `UserService`
- `ApiClient` en attente d'integration future
