# 04 - Reference du Front-Office Parent

## Home publique

### Route Symfony

- `/`

### Controleur

- `App\Controller\FrontOffice\HomeController::index()`

### Template

- `templates/front_office/home/index.html.twig`

### Donnees affichees

- hero marketing
- univers artistiques statiques
- statistiques marketing statiques
- etapes de fonctionnement
- boutons login/register

### Actions

- aller au login
- aller au register
- acces dashboard si deja connecte

### Equivalent JavaFX

Optionnel dans la future application JavaFX. La priorite JavaFX est surtout l'authentification et les espaces parent/admin.

## Login

### Route Symfony

- `/login`

### Controleur

- `App\Controller\FrontOffice\SecurityController::login()`

### Template

- `templates/front_office/security/login.html.twig`

### Donnees affichees

- dernier username saisi
- erreur de connexion si presente

### Actions

- saisir email
- saisir mot de passe
- soumettre le formulaire de connexion
- lien vers inscription parent

### Equivalent JavaFX a creer

- `login.fxml`
- `LoginController.java`

## Register parent

### Route Symfony

- `/register`

### Controleur

- `App\Controller\FrontOffice\RegistrationController::register()`

### Template

- `templates/front_office/registration/register.html.twig`

### Formulaire

- `App\Form\FrontOffice\RegistrationFormType`

### Donnees affichees

- nom
- prenom
- email
- telephone
- mot de passe
- confirmation mot de passe

### Actions

- creer un compte parent
- redirection login apres succes

### Equivalent JavaFX a creer

Si la version JavaFX reproduit l'inscription mockee :

- `register.fxml`
- `RegisterController.java`

Si l'inscription n'est pas implementee dans la premiere version JavaFX, noter que l'ecran existe cote Symfony mais pourra etre reporte.

## Dashboard parent

### Routes Symfony

- `/parent`

### Controleur

- `App\Controller\FrontOffice\ParentController::index()`

### Template

- `templates/front_office/parent/dashboard.html.twig`

### Donnees affichees

- parent connecte
- date du jour
- nombre d'enfants
- nombre total de reservations
- nombre de reservations confirmees
- nombre de reservations en attente
- nombre de prochaines activites
- liste des enfants
- prochaines activites reservees
- recommandations intelligentes par enfant
- historique recent des reservations

### Services utilises

- `ParentStatisticService`
- `RecommendationService`
- `AiRecommendationService`

### Actions

- ajouter un enfant
- voir les activites
- voir les reservations
- voir / modifier / supprimer un enfant
- voir la fiche enfant
- voir le detail d'une activite recommandee
- reserver une activite recommandee

### Equivalent JavaFX a creer

- `parent_dashboard.fxml`
- `ParentDashboardController.java`

## Mes enfants

### Route Symfony

- `/parent/children`

### Controleur

- `App\Controller\FrontOffice\ParentChildController::index()`

### Template

- `templates/front_office/child/index.html.twig`

### Donnees affichees

- nom complet
- date de naissance
- age
- sexe
- nombre de reservations

### Actions

- voir un enfant
- modifier un enfant
- supprimer un enfant
- aller vers les activites filtrees par age
- ajouter un enfant

### Equivalent JavaFX a creer

- `parent_children.fxml`
- `ParentChildController.java`

## Ajouter un enfant

### Route Symfony

- `/parent/children/new`

### Controleur

- `App\Controller\FrontOffice\ParentChildController::new()`

### Template

- `templates/front_office/child/new.html.twig`

### Formulaire

- `App\Form\FrontOffice\ChildType`

### Champs

- nom
- prenom
- dateNaissance
- sexe

### Actions

- enregistrer un enfant rattache automatiquement au parent connecte

### Equivalent JavaFX a creer

- `parent_child_form.fxml`
- `ParentChildFormController.java`

## Modifier un enfant

### Route Symfony

- `/parent/children/{id}/edit`

### Controleur

- `App\Controller\FrontOffice\ParentChildController::edit()`

### Template

- `templates/front_office/child/edit.html.twig`

### Formulaire

- `App\Form\FrontOffice\ChildType`

### Actions

- modifier uniquement son propre enfant

### Equivalent JavaFX a creer

- meme ecran que l'ajout, en mode edition

## Detail enfant

### Route Symfony

- `/parent/children/{id}`

### Controleur

- `App\Controller\FrontOffice\ParentChildController::show()`

### Template

- `templates/front_office/child/show.html.twig`

### Donnees affichees

- date de naissance
- age
- sexe
- nombre de reservations
- historique de reservations
- activites recommandees pour cet enfant

### Actions

- modifier l'enfant
- retour a la liste
- voir detail d'une reservation
- voir detail d'une activite recommandee
- reserver une activite recommandee

### Equivalent JavaFX a creer

- `parent_child_details.fxml` ou fusion avec `parent_children.fxml`

## Liste des activites

### Route Symfony

- `/parent/activities`

### Controleur

- `App\Controller\FrontOffice\ParentActivityController::index()`

### Template

- `templates/front_office/activity/index.html.twig`

### Donnees affichees

- activites disponibles uniquement
- categorie
- statut
- date
- horaire
- lieu
- prix
- age min/max
- places disponibles

### Filtres

- recherche par titre (`q`)
- categorie (`category`)
- age (`age`)

### Actions

- filtrer
- voir le detail d'une activite

### Equivalent JavaFX a creer

- `parent_activities.fxml`
- `ParentActivityController.java`

## Detail activite

### Route Symfony

- `/parent/activities/{id}`

### Controleur

- `App\Controller\FrontOffice\ParentActivityController::show()`

### Template

- `templates/front_office/activity/show.html.twig`

### Donnees affichees

- image
- categorie
- titre
- description
- date
- horaire
- lieu
- prix
- age autorise
- places disponibles
- statut
- liste des enfants compatibles
- compatibilite de tous les enfants du parent
- autres suggestions proches

### Actions

- retour a la liste
- reserver une activite pour un enfant compatible

### Equivalent JavaFX a creer

- `parent_activity_details.fxml`
- `ParentActivityDetailsController.java`

## Reservation d'activite

### Route Symfony

- `/parent/activities/{id}/reserve`

### Controleur

- `App\Controller\FrontOffice\ParentActivityController::reserve()`

### Methode

- `POST`

### Saisie attendue

- `_token`
- `child_id`

### Comportement

- verifie le token CSRF
- verifie l'existence d'au moins un enfant
- verifie que l'enfant choisi appartient au parent
- appelle `ReservationService`
- redirige apres succes ou erreur

### Equivalent JavaFX a creer

- action "Reserver" dans `ParentActivityDetailsController`

## Mes reservations

### Route Symfony

- `/parent/reservations`

### Controleur

- `App\Controller\FrontOffice\ParentReservationController::index()`

### Template

- `templates/front_office/reservation/index.html.twig`

### Donnees affichees

- id reservation
- enfant
- activite
- categorie
- date activite
- date reservation
- statut

### Actions

- voir detail reservation
- annuler reservation si statut non annule / non termine

### Equivalent JavaFX a creer

- `parent_reservations.fxml`
- `ParentReservationController.java`

## Detail reservation

### Route Symfony

- `/parent/reservations/{id}`

### Controleur

- `App\Controller\FrontOffice\ParentReservationController::show()`

### Template

- `templates/front_office/reservation/show.html.twig`

### Donnees affichees

- Information non verifiee en detail dans le template actuel. Le controleur fournit un objet `reservation`.

### Equivalent JavaFX a creer

- ecran detail optionnel
- ou detail integre dans la liste

## Annulation reservation

### Route Symfony

- `/parent/reservations/{id}/cancel`

### Controleur

- `App\Controller\FrontOffice\ParentReservationController::cancel()`

### Methode

- `POST`

### Comportement

- verifie que la reservation appartient au parent
- verifie le token CSRF
- appelle `ReservationService::cancelReservation()`
- redirige vers la liste

### Equivalent JavaFX a creer

- bouton "Annuler" dans `parent_reservations.fxml`
