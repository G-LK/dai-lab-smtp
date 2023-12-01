# Labo SMTP - DAI
Groupe: Leonard Klasen et Samuel Roland

## Objectif
Le but de ce laboratoire est de se familiariser avec le protocole SMTP en écrivant un programme Java permettant d'envoyer une campagne d'emails pranks (de blagues). Le but est d'intéragir directement avec un serveur SMTP via des sockets TCP Java, sans aide de librairie pour implémenter l'envoi d'emails en SMTP.

**Note: ce projet est fait uniquement dans un but d'apprentissage et ne doit jamais être utilisé sur un vrai serveur SMTP. Nous utilisons ici un serveur mock (maildev) qui permet de simuler l'envoi d'emails vers un vrai serveur.**

## Configuration
La liste des adresses emails des victimes se trouvent dans `victims.json` et la liste des emails de pranks sont dans `messages.json`. Ces 2 fichiers de configurations doivent rester dans leur format actuels et doivent être du JSON valide.

## Exécution
Pour lancer le serveur maildev, on peut le faire avec Docker:
```
docker run -d -p 1080:1080 -p 1025:1025 maildev/maildev
```

A cette étape, il est déjà possible d'ouvrir l'interface web de maildev sur `localhost:1080`.

Pour lancer notre programme, il faut déjà cloner ce repository:
```
git clone https://github.com/G-LK/dai-lab-smtp.git
cd dai-lab-smtp
```

Il faut d'abord le compiler (avec Maven):
```
mvn package
```

Si nécessaire, il est possible de changer la liste des victimes (`victims.json`) et des messages (`messages.json`).

Et pour lancer le fichier `.jar` créé sous target, il suffit de lancer:
```
java -jar target/*.jar <number of groups>
```
où `<number of groups>` est un nombre positif de groupes souhaités. Chaque groupe recevra un email et est composé de 2 à 5 adresses de victimes. Une de ces adresses sera utilisée comme expéditeur. Un message est choisi au hasard parmi la liste et utilisé comme sujet et corps de l'email.

Dans l'interface web de maildev on devrait voir le nombre d'emails attendus reçus et le tour est joué !

## Fonctionnement
Voici déjà un diagramme de classe pour avoir une idée du fonctionnement:
TODO: generate Mermaid diagram

**En résumé, notre programme suit les étapes suivantes:**
1. `Main` crée un objet `Sender` 
   1. Vérifie la validité du premier paramètre passé au constructeur
   1. L'objet `config` charge la configuration depuis les 2 fichiers
   1. Il valide la configuration et affiche des erreurs si nécessaire
1. `Main` appelle ensuite successivement les 2 étapes et s'arrête en cas de retour `false`, qui signifie qu'il y a eu une erreur et que le programme doit s'arrêter. Les exceptions sont égalements catchées.
   1. `Main` appelle `Sender.prepare()` pour lancer la génération les emails (aléatoirement depuis les valeurs de configuration) en s'assurant d'avoir des adresses uniques
   1. `Main` appelle `Sender.connectAndSend()`, pour qu'il cherche à se connecter sur le port `1025` sur l'hôte `localhost` et génère une erreur s'il n'arrive pas s'y connecter. Si la connexion est établie, il peut envoyer les emails l'un après l'autre.
1. Fin du programme

**Exemples de dialogue**
TODO: inclure image du détails des échanges loggés dans la console

**Tests**
Nous avons écrit de nombreux tests unitaires avec JUnit et architecturé notre programme de façon à faciliter leurs écritures.

TODO: ajouter des détails et image d'execution