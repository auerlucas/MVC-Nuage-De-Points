package fr.univlille.iut.s302.model;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iut.s302.exception.classification.ClassificationException;

public class DataSetTest{

    private DataSet dataSet;
    private Map<String, Double> valeurs;

    @BeforeEach
    void firstly(){
        dataSet = new DataSet();
        valeurs = Map.of("A", 5.1, "B", 13.5);
    }

    @Test
    void constructeurCheck(){
        assertNotNull(dataSet.getAttributeNames());
        assertNotNull(dataSet.getCategories());
        assertNotNull(dataSet.getDatas());
        assertNotNull(dataSet.getObservers());
        assertTrue(dataSet.getAttributeNames().isEmpty());
        assertTrue(dataSet.getCategories().isEmpty());
        assertTrue(dataSet.getDatas().isEmpty());
        assertTrue(dataSet.getObservers().isEmpty());
    }

    @Test
    void ajoutDataCheck(){
        Data d = new Data(valeurs, "Animal");
        dataSet.addData(d);
        assertEquals(1, dataSet.getDatas().size());
        assertTrue(dataSet.getDatas().contains(d));
        assertTrue(d.isAdded());
    }

    @Test
    void removeData(){
        Data d = new Data(valeurs, "Animal");
        dataSet.addData(d);
        dataSet.removeData(d);
        assertFalse(dataSet.getDatas().contains(d));
        assertTrue(dataSet.getDatas().isEmpty());
    }

    @Test
    void removeDataInexistant(){
        Data d = new Data(valeurs, "Animal");
        dataSet.removeData(d);
        assertTrue(dataSet.getDatas().isEmpty());
    }

    @Test
    void removeAddedDataCheck(){
        Data d1 = new Data(valeurs, "Animal");
        Data d2 = new Data(valeurs, "Voiture");

        dataSet.addData(d1);
        d2.setAdded(false);
        // d2 n'est pas encore dans le DataSet.
        dataSet.getDatas().add(d2);
        dataSet.removeAddedData();
        assertFalse(dataSet.getDatas().contains(d1));
        assertTrue(dataSet.getDatas().contains(d2));
        assertEquals(1, dataSet.getDatas().size());
    }

    @Test
    void getNonClassifiedDatasCheck(){
        Data d1 = new Data(valeurs, "Animal");
        Data d2 = new Data(valeurs, null);
        Data d3 = new Data(valeurs, null);
        dataSet.addData(d1);
        dataSet.addData(d2);
        dataSet.addData(d3);

        assertEquals(2, dataSet.getUnclassifiedDatas().size());
        assertTrue(dataSet.getUnclassifiedDatas().contains(d2));
        assertTrue(dataSet.getUnclassifiedDatas().contains(d3));
        assertFalse(dataSet.getUnclassifiedDatas().contains(d1));
    }

    @Test
    void addObservateurCheck(){
        DataSetObserver observateur = () -> {}; // Crée une instance d'une classe qui implémente l'interface
        dataSet.addObserver(observateur);
        assertEquals(1, dataSet.getObservers().size());
        assertTrue(dataSet.getObservers().contains(observateur));
    }

    @Test
    void removeObservateurCheck(){
        DataSetObserver observateur = () -> {};
        dataSet.addObserver(observateur);
        dataSet.removeObserver(observateur);
        assertTrue(dataSet.getObservers().isEmpty());
        assertFalse(dataSet.getObservers().contains(observateur));
    }

    @Test //on verife le notifyObserver et qu'il est bien appelé
    void notificationObservateurCheck() {
        AtomicInteger compteur = new AtomicInteger();
        DataSetObserver observateur = compteur::incrementAndGet; //pour chaque appel au notify on incremente
        dataSet.addObserver(observateur);
        dataSet.addData(new Data(valeurs, "Animal"));
        assertEquals(1, compteur.get());
    }

    @Test
    void addAndClassifyCheck() throws ClassificationException {
        dataSet.getCategories().add("Animal");
        Data d = new Data(valeurs);
        boolean resultat = dataSet.addAndClassify(d);
        assertTrue(dataSet.getDatas().contains(d));
        assertTrue(d.isAdded());
        assertTrue(resultat);
    }
}
