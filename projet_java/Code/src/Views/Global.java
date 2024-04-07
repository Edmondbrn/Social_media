package Views;


public class Global {
    private final String TITRE_FENETRE = new String("Projet Java GB4");
    private final double LARGEUR_FENETRE = 1200;
    private final double LONGUEUR_FENETRE = 720;
    private final int TAILLE_FIELD_TEXTE = 400;


    public String get_TITRE_FENETRE (){
        return this.TITRE_FENETRE;
    }

    public double get_LARGEUR_FENETRE(){
        return this.LARGEUR_FENETRE;
    }

    public double get_LONGUEUR_FENETRE (){
        return this.LONGUEUR_FENETRE;
    }

    public int get_TAILLE_FIELD_TEXTE(){
        return this.TAILLE_FIELD_TEXTE;
    }
}
