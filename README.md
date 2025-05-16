# IUT-SAE2.01-2.02

Ce projet est structuré en trois grandes parties : **Graphes**, **POO**, et **IHM**. Vous trouverez ci-dessous les détails pour chaque partie.

---

## Graphes

### Localisation des versions du rapport
Le rapport sur les graphes est disponible en trois versions, situées dans l'arborescence suivante :

- **Version 1** : `docs/graphes/rapport_v1.md`
- **Version 2** : `docs/graphes/rapport_v2.md`
- **Version 3** : `docs/graphes/rapport_v3.md`

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

## IHM