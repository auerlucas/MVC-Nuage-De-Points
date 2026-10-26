package fr.univlille.iut.s302.model;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Cette classe représente un point, des valeurs numériques et une catégorie optionnelle
 * @author Oussama HOUAIDJ / Kenan YUMER
 */
public class Data {
    private Map<String, Double> values;
    private String category;
    private boolean isAdded;

    public Data(Map<String, Double> values, String category){
        this.values = new LinkedHashMap<>(values) ;
        this.category = category;
        this.isAdded = false;
    }

    public Data(Map<String, Double> values){
        this(values, null);
    }

    public Double getValues(String attribute){
        return this.values.get(attribute);
    }

    public Map<String, Double> getValues(){
        return this.values;
    }

    public String getCategory(){
        return this.category;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setCategory(String category){this.category = category;}


    /**
     * Change la valeur de isAdded avec le paramètre donnée
     * @param added
     */
    public void setAdded(boolean added){ this.isAdded = added ;}

    @Override
    public String toString(){
        return "Data{valeurs=" + this.values + ", categorie=" + this.category + ", isAdded=" + this.isAdded + "}";
    }

    /**
     * Vérifie qu'une data est classifié ou non.
     * @return true si il a une categorie (donc classifié)
     */
    public boolean isClassified() {
        if(this.category == null){
            return false;
        }
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return Objects.equals(getValues(), data.getValues()) && Objects.equals(getCategory(), data.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValues(), getCategory());
    }


}
