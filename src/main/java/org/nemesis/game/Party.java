package org.nemesis.game;

import java.util.ArrayList;
import java.util.List;

import io.scvis.entity.Children;
import io.scvis.proto.Identifiable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Party implements Children, Identifiable, Displayable {
	private final List<Player> players = new ArrayList<>();

	private final Game game;

	private final Label label = new Label(getClass().getSimpleName());
	{
		label.setFont(Font.font("Fira Sans", FontWeight.BOLD, 14));
		label.setTextFill(Color.WHITE);
		label.setPadding(new Insets(5));
		label.setPrefWidth(100);
		label.setBorder(new Border(
				new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(50), new BorderWidths(3))));
	}

	public Party(Game game) {
		this.game = game;
		getParent().getChildren().add(this);
		getParent().getParties().add(this);
	}

	@Override
	public void update(double deltaT) {

	}

	@Override
	public void destroy() {
		game.getParties().remove(this);
	}

	public List<Player> getPlayers() {
		return players;
	}

	@Override
	public Game getParent() {
		return game;
	}

	@Override
	public Node getGraphic() {
		return label;
	}
}
