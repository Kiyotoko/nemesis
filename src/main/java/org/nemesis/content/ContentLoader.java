package org.nemesis.content;

import org.dosl.DoslListing;
import org.dosl.DoslUtilsKt;
import org.nemesis.Launcher;
import org.nemesis.game.*;

import javax.annotation.CheckForNull;
import java.util.HashMap;
import java.util.Map;

public class ContentLoader {

    public ContentLoader() {
        try {
            DoslListing listing = DoslUtilsKt.parseDoslFile(Launcher.class, "resources.dosl");

            for (String path : listing.getLabelsToPaths().get("projectile")) {
                Projectile.Properties properties = FileUtils.getJson(path, Projectile.Properties.class);
                projectileFactoryMap.put(properties.id, Factories.createFactory(properties));
            }

            for (String path: listing.getLabelsToPaths().get("weapon")) {
                Weapon.Properties properties = FileUtils.getJson(path, Weapon.Properties.class);
                properties.withContentLoader(this);
                hardPointFactoryMap.put(properties.id, Factories.createFactory(properties));
            }
            for (String path: listing.getLabelsToPaths().get("engine")) {
                Engine.Properties properties = FileUtils.getJson(path, Engine.Properties.class);
                hardPointFactoryMap.put(properties.id, Factories.createFactory(properties));
            }

            for (String path : listing.getLabelsToPaths().get("unit")) {
                Unit.Properties properties = FileUtils.getJson(path, Unit.Properties.class);
                properties.withContentLoader(this);
                unitFactoryMap.put(properties.id, Factories.createFactory(properties));
            }
        } catch (Exception ex) {
            throw new InternalError(ex);
        }
    }

    private final Map<String, Factory<Weapon, Projectile>> projectileFactoryMap = new HashMap<>();

    @CheckForNull
    public Factory<Weapon, Projectile> getProjectileFactory(String id) {
        return projectileFactoryMap.get(id);
    }

    private final Map<String, Factory<Unit, ? extends HardPoint>> hardPointFactoryMap = new HashMap<>();

    public Map<String, Factory<Unit, ? extends HardPoint>> getHardPointFactoryMap() {
        return hardPointFactoryMap;
    }

    private final Map<String, Factory<Player, Unit>> unitFactoryMap = new HashMap<>();

    @CheckForNull
    public Factory<Player, Unit> getUnitFactory(String id) {
        return unitFactoryMap.get(id);
    }
}
