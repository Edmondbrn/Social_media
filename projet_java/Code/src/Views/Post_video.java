package Views;



import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Post_video extends Post_photo{

    private String duree = new String();
    private String video = new String();

    public Post_video(){
        super();
        this.duree = "";
        this.video = "";
    }


    public String getDuree(){
        return this.duree;
    }

    public String getVideo(){
        return this.video;
    }

    public void setVideo(String nouveau_url){
        this.video = nouveau_url;
    }

    public void setDuree(String nouvelle_duree){
        this.duree = nouvelle_duree;
    }
   
    public void Mise_en_forme_Video(Media une_video, VBox vbox_mise_en_forme_post, VBox vbox_post, HBox hbox_boutons_action_post, Label label_like, Label label_nom ,Label label_date){
        /*
         * Méthode qui met en forme une vidéo avec le bouton play et stop et les boutons d'actions classiques
         */
        
        MediaPlayer mediaPlayer = new MediaPlayer(une_video);
        MediaView mediaView = new MediaView(mediaPlayer);
        HBox hbox_video = new HBox(15);
        hbox_video.setAlignment(Pos.CENTER);
        mediaView.setFitWidth(1200); // Règle la largeur de la vidéo à 600 pixels
        mediaView.setFitHeight(720); // Règle la hauteur de la vidéo à 400 pixels
        // Créez un bouton Play
        Button playButton = new Button("Play");
        playButton.setOnAction(e -> mediaPlayer.play());

        // Créez un bouton Stop
        Button stopButton = new Button("Stop");
        stopButton.setOnAction(e -> mediaPlayer.pause());

        // Créez un slider pour afficher la progression de la vidéo
        Slider slider = new Slider();
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(newValue.toSeconds());
        });
        mediaPlayer.totalDurationProperty().addListener((observable, oldValue, newValue) -> {
            slider.setMax(newValue.toSeconds());
        });
        slider.setOnMousePressed(e -> mediaPlayer.seek(javafx.util.Duration.seconds(slider.getValue()))); // Permet de se déplacer dans la vidéo
        // Ajoutez le MediaView et les boutons à votre VBox
        hbox_video.getChildren().addAll(playButton, stopButton, slider);
        vbox_mise_en_forme_post.getChildren().addAll(mediaView, hbox_video, hbox_boutons_action_post, label_like, label_nom ,label_date);
        vbox_post.getChildren().add(vbox_mise_en_forme_post);
    }

    public Media TraitementVideo(String texte, Label texte_du_post, String id_user, String id_post, String date_post, int nombre_like, VBox vbox_post){
        /*
         * Methode qui se charge de créer la video et de la mettre en forme en pixels
         */
        // Cette requete permet de récupérer les url des vidéos en supposant que le titre de la vidéo (texte) est unique (ce qui est le cas dans notre cas)
        String requete_sql_bis = new String("SELECT * FROM POSTS where POSTS.texte = '" + texte + "' ;");
        this.setVideo(moteur_de_Requete.parcoursTableSQL(requete_sql_bis, "urlVID").get(0));
        File file = new File(this.getVideo());
        Media media = new Media(file.toURI().toString());
        this.gestionTitreImage(texte_du_post, vbox_post); // Gère le titre de la vidéo
        return media;
   }

}
