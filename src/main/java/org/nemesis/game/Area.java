package org.nemesis.game;

import javafx.scene.layout.Pane;
import org.nemesis.content.MetaLoader;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Supplier;

public class Area {

    private final @Nonnull Pane pane = new Pane();
    private final @Nonnull Block[][] blocks;
    private final @Nonnull Properties properties;
    private final double width;
    private final double height;

    public Area(Properties properties) {
        this.properties = properties;
        this.height = properties.getRows() * Block.BLOCK_EDGE_SIZE_PX;
        this.width = properties.getColumns() * Block.BLOCK_EDGE_SIZE_PX;

        MetaLoader<Block.Properties> metaLoader = new MetaLoader<>("block", Block.Properties.class);
        Map<Integer, Supplier<Block>> suppliers = new HashMap<>();
        for (Map.Entry<Integer, String> alias : properties.getAlias().entrySet()) {
            suppliers.put(alias.getKey(), new Supplier<>() {
                private final Block.Properties properties = metaLoader.getObject(alias.getValue());

                @Override
                public Block get() {
                    return new Block(properties);
                }
            });
        }

        blocks = new Block[properties.rows][properties.columns];
        for (int y = 0; y < properties.getRows(); y++) {
            for (int x = 0; x < properties.getColumns(); x++) {
                Block block = suppliers.get(properties.getBlocks()[y][x]).get();
                blocks[y][x] = block;
                getPane().getChildren().add(block.getView());
                block.getView().setLayoutX(x * Block.BLOCK_EDGE_SIZE_PX);
                block.getView().setLayoutY(y * Block.BLOCK_EDGE_SIZE_PX);
            }
        }
    }

    public static class Properties implements Serializable {

        private int[][] blocks;

        public Properties setBlocks(int[][] blocks) {
            this.blocks = blocks;
            return this;
        }

        public int[][] getBlocks() {
            return blocks;
        }

        private Map<Integer, String> alias;

        public Properties setAlias(Map<Integer, String> alias) {
            this.alias = alias;
            return this;
        }

        public Map<Integer, String> getAlias() {
            return alias;
        }

        private List<SpawnPoint.Properties> spawnPoints;

        public Properties setSpawnPoints(List<SpawnPoint.Properties> spawnPoints) {
            this.spawnPoints = spawnPoints;
            return this;
        }

        public List<SpawnPoint.Properties> getSpawnPoints() {
            return spawnPoints;
        }

        private List<ControlPoint.Properties> controlPoints;

        public Properties setControlPoints(List<ControlPoint.Properties> controlPoints) {
            this.controlPoints = controlPoints;
            return this;
        }

        public List<ControlPoint.Properties> getControlPoints() {
            return controlPoints;
        }

        private int rows;

        public Properties setRows(int rows) {
            this.rows = rows;
            return this;
        }

        public int getRows() {
            return rows;
        }

        private int columns;

        public Properties setColumns(int columns) {
            this.columns = columns;
            return this;
        }

        public int getColumns() {
            return columns;
        }
    }

    public boolean isInside(double posX, double posY) {
        return (posX >= 0 && posX <= getWidth() && posY >= 0 && posY <= getHeight());
    }

    @CheckForNull
    public Block getBlock(double posX, double posY) {
        if (!isInside(posX, posY)) return null;
        int x = (int) (posX / Block.BLOCK_EDGE_SIZE_PX);
        int y = (int) (posY / Block.BLOCK_EDGE_SIZE_PX);
        return blocks[y][x];
    }

    @Nonnull
    public Pane getPane() {
        return pane;
    }

    @Nonnull
    public Properties getProperties() {
        return properties;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
