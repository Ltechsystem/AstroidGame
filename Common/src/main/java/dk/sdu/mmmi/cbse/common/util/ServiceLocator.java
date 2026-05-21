package dk.sdu.mmmi.cbse.common.util;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ServiceLocator {

    private static final String PLUGINS_DIR = "plugins";

    public static ModuleLayer getPluginLayer() {
        Path pluginsDir = Paths.get(PLUGINS_DIR);
        if (!Files.isDirectory(pluginsDir)) return null;

        List<Path> jars;
        try {
            jars = Files.list(pluginsDir)
                    .filter(p -> p.toString().endsWith(".jar"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("[ServiceLocator] Cannot list plugins/: " + e.getMessage());
            return null;
        }

        if (jars.isEmpty()) return null;

        try {
            ModuleFinder finder = ModuleFinder.of(jars.toArray(Path[]::new));
            Set<String> roots   = finder.findAll().stream()
                    .map(ref -> ref.descriptor().name())
                    .collect(Collectors.toSet());

            ModuleLayer   parent = ModuleLayer.boot();
            Configuration config = parent.configuration()
                    .resolve(finder, ModuleFinder.of(), roots);

            return parent.defineModulesWithOneLoader(
                    config, ClassLoader.getSystemClassLoader());

        } catch (Exception e) {
            System.err.println("[ServiceLocator] Failed to build plugin layer: " + e.getMessage());
            return null;
        }
    }
}