
# Projet JAVA GB4 BIMB (Bioinformatique et modélisation pour la biologie)

Ce projet consiste à recréer un réseau social à l'aide de JAVA. Ce programme gère différents types de posts (texte, image et vidéo) grâce à l'utilisation de la bibliothèque javafx et du driver SQLite pour la gestion de la base de données.

## Préparation

Ce projet a été compilé à l'aide du JDK 19, une version équivalente est donc nécessaire pour éxécuter le programme.

## Utilisation

Ce programme a été principalement testé sur la distribution Ubuntu 22.04 de Linux et un peu sur MacOS. Son fonctionnement sur Windows n'est pas garanti.

Pour lancer le programme sur uns distribution linux:
```bash
bash boot_linux
```

Pour le lancer sur MacOS (zsh):
```zsh
zsh boot_mac
```

Si votre MacOs ne prend pas en charge zsh, veuillez utiliser la commande bash. Si vous rencontrez des erreurs de lancement liées à OpenGL2 ou autres, veuillez télécharger une autre version de javafx pour MacOs selon correspondante à votre système (x64, x86...). Il faudra simplement respecter le même répertoire que celui d'origine.
Ce script bash execute le .jar du projet qui ne doit pas être déplacé de son emplacement d'origine.

## Bug reports

- La barre de recherche peut entrainer un "softlock" de l'application si vous essayez d'accéder à un mur sur lequel vous êtes bloqué. La fenêtre d'erreur apparaitra autant de fois que vous avez entré de lettres pour trouver le mur en question.

- Lorsque vous postez des images ou des vidéos, veuillez à bien changer le titre des cer dernières pour éviter un bug de duplication.

## Authors
Edmond BERNE
- [@Edmondbrn](https://www.github.com/Edmondbrn)


## Gallery

![Logo](image1.png)
![Logo](image2.png)


