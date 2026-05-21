package dk.sdu.mmmi.cbse.common.data;

public class GameData {

    private int displayWidth  = 800;
    private int displayHeight = 800;
    private final GameKeys keys = new GameKeys();
    private int score            = 0;
    private int asteroidsToSpawn = 0;
    private int enemiesToSpawn   = 0;

    public void queueAsteroidSpawn() { asteroidsToSpawn++; }
    public boolean pollAsteroidSpawn() {
        if (asteroidsToSpawn > 0) { asteroidsToSpawn--; return true; }
        return false;
    }

    public void queueEnemySpawn() { enemiesToSpawn++; }
    public boolean pollEnemySpawn() {
        if (enemiesToSpawn > 0) { enemiesToSpawn--; return true; }
        return false;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    public GameKeys getKeys() {
        return keys;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void resetScore() {
        this.score = 0;
    }
}