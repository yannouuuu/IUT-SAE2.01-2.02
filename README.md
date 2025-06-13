<br/>
<p align="center">
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="https://github.com/yannouuuu/IUT-SAE1.01/raw/main/.github/assets/header_univlille_light.png" width="200px">
        <img alt="UnivLilleLogo" src="https://github.com/yannouuuu/IUT-SAE1.01/raw/main/.github/assets/header_univlille_dark.png" width="200px">
    </picture>
  <h1 align="center">IUT-SAE2.01-2.02 | Développement d'un outil d'aide à la décision</h1>
</p>

<p align="center">
    <a href="https://github.com/yannouuuu/IUT-SAE2.01-2.02/" title="Voir le projet sur GitHub"><img src="https://img.shields.io/badge/Voir%20le%20projet%20sur-GitHub-blue?logo=github" alt="GitHub"/></a>
    <a href="https://gitlab.com/yannouuuu/IUT-SAE2.01-2.02" title="Voir le projet sur GitLab"><img src="https://img.shields.io/badge/Voir%20le%20projet%20sur-GitLab-orange?logo=gitlab" alt="GitLab"/></a>
</p>

<p align="center">
    Module d'initiation au Developpement d'une application en BUT1 d'Informatique
    <br/>
    Développement d'une application 
    - Exploration algorithmique
    - Interface Homme-Machine
    - Graphes
    <br/>
    <br/>
    <a href="https://moodle.univ-lille.fr/course/view.php?id=30827&sectionid=266879"><strong>Voir la page sur le moodle »</strong></a>
    <br/>
    <br/>
</p>

## Documentation Technique (Javadoc)

La documentation technique complète du code source (Javadoc) est générée automatiquement à chaque nouvelle version. Elle détaille l'ensemble des classes, méthodes et leurs interactions.

> **Consulter la Javadoc en ligne :**
> - [Sur GitLab](https://gitlabpages.univ-lille.fr/d7-a7a56e/) (nécessite une connexion au portail de l'Université de Lille)
> - [Sur GitHub](https://yannouuuu.github.io/IUT-SAE2.01-2.02/)

## Structure des fichiers

```
/
├── src/                 # Code source Java (POO)
│   ├── main/            # Classes principales
│   │   └── java/        # Code source Java
│   │       └── sae/     # Package principal
│   └── test/            # Tests unitaires
├── docs/                # Documentation
│   ├── graphes/         # Rapports algorithmes de graphes
│   └── poo/             # Rapport POO et diagrammes UML
├── lib/                 # Bibliothèques externes
├── bin/                 # Fichiers binaires
├── mockup/              # Relatif à la maquette Figma
├── shots/               # Captures d'écran de l'application
├── pom.xml              # Configuration Maven
├── rapportIHM.pdf       # Rapport sur l'Interface Homme-Machine
├── rapportPOO.pdf       # Rapport sur la Programmation Orientée Objet
└── README.md            # Documentation principale
```


Ce projet est structuré en trois grandes parties : **Graphes**, **POO**, et **IHM**. Vous trouverez ci-dessous les détails pour chaque partie.

---

## Lancement du projet

Ce projet utilise [Apache Maven](https://maven.apache.org/) pour la gestion des dépendances et la construction du projet. Assurez-vous d'avoir Maven et un JDK 24 (ou supérieur) installés sur votre système.

### Lancement en ligne de commande (CLI)

Pour lancer l'application en mode ligne de commande, exécutez la commande Maven suivante à la racine du projet :

```bash
mvn clean compile exec:java
```

Cette commande va d'abord nettoyer et compiler le projet puis démarrer l'application en mode console.


### Lancement de l'interface graphique (JavaFX)

L'interface graphique est développée avec JavaFX. Pour lancer l'application, exécutez la commande Maven suivante à la racine du projet :

```bash
mvn javafx:run
```

Cette commande va compiler le projet, télécharger les dépendances nécessaires et démarrer l'interface utilisateur.

### Exécution des tests

Pour exécuter tous les tests unitaires du projet, utilisez la commande :

```bash
mvn test
```

Pour exécuter un test spécifique, vous pouvez utiliser :

```bash
mvn test -Dtest=NomDeLaClasseDeTest
```

### Exécutables JAR

Le projet est configurable pour produire deux fichiers JAR exécutables distincts, un pour l'interface graphique et un pour la ligne de commande.

#### Compilation des JARs
Pour compiler les JARs, vous devez utiliser les profils Maven appropriés :

- **Pour compiler l'application Graphique (GUI) :**
  ```bash
  mvn clean package
  ```
  Cette commande génère le fichier `D7-app-gui.jar`.

- **Pour compiler l'application Ligne de Commande (CLI) :**
  ```bash
  mvn clean package -P app-cli
  ```
  Cette commande génère le fichier `D7-app-cli.jar`.

#### Lancement des JARs
Une fois les fichiers générés à la racine du projet, vous pouvez les lancer avec `java -jar` :
-   `java -jar D7-app-gui.jar` : Pour démarrer l'interface graphique.
-   `java -jar D7-app-cli.jar` : Pour utiliser l'application en ligne de commande.

---

## Graphes

### Localisation des versions du rapport
Le rapport sur les graphes est disponible en trois versions, situées dans l'arborescence suivante :

- **Version 1** : `docs/graphes/rapport_v1.md`
- **Version 2** : `docs/graphes/rapport_v2.md`
- **Version 3** : `docs/graphes/rapport_v3.md`
- **Rapport Final** : `docs/graphes/rapport_full.md`

### Conversion des fichiers Markdown
Pour convertir les fichiers `.md` en un format lisible (par exemple, PDF), vous devez utiliser le fichier de styles YAML fourni. Ce fichier se trouve à l'emplacement suivant :

#### Commande de conversion
Voici la commande pour convertir un fichier `.md` en PDF à l'aide de Pandoc et du fichier de styles :

```bash
cd docs/graphes
pandoc rapport_full.md -o ./pdf/rapport_full.pdf --metadata-file=./styles/style-rapport-graphes.yaml
```

Remplacez rapport_vX.md par la version souhaitée (rapport_v1.md, rapport_v2.md, ou rapport_v3.md).

## POO

### Localisation du rapport
Le rapport sur la Programmation Orientée Objet est disponible au format AsciiDoc :

- **Rapport complet** : `docs/poo/rapport.adoc`

Ce rapport couvre les évolutions principales entre les différentes versions de ce projet.

### Diagrammes UML
Les diagrammes UML sont générés avec PlantUML et organisés par semaines :

- **Semaines 1-2** : `docs/poo/UML_plantuml/SAE_UML_Version1.puml` → `SAE_UML_Version1.png`
- **Semaines 3-4** : `docs/poo/UML_plantuml/SAE_UML_Version2.puml` → `SAE_UML_Version2.png`
- **Semaines 5-6** : `docs/poo/UML_plantuml/SAE_UML_Version3.puml` → `SAE_UML_Version3.png`
- **Semaines 7-8** : `docs/poo/UML_plantuml/SAE_UML_Version4.puml` → `SAE_UML_Version4.png`

### Conversion du rapport AsciiDoc
Pour convertir le fichier `.adoc` en format PDF, utilisez AsciiDoctor :

```bash
cd docs/poo
asciidoctor-pdf rapport.adoc -o rapport.pdf
```

Ou pour générer en HTML :

```bash
cd docs/poo
asciidoctor rapport.adoc -o rapport.html
```

### Génération des diagrammes UML
Pour régénérer les diagrammes PNG à partir des fichiers PlantUML :

```bash
cd docs/poo/UML_plantuml
plantuml *.puml
```

## IHM

### Prototype Figma
Le prototype de l'interface utilisateur est disponible sur Figma à cette adresse :

- **Prototype interactif** : [Voir le prototype Figma](https://www.figma.com/proto/Y7ewJ6pdu0NNGn80LHDW8e/D7-SAE2.01-2.02---IHM?page-id=7577%3A6818&node-id=7594-7221&p=f&viewport=313%2C433%2C0.09&t=ZMQ0aQlRNwuxiWea-1&scaling=scale-down&content-scaling=fixed&starting-point-node-id=7594%3A7221&show-proto-sidebar=1)

### Interface utilisateur
L'interface permet de :
- Visualiser les données des adolescents
- Configurer les critères d'appariement
- Lancer l'algorithme d'affectation
- Consulter les résultats de l'appariement

---

## Captures d'écran
Des exemples visuels du fonctionnement de l'application sont disponibles dans le répertoire [shots](./mockup).

## Développé avec 💖 par
- Yann RENARD
  > yann.renard.etu@univ-lille.fr
- Yanis MEKKI
  > yanis.mekki.etu@univ-lille.fr
- Rémy Martin
  > remy.martin3.etu@univ-lille.fr

---

### Remerciements

Nous tenons à créditer **BONEVA Iovka, NONGAILLARD Antoine, SANTANA MAIA Deise, CASIEZ Géry, DELECROIX Fabien, FRUCHARD Bruno et bien d'autres** pour la création des diaporamas de cours, des TP, TD et pour la réalisation des SAE. Leur travail a été précieux pour notre apprentissage.

<br/>
<p align="center">
    <picture>
        <img alt="UnivLilleLogo" src="https://github.com/yannouuuu/IUT-SAE1.01/raw/main/.github/assets/footer_univlille.png">
    </picture>
</p>