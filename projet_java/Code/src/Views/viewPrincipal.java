package Views;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class viewPrincipal extends ScrollPane {
    /*
     * Classe qui gère de créer la vue de la page principale de l'application elle hérite de ScrollPane pour pouvoir ajouter des éléments de vues
     * Elle gère également la mise à jour des posts de l'utilisateur
     */

    private Global global = new Global(); 
    private StackPane fond = new StackPane(); // crée un stackpane pour accueillir le fond d'écran
    private VBox vbox_principal = new VBox(); // Crée une Vbox pour accueillir les éléments de la pas de sorte à ce qu'ils soient ordonés
    private HBox topBar = new HBox(); // box pour gérer la barre de recherche et les boutons de navigation

    private String TITRE_FENETRE = global.get_TITRE_FENETRE();
    private double LARGEUR_FENETRE = global.get_LARGEUR_FENETRE();
    private int TAILLE_FIELD_TEXTE = 400;

    // Enonciation des variables pour le fond d'écran
    File file = new File("projet_java/Code/ressources/Image/background2.png");
    String absolutePath = file.getAbsolutePath();
    private ImageView backgroundImage = new ImageView(new Image("file:///" + absolutePath));
    private ImageView backgroundImage2 = new ImageView(new Image("file:///" + absolutePath));
    private ImageView backgroundImage3 = new ImageView(new Image("file:///" + absolutePath));
    private ImageView backgroundImage4 = new ImageView(new Image("file:///" + absolutePath));
    private ImageView backgroundImage5 = new ImageView(new Image("file:///" + absolutePath));

    private VBox vbox_topbar = new VBox();
    private Line separateur_top_barre = new Line();
    private Label titre_fenetre_principal = new Label("Page personnel de XXX");
    private TextField barre_recherche = new TextField();
    private ListView<String> resultat_recherche = new ListView<String>();
    private Label titre_recherche = new Label("Rechercher une page");
    private VBox vbox_recherche = new VBox();
    private ChoiceBox<String> type_post = new ChoiceBox<String>();
    private Label titre_post = new Label("Sélection du type de post");
    private VBox vbox_choix_post = new VBox();
    public VBox vbox_post = new VBox(20);
    private VBox vbox_fond = new VBox();
    // Element de la barre de menu
    private MenuBar menuBar = new MenuBar();
    private Menu menu_ami = new Menu("Amis");
    private Menu menu_se_deconnecter = new Menu("Se déconnecter");
    private MenuItem menu_liste_ami = new MenuItem("Voir liste d'amis");
    private MenuItem menu_liste_bloque = new MenuItem("Voir liste des utilisateurs bloqués");
    public MenuItem menu_deconnexion = new MenuItem("Déconnexion");
    
    private Button bouton_poster = new Button("Poster");

    private static User un_utilisateur_admin = new User();
    private static User proprietaire_mur;
    
    private TextArea champ_titre = new TextArea(); // Définition de la zone d'édition du texte
    private Label texte_du_titre = new Label(); // futur variable qui va contenir le texte
    private Button bouton_valider_titre = new Button("Poster");
    
    
    private Requete moteur_de_requete_mur = new Requete(); // Crée un moteur de requête pour récupérer les posts de l'utilisateur
    private Post_photo post_photo = new Post_photo();
    private Post_video post_video = new Post_video();


    // Constructeur de la viewPrincipal
    public viewPrincipal(Stage un_autre_Stage){ // user à rajouter
        vbox_principal.setAlignment(Pos.TOP_CENTER); // Centre les éléments de la page
        
        this.barre_de_menu(); // Ajoute la barre de menu à la page
        enTetePage(); // Ajoute l'en tête de la page
        gestionFond(); // Ajoute le fond d'écran à la page
        
        vbox_principal.getChildren().addAll(vbox_topbar, vbox_post); // Ajoute le fond et l'en tête à la page
        vbox_principal.setSpacing(20); // Ajoute un espacement entre les éléments de la page
        
        fond.getChildren().addAll(vbox_fond,vbox_principal); // le stack pane recupère la vbox et ajoute le fond
        
        this.setContent(fond); // le scrollpane récupère le stackpane et affiche la scrollbar
        
        this.gestionPost(un_autre_Stage); // Gère les posts de la page
    }
    
    public User creation_USER_admin(viewLogin viewconnexion){
        /*
        * Méthode qui crée un utilisateur à partir des informations de connexion
        */
        User un_utilisateur = new User(); 
        String id_utilisateur = new String(viewconnexion.slot_id.getText());
        String mdp_utilisateur = new String(viewconnexion.slot_mdp.getText());
        String selection_id_user = new String("SELECT idU, nom FROM USERS WHERE login = '" + id_utilisateur + "' ;"); // sélection de l'idU de l'utilisateur
        ArrayList<String> liste_id_user = moteur_de_requete_mur.parcoursTableSQL(selection_id_user, "idU");
        ArrayList<String> liste_nom_user = moteur_de_requete_mur.parcoursTableSQL(selection_id_user, "nom");
        String requete_mur = new String("SELECT idW FROM WALLS WHERE \"#idU\" =" + liste_id_user.get(0) + ";");
        ArrayList<String> liste_id_mur = moteur_de_requete_mur.parcoursTableSQL(requete_mur, "idW");
        
        un_utilisateur.setLogin(id_utilisateur);
        un_utilisateur.setPassword(mdp_utilisateur);
        un_utilisateur.setId_mur(Integer.parseInt(liste_id_mur.get(0)));
        un_utilisateur.setId_user(Integer.parseInt(liste_id_user.get(0)));
        un_utilisateur.setNom(liste_nom_user.get(0));

        return un_utilisateur;
    }

    private User creation_User_Annexe(int id_mur, int id_propietaire, String login, String nom){
        /*
         * Methode qui créee un utilisateur annexe propriétaires des murs visités
         */

        User proprietaire_murbis = new User();
        proprietaire_murbis.setLogin(login);
        proprietaire_murbis.setId_mur(id_mur);
        proprietaire_murbis.setId_user(id_propietaire);
        proprietaire_murbis.setNom(nom);

        return proprietaire_murbis;
    }
    
    public void setUtilisateurAdmin(User un_utilisateur){
        /*
        * Méthode qui permet de définir l'utilisateur administrateur de la page pour des besoins futurs si nécessaires 
        */
        un_utilisateur_admin = un_utilisateur;
    }

    public void info_post_mur(User un_utilisateur){
        /*
        * Méthode qui récupère les posts déjà présents sur le mur de l'utilisateur
        */
        int id_user = un_utilisateur.getId_user(); // recuperation de l'id de l'utilisateur
        String requete_sql = "SELECT Type_posts, texte, idP FROM USERS inner join POSTS ON USERS.idU = POSTS. \"#idU\" WHERE POSTS.idP not in (SELECT \"#idP_Rep\" from COMMENTS) AND Users.idU ='" + id_user + "';"; // requete pour récupérer le texte des posts et leurs types qui ne sont pas des commentaires
        String requete_sql_commentaire = "SELECT texte, idP FROM USERS inner join POSTS ON USERS.idU = POSTS. \"#idU\" WHERE POSTS.idP in (SELECT \"#idP_Rep\" from COMMENTS);"; // requete pour récupérer le texte et les id des commentaires
        ArrayList<String> liste_texte_commentaire = moteur_de_requete_mur.parcoursTableSQL(requete_sql_commentaire, "texte"); // liste du texte des commentaires
        ArrayList<String> liste__texte_poste_utilisateur = moteur_de_requete_mur.parcoursTableSQL(requete_sql, "texte"); // liste des textes des posts
        ArrayList<String> liste__type_poste_utilisateur = moteur_de_requete_mur.parcoursTableSQL(requete_sql, "Type_posts"); // lise des types des posts
        ArrayList<String> liste_id_commentaire = moteur_de_requete_mur.parcoursTableSQL(requete_sql_commentaire, "idP"); // liste des id des commentaires
        ArrayList<String> liste_idP = moteur_de_requete_mur.parcoursTableSQL(requete_sql, "idP"); // liste des id des posts
        LinkedHashMap<String, String> dico_post_utilisateur = new LinkedHashMap<String, String>(); // dictionnaire pour faire le lien entre les 2 listes
        LinkedHashMap<String, String> dico_commentaire = new LinkedHashMap<String, String>(); // dictionnaire pour faire le lien entre le commentaire et son post d'origine
        
        //  Boucle pour remplir le dictionnaire pour faire le lien entre les textes et les types des posts (texte d'un post texte est le corps du texte et le texte d'une image/video est le titre)
        for (int i = 0; i < liste__texte_poste_utilisateur.size(); i++)
            dico_post_utilisateur.put(liste__texte_poste_utilisateur.get(i), liste__type_poste_utilisateur.get(i));
        
        // Boucle pour remplir le dictionnaire qui fait le lien entre un id de commentaire et l'id du post initial
        for (int j = 0; j < liste_id_commentaire.size(); j++){
            String id_commentaire = new String(liste_id_commentaire.get(j));
            String requete_post_original = new String("SELECT \"#idP_Init\" FROM COMMENTS WHERE \"#idP_Rep\" = " + id_commentaire + ";");
            ArrayList<String> liste_id_post_original = moteur_de_requete_mur.parcoursTableSQL(requete_post_original, "#idP_Init");
            
            if (j < liste_texte_commentaire.size()) // si on a des commentaires
                dico_commentaire.put(id_commentaire, liste_id_post_original.get(0)); 
        }
        parcourDicoPost(dico_post_utilisateur, dico_commentaire, liste_idP, id_user); // parcours les dictionnaires pour afficher les différents posts

    }
    
    private void parcourDicoPost(LinkedHashMap<String, String> dico_post_utilisateur, LinkedHashMap<String, String> dico_commentaire, ArrayList<String> liste_idP, int id_user){
        /*
         * Méthode qui parcourt les dictionnaires pour afficher les posts
         */
        // Boucle pour afficher les posts
        int cpt = 0;
        for (String texte : dico_post_utilisateur.keySet()){
            Label texte_du_post = new Label(texte); // transforme la string en label qui peut être afficheé sur javafx
            String id_post  = new String(liste_idP.get(cpt));
            String requete_sql_nbr_like = new String("SELECT COUNT(\"#idP\") FROM LIKES GROUP BY \"#idP\" HAVING \"#idP\" = " + id_post + ";"); // compte nbr de like du post
            ArrayList<String> liste_nbr_like = moteur_de_requete_mur.parcoursTableSQL(requete_sql_nbr_like, "COUNT(\"#idP\")");
            String requete_sql_date_post = new String("SELECT dateM, dateC from POSTS WHERE \"#idU\" = '"+ id_user  +"' AND texte = '"+  texte + "';"); // recupère la date de création et de modiccation du post
            ArrayList<String> liste_date_post_modif = moteur_de_requete_mur.parcoursTableSQL(requete_sql_date_post, "dateM");
            ArrayList<String> liste_date_post = moteur_de_requete_mur.parcoursTableSQL(requete_sql_date_post, "dateC");
            String date_post = new String(liste_date_post.get(0));
            int nombre_like;

            if (!"null".equals(String.valueOf(liste_date_post_modif.get(0))))
                date_post = liste_date_post_modif.get(0);
             // charge la date de modification si elle existe

            if (liste_nbr_like.isEmpty()) // initialise le nombre de like à 0 si il n'y en a pas
                nombre_like = 0;
            
            else
                nombre_like = Integer.parseInt(liste_nbr_like.get(0)); // cast le string nbrlike en integer
            
            if (dico_post_utilisateur.get(texte).equals("Texte")) // si le post est un texte on lui applique le traitement approprié
                mise_en_page_post(texte_du_post, true, false,false, nombre_like, String.valueOf(id_user), id_post, null, date_post);

            else if (dico_post_utilisateur.get(texte).equals("Image")){
                // si c'est une image on doit la traiter un peu
                ImageView image = post_photo.TraitementImage(texte, texte_du_post, String.valueOf(id_user), id_post, date_post, nombre_like, vbox_post); // mise en forme de l'image
                mise_en_page_post(image, false, false,false, nombre_like, String.valueOf(id_user), id_post, null, date_post); // affiche les posts avec les boutons like, commenter et supprimer

            } 

            else {
                Media media = post_video.TraitementVideo(texte, texte_du_post, String.valueOf(id_user), id_post, date_post, nombre_like, vbox_post); /// mise en forme de la video
                mise_en_page_post(null, false, true,false, nombre_like, String.valueOf(id_user), id_post, media, date_post); // affiche les posts avec les boutons like, commenter et supprimer

            }
            // Commentaires
            if (dico_commentaire.containsValue(id_post))
                gestionCommentaire(dico_commentaire, id_post, date_post);    // affiche les commentaires sous les posts correspondants
        
            cpt ++;
        }
    }

    
    private void gestionCommentaire(LinkedHashMap<String, String> dico_commentaire, String id_post, String date_post){
        /*
         * Méthode qui gère les commentaires en vérifiant si le post qui vient d'être ajouté a un des commentaires associés
         */
        for (String commentaire : dico_commentaire.keySet()){ // parcours des clés du dictionnaire des commentaire (id_Commentaire)
            if (dico_commentaire.get(commentaire).equals(id_post)){ // si la valeur associé à la clé (id_post parent) est égale à l'id du post qui vient d'être ajouté
                String requete_texte = new String("SELECT texte, \"#idU\" FROM POSTS WHERE idP = " + commentaire + ";"); //  recupération des info (contenu du commentair et son auteur)
                ArrayList<String> liste_texte_commentaire_bis = moteur_de_requete_mur.parcoursTableSQL(requete_texte, "texte");
                ArrayList<String> liste_id_user_commentaire = moteur_de_requete_mur.parcoursTableSQL(requete_texte, "#idU");
                Label texte_du_commentaire = new Label(liste_texte_commentaire_bis.get(0));
                String requete_sql_nbr_like_com = new String("SELECT COUNT(\"#idP\") FROM LIKES GROUP BY \"#idP\" HAVING \"#idP\" = " + commentaire + ";");
                ArrayList<String> liste_nbr_like_com = moteur_de_requete_mur.parcoursTableSQL(requete_sql_nbr_like_com, "COUNT(\"#idP\")");
                int nombre_like_com;
                if (liste_nbr_like_com.isEmpty()){
                    nombre_like_com = 0;
                }
                else{
                    nombre_like_com = Integer.parseInt(liste_nbr_like_com.get(0)); // cast le string nbrlike en integer
                }
                mise_en_page_post(texte_du_commentaire, true, false,true, nombre_like_com, liste_id_user_commentaire.get(0), commentaire, null, date_post);
            }
        }
    }

    public StackPane mise_en_page_texte(Node node, boolean commentaire){
        /*
         * Méthode qui met en forme le texte des posts sous forme de stackpane
         */
        StackPane mise_en_forme_texte = new StackPane();
        Rectangle rectangle_texte = new Rectangle();
        Label labelNode = (Label) node;
    
        // Définit les dimensions initiales du Rectangle en fonction des dimensions préférées du Label
        rectangle_texte.setWidth(labelNode.getPrefWidth() + 20);
        rectangle_texte.setHeight(labelNode.getPrefHeight() + 20);
    
        mise_en_forme_texte.applyCss();
    
        Platform.runLater(() -> {
            labelNode.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                rectangle_texte.setWidth(newValue.doubleValue() + 20);
            });
    
            labelNode.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                rectangle_texte.setHeight(newValue.doubleValue() + 20);
            });
    
            if (commentaire)
                rectangle_texte.setFill(Color.web("gray", 0.8));
            else
                rectangle_texte.setFill(Color.web("white", 0.8));
    
            rectangle_texte.setStroke(Color.web("black", 0.6));
            rectangle_texte.setArcHeight(30);
            rectangle_texte.setArcWidth(30);
    
            mise_en_forme_texte.getChildren().addAll(rectangle_texte, node);
    
            // Force le StackPane à mettre à jour sa mise en page
            mise_en_forme_texte.layout();
        });
    
        return mise_en_forme_texte;
    }
    

    public void mise_en_page_post(Node node, boolean texte, boolean video ,boolean commentaire ,int nbr_like, String id_user, String id_post, Media une_video, String date_du_post){ // les images et les texte de javafx héritent de la classe Node

        /*
        * Méthode pour mettre en forme tous les posts avec les boutons like, commenter et supprimer
        */
        
        HBox hbox_boutons_action_post = new HBox(15);
        Button bouton_like = new Button("J'aime");
        StackPane mise_en_forme_du_texte = new StackPane();
        Button bouton_commenter = new Button("Commenter");
        Button bouton_supprimer = new Button("Supprimer");
        Button bouton_like_commentaire = new Button("Like Commentaire");
        Button bouton_supprimer_commentaire = new Button("Supprimer Commentaire");
        Button bouton_valider_commentaire = new Button("Poster");
        TextArea champ_commentaire = new TextArea(); // Définition de la zone d'édition du texte
        Label label_like = new Label();
        Label label_date = new Label(date_du_post);
        Label label_like_commentaire = new Label();
        VBox vbox_mise_en_forme_post = new VBox(10);

        String requete_nom_auteur = new String("SELECT nom FROM USERS WHERE idU = " + id_user + ";");
        ArrayList<String> liste_nom_auteur = moteur_de_requete_mur.parcoursTableSQL(requete_nom_auteur, "nom");
        Label nom_auteur = new Label(liste_nom_auteur.get(0));

        label_like.setFont(Font.font("System", FontWeight.BOLD, 15)); // change la police d'écriture et la passe en gras
        label_like.setText("Nombre de like du post : " + nbr_like); // affiche le nombre de like (0 par défaut)
        label_like_commentaire.setText("Nombre de like du commentaire : " + nbr_like);
        label_like_commentaire.setFont(Font.font("System", FontWeight.BOLD, 15)); // change la police d'écriture et la passe en gras
        vbox_mise_en_forme_post.setAlignment(Pos.CENTER); // Mise en forme des vbox et hbox
        hbox_boutons_action_post.setAlignment(Pos.CENTER);
        hbox_boutons_action_post.getChildren().addAll(bouton_like, bouton_commenter,bouton_supprimer); // ajout des boutons
        
        if (texte){ // si le post est un texte
            vbox_mise_en_forme_post.getChildren().clear(); //reset la vbox de la mise en page du texte précédent
            mise_en_forme_du_texte = mise_en_page_texte(node, commentaire); // récupère le texte et le met en forme
            if (commentaire){ // On ne met pas le bouton commenter
                hbox_boutons_action_post.getChildren().clear();
                hbox_boutons_action_post.getChildren().addAll(bouton_like_commentaire,  bouton_supprimer_commentaire); // retire le bouton commenter si c'est un commentaire
                vbox_mise_en_forme_post.getChildren().addAll(mise_en_forme_du_texte, hbox_boutons_action_post, label_like_commentaire, nom_auteur ,label_date); // ajout des boutons et du texte
            }

            else // si c'est un post normal
                vbox_mise_en_forme_post.getChildren().addAll(mise_en_forme_du_texte, hbox_boutons_action_post, label_like, nom_auteur , label_date); // ajout des boutons et du texte
            
            vbox_post.getChildren().add(vbox_mise_en_forme_post); // ajout du post à la page
                
    }
    
        else{ // si le post est une image ou une video

            if (video){ // Creation d'un post video et on le met en forme
                Post_video post_video = new Post_video();
                post_video.Mise_en_forme_Video(une_video, vbox_mise_en_forme_post, vbox_post, hbox_boutons_action_post, label_like, nom_auteur,label_date);
                
            }
            else { // ajout de l'image (node est une image ici)
                vbox_mise_en_forme_post.getChildren().addAll(node, hbox_boutons_action_post, label_like, nom_auteur ,label_date);
                vbox_post.getChildren().add(vbox_mise_en_forme_post);
            }
        }
        
            /*
             * Gestion des boutons liés aux posts, ils sont considérés comme des méthodes
             */
            final AtomicInteger nbr_like2 = new AtomicInteger(nbr_like); // Crée un compteur de like
            bouton_like.setOnAction(new EventHandler<ActionEvent>() { // gère le bouton like
                @Override
                public void handle(ActionEvent event) { 
                    un_utilisateur_admin.LikerPost(id_post, label_like, nbr_like2, false);
                }
            });


            bouton_supprimer.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try { // gère le cas où l'utilisateur n'a pas le droit de supprimer le post
                        un_utilisateur_admin.SupprimerPost(id_post, vbox_post, vbox_mise_en_forme_post, id_user);
                        vbox_post.getChildren().clear();
                        gestionImageProfil(un_utilisateur_admin);
                        info_post_mur(un_utilisateur_admin); 
                    }
                    catch (Exception error){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur de droit de suppression");
                        alert.setHeaderText(null);
                        alert.setContentText(error.getMessage());
                        alert.showAndWait();
                    }
                   
                }
            });
            
            
            bouton_commenter.setOnAction(new EventHandler<ActionEvent>() { // affiche la boite de dialogue pour commenter
                @Override
                public void handle(ActionEvent event) {
                    champ_commentaire.setPrefWidth(TAILLE_FIELD_TEXTE); // Fixe la taille de la zone d'édition
                    champ_commentaire.setPromptText("Ecrivez votre commentaire ici");
                    vbox_mise_en_forme_post.getChildren().addAll(champ_commentaire, bouton_valider_commentaire); // Ajoute le champ de texte à la page
                }
            });

            // gère ce que fait le bouton valider
            bouton_valider_commentaire.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    un_utilisateur_admin.PosterCommentaire(label_like_commentaire, vbox_mise_en_forme_post, champ_commentaire, bouton_valider_commentaire, bouton_like_commentaire, bouton_supprimer_commentaire, id_post);
                    insertionPostBDD(champ_commentaire, bouton_valider_commentaire, true, vbox_mise_en_forme_post, id_post); // enregistre le commentaire dans la BDD
                    vbox_post.getChildren().clear();
                    if (un_utilisateur_admin.getId_user() == Integer.parseInt(id_user)){
                        gestionImageProfil(un_utilisateur_admin); // recharge la page avec les nouveaux commentaires
                        info_post_mur(un_utilisateur_admin); 
                    }
                    else{
                        gestionImageProfil(proprietaire_mur);
                        info_post_mur(proprietaire_mur);
                    }
                     
                }
            });
                
                final AtomicInteger nbr_like_com = new AtomicInteger(nbr_like); // Crée un compteur de like pour les commentaires
                bouton_like_commentaire.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        un_utilisateur_admin.LikerPost(id_post, label_like_commentaire, nbr_like_com, true);

                    }
                });

                bouton_supprimer_commentaire.setOnAction(new EventHandler<ActionEvent>() { // Gère la suppresion des commentaires
                    @Override
                    public void handle(ActionEvent event) {
                        try{ // gère les droits de suppression d'un commentaire
                            un_utilisateur_admin.SupprimerCommentaire(id_user, id_post, vbox_mise_en_forme_post, proprietaire_mur);
                        }
                        catch (Exception error){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Boite d'erreur");
                            alert.setHeaderText(null);
                            alert.setContentText(error.getMessage());
                            alert.showAndWait();
                        } 
                    }
                });
            }

    private void barre_de_menu(){
        /*
         * Méthode qui crée une barre de menu pour la page
         */
        menu_ami.getItems().addAll(menu_liste_ami, menu_liste_bloque); // ajout des éléments à l'option menu ami
        menu_se_deconnecter.getItems().addAll(menu_deconnexion); // ajout des éléments à l'option menu se déconnecter
        // Ajouter le menu à la barre de menu
        menuBar.getMenus().addAll(menu_ami, menu_se_deconnecter); // ajout des éléments à la barre de menu
        // Gère le bouton d'affichage de la liste d'amis
        menu_ami.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Créer une nouvelle fenêtre
                Stage nouvelle_fenetre = new Stage();
                // Selectionne les amis de l'utilisateur
                String requete_liste_follower = new String("SELECT prenom FROM USERS INNER JOIN FOLLOWERS ON USERS.idU = FOLLOWERS.\"#idU\" WHERE \"#idW\" =" + un_utilisateur_admin.getId_mur() + " AND blocked = 0;");
                ArrayList<String> liste_follower = moteur_de_requete_mur.parcoursTableSQL(requete_liste_follower, "prenom");
                Tableau_follower tableau_follower = new Tableau_follower(liste_follower);
                try {
                    tableau_follower.start(nouvelle_fenetre);
                } catch (Exception error) {
                    error.printStackTrace();
                }
                
            }
        });

        // Gère le bouton d'affichage de la liste des bloqués
        menu_liste_bloque.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Créer une nouvelle fenêtre
                Stage nouvelle_fenetre_bloque = new Stage();
                String requete_liste_follower = new String("SELECT prenom FROM USERS INNER JOIN FOLLOWERS ON USERS.idU = FOLLOWERS.\"#idU\" WHERE \"#idW\" =" + un_utilisateur_admin.getId_mur() + " AND blocked = 1;");
                ArrayList<String> liste_bloque = moteur_de_requete_mur.parcoursTableSQL(requete_liste_follower, "prenom");
                Tableau_bloque tableau_bloque = new Tableau_bloque(liste_bloque);
                try {
                    tableau_bloque.start(nouvelle_fenetre_bloque);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        });

        // Ajouter la barre de menu à la boîte de mise en page verticale (VBox)
        vbox_principal.getChildren().add(menuBar); // ajout de la barre de menu à la page
    }

    private Circle creationCercle(){
        /*
         * Méthode qui crée un cercle pour l'image de profil
         * 
         */
        Circle cercle_contourbis = new Circle(); // Crée un cercle pour le contour de l'image de profil
        cercle_contourbis.setRadius(100); // Set the radius to the desired value
        cercle_contourbis.setCenterX(100);
        cercle_contourbis.setCenterY(100);
        cercle_contourbis.setFill(Color.TRANSPARENT); // Change la couleur de l'intérieur du cercle en transparent
        cercle_contourbis.setStroke(Color.BLACK); // Change la couleur de la bordure du cercle en noir
        cercle_contourbis.setStrokeWidth(5);
        return cercle_contourbis;
    }


    public void gestionImageProfil(User utilisteur_propriétaire){
        /*
         * Méthode qui gère l'image de profil de l'utilisateur et le titre du mur 
         * 
         */
        String requete_sql_photo_profil = new String("SELECT urlPhoto, descr FROM WALLS INNER JOIN USERS ON WALLS.\"#idU\" =  USERS.idU WHERE USERS.login = '"  + utilisteur_propriétaire.getLogin() + "' ;");
        ArrayList<String> liste_image_profil =  moteur_de_requete_mur.parcoursTableSQL(requete_sql_photo_profil, "urlPhoto");
        ArrayList<String> liste_description_mur =  moteur_de_requete_mur.parcoursTableSQL(requete_sql_photo_profil, "descr");
        Button bouton_follow  = new Button("Follow");
        VBox vbox_image_profil = new VBox();
        StackPane mise_forme_image = new StackPane();
        Circle circle = new Circle(); // Crée un cercle pour l'image de profil
        Circle cercle_contour = new Circle(); // Crée un cercle pour le contour de l'image de profil
        Label titre_mur = new Label(liste_description_mur.get(0)); // Récupère le titre du mur
        Image imageoriginale;

        if (liste_image_profil.get(0).equals("null")) // si l'utilisateur n'a pas d'image de profil
            liste_image_profil.set(0, "https://www.pngall.com/wp-content/uploads/5/Profile-Female.png"); // image par défaut
       
        // Préparation de l'image de profil
        if (liste_image_profil.get(0).startsWith("http"))  // test si l'image a un url ou est en local
            imageoriginale = new Image(liste_image_profil.get(0));

        else {
            String relativePath = liste_image_profil.get(0); // récupère le chemin relatif de l'image
            File file = new File(relativePath);
            String absolutePath = file.getAbsolutePath(); // récupère le chemin absolu de l'image
            imageoriginale = new Image("file:///" + absolutePath);
        }
        
        // Crée une image rognée carrée
        double minDimension = Math.min(imageoriginale.getWidth(), imageoriginale.getHeight());
        Image image_rognee = new WritableImage(imageoriginale.getPixelReader(), 0, 0, (int) minDimension, (int) minDimension);
        ImageView image_profil = new ImageView(image_rognee);

        titre_mur.setFont(Font.font("System", FontWeight.BOLD, 20)); // change la police d'écriture et la passe en gras

        image_profil.setFitWidth(200); // Règle les dimensions de l'image
        image_profil.setFitHeight(200);
        image_profil.setPreserveRatio(true); // Garde les proportions de l'image
        circle.setRadius(100); // Modifie le cercle de l'image de profil
        circle.setCenterX(100);
        circle.setCenterY(100);
        circle.setStroke(Color.BLACK); // Change la couleur de la bordure du cercle en noir
        circle.setStrokeWidth(5);
        image_profil.setClip(circle);

        cercle_contour = this.creationCercle(); // creation du cercle 
        mise_forme_image.getChildren().addAll(image_profil, cercle_contour ); // Ajoute l'image de profil au cercle

        vbox_image_profil.getChildren().addAll(titre_mur, mise_forme_image , bouton_follow); // Ajoute l'image, le titre et le bouton follow
        vbox_image_profil.setAlignment(Pos.CENTER); // Centre les éléments de la page
        vbox_post.getChildren().add(vbox_image_profil); // Ajoute l'image de profil à la page

        // Gère le bouton follow
        bouton_follow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    if (proprietaire_mur == null)
                        un_utilisateur_admin.Follow( un_utilisateur_admin.getId_mur());
                    else    
                        un_utilisateur_admin.Follow( proprietaire_mur.getId_mur());

                }
                catch (Exception error){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText(error.getMessage());  
                    alert.showAndWait(); 
                }
            }
        });

    }

    private void enTetePage(){
        /*
         * Méthode qui crée l'en tête de la page
         * 
         */
        topBar.setAlignment(Pos.TOP_CENTER);
        topBar.setSpacing(40); // Ajoute un espacement entre les éléments de l'en tête
        titre_fenetre_principal.setText(TITRE_FENETRE); // Définit le titre de la page (à modifier plus tard
        titre_fenetre_principal.setFont(Font.font(20)); // fixe la police d'écriture
        titre_fenetre_principal.setTranslateY(40); // Centre le titre de la page
        titre_fenetre_principal.setFont(Font.font("System", FontWeight.BOLD, 20)); // change la police d'écriture et la passe en gras

        barre_recherche.setPrefWidth(400); // Règle la taille de la barre de recherche
        resultat_recherche.setPrefHeight(20);
        titre_recherche.setLabelFor(barre_recherche);
        titre_recherche.setFont(Font.font("System", FontWeight.BOLD, 15)); // change la police d'écriture et la passe en gras
        vbox_recherche.setAlignment(Pos.CENTER);
        vbox_recherche.getChildren().addAll(titre_recherche, barre_recherche, resultat_recherche); // Ajoute les éléments à la barre de recherche

        type_post.getItems().addAll("Texte", "Image", "Vidéo"); // Ajoute les éléments à la liste déroulante
        titre_post.setLabelFor(type_post);
        titre_post.setFont(Font.font("System", FontWeight.BOLD, 15)); // change la police d'écriture et la passe en gras
        vbox_choix_post.getChildren().addAll(titre_post, type_post, bouton_poster); // Ajoute les éléments à la liste déroulante
        vbox_choix_post.setAlignment(Pos.CENTER);
        vbox_choix_post.setSpacing(5);

        vbox_post.setAlignment(Pos.CENTER); // Permet de centrer les pots et de les aligner à la verticale
        
        topBar.getChildren().addAll(vbox_recherche ,titre_fenetre_principal, vbox_choix_post); // Ajoute les éléments à la barre de recherche
        
        // Gestion du séparateur de l'en tête
        separateur_top_barre.setStartX(0.0f); // Point de départ
        separateur_top_barre.setStartY(0.0f);
        separateur_top_barre.setEndX(1920); // Défini la longueur du séparateur
        separateur_top_barre.setEndY(0.0f);
        separateur_top_barre.setStrokeWidth(3.0f); 
        separateur_top_barre.setStroke(Color.BLACK); // Change la couleur de la ligne en noir

        vbox_topbar.getChildren().addAll(topBar, separateur_top_barre); 
        vbox_topbar.setSpacing(10); // Ajoute un espacement entre les éléments de l'en tête
        gestionBarreRecherche();
    }

    private void gestionBarreRecherche(){
         /*
          * Méthode qui gère la barre de recherche
          */
        barre_recherche.textProperty().addListener((observable, oldValue, recherche) -> {
        // Créez une instance de votre classe de gestion de base de données
        
        // on extrait les murs de la BDD
        String requete = "SELECT nom FROM WALlS INNER JOIN USERS ON WALLS.\"#idU\" = USERS.idU WHERE nom LIKE '%" + recherche + "%';";
        List<String> walls = moteur_de_requete_mur.parcoursTableSQL(requete, "nom");

        // Actualisation de la listview en fonction de ce que l'on tape dans la barre
        resultat_recherche.getItems().clear();
        resultat_recherche.getItems().addAll(walls);

        resultat_recherche.getSelectionModel().selectedItemProperty().addListener((observation, ancienne_valeur, newValue) -> {

            String requete_nouveau_mur = new String("SELECT login, nom, idW, idU FROM WALLS INNER JOIN USERS ON WALLS.\"#idU\" = USERS.idU WHERE nom = '" + newValue + "';");
            ArrayList<String> liste_login_proprio = moteur_de_requete_mur.parcoursTableSQL(requete_nouveau_mur, "login");
            ArrayList<String> liste_idW_proprio = moteur_de_requete_mur.parcoursTableSQL(requete_nouveau_mur, "idW");
            ArrayList<String> liste_idU_proprio = moteur_de_requete_mur.parcoursTableSQL(requete_nouveau_mur, "idU");
            ArrayList<String> liste_nom_proprio = moteur_de_requete_mur.parcoursTableSQL(requete_nouveau_mur, "nom");


            if (!liste_login_proprio.isEmpty() && !liste_idW_proprio.isEmpty() && !liste_idU_proprio.isEmpty())
                proprietaire_mur = creation_User_Annexe(Integer.parseInt(liste_idW_proprio.get(0)), Integer.parseInt(liste_idU_proprio.get(0)), liste_login_proprio.get(0), liste_nom_proprio.get(0));
            
            String requete_statut = new String("SELECT blocked FROM FOLLOWERS WHERE \"#idU\" = " + un_utilisateur_admin.getId_user() + " AND \"#idW\" = " + proprietaire_mur.getId_mur() + ";");
            ArrayList<String> liste_statut = moteur_de_requete_mur.parcoursTableSQL(requete_statut, "blocked");
            try{
                gestionStatutUser(liste_statut); // bloque qui affiche le mur de l'utilisateur sélectionné ou non s'il est bloqué
            }
            catch (Exception error){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Boite d'erreur");
                alert.setHeaderText(null);
                alert.setContentText(error.getMessage());
                alert.showAndWait();
            }
            
        });
    });

    }

    private void gestionStatutUser(ArrayList<String> liste_du_statut) throws VerifStatut{
        /*
         * Méthode pour gérér le droit d'accèes de l'utilisateur à un mur
         */
        if (liste_du_statut.isEmpty()){ // si l'utilisateur admin ne suit pas ou n'est pas bloqué par le propriétaire du mur
                vbox_post.getChildren().clear();
                gestionImageProfil(proprietaire_mur);
                info_post_mur(proprietaire_mur);
            }
            else if (liste_du_statut.get(0).equals("0")){ // si l'utilisateur admin suit le propriétaire du mur on le charge alors
                vbox_post.getChildren().clear();
                gestionImageProfil(proprietaire_mur);
                info_post_mur(proprietaire_mur);
            }
            else{
                throw new VerifStatut("Vous êtes bloqué par cet utilisateur");
                
            }
    }

    private void gestionFond(){
        /*
         * Méthode qui ajoute les fonds à la fenetre
         * 
         */
        backgroundImage.fitWidthProperty().bind(this.widthProperty()); // Règle les dimensions de l'image
        backgroundImage.fitHeightProperty().bind(this.heightProperty());
        backgroundImage2.fitWidthProperty().bind(this.widthProperty()); // Règle les dimensions de l'image
        backgroundImage2.fitHeightProperty().bind(this.heightProperty());
        backgroundImage3.fitWidthProperty().bind(this.widthProperty()); // Règle les dimensions de l'image
        backgroundImage3.fitHeightProperty().bind(this.heightProperty());
        backgroundImage4.fitWidthProperty().bind(this.widthProperty()); // Règle les dimensions de l'image
        backgroundImage4.fitHeightProperty().bind(this.heightProperty());
        backgroundImage5.fitWidthProperty().bind(this.widthProperty()); // Règle les dimensions de l'image
        backgroundImage5.fitHeightProperty().bind(this.heightProperty());

         // Ajout de tous les fonds à la vbox pour créer le fond d'écran (les aligne à la verticale)
         vbox_fond.getChildren().addAll(backgroundImage, backgroundImage2,  backgroundImage3,  backgroundImage4,  backgroundImage5); 
       
    }

    private void gestionPost(Stage un_exemple_de_stage){
        // gère le bouton poster
        this.bouton_poster.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                try{ // test ce que l'on fait avec le bouton post
                    verifPost();
                }
                catch (Exception error){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText(error.getMessage());
                    alert.showAndWait();
                    return; // arrete le code si une erreur est détectée
                }

                String choix_du_post = new String(type_post.getValue()); // récupère le choix de l'utilisateur pour son post
                
                // Gère le cas où l'utilisateur a choisi de poster une Image
                if (choix_du_post.equals("Image"))
                    gestionImage(un_exemple_de_stage);  
                
                else if (choix_du_post.equals("Texte"))// Gère le cas où l'utilisateur a choisi de poster du texte
                    gestionTexte(un_utilisateur_admin, false);
                
                else if (choix_du_post.equals("Vidéo")) // Gère le cas où l'utilisateur a choisi de poster une vidéo
                    miseEnPageVideo(un_exemple_de_stage);
                }
                
            });
        }

    private void verifPost() throws VerifIdentiteUser, VerifTypePost{
        if (type_post.getValue() == null){
            throw new VerifTypePost("Veuillez choisir un type de post");
        }
        
        if (proprietaire_mur == null || un_utilisateur_admin.getId_user() != proprietaire_mur.getId_user()){ // test si l'utilisateur est sur son propre mur ou non
            if (proprietaire_mur!=null) // test si l'utilisateur est sur le mur de quelqu'un d'autre
                throw new VerifIdentiteUser("Vous ne pouvez pas poster sur le mur d'un autre utilisateur");
        }
    }

    private void gestionTexte(User utilisateur_auteur, boolean iscommentaire){
            /*
            * Méthode qui gère la mise en forme des post de type texte sur la page
            */
        TextArea champ_texte = new TextArea(); // Définition de la zone d'édition du texte
        Button bouton_valider_post = new Button("Poster");
        champ_texte.setPrefWidth(TAILLE_FIELD_TEXTE); // Fixe la taille de la zone d'édition
        champ_texte.setPromptText("Ecrivez votre Poste ici");
        vbox_post.getChildren().addAll(champ_texte, bouton_valider_post); // Ajoute le champ de texte à la page
        
        // gère ce que fait le bouton valider
        bouton_valider_post.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                insertionPostBDD(champ_texte, bouton_valider_post, iscommentaire, null, " ");
                vbox_post.getChildren().clear();
                gestionImageProfil(un_utilisateur_admin);
                info_post_mur(un_utilisateur_admin);             
            }
        });
    }

    private void insertionPostBDD(TextArea champs_du_texte, Button btn_valider_post, boolean commentaire, VBox vbox_commentaire, String id_post_parent){

        /*
        * Méthode qui insère un nouveau Post_texte dans la BDD
        */
        un_utilisateur_admin.posterTexte(champs_du_texte, id_post_parent, commentaire, vbox_post, vbox_commentaire, btn_valider_post, proprietaire_mur);

    }

    
    
    private void gestionImage(Stage un_autrestage){
        /*
         * Méthode qui gère la mise en forme des post de type image sur la page
         * 
         */
        champ_titre.setMaxWidth(200);// fie la longueur du champ de texte
        champ_titre.setPrefHeight(30); // fixe la hauteur du champ de texte
        champ_titre.setPromptText("Ecrivez votre titre ici");
        vbox_post.getChildren().addAll(champ_titre, bouton_valider_titre); // Ajoute le champ de texte à la page
        
        // Gère le bouton valider pour
        bouton_valider_titre.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                insertionImageBDD(un_autrestage); // Crée un post de type image
                vbox_post.getChildren().clear(); // recharge la page
                gestionImageProfil(un_utilisateur_admin);
                info_post_mur(un_utilisateur_admin);
            }
        });
    }
    
    private void insertionImageBDD(Stage un_autrestage){
        /*
         * Méthode qui insère une image dans la BDD et la met en forme
         */
        
        post_photo.gestionRetraitLayoutTitre(texte_du_titre, vbox_post, champ_titre, bouton_valider_titre);
        un_utilisateur_admin.postPhoto(un_autrestage, texte_du_titre);
    }
    

    public HashMap<ImageView, Path> gestionChoixFichierImage(Stage un_autrestage){
         // Définition de la boite de dialogue pour choisir un fichier image 
         Path targetPath = null;
         FileChooser choix_fichier = new FileChooser();
         ImageView image = new ImageView();
         HashMap<ImageView, Path> info_image = new HashMap<>();
         // Filtre les fichiers sélectionnables
         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png)", "*.png");
         choix_fichier.getExtensionFilters().add(extFilter);
         File fichier_choisi = choix_fichier.showOpenDialog(un_autrestage); // affiche la boite de dialogue
         if(fichier_choisi == null)
             ; // ne fait rien si l'utilisateur n'a rien sélectionné
         
         else {
             // Récupération du chemin du fichier image
            String chemin = new String(fichier_choisi.getAbsolutePath());
            Path sourcePath = Paths.get(chemin);
            
            targetPath = Paths.get("projet_java/Code/ressources/Image/" + fichier_choisi.getName()); // chemin relatif dans le projet java
            

             // Copie le fichier image sélectionné pour le retrouver plus tard
             try {
                 Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
             } catch (IOException e) {
                 e.printStackTrace();
             }
            
            // Chargement d'une ImageView avec le chemin absolu (universel pour tout le monde)
            image = new ImageView(new Image(targetPath.toUri().toString()));
            image.setFitWidth(600); // Règle les dimensions de l'image
            image.setFitHeight(400);
            image.setPreserveRatio(true); // Garde les proportions de l'image
            info_image.put(image, targetPath);
            
        }
            return info_image;
        }


    public HashMap<Media, Path> gestionChoixFichierVideo(Stage un_autrestage){
        Path targetPath = null;
        FileChooser choix_fichier = new FileChooser();
        HashMap<Media, Path> info_video = new HashMap<>();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.mp4)", "*.mp4");
        choix_fichier.getExtensionFilters().add(extFilter);
        
        File fichier_choisi = choix_fichier.showOpenDialog(un_autrestage); // affiche la boite de dialogue
        if(fichier_choisi == null)
        ; // ne fait rien si l'utilisateur n'a rien sélectionné
    
        else {
        // Récupération du chemin du fichier image
            String chemin = new String(fichier_choisi.getAbsolutePath());
            Path sourcePath = Paths.get(chemin);
            
            targetPath = Paths.get("projet_java/Code/ressources/Video/" + fichier_choisi.getName()); // chemin relatif dans le projet java

            // Copie le fichier image sélectionné pour le retrouver plus tard
            try {
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Media media = new Media(targetPath.toUri().toString());
        info_video.put(media, targetPath);
    }
    return info_video;
}
    
    private void miseEnPageVideo(Stage un_autrestage){
        /*
        * Méthode qui met en forme une vidéo
        * 
        */
        champ_titre.setMaxWidth(200); // fixe la longueur du champ de texte
        champ_titre.setPrefHeight(30); // fixe la hauteur du champ de texte
        champ_titre.setPromptText("Ecrivez votre titre ici");
        vbox_post.getChildren().addAll(champ_titre, bouton_valider_titre); // Ajoute le champ de texte à la page

        // Gère le bouton valider pour
        bouton_valider_titre.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                insertionVideoBDD(un_autrestage);
                vbox_post.getChildren().clear(); // recharge la page
                gestionImageProfil(un_utilisateur_admin);
                info_post_mur(un_utilisateur_admin);
            }
        });
    }

    private void insertionVideoBDD(Stage un_autrestage){
        post_video.gestionRetraitLayoutTitre(texte_du_titre, vbox_post, champ_titre, bouton_valider_titre);
        un_utilisateur_admin.postVideo(un_autrestage, texte_du_titre);
    
    }
    
}
