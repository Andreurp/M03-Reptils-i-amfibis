package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;

public class SampleController implements Initializable {
	@FXML
	private ImageView imgAnimal;
	@FXML
	private ComboBox<String> cbFamilia;
	@FXML
	private ComboBox<String> cbOrdre;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtEspecia;
	@FXML
	private ComboBox<String> cbEstat;
	@FXML
	private TextArea txaDescripcio;
	@FXML
	private Button btnDesa;
	@FXML
	private Button btnAnterior;
	@FXML
	private Button btnSeguent;

	private Connection con = null;
	private ArrayList<Animal> llistaAnimals = new ArrayList<Animal>();
	private Statement consulta;
	private int posicioAnimal = 0;

	public void initialize(URL arg0, ResourceBundle arg1) {
		// obrir base de dades
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reptils", "foot", "ball");

			consulta = con.createStatement();
			// select a la taula familia
			ResultSet resultat = consulta.executeQuery("SELECT * FROM families");
			// omplir el cbFamilia amb els valors de la bdd
			cbFamilia.setValue("Tria una familia");
			while (resultat.next()) {
				cbFamilia.getItems().addAll(resultat.getString("nom"));
			}
			cbEstat.getItems().addAll("Extinta", "Extinta en estat salvatge", "En perill greu", "En perill",
					"Vulnerable", "Depèn de la conservació", "Gairebé amenaçada", "Risc mínim", "No avaluada");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Event Listener on ComboBox[#cbFamilia].onAction
	@FXML
	public void carregaOrdres(ActionEvent event) {
		cbOrdre.getItems().clear();
		String familia = cbFamilia.getValue().toString();
		// seleccionar els ordres que pertanyen a la familia
		try {
			ResultSet resultat = consulta.executeQuery(
					"SELECT nom FROM ordres WHERE familia = (SELECT codi FROM families WHERE nom = '" + familia + "')");
			// posa-los al cbOrdres
			while (resultat.next()) {
				cbOrdre.getItems().addAll(resultat.getString("nom"));
			}
			cbOrdre.setValue("Tria un ordre de la familia");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Event Listener on ComboBox[#cbOrdre].onAction
	@FXML
	public void carregaAnimals(ActionEvent event) {
		txtNom.setText(" ");
		txtEspecia.setText(" ");
		cbEstat.setValue(" ");
		txaDescripcio.setText(" ");
		imgAnimal.setImage(null);

		if (cbOrdre.getItems().size() > 0 && !cbOrdre.getValue().equals("Tria un ordre de la familia")) {
			String ordre = cbOrdre.getValue().toString();
			// Fer un select de tots els animals de l'ordre seleccionat
			try {
				ResultSet resultat = consulta.executeQuery(
						"SELECT * FROM animals WHERE ordre = (SELECT codi from ordres WHERE nom = '" + ordre + "')");
				llistaAnimals.clear();
				while (resultat.next()) {
					// posar-los a la llistaAnimals
					Animal animal = new Animal(resultat.getInt("codi"), resultat.getInt("ordre"),
							resultat.getString("nom"), resultat.getString("especie"), resultat.getString("estat"),
							resultat.getString("descripcio"), resultat.getString("imatge"));
					llistaAnimals.add(animal);
				}

				ompleAnimal();
				// btnDesa.setDisable(true);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void ompleAnimal() {
		if (posicioAnimal >= llistaAnimals.size()) {
			posicioAnimal = 0;
		} else if (posicioAnimal < 0) {
			posicioAnimal = llistaAnimals.size() - 1;
		}

		// mostrar el primer (omplir tots els camps)
		txtNom.setText(llistaAnimals.get(posicioAnimal).getNom());
		txtEspecia.setText(llistaAnimals.get(posicioAnimal).getEspecie());
		txaDescripcio.setText(llistaAnimals.get(posicioAnimal).getDescripcio());

		String url = llistaAnimals.get(posicioAnimal).getImatge();
		String uri1 = url.substring(0, 8);
		String uri2 = url.substring(10);
		String urlfinal = uri1 + uri2;
		Image img = new Image(urlfinal);
		imgAnimal.setImage(img);

		cbEstat.setValue(llistaAnimals.get(posicioAnimal).getEstat());
	}

	// Event Listener on ComboBox[#cbEstat].onAction
	@FXML
	public void habilitaDesa(ActionEvent event) {

		// System.out.println("hola");
		btnDesa.setDisable(false);
	}

	// Event Listener on TextField[#txtEspecia].onKeyPressed
	@FXML
	public void habilitaDesatxt(KeyEvent event) {
		btnDesa.setDisable(false);
	}

	// Event Listener on Button[#btnDesa].onMouseClicked
	@FXML
	public void desaAnimal(MouseEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on Button[#btnAnterior].onMouseClicked
	@FXML
	public void animalAnterior(MouseEvent event) {
		posicioAnimal--;
		ompleAnimal();
	}

	// Event Listener on Button[#btnSeguent].onMouseClicked
	@FXML
	public void animalSeguent(MouseEvent event) {
		posicioAnimal++;
		ompleAnimal();
	}
}
