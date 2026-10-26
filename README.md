# Graphe de dispersion (JavaFX)

Application de visualisation et de classification de données sous forme de nuage de points, développée en Java avec JavaFX en suivant l'architecture MVC.

## Dépôt GitHub

* **Lien du dépôt** : <https://github.com/auerlucas/MVC-Nuage-De-Points>

## Fonctionnalités principales

* **Import de fichiers CSV** : Chargement dynamique de jeux de données avec détection automatique des attributs.

* **Visualisation interactive** : Affichage d'un nuage de points avec choix personnalisable des axes X et Y.

* **Gestion des catégories** : Attribution automatique de couleurs et de formes distinctes pour chaque catégorie, avec mise à jour dynamique de la légende.

* **Classification et Ajout de données** :

  * Interface dédiée pour classifier les points non assignés.

  * Ajout manuel de nouvelles données (avec ou sans classification immédiate).

* **Multi-fenêtrage** : Possibilité d'ouvrir plusieurs vues synchronisées grâce au pattern Observer.

## Architecture technique

Le projet repose sur une architecture MVC (Modèle-Vue-Contrôleur) rigoureuse :

* **Modèle** : Gère les données (`Data`, `DataSet`) et la logique métier de manière indépendante de l'interface graphique.

* **Contrôleur** (`DataSetController`) : Fait la liaison entre les actions de l'utilisateur (via la vue) et les modifications du modèle.

* **Vue** : Composants graphiques JavaFX (`MainApp`, `ViewManager`, `ScatterPlotView`) assurant l'affichage dynamique.

**Design patterns utilisés** : Observer (pour la mise à jour automatique des graphiques) et Strategy (pour modéliser les algorithmes de classification).

## Lancement du projet

Assurez-vous d'avoir un JDK compatible (Java 17 ou supérieur) ainsi que le SDK JavaFX configuré dans votre environnement de développement (IntelliJ IDEA, Eclipse, etc.).

Le point d'entrée de l'application se trouve dans la classe :

`fr.univlille.iut.s302.view.MainApp`