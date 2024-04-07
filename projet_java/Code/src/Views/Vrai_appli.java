package Views;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;
import javafx.stage.WindowEvent;

public class Vrai_appli extends Application {
    /*
    * Classe qui permet de lancer l'application et qui gère le passage entre les différentes scènes
    */
    
    // Importation et accès aux variables globales de la fenêtre
    private Global global = new Global(); 
    private String TITRE_FENETRE = global.get_TITRE_FENETRE();
    private double LARGEUR_FENETRE  = global.get_LARGEUR_FENETRE();
    private double LONGUEUR_FENETRE  = global.get_LONGUEUR_FENETRE();
    private Requete moteur_de_Requete = new Requete();
    private User user = new User();
    private viewPrincipal viewprincipal;
    private viewLogin viewconnexion;
    private Scene sceneconnexion;
    private Scene sceneprincipal;
    private viewSignin viewinscription ;
    private Scene sceneinscription;
    final AtomicReference<String> chemin_photo = new AtomicReference<>(); // copie du chemin de la photo de profil défini comme variable globale
    
    public void start(Stage primaryStage) throws IOException {
        /*
         * Méthode qui permet de lancer l'application, elle remplace le main de java classique 
         */
        viewprincipal = new viewPrincipal(primaryStage);
        viewconnexion = new viewLogin(5, primaryStage);
        sceneconnexion = new Scene(viewconnexion);
        sceneprincipal = new Scene(viewprincipal);
        
        viewinscription = new viewSignin(primaryStage);
        sceneinscription = new Scene(viewinscription);
        
        primaryStage.setScene(sceneconnexion); // charge la scene de base (connexion)
        primaryStage.setTitle(TITRE_FENETRE); // Définitions des détails de le fenetre avec des variables globales
        primaryStage.setWidth(LARGEUR_FENETRE);
        primaryStage.setHeight(LONGUEUR_FENETRE);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        
        // gère ce que fait le bouton inscription
        viewconnexion.btn_inscription.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setscene_visible(primaryStage, sceneinscription); // affiche la scène d'inscription
            }
        });
         // gère ce que fait le bouton connexion
        viewconnexion.btn_valider.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String identifiant = new String(viewconnexion.slot_id.getText()); // recupère les info rentrées
                String mdp = new String(viewconnexion.slot_mdp.getText());
                try {  // bloc try catch pour gérer les erreurs de connexion
                   gestionInfoConnexion(primaryStage, identifiant, mdp);
                }   
                catch (Exception error) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de connexion");
                    alert.setHeaderText(null);
                    alert.setContentText(error.getMessage());
                    alert.showAndWait();
                }
        }});

        // Gère le Bouton retour lorsque l'on est sur la page d'inscription
          viewinscription.bouton_retour_inscription.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setscene_visible(primaryStage, sceneconnexion); // recharge scène de départ
            }
        });

        viewinscription.btn_photo_profil.setOnAction(e -> {
                /*
                 * Fonction qui permet de choisir une photo de profil
                 */
                FileChooser choix_photo = new FileChooser();
                // Filtre les fichiers sélectionnables
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png)", "*.png");
                choix_photo.getExtensionFilters().add(extFilter);
                File photo_choisi = choix_photo.showOpenDialog(primaryStage); // affiche la boite de dialogue
                
                if(photo_choisi == null)
                    ; // ne fait rien si l'utilisateur n'a rien sélectionné
                
                else {

                    // Copie de l'image dans le dossier Image
                    
                    Path targetDirPath = Paths.get("projet_java/Code/ressources/Image"); // chemin relatif dans le projet java
                    File targetDir = targetDirPath.toFile();

                    if (!targetDir.exists()) // Crée le dossier s'il n'existe pas
                        targetDir.mkdir();

                    File targetFile = new File(targetDir, photo_choisi.getName());
                    try {
                        Files.copy(photo_choisi.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Récupération du chemin relatif de l'image copiée
                    String relativePath = targetFile.getPath();
                    chemin_photo.set(relativePath);
    }});
        
        // Gère le bouton de validation de l'inscription
        viewinscription.btn_valider_inscription.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nom = viewinscription.slot_nom.getText().trim(); // récupère les infos rentrées
                String prenom = viewinscription.slot_prenom.getText().trim();
                String identifiant = new String(creationID(nom, prenom)); // création de l'id
                String mdp = new String(viewinscription.slot_mdp.getText());
                String mdp2 = new String(viewinscription.slot_password2.getText());
                String description_mur = new String(viewinscription.slot_mur.getText());
                LocalDate date_anniversaire = viewinscription.date_anniversaire.getValue(); // convertit la date en string
                
                try { // inscription du nouvel utilisateur
                    User new_user = new User();
                    new_user.gestionInfoInscription(primaryStage, sceneconnexion, nom, prenom, identifiant, mdp, mdp2, date_anniversaire, description_mur, chemin_photo);
                    setscene_visible(primaryStage, sceneconnexion);
                } 
                catch (Exception error) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur d'inscription");
                    alert.setHeaderText(null);
                    alert.setContentText(error.getMessage());
                    alert.showAndWait();
                }  
            }
        });
        
        //  Gère le bouton de déconnexion
        viewprincipal.menu_deconnexion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setscene_visible(primaryStage, sceneconnexion);
                viewprincipal.vbox_post.getChildren().clear();
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // Insérez ici le code à exécuter lors de la fermeture de la fenêtre
                moteur_de_Requete.fermer(); // ferme la connexion à la base de données
            }
        });
        
    }

    
    private void gestionInfoConnexion(Stage primaryStage, String identifiant, String mdp) throws VerifMDP, VerifLogin{
        /*
        * Méthode qui permet de tester si les idnetifiants entrés correspondent à ceux de la base de données
        */
        // On teste si l'identifiant est dans la base de données
         if (moteur_de_Requete.parcoursTableSQL("SELECT login FROM USERS;", "login").contains(identifiant)) {
            // On regarde si le mdp correspond à l'identifiant
            if (moteur_de_Requete.parcoursTableSQL("SELECT mdp FROM USERS WHERE login = '" + identifiant + "'", "mdp").contains(mdp)) {
                setscene_visible(primaryStage, sceneprincipal); // Accès au mur 
                // Gestion du mur et des ses posts
                user = viewprincipal.creation_USER_admin(viewconnexion);
                viewprincipal.gestionImageProfil(user);
                viewprincipal.setUtilisateurAdmin(user);
                viewprincipal.info_post_mur(user);
            }
                
            else // une exception est levée si le mot de passe est incorrect
                throw new VerifMDP("Mot de passe incorrect");
        }
        else // idem pour l'identifiant
            throw new VerifLogin("Identifiant inconnu");
    } 
            
    private void setscene_visible(Stage unStage, Scene uneScene){
        /*
         * Méthode qui permet de changer la scène visible
         */
        unStage.setScene(uneScene);
        unStage.sizeToScene(); // force actualisation de la scène
        unStage.setWidth(LARGEUR_FENETRE); // la redimensionne
        unStage.setHeight(LONGUEUR_FENETRE);
        unStage.setFullScreen(true);
    }
    
    private String creationID(String nom, String prenom){
        /*
         * Méthode qui créee un identifiant à partir du nom et du prénom de l'utilisateur
         */
        if (nom.isEmpty() || prenom.isEmpty())
            return "";
        else
            return String.valueOf(prenom.toLowerCase().charAt(0))  + String.valueOf(nom.toLowerCase().charAt(0));
    }

    public static void main (String[] args) {   
        launch(args);
    }
    
}
