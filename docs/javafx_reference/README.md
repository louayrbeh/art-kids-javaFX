# Documentation de reference JavaFX pour ArtKids

## Pourquoi ce dossier existe

Ce dossier existe pour preparer la creation d'une future application JavaFX separee, basee sur le projet Symfony `art_kids`.

Le but n'est pas de modifier Symfony ici, mais de fournir une documentation de reference exploitable dans un autre repository.

## Ce que contient ce dossier

- `01_project_overview.md` : vue d'ensemble fonctionnelle
- `02_entities_reference.md` : entites, champs, relations, contraintes
- `03_roles_and_security.md` : roles et securite
- `04_front_office_reference.md` : reference des ecrans parent
- `05_back_office_reference.md` : reference des ecrans admin
- `06_business_rules.md` : regles metier a reproduire
- `07_routes_reference.md` : routes importantes et correspondances d'ecrans
- `08_ui_screens_reference.md` : reference des ecrans JavaFX a creer
- `09_future_api_integration_plan.md` : plan d'integration REST futur
- `10_javafx_architecture_target.md` : architecture cible recommande
- `11_javafx_generation_prompt.md` : prompt directement copiable pour Codex

## Ce que ce dossier ne fait pas

- il ne cree pas l'application JavaFX
- il ne modifie pas Symfony
- il ne change pas la base de donnees
- il ne cree pas d'API REST maintenant

## Ordre recommande de lecture

1. `01_project_overview.md`
2. `02_entities_reference.md`
3. `03_roles_and_security.md`
4. `06_business_rules.md`
5. `04_front_office_reference.md`
6. `05_back_office_reference.md`
7. `07_routes_reference.md`
8. `08_ui_screens_reference.md`
9. `10_javafx_architecture_target.md`
10. `09_future_api_integration_plan.md`
11. `11_javafx_generation_prompt.md`

## Utilisation recommandee

1. Copier le dossier `docs/javafx_reference/` dans le repository JavaFX.
2. Ouvrir Codex dans le repository JavaFX.
3. Donner a Codex le contenu de `11_javafx_generation_prompt.md`.
4. Verifier que Codex cree uniquement l'application JavaFX.

## Quels fichiers copier vers le repo JavaFX

Tous les fichiers du dossier :

```text
docs/javafx_reference/
```

Le plus important est de conserver ensemble :

- les references fonctionnelles
- les regles metier
- l'architecture cible
- le prompt final

## Comment utiliser `11_javafx_generation_prompt.md`

- ouvrir le fichier
- copier son contenu integral
- le coller dans Codex dans le repository JavaFX
- laisser Codex construire le projet en s'appuyant sur les autres documents

## Recommandation pratique

Si une future version JavaFX doit se connecter a Symfony :

- commencer avec des donnees mockees
- puis remplacer progressivement les services mockes par des services API
- garder Symfony comme backend principal

## Important

Cette documentation est basee sur le code actuel du projet Symfony `art_kids`.

Quand une information n'a pas ete trouvee de maniere certaine, elle a ete signalee explicitement au lieu d'etre inventee.
