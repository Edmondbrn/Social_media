package Views;



import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;


public class viewSignin extends StackPane{
    /*
     * Classe qui permet de créer la vue de la page d'inscription elle hérite de stackpane pour pouvoir empiler des éléments de vues
     */
        // Définition des variables
    

        // Définitions des éléments de la page
        
        private Label titre_fenetre = new Label("Inscription");
        
        private Label Titre_nom = new Label("Entrer votre nom");
        public TextField slot_nom = new TextField();

        private Label Titre_prenom = new Label("Entrer votre prénom");
        public TextField slot_prenom = new TextField();

        private Label Titre_anniverssaire = new Label("Entrer votre date d'anniversaire");
        public DatePicker date_anniversaire = new DatePicker();
    
        private Label titre_mdp = new Label("Entrer votre mot de passe");
        public PasswordField slot_mdp = new PasswordField();

        private Label titre_password2 = new Label("Entrer votre mot de passe une nouvelle fois");
        public PasswordField slot_password2 = new PasswordField();

        private Label titre_mur = new Label("Entrer une brève description de votre mur");
        public TextField slot_mur = new TextField();

        private Label photo_profil = new Label("Choisir une photo de profil (optionnel)");
        public Button btn_photo_profil = new Button("Choisir un fichier");

        private Rectangle rectangle = new Rectangle (550, 550);

        public Button btn_valider_inscription = new Button("Valider");
        public Button bouton_retour_inscription = new Button("Retour");

        private VBox vbox = new VBox(10);
        private ObservableList<Node> components = this.getChildren(); 
        private File file = new File("projet_java/Code/ressources/Image/background.png");
        private String absolutePath = file.getAbsolutePath();
        private ImageView backgroundImage = new ImageView(new Image("file:///" + absolutePath));



        public viewSignin(Stage un_autrestage) {
            super(); 
            backgroundImage.fitWidthProperty().bind(this.widthProperty()); // Règle les dimensions de l'image
            backgroundImage.fitHeightProperty().bind(this.heightProperty());
            // Fixe la taille de la police de caractère
            titre_fenetre.setFont(Font.font(20));
    
            titre_mdp.setFont(Font.font(15));

            titre_password2.setFont(Font.font(15));
            titre_mur.setFont(Font.font(15));
            photo_profil.setFont(Font.font(15));
    
            slot_mdp.setMaxWidth(400);
            slot_password2.setMaxWidth(400);
    
      
    
            Titre_nom.setFont(Font.font(15));
            Titre_prenom.setFont(Font.font(15));

            slot_nom.setMaxWidth(400); // Défini la longueur de l'espace dédié au champs
            slot_prenom.setMaxWidth(400);
            slot_mur.setMaxWidth(400);

            Titre_anniverssaire.setFont(Font.font(15));
            
            btn_valider_inscription.setMaxWidth(100);
            bouton_retour_inscription.setMaxWidth(120);
            
            rectangle.setFill(Color.web("white", 0.7));  // Définit la couleur du rectangle en blanc avec 60% de transparence
            rectangle.setStroke(Color.web("black", 0.6)); // Définit la couleur de la bordure du rectangle en noir avec 60% de transparence
            rectangle.setArcHeight(50);
            rectangle.setArcWidth(50);
            this.setAlignment(Pos.CENTER);

            vbox.setAlignment(Pos.CENTER); // Alignez les éléments au centre verticalement

            // Ajout éléments à Vbox pour qu'ils soient alignés automatiquement
            vbox.getChildren().addAll(titre_fenetre, Titre_nom, slot_nom,Titre_prenom, slot_prenom, 
                                            titre_mdp, slot_mdp, titre_password2, slot_password2, Titre_anniverssaire , 
                                            date_anniversaire, titre_mur , slot_mur , photo_profil , btn_photo_profil  ,btn_valider_inscription, bouton_retour_inscription);
    
            components.addAll(backgroundImage, rectangle, vbox);
        }

       
}
