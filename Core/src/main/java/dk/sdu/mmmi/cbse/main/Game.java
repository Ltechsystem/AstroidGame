package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private final List<IGamePluginService>          gamePlugins;
    private final List<IEntityProcessingService>    entityProcessors;
    private final List<IPostEntityProcessingService> postProcessors;
    private final ScoreClient                       scoreClient;

    private final GameData gameData = new GameData();
    private final World    world    = new World();

    private final Map<String, Polygon> polygonMap = new HashMap<>();
    private Pane          gameWindow;
    private Text          scoreLabel;
    private AnimationTimer gameLoop;

    public Game(List<IGamePluginService>            gamePlugins,
                List<IEntityProcessingService>       entityProcessors,
                List<IPostEntityProcessingService>   postProcessors,
                ScoreClient                          scoreClient) {
        this.gamePlugins      = gamePlugins;
        this.entityProcessors = entityProcessors;
        this.postProcessors   = postProcessors;
        this.scoreClient      = scoreClient;
    }

    public void start(Stage stage) {
        gameWindow = new Pane();
        gameWindow.setStyle("-fx-background-color: black;");

        scoreLabel = new Text("Score: 0");
        scoreLabel.setFill(Color.WHITE);
        scoreLabel.setX(10);
        scoreLabel.setY(20);
        gameWindow.getChildren().add(scoreLabel);

        Scene scene = new Scene(gameWindow,
                gameData.getDisplayWidth(),
                gameData.getDisplayHeight());
        scene.setFill(Color.BLACK);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP    -> gameData.getKeys().setKey(GameKeys.UP,    true);
                case LEFT  -> gameData.getKeys().setKey(GameKeys.LEFT,  true);
                case RIGHT -> gameData.getKeys().setKey(GameKeys.RIGHT, true);
                case SPACE -> gameData.getKeys().setKey(GameKeys.SPACE, true);
                default    -> {}
            }
        });
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case UP    -> gameData.getKeys().setKey(GameKeys.UP,    false);
                case LEFT  -> gameData.getKeys().setKey(GameKeys.LEFT,  false);
                case RIGHT -> gameData.getKeys().setKey(GameKeys.RIGHT, false);
                case SPACE -> gameData.getKeys().setKey(GameKeys.SPACE, false);
                default    -> {}
            }
        });

        for (IGamePluginService plugin : gamePlugins) {
            plugin.start(gameData, world);
        }

        for (Entity e : world.getEntities()) {
            addPolygon(e);
        }

        stage.setTitle("Asteroids");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

    public void render() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }
        };
        gameLoop.start();
    }

    private void update() {
        for (IEntityProcessingService processor : entityProcessors) {
            processor.process(gameData, world);
        }
        for (IPostEntityProcessingService postProcessor : postProcessors) {
            postProcessor.process(gameData, world);
        }
        // Post score to the scoring microservice
        scoreClient.postScore(gameData.getScore());
    }

    private void draw() {
        // Remove polygons for entities that no longer exist
        polygonMap.entrySet().removeIf(entry -> {
            if (world.getEntity(entry.getKey()) == null) {
                gameWindow.getChildren().remove(entry.getValue());
                return true;
            }
            return false;
        });

        // Add polygons for new entities
        for (Entity entity : world.getEntities()) {
            if (!polygonMap.containsKey(entity.getID())) {
                addPolygon(entity);
            }
            updatePolygon(entity);
        }

        scoreLabel.setText("Score: " + gameData.getScore());
        scoreLabel.toFront();
    }

    private void addPolygon(Entity entity) {
        if (entity.getPolygonCoordinates() == null) return;
        Polygon poly = new Polygon(entity.getPolygonCoordinates());
        poly.setStroke(Color.WHITE);
        poly.setFill(Color.TRANSPARENT);
        polygonMap.put(entity.getID(), poly);
        gameWindow.getChildren().add(poly);
    }

    private void updatePolygon(Entity entity) {
        Polygon poly = polygonMap.get(entity.getID());
        if (poly == null) return;
        poly.setTranslateX(entity.getX());
        poly.setTranslateY(entity.getY());
        poly.setRotate(entity.getRotation());
    }
}