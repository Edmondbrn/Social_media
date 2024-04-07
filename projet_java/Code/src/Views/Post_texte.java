package Views;


import java.util.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Post_texte {

    protected Requete moteur_requete = new Requete();
    protected String auteur = new String();
    protected String texte = new String();
    protected int nbr_like;
    protected ArrayList <String> liste_commentaire;
    protected Date date_post;
    protected DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT); // prepare la forme de la date jour/mois/année

    protected Requete moteur_de_Requete = new Requete();

    // Constructeur de base de l'objet Post_texte
    public Post_texte(){
        auteur = "";
        texte = "";
        nbr_like = 0;
        liste_commentaire = new ArrayList<>();
        date_post = new Date();

    }
    // Constructeur élaboré de l'objet Post_texte
    public Post_texte(String nom_auteur, String texte_du_post, int like, ArrayList<String> commentaire){
        auteur = nom_auteur;
        texte = texte_du_post;
        nbr_like = like;
        liste_commentaire = new ArrayList<String>(commentaire);
        date_post = new Date();
    }

    // Accesseur
    public String getAuteur(){
        return this.auteur;
    }

    public int getLike(){
        return this.nbr_like;
    }

    public ArrayList<String> getCommentaire(){
        return this.liste_commentaire;
    }

    public String getDate(){ // renvoie la date mise en forme 
        return shortDateFormat.format(this.date_post);
    }

    public String gettexte(){
        return this.texte;
    }
    

    // Mutateurs
    public void setAuteur(String name){
        this.auteur = name;
    }

    public void setLike_ajout(){
        this.nbr_like ++;
    }

    public void setLike_retrait(){
        this.nbr_like --;
    }

    public void setCommentaire(String un_commentaire){
        this.liste_commentaire.add(un_commentaire);
    }

    public void setCommentaire_retrait(String un_commentaire){
        this.liste_commentaire.remove(un_commentaire);
    }


    public String toString(){
        return "Auteur : " + this.auteur + " texte : " + this.texte + " Nombre de like : " + this.nbr_like + " Commentaire : " + this.liste_commentaire + " Date : " + getDate();
    }

    public void insertion_BDD_post(HashMap <String , Object> dico_info_post ){
        // On insère les informations du post dans la base de données

        String requete_SQL_insertion = "INSERT INTO POSTS (texte, format, urlIMG, urlVID, duree, dateC, dateM, \"#idU\", \"#idW\", Type_posts) VALUES ('" 
        + dico_info_post.get("texte") + "', '"
        + dico_info_post.get("format") + "', '"
        + dico_info_post.get("urlIMG") + "', '"
        + dico_info_post.get("urlVID") + "', '"
        + dico_info_post.get("duree") + "', '"
        + dico_info_post.get("dateC") + "', '"
        + dico_info_post.get("dateM") + "', '"
        + dico_info_post.get("#idU") + "', '"
        + dico_info_post.get("#idW") + "', '"
        + dico_info_post.get("Type_posts") + "');";

        moteur_de_Requete.insertion_sql(requete_SQL_insertion);

    
    }

    
 
}
