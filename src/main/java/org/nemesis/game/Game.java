package org.nemesis.game;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private final Map<String, Player> players = new HashMap<>();
    private final Map<String, Region> regions = new HashMap<>();

    private final Clock clock = new Clock(new Runnable() {
        @Override
        public void run() {
            for (Region region : regions.values()) {
                region.update(1);
            }
        }
    }, 50l);

    public Game() {
        Region sun = Region.at(this, 0, 0);
        Region.sub(this, sun);
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public Clock getClock() {
        return clock;
    }
}
