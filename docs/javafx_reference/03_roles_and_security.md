# 03 - Roles et securite

## Roles existants

Le projet utilise deux roles metier explicites :

- `ROLE_ADMIN`
- `ROLE_PARENT`

Ils sont portes dans le champ `roles` de l'entite `User`.

## Regles globales d'acces

Dans `config/packages/security.yaml` :

- toutes les routes commençant par `/admin` exigent `ROLE_ADMIN`
- toutes les routes commençant par `/parent` exigent `ROLE_PARENT`

## Authentification

### Provider

- provider Doctrine base sur `App\Entity\User`
- propriete d'identification : `email`

### Login Symfony

- `form_login`
- `login_path: app_login`
- `check_path: app_login`
- `default_target_path: app_post_login_redirect`
- CSRF active

### Logout Symfony

- `path: app_logout`
- `target: app_front_home`

## Logique de login

### Route

- `/login`

### Comportement

- si l'utilisateur est deja connecte, redirection vers `app_post_login_redirect`
- le formulaire attend `_username`, `_password`, `_csrf_token`
- apres login, la route `/post-login` redirige selon le role :
  - admin -> dashboard admin
  - parent -> dashboard parent
  - sinon -> home publique

## Logique de register

### Route

- `/register`

### Comportement

- route publique
- si un utilisateur est deja connecte, redirection vers `app_post_login_redirect`
- l'inscription cree toujours un compte parent
- le role admin n'est jamais choisi par le visiteur public
- le compte cree est actif par defaut

## Restrictions parent

Le parent :

- ne peut acceder qu'aux routes `/parent/*`
- ne doit pas acceder au Back-Office admin
- ne doit voir que ses propres enfants
- ne doit modifier que ses propres enfants
- ne doit supprimer que ses propres enfants
- ne doit voir que ses propres reservations
- ne doit reserver une activite que pour ses propres enfants

### Verifications observees

- `ParentChildController::denyUnlessOwnChild()`
- `ParentReservationController::denyUnlessOwnReservation()`
- `ParentActivityController::reserve()` verifie que l'enfant choisi appartient bien au parent connecte
- `ReservationService::assertReservationIsAllowed()` peut verifier le parent attendu

## Restrictions admin

L'administrateur :

- accede au Back-Office `/admin`
- gere categories, activites, utilisateurs, reservations
- peut consulter les enfants

### Protections specifiques observees

Dans `UserController` :

- un admin ne peut pas retirer son propre role admin
- un admin ne peut pas desactiver son propre compte
- le dernier admin ne peut pas perdre son role admin
- le dernier admin actif ne peut pas etre desactive
- un admin ne peut pas supprimer son propre compte
- le dernier admin ne peut pas etre supprime

## CSRF

Les actions sensibles utilisent des tokens CSRF :

- suppression enfant
- suppression categorie
- suppression activite
- suppression utilisateur
- activation/desactivation utilisateur
- reservation activite
- annulation reservation parent
- confirmation reservation admin
- annulation reservation admin
- generation IA de description activite via header CSRF

## Ce qu'il faudra reproduire dans JavaFX

JavaFX n'utilisera pas `security.yaml`, mais devra reproduire la logique fonctionnelle :

- login avec deux profils : parent et admin
- navigation differente selon le role
- ecrans parent isoles des ecrans admin
- impossibilite pour un parent de consulter ou modifier des donnees appartenant a un autre parent
- protection metier sur les reservations et la gestion des enfants

## Recommandation JavaFX

Dans la future application desktop :

- stocker l'utilisateur connecte dans un contexte applicatif ou `SessionContext`
- exposer des methodes utilitaires :
  - `isAdmin()`
  - `isParent()`
  - `getCurrentUser()`
- securiser les services metier, pas seulement l'interface

## Information non trouvee dans le code actuel

- Aucun mecanisme de permissions plus fines que `ROLE_ADMIN` / `ROLE_PARENT` n'a ete trouve.
