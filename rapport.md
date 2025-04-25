---
title: SAE S2.02 -- Rapport graphes
subtitle: Équipe D7
author: Yann Renard, Yanis Mekki, Rémy Martin 
date: 2025
---

_Effacez les indications en italique avant de rendre le rapport._

# Version 1

## Choix pour la modélisation
          
Voici un  tableau présentant les valeurs pour les colonnes HOBBIES, GENDER, PAIR_GENDER et BIRTH_DATE pour des hôtes (H1, H2, ...) et des visiteurs (V1, V2, ...).

| name | role    | hobbies           | gender | pair_gender | birth_date  |
|------|---------|-------------------|--------|-------------|-------------|
| h1   | host    | painting, yoga    | m      | f           | 2000-05-12  |
| h2   | host    | hiking, photography | f    | f           | 1997-11-23  |
| h3   | host    | chess, gardening  | m      | m           | 2003-07-08  |
| h4   | host    | dancing, swimming | f      | f           | 2000-03-15  |
| h5   | host    | coding, music     | m      | f           | 1998-08-15  |

| name | role    | hobbies           | gender | pair_gender | birth_date  |
|------|---------|-------------------|--------|-------------|-------------|
| v1   | guest   | sculpting, tennis | f      | m           | 2002-06-30  |
| v2   | guest   | cooking, poetry   | m      | m           | 2000-01-17  |
| v3   | guest   | gaming, astronomy | m      | m           | 2004-12-01  |
| v4   | guest   | writing, theater  | f      | f           | 2001-02-20  |
| v5   | guest   | skating, singing  | f      | f           | 1997-02-20  |

### Forte affinité
| h1   | host    | painting, yoga    | m      | f           | 2000-05-12  |
| v1   | guest   | sculpting, tennis | f      | m           | 2002-06-30  |

Forte affinité puisque même hobbies, même paire de genre voulu et écart d'age de moins d'1 an et demi d'écart.

### Faible affinité
| h2   | host    | hiking, photography | f    | f           | 1997-11-23  |
| v2   | guest   | cooking, poetry   | m      | m           | 2000-01-17  |

Faible affinité puisque hobbies différent, paire de genre pas voulu des deux cotés et plus d'1 an et demi d'écart.

### Arbitrage entre les critères d'affinité
| h3   | host    | chess, gardening  | m      | f           | 2003-07-08  |
| v3   | guest   | gaming, astronomy | m      | m           | 2004-12-01  |

Hobbies différent, paire de genre voulu pour un des deux cotés et moins d'1 an et demi d'écart.

## Exemple complet
_Donnez un exemple de quatre hôtes A, B, C, D et quatre visiteurs W, X, Y, Z. Puis, donnez l'appariement qui vous considérez le meilleur entre ces hôtes et visiteurs._

## Score d'affinité

_**Lisez les conseils pour l'écriture de pseudo-code se trouvant sur Moodle**._

_Donner le pseudo-code de la fonction `score_affinité_1(hôte, visiteur)` qui retourne un nombre représentant le degré d'affinité entre un hôte et un visiteur._

```
double score_affinité_1(hôte, visiteur) 
  // compléter le code ici
  // ...
  // ...
```

## Retour sur l'exemple

_Donnez la matrice d'adjacence du graphe biparti complet entre les hôtes A, B, C, D et les visiteurs W, X, Y, Z que vous avez introduit plus haut. Les poids des arêtes sont donnés par la fonction `score_affinité_1`._

_Calculez l'appariement de poids minimal pour ce graphe. Obtenez-vous l'appariement que vous aviez identifié comme le meilleur ?_



# Version 2

_Ci-dessous, vous définirez des hôtes ayant des noms A1, A2, B1, B2, etc., et des visiteurs ayant des noms W1, W2, X1, X2, etc. Pour chacun et chacune d'entre iels, vous devrez donner :_

- _la valeur pour la coronne NAME parmi A1, A2, B1, ..., W1, W2, X1, ... ;_
- _des valeurs pour les colonnes HOBBIES, GENDER, PAIR_GENDER, BIRTH_DATE pour tout le monde ;_
- _des valeurs pour les colonnes HOST_HAS_ANIMAL, HOST_FOOD pour les hôtes ;_
- _des valeurs pour les colonnes GUEST_ANIMAL_ALLERGY, GUEST_FOOD_CONSTRAINT pour les visiteurs._

## Exemple avec appariement total

_Donnez un exemple de quatre hôtes A1, B1, C1, D1 et quatre visiteurs W1, X1, Y1, Z1 pour lesquels il existe des incompatibilités entre certains hôtes et certains visiteurs, mais il est possible de trouver un appariement qui respecte les contraintes rédhibitoires._

_Donnez également l'appariement que vous considérez le meilleur pour cet exemple. Expliquez pourquoi._

## Exemple sans appariement total

_Donnez un exemple de quatre hôtes A2, B2, C2, D2 et quatre visiteurs W2, X2, Y2, Z2 pour lesquels il n'est pas possible de former quatre paires hôte-visiteur à cause d'incompatibilités._

_Pour cet exemple, quel est le plus grand nombre de paires qu'on peut former ?_

_Donnez l'appariement que vous considérez le meilleur. Expliquez pourquoi._

## Score d'affinité

_Donner le pseudo-code de la fonction `score_affinité_2(hôte, visiteur)` qui retourne un nombre représentant le degré d'affinité entre un hôte et un visiteur. Vous pouvez réutiliser la fonction `score_affinité_1` (l'appeler ou copier du code)._

```
double score_affinité_2(hôte, visiteur) 
  // compléter le code ici
  // ...
  // ...
```

## Retour sur l'exemple

_Donnez les matrices d'adjacence pour les deux exemples de la Version 2 (A1,B1,C1,D1/W1,X1,Y1,Z1 et A2,B2,C2,D2/W2,X2,Y2,Z2). Les poids des arêtes sont déterminés par la fonction `score_affinité_2`. Pensez à nommer les lignes et les colonnes._

_Calculez l'appariement de poids minimal pour chacun des graphes. Obtenez-vous l'appariement que vous aviez identifié comme le meilleur ?_

## Robustesse de la modélisation (question difficile)

_Est-ce que votre fonction `score_affinité_2` garantit que les contraintes rédhibitoires seront toujours respectées, quel que soit le jeu de données ? Justifiez votre réponse._

_**Indications** : Cherchez un exemple de **grande taille** pour lequel la fonction `score_affinité_2` pourrait ne pas garantir le respect des contraintes. Dans cet exemple, vous auriez beaucoup d'adolescents compatibles sans affinité, et quelques adolescents incompatibles avec beaucoup d'affinité._

_Il est possible que votre fonction garantisse le respect des contraintes quel que soit l'exemple. Si vous pensez que c'est le cas, donnez des arguments pour convaincre._ 



# Version 3

_Ci-dessous, H1, H2, etc. désignent des noms d'hôtes et V1, V2, etc désignent des noms de visiteurs. Pour chacun et chacune d'entre iels, vous devrez donner des valeurs pour toutes les colonnes pertinentes en fonction de leur rôle, hôte ou visiteur._

## Équilibrage entre affinité / incompatibilité

_Donnez au moins quatre paires hôte-visiteur (H1, V1), (H2, V2), (H3, V3), (H4, V4), ... que vous considérez quasi équivalents pour l'affectation. Certaines de ces paires doivent ne pas respecter les contraintes considérées rédhibitoires dans la Version 2, d'autres doivent les respecter. Ces exemples doivent illustrer l'équilibrage que vous faites entre l'incompatibilité d'une part et l'affinité d'autre part : combien et quel type d'affinité permet de compenser combien et quel type d'incompatibilité. Les exemples seront accompagnés de commentaires expliquant vos choix._

## Score d'affinité

_Donner le pseudo-code de la fonction `score_affinité_3(hôte, visiteur)` qui retourne un nombre représentant le degré d'affinité entre un hôte et un visiteur. Vous pouvez réutiliser les fonctions `score_affinité_1` et `score_affinité_2`._

```
double score_affinité_3(hôte, visiteur) 
  // compléter le code ici
  // ...
  // ...
```

## Retour sur l'exemple

_Donnez le résultat de la fonction `score_affinité_3` pour les exemples d'équilibrage (H1, V1), (H2, V2), etc. ci-dessus. Est-ce que vous obtenez des scores proches ?_ 

_**Remarque**: Deux scores ne sont pas proches ou éloignés dans l'absolu ; cela dépend de la valeur minimale et la valeur maximale que peut prendre le score. Par exemple, les nombres 10 et 20 sont "proches" à l'échelle de l'intervalle de 0 à 1000, mais ne sont pas "proches" à l'échelle de l'intervalle 0 à 30._


