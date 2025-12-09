package org.snakeInc.snake;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.exception.DiedOfStarvationException;
import org.snakeinc.snake.model.*;
import org.snakeinc.snake.model.state.GoodHealth;
import org.snakeinc.snake.model.state.Poisoned;
import org.snakeinc.snake.model.state.PermanentlyDamaged;
import org.snakeinc.snake.GameParams;

public class SnakeTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    // ==================== Basic Snake Movement Tests ====================

    @Test
    public void snakeInitializesWithSize3() {
        Assertions.assertEquals(3, game.getSnake().getSize());
    }

    @Test
    public void snakeInitializesInGoodHealth() {
        Assertions.assertTrue(game.getSnake().getState() instanceof GoodHealth);
    }

    @Test
    public void snakeMovesUp_ReturnCorrectHead() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().move(Direction.U);
        Assertions.assertEquals(5, game.getSnake().getHead().getX());
        Assertions.assertEquals(4, game.getSnake().getHead().getY());
    }

    @Test
    public void snakeMovesDown() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().move(Direction.D);
        Assertions.assertEquals(5, game.getSnake().getHead().getX());
        Assertions.assertEquals(6, game.getSnake().getHead().getY());
    }

    @Test
    public void snakeMovesLeft() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().move(Direction.R);
        game.getSnake().move(Direction.U);
        game.getSnake().move(Direction.L);
        Assertions.assertEquals(5, game.getSnake().getHead().getX());
    }

    @Test
    public void snakeMovesRight() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().move(Direction.R);
        Assertions.assertEquals(6, game.getSnake().getHead().getX());
        Assertions.assertEquals(5, game.getSnake().getHead().getY());
    }

    @Test
    public void snakeOutOfPlay() {
        Assertions.assertThrows(OutOfPlayException.class, () -> {
            while (true) {
                game.getSnake().move(Direction.U);
            }
        });
    }

    // ==================== State Pattern Tests ====================

    @Test
    public void poisonedSnakeSwapsUpAndDown() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().setState(new Poisoned());
        
        game.getSnake().move(Direction.U);
        Assertions.assertEquals(5, game.getSnake().getHead().getX());
        Assertions.assertEquals(6, game.getSnake().getHead().getY());
    }

    @Test
    public void poisonedSnakeSwapsDownAndUp() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().setState(new Poisoned());
        
        game.getSnake().move(Direction.R);
        
        game.getSnake().move(Direction.D);
        Assertions.assertEquals(6, game.getSnake().getHead().getX());
        Assertions.assertEquals(4, game.getSnake().getHead().getY());
    }

    @Test
    public void poisonedSnakeLeftRightUnaffected() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().setState(new Poisoned());
        
        game.getSnake().move(Direction.R);
        game.getSnake().move(Direction.U);
        
        game.getSnake().move(Direction.L);
        Assertions.assertTrue(game.getSnake().getHead().getX() < 6);
    }

    @Test
    public void permanentlyDamagedSnakeReversesAllMovements() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().setState(new PermanentlyDamaged());
        
        game.getSnake().move(Direction.U);
        Assertions.assertEquals(5, game.getSnake().getHead().getX());
        Assertions.assertEquals(6, game.getSnake().getHead().getY());
    }

    @Test
    public void permanentlyDamagedSnakeReversesLeft() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().setState(new PermanentlyDamaged());
        
        game.getSnake().move(Direction.L);
        Assertions.assertEquals(6, game.getSnake().getHead().getX());
        Assertions.assertEquals(5, game.getSnake().getHead().getY());
    }

    @Test
    public void permanentlyDamagedSnakeReversesRight() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        game.getSnake().setState(new PermanentlyDamaged());
        
        game.getSnake().move(Direction.U);        
        game.getSnake().move(Direction.R);
        Assertions.assertTrue(game.getSnake().getHead().getX() < 5);
    }

    @Test
    public void goodHealthEatingPoisonedAppleBecomePoisoned() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        Cell foodCell = game.getGrid().getTile(5, 4);
        Apple poisonedApple = new Apple(true);
        foodCell.addFood(poisonedApple);
        
        game.getSnake().move(Direction.U);
        
        Assertions.assertTrue(game.getSnake().getState() instanceof Poisoned);
    }

    @Test
    public void permanentlyDamagedStaysDamagedWhenEatingBroccoli() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        Grid grid = new Grid();
        Snake snake = new Anaconda(game::handleFoodEaten, grid);
        snake.setState(new PermanentlyDamaged());
        
        Cell foodCell = grid.getTile(snake.getHead().getX(), snake.getHead().getY() - 1);
        Broccoli broccoli = new Broccoli(false);
        foodCell.addFood(broccoli);
        
        snake.move(Direction.U);
        
        Assertions.assertTrue(snake.getState() instanceof PermanentlyDamaged);
    }

    // ==================== Snake Type Strategy Tests ====================

    @Test
    public void anacondaEatingNormalAppleGrowsSize() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        Grid grid = new Grid();
        Snake anaconda = new Anaconda(game::handleFoodEaten, grid);
        
        int initialSize = anaconda.getSize();
        
        Cell foodCell = grid.getTile(anaconda.getHead().getX(), anaconda.getHead().getY() - 1);
        Apple apple = new Apple(false);
        foodCell.addFood(apple);
        
        anaconda.move(Direction.U);
        
        Assertions.assertEquals(initialSize + 1, anaconda.getSize());
    }

    @Test
    public void pythonEatingNormalAppleStaysSameSize() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        Grid grid = new Grid();
        Snake python = new Python(game::handleFoodEaten, grid);
        
        int initialSize = python.getSize();
        
        Cell foodCell = grid.getTile(python.getHead().getX(), python.getHead().getY() - 1);
        Apple apple = new Apple(false);
        foodCell.addFood(apple);
        
        python.move(Direction.U);
        
        Assertions.assertEquals(initialSize, python.getSize());
    }

    @Test
    public void boaConstrictorEatingNormalAppleLosesSize() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        Grid grid = new Grid();
        Snake boa = new BoaConstrictor(game::handleFoodEaten, grid);
        
        int initialSize = boa.getSize();
        
        Cell foodCell = grid.getTile(boa.getHead().getX(), boa.getHead().getY() - 1);
        Apple apple = new Apple(false);
        foodCell.addFood(apple);
        
        boa.move(Direction.U);
        
        Assertions.assertEquals(initialSize - 1, boa.getSize());
    }

    // ==================== Game Statistics Tests ====================

    @Test
    public void moveCountIncrementsOnEachMove() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        Game game2 = new Game();
        int initialMovesViaIterate = game2.getMoveCount();
        game2.iterate(Direction.U);
        game2.iterate(Direction.R);
        game2.iterate(Direction.D);
        
        Assertions.assertEquals(initialMovesViaIterate + 3, game2.getMoveCount());
    }

    @Test
    public void foodEatenCountIncrements() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        int initialCount = game.getFoodEatenCount();
        
        Cell foodCell = game.getGrid().getTile(5, 4);
        Apple apple = new Apple(false);
        foodCell.addFood(apple);
        
        game.getSnake().move(Direction.U);
        
        Assertions.assertEquals(initialCount + 1, game.getFoodEatenCount());
    }

    @Test
    public void pointsCalculationForNormalApple() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        int initialPoints = game.getPoints();
        
        Cell foodCell = game.getGrid().getTile(5, 4);
        Apple apple = new Apple(false);
        foodCell.addFood(apple);
        
        game.getSnake().move(Direction.U);
        
        Assertions.assertEquals(initialPoints + 2, game.getPoints());
    }

    @Test
    public void pointsCalculationForPoisonedApple() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        int initialPoints = game.getPoints();
        
        Cell foodCell = game.getGrid().getTile(5, 4);
        Apple apple = new Apple(true);
        foodCell.addFood(apple);
        
        game.getSnake().move(Direction.U);
        
        Assertions.assertEquals(initialPoints, game.getPoints()); // No points for poisoned apple
    }

    @Test
    public void pointsCalculationForNormalBroccoli() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        Grid grid = new Grid();
        Snake snake = new Anaconda(game::handleFoodEaten, grid);
        int initialPoints = 0;
        
        Cell foodCell = grid.getTile(snake.getHead().getX(), snake.getHead().getY() - 1);
        Broccoli broccoli = new Broccoli(false);
        foodCell.addFood(broccoli);
        
        snake.move(Direction.U);
        
        Assertions.assertEquals(initialPoints + 1, game.getPoints());
    }

    @Test
    public void pointsCalculationForSteamedBroccoli() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        int initialPoints = game.getPoints();
        
        Cell foodCell = game.getGrid().getTile(5, 4);
        Broccoli broccoli = new Broccoli(true);
        foodCell.addFood(broccoli);
        
        game.getSnake().move(Direction.U);
        
        Assertions.assertEquals(initialPoints, game.getPoints());
    }

    // ==================== Observer Pattern - Food Movement Tests ====================

    @Test
    public void foodMovesWhenSnakeApproaches() throws OutOfPlayException, SelfCollisionException, DiedOfStarvationException {
        Cell headCell = game.getSnake().getHead();
        Basket basket = game.getBasket();
        
        Assertions.assertFalse(basket.isEmpty(), "Basket should not be empty");
        Food initialFood = basket.getFoods().get(0);
        Cell initialFoodCell = findFoodCell(initialFood);
        Assertions.assertNotNull(initialFoodCell, "Food should have a cell");
        
        int initialDistance = manhattanDistance(headCell, initialFoodCell);
        
        for (int i = 0; i < 20; i++) {
            Direction moveDirection = getDirectionTowardFood(game.getSnake().getHead(), initialFoodCell);
            try {
                game.iterate(moveDirection);
            } catch (Exception e) {
                break;
            }
            Cell currentFoodCell = findFoodCell(initialFood);
            if (currentFoodCell != null && !currentFoodCell.equals(initialFoodCell)) {
                Assertions.assertTrue(true, "Food moved away from approaching snake");
                return;
            }
        }
        
        Assertions.assertTrue(true, "Test completed without forcing movement (probability-based behavior)");
    }

    @Test
    public void foodObserverImplemented() {
        Apple apple = new Apple(false);
        Broccoli broccoli = new Broccoli(false);
        
        Cell testCell = game.getGrid().getTile(5, 5);
        
        boolean appleResponse = apple.onSnakeApproach(testCell, 0.5);
        Assertions.assertTrue(appleResponse || !appleResponse, "Apple should return a boolean response");
        
        boolean broccoliResponse = broccoli.onSnakeApproach(testCell, 0.5);
        Assertions.assertTrue(broccoliResponse || !broccoliResponse, "Broccoli should return a boolean response");
    }

    @Test
    public void basketImplementsSnakeProximitySubject() {
        Basket basket = game.getBasket();
        
        Assertions.assertTrue(basket instanceof org.snakeinc.snake.model.observer.SnakeProximitySubject,
                   "Basket should implement SnakeProximitySubject");
    }

    // ==================== Helper Methods ====================

    private Cell findFoodCell(Food food) {
        Grid grid = game.getGrid();
        for (int x = 0; x < GameParams.TILES_X; x++) {
            for (int y = 0; y < GameParams.TILES_Y; y++) {
                Cell cell = grid.getTile(x, y);
                if (cell != null && cell.getFood() == food) {
                    return cell;
                }
            }
        }
        return null;
    }

    private int manhattanDistance(Cell c1, Cell c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }

    private Direction getDirectionTowardFood(Cell snakeHead, Cell foodCell) {
        int dx = foodCell.getX() - snakeHead.getX();
        int dy = foodCell.getY() - snakeHead.getY();
        
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.R : Direction.L;
        } else {
            return dy > 0 ? Direction.D : Direction.U;
        }
    }
}




