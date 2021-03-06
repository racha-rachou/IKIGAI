package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import classes.InfoApprises;
import classes.PointsPositifs;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class InfoApprisesController implements Initializable{

    @FXML
    private AnchorPane container;

    @FXML
    private Button backBtn;

    @FXML
    private Button validerBtn;

    
    @FXML
    private Button confirm;

    @FXML
    private TableView<InfoApprises> InfoTable;

    @FXML
    private TextField InfoAppTextField;

    

    @FXML
    private TableColumn<InfoApprises, String> InfoAppT;
    
    public ObservableList<InfoApprises>  infos = FXCollections.observableArrayList();
    

    @FXML
    void Back(ActionEvent event) throws IOException {
    	
    	Parent root =FXMLLoader.load(getClass().getResource("Home.fxml"));

        
	  	   Scene scene = backBtn.getScene();
	         root.translateYProperty().set(-scene.getHeight());

	         AnchorPane parentcontainer = (AnchorPane) scene.getRoot();

	         parentcontainer.getChildren().add(root);

	         Timeline timeline = new Timeline();
	         KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
	         KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
	         timeline.getKeyFrames().add(kf);
	         timeline.setOnFinished(t -> {
	             parentcontainer.getChildren().remove(parentcontainer);
	         });
	         timeline.play();

    }
    
    
 

    /**
     * Initializes the controller class.
     */
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		//set up the columns in the table
    	InfoAppT.setCellValueFactory(new PropertyValueFactory<InfoApprises,String>("infoApprise"));
    /*==========================================================================================*/
        try {
      	  //Charger les information apprises de ce jour si ils existent
			this.AfficherInfoApprisesCeJour();
			InfoTable.setItems(infos);
	 
		
		}
        catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        
      /*==========================================================================================*/
/*
        //Update the table to allow for the first and last name fields
        //to be editable
       InfoTable.setEditable(true);
       InfoAppT.setCellFactory(TextFieldTableCell.forTableColumn());
     */
        
     
		
	}

	//Methode pour pouvoire ajouter dans l'observable list
    public void  Addinfos(InfoApprises i)
    { 	   infos.add(i);    
    }
    
    
    
    
    
    //======================================================================================
    //Recuperer le user en cours
    LoginController LC = new LoginController();
    int numSeq=0; 
    int idfDate;
	int  idInfoApp;
    /*================================Afficher dans la tanle Information Apprise=======================*/
    public void AfficherInfoApprisesCeJour() throws Exception {
  	try {	
  	
  	if(LC.Currentuser.isDataBaseConnected()) {
  	int Iduser =LC.Currentuser.getIdUser().get();
  	

  	int NbrJourU =LC.Currentuser.getNbJour(Iduser);
  
  	 
  	 //Numero sequentiel pour remplir le idfdate de la base de données
  	 numSeq =  InfoTable.getItems().size()+1;
 
  	 //Nombre de ligne de la table +1 
  	idfDate = Integer.parseInt(String.valueOf(Iduser)+ String.valueOf(NbrJourU));  
	
  	//Affecter l'id sa valeure conforme a la codification
  	idInfoApp= Integer.parseInt(String.valueOf(idfDate)+String.valueOf(numSeq));
	
  	InfoApprises  InfoApp= new InfoApprises();//Creer l'objet   
 
  
  	//Si il est connecté a la base de données
      if( InfoApp.isDataBaseConnected()) {   
//Pour pouvoir afficher au user ses informations apprise de cjour
      	while( InfoApp.ChercherInfoApprise(idInfoApp)!=null) {	
 
        	this.Addinfos(InfoApp.ChercherInfoApprise(idInfoApp));
        	
        	numSeq++;
      	idInfoApp = Integer.parseInt(String.valueOf(idfDate)+String.valueOf(numSeq));
   	}
      	}}else{
      		System.out.println("user not connected");//CUrrent user is deb connected
      	}
  	}//try
  	catch (SQLException e)
      {
          System.out.println("Vous avez un probleme dans la classe InfoAppriseController/NouveauInfoAppriseBtn");
         
      }  
 	catch (Exception e)
    {
        System.out.println("Vous avez un probleme dans la classe InfoAppriseController/NouveauInfoAppriseBtn"+e.getMessage());
       
    }  
	
  	
    
    }

	InfoApprises InfoAppr = new InfoApprises();//C

    /*================================Nouvelle Information Apprise=======================*/
    public void NouveauInfoAppBtn(ActionEvent event) throws Exception {
  	try {
  		
  	if(LC.Currentuser.isDataBaseConnected()) {
  	int Iduser =LC.Currentuser.getIdUser().get();
  	int NbrJourU =LC.Currentuser.getNbJour(Iduser);

	InfoApprises InfoAppr = new InfoApprises();
  	//Creer l'objet   
     
  	if(InfoAppr.isDataBaseConnected()) {
      	//Si l'insertion est vraie
      	if(InfoAppr.Insertion(idInfoApp, InfoAppTextField.getText())) {	
  	
      		InfoAppr.setidfInfoApprise(idInfoApp);
  		 InfoAppr.setInfoApprise(InfoAppTextField.getText());
  	//Ajouter a la table
  	   infos.add(InfoAppr);
  	 this.AfficherInfoApprisesCeJour();
  	 InfoTable.setItems(infos);
  	
  	   
  	    }   
      }//db connected
  	
  	//end else
      	
  	}
  		
  	//CUrrent user is deb connected
  	}//try
  	catch (SQLException e)
      {
          System.out.println("Vous avez un probleme dans la classe InfoApprisesController/NouveauInfoApprisesBtn");
         
      }
  	

    
    }


      	
    
    

	

  

}
