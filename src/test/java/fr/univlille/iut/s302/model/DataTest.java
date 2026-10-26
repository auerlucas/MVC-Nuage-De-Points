package fr.univlille.iut.s302.model;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test de Data.
 */
public class DataTest {
        
    private Map<String, Double> valeurs;

    @BeforeEach
    void firstly(){
        valeurs = Map.of("A", 5.1, "B", 13.5, "C", 6.4, "D", 2.2);
    }
    
    @Test
    void constructeurAvecCategorie(){
        Data d = new Data(valeurs, "Animal");
        assertEquals("Animal", d.getCategory());
        assertEquals(5.1, d.getValues("A"));
        assertEquals(13.5, d.getValues("B"));
    }

    @Test
    void constructeurSansCategorie(){
        Data d = new Data(valeurs);
        assertNull(d.getCategory());
    }

    @Test
    void dataEstClassifie(){
        Data d = new Data(valeurs, "Animal");
        assertTrue(d.isClassified());
    }

    @Test
    void dataPasClassifie(){
        Data d = new Data(valeurs, null);
        assertFalse(d.isClassified());
    }

    @Test
    void getValuesSurAttributInexistantRenvoieNull(){
        Data d = new Data(valeurs, "Animal");
        assertNull(d.getValues("Z"));
    }

    @Test
    void dataPasAjoute() {
        Data d = new Data(valeurs, "Animal");
        assertFalse(d.isAdded());
    }

    @Test
    void setAddedCheck(){
        Data d = new Data(valeurs, "Animal");
        d.setAdded(true);
        assertTrue(d.isAdded());
    }

    @Test
    void setCategorieCheck(){
        Data d = new Data(valeurs,null);
        d.setCategory("Animal");
        assertEquals("Animal",d.getCategory());
        assertTrue(d.isClassified());
    }

    @Test
    void equalsCheck() {
        Data d1 = new Data(valeurs, "Animal");
        Data d2 = new Data(valeurs, "Animal");
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void pasEqualsCategorieCheck(){
        Data d1 = new Data(valeurs, "Animal");
        Data d2 = new Data(valeurs, "Voiture");
        assertNotEquals(d1, d2);
    }

    @Test
    void pasEqualsValeursCheck(){
        Data d1 = new Data(valeurs, "Voiture");
        Data d2 = new Data(Map.of("A", 1.0), "Voiture");
        assertNotEquals(d1, d2);
    }

    @Test
    void CopieMapConstructeur(){
        Map<String, Double> source = new HashMap<>();
        source.put("A", 1.0);
        Data d = new Data(source, "Voiture");
        source.put("B", 2.0); // modification externe après construction
        assertNull(d.getValues("B")); // la Data ne doit pas être affectée
    }
}
