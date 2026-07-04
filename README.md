# ArtKids JavaFX

Application desktop JavaFX de gestion et de reservation d'activites artistiques pour enfants, basee sur la documentation fonctionnelle du projet Symfony ArtKids.

## Objectif

Cette application reproduit la separation fonctionnelle du projet de reference :

- Front-Office Parent
- Back-Office Admin

Pour cette etape, l'application fonctionne uniquement avec des donnees mockees en memoire. Le projet Symfony n'est pas modifie et aucune connexion JDBC/MySQL n'est utilisee.

## Technologies

- Java 17
- JavaFX
- Maven
- FXML
- CSS JavaFX
- Architecture MVC / Service / Model

## Installation

Prerequis :

- Java 17+
- Maven 3.9+

Depuis la racine du repository :

```bash
mvn clean javafx:run
```

## Comptes de test

Admin :

- `admin@artkids.com`
- `admin123`

Parent :

- `parent@artkids.com`
- `parent123`

## Fonctionnalites disponibles

- Connexion mockee admin / parent
- Dashboard parent
- Gestion des enfants du parent
- Consultation des activites ouvertes
- Reservation d'activites pour les enfants du parent
- Consultation et annulation des reservations
- Dashboard admin
- CRUD categories
- CRUD activites
- CRUD utilisateurs
- Consultation de tous les enfants
- Gestion admin des reservations

## Structure du projet

```text
pom.xml
src/main/java/com/artkids/
src/main/resources/com/artkids/
docs/javafx_reference/
```

Principaux packages :

- `config` : configuration applicative et session
- `model` : modeles metier
- `enums` : enums du domaine
- `service` : logique metier et donnees mockees
- `controller` : controleurs JavaFX
- `util` : navigation, validations, alertes, dates

## Donnees mockees

L'application initialise en memoire :

- 1 administrateur
- 1 parent
- 2 enfants
- 6 categories
- plusieurs activites avec statuts varies
- plusieurs reservations

## Integration future avec Symfony

Le fichier `ApiClient.java` est present pour preparer une future integration REST avec Symfony.

Important pour cette version :

- Symfony n'est pas modifie
- aucune route API Symfony n'est appelee
- aucune base MySQL n'est touchee
- aucun acces JDBC n'est implemente

## Documentation de reference

La documentation fonctionnelle utilisee pour construire cette application est conservee dans :

- [docs/javafx_reference/01_project_overview.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/01_project_overview.md)
- [docs/javafx_reference/02_entities_reference.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/02_entities_reference.md)
- [docs/javafx_reference/03_roles_and_security.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/03_roles_and_security.md)
- [docs/javafx_reference/04_front_office_reference.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/04_front_office_reference.md)
- [docs/javafx_reference/05_back_office_reference.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/05_back_office_reference.md)
- [docs/javafx_reference/06_business_rules.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/06_business_rules.md)
- [docs/javafx_reference/07_routes_reference.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/07_routes_reference.md)
- [docs/javafx_reference/08_ui_screens_reference.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/08_ui_screens_reference.md)
- [docs/javafx_reference/09_future_api_integration_plan.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/09_future_api_integration_plan.md)
- [docs/javafx_reference/10_javafx_architecture_target.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/10_javafx_architecture_target.md)
- [docs/javafx_reference/11_javafx_generation_prompt.md](/C:/Users/louay/IdeaProjects/art_kids_javafx/docs/javafx_reference/11_javafx_generation_prompt.md)
