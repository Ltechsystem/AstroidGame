package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@Configuration
public class AppConfig {
    private static final java.lang.ModuleLayer pluginLayer =
            ServiceLocator.getPluginLayer();

    @Bean
    public List<IGamePluginService> gamePluginServices() {
        List<IGamePluginService> services = new ArrayList<>();
        loader(IGamePluginService.class).forEach(services::add);
        return services;
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServices() {
        List<IEntityProcessingService> services = new ArrayList<>();
        loader(IEntityProcessingService.class).forEach(services::add);
        return services;
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices() {
        List<IPostEntityProcessingService> services = new ArrayList<>();
        loader(IPostEntityProcessingService.class).forEach(services::add);
        return services;
    }

    private <S> ServiceLoader<S> loader(Class<S> service) {
        return pluginLayer != null
                ? ServiceLoader.load(pluginLayer, service)
                : ServiceLoader.load(service);
    }

    @Bean
    public Game game(List<IGamePluginService>           gamePluginServices,
                     List<IEntityProcessingService>      entityProcessingServices,
                     List<IPostEntityProcessingService>  postEntityProcessingServices) {
        return new Game(gamePluginServices, entityProcessingServices,
                postEntityProcessingServices, new ScoreClient());
    }
}