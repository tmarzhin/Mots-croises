package vue;

import application.MotsCroisesTP6;
import application.ChargerGrille;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.Map;


public class ControllerRandom {
    private ChargerGrille grilleLoader;
    private Controller grilleController;

    @FXML
    public MenuItem exit;

    @FXML
    private Menu list;

    @FXML
    private MenuItem random;

    @FXML
    public void initialize() {
        this.grilleLoader = new ChargerGrille();
        Map<Integer, String> jeux = this.grilleLoader.grilleDisponibles();

        for (Integer cle : jeux.keySet()) {
            String nomJeu = jeux.get(cle);
            MenuItem item = new MenuItem(nomJeu);

            item.setOnAction(event -> this.genererGrille(cle));

            this.list.getItems().add(item);
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void random() throws Exception {
        int numGrille = 1 + (int) (Math.random() * 11);
        this.genererGrille(numGrille);
    }

    private void genererGrille(int numGrille) {
        try {
            grilleController.setMotsCroises(this.grilleLoader.extraireGrille(numGrille));
            grilleController.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setGrilleController(Controller grilleController) {
        this.grilleController = grilleController;
    }
}
