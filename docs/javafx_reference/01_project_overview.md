# 01 - Vue d'ensemble du projet ArtKids

## Nom du projet

`art_kids`

## Objectif de l'application

ArtKids est une application Symfony 6.4 de gestion d'activites artistiques pour enfants.

L'application permet :

- a un parent de creer un compte, ajouter ses enfants, consulter les activites disponibles, reserver des activites et suivre ses reservations ;
- a un administrateur de piloter les categories, les activites, les utilisateurs, les enfants et les reservations depuis un Back-Office ;
- d'exploiter une logique metier autour des places disponibles, des tranches d'age, des statuts d'activite et des statuts de reservation ;
- d'utiliser des fonctionnalites complementaires comme l'upload d'images, la generation IA de description d'activite, les recommandations intelligentes et les statistiques admin.

## Technologies observees

- PHP 8+
- Symfony 6.4
- Doctrine ORM
- Twig
- Form component Symfony
- Security component Symfony
- Bootstrap 5
- Bootstrap Icons
- Chart.js via CDN
- OpenAI via `HttpClientInterface` pour certaines fonctions IA
- MySQL comme base principale

## Separation Front-Office / Back-Office

Le projet separe clairement :

- le Front-Office parent dans `src/Controller/FrontOffice/` et `templates/front_office/`
- le Back-Office admin dans `src/Controller/BackOffice/` et `templates/back_office/`

Cette separation est importante a conserver dans la future application JavaFX.

## Roles applicatifs

- `ROLE_PARENT` : acces au Front-Office parent
- `ROLE_ADMIN` : acces au Back-Office admin

## Role du parent

Le parent peut :

- s'inscrire publiquement via `/register`
- se connecter via `/login`
- acceder a son dashboard `/parent`
- gerer uniquement ses propres enfants
- consulter les activites reservables
- reserver une activite pour un de ses enfants
- annuler ses propres reservations
- consulter des recommandations d'activites par enfant

## Role de l'administrateur

L'administrateur peut :

- acceder au dashboard admin `/admin`
- gerer les categories
- gerer les activites
- generer automatiquement une description d'activite avec IA
- uploader des images pour categories et activites
- gerer les utilisateurs
- consulter les enfants
- consulter et administrer les reservations
- consulter des statistiques avancees et des graphiques

## Modules principaux

### Front-Office

- Home publique
- Login
- Register parent
- Dashboard parent
- Gestion des enfants
- Liste des activites
- Detail activite
- Reservation activite
- Liste des reservations
- Detail reservation
- Annulation reservation

### Back-Office

- Dashboard admin
- CRUD categories
- CRUD activites
- IA de description d'activite
- Upload d'images
- CRUD utilisateurs
- Consultation enfants
- Gestion reservations
- Statistiques avancees

## Resume fonctionnel global

Le coeur fonctionnel du projet est la reservation d'activites artistiques pour enfants dans un cadre securise :

1. un parent cree son compte ;
2. il ajoute ses enfants ;
3. il consulte des activites compatibles avec leur age ;
4. il reserve une activite ;
5. l'admin pilote le catalogue, les utilisateurs et les reservations ;
6. l'application applique des regles metier strictes sur l'age, la capacite, les statuts et la propriete des donnees.

## Ce que doit retenir la future application JavaFX

La future application JavaFX devra reproduire :

- la separation parent / admin ;
- la logique metier de reservation ;
- les statuts et les contraintes ;
- la securisation des donnees par role ;
- l'architecture en services metier ;
- l'idee qu'a terme Symfony restera le backend principal et JavaFX deviendra un client desktop.
