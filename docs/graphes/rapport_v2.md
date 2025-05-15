---
title: SAE S2.02 -- Rapport graphes -- Première Version
subtitle: Équipe D7
author: Yann Renard, Yanis Mekki, Rémy Martin 
date: 2025
---

# SAE S2.02 - Rapport Graphes - Deuxième Version

## Exemple avec appariement total

| name | role    | hobbies             | gender | pair_gender | birth_date  | HOST_HAS_ANIMAL | HOST_FOOD
|------|---------|---------------------|--------|-------------|-------------|-----------------|-------------
| A1   | host    | painting, yoga      | m      | f           | 2001-05-12  | YES             | NA
| A2   | host    | hiking, photography | f      | f           | 1997-11-23  | YES             | nonuts
| B1   | host    | chess, gardening    | m      | m           | 2003-07-08  | NO              | vegetarian
| B2   | host    | music  , swimming   | f      | f           | 2000-03-15  | NO              | NA

| name | role    | hobbies           | gender | pair_gender | birth_date  | GUEST_ANIMAL_ALLERGY |  GUEST_FOOD_CONSTRAINT 
|------|---------|-------------------|--------|-------------|-------------|-----------------------|-------------------------
| W1    | guest  | painting, yoga    | f      | m           | 2002-06-30  | NO                    | NA
| W2    | guest  | cooking, poetry   | m      | m           | 2000-01-17 | YES                   | vegetarian
| X1    | guest  | gardening, yoga   | m      | m           | 1999-11-15  | YES                   | NA
| X2    | guest  | hiking, photography |  f  | f       | 1998-02-20  | NO                    | nonuts

A1 avec W1 contraintes alimentaire et animal respectés, même hobbies, genre demandé et moins d'1 an et demi.
A2 avec X2 contraintes alimentaire et animal respectés, même hobbies, genre demandé et moins d'1 an et demi.
B1 avec W2 contraintes alimentaire et animal respectés, genre demandé.
B2 avec X1 contraintes alimentaire et animal respectés, moins d'1 an et demi.

## Exemple sans appariement total

| name | role    | hobbies             | gender | pair_gender | birth_date  | HOST_HAS_ANIMAL | HOST_FOOD
|------|---------|---------------------|--------|-------------|-------------|-----------------|-------------
| A1   | host    | painting, yoga      | m      | f           | 2001-05-12  | YES             | vegetarian
| A2   | host    | hiking, photography | f      | f           | 1997-11-23  | YES             | nonuts
| B1   | host    | chess, gardening    | m      | m           | 2003-07-08  | NO              | nonuts
| B2   | host    | music  , swimming   | f      | f           | 2000-03-15  | YES              | NA

| name | role    | hobbies           | gender | pair_gender | birth_date  | GUEST_ANIMAL_ALLERGY |  GUEST_FOOD_CONSTRAINT 
|------|---------|-------------------|--------|-------------|-------------|-----------------------|-------------------------
| W1    | guest  | painting, yoga    | f      | m           | 2002-06-30  | NO                    | NA
| W2    | guest  | cooking, poetry   | m      | m           | 2000-01-17 | YES                   | vegetarian
| X1    | guest  | gardening, yoga   | m      | m           | 1999-11-15  | YES                   | nonuts
| X2    | guest  | hiking, photography |  f  | f       | 1998-02-20  | NO                    | nonuts

A1 avec W1 contraintes alimentaire et animal respectés, même hobbies, genre demandé et moins d'1 an et demi.
A2 avec X2 contraintes alimentaire et animal respectés, même hobbies, genre demandé et moins d'1 an et demi.
B1 avec X1 contraintes alimentaire et animal respectés, 1 hobbies en commun, genre demandé.
W2 et X1 etant tous deux allergique au animaux et allergie alimentaires. Ils restent donc W2 et B2 sans paire.
On peut former 3 paires.


## Score d’affinité

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


## Retour sur l’exemple

Pour le premiere cas

Matrice d'adjacence :

;A1;A2;B1;B2 
W1;0;4;3;3 
W2;9999;9999;3;9999 
X1;9999;9999;2;9999 
X2;4;1;4;2


Affectation de cout minimal 8,000000 :
(W2, B1, 3,000000)
(W1, A1, 1,000000)
(X2, A2, 0,000000)
(X1, B2, 4,000000)

Pour le deuxième cas

Matrice d'adjacence :

;A1;A2;B1;B2
W1;0;5;3;5
W2;9999;9999;9999;9999
X1;9999;9999;2;9999
X2;3;0;4;9999

Affectation de cout minimal 10001,000000 :
(W1, A1, 0,000000)
(X2, A2, 0,000000)
(W2, B2, 9999,000000)
(X1, B1, 2,000000)


On obtient dans les deux cas l'appatriement trouvé auparavant.

## Robustesse de la modélisation (question difficile)

Le problème est le 9999 retourner en cas d'incompatibilité. En cas de grand nombre d'adolescents l'algorithme va tenté de juste esquiver le cout de 9999 sans prendre en compte les préférences. Et en plus de ca il va former es paires incompatibles quoi qu'il arrive.
La pénalité de 9999 est toujours plus coûteuse que n'importe quelle combinaison de scores compatibles.