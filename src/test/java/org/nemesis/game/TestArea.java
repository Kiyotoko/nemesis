package org.nemesis.game;

import com.google.gson.*;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.List;

class TestArea {

    @Test
    void properties() {
        Area.Properties properties = new Area.Properties().setBlocks(
                new int[][]{{1, 0, 0, 0}, {1, 1, 1, 0}, {2, 1, 1, 1}}
        ).setAlias(Map.of(0, "Block0", 1, "Block1", 2, "Block2")).setControlPoints(List.of(
                new ControlPoint.Properties().setPosition(new Point2D(50, 50)).setRange(25).setCapacity(3))
        ).setSpawnPoints(List.of(
                new SpawnPoint.Properties().setPosition(new Point2D(50, 10)).setNumber(1),
                new SpawnPoint.Properties().setPosition(new Point2D(50, 90)).setNumber(2))
        ).setColumns(4).setRows(3);

        for (int y = 0; y < properties.getRows(); y++) {
            for (int x = 0; x < properties.getColumns(); x++) {
                Assertions.assertTrue(properties.getAlias().containsKey(properties.getBlocks()[y][x]));
            }
        }

        Assertions.assertDoesNotThrow(() -> System.out.println(new GsonBuilder().registerTypeAdapter(Point2D.class,
                (JsonSerializer<Point2D>) (src, typeOfSrc, context) -> {
                    JsonObject object = new JsonObject();
                    object.add("x", context.serialize(src.getX()));
                    object.add("y", context.serialize(src.getX()));
                    return object;
                }).create().toJson(properties)));
    }
}
