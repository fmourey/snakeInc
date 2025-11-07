package org.snakeinc.snake.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Data
@EqualsAndHashCode
public class Cell {

    @Getter
    private int x;

    @Getter
    private int y;

    List<GameObject> gameObjectsInTile = new ArrayList<>();

    protected Cell(int x, int y) {
        setX(x);
        setY(y);
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjectsInTile.add(gameObject);
    }

}
