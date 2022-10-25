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
