package fr.univlille.iut.s302.model.classification;

import fr.univlille.iut.s302.exception.classification.ClassificationException;
import fr.univlille.iut.s302.model.DataSet;
import org.junit.jupiter.api.BeforeEach;
import fr.univlille.iut.s302.model.Data;
import org.junit.jupiter.api.Test;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe de test de ClassificationAleatoire.
 */
public class RandomClassificationTest {
    private RandomClassification classif ;

    @BeforeEach
    void firstly(){
        classif = new RandomClassification();
    }

    private Data nouvelleData(){
        return new Data(Map.of("x",1.0),null);
    }


    private DataSet dataSetAvec(String... categorie){
        DataSet ds = new DataSet(classif);
        ds.getCategories().addAll(Arrays.asList(categorie));
        return ds;
    }

    @Test
    void categorieAttribueeAppartientAuDataSet() throws ClassificationException {
        DataSet ds = dataSetAvec("c1","c2","c3");
        Data d = nouvelleData();

        classif.classify(d,ds);
        assertTrue(ds.getCategories().contains(d.getCategory()));
    }

    @Test
    void exceptionSiAucuneCategorie(){
        DataSet ds = dataSetAvec();
        Data d = nouvelleData() ;

        assertThrows(ClassificationException.class, () -> classif.classify(d,ds));
        assertNull(d.getCategory());
    }

    @Test
    void uneSeuleCategorieEstToujoursChoisie() throws ClassificationException {
        DataSet ds = dataSetAvec("seule");
        Data d = nouvelleData() ;
        classif.classify(d,ds);
        assertEquals("seule",d.getCategory());
    }

    @Test
    void ajouterEtClassifySurDataSetVideNAjoutePasLaData() {
        DataSet ds = dataSetAvec();
        Data d = nouvelleData();

        assertThrows(ClassificationException.class, () -> ds.addAndClassify(d));
        assertTrue(ds.getDatas().isEmpty());
    }


    private static class FakeRandom extends Random{
        private final int valeur ;
        int dernierBound = -1;


        FakeRandom(int valeurs){
            this.valeur = valeurs ;
        }

        @Override
        public int nextInt(int bound){
            dernierBound = bound ;
            return valeur ;
        }

    }

    @Test
    void indexInjecteChoisitLaCategorieCorrespondante() throws ClassificationException {
        DataSet ds = dataSetAvec("c1", "c2", "c3");
        List<String> categories = new ArrayList<>(ds.getCategories());
        RandomClassification fake = new RandomClassification(new FakeRandom(1));
        Data d = nouvelleData();

        fake.classify(d, ds);

        assertEquals(categories.get(1), d.getCategory());
    }

    @Test
    void premiereCategorieQuandRandomRenvoieZero() throws ClassificationException {
        DataSet ds = dataSetAvec("c1", "c2", "c3");
        List<String> categories = new ArrayList<>(ds.getCategories());
        RandomClassification fake = new RandomClassification(new FakeRandom(0));
        Data d = nouvelleData();
        fake.classify(d, ds);
        assertEquals(categories.get(0), d.getCategory());
    }

    @Test
    void derniereCategorieQuandRandomRenvoieLIndexMax() throws ClassificationException {
        DataSet ds = dataSetAvec("c1", "c2", "c3");
        List<String> categories = new ArrayList<>(ds.getCategories());
        RandomClassification fake = new RandomClassification(new FakeRandom(2));
        Data d = nouvelleData();
        fake.classify(d, ds);
        assertEquals(categories.get(2), d.getCategory());
    }

    @Test
    void nextIntRecoitLeNombreDeCategories() throws ClassificationException {
        DataSet ds = dataSetAvec("c1", "c2", "c3", "c4");
        FakeRandom fakeRandom = new FakeRandom(0);
        RandomClassification fake = new RandomClassification(fakeRandom);
        fake.classify(nouvelleData(), ds);
        assertEquals(4, fakeRandom.dernierBound);
    }
}
