package yegbe.izere.tp6;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class ControleurTP6 {

	private MotsCroisesTP6 motcroises;

	private ChargerGrille chargeG;

	@FXML
	private GridPane monGridPane;

	@FXML
	private MenuBar menuBar;

	@FXML
	public void processExit(ActionEvent event) {
	    Platform.exit();
	    System.exit(0);
	}
	
	@FXML
	private void initialize() {

		// Chargement d'une grille
		chargeG = new ChargerGrille();
		motcroises = chargeG.extraireGrille();
		recreerGrille();
		init();
		

		// controlleur associer sous forme d'une nouvelle classe java
	/**     menuBar = new MenuBar();
		  Menu menu = menuBar.getMenus().get(0); for (MenuItem menuItem :
		  menu.getItems()) { menuItem.setOnAction((e) -> {this.clicMenu(e);});; }
		**/ 
	}

	// 1.2 Creation d'une IHM JavaFX minimale
	private void init() {

		for (Node n : monGridPane.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;
				int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
				int col = ((int) n.getProperties().get("gridpane-column")) + 1;

				// Saisie des propositions
				tf.textProperty().bindBidirectional(motcroises.propositionProperty(lig, col));
				// Fin 1.3

				// Affichage des infobulles
				String texte = getInfobulles(lig, col);
				if (texte != null)
					tf.setTooltip(new Tooltip(texte));

				// Révélation d'une solution sur demande
				tf.setOnMouseClicked((e) -> {
					this.clicCase(e);
				});

				tf.textProperty().addListener((arg0, ancienneValeur, nouvelleValeur) -> {
					// on récupère que le dernier caractère
					
					int len = nouvelleValeur.length();
					if (len > 1) {
						nouvelleValeur = "" + nouvelleValeur.charAt(len - 1);
					}
					tf.setText(nouvelleValeur.toUpperCase());
				});

				/**
				 * l'approche <<programmee> appelant votre methode,et a affecter celle-ci a
				 * chacun des <<enfants>> du GridPane, toujours depuis la sequence
				 * initialisation du TextField
				 */

			}
		}
	}

	// 1.4 Affichage des infobulles
	private String getInfobulles(int lig, int col) {
		String texte = null;

		if (!motcroises.estCaseNoire(lig, col)) {
			String horizontal = motcroises.getDefinition(lig, col, true);
			String vertical = motcroises.getDefinition(lig, col, false);

			if (horizontal != null && vertical != null)
				texte = horizontal + "/" + vertical;

			if (horizontal != null && vertical == null)
				texte = horizontal.toString();

			if (horizontal == null && vertical != null)
				texte = vertical.toString();
		}

		return texte;
	}

	@FXML
	public void clicCase(MouseEvent e) {
		// si le bouton gauche est selectionné et qu'on est au 2e click (DOUBLE CLIK)
		if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
			TextField casse = (TextField) e.getSource();
			int lig = ((int) casse.getProperties().get("gridpane-row")) + 1;
			int col = ((int) casse.getProperties().get("gridpane-column")) + 1;

			casse.textProperty().bindBidirectional(motcroises.propositionProperty(lig, col));
			motcroises.montrerSolution(lig, col);
		}
	}

	// 2. Chargement d'une grille
	private void recreerGrille() {
		/**
		 * sauvegarder dans une variable locale le premier TextField donne par
		 * l'expression
		 * 
		 */
		TextField modele = (TextField) monGridPane.getChildren().get(0);
		/**
		 * caster monGridPane.getChildren().get(0) dans un type TextField et le
		 * sauvegarder dans une variable de type TextField
		 */
		monGridPane.getChildren().clear();
		// supprimer tous les textfield contenus dans le GridPane

		for (int lig = 0; lig < motcroises.getHauteur(); lig++) {
			for (int col = 0; col < motcroises.getLargeur(); col++) {
				if (!motcroises.estCaseNoire(lig + 1, col + 1)) {
					// creer une nouvelle instance de TextField
					TextField nouveau = new TextField();
					/**
					 * affecter au nouveau TextField les dimensions du TextField sauvegarde , par
					 * des appels de setPrefHeight(modele.getPrefWidth()) puis de
					 * setPrefHeight(modele.getPrefHeight())
					 */
					nouveau.setPrefWidth(modele.getPrefWidth());
					nouveau.setPrefHeight(modele.getPrefHeight());
					/**
					 * affectation au nouveau TextField, toutes les autres proprietes du TextField
					 * sauvegarde
					 */
					for (Object cle : modele.getProperties().keySet()) {
						nouveau.getProperties().put(cle, modele.getProperties().get(cle));
					}
					/**
					 * ajout du nouveau TextField au GridPane
					 */
					monGridPane.add(nouveau, col, lig);
				}
			}
		}
	}

	// 3 IHM pour grille renouvelee aleatoirement
	@FXML
	public void clicMenu(ActionEvent e) {
		MenuItem m = (MenuItem) e.getSource();
		int index = Integer.parseInt(m.getId());
		if (index < 11) {
			if (index == 0) {
				double random = (Math.random() * 100) % 10;
				index = (int) random;
				if (index == 0)
					index = 10;
			}
			motcroises = chargeG.extraireGrille(index);
			recreerGrille();
			init();
		} else {
			Platform.exit();
		}
	}
}
