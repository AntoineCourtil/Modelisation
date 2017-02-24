# Projet Modelisation

# Rendu 2 :


#Membres du groupe :
  - COURTIL Antoine


#Lien du github :
  - https://github.com/AntoineCourtil/Modelisation

#Utilisation :

Exécuter le .jar de la manière suivante :
	java -jar modelisation.jar \<pictureName> -options \<reduceyBy>


usage : java -jar modelisation.jar <pictureName> -options <reduceBy>
	options :
		-width : reduce width
		-height : reduce height

Liste des \<pictureName> :
  - ex1.pgm
  - ex2.pgm
  - ex3.pgm
  - test.pgm
  - paris.pgm
  - bateau.pgm

pour le \<reduceBy> :
  indiquer le nombre de pixel àreduire en largeur, par exemple 30.


Les images sont incluse dans le dossier res du projet, directement dans le jar.
On ne peut pas encore utiliser des images personelles.

Le logiciel va alors commencer la réduction, et une fois terminée, il affichera un message de fin.
L'image résultante se trouvera dans le dossier courant.


#Amélioration apportée

L'amélioration apportée est la possibilité de pouvoir supprimer des lignes et non des colonnes.
Ceci a donc du impliquer un nouveau système de graphe horizontal, ainsi qu'une nouvelle fonction d'interet.



#Reponse au botched_fds_1 :

Affiche dans l'ordre ou il a push les sommets dans la stack

Il push dans l'ordre ou si le fils n'a pas ete visite, alors il le push

Ici, il depile 0 et l'affiche, et push 1, 2 , 3

Puis depile 3 et l'affiche, et push 5.
Alors que 3 n'a pas a etre affiche


#Reponse au botched_fds_2 :

Il affiche a chaque fois tous les sommets fils  non visites du sommet actuel
c'est a dire quasiment dans l'ordre.

Il depile 0 et push 1, l'affiche, et fais pareil pour 2 et 3.
alors qu'il n'a pas a afficher 3.

Le sommet a etre depile est 3 donc
il push 5 et l'affiche et etc.


#Reponse au botched_fds_3 :

Etant donne que l'on ne met pas visite[s] = true a tous les sommets
une fois push dans la stack, on risque de les rajouter une nouvelle fois
si un autre sommet pointe vers lui puisqu'il ne sera toujours pas considere comme visite.


Exemple d'un graphe à 100 sommets :

On depile 0, on met visite[0]=true,
0 est lie de 1 à 99 donc on push de 1 à 99

on depile 99, on met visite[99]=true,
mais il est lié de 1 à 98 donc on push de 1 à 98

ici on a donc des doublons de 1 à 98.

on depile 98, on met visite[98]=true,
mais il est lié de 1 à 97 donc on push de 1 à 97

ici on a donc trois occurences de 1 à 97 dans la pile.

etc etc

Ceci montre bien que l'on peut facilement depasser la limite de la pile


#Reponse au botched_fds_4 :

Aucune