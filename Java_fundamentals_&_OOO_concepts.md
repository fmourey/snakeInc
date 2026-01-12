Launch the app. Explore the code.
Explain briefly these principles and find in the snake's code parts to illustrate them.

### Class, object, primitive types
Une classe définit un type d'objet et la manière dont cet objet interagit : ses attributs, ses méthodes, ...
Un exemple de classe : 
```java
public class AppleFactory {

    public static Apple createAppleInCell(Cell cell) {
        Apple apple = new Apple();
        cell.addApple(apple);
        return apple;
    }

}
```

Un objet, c'est une instance d'une classe. Dans l'exemple ci-dessous, on crée un objet apple de la classe Apple en appelant le constructeur présent dans la méthode createAppelInCell de la classe AppleFactory (Apple apple = new Apple();)
```java
Apple apple = AppleFactory.createAppleInCell(cell);
```

Les types primitifs représentent les types de base de Java : byte, short, int, long, float, double, boolean, char. 
On en trouve par exemple pour définir les coordonnées des cases : 
```java
public class Cell {

    @Getter
    private int x;

    @Getter
    private int y;
    ...
}
```


### Encapsulation, properties, getter and setter, final.

L'encapsulation c'est le fait d'avoir des attributs, méthodes, classes, etc. définis comme étant public, privé ou protégé. 

Les properties, ou attributs, sont les données que portent les instances d'une classe. Par exemple, Une instance de la classe Cell possède comme attributs les coordonnées x et y.

Les getters et setters sont des méthodes publiques qui permettent d'accéder à des attributs privés en dehors d'une classe.
Dans le code, on en a par exemple dans la classe Cell : 
```java
    @Getter
    private int x;

    @Getter
    private int y;
```

Un élément "final" est un élément qui ne peut pas être modifié. On ne peut pas masquer une méthode finale dans une classe fille, on ne peut pas modifier la valeur d'une variable final etc.
Dans la classe Game, tous les attributs sont final : 

```java
public class Game {

    private final Grid grid;
    private final Basket basket;
    private final Snake snake;

    ...
}
```

### Instantiation of objects, Constructors.

Un constructeur est une méthode spéciale dans une classe qui permet de créer un objet en initialisant la valeur des ses attributs.
Un constructeur est une méthode qui a le même nom que sa classe : 

```java
public class Basket {

    private Grid grid;
    private List<Apple> apples;

    public Basket(Grid grid) {
        apples = new ArrayList<>();
        this.grid = grid;
    }

    ...
}
```

Lorsqu'on appelle un constructeur, on crée une instance de la classe.

### Static fields, static methods. What are the particularity of "static" ?

Lorsqu'une méthode ou un attribut est "static", cela signifie qu'on n'a pas besoin d'une instance de la classe qui définit cette méthode ou cet attribut pour pouvoir accéder à l'attrbut ou la méthode.


### Composition.

La composition est une manière de définir la relation entre deux classes. Si la classe A ne peut pas exister (ou si cela n'aurait pas de sens) sans la classe B, on dit qu'il y a composition. Si les deux classes peuvent exister l'une sans l'autre, on parle plutôt d'agrégation.

Par exemple, la classe AppleFactory ne pourrait pas exister (ou n'aurait pas de sens) sans la classe Apple. Il y a composition.

### Inheritance, interface, polymorphism
Une Classe peut hériter d'une autre. On appelle ça l'héritage. Quand une classe hérite d'une autre, elle a accès aux méthodes et aux attributs de la classe mère, et peut étender / modifier ces derniers. 

Une interface est une classe qui ne possède que des méthodes virtuelles.

Le Polymorphisme, c'est le fait de pouvoir définir plusieurs méthodes avec le même nom tant qu'elles n'ont pas la même signature (ne demandent pas les mêmes paramètres). Le programme déterminera laquelle des méthodes exécuter.

### Static VS dynamic types.

### Separation of concerns (design principle)

Diviser pour mieux régner ...?

### Collections
Les collections sont des 

### Exceptions

Les exceptions sont une manière de gérer les erreurs lors de l'exécution (runtime) pour récolter des informations ou éviter que le programme ne plante.

Par exemple, dans le programme : 
```java
public void move(char direction) throws OutOfPlayException, SelfCollisionException {...}
```
### Functional interfaces / Lambda

Une interface fonctionnelle est une interface qui ne possède qu'une seule méthode abstraite.

Lambda correspond à la définition d'une fonction "anonyme", qu'on peut définir à n'importe quel moment dans le code : pas besoin de créer une classe et d'appeler la méthode.

### Lombok : https://projectlombok.org/

Une librairie externe qui permet de simplifier le code : getters, setters etc. Seuelement besoin d'ajouter un @getter au lieu de définir la fonction à chaque fois.