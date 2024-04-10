package Views;


import java.io.File;
import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;


public class Post_photo extends Post_texte{ // classe qui hérite de Post_texte car ils partagent des caractéristiques
    /*
     * 
     * Cette classe gère tout ce qui a un rapport avec le post de type Image (mise en forme, insertion dans la BDD etc..)
     */
    private String format_image = new String();
    private String image;
    
    // constructeur de la classe
    public Post_photo(){
        super();
        this.format_image = "png";
        this.image = "";

    }

    public String getFormat_image(){
        return this.format_image;
    }

    public String getImage(){
        return this.image;
    }

    public void setFormat_image(String nouveau_format){
        this.format_image = nouveau_format;
    }

    public void setImage(String nouveau_url){
        this.image = nouveau_url;
    }

 

    public ImageView TraitementImage(String texte , Label texte_du_post, String id_user, String id_post, String date_post, int nombre_like,  VBox vbox_post, Requete moteur_de_requete){
        /*
        * Méthode qui permet de traiter les images
        */
        // Cette requete permet de récupérer les url des images en supposant que le titre de l'image (texte) est unique (ce qui est vrai dans notre cas)
        String requete_sql_bis = new String("SELECT * FROM POSTS WHERE POSTS.texte = '" + texte + "' ;");
        ArrayList<String> liste_URL_image =  moteur_de_requete.parcoursTableSQL(requete_sql_bis, "urlIMG");
        String URL_image = new String(liste_URL_image.get(0));
        ImageView image = new ImageView();
        if (URL_image.startsWith("http")) // pas besoin de traitement particulier si l'image est en ligne
            image = new ImageView(new Image(URL_image));
        else{
            File file = new File(URL_image);
            image = new ImageView(new Image(file.toURI().toString())); // récupère le chemin absolu pour toutes les machines différentes
        }
        image.setFitWidth(1200); // Règle les dimensions de l'image
        image.setFitHeight(720);
        image.setPreserveRatio(true); // Garde les proportions de l'image
        gestionTitreImage(texte_du_post, vbox_post); // Gère le titre de l'image
        return image;
     
}

    protected void gestionTitreImage(Label titre_image, VBox vbox_post){
        /*
        * Méthode qui gère la mise en forme du titre d'une image
        */
        titre_image.underlineProperty().set(true); // souligne le texte
        titre_image.setFont(Font.font("System", FontWeight.BOLD, 15)); // change la police d'écriture et la passe en gras
        vbox_post.getChildren().add(titre_image); // Ajoute le titre de l'image à la page

    }

    protected void gestionRetraitLayoutTitre(Label texte_du_titre, VBox vbox_post, TextArea champ_titre, Button bouton_valider_titre){
        /*
        * Méthode qui gère le retrait du layout du titre d'une image pour la remplacer par le vrai titre
        */
        texte_du_titre.setText(champ_titre.getText()); // Crée un texte à partir du champ de texte
        gestionTitreImage(texte_du_titre, vbox_post); // Gère le titre de l'image
        vbox_post.getChildren().remove(champ_titre); // Retire le champ de texte de la page
        vbox_post.getChildren().remove(bouton_valider_titre); // Supprime le bouton valider de la page
        
    
}

  
}
