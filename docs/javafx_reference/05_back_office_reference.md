# 05 - Reference du Back-Office Admin

## Dashboard admin

### Route Symfony

- `/admin`

### Controleur

- `App\Controller\BackOffice\AdminController::index()`

### Template

- `templates/back_office/admin/dashboard.html.twig`

### Services utilises

- `AdminStatisticService`

### Donnees affichees

- cartes statistiques globales
- resume global
- activite la plus reservee
- categorie la plus populaire
- parent le plus actif
- enfant le plus actif
- derniere reservation
- dernier parent cree
- taux moyen de remplissage
- total des places disponibles
- total des places reservees
- activites bientot completes
- graphiques Chart.js
- dernieres reservations
- prochaines activites

### Actions

- creer un utilisateur
- ajouter une activite
- naviguer vers les modules admin

### Equivalent JavaFX

- `admin_dashboard.fxml`
- `AdminDashboardController.java`

## Gestion des categories

### Liste categories

#### Route Symfony

- `/admin/categories`

#### Controleur

- `App\Controller\BackOffice\CategoryController::index()`

#### Template

- `templates/back_office/category/index.html.twig`

#### Donnees affichees

- image
- nom
- description
- nombre d'activites

#### Actions

- voir
- modifier
- supprimer
- creer une categorie

#### Equivalent JavaFX

- `admin_categories.fxml`
- `AdminCategoryController.java`

### Ajouter categorie

#### Route Symfony

- `/admin/categories/new`

#### Controleur

- `App\Controller\BackOffice\CategoryController::new()`

#### Template

- `templates/back_office/category/new.html.twig`

#### Formulaire

- `App\Form\BackOffice\CategoryType`

#### Champs

- nom
- description
- imageFile (upload non mappe)

#### Services impliques

- `ImageUploaderService`

#### Equivalent JavaFX

- `admin_category_form.fxml`
- `AdminCategoryFormController.java`

### Modifier categorie

#### Route Symfony

- `/admin/categories/{id}/edit`

#### Controleur

- `App\Controller\BackOffice\CategoryController::edit()`

#### Template

- `templates/back_office/category/edit.html.twig`

#### Comportement

- conserve l'ancienne image si aucun nouveau fichier
- remplace l'image si nouveau fichier
- supprime l'ancienne image physique apres remplacement

### Supprimer categorie

#### Route Symfony

- `/admin/categories/{id}/delete`

#### Controleur

- `App\Controller\BackOffice\CategoryController::delete()`

#### Methode

- `POST`

#### Regles

- token CSRF obligatoire
- suppression interdite si la categorie possede encore des activites
- l'image physique est supprimee apres suppression

## Gestion des activites

### Liste activites

#### Route Symfony

- `/admin/activities`

#### Controleur

- `App\Controller\BackOffice\ActivityController::index()`

#### Template

- `templates/back_office/activity/index.html.twig`

#### Donnees affichees

- image
- titre
- categorie
- date
- capacite
- statut

#### Actions

- voir
- modifier
- supprimer
- creer une activite

#### Equivalent JavaFX

- `admin_activities.fxml`
- `AdminActivityController.java`

### Ajouter activite

#### Route Symfony

- `/admin/activities/new`

#### Controleur

- `App\Controller\BackOffice\ActivityController::new()`

#### Template

- `templates/back_office/activity/new.html.twig`

#### Formulaire

- `App\Form\BackOffice\ActivityType`

#### Champs

- titre
- description
- imageFile
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

#### Services impliques

- `ImageUploaderService`

### Modifier activite

#### Route Symfony

- `/admin/activities/{id}/edit`

#### Controleur

- `App\Controller\BackOffice\ActivityController::edit()`

#### Comportement

- mise a jour des dates et statuts
- gestion de remplacement d'image
- conservation image si aucun nouveau fichier

### Supprimer activite

#### Route Symfony

- `/admin/activities/{id}/delete`

#### Controleur

- `App\Controller\BackOffice\ActivityController::delete()`

#### Regles

- token CSRF obligatoire
- suppression interdite si reservations actives
- suppression de l'image physique si l'activite est bien supprimee

## Generation IA de description d'activite

### Route Symfony

- `/admin/activities/generate-description`

### Controleur

- `App\Controller\BackOffice\ActivityAiController`

### Methode

- `POST`

### Format

- requete JSON
- reponse JSON

### Securite

- `ROLE_ADMIN`
- verification CSRF via header `X-CSRF-TOKEN`

### Validation

- titre obligatoire
- ageMin / ageMax entiers si fournis
- ageMax >= ageMin

### Service implique

- `ActivityAiService`

### Equivalent JavaFX

- bouton "Generer description"
- appel a un service local mock dans un premier temps
- `ActivityAiHelperService.java` ou integration dans `ActivityService`

## Upload image

### Service Symfony

- `App\Service\ImageUploaderService`

### Dossiers utilises

- `public/uploads/categories`
- `public/uploads/activities`

### Regles

- seuls les noms de fichiers sont stockes en base
- categories : upload et suppression supportes
- activites : upload et suppression supportes
- extensions controlees par les formulaires Symfony

### Equivalent JavaFX

- pour la premiere version mockee : champ texte image ou simulation locale
- plus tard : upload via API Symfony

## Gestion des utilisateurs

### Liste utilisateurs

#### Route Symfony

- `/admin/users`

#### Controleur

- `App\Controller\BackOffice\UserController::index()`

#### Template

- `templates/back_office/user/index.html.twig`

#### Donnees affichees

- id
- nom complet
- email
- telephone
- role
- statut actif/desactive
- date creation
- nombre d'enfants

#### Actions

- voir
- modifier
- activer/desactiver
- supprimer
- creer un utilisateur

#### Equivalent JavaFX

- `admin_users.fxml`
- `AdminUserController.java`

### Ajouter utilisateur

#### Route Symfony

- `/admin/users/new`

#### Controleur

- `App\Controller\BackOffice\UserController::new()`

#### Formulaire

- `App\Form\BackOffice\UserType`

#### Champs

- nom
- prenom
- email
- telephone
- role
- plainPassword
- isActive

#### Regles

- mot de passe requis a la creation
- role parent par defaut

### Modifier utilisateur

#### Route Symfony

- `/admin/users/{id}/edit`

#### Controleur

- `App\Controller\BackOffice\UserController::edit()`

#### Regles importantes

- interdiction de retirer son propre role admin
- interdiction de se desactiver soi-meme
- protection du dernier admin
- mot de passe optionnel en edition

### Activer / desactiver utilisateur

#### Route Symfony

- `/admin/users/{id}/toggle-status`

#### Methode

- `POST`

### Supprimer utilisateur

#### Route Symfony

- `/admin/users/{id}/delete`

#### Methode

- `POST`

#### Regles

- auto-suppression interdite
- dernier admin non supprimable
- si l'utilisateur a des enfants, il est desactive au lieu d'etre supprime

## Gestion des enfants admin

### Liste enfants

#### Route Symfony

- `/admin/children`

#### Controleur

- `App\Controller\BackOffice\ChildController::index()`

#### Template

- `templates/back_office/child/index.html.twig`

#### Donnees affichees

- enfants avec parent associe

### Detail enfant

#### Route Symfony

- `/admin/children/{id}`

#### Controleur

- `App\Controller\BackOffice\ChildController::show()`

#### Template

- `templates/back_office/child/show.html.twig`

### Information non trouvee dans le code actuel

- aucune route admin pour creer, modifier ou supprimer un enfant n'a ete trouvee

## Gestion des reservations admin

### Liste reservations

#### Route Symfony

- `/admin/reservations`

#### Controleur

- `App\Controller\BackOffice\ReservationController::index()`

#### Template

- `templates/back_office/reservation/index.html.twig`

#### Donnees affichees

- id
- parent
- enfant
- activite
- statut
- date de reservation

#### Actions

- voir
- confirmer
- annuler

#### Equivalent JavaFX

- `admin_reservations.fxml`
- `AdminReservationController.java`

### Detail reservation

#### Route Symfony

- `/admin/reservations/{id}`

#### Controleur

- `App\Controller\BackOffice\ReservationController::show()`

#### Template

- `templates/back_office/reservation/show.html.twig`

### Confirmer reservation

#### Route Symfony

- `/admin/reservations/{id}/confirm`

#### Methode

- `POST`

#### Regles

- token CSRF
- impossible si reservation annulee
- impossible si activite annulee / terminee / passee
- impossible si capacite deja depassee

### Annuler reservation

#### Route Symfony

- `/admin/reservations/{id}/cancel`

#### Methode

- `POST`

#### Regles

- token CSRF
- passe le statut a annule
- remet a jour le statut de l'activite
