package fr.univlille.iut.s302.model;

import fr.univlille.iut.s302.exception.classification.ClassificationException;
import fr.univlille.iut.s302.exception.csvReader.CSVFormatException;
import fr.univlille.iut.s302.model.classification.Classification;
import fr.univlille.iut.s302.model.classification.RandomClassification;
import fr.univlille.iut.s302.util.CSVReader;

import java.util.*;

/**
 * Classe contenant les Observers, les Data, les Catégories et les Attributs, elle sera la classe modèle observable
 * @author Oussama HOUAIDJ / Kenan YUMER
 */
public class DataSet {
    private List<String> attributeNames;
    private Set<String> categories;
    private List<Data> datas;
    private List<DataSetObserver> observers;
    private Classification classification ;




    public DataSet(Classification classification){
        this.attributeNames = new ArrayList<>();
        this.categories = new LinkedHashSet<>();
        this.datas = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.classification = classification ;
    }

    public DataSet(){
        this(new RandomClassification());
    }

    public List<String> getAttributeNames(){
        return this.attributeNames;
    }

    public Set<String> getCategories(){
        return this.categories;
    }

    public List<Data> getDatas(){
        return this.datas;
    }

    public List<DataSetObserver> getObservers(){
        return this.observers;
    }

    public void setClassification(Classification classification){
        this.classification = classification ;
    }

    /**
     * Charge le CSV et remplit les attributs de la classe
     * @param path chemin vers le fichier CSV
     * @throws CSVFormatException
     */
    public void loadCSV(String path) throws CSVFormatException {
        List<Data> newData = CSVReader.read(path);
        List<String> newAttributes = CSVReader.readHeader(path);
        Set<String> newCategories = new LinkedHashSet<>();

        for(Data d : newData){
            if(d.isClassified()){
                newCategories.add(d.getCategory());
            }
        }
        this.datas = new ArrayList<>(newData);
        this.attributeNames = newAttributes;
        this.categories = newCategories ;
        notifyObserver();
    }

    /**
     * Ajoute et Classifie la Data passé en paramètre au jeu de donnée
     * @param d un Data
     * @return true si elle est bien classifié
     * @throws ClassificationException
     */
    public boolean addAndClassify(Data d) throws ClassificationException {
        this.classification.classify(d,this);
        d.setAdded(true);
        this.datas.add(d);
        notifyObserver();
        return d.isClassified();
    }

    /**
     * Classifie la Data passé en paramètre
     * @param d un Data
     * @return true si elle est bien classifié
     */
    public boolean classify(Data d) throws ClassificationException {
        this.classification.classify(d, this);
        d.setAdded(true);
        notifyObserver();
        return d.isClassified();
    }

    /**
     * Ajoute la Data à la liste datas
     * @param d un Data
     */
    public void addData(Data d){
        d.setAdded(true);
        this.datas.add(d);
        notifyObserver();
    }

    /**
     * Supprime la Data de la liste datas
     * @param d un Data
     */
    public void removeData(Data d){
        if(datas.remove(d)){
            notifyObserver();
        }
    }

    /**
     * Supprime l'ensemble des points ajoutés par l'utilisateur.
     */
    public void removeAddedData() {
        this.datas.removeIf(Data::isAdded);
        notifyObserver();
    }

    /**
     * Récupère toutes les Data non classifié
     * @return Liste de Data
     */
    public List<Data> getUnclassifiedDatas(){
        List<Data> res = new ArrayList<>();
        for(Data d : this.datas){
            if (!d.isClassified()) res.add(d);
        }
        return res;
    }

    /**
     * Ajoute un Observateur à la liste observateurs
     * @param o un DataSetObserver
     */
    public void addObserver(DataSetObserver o){
        this.observers.add(o);
    }

    /**
     * Supprime un Observateur de la liste observateurs
     * @param o un DataSetObserver
     */
    public void removeObserver(DataSetObserver o){
        this.observers.remove(o);
    }

    /**
     * Notifie tout les observateurs de ses modifications
     */
    protected void notifyObserver(){
        for (DataSetObserver o : this.observers){
            o.update();
        }
    }
}
