package fr.univlille.iut.s302.exception.csvReader;

/**
 * Exception levée lorsqu'un fichier CSV ne peut pas être lu :
 * fichier introuvable, vide ou mal formé (nombre de colonnes incorrect, valeur non numérique).
 * @author Adam Ternoy
 */
public class CSVFormatException extends Exception{

    /**
     * Crée une exception de format CSV.
     * @param message description de l'erreur
     */
    public CSVFormatException(String message){
        super(message);
    }

    /**
     * Crée une exception de format CSV avec la cause d'origine.
     * @param message description de l'erreur
     * @param cause   exception à l'origine de l'erreur (par exemple
     *                {@link java.io.FileNotFoundException} ou {@link NumberFormatException})
     */
    public CSVFormatException(String message , Throwable cause){
        super(message,cause);
    }
}
