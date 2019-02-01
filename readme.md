# Properties-manager

## Objectifs

Résoudre la problématique des constantes reposant sur des niveaux en avals comme par exemple, la constante **PROJECT** qui contient "N5_" ou "N9_" est qui est utilisée dans le socle technique.

## Fonctionnalités

Permettre de gérer des propriétés par service plutôt que par constante.
Permettre de gérer des propriétés avec résolutions de variables statique.
Exemple :

```java
nomComplet="${project}.${level}"
```

Permettre de gérer des propriétés avec résolution de variables dynamique.
Exemple :

```java
nomComplet="${project}.${level}.#{currentModule}"
```

Dans ce cas, la variable **currentModule** sera calculée au moment de la requête à la propriété **nomComplet**

## Exemple de code

### Paramètres STATIC simple

```java
pService.setProperty("project.name", "N5");
pService.setProperty("project.version", "1.0.0");
```

Pour ces valeurs, il une résolution dynamique au moment du setProperty.
L'appel à getProperty retourne toujours la même valeur.
Si la valeur de la propriété **project.name** change, la valeur de **project.version** reste la même.

### Paramètres STATIC avec variable

```java
pService.setProperty("project.name", "N5");
pService.setProperty("project.version", "1.0.0");
pService.setProperty("project.location", "/home/${project.name}/${project.version}");
```

Dans cet exemple, la résolution est possible au moment du setProperty pour toutes les valeurs.
Si les appels sont inversés comme le montre l'exemple ci-dessous, une résolution sera aussi appliquée au moment du getProperty.
Si le getProperty résoud totalement la valeur, tous les autres appels retourneront la valeur calculée et plus aucune résolution de sera faite.

```java
// project.name est totalement résolu
pService.setProperty("project.name", "N5"); 

// project.location est partiellement résolu -> "/home/N5/${project.version}"
pService.setProperty("project.location", "/home/${project.name}/${project.version}");

// project.version est totalement résolu
pService.setProperty("project.version", "1.0.0");

// Résolution totale de la valeur -> "/home/N5/1.0.0"
pService.getProperty("project.location");

// Retourne la valeur résolue sans recalcul de celle-ci
pService.getProperty("project.location");
```

### Paramètres DYNAMIC avec variable

```java
pService.setProperty("project.name", "N5");
pService.setProperty("project.version", "1.0.0");
pService.setProperty("project.location", "/home/#{project.name}/${project.version}");
```

## Fonctionnalités additionnelles

En standard, les variables d'environnement du système aisi que les propriétés système Java sont prises en charge.
Il est donc possible de créer des propriétés reposant sur ces valeurs.
Exemple :

```java
// Utilise une propriété de System.getProperty()
pService.setProperty("source.path", "/home/${os.arch}");

// Utilise une propriété de System.getEnv()
pService.setProperty("sylob.data.path", "${ProgramData}/sylob");
```
