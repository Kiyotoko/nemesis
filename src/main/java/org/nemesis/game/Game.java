package org.nemesis.game;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<String, Player> players = new HashMap<>();
    private final Map<String, Region> regions = new HashMap<>();
    private final Map<String, Building> buildings = new HashMap<>();
    private final Map<String, Fleet> fleets = new HashMap<>();

    private final Clock clock = new Clock(new Runnable() {
        @Override
        public void run() {
            for (Region region : regions.values()) {
                region.update(1);
            }
        }
    }, 50l);

    public Game() {
        Region sun = new Region();
        regions.put(sun.getId(), sun);

        for (double radius = 50 + Math.random() * 50; radius < 786 / 2; radius += 50 + Math.random() * radius) {
            double angel = Math.toRadians(Math.random() * 360);
            Region planet = new Region(sun, Math.cos(angel) * radius, Math.sin(angel) * radius);
            regions.put(planet.getId(), planet);
        }
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public Map<String, Building> getBuildings() {
        return buildings;
    }

    public Map<String, Fleet> getFleets() {
        return fleets;
    }

    public Clock getClock() {
        return clock;
    }
}
