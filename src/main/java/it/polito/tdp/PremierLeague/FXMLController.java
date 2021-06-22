/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Opponenti;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	double x;
    	try {
    		x=Double.parseDouble(this.txtGoals.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero minimo di goal");
    		return;
    	}
    	
    	this.model.creaGrafo(x);
    	
    	this.txtResult.setText("Grafo creato con: "+this.model.nVertici()+" vertici e "+this.model.nArchi()+" archi.");
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	txtResult.clear();
    	
    	int k;
    	try {
    		k=Integer.parseInt(this.txtK.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero intero di giocatori");
    		return;
    	}
    	
    	if(this.model.getGrafo()==null) {
    		this.txtResult.setText("Creare prima il grafo");
    		return;
    	}
    	
    	List<Player> team=this.model.dreamTeam(k);
    	this.txtResult.appendText("Dream team con grado di titolarità "+this.model.getGradoMax()+": \n");
    	for(Player p:team) {
    		this.txtResult.appendText(p.getName()+"\n");
    	}
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	txtResult.clear();
    	if(this.model.getGrafo()==null) {
    		this.txtResult.setText("Creare prima il grafo!");
    		return;
    	}
    	
    	this.txtResult.appendText("TOP PLAYER: "+model.giocatoreMigliore()+"\n\n"+"AVVERSARI BATTUTI: "+"\n");
    	
    	for(Opponenti o:this.model.avversariBattuti()) {
    		this.txtResult.appendText(o.toString()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
