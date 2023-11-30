# Labo SMTP - DAI
Groupe: Leonard Klasen et Samuel Roland

## Objectif
Le but de ce laboratoire est de se familiariser avec le protocole SMTP en écrivant un programme Java permettant d'envoyer une campagne d'emails pranks (de blagues). Le but est d'intéragir directement avec un serveur SMTP via des sockets Java, sans aide de librairie pour implémenter l'envoi d'emails en SMTP.

**Note: ce projet est fait uniquement dans un but d'apprentissage et ne doit jamais être utilisé sur un vrai serveur SMTP. Nous utilisons ici un serveur mock (maildev) qui permet de simuler l'envoi d'emails vers un vrai serveur.**

## Configuration
La liste des adresses emails des victimes se trouvent dans `victims.json` et la liste des emails de pranks sont dans `messages.json`. Ces 2 fichiers de configurations doivent rester dans leur format actuels et doivent être du JSON valide.

## Exécution
Pour lancer le serveur maildev, on peut le faire avec Docker:
```
docker run -d -p 1080:1080 -p 1025:1025 maildev/maildev
```

Pour lancer notre programme, il faut d'abord le compiler (avec Maven):
```
mvn package
```
Et pour lancer le fichier `.jar` créé sous target, il suffit de lancer:
```
java -jar target/*.jar <group number>
```
où `<group number>` est un nombre de groupes souhaités. Chaque groupe recevra un email et est composé de 2 à 5 adresses de victimes. Une de ces adresses sera utilisée comme expéditeur. Un message est choisi au hasard parmi la liste et utilisé comme sujet et corps de l'email.

