package Views;


import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Tableau_follower extends Application {
    /*
     * Classe qui gère la fenetre de la liste des followers
     */
    // Créer une nouvelle fenêtre
    private Stage nouvelle_fenetre = new Stage();
    // Créer une TableView
    private TableView<Personne> tableView = new TableView<>();

    // Créer une colonne pour le nom de l'ami
    private TableColumn<Personne, String> nomColonne = new TableColumn<>("Amis");

    // Créer une colonne pour le nom de l'utilisateur bloqué
    private ArrayList<String> liste_followers = new ArrayList<>();

    private ObservableList<Personne> data_personne = FXCollections.observableArrayList(); // liste des personnes à afficher et actualisable
    
    private Button fermerBouton = new Button("Fermer");

    // Créer une VBox pour contenir la TableView et le bouton
    private VBox vbox = new VBox(tableView, fermerBouton);

    // Créer une nouvelle scène
    private Scene scene = new Scene(vbox, 200, 200);
    private int id_mur_admin;

    private Requete moteur_de_Requete = new Requete();
    public Tableau_follower(ArrayList<String> liste_personne, int id_mur) {
        liste_followers = liste_personne;   
        id_mur_admin = id_mur;  
    } 

    public void start(Stage primaryStage) { // méthode pour lancer une appli/fenetre
        
        // Parcourir la liste des amis
        for (String ami : this.liste_followers) {
            // Créer un nouvel objet Ami avec le nom de l'ami
            Personne individus = new Personne(ami);
            // Ajouter l'ami à la liste
            data_personne.add(individus);
        }

        // ajout des colonnes
        nomColonne.setCellValueFactory(new PropertyValueFactory<>("nom"));
        
        // Ajouter les colonnes à la TableView
        tableView.getColumns().add(nomColonne);
        
        
        // Ajouter la liste à la TableView
        tableView.setItems(data_personne);
                

        // Créer un bouton pour fermer la fenêtre
        fermerBouton.setOnAction(e -> nouvelle_fenetre.close());

        // Appeler la méthode pour gérer le blocage de l'utilisateur
        this.gestionUtilisateurBloque();


        // Ajouter la scène à la nouvelle fenêtre
        nouvelle_fenetre.setScene(scene);

        // Configurer la nouvelle fenêtre
        nouvelle_fenetre.setTitle("Liste de vos amis");
        nouvelle_fenetre.setX(primaryStage.getX() + 200);
        nouvelle_fenetre.setY(primaryStage.getY() + 100);

        // Afficher la nouvelle fenêtre
        nouvelle_fenetre.show();
    }
    
 
    
    private void gestionUtilisateurBloque() {
        /*
         * Méthode qui crée et ajoute un élément de menu lors du clique droit sur un nom d'utilisateur et qui permet de bloquer l'utilisateru sélectionné
         * 
         */
        // Définition des éléments du menu contextuel (clique droit sur un nom)
        ContextMenu contextMenu = new ContextMenu();
        MenuItem bloquerItem = new MenuItem("Bloquer cet utilisateur ?");

        // Ajouter un écouteur d'événements pour l'élément de menu
            bloquerItem.setOnAction(e -> {
                Personne selectedPersonne = tableView.getSelectionModel().getSelectedItem();
                if (selectedPersonne != null) {
                    data_personne.remove(selectedPersonne); // enelève de la liste la personne bloquée
                    // Récupère l'ID de la personne bloquée en supposant que son prénom est unique sur la base, on pourrait ajouter le nom pour être plus sûr
                    String recup_id_personne_mur = new String("SELECT idU FROM USERS INNER JOIN WALLS ON idU = \"#idU\" WHERE prenom = '" + selectedPersonne.getNom() + "';");
                    ArrayList<String> id_personne = moteur_de_Requete.parcoursTableSQL(recup_id_personne_mur, "idU");
                    // Mise à jour de la base de données
                    String requet_maj_statut = new String("UPDATE FOLLOWERS SET blocked = 1 WHERE \"#idU\" = " + id_personne.get(0) + " AND \"#idW\" = " + this.id_mur_admin + ";");
                    moteur_de_Requete.insertion_sql(requet_maj_statut);
                }
            });
  
        // Ajouter l'élément de menu au menu contextuel
        contextMenu.getItems().add(bloquerItem);

        // Ajouter le menu contextuel à la TableView
        tableView.setContextMenu(contextMenu);
    }


    


}