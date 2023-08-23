package org.nemesis.game;

import javafx.scene.Node;

import javax.annotation.Nonnull;

public interface Displayable {

	@Nonnull
	Node getGraphic();
}
