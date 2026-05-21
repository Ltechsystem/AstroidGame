module Core {
    requires Common;
    requires CommonBullet;

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.base;

    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires spring.aop;
    requires spring.expression;
    requires java.net.http;  /* for ScoreClient HttpClient */

    /* Spring needs reflective access to create proxies and inject dependencies */
    opens dk.sdu.mmmi.cbse.main to spring.core, spring.beans, spring.context, javafx.graphics;

    exports dk.sdu.mmmi.cbse.main;

    /* JavaLab + JPMSLab2: declare which services this module consumes */
    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
}