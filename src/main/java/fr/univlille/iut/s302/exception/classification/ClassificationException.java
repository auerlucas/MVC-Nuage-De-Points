package fr.univlille.iut.s302.exception.classification;

/**
 * Exception levée lorsqu'une classification ne peut pas être effectuée.
 * @author Adam Ternoy
 */
public class ClassificationException extends Exception{


    /**
     * Crée une exception de classification.
     * @param message description de l'erreur
     */
    public ClassificationException(String message){
        super(message);
    }
}
