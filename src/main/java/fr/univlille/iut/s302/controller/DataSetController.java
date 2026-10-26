package fr.univlille.iut.s302.controller;

import fr.univlille.iut.s302.exception.classification.ClassificationException;
import fr.univlille.iut.s302.exception.csvReader.CSVFormatException;
import fr.univlille.iut.s302.model.Data;
import fr.univlille.iut.s302.model.DataSet;
import fr.univlille.iut.s302.model.DataSetObserver;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe controller, celle qui est utilisée dans la vue pour ne pas impacter le model
 * @author Kenan YUMER
 */
public class DataSetController {
    private DataSet dataSet;

    public DataSetController(){
        this.dataSet = new DataSet();
    }

    public List<Data> getDatas(){ return this.dataSet.getDatas() ; }

    public List<Data> getUnclassifiedDatas() { return this.dataSet.getUnclassifiedDatas(); }

    public List<String> getAttributeNames(){ return this.dataSet.getAttributeNames(); }

    public Set<String> getCategories(){ return this.dataSet.getCategories(); }

    public void addObserver(DataSetObserver o){ this.dataSet.addObserver(o); }

    public void removeObserver(DataSetObserver o){ this.dataSet.removeObserver(o);  }

    /**
     * Charge un fichier CSV.
     * @param path le chemin du fichier CSV.
     * @throws CSVFormatException
     */
    public void loadFile(String path) throws CSVFormatException {
        this.dataSet.loadCSV(path);
    }

    /**
     * Ajoute et Classifie la value passé en paramètre
     * @param value une Map<String, Double>
     * @return true si elle est bien classifié
     * @throws ClassificationException
     */
    public boolean addAndClassify(Map<String, Double> value) throws ClassificationException {
        Data d = new Data(value);
        return this.dataSet.addAndClassify(d);
    }

    /**
     * Ajoute la value à la liste datas
     * @param value une Map<String, Double>
     * @return si la data a bien été ajouté
     */
    public boolean addPoint(Map<String, Double> value){
        this.dataSet.addData(new Data(value));
        return this.dataSet.getDatas().get(this.dataSet.getDatas().size() - 1).isAdded();
    }

    /**
     * Classifie la Data passé en paramètre
     * @param d un Data
     * @return si elle a bien été classifié
     * @throws ClassificationException
     */
    public boolean classify(Data d) throws ClassificationException {
        if(!d.isClassified()){
            return this.dataSet.classify(d);
        }
        return false;
    }
}
