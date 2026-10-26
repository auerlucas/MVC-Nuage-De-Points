package fr.univlille.iut.s302.util;

import fr.univlille.iut.s302.exception.csvReader.CSVFormatException;
import fr.univlille.iut.s302.model.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * Outil de lecture brut de fichier CSV.
 * <p>
 * Format attendu : séparateur virgule, première ligne contenant les en-têtes,
 * dernière colonne contenant la catégorie. Toutes les autres colonnes doivent être numériques.
 * @author Adam Ternoy / Kenan YUMER
 */
public class CSVReader {


    private static final String SEPARATOR = ",";


    /**
     * Lit le contenu d'un fichier CSV brut pour le transformer en une liste d'objets {@link Data}.
     * Les lignes vides sont ignorées.
     * @param filename chemin du fichier
     * @return liste des {@link Data} lues, une par ligne non vide, dans l'ordre du fichier
     * @throws CSVFormatException si le fichier est introuvable ou vide, si une ligne n'a pas
     *                            le bon nombre de colonnes, si une valeur n'est pas numérique
     *                            ou si le fichier ne contient aucune donnée
     */
    public static List<Data> read(String filename) throws CSVFormatException
    {
        List<Data> list = new ArrayList<>();
        try(Scanner sc = new Scanner(new File(filename))){
            if(!sc.hasNextLine()){
                throw new CSVFormatException("Fichier vide.");
            }
            String[] headers = sc.nextLine().split(SEPARATOR,-1);
            int lineNumber = 1 ;
            while (sc.hasNextLine()) {
                lineNumber++;
                String lineStr = sc.nextLine();
                if(lineStr.isBlank()){
                    continue; // saute les lignes vide au lieu de les convertir en Data .
                }
                String[] lineParts = lineStr.split(SEPARATOR,-1);
                if (lineParts.length != headers.length) {
                    throw new CSVFormatException("Ligne " + lineNumber + " : "
                            + lineParts.length + " colonnes au lieu de " + headers.length);
                }
                Map<String, Double> values = new LinkedHashMap<>();
                for(int i = 0; i < lineParts.length -1; i++){
                    try {
                        values.put(headers[i], Double.parseDouble(lineParts[i].trim()));
                    }catch (NumberFormatException e){
                        throw new CSVFormatException("Ligne : "+lineNumber + " valeur non-numérique détectée.",e);
                    }
                }
                String category = lineParts[lineParts.length-1].trim();
                list.add(new Data(values,category));
            }
        }catch (FileNotFoundException e){
            throw new CSVFormatException(filename +" :Fichier introuvable.",e);
        }
        if(list.isEmpty()){
            throw new CSVFormatException("Aucune donnée dans le fichier.");
        }
        return list;
    }

    /**
     * Lit la ligne d'en-têtes d'un fichier CSV brut.
     * La dernière colonne (la catégorie) n'est pas retournée.
     * @param filename chemin du fichier
     * @return noms des attributs numériques, dans l'ordre du fichier
     * @throws CSVFormatException si le fichier est vide ou introuvable
     */
    public static List<String> readHeader(String filename) throws CSVFormatException{
        List<String> attribute = new ArrayList<>();
        try(Scanner sc = new Scanner(new File(filename))){
            if(!sc.hasNextLine()){
                throw new CSVFormatException("Fichier vide.");
            }
            String[] headers = sc.nextLine().split(SEPARATOR ,-1);
            for(int i = 0; i < headers.length - 1; i++){
                attribute.add(headers[i]);
            }
        } catch (FileNotFoundException e) {
            throw new CSVFormatException(filename +" :Fichier introuvable.",e);
        }
        return attribute;
    }
}