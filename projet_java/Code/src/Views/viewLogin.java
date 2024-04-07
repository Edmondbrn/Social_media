package Views;


import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

import java.io.File;

import javafx.collections.ObservableList;


public class viewLogin extends StackPane{
    /*
     * Classe qui permet de créer la vue de la page de connexion elle hérite de stackpane pour pouvoir empilé des éléments de vues
     */

    
    private VBox vbox = new VBox(10);
    private ObservableList<Node> components = this.getChildren(); 
    // gère les éléments de la page de connexion
    private Label titre_fenetre = new Label("Veuillez entrer vos identifiants pour vous connecter");
    private Label titre_mdp = new Label("Mot de passe");
    private Label titre_id = new Label("Identifiant");
    private Label titre_inscription = new Label("Pas encore inscrit ?");
    public PasswordField slot_mdp = new PasswordField();
    public TextField slot_id = new TextField();
    public Button btn_inscription = new Button("S'inscrire");
    public Button btn_valider = new Button("Valider");
    private File file = new File("projet_java/Code/ressources/Image/background.png");
    private String absolutePath = file.getAbsolutePath();
    private ImageView backgroundImage = new ImageView(new Image("file:///" + absolutePath));
    private Rectangle rectangle = new Rectangle (550, 300);
    
    public viewLogin(double spacing, Stage unStage) {
        super(); // constructeur de Stackpane
        
        backgroundImage.fitWidthProperty().bind(this.widthProperty()); // Règle les dimensions de l'image
        backgroundImage.fitHeightProperty().bind(this.heightProperty());
        
        // Règle la taille du texte, taille des champs etc...
        titre_fenetre.setFont(Font.font(20));
        
        titre_mdp.setFont(Font.font(15));
        
        slot_mdp.setMaxWidth(400);
        
        titre_id.setFont(Font.font(15));

        slot_id.setMaxWidth(400);

        titre_inscription.setFont(Font.font(15));

        btn_inscription.setMaxWidth(100);
        btn_valider.setMaxWidth(100);
        rectangle.setFill(Color.web("white", 0.7));  // Définit la couleur du rectangle en blanc avec 60% de transparence
        rectangle.setStroke(Color.web("black", 0.6)); // Définit la couleur de la bordure du rectangle en noir avec 60% de transparence
        rectangle.setArcHeight(50);
        rectangle.setArcWidth(50);
        this.setAlignment(Pos.CENTER); // Place le rectangle au centre de la fenêtre
        
        vbox.setAlignment(Pos.CENTER); // Alignez les éléments au centre verticalement

        // Ajout éléments à Vbox pour qu'ils soient alignés automatiquement
        vbox.getChildren().addAll(titre_fenetre, titre_id, slot_id, titre_mdp, slot_mdp, btn_valider, titre_inscription, btn_inscription);

        // Ajout image de fond et la VBox à la liste des éléments du StackPane (les empile)
        components.addAll(backgroundImage, rectangle, vbox);
           

    }


}
