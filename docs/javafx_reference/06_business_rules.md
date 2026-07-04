# 06 - Regles metier

## Regle 1 - Un parent ne peut gerer que ses propres enfants

### Explication

Un parent ne doit pas voir, modifier ou supprimer les enfants d'un autre parent.

### Emplacement Symfony

- `ParentChildController::denyUnlessOwnChild()`
- `ParentActivityController::reserve()` verifie `child->getParent() === parent`

### Reproduction JavaFX

- dans le service enfant, filtrer toujours par parent connecte
- en edition/suppression, verifier que `child.parentId == currentUser.id`

## Regle 2 - Un parent ne peut voir que ses propres reservations

### Explication

Le parent ne doit pas consulter les reservations d'autres familles.

### Emplacement Symfony

- `ParentReservationController::denyUnlessOwnReservation()`
- `ReservationRepository::findByParent()`

### Reproduction JavaFX

- filtrer les reservations via les enfants du parent connecte
- verifier la propriete avant detail/annulation

## Regle 3 - Un enfant ne peut pas reserver deux fois la meme activite

### Explication

Une double inscription pour le meme couple enfant + activite est interdite.

### Emplacement Symfony

- contrainte unique dans `Reservation` sur `(child_id, activity_id)`
- `ReservationRepository::existsForChildAndActivity()`
- `ReservationService::assertReservationIsAllowed()`

### Reproduction JavaFX

- avant creation, rechercher si une reservation existe deja pour `childId + activityId`
- interdire la creation si une reservation active existe deja

## Regle 4 - Une activite complete ne peut plus etre reservee

### Explication

Quand la capacite est atteinte, l'activite n'est plus reservable.

### Emplacement Symfony

- `Activity::placesDisponibles()`
- `Activity::estComplete()`
- `Activity::estDisponible()`
- `ReservationService::assertReservationIsAllowed()`

### Reproduction JavaFX

- calculer `remainingPlaces = capaciteMax - activeReservations`
- bloquer la reservation si `remainingPlaces <= 0`

## Regle 5 - Une activite annulee ne peut pas etre reservee

### Explication

Une activite annulee reste visible selon certains contextes, mais ne doit plus etre reservable.

### Emplacement Symfony

- `Activity::updateStatutIfNeeded()`
- `Activity::estDisponible()`
- `ReservationService::assertReservationIsAllowed()`

### Reproduction JavaFX

- si statut = `ANNULEE`, interdire la reservation

## Regle 6 - Une activite terminee ou passee ne peut pas etre reservee

### Explication

Les activites passees ne sont plus reservables.

### Emplacement Symfony

- `Activity::estFuture()`
- `Activity::updateStatutIfNeeded()`
- `ParentActivityController::show()`
- `ReservationService::assertReservationIsAllowed()`

### Reproduction JavaFX

- comparer la date de l'activite a la date du jour
- si la date n'est pas future, l'activite devient non reservable

## Regle 7 - L'age de l'enfant doit etre entre ageMin et ageMax

### Explication

L'activite n'est reservable que si l'age de l'enfant correspond a la tranche.

### Emplacement Symfony

- `ParentActivityController::getCompatibleChildren()`
- `ReservationService::assertReservationIsAllowed()`
- `RecommendationService::isEligibleForChild()`

### Reproduction JavaFX

- calculer l'age de l'enfant
- verifier `age >= ageMin && age <= ageMax`

## Regle 8 - La capacite maximale ne doit pas etre depassee

### Explication

Le nombre de reservations actives ne doit pas depasser `capaciteMax`.

### Emplacement Symfony

- `ReservationRepository::countActiveForActivity()`
- `ReservationService::assertReservationIsAllowed()`
- `ReservationController::confirm()`

### Reproduction JavaFX

- compter les reservations non annulees pour l'activite
- refuser reservation / confirmation si la capacite est atteinte

## Regle 9 - Une categorie avec activites ne doit pas etre supprimee

### Explication

Pour conserver l'integrite du catalogue, une categorie liee a des activites ne doit pas etre supprimee.

### Emplacement Symfony

- `CategoryRepository::hasActivities()`
- `CategoryController::delete()`

### Reproduction JavaFX

- avant suppression, verifier si une activite reference la categorie

## Regle 10 - Une activite avec reservations actives ne doit pas etre supprimee

### Explication

On ne supprime pas une activite encore liee a des reservations actives.

### Emplacement Symfony

- `ActivityRepository::hasActiveReservations()`
- `ActivityController::delete()`

### Reproduction JavaFX

- verifier les reservations non annulees de l'activite
- si existantes, refuser suppression

## Regle 11 - L'admin ne doit pas supprimer son propre compte

### Explication

Protection de base contre l'auto-suppression.

### Emplacement Symfony

- `UserController::delete()`
- `UserController::isCurrentUser()`

### Reproduction JavaFX

- si `selectedUser.id == currentUser.id`, interdire suppression

## Regle 12 - Le dernier administrateur ne doit pas etre supprime ou desactive

### Explication

L'application doit toujours conserver au moins un admin actif.

### Emplacement Symfony

- `UserController::wouldRemoveLastAdminRole()`
- `UserController::wouldDeactivateLastActiveAdmin()`
- `UserRepository::countAdmins()`
- `UserRepository::countActiveAdmins()`

### Reproduction JavaFX

- compter les admins et les admins actifs
- bloquer suppression / desactivation / changement de role si cela retire le dernier admin

## Regle 13 - Un admin ne doit pas retirer son propre role admin

### Explication

Protection explicite du compte courant.

### Emplacement Symfony

- `UserController::edit()`

### Reproduction JavaFX

- si edition du compte courant et role cible != admin, refuser

## Regle 14 - Un admin ne doit pas se desactiver lui-meme

### Explication

Le compte courant admin doit rester actif.

### Emplacement Symfony

- `UserController::edit()`
- `UserController::toggleStatus()`

### Reproduction JavaFX

- si compte courant et `isActive = false`, refuser

## Regle 15 - Si un utilisateur a des enfants, la suppression devient une desactivation

### Explication

Le projet conserve l'historique en desactivant le compte au lieu de le supprimer.

### Emplacement Symfony

- `UserController::delete()`

### Reproduction JavaFX

- si `user.children.size() > 0`, appliquer `isActive = false` au lieu d'une suppression physique

## Regle 16 - Les champs obligatoires doivent etre valides

### Explication

Les formulaires Symfony imposent les champs obligatoires.

### Emplacement Symfony

- validations d'entites
- `ChildType`
- `RegistrationFormType`
- `CategoryType`
- `ActivityType`
- `UserType`

### Reproduction JavaFX

- valider cote UI et cote service
- afficher messages clairs

## Regle 17 - La date d'activite doit etre future

### Explication

Une activite ne peut pas etre creee avec une date aujourd'hui ou passee.

### Emplacement Symfony

- `Activity` : `Assert\GreaterThan('today')`
- `ActivityService` mock JavaFX observe precedemment, mais ici la reference Symfony est l'entite

### Reproduction JavaFX

- refuser toute date <= aujourd'hui

## Regle 18 - L'heure de fin doit etre superieure a l'heure de debut

### Explication

La coherence horaire est obligatoire.

### Emplacement Symfony

- `Activity::validate()` callback

### Reproduction JavaFX

- comparer heure fin et heure debut avant sauvegarde

## Regle 19 - L'age max doit etre superieur ou egal a l'age min

### Explication

La tranche d'age doit etre valide.

### Emplacement Symfony

- `Activity::validate()` callback
- `ActivityAiController` verifie aussi cet ordre pour l'IA

### Reproduction JavaFX

- refuser sauvegarde si `ageMax < ageMin`

## Regle 20 - Une reservation annulee ne peut pas etre confirmee

### Explication

Le Back-Office ne doit pas confirmer une reservation deja annulee.

### Emplacement Symfony

- `ReservationController::confirm()`

### Reproduction JavaFX

- dans l'espace admin, refuser action confirmer si statut = annulee

## Regle 21 - Une reservation terminee ne peut plus etre annulee

### Explication

Une reservation finalisee n'est plus annulable.

### Emplacement Symfony

- `ReservationService::cancelReservation()`

### Reproduction JavaFX

- interdire annulation si statut = `TERMINEE`

## Regle 22 - Une reservation pour activite passee ne peut plus etre annulee

### Explication

L'annulation cote parent est impossible si l'activite est deja passee.

### Emplacement Symfony

- `ReservationService::cancelReservation()`

### Reproduction JavaFX

- verifier la date de l'activite avant annulation

## Regle 23 - Upload image : seuls les noms de fichiers sont stockes

### Explication

Le projet stocke seulement le nom du fichier, pas le chemin complet.

### Emplacement Symfony

- `ImageUploaderService`
- `CategoryController`
- `ActivityController`

### Reproduction JavaFX

- version mock : stocker seulement `String image`
- version future API : laisser Symfony gerer l'upload reel

## Regle 24 - IA activite et recommandations : fallback local obligatoire

### Explication

Les fonctions IA doivent continuer a fonctionner sans cle OpenAI.

### Emplacement Symfony

- `ActivityAiService`
- `AiRecommendationService`

### Reproduction JavaFX

- pour la premiere version, utiliser uniquement du mock ou du local
- prevoir un service separe pour la future API
