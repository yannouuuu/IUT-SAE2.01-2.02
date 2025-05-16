---
title: SAE S2.02 -- Rapport graphes -- Première Version
subtitle: Équipe D7
author: Yann Renard, Yanis Mekki, Rémy Martin 
date: 2025
---

# SAE S2.02 - Rapport Graphes - Troisième Version

## Equilibrage entre affinité / incompatibilité

| name | role    | hobbies             | gender | pair_gender | birth_date  | HOST_HAS_ANIMAL | HOST_FOOD
|------|---------|---------------------|--------|-------------|-------------|-----------------|-------------
| H1   | host    | painting, yoga      | m      | f           | 2001-05-12  | YES             | vegetarian
| H2   | host    | hiking, photography | f      | f           | 1997-11-23  | NO             | nonuts
| H3   | host    | chess, gardening    | m      | m           | 2003-07-08  | YES              | NA
| H4   | host    | music  , swimming   | f      | f           | 2000-03-15  | NO              | vegetarian

| name | role    | hobbies           | gender | pair_gender | birth_date  | GUEST_ANIMAL_ALLERGY |  GUEST_FOOD_CONSTRAINT 
|------|---------|-------------------|--------|-------------|-------------|-----------------------|-------------------------
| V1    | guest  | painting, yoga    | f      | m           | 2002-06-30  | YES                    | NA
| v2    | guest  | hiking, photography   | f      | f           | 1998-02-20 | NO                   | nonuts
| V3    | guest  | gardening, yoga   | m      | m           | 2004-12-01  | NO                   | vegetarian
| V4    | guest  | music, swimming |  m  | f       | 2001-02-20  | YES                    | NA


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


## Score d’affinité

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

## Retour sur l’exemple

;H1;H2;H3;H4
V1;10;4;13;4
V2;14;0;15;14
V3;3;14;11;4
V4;14;4;13;1

Score de :

(H1/V1) = 10
(H2/V2) = 0
(H3/V3) = 11
(H4/V4) = 1

Mais si on éxecute la matrice d'adjacence avec le jar on trouve d'autres pairs : 

Affectation de cout minimal 17,000000 :
(V4, H4, 0,000000)
(V3, H1, 3,000000)
(V2, H2, 1,000000)
(V1, H3, 13,000000)

Dans ce cas les scores sont plûtot proches sauf pour la pair V1 et H3 ou c'est éloignés.