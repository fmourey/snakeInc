## Installation

1. **Clone the repository**
```bash
git clone https://github.com/fmourey/snakeInc.git
cd snakeInc
```

2. **Run docker-compose**
```bash
docker-compose up -d
```

3. **Build both modules**
```bash
# Build API
cd api
./gradlew clean build

# Build Snake Game
cd ../snake
./gradlew clean build
```

4. **Start the API server**
```bash
cd api
./gradlew bootRun
```

5. **Launch the game**
```bash
cd snake
./gradlew run
```

## Usage

### Game Flow

1. **Launch** → Player selection dialog appears
2. **Select/Create Player** → Choose or register a player
3. **Play** → Use arrow keys to move snake and collect food
4. **Game Over** → Score automatically submitted to database
5. **Statistics** → View your performance via API

### API Endpoints

#### Players
```
POST   /api/v1/players              Create new player
GET    /api/v1/players/{id}         Get player by ID
GET    /api/v1/players?name=...     Get player by name
```

#### Scores
```
POST   /api/v1/scores               Submit a score
GET    /api/v1/scores/{id}          Get score details
GET    /api/v1/scores?player=...    Get all scores for player
GET    /api/v1/scores/stats?player= Get player statistics
```

## Running Tests

### API Tests
```bash
cd api
./gradlew test
```

### Game Tests
```bash
cd snake
./gradlew test
```

## Game Configuration
Edit `snake/src/main/java/org/snakeinc/snake/GameParams.java`:
```java
public static final int TILES_X = 50;
public static final int TILES_Y = 50;
public static final int SNAKE_DEFAULT_X = 25;
public static final int SNAKE_DEFAULT_Y = 25;
```