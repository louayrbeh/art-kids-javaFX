# 02 - Reference des entites

## Remarque generale

Les informations ci-dessous sont basees sur les entites observees dans `src/Entity/`.

Les types Java recommandes sont proposes pour une future application JavaFX.

## User

### Role

Represente un utilisateur authentifiable de la plateforme. Un utilisateur peut etre parent ou administrateur.

### Champs

| Champ | Type Symfony | Type Java recommande | Obligatoire | Description |
|---|---|---|---|---|
| id | `?int` | `int` | Oui en persistance | Identifiant |
| nom | `?string` | `String` | Oui | Nom |
| prenom | `?string` | `String` | Oui | Prenom |
| email | `?string` | `String` | Oui | Email unique |
| password | `?string` | `String` | Oui | Mot de passe hashé |
| telephone | `?string` | `String` | Non | Telephone |
| roles | `array<string>` JSON | `List<String>` ou `Set<String>` | Oui | Roles de securite |
| plainPassword | `?string` non Doctrine | `String` temporaire | Selon contexte | Mot de passe brut pour formulaires |
| isActive | `bool` | `boolean` | Oui | Etat du compte |
| createdAt | `DateTimeImmutable` | `LocalDateTime` | Oui | Date de creation |
| updatedAt | `?DateTimeImmutable` | `LocalDateTime` | Non | Date de mise a jour |

### Relations

- `OneToMany` vers `Child` via `children`

### Methodes importantes

- `getFullName()`
- `isAdmin()`
- `isParent()`
- `getUserIdentifier()`
- `addChild()`
- `removeChild()`

### Contraintes importantes

- email unique
- nom obligatoire
- prenom obligatoire
- email obligatoire et valide
- mot de passe obligatoire sur certains groupes de validation
- seul un utilisateur parent peut porter des enfants via `addChild()`

### Equivalent JavaFX a creer

- `User.java`
- `UserRole.java`
- champs `roles` gardes en liste ou ensemble
- methodes utilitaires `isAdmin()`, `isParent()`, `getFullName()`

## Child

### Role

Represente un enfant rattache a un parent.

### Champs

| Champ | Type Symfony | Type Java recommande | Obligatoire | Description |
|---|---|---|---|---|
| id | `?int` | `int` | Oui en persistance | Identifiant |
| nom | `?string` | `String` | Oui | Nom |
| prenom | `?string` | `String` | Oui | Prenom |
| dateNaissance | `?DateTimeImmutable` | `LocalDate` | Oui | Date de naissance |
| sexe | `?SexeEnum` | `Sexe` | Oui | Sexe |
| createdAt | `DateTimeImmutable` | `LocalDateTime` | Oui | Date de creation |
| updatedAt | `?DateTimeImmutable` | `LocalDateTime` | Non | Date de mise a jour |
| parent | `?User` | `User` ou `int parentId` | Oui | Parent proprietaire |

### Relations

- `ManyToOne` vers `User` via `parent`
- `OneToMany` vers `Reservation` via `reservations`

### Methodes importantes

- `getAge()`
- `getFullName()`
- `setParent()`
- `addReservation()`
- `removeReservation()`

### Contraintes importantes

- nom obligatoire
- prenom obligatoire
- date de naissance obligatoire
- date de naissance <= aujourd'hui
- sexe obligatoire
- parent obligatoire
- `setParent()` refuse un utilisateur sans role parent

### Equivalent JavaFX a creer

- `Child.java`
- `Sexe.java`
- conserver `getAge()` et `getFullName()`
- associer l'enfant a un parent mocké ou a `parentId`

## Category

### Role

Represente un univers artistique : peinture, dessin, musique, etc.

### Champs

| Champ | Type Symfony | Type Java recommande | Obligatoire | Description |
|---|---|---|---|---|
| id | `?int` | `int` | Oui en persistance | Identifiant |
| nom | `?string` | `String` | Oui | Nom unique |
| description | `?string` | `String` | Non | Description |
| image | `?string` | `String` | Non | Nom de fichier ou URL image |
| createdAt | `DateTimeImmutable` | `LocalDateTime` | Oui | Date de creation |
| updatedAt | `?DateTimeImmutable` | `LocalDateTime` | Non | Date de mise a jour |

### Relations

- `OneToMany` vers `Activity` via `activities`

### Methodes importantes

- `__toString()`
- `addActivity()`
- `removeActivity()`

### Contraintes importantes

- nom obligatoire
- nom unique
- image optionnelle

### Equivalent JavaFX a creer

- `Category.java`
- CRUD simple
- champ image conserve comme `String`

## Activity

### Role

Represente une activite artistique reservable.

### Champs

| Champ | Type Symfony | Type Java recommande | Obligatoire | Description |
|---|---|---|---|---|
| id | `?int` | `int` | Oui en persistance | Identifiant |
| titre | `?string` | `String` | Oui | Titre |
| description | `?string` | `String` | Oui | Description |
| image | `?string` | `String` | Non | Nom de fichier ou URL |
| dateActivite | `?DateTimeImmutable` | `LocalDate` | Oui | Date de l'activite |
| heureDebut | `?DateTimeImmutable` | `LocalTime` | Oui | Heure de debut |
| heureFin | `?DateTimeImmutable` | `LocalTime` | Oui | Heure de fin |
| capaciteMax | `?int` | `int` | Oui | Capacite maximale |
| ageMin | `?int` | `int` | Oui | Age minimum |
| ageMax | `?int` | `int` | Oui | Age maximum |
| prix | `?string` decimal | `BigDecimal` ou `double` | Non | Prix |
| statut | `?ActivityStatusEnum` | `ActivityStatus` | Oui | Statut |
| lieu | `?string` | `String` | Non | Lieu |
| createdAt | `DateTimeImmutable` | `LocalDateTime` | Oui | Date creation |
| updatedAt | `?DateTimeImmutable` | `LocalDateTime` | Non | Date mise a jour |
| category | `?Category` | `Category` ou `int categoryId` | Oui | Categorie |

### Relations

- `ManyToOne` vers `Category`
- `OneToMany` vers `Reservation`

### Methodes importantes

- `placesDisponibles()`
- `estComplete()`
- `estDisponible()`
- `estFuture()`
- `updateStatutIfNeeded()`
- `addReservation()`
- `removeReservation()`

### Contraintes importantes

- titre obligatoire
- description obligatoire
- date future obligatoire
- heure debut obligatoire
- heure fin obligatoire
- capacite max > 0
- age min >= 0
- age max >= age min
- categorie obligatoire
- statut enum

### Equivalent JavaFX a creer

- `Activity.java`
- `ActivityStatus.java`
- reproduire `placesDisponibles()`, `estComplete()`, `estDisponible()`, `estFuture()`, `updateStatutIfNeeded()`

## Reservation

### Role

Represente l'inscription d'un enfant a une activite.

### Champs

| Champ | Type Symfony | Type Java recommande | Obligatoire | Description |
|---|---|---|---|---|
| id | `?int` | `int` | Oui en persistance | Identifiant |
| dateReservation | `DateTimeImmutable` | `LocalDateTime` | Oui | Date de reservation |
| statut | `?ReservationStatusEnum` | `ReservationStatus` | Oui | Statut |
| createdAt | `DateTimeImmutable` | `LocalDateTime` | Oui | Date creation |
| updatedAt | `?DateTimeImmutable` | `LocalDateTime` | Non | Date mise a jour |
| child | `?Child` | `Child` ou `int childId` | Oui | Enfant concerne |
| activity | `?Activity` | `Activity` ou `int activityId` | Oui | Activite concernee |

### Relations

- `ManyToOne` vers `Child`
- `ManyToOne` vers `Activity`

### Methodes importantes

- `confirmer()`
- `annuler()`
- `estEnAttente()`
- `estConfirmee()`
- `estAnnulee()`

### Contraintes importantes

- dateReservation obligatoire
- statut obligatoire
- enfant obligatoire
- activite obligatoire
- contrainte d'unicite Doctrine/SQL sur `(child_id, activity_id)`

### Equivalent JavaFX a creer

- `Reservation.java`
- `ReservationStatus.java`
- reproduire les methodes d'etat
- verifier la non duplication d'une reservation enfant + activite

## Enums a reproduire

### UserRole

- `ROLE_ADMIN`
- `ROLE_PARENT`

### SexeEnum

- `GARCON`
- `FILLE`

### ActivityStatusEnum

- `OUVERTE`
- `COMPLETE`
- `ANNULEE`
- `TERMINEE`

### ReservationStatusEnum

- `EN_ATTENTE`
- `CONFIRMEE`
- `ANNULEE`
- `TERMINEE`
