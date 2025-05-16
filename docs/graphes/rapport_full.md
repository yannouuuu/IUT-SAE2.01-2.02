---
title: SAE S2.02 -- Rapport graphes
subtitle: Équipe D7
author: Yann Renard, Yanis Mekki, Rémy Martin 
toc: 3
toc-title: "Table des matières"
date: 2025
---

\pagebreak

# Première version

## Choix pour la modélisation
          
### Forte affinité
| name | role    | hobbies           | gender | pair_gender | birth_date  |
|------|---------|-------------------|--------|-------------|-------------|
| h1   | host    | painting, yoga    | m      | f           | 2001-05-12  |
| v1   | guest   | painting, yoga    | f      | m           | 2002-06-30  |

Forte affinité puisque même hobbies, même paire de genre voulu et écart d'age de moins d'1 an et demi d'écart.

### Faible affinité
| name | role    | hobbies             | gender | pair_gender | birth_date  |
|------|---------|---------------------|--------|-------------|-------------|
| h2   | host    | hiking, photography | f      | f           | 1997-11-23  |
| v2   | guest   | cooking, poetry     | m      | m           | 2000-01-17  |
 
Faible affinité puisque hobbies différent, paire de genre non voulue des deux cotés et plus d'1 an et demi d'écart.

### Arbitrage entre les critères d'affinité
| name | role    | hobbies           | gender | pair_gender | birth_date  |
|------|---------|-------------------|--------|-------------|-------------|
| h3   | host    | chess, gardening  | m      | m           | 2003-07-08  |
| v3   | guest   | gaming, astronomy | m      | m           | 2004-12-01  |

Hobbies différent, paire de genre voulue pour les deux cotés et moins d'1 an et demi d'écart.

| name | role    | hobbies           | gender | pair_gender | birth_date  |
|------|---------|-------------------|--------|-------------|-------------|
| h4   | host    | dancing, swimming | f      | m           | 2000-03-15  |
| v4   | guest   | dancing, swimming | f      | m           | 2001-02-20  |

Même hobbies, paire de genre non voulue pour les deux cotés et moins d'1 an et demi d'écart.

| name | role    | hobbies           | gender | pair_gender | birth_date  |
|------|---------|-------------------|--------|-------------|-------------|
| h5   | host    | coding, music     | m      | f           | 1998-08-15  |
| v5   | guest   | skating, music    | f      | f           | 1998-02-20  |

Un hobbie en commun, paire de genre voulue pour un des deux cotés et moins d'1 an et demi d'écart.

## Exemple complet

### Exemple d'hôtes et visiteurs

| name | role    | hobbies             | gender | pair_gender | birth_date  |
|------|---------|---------------------|--------|-------------|-------------|
| A    | host    | painting, yoga      | m      | f           | 2001-05-12  |
| B    | host    | hiking, photography | f      | f           | 1997-11-23  |
| C    | host    | chess, gardening    | m      | m           | 2003-07-08  |
| D    | host    | music  , swimming   | f      | m           | 2000-03-15  |

| name | role    | hobbies           | gender | pair_gender | birth_date  |
|------|---------|-------------------|--------|-------------|-------------|
| W    | guest   | painting, yoga    | f      | m           | 2002-06-30  |
| X    | guest   | cooking, poetry   | m      | m           | 2000-01-17  |
| Y    | guest   | gardening, yoga   | m      | f           | 1998-11-15  |
| Z    | guest   | painting, music   | f      | f           | 1998-02-20  |

Ideal pour nous :

A avec W,<br>
B avec Z,<br>
C avec X,<br>
D avec Y

## Score d'affinité

```
fonction score_affinité_1(hôte, visiteur) : nombre // Retourne un nombre représentant l'affinité

  score = 5

  // Hobby
  // Retire 1 point pour chaque hobby commun
  nb_hobbies = nombre_hobbies_en_commun(hôte, visiteur)
  score = score - nb_hobbies // Ajoute 1 point par hobby

  // Age
  // Retire 1 point si la différence d'âge est inférieure à 18 mois
  si difference_age(hôte, visiteur) < 18 mois alors 
    score = score - 1
  fin si

  // Genre
  // Vérifie si les préférences de genre sont satisfaites
  // Une préférence vide (non spécifiée) est toujours considérée comme satisfaite
  pref_hote_ok = (hôte.pair_gender est non spécifié OU hôte.pair_gender == visiteur.gender)
  pref_visiteur_ok = (visiteur.pair_gender est non spécifié OU visiteur.pair_gender == hôte.gender)

  si pref_hote_ok ET pref_visiteur_ok alors
    // Retire 2 points si les préférences des deux sont satisfaites
    score = score - 2
  sinon si pref_hote_ok OU pref_visiteur_ok alors
    // Retire 1 point si la préférence d'un seul est satisfaite
    score = score - 1
  // Sinon (aucune préférence satisfaite), ne retire aucun point (0)
  fin si

  retourner score

fin fonction
```

## Retour sur l'exemple

### Matrice d'adjacence

|      | A | B | C | D |
|------|---|---|---|----|
| W    | 0 | 4 | 2 | 3  |
| X    | 3 | 3 | 3 | 3  |
| Y    | 3 | 3 | 3 | 2  |
| Z    | 2 | 1 | 3 | 2  |


Affectation de coût minimal 6,000000 :<br>
- (Z, B, 1,000000)<br>
- (X, C, 3,000000)<br>
- (W, A, 0,000000)<br>
- (Y, D, 2,000000)

Oui on trouve le même agencement que trouvé précédemment. 

\pagebreak

# Deuxième version

## Exemple avec appariement total

| name | role    | hobbies             | gender | pair_gender | birth_date  | HOST_HAS_ANIMAL | HOST_FOOD
|------|---------|---------------------|--------|-------------|-------------|-----------------|-------------
| A1   | host    | painting, yoga      | m      | f           | 2001-05-12  | YES             | NA
| A2   | host    | hiking, photography | f      | f           | 1997-11-23  | YES             | nonuts
| B1   | host    | chess, gardening    | m      | m           | 2003-07-08  | NO              | vegetarian
| B2   | host    | music, swimming     | f      | f           | 2000-03-15  | NO              | NA


| name | role    | hobbies           | gender | pair_gender | birth_date  | GUEST_ANIMAL_ALLERGY |  GUEST_FOOD_CONSTRAINT 
|------|---------|-------------------|--------|-------------|-------------|-----------------------|-------------------------
| W1   | guest   | painting, yoga    | f      | m           | 2002-06-30  | NO                   | NA
| W2   | guest   | cooking, poetry   | m      | m           | 2000-01-17  | YES                  | vegetarian
| X1   | guest   | gardening, yoga   | m      | m           | 1999-11-15  | YES                  | NA
| X2   | guest   | hiking, photography| f     | f           | 1998-02-20  | NO                   | nonuts

A1 avec W1 : contraintes alimentaires et animaux respectées, mêmes hobbies, genre demandé et moins d'1 an et demi.
A2 avec X2 : contraintes alimentaires et animaux respectées, mêmes hobbies, genre demandé et moins d'1 an et demi.
B1 avec W2 : contraintes alimentaires et animaux respectées, genre demandé.
B2 avec X1 : contraintes alimentaires et animaux respectées, moins d'1 an et demi.

## Exemple sans appariement total

| name | role    | hobbies             | gender | pair_gender | birth_date  | HOST_HAS_ANIMAL | HOST_FOOD
|------|---------|---------------------|--------|-------------|-------------|-----------------|-------------
| A1   | host    | painting, yoga      | m      | f           | 2001-05-12  | YES             | vegetarian
| A2   | host    | hiking, photography | f      | f           | 1997-11-23  | YES             | nonuts
| B1   | host    | chess, gardening    | m      | m           | 2003-07-08  | NO              | nonuts
| B2   | host    | music, swimming     | f      | f           | 2000-03-15  | YES             | NA

| name | role    | hobbies           | gender | pair_gender | birth_date  | GUEST_ANIMAL_ALLERGY |  GUEST_FOOD_CONSTRAINT 
|------|---------|-------------------|--------|-------------|-------------|-----------------------|-------------------------
| W1   | guest   | painting, yoga    | f      | m           | 2002-06-30  | NO                   | NA
| W2   | guest   | cooking, poetry   | m      | m           | 2000-01-17  | YES                  | vegetarian
| X1   | guest   | gardening, yoga   | m      | m           | 1999-11-15  | YES                  | nonuts
| X2   | guest   | hiking, photography| f     | f           | 1998-02-20  | NO                   | nonuts

A1 avec W1 : contraintes alimentaires et animaux respectées, mêmes hobbies, genre demandé et moins d'1 an et demi.
A2 avec X2 : contraintes alimentaires et animaux respectées, mêmes hobbies, genre demandé et moins d'1 an et demi.
B1 avec X1 : contraintes alimentaires et animaux respectées, 1 hobby en commun, genre demandé.
W2 et X1 étant tous deux allergiques aux animaux et ayant des allergies alimentaires. Ils restent donc W2 et B2 sans paire.
On peut former 3 paires.

## Score d'affinité

```
fonction score_affinité_2(hôte, visiteur) : nombre // Retourne un nombre représentant l'affinité
  si HOST_HAS_ANIMAL='YES' et GUEST_ANIMAL_ALLERGY='YES'
     retourner 9999
  fin si 
  si GUEST_FOOD_CONSTRAINT != 'NA' // Vérifier seulement si une contrainte existe
     si GUEST_FOOD_CONSTRAINT == 'végétarien' et HOST_FOOD != 'végétarien'
        retourner 9999 // Hôte doit proposer végétarien ou NA
     fin si
     si GUEST_FOOD_CONSTRAINT == 'nonuts' et HOST_FOOD != 'nonuts'
        retourner 9999 // Hôte doit proposer nonuts ou NA
     fin si
  fin si


  score = 5

  // Hobby
  // Retire 1 point pour chaque hobby commun
  nb_hobbies = nombre_hobbies_en_commun(hôte, visiteur)
  score = score - nb_hobbies // Ajoute 1 point par hobby

  // Age
  // Retire 1 point si la différence d'âge est inférieure à 18 mois
  si difference_age(hôte, visiteur) < 18 mois alors 
    score = score - 1
  fin si

  // Genre
  // Vérifie si les préférences de genre sont satisfaites
  // Une préférence vide (non spécifiée) est toujours considérée comme satisfaite
  pref_hote_ok = (hôte.pair_gender est non spécifié OU hôte.pair_gender == visiteur.gender)
  pref_visiteur_ok = (visiteur.pair_gender est non spécifié OU visiteur.pair_gender == hôte.gender)

  si pref_hote_ok ET pref_visiteur_ok alors
    // Retire 2 points si les préférences des deux sont satisfaites
    score = score - 2
  sinon si pref_hote_ok OU pref_visiteur_ok alors
    // Retire 1 point si la préférence d'un seul est satisfaite
    score = score - 1
  // Sinon (aucune préférence satisfaite), ne retire aucun point (0)
  fin si

  retourner score

fin fonction
```

## Retour sur l'exemple

Pour le premier cas

Matrice d'adjacence :

|      |  A1  |  A2  |  B1  |  B2  |
|------|------|------|------|------|
| W1   | 0    | 4    |  3   | 3    |
| W2   | 9999 | 9999 |  3   | 9999 |
| X1   | 9999 | 9999 |  2   | 9999 |
| X2   | 4    | 1    |  4   | 2    |


Affectation de coût minimal 8,000000 :
(W2, B1, 3,000000)
(W1, A1, 1,000000)
(X2, A2, 0,000000)
(X1, B2, 4,000000)

Pour le deuxième cas

Matrice d'adjacence :

|      |  A1  |  A2  |  B1  |  B2  |
|------|------|------|------|------|
| W1   | 0    | 5    |  3   | 5    |
| W2   | 9999 | 9999 | 9999 | 9999 |
| X1   | 9999 | 9999 |  2   | 9999 |
| X2   | 3    | 0    |  4   | 9999 |

Affectation de coût minimal 10001,000000 :
(W1, A1, 0,000000)
(X2, A2, 0,000000)
(W2, B2, 9999,000000)
(X1, B1, 2,000000)


On obtient dans les deux cas l'appariement trouvé auparavant.

## Robustesse de la modélisation (question difficile)

Le problème est le 9999 retourné en cas d'incompatibilité. En cas de grand nombre d'adolescents, l'algorithme va tenter de juste esquiver le coût de 9999 sans prendre en compte les préférences. Et en plus de cela, il va former des paires incompatibles quoi qu'il arrive.
La pénalité de 9999 est toujours plus coûteuse que n'importe quelle combinaison de scores compatibles.

\pagebreak

# Troisième version

## Équilibrage entre affinité / incompatibilité

| name | role    | hobbies             | gender | pair_gender | birth_date  | HOST_HAS_ANIMAL | HOST_FOOD
|------|---------|---------------------|--------|-------------|-------------|-----------------|-------------
| H1   | host    | painting, yoga      | m      | f           | 2001-05-12  | YES             | vegetarian
| H2   | host    | hiking, photography | f      | f           | 1997-11-23  | NO              | nonuts
| H3   | host    | chess, gardening    | m      | m           | 2003-07-08  | YES             | NA
| H4   | host    | music, swimming     | f      | f           | 2000-03-15  | NO              | vegetarian

| name | role    | hobbies           | gender | pair_gender | birth_date  | GUEST_ANIMAL_ALLERGY |  GUEST_FOOD_CONSTRAINT 
|------|---------|-------------------|--------|-------------|-------------|-----------------------|-------------------------
| V1   | guest   | painting, yoga    | f      | m           | 2002-06-30  | YES                  | NA
| V2   | guest   | hiking, photography| f     | f           | 1998-02-20  | NO                   | nonuts
| V3   | guest   | gardening, yoga   | m      | m           | 2004-12-01  | NO                   | vegetarian
| V4   | guest   | music, swimming   | m      | f           | 2001-02-20  | YES                  | NA


Paire (H1, V1) :
Affinité : Élevée (mêmes passe-temps : painting, yoga ; préférences de genre satisfaites ; écart d'âge < 18 mois).
Incompatibilité : Viole la contrainte animale (H1 a un animal, V1 est allergique).

Paire (H2, V2) :
Affinité : Élevée (mêmes passe-temps : hiking, photography ; préférences de genre satisfaites ; écart d'âge > 18 mois).
Incompatibilité : Aucune (respecte les contraintes animales et alimentaires : H2 n'a pas d'animal, nonuts de V2 correspond à nonuts de H2).

Paire (H3, V3) :
Affinité : Modérée (un passe-temps commun : gardening ; préférences de genre satisfaites ; écart d'âge < 18 mois).
Incompatibilité : Viole la contrainte alimentaire (V3 exige végétarien, H3 propose NA).

Paire (H4, V4) :
Affinité : Élevée (mêmes passe-temps : music, swimming ; préférences de genre satisfaites 1/2 ; écart d'âge < 18 mois).
Incompatibilité : Aucune (H4 n'a pas d'animal, V4 est allergique mais cela n'a pas d'impact ; NA alimentaire est compatible).

## Score d'affinité

```
fonction score_affinité_3(hôte, visiteur) : nombre // Retourne un nombre représentant l'affinité

  score = 5

  // Vérification des contraintes avec des pénalités finies
  si HOST_HAS_ANIMAL == 'YES' et GUEST_ANIMAL_ALLERGY == 'YES' alors
     score = score + 10 // Pénalité élevée pour incompatibilité animale
  fin si 
  si GUEST_FOOD_CONSTRAINT != 'NA' alors
     si GUEST_FOOD_CONSTRAINT == 'végétarien' et HOST_FOOD != 'végétarien' alors
        score = score + 10 // Pénalité élevée pour incompatibilité alimentaire
     fin si
     si GUEST_FOOD_CONSTRAINT == 'nonuts' et HOST_FOOD != 'nonuts' alors
        score = score + 10 // Pénalité élevée pour incompatibilité alimentaire
     fin si
  fin si

  // Passe-temps
  // Retire 1 point pour chaque passe-temps commun
  nb_hobbies = nombre_hobbies_en_commun(hôte, visiteur)
  score = score - nb_hobbies

  // Âge
  // Retire 1 point si la différence d'âge est inférieure à 18 mois
  si difference_age(hôte, visiteur) < 18 mois alors 
    score = score - 1
  fin si

  // Genre
  // Vérifie si les préférences de genre sont satisfaites
  pref_hote_ok = (hôte.pair_gender est non spécifié OU hôte.pair_gender == visiteur.gender)
  pref_visiteur_ok = (visiteur.pair_gender est non spécifié OU visiteur.pair_gender == hôte.gender)

  si pref_hote_ok ET pref_visiteur_ok alors
    score = score - 2
  sinon si pref_hote_ok OU pref_visiteur_ok alors
    score = score - 1
  fin si

  retourner score

fin fonction
```

## Retour sur l'exemple

|      |  H1  |  H2  |  H3  |  H4  |
|------|------|------|------|------|
| V1   | 10   | 4    | 13   | 4    |
| V2   | 14   | 0    | 15   | 14   |
| V3   | 3    | 14   | 11   | 4    |
| V4   | 14   | 4    | 13   | 1    |

Score de :

(H1/V1) = 10
(H2/V2) = 0
(H3/V3) = 11
(H4/V4) = 1

Mais si on exécute la matrice d'adjacence avec le JAR on trouve d'autres paires : 

Affectation de coût minimal 17,000000 :
(V4, H4, 0,000000)
(V3, H1, 3,000000)
(V2, H2, 1,000000)
(V1, H3, 13,000000)

Dans ce cas les scores sont plutôt proches sauf pour la paire V1 et H3 où c'est éloigné. 