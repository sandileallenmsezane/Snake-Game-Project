import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static class Tile {
        int x;
        int y;
        
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    // Constants
    private static final int TILE_SIZE = 25;
    private static final Color SNAKE_HEAD_COLOR = new Color(0, 100, 255);
    private static final Color SNAKE_BODY_COLOR = new Color(0, 150, 255);
    private static final Color FOOD_COLOR = new Color(255, 215, 0);
    private static final Color GRID_COLOR = new Color(50, 50, 50);
    
    // Game components
    private final int WIDTH;
    private final int HEIGHT;
    private final JComboBox<Level> levelComboBox;
    private final JButton startButton;
    private final JButton restartButton;
    
    // Game state
    private Tile head;
    private final ArrayList<Tile> body;
    private Tile food;
    private final Random random;
    private int velocityX;
    private int velocityY;
    private Timer gameSpeed;
    private int highScore = 0;
    private boolean gameStart = false;
    private boolean gameOver = false;
    private int currentScore = 0;
    private Level currentLevel;
    
    public SnakeGame(int boardWidth, int boardHeight) {
        this.WIDTH = boardWidth;
        this.HEIGHT = boardHeight;
        
        // Initialize components
        levelComboBox = new JComboBox<>(Level.values());
        startButton = new JButton("Start");
        restartButton = new JButton("Restart");
        body = new ArrayList<>();
        random = new Random();
        
        setupGame();
        setupControls();
        setupUI();
    }
    
    private void setupGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        
        // Initialize snake
        resetGame();
        
        // Initialize timer
        gameSpeed = new Timer(Level.MEDIUM.getDelay(), this);
    }
    
    private void setupControls() {
        startButton.addActionListener(e -> {
            gameStart = true;
            gameSpeed.start();
            requestFocusInWindow();
        });
        
        restartButton.addActionListener(e -> {
            resetGame();
            gameSpeed.start();
            requestFocusInWindow();
        });
        
        levelComboBox.addActionListener(e -> {
            currentLevel = (Level) levelComboBox.getSelectedItem();
            gameSpeed.setDelay(currentLevel.getDelay());
        });
    }
    
    private void setupUI() {
        setLayout(null);
        
        // Position components
        levelComboBox.setBounds(WIDTH/2 - 100, HEIGHT/2 - 50, 200, 30);
        startButton.setBounds(WIDTH/2 - 100, HEIGHT/2, 200, 30);
        restartButton.setBounds(WIDTH/2 - 100, HEIGHT/2 + 50, 200, 30);
        
        add(levelComboBox);
        add(startButton);
        add(restartButton);
        
        restartButton.setVisible(false);
    }
    
    private void resetGame() {
        head = new Tile(WIDTH/(2*TILE_SIZE), HEIGHT/(2*TILE_SIZE));
        body.clear();
        velocityX = 0;
        velocityY = 0;
        gameOver = false;
        currentScore = 0;
        placeFood();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (!gameStart) {
            drawStartScreen(g2d);
        } else if (gameOver) {
            drawGameOverScreen(g2d);
        } else {
            drawGame(g2d);
        }
    }
    
    private void drawStartScreen(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        String title = "Snake Game";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (WIDTH - titleWidth)/2, HEIGHT/3);
        
        levelComboBox.setVisible(true);
        startButton.setVisible(true);
        restartButton.setVisible(false);
    }
    
    private void drawGameOverScreen(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String gameOverText = "Game Over!";
        String scoreText = "Score: " + currentScore;
        String highScoreText = "High Score: " + highScore;
        
        int gameOverWidth = g.getFontMetrics().stringWidth(gameOverText);
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
        int highScoreWidth = g.getFontMetrics().stringWidth(highScoreText);
        
        g.drawString(gameOverText, (WIDTH - gameOverWidth)/2, HEIGHT/3);
        g.drawString(scoreText, (WIDTH - scoreWidth)/2, HEIGHT/2);
        g.drawString(highScoreText, (WIDTH - highScoreWidth)/2, 2*HEIGHT/3);
        
        levelComboBox.setVisible(false);
        startButton.setVisible(false);
        restartButton.setVisible(true);
    }
    
    private void drawGame(Graphics2D g) {
        // Draw grid
        g.setColor(GRID_COLOR);
        for (int i = 0; i < WIDTH/TILE_SIZE; i++) {
            g.drawLine(i*TILE_SIZE, 0, i*TILE_SIZE, HEIGHT);
            g.drawLine(0, i*TILE_SIZE, WIDTH, i*TILE_SIZE);
        }
        
        // Draw food
        g.setColor(FOOD_COLOR);
        g.fillRoundRect(food.x*TILE_SIZE, food.y*TILE_SIZE, TILE_SIZE, TILE_SIZE, 10, 10);
        
        // Draw snake body
        g.setColor(SNAKE_BODY_COLOR);
        for (Tile bodyPart : body) {
            g.fillRoundRect(bodyPart.x*TILE_SIZE, bodyPart.y*TILE_SIZE, TILE_SIZE, TILE_SIZE, 8, 8);
        }
        
        // Draw snake head
        g.setColor(SNAKE_HEAD_COLOR);
        g.fillRoundRect(head.x*TILE_SIZE, head.y*TILE_SIZE, TILE_SIZE, TILE_SIZE, 8, 8);
        
        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + currentScore, 10, 30);
        g.drawString("High Score: " + highScore, WIDTH - 150, 30);
        
        levelComboBox.setVisible(false);
        startButton.setVisible(false);
        restartButton.setVisible(false);
    }
    
    private void placeFood() {
        boolean validPosition;
        do {
            food = new Tile(random.nextInt(WIDTH/TILE_SIZE), random.nextInt(HEIGHT/TILE_SIZE));
            validPosition = true;
            
            // Check if food spawns on snake
            if (food.x == head.x && food.y == head.y) {
                validPosition = false;
            }
            for (Tile bodyPart : body) {
                if (food.x == bodyPart.x && food.y == bodyPart.y) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
    }
    
    private void move() {
        // Check if game hasn't started moving
        if (velocityX == 0 && velocityY == 0) {
            return;
        }
        
        // Move body
        for (int i = body.size()-1; i >= 0; i--) {
            Tile bodyPart = body.get(i);
            if (i == 0) {
                bodyPart.x = head.x;
                bodyPart.y = head.y;
            } else {
                Tile prevBodyPart = body.get(i-1);
                bodyPart.x = prevBodyPart.x;
                bodyPart.y = prevBodyPart.y;
            }
        }
        
        // Move head
        head.x += velocityX;
        head.y += velocityY;
        
        // Check collisions
        checkCollisions();
    }
    
    private void checkCollisions() {
        // Wall collision
        if (head.x*TILE_SIZE < 0 || head.x*TILE_SIZE >= WIDTH || 
            head.y*TILE_SIZE < 0 || head.y*TILE_SIZE >= HEIGHT) {
            gameOver = true;
            return;
        }
        
        // Body collision
        for (Tile bodyPart : body) {
            if (head.x == bodyPart.x && head.y == bodyPart.y) {
                gameOver = true;
                return;
            }
        }
        
        // Food collision
        if (head.x == food.x && head.y == food.y) {
            body.add(new Tile(food.x, food.y));
            currentScore++;
            if (currentScore > highScore) {
                highScore = currentScore;
            }
            placeFood();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStart && !gameOver) {
            move();
            repaint();
        }
        if (gameOver) {
            gameSpeed.stop();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (velocityY != 1) {
                    velocityX = 0;
                    velocityY = -1;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (velocityY != -1) {
                    velocityX = 0;
                    velocityY = 1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (velocityX != 1) {
                    velocityX = -1;
                    velocityY = 0;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (velocityX != -1) {
                    velocityX = 1;
                    velocityY = 0;
                }
                break;
            case KeyEvent.VK_SPACE:
                if (gameOver) {
                    resetGame();
                    gameSpeed.start();
                }
                break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
}