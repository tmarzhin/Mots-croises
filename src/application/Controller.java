package vue;

import application.ChargerGrille;
import application.Iteration;
import application.MotsCroisesTP6;
import go.TabControl;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;



public class Controller {

	private MotsCroisesTP6 m;
	private TabControl<TextField> gridSnap;
	private Direction currentDirection = Direction.HORIZONTAL;
	private TextField currentField = null;

	@FXML
	private GridPane monGridPane;

	@FXML
	public void initialize() {
		ChargerGrille load = new ChargerGrille();
		try {
			this.m = load.extraireGrille(ChargerGrille.CHOIX_GRILLE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setUp();
	}

	public void setUp() {
		TextField modele = (TextField) this.monGridPane.getChildren().get(0);
		this.monGridPane.getChildren().clear();

		for (int lig = 1; lig <= this.m.getHauteur(); ++lig) {
			for (int col = 1; col <= this.m.getLargeur(); ++col) {
				if (!this.m.estCaseNoire(lig, col)) {
					TextField tf = new TextField();
					tf.setAlignment(Pos.CENTER);
					tf.setPrefWidth(modele.getPrefWidth());
					tf.setPrefHeight(modele.getPrefHeight());

					for (Object cle : modele.getProperties().keySet()) {
						tf.getProperties().put(cle, modele.getProperties().get(cle));
					}

					this.monGridPane.add(tf, col - 1, lig - 1);
				}
			}
		}

		this.settings();
	}

	private void settings() {
		// Initialization
		this.gridSnap = new TabControl<>(this.m.getHauteur(), this.m.getLargeur());

		for (Node n : this.monGridPane.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;
				int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
				int col = ((int) n.getProperties().get("gridpane-column")) + 1;

				tf.textProperty().bindBidirectional(this.m.propositionProperty(lig, col));
				tf.textProperty().addListener((observable, oldValue, newValue) -> {
					if (!newValue.isEmpty()) {
						this.currentField.getStyleClass().remove("has-error");
						this.currentField.getStyleClass().remove("is-success");
						ScaleTransition transition = new ScaleTransition(Duration.millis(500), tf);
						// 0%
						transition.setFromX(0.0);
						transition.setFromY(0.0);
						// 100%
						transition.setToX(1.0);
						transition.setToY(1.0);
						transition.setAutoReverse(true);
						transition.play();
					}
				});

				tf.lengthProperty().addListener((observable, oldValue, newValue) -> {
					if (newValue.intValue() > oldValue.intValue() && tf.getText().length() >= 1)
						tf.setText(tf.getText().trim().substring(0, 1));

				});

				String defHoriz = this.m.getDefinition(lig, col, true);
				String defVert = this.m.getDefinition(lig, col, false);
				if (defHoriz != null && defVert != null)
					tf.setTooltip(new Tooltip(defHoriz + " / " + defVert));
				else if (defHoriz != null)
					tf.setTooltip(new Tooltip(defHoriz));
				else if (defVert != null)
					tf.setTooltip(new Tooltip(defVert));

				tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
					if (newValue) {
						currentField = tf;
						currentField.getStyleClass().add("is-current");
					}

					if (oldValue)
						tf.getStyleClass().remove("is-current");
				});
				tf.setOnMouseClicked(this::clicCase);
				this.gridSnap.add(lig, col, tf);
			}
		}

		TextField firstField = (TextField) this.monGridPane.getChildren().get(0);
		firstField.requestFocus();
	}

	private void moveTo(boolean next, TextField src) {
		int lig = ((int) src.getProperties().get("gridpane-row")) + 1;
		int col = ((int) src.getProperties().get("gridpane-column")) + 1;
		switch (this.currentDirection) {
		case HORIZONTAL:
			src = this.gridSnap.getNextNotNullValue(lig, next ? ++col : --col, true, next);
			break;

		case VERTICAL:
			src = this.gridSnap.getNextNotNullValue(next ? ++lig : --lig, col, false, next);
			break;
		}

		src.requestFocus();
	}

	@FXML
	private void clicCase(MouseEvent e) {
		if (e.getButton() == MouseButton.MIDDLE) {
			TextField cas = (TextField) e.getSource();
			int ligne = ((int) cas.getProperties().get("gridpane-row")) + 1;
			int colonne = ((int) cas.getProperties().get("gridpane-column")) + 1;
			cas.getStyleClass().remove("has-error");
			cas.getStyleClass().remove("is-success");
			this.m.reveler(ligne, colonne);
			moveTo(true, cas);
		}
	}

	public void setMotsCroises(MotsCroisesTP6 m) {
		this.m = m;
	}

	private enum Direction {
		HORIZONTAL, VERTICAL
	}
}
