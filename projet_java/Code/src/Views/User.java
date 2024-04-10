package Views;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class User {
    /*
     * Classe qui gère les actions et les droits que peut avoir un utilisateur du réseau social
     */
 
    private String nom = new String();
    private String prenom = new String();
    private String password = new String();
    private int id_mur;
    private String login;
    private int id_user;
    private viewPrincipal scene_principale = new viewPrincipal(new Stage());

    // Constructeur
    public User(){
        this.nom = "default";
        this.prenom = "default";
        this.login = "default";
        this.password = "default";
        this.id_mur = 0;
        this.id_user = 0;
    }


    public User(int id_du_mur, String nom, String prenom, String password, int id_du_user) {
        this.nom = nom;
        this.prenom = prenom;
        this.login = String.valueOf(prenom.charAt(0)) + String.valueOf(nom.charAt(0));
        this.password = password;
        this.id_mur = id_du_mur;
        this.id_user = id_du_user;
    }


    // Accesseur
    public int getId_mur() {
        return this.id_mur;
    }

    public String getNom() {
        return this.nom;
    }


    public String getPrenom() {
        return this.prenom;
    }


    public String getPassword() {
        return this.password;
    }

    public int getId_user() {
        return this.id_user;
    }


    public String getLogin() {
        return this.login;
    }

   // mutateurs

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setId_mur(int id_mur) {
        this.id_mur = id_mur;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    public void gestionInfoInscription(Stage primaryStage, Scene sceneconnexion, String nom, String prenom, String identifiant, String mdp, String mdp2, LocalDate date_anniversaire, String description_mur, AtomicReference<String> chemin_photo, Requete moteur_requete) throws VerifInfoInscription{
        /*
         * Méthode qui permet de gérer les informations d'inscription d'un utilisateur et de les insérer dans la BDD si besoin
         */
        // test si l'un des champs est vide
        if (nom.isEmpty() || prenom.isEmpty() || mdp.isBlank() || mdp2.isBlank() || date_anniversaire == null || description_mur.isBlank()) 
            throw new VerifInfoInscription("Veuillez remplir tous les champs");
        
        // On teste si l'identifiant est déjà dans la base de données
        else if (moteur_requete.parcoursTableSQL("SELECT * FROM USERS;", "login").contains(identifiant)) 
            throw new VerifInfoInscription("Identifiant déjà utilisé");
        
        // On teste si les deux mots de passe sont identiques
        else if (!mdp.equals(mdp2)) 
            throw new VerifInfoInscription("Les mots de passe ne correspondent pas");
        
        
        else { // création du user dans la base de données
            moteur_requete.insertion_sql("INSERT INTO USERS (login, mdp, nom, prenom, dateNaiss) VALUES ('" + identifiant + "', '" + mdp + "', '" + nom.toUpperCase()  + "' , '" + prenom + "' , '" + date_anniversaire + "');");
            ArrayList<String> liste_idU = moteur_requete.parcoursTableSQL("SELECT idU FROM USERS WHERE login = '" + identifiant + "' ;", "idU");
            int idU = Integer.valueOf(liste_idU.get(0));
            moteur_requete.insertion_sql("INSERT INTO WALLS (descr, urlPHOTO, \"#idU\") VALUES ('" + description_mur + "', '" + chemin_photo.get() +  "', " + idU + " );");
        }  
        
    } 

    public void Follow(int id_mur, Requete moteur_requete) throws VerifBloque{
        /*
         * Fonction qui permet de suivre le mur d'un utilisateur
         */
        String requete_generale = new String("SELECT * FROM FOLLOWERS WHERE \"#idU\" = '" + this.id_user + "' AND \"#idW\" = '" + id_mur + "';");
        if(moteur_requete.parcoursTableSQL(requete_generale, "blocked").contains("1")) // on vérifie si l'utilisateur a été bloqué
            throw new VerifBloque("Vous avez été bloqué par cet utilisateur.");     
        // verification si on suit déjà le mur ou non
        else if (moteur_requete.parcoursTableSQL(requete_generale, "#idU").contains(String.valueOf(this.id_user)) && moteur_requete.parcoursTableSQL(requete_generale, "#idW").contains(String.valueOf(id_mur))){
            throw new VerifBloque("Vous suivez déjà ce mur.");
        }
        else{ // insertion du follower dans la BDD
            String requete_ami = new String("INSERT INTO FOLLOWERS (\"#idU\", \"#idW\", blocked) VALUES (" +this.id_user + ", " + id_mur + ", " + 0 +");");
            moteur_requete.insertion_sql(requete_ami);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inscription réussie");
            alert.setHeaderText(null);
            alert.setContentText("Vous vous êtes bien inscrit au mur.");
            // Afficher la boîte de dialogue et attendre que l'utilisateur la ferme
            alert.showAndWait();
        }
         
    }

    public void LikerPost(String id_post, Label label_like, AtomicInteger nbr_like2, boolean iscommentaire , Requete moteur_requete){
        /*
         * Fonction qui permet de liker un post ou de déliker un post
         */

        // On recupere le nombre de like du post en question
        String requete_control = new String("SELECT COUNT(*) FROM LIKES WHERE \"#idU\" =" + this.getId_user()  + " AND \"#idP\" =" + id_post +";");
        ArrayList<String> liste_control = moteur_requete.parcoursTableSQL(requete_control, "COUNT(*)");
        String boite_texte;
        if (iscommentaire)
            boite_texte = new String("Nombre de like du commentaire :");
        else
            boite_texte = new String("Nombre de like du post :");

        if (liste_control.get(0).equals("0")) { // si c'est égale à 0 alors on ajoute un like
            nbr_like2.incrementAndGet();
            label_like.setText(boite_texte + nbr_like2);
            // insertion du like dans la BDD
            String requete_sql_maj_like = new String("INSERT INTO LIKES (\"#idP\", \"#idU\") VALUES (" + id_post + ", " + this.getId_user()  + ");");
            moteur_requete.insertion_sql(requete_sql_maj_like);
        }

        else { // sinon on retire un like
            nbr_like2.decrementAndGet();
            label_like.setText(boite_texte + nbr_like2);
            String requete_sql_maj_like = new String("DELETE FROM LIKES WHERE \"#idP\" = " + id_post + " AND \"#idU\" = " + this.getId_user()  + ";");
            moteur_requete.insertion_sql(requete_sql_maj_like);
        }
    }

    public  void SupprimerPost(String id_post, VBox vbox_post, VBox vbox_mise_en_forme_post, String id_user, Requete moteur_requete)  throws VerifDroitSuppression{
        /*
         * Fonction qui permet de supprimer un post
         */
        String requete_ctrl_proprietaire = new String("SELECT idP, \"#idU\" FROM POSTS WHERE idP = " + id_post + ";"); // recupère id de l'auteur du post
        String requete_commentaire_associe = new String("SELECT \"#idP_Rep\" FROM COMMENTS WHERE \"#idP_Init\" = " + id_post + ";"); // recupère id du posts associé
        ArrayList<String> liste_id_proprietaire = moteur_requete.parcoursTableSQL(requete_ctrl_proprietaire, "#idU");
        ArrayList<String> liste_commentaire_associe = moteur_requete.parcoursTableSQL(requete_commentaire_associe, "#idP_Rep");
        // On vérifie si l'utilisateur est bien le propriétaire du post
        if (id_user.equals(String.valueOf(this.getId_user())) || liste_id_proprietaire.get(0).equals(String.valueOf(this.getId_user()))){
            // Supprime le post de la base de données
            String requete_sql_suppression = new String("DELETE FROM POSTS WHERE idP = " + id_post + ";");
            String requete_sql_suppression_like_post = new String("DELETE FROM LIKES WHERE \"#idP\" = " + id_post + ";");
            String requete_sql_suppression_commentaire_post = new String("DELETE FROM COMMENTS WHERE \"#idP_Init\" = " + id_post + ";");
            moteur_requete.insertion_sql(requete_sql_suppression);
            moteur_requete.insertion_sql(requete_sql_suppression_like_post);
            // Parcours de la liste contenant les commentaire du post si il y en a et suppression de ces derniers dans la BDD
            for (String id_commentaire : liste_commentaire_associe){
                String requete_sql_suppression_commentaire = new String("DELETE FROM POSTS WHERE idP = " + id_commentaire + ";");
                String requete_sql_suppression_like_commentaire = new String("DELETE FROM LIKES WHERE \"#idP\" = " + id_commentaire + ";");
                moteur_requete.insertion_sql(requete_sql_suppression_commentaire);
                moteur_requete.insertion_sql(requete_sql_suppression_like_commentaire);
            }
            moteur_requete.insertion_sql(requete_sql_suppression_commentaire_post); // suppression dans la base
            vbox_post.getChildren().remove(vbox_mise_en_forme_post); // suppression sur le mur
        }
        else {
            throw new VerifDroitSuppression("Vous ne pouvez pas supprimer le post d'un autre utilisateur");
            
        }
    }

    public void SupprimerCommentaire(String id_user, String id_post, VBox vbox_post, User proprietaire_mur, Requete moteur_requete) throws VerifDroitSuppressionCommentaire{
        /*
         * Fonction qui permet de supprimer un commentaire
         */
        String requete_ctrl_proprietaire = new String("SELECT \"#idW\", \"#idU\" FROM POSTS WHERE idP = " + id_post + ";");
        ArrayList<String> liste_id_proprietaire = moteur_requete.parcoursTableSQL(requete_ctrl_proprietaire, "#idU");
        ArrayList<String> liste_id_wall = moteur_requete.parcoursTableSQL(requete_ctrl_proprietaire, "#idW");
        // On vérifie si l'utilisateur est bien le propriétaire du post ou si le commentaire va être supprimé par le propriétaire du mur
        if (id_user.equals(String.valueOf(this.getId_user())) || liste_id_proprietaire.get(0).equals(String.valueOf(this.getId_user())) || this.getId_mur() == Integer.valueOf(liste_id_wall.get(0))){
            // Supprime le post de la base de données
            String requete_sql_suppression = new String("DELETE FROM POSTS WHERE idP = " + id_post + ";");
            String requete_sql_suppression_like_post = new String("DELETE FROM LIKES WHERE \"#idP\" = " + id_post + ";");
            String requete_sql_suppression_commentaire_post = new String("DELETE FROM COMMENTS WHERE \"#idP_Rep\" = " + id_post + ";");
            moteur_requete.insertion_sql(requete_sql_suppression);
            moteur_requete.insertion_sql(requete_sql_suppression_like_post);
            moteur_requete.insertion_sql(requete_sql_suppression_commentaire_post);
            
            vbox_post.getChildren().clear();
            if (proprietaire_mur == null){ // on recharge la page du mur correspondante au user en question (utilisateur connecté)
                scene_principale.gestionImageProfil(this);
                scene_principale.info_post_mur(this);
            }
            else{ // page sur d'une autre personne
                scene_principale.gestionImageProfil(proprietaire_mur);
                scene_principale.info_post_mur(proprietaire_mur);
            }
        }
        else { // si on n a pas les droits pour supprimer le commentaire
            throw new VerifDroitSuppressionCommentaire("Vous ne pouvez pas supprimer le commentaire d'un autre utilisateur");
        }
    }
    
    public void posterTexte(TextArea champs_du_texte, String id_post_parent, boolean commentaire, VBox vbox_post, VBox vbox_commentaire, Button btn_valider_post, User proprietaire_mur, Requete moteur_requete){
        /*
         * Méthode qui permet à un user de créer un post_texte et de l'insérer dans la base de données
         */

        Post_texte nouveau_post = new Post_texte(); // Crée un nouveau post
        HashMap<String, Object> info_nouveau_post = new HashMap<String, Object>(); // Crée un dictionnaire pour stocker les informations du post
        String requete_selection_id_user = new String("SELECT idU, idW FROM USERS INNER JOIN WALLS ON WALLS.\"#idU\" = USERS.idU WHERE login = '" + this.getLogin() + "' ;"); // sélection de l'idU de l'utilisateur
        String requete_mur_bis;
        ArrayList<String> liste_id_wall;
        String date_post = new String(nouveau_post.getDate());
        Label texte_du_post = new Label(); // Crée un label pour le texte du post
        if (proprietaire_mur != null){ // l'idW change si on est sur la page de quelqu'un d'autre
            requete_mur_bis = new String("SELECT idW FROM WALLS WHERE \"#idU\" = " + proprietaire_mur.getId_user() + ";");
            liste_id_wall = moteur_requete.parcoursTableSQL(requete_mur_bis, "idW");
        }
        else
            liste_id_wall = moteur_requete.parcoursTableSQL(requete_selection_id_user, "idW");
        
        texte_du_post.setText(champs_du_texte.getText()); // Crée un texte à partir du champ de texte
         if (!commentaire){
             vbox_post.getChildren().remove(champs_du_texte); // Retire le champ de texte de la page
             vbox_post.getChildren().remove(btn_valider_post); // Supprime le bouton valider de la page
         }
         else{
             vbox_commentaire.getChildren().remove(champs_du_texte); // Retire le champ de texte de la page
             vbox_commentaire.getChildren().remove(btn_valider_post); // Supprime le bouton valider de la page
         }

         info_nouveau_post.put("texte", texte_du_post.getText()); // dictionnaire pour remplir la BDD
         info_nouveau_post.put("format", null);
         info_nouveau_post.put("urlIMG", null);
         info_nouveau_post.put("urlVID", null);
         info_nouveau_post.put("duree", null);
         info_nouveau_post.put("dateC", date_post);
         info_nouveau_post.put("dateM", null);
         info_nouveau_post.put("#idU", this.getId_user());
         info_nouveau_post.put("#idW", liste_id_wall.get(0));
         info_nouveau_post.put("Type_posts", "Texte");
         
         nouveau_post.insertion_BDD_post(info_nouveau_post, moteur_requete); // insère le post dans la base de données
         String requete_idpost = new String("SELECT idP FROM POSTS WHERE \"#idU\" =" + this.getId_user() + " AND dateC ='" + date_post + "' AND texte = '" +  info_nouveau_post.get("texte") + "' ;");
         ArrayList<String> liste_id_post = moteur_requete.parcoursTableSQL(requete_idpost, "idP");

         if (commentaire){
             String requete_insertion_commentaire = new String("INSERT INTO COMMENTS (\"#idP_Init\", \"#idP_Rep\") VALUES (" + id_post_parent + "," + liste_id_post.get(0) + ");");
             moteur_requete.insertion_sql(requete_insertion_commentaire);
         }

    }

    public void PosterCommentaire(Label label_like_commentaire, VBox vbox_mise_en_forme_post, TextArea champ_commentaire, Button bouton_valider_commentaire, Button bouton_like_commentaire, Button bouton_supprimer_commentaire, String id_post){
        /*
         * Méthode qui permet à un user de créer un commentaire et de l'insérer dans la base de données
         */
       
        Post_texte commentaire = new Post_texte();
        Label label_date_commentaire = new Label(commentaire.getDate());
        Label texte_commentaire = new Label(champ_commentaire.getText());
        VBox vbox_mise_en_forme_commentaire = new VBox(10);
        HBox layout_commentaire = new HBox(15);
        StackPane mise_en_forme_du_texte_commentaire = new StackPane();
        
        layout_commentaire.setAlignment(Pos.CENTER);
        vbox_mise_en_forme_post.getChildren().remove(champ_commentaire); // Retire le champ de texte de la page
        vbox_mise_en_forme_post.getChildren().remove(bouton_valider_commentaire); // Supprime le bouton valider de la page
        mise_en_forme_du_texte_commentaire = scene_principale.mise_en_page_texte(texte_commentaire, true); // rempli le stackpane pour mettre en forme le texte
        layout_commentaire.getChildren().addAll(bouton_like_commentaire, bouton_supprimer_commentaire); // ajout des boutons like et supprimer
        vbox_mise_en_forme_commentaire.getChildren().addAll(mise_en_forme_du_texte_commentaire, layout_commentaire,label_like_commentaire , label_date_commentaire); // ajout du texte et des boutons
        vbox_mise_en_forme_commentaire.setAlignment(Pos.CENTER); // centre les éléments
        
        vbox_mise_en_forme_post.getChildren().add(vbox_mise_en_forme_commentaire); // ajout du commentaire

    }

     public void postPhoto(Stage un_autrestage, Label texte_du_titre, Requete moteur_requete){
        /*
         * Méthode qui crée un post photo et l'insère dans la base de données
         */
        Post_photo nouveau_post_image = new Post_photo(); // Crée un nouveau post de type photo
        HashMap<ImageView, Path> image_post = scene_principale.gestionChoixFichierImage(un_autrestage); // Gère le choix du fichier image
        HashMap<String, Object> info_nouveau_post_photo = new HashMap<String, Object>(); // Crée un dictionnaire pour stocker les informations du post
        String url_image = new String(image_post.get(image_post.keySet().toArray()[0]).toString()); // récupère l'url de l'image
        String date_post = new String(nouveau_post_image.getDate());

        // On remplir le dictionnaire poiur les informations de la BDD
        info_nouveau_post_photo.put("texte", texte_du_titre.getText());
        info_nouveau_post_photo.put("format", ".png");
        info_nouveau_post_photo.put("urlIMG", url_image);
        info_nouveau_post_photo.put("urlVID", null);
        info_nouveau_post_photo.put("duree", null);
        info_nouveau_post_photo.put("dateC", date_post);
        info_nouveau_post_photo.put("dateM", null);
        info_nouveau_post_photo.put("#idU", this.getId_user());
        info_nouveau_post_photo.put("#idW", this.getId_mur());
        info_nouveau_post_photo.put("Type_posts", "Image");
        
        nouveau_post_image.insertion_BDD_post(info_nouveau_post_photo, moteur_requete); // insère le post dans la base de données

    }


    public void postVideo(Stage un_autrestage, Label texte_du_titre, Requete moteur_requete){
        /*
         * Méthode qui creé un post vidéo et l'insère dans la base de données
         * 
         */

        Post_video nouveau_post = new Post_video(); // Crée un nouveau post de type vidéo
        HashMap<Media, Path> video_post = scene_principale.gestionChoixFichierVideo(un_autrestage); // Gère le choix du fichier vidéo
        HashMap<String, Object> info_nouveau_post_video = new HashMap<String, Object>(); // Crée un dictionnaire pour stocker les informations du post
        String url_video = new String(video_post.get(video_post.keySet().toArray()[0]).toString()); // récupère l'url de la vidéo
        Media video_poste = (Media) video_post.keySet().toArray()[0]; // récupère la vidéo
        MediaPlayer mediaPlayer = new MediaPlayer(video_poste);
        String date_post = new String(nouveau_post.getDate());

        info_nouveau_post_video.put("texte", texte_du_titre.getText());
        info_nouveau_post_video.put("format", ".mp4");
        info_nouveau_post_video.put("urlIMG", null);
        info_nouveau_post_video.put("urlVID", url_video);
        info_nouveau_post_video.put("duree", mediaPlayer.getTotalDuration().toSeconds());
        info_nouveau_post_video.put("dateC", date_post);
        info_nouveau_post_video.put("dateM", null);
        info_nouveau_post_video.put("#idU", this.getId_user());
        info_nouveau_post_video.put("#idW", this.getId_mur());
        info_nouveau_post_video.put("Type_posts", "Video");

        nouveau_post.insertion_BDD_post(info_nouveau_post_video, moteur_requete); // insère le post dans la base de données
    
        
    }


}
 
    
    

