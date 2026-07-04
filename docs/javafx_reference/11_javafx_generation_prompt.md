# 11 - Prompt de generation JavaFX

Copier le texte ci-dessous dans Codex, mais uniquement dans un repository JavaFX vierge ou dedie a l'application desktop.

---

Tu travailles dans un repository JavaFX vierge destine a accueillir une nouvelle application desktop nommee ArtKids.

Tu disposes de documents de reference issus d'un projet Symfony existant. Utilise-les comme source de verite fonctionnelle :

- `docs/javafx_reference/01_project_overview.md`
- `docs/javafx_reference/02_entities_reference.md`
- `docs/javafx_reference/03_roles_and_security.md`
- `docs/javafx_reference/04_front_office_reference.md`
- `docs/javafx_reference/05_back_office_reference.md`
- `docs/javafx_reference/06_business_rules.md`
- `docs/javafx_reference/07_routes_reference.md`
- `docs/javafx_reference/08_ui_screens_reference.md`
- `docs/javafx_reference/09_future_api_integration_plan.md`
- `docs/javafx_reference/10_javafx_architecture_target.md`

Objectif :
Creer une application JavaFX complete qui reprend le concept du projet Symfony ArtKids, mais sans creer de dossier Symfony ni tenter de modifier Symfony.

Contraintes obligatoires :

- Tu travailles dans un repository JavaFX uniquement.
- Ne cree pas de projet Symfony.
- Ne cree pas de fichiers Twig.
- Ne cree pas de configuration Doctrine ou MySQL directe.
- Ne modifie pas un backend Symfony ici.
- Utilise Java 17 ou plus.
- Utilise JavaFX.
- Utilise Maven.
- Utilise FXML.
- Utilise CSS JavaFX.
- Utilise des donnees mockees pour cette premiere version.
- Cree un `MockDataService`.
- Cree un `ApiClient` mais ne l'utilise pas encore reellement.
- Prepare la future integration REST avec Symfony sans l'implementer maintenant.

Architecture cible a respecter :

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

Fonctionnalites a creer :

1. Login mocke
- comptes de test :
  - admin@artkids.com / admin123 / ADMIN
  - parent@artkids.com / parent123 / PARENT
- si ADMIN -> dashboard admin
- si PARENT -> dashboard parent

2. Front-Office Parent
- dashboard parent
- liste de ses enfants
- ajout enfant
- modification enfant
- suppression enfant
- liste des activites disponibles
- detail activite
- reservation activite
- liste de ses reservations
- annulation reservation

3. Back-Office Admin
- dashboard admin
- gestion categories
- gestion activites
- gestion utilisateurs
- consultation enfants
- gestion reservations

4. Modeles Java a creer
- User
- Child
- Category
- Activity
- Reservation

5. Enums a creer
- UserRole
- Sexe
- ActivityStatus
- ReservationStatus

6. Services a creer
- AuthService
- MockDataService
- ChildService
- CategoryService
- ActivityService
- ReservationService
- UserService
- ApiClient

7. Regles metier a reproduire
- un parent ne peut gerer que ses propres enfants
- un parent ne voit que ses propres reservations
- un enfant ne peut pas reserver deux fois la meme activite
- une activite complete ne peut plus etre reservee
- une activite annulee ne peut pas etre reservee
- une activite terminee ou passee ne peut pas etre reservee
- l'age de l'enfant doit etre entre ageMin et ageMax
- la capacite maximale ne doit pas etre depassee
- une categorie avec activites ne doit pas etre supprimee
- une activite avec reservations actives ne doit pas etre supprimee
- un admin ne doit pas supprimer son propre compte si tu implementes cette gestion
- le dernier admin ne doit pas etre supprime ou desactive si tu implementes cette gestion
- la date d'activite doit etre future
- l'heure de fin doit etre superieure a l'heure de debut
- ageMax doit etre superieur ou egal a ageMin

8. UI a creer
- `login.fxml`
- `parent_dashboard.fxml`
- `parent_children.fxml`
- `parent_child_form.fxml`
- `parent_activities.fxml`
- `parent_activity_details.fxml`
- `parent_reservations.fxml`
- `admin_dashboard.fxml`
- `admin_categories.fxml`
- `admin_category_form.fxml`
- `admin_activities.fxml`
- `admin_activity_form.fxml`
- `admin_users.fxml`
- `admin_user_form.fxml`
- `admin_children.fxml`
- `admin_reservations.fxml`

9. Design
- design moderne et professionnel
- separation claire parent / admin
- sidebar admin
- appbar parent
- cartes de statistiques
- tableaux propres
- formulaires lisibles
- boutons coherents
- palette ArtKids cohérente si disponible dans les docs

10. Navigation
- cree un `SceneManager`
- gere la session de l'utilisateur connecte
- gere la deconnexion

11. Lancement
- le projet doit etre lancable avec :

```bash
mvn clean javafx:run
```

12. Qualite attendue
- architecture propre
- controllers legers
- logique metier dans les services
- code lisible et maintenable
- commentaires seulement si necessaires

Important :
- Base-toi uniquement sur les documents `docs/javafx_reference/`
- Ne reinvente pas la logique metier si elle est documentee
- Si une information manque, fais une hypothese raisonnable et indique-la dans ta reponse finale
- N'essaie pas de connecter MySQL
- N'essaie pas de consommer Symfony maintenant
- Prepare seulement une premiere version JavaFX complete avec donnees mockees

---
