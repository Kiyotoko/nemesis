package org.nemesis.game;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.nemesis.game.Factory.ProjectileFactory;
import org.nemesis.game.Factory.UnitFactory;

public class ResourceLoader {
    private final Map<String, ProjectileFactory> projectileFactories = new HashMap<>();
    private final Map<String, UnitFactory> unitFactories = new HashMap<>();

    public static final ResourceLoader DEFAULT_RESOURCE_LOADER = new ResourceLoader();
    static {
        DEFAULT_RESOURCE_LOADER.loadProjectilesFromFolder(new File("src/main/resources/org/nemesis/game/projectile"));
        DEFAULT_RESOURCE_LOADER.loadUnitsFromFolder(new File("src/main/resources/org/nemesis/game/unit"));
    }

    public void loadProjectileFromFile(@Nonnull File file) {
        if (file.isFile()) {
            ProjectileFactory factory = new ProjectileFactory(file);
            getProjectileFactories().put(file.getName(), factory);
        } else
            throw new IllegalArgumentException();
    }

    public void loadProjectilesFromFolder(@Nonnull File folder) {
        if (folder.isDirectory())
            for (File file : folder.listFiles()) {
                if (file.isDirectory())
                    loadProjectilesFromFolder(file);
                else if (file.isFile())
                    loadProjectileFromFile(file);
            }
        else
            throw new IllegalArgumentException();
    }

    public void loadUnitFromFile(@Nonnull File file) {
        if (file.isFile()) {
            UnitFactory factory = new UnitFactory(file);
            getUnitFactories().put(file.getName(), factory);
        } else
            throw new IllegalArgumentException();
    }

    void loadUnitsFromFolder(@Nonnull File folder) {
        if (folder.isDirectory())
            for (File file : folder.listFiles()) {
                if (file.isDirectory())
                    loadUnitsFromFolder(file);
                else if (file.isFile())
                    loadUnitFromFile(file);
            }
        else
            throw new IllegalArgumentException();
    }

    public Map<String, ProjectileFactory> getProjectileFactories() {
        return projectileFactories;
    }

    public Map<String, UnitFactory> getUnitFactories() {
        return unitFactories;
    }
}
