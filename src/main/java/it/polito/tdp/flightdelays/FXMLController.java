package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Arco;
import it.polito.tdp.flightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private ComboBox<Airline> cmbBoxLineaAerea;

    @FXML
    private Button caricaVoliBtn;

    @FXML
    private TextField numeroPasseggeriTxtInput;

    @FXML
    private TextField numeroVoliTxtInput;

    @FXML
    void doCaricaVoli(ActionEvent event) {
    	this.txtResult.clear();

    	Airline compagnia = this.cmbBoxLineaAerea.getValue();
    	
    	model.creaGrafo(compagnia);
    	
    	this.txtResult.appendText(String.format("Grafo creato!\n#vertici: %d\n#archi: %d\n", model.nVertici(), model.nArchi()));
    	this.txtResult.appendText("\n\nLe 10 peggiori rotte:\n");
    	for(Arco a : model.rottePeggiori()) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    	
    	
    }

    @FXML
    void doSimula(ActionEvent event) {

    	this.txtResult.clear();
    	
    	try {
    		Integer nPasseggeri = Integer.valueOf(this.numeroPasseggeriTxtInput.getText());
    		Integer nVoli = Integer.valueOf(this.numeroVoliTxtInput.getText());
    		
    		Map<Integer, Double> ritardi = model.simulazione(nPasseggeri, nVoli);
    		this.txtResult.appendText("Ritardo per ogni passeggero: \n");
    		for(Integer i : ritardi.keySet()){
    			this.txtResult.appendText((i+1)+" "+ritardi.get(i)+"\n");
    		}
    		
    	}catch(NumberFormatException nfe) {
    		this.txtResult.appendText("Inserisci valori corretti");
    	}
    	
    	
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxLineaAerea != null : "fx:id=\"cmbBoxLineaAerea\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert caricaVoliBtn != null : "fx:id=\"caricaVoliBtn\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroPasseggeriTxtInput != null : "fx:id=\"numeroPasseggeriTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.cmbBoxLineaAerea.getItems().addAll(model.tendina());
		this.cmbBoxLineaAerea.setValue(model.tendina().get(0));
	}
}
