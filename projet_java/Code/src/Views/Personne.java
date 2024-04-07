package Views;

import javafx.beans.property.SimpleStringProperty;

public class Personne {
    /*Classe qui crée un objet personne pour les listes bloqués et de followers
     */
    private final SimpleStringProperty nom;

    public Personne(String nom) {
        this.nom = new SimpleStringProperty(nom); // type spécique aux listes de javafx
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }
}