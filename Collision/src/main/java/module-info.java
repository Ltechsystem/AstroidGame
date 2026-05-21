module Collision {
    requires Common;
    requires CommonBullet;
    requires CommonAsteroids;

    uses dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;

    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
}