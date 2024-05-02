package org.nemesis.content;

import org.dosl.DoslListing;
import org.dosl.DoslUtilsKt;
import org.nemesis.Launcher;
import org.nemesis.game.Projectile;
import org.nemesis.game.Unit;

import javax.annotation.CheckForNull;
import java.util.HashMap;
import java.util.Map;

public class ContentLoader {

    public ContentLoader() {
        try {
            DoslListing listing = DoslUtilsKt.parseDoslFile(Launcher.class, "resources.dosl");

            for (String path : listing.getLabelsToPaths().get("projectile")) {
                Projectile.Properties properties = FileUtils.getJson(path, Projectile.Properties.class);
                projectileFactoryMap.put(properties.id, new ProjectileFactory(properties));
            }

            for (String path : listing.getLabelsToPaths().get("unit")) {
                Unit.Properties properties = FileUtils.getJson(path, Unit.Properties.class);
                unitFactoryMap.put(properties.id, new UnitFactory(properties));
            }
        } catch (Exception ex) {
            throw new InternalError(ex);
        }
    }

    private final Map<String, UnitFactory> unitFactoryMap = new HashMap<>();

    @CheckForNull
    public UnitFactory getUnitFactory(String id) {
        return unitFactoryMap.get(id);
    }

    private final Map<String, ProjectileFactory> projectileFactoryMap = new HashMap<>();

    @CheckForNull
    public ProjectileFactory getProjectileFactory(String id) {
        return projectileFactoryMap.get(id);
    }
}
