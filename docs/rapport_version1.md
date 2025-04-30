---
title: SAE S2.02 -- Rapport graphes -- Première Version
subtitle: Équipe D7
author: Yann Renard, Yanis Mekki, Rémy Martin 
date: 2025
---

# Version 1

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

A avec W
B avec Z 
C avec x
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

```
# Matrice d'adjacence
   ;A;B;C;D
  W;0;4;2;3   
  X;3;4;3;3  
  Y;3;3;3;2  
  Z;2;1;3;2  


Affectation de cout minimal 6,000000 :
(Z, B, 1,000000)
(X, C, 3,000000)
(W, A, 0,000000)
(Y, D, 2,000000)

Oui on trouve le même agecement que trouvé précédemment. 