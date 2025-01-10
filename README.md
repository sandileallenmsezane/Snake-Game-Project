# Java Snake Game 🐍

A classic Snake game implementation in Java using Swing. This project features a modern take on the classic Snake game with multiple difficulty levels, score tracking, and smooth graphics.

## Features

- 🎮 Classic snake gameplay mechanics
- 🏃 Multiple difficulty levels (Easy, Medium, Hard)
- 📊 Score and high score tracking
- 🎨 Smooth graphics with anti-aliasing
- 🔄 Game state management
- 🎯 Intelligent food placement
- 🔊 Responsive controls
- 📱 Clean and modern UI

## Screenshots



## How to run

To run this game, you'll need:
- Java Development Kit (JDK) 8 or higher
- Any Java IDE (Eclipse, IntelliJ IDEA, etc.) or command line tools



## How to Play

1. Launch the game
2. Select your preferred difficulty level
3. Click "Start" or press any arrow key to begin
4. Use arrow keys to control the snake:
   - ⬆️ Up Arrow: Move up
   - ⬇️ Down Arrow: Move down
   - ⬅️ Left Arrow: Move left
   - ➡️ Right Arrow: Move right
5. Press Space to restart when game is over
6. Try to eat the food (yellow squares) to grow longer
7. Avoid hitting the walls or the snake's own body

## Game Controls

| Key           | Action        |
|---------------|---------------|
| Arrow Keys    | Move Snake    |
| Space Bar     | Quick Restart |

## Difficulty Levels

- **Easy**: Slower snake movement (100ms delay)
- **Medium**: Moderate speed (50ms delay)
- **Hard**: Fast movement (20ms delay)

## Project Structure

```
snake-game/
│
├── src/
│   ├── App.java          # Main application entry point
│   ├── SnakeGame.java    # Core game logic and rendering
│   └── Level.java        # Game difficulty enumeration
│
└── 
```

## Technical Details

- Built with Java Swing for GUI
- Uses custom rendering with Java 2D graphics
- Implements game loop using javax.swing.Timer
- Object-oriented design with clear separation of concerns
- Smooth animations and collision detection


## Future Improvements

- [ ] Add sound effects
- [ ] Implement power-ups
- [ ] Add different types of food
- [ ] Create different snake skins
- [ ] Add a local leaderboard
- [ ] Implement save game functionality
- [ ] Add background music
- [ ] Create different map layouts


## Author

Sandile Allen Msezane
- GitHub: @sandileallenmsezane(https://github.com/sandileallenmsezane)
- LinkedIn: Sandile Msezane(https://linkedin.com/in/sandile-msezane)

## Acknowledgments

- Inspired by the classic Snake game
