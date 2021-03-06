package classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import DbUtil.DbConnection;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;



public class User {
	private IntegerProperty idUser;
	private StringProperty FullName;
	private StringProperty Password;
	private StringProperty Email;
	private StringProperty  SessionSkill;
	
	public User(StringProperty fullName, StringProperty password,StringProperty email, StringProperty sessionSkill) {
		FullName = fullName;
		Password = password;
		Email = email;
		SessionSkill = sessionSkill;
	}
	
	
	public User() {
		   super();
	        idUser=new SimpleIntegerProperty();
	        FullName= new SimpleStringProperty();
	        Password = new SimpleStringProperty();
	        Email = new SimpleStringProperty();
	        SessionSkill = new SimpleStringProperty();
	       
	}


	public String getFullName() {
		return FullName.get();
	}
	public void setFullName(String fullName) {
		FullName.set(fullName); 
	}
	public String getPassword() {
		return Password.get();
	}
	public void setPassword(String password) {
		this.Password.set(password);
	}
	public String getEmail() {
		return Email.get();
	}
	public IntegerProperty getIdUser() {
		return idUser;
	}


	public void setIdUser(int idUser) {
		this.idUser.set(idUser); 
	}


	public void setEmail(String email) {
		Email.set(email);
	}
	public String getSessionSkill() {
		return SessionSkill.get();
	}
	public void setSessionSkill(String sessionSkill) {
		SessionSkill.set(sessionSkill);
	}
	
	 //****=============FONCTION QUI PERMET DE NOUS CONNECTER A LA BDD===========
    Connection connection;



    public boolean isDataBaseConnected() {
        try {

            this.connection= DbConnection.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection != null;
    }
    
    //****=============FONCTION QUI PERMET DE MODIFIER UN USER DANS LA BDD========================
    public boolean ModifierUSR(String fullName, String Oldmail,String Email, String mdp, String skill) {

        String query ="UPDATE USER SET fullName = ?, email = ? , mdp = ?,skill  =? WHERE email = ? ";

        try (PreparedStatement pr = this.connection.prepareStatement(query)) {
            pr.setString(1,  fullName);
            //Nouveau email
            pr.setString(2, Email);
            pr.setString(3, mdp);
            pr.setString(4, skill);
            pr.setString(5, Oldmail);
            
            
       
            
        pr.executeUpdate();
        return true;
     
       
        } catch (SQLException e) {
            System.out.println("Vous avez un probleme dans la classe User Modifier User");
        return false;
        }

    }
    
    
    //===============++Methode Pour Avoir le Nombre de jour  Du User=================

    public  int getNbJour(int idUser) throws Exception{
        PreparedStatement pr=null;
        ResultSet rs=null;
        String sql="SELECT COUNT(*) as nbrJour  FROM DateCoins where  idfU= ?";
        int nbJour =0;

        try{
            pr=this.connection.prepareStatement(sql);
            pr.setInt(1,  idUser);
       
            rs = pr.executeQuery();
          
         if(rs.next())
            {
        	 nbJour= rs.getInt("nbrJour")-1;
        	 
            }
            return (nbJour);//reccuperer le nombre de coins
        }catch (SQLException e)
        {
            System.out.println("Vous avez un probleme dans la classe User GetNbJour");
            return 0;
        }
        finally {

            assert pr != null;
            pr.close();

            assert rs != null;
            rs.close();
            return (nbJour);
        }
    }
    
   
    //FONCTION QUI PERMET D'OBTENIR LE NB COINS USER
    public  int getNbCoins(int idUser,int jour) throws Exception{
        PreparedStatement pr=null;
        ResultSet rs=null;
        String sql="select nbrCoinsJour from DateCoins where  idfU  = ? and nbrJour  =? ";
        int nbcoins =0;

        try{
            pr=this.connection.prepareStatement(sql);
            pr.setInt(1,  idUser);
            pr.setInt(2,  jour);
            rs = pr.executeQuery();
          
         if(rs.next())
            {
         nbcoins= rs.getInt("nbrCoinsJour");
        	 
            }
            return (nbcoins);//reccuperer le nombre de coins
        }catch (SQLException e)
        {
            System.out.println("Vous avez un probleme dans la classe User getNbCoins");
            return 0;
        }
        finally {

            assert pr != null;
            pr.close();

            assert rs != null;
            rs.close();
        }
    }

  //=======================================AJOUTER CONNEXION USER============================*/
  		public void AjouterConnexion(int idfU,int nbJour) {
  			DbConnection db = new DbConnection();
  			String query="INSERT INTO Connexion (dateSys,nbrJour,idfU) VALUES (?, ?   ,? )";
  			   try (PreparedStatement pr = this.connection.prepareStatement(query)) {
  		           
  		          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd ");  
				   LocalDateTime now = LocalDateTime.now();  
				   String dateSys= dtf.format(now); 	
  		          pr.setString(1, dateSys);
  		    pr.setInt(2, nbJour);
  		        pr.setInt(3, idfU);
  		
  		            pr.executeUpdate();
  		        } catch (SQLException e) {
  		            System.out.println("Vous avez un probleme dans la classe User/  AjouterConnexion");
  		        }
  		}

  	    //****=============FONCTION QUI PERMET DE MANIPULER LES STATS DU USER========================

	public boolean    ProgresUser(XYChart.Series series, BarChart<String,Number> Barchart,int idfU) throws Exception {
	
	        PreparedStatement pst=null;
			
			ResultSet rst=null;
			
		
			String query="SELECT  nbrCoinsJour FROM  DateCoins  where idfU=? and nbrJour =?";
			
				try {
					
					pst = connection.prepareStatement(query);
					pst.setInt(1, idfU);
					
					
					
				int	cpt=this.getNbJour(idfU); //pour avoir le nombre de jour du user
			int cpt2 =1;
					pst.setInt(2, cpt2);
					rst = pst.executeQuery(); 
					
				String cptString;
					//if it is returning any result or not 
						if(rst!=null)
						{
							
							
							
							if(rst.next()) {			
								
				//Pour avoir le nombre de jour du User, a mettre sur la barre des X
						for(int i=0;i<cpt;i++) {
			//Transormer le nombre  de jour en chaine de caractere pour l'utiliser dans le graphe
							cptString=String.valueOf(cpt2);
							
							
								series.getData().add(new XYChart.Data(cptString,rst.getInt("nbrCoinsJour")));
								
								cpt2=cpt2+1;
								pst.setInt(2, cpt2);
								rst = pst.executeQuery();
								
						}
						
						
						
						
						
							}
							//ajouter au chart
							Barchart.getData().addAll(series);
						
							return true;
						
						
						}
						else
						{
							System.out.println("Il y'a une erreur dans User/ProgresUser");
							return false;
						}
					
						
						
			} 
				
				catch (SQLException e)
				{
					
					System.out.println("Erreur"+e);
					System.err.println(e.getClass()+e.getMessage());
					e.printStackTrace();
					return false;
					// TODO: handle exception
				}
				
				
				
				finally {
					pst.close();
				
				//	rst.close();
				}
	}
	  //=======================================CHERCHER SI LE USER SEST CONNECT2 DANS CE JOUR ============================*/
  //Pour voir si on incremente nbjour ou pas
	
	public boolean chercherConnexionUser(int idU) throws  Exception{
        PreparedStatement pr=null;
        ResultSet rs=null;

        String sql="select * from Connexion where dateSys= ? and idfU=?";//On utilise le numero de telephone du patient car c'est la seul donnée de la table qui est obligatoirement unique
       
        
        
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd ");  
		   LocalDateTime now = LocalDateTime.now();  
		   String dateSys= dtf.format(now);
        try{
            pr=this.connection.prepareStatement(sql);
            pr.setString(1,dateSys);
            pr.setInt(2,idU);

            rs = pr.executeQuery();


            if (rs.next()){
//si il existe on retourne true
            return true;
            }
            else{

                return false;// sinon on retourne false
            }
        }catch (SQLException e)
        {

            System.out.println("Vous avez un probleme dans la classe User /chercherConnexionUser");
            return false;
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
        	return false;
		
		}
        finally {
            assert pr != null;
            pr.close();
            assert rs!= null;
            rs.close();
        }

    }
	
	
	  //=======================================METTRE COINS A 60 CHAQUE NOUVEAU JOUR ============================*/

	public void AjouterCoinsUSerInit(int idfU,int nbjour) {
			DbConnection db = new DbConnection();
			String query="INSERT INTO DateCoins (idfDate ,nbrCoinsJour,idfU,nbrJour ) VALUES (?, ?   ,?,? )";
			   try (PreparedStatement pr = this.connection.prepareStatement(query)) {
		        
			    int	idfDate = Integer.parseInt(String.valueOf(idfU)+ String.valueOf( nbjour));  

			  pr.setInt(1, idfDate );
				   //valeure intial des coins
			 pr.setInt(2, 60);  
		    pr.setInt(3,idfU);
		  
		        pr.setInt(4, nbjour);
		
		            pr.executeUpdate();
		        } catch (SQLException e) {
		            System.out.println("Vous avez un probleme dans la classe User/ AJouterCoinsInit");
		        }
		}

	  //=======================================AJOUTER DANS FIN SESSION ============================*/
		public void AjouterFinSession(int idfU) {
			DbConnection db = new DbConnection();
			String query="INSERT INTO FinSession (idfU ,dateFin ) VALUES (?, ?   )";
			   try (PreparedStatement pr = this.connection.prepareStatement(query)) {
				   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd ");  
				   LocalDateTime now = LocalDateTime.now();  
				   String dateFin= dtf.format(now); 	
				  

			  pr.setInt(1, idfU );
				 
			 pr.setString(2, dateFin);  
		 
		            pr.executeUpdate();
		        } catch (SQLException e) {
		            System.out.println("Vous avez un probleme dans la classe User/   AjouterFinSession");
		        }
		}


}
