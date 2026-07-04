# 07 - Reference des routes Symfony

## Remarque

Le tableau ci-dessous reprend les routes importantes observees dans les controles `#[Route]`.

Quand deux noms de route pointent vers la meme URL, les deux noms sont notes.

| Route | URL | Methode | Controleur | Role | Description | Ecran JavaFX |
|---|---|---|---|---|---|---|
| `app_front_home` | `/` | `GET` | `HomeController::index` | Public | Home publique | Optionnel |
| `app_login` | `/login` | `GET`,`POST` | `SecurityController::login` | Public | Connexion | `login.fxml` |
| `app_front_login` | `/login` | `GET`,`POST` | `SecurityController::login` | Public | Alias front login | `login.fxml` |
| `app_logout` | `/logout` | `GET` | `SecurityController::logout` | Connecte | Deconnexion | Action logout |
| `app_front_logout` | `/logout` | `GET` | `SecurityController::logout` | Connecte | Alias front logout | Action logout |
| `app_post_login_redirect` | `/post-login` | `GET` | `SecurityController::postLoginRedirect` | Connecte | Redirection selon role | Navigation JavaFX |
| `app_register` | `/register` | `GET`,`POST` | `RegistrationController::register` | Public | Inscription parent | `register.fxml` optionnel |
| `app_front_register` | `/register` | `GET`,`POST` | `RegistrationController::register` | Public | Alias front register | `register.fxml` optionnel |
| `app_front_parent_index` | `/parent` | `GET` | `ParentController::index` | `ROLE_PARENT` | Dashboard parent | `parent_dashboard.fxml` |
| `app_front_office_parent_dashboard` | `/parent` | `GET` | `ParentController::index` | `ROLE_PARENT` | Alias dashboard parent | `parent_dashboard.fxml` |
| `app_front_child_index` | `/parent/children` | `GET` | `ParentChildController::index` | `ROLE_PARENT` | Liste des enfants du parent | `parent_children.fxml` |
| `app_front_child_new` | `/parent/children/new` | `GET`,`POST` | `ParentChildController::new` | `ROLE_PARENT` | Ajouter un enfant | `parent_child_form.fxml` |
| `app_front_child_show` | `/parent/children/{id}` | `GET` | `ParentChildController::show` | `ROLE_PARENT` | Detail d'un enfant | `parent_child_details.fxml` |
| `app_front_child_edit` | `/parent/children/{id}/edit` | `GET`,`POST` | `ParentChildController::edit` | `ROLE_PARENT` | Modifier un enfant | `parent_child_form.fxml` |
| `app_front_child_delete` | `/parent/children/{id}/delete` | `POST` | `ParentChildController::delete` | `ROLE_PARENT` | Supprimer un enfant | Action suppression |
| `app_front_activity_index` | `/parent/activities` | `GET` | `ParentActivityController::index` | `ROLE_PARENT` | Liste des activites reservables | `parent_activities.fxml` |
| `app_front_activity_show` | `/parent/activities/{id}` | `GET` | `ParentActivityController::show` | `ROLE_PARENT` | Detail activite | `parent_activity_details.fxml` |
| `app_front_activity_reserve` | `/parent/activities/{id}/reserve` | `POST` | `ParentActivityController::reserve` | `ROLE_PARENT` | Reservation d'activite | Action reservation |
| `app_front_reservation_index` | `/parent/reservations` | `GET` | `ParentReservationController::index` | `ROLE_PARENT` | Liste des reservations du parent | `parent_reservations.fxml` |
| `app_front_reservation_show` | `/parent/reservations/{id}` | `GET` | `ParentReservationController::show` | `ROLE_PARENT` | Detail reservation | `parent_reservation_details.fxml` optionnel |
| `app_front_reservation_cancel` | `/parent/reservations/{id}/cancel` | `POST` | `ParentReservationController::cancel` | `ROLE_PARENT` | Annuler une reservation | Action annulation |
| `app_back_admin_index` | `/admin` | `GET` | `AdminController::index` | `ROLE_ADMIN` | Dashboard admin | `admin_dashboard.fxml` |
| `app_back_office_admin_dashboard` | `/admin` | `GET` | `AdminController::index` | `ROLE_ADMIN` | Alias dashboard admin | `admin_dashboard.fxml` |
| `app_back_category_index` | `/admin/categories` | `GET` | `CategoryController::index` | `ROLE_ADMIN` | Liste categories | `admin_categories.fxml` |
| `app_back_category_new` | `/admin/categories/new` | `GET`,`POST` | `CategoryController::new` | `ROLE_ADMIN` | Ajouter categorie | `admin_category_form.fxml` |
| `app_back_category_show` | `/admin/categories/{id}` | `GET` | `CategoryController::show` | `ROLE_ADMIN` | Detail categorie | `admin_category_details.fxml` optionnel |
| `app_back_category_edit` | `/admin/categories/{id}/edit` | `GET`,`POST` | `CategoryController::edit` | `ROLE_ADMIN` | Modifier categorie | `admin_category_form.fxml` |
| `app_back_category_delete` | `/admin/categories/{id}/delete` | `POST` | `CategoryController::delete` | `ROLE_ADMIN` | Supprimer categorie | Action suppression |
| `app_back_activity_index` | `/admin/activities` | `GET` | `ActivityController::index` | `ROLE_ADMIN` | Liste activites | `admin_activities.fxml` |
| `app_back_activity_new` | `/admin/activities/new` | `GET`,`POST` | `ActivityController::new` | `ROLE_ADMIN` | Ajouter activite | `admin_activity_form.fxml` |
| `app_back_activity_show` | `/admin/activities/{id}` | `GET` | `ActivityController::show` | `ROLE_ADMIN` | Detail activite | `admin_activity_details.fxml` optionnel |
| `app_back_activity_edit` | `/admin/activities/{id}/edit` | `GET`,`POST` | `ActivityController::edit` | `ROLE_ADMIN` | Modifier activite | `admin_activity_form.fxml` |
| `app_back_activity_delete` | `/admin/activities/{id}/delete` | `POST` | `ActivityController::delete` | `ROLE_ADMIN` | Supprimer activite | Action suppression |
| `app_back_office_activity_generate_description` | `/admin/activities/generate-description` | `POST` | `ActivityAiController::__invoke` | `ROLE_ADMIN` | Generation IA de description | Action IA |
| `app_back_user_index` | `/admin/users` | `GET` | `UserController::index` | `ROLE_ADMIN` | Liste utilisateurs | `admin_users.fxml` |
| `app_back_user_new` | `/admin/users/new` | `GET`,`POST` | `UserController::new` | `ROLE_ADMIN` | Ajouter utilisateur | `admin_user_form.fxml` |
| `app_back_user_show` | `/admin/users/{id}` | `GET` | `UserController::show` | `ROLE_ADMIN` | Detail utilisateur | `admin_user_details.fxml` optionnel |
| `app_back_user_edit` | `/admin/users/{id}/edit` | `GET`,`POST` | `UserController::edit` | `ROLE_ADMIN` | Modifier utilisateur | `admin_user_form.fxml` |
| `app_back_user_toggle_status` | `/admin/users/{id}/toggle-status` | `POST` | `UserController::toggleStatus` | `ROLE_ADMIN` | Activer ou desactiver utilisateur | Action changement statut |
| `app_back_user_delete` | `/admin/users/{id}/delete` | `POST` | `UserController::delete` | `ROLE_ADMIN` | Supprimer utilisateur | Action suppression |
| `app_back_child_index` | `/admin/children` | `GET` | `ChildController::index` | `ROLE_ADMIN` | Liste enfants | `admin_children.fxml` |
| `app_back_child_show` | `/admin/children/{id}` | `GET` | `ChildController::show` | `ROLE_ADMIN` | Detail enfant | `admin_child_details.fxml` optionnel |
| `app_back_reservation_index` | `/admin/reservations` | `GET` | `ReservationController::index` | `ROLE_ADMIN` | Liste reservations | `admin_reservations.fxml` |
| `app_back_reservation_show` | `/admin/reservations/{id}` | `GET` | `ReservationController::show` | `ROLE_ADMIN` | Detail reservation | `admin_reservation_details.fxml` optionnel |
| `app_back_reservation_confirm` | `/admin/reservations/{id}/confirm` | `POST` | `ReservationController::confirm` | `ROLE_ADMIN` | Confirmer reservation | Action confirmation |
| `app_back_reservation_cancel` | `/admin/reservations/{id}/cancel` | `POST` | `ReservationController::cancel` | `ROLE_ADMIN` | Annuler reservation | Action annulation |

## Information non trouvee dans le code actuel

- Aucune route admin de creation ou edition d'enfant n'a ete trouvee.
- Aucune route admin de creation ou edition manuelle de reservation n'a ete trouvee.
