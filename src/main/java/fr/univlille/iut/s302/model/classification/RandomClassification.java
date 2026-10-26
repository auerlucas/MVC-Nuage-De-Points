package fr.univlille.iut.s302.model.classification;

import fr.univlille.iut.s302.exception.classification.ClassificationException;
import fr.univlille.iut.s302.model.Data;
import fr.univlille.iut.s302.model.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classifieur aléatoire : attribue à une {@link Data} une catégorie
 * tirée au hasard parmi celles du {@link DataSet}.
 * @author Adam Ternoy
 */
public class RandomClassification implements Classification {
    private final Random rand;


    /**
     * Crée un classifieur utilisant un générateur aléatoire par défaut.
     */
    public RandomClassification() {
        this(new Random());
    }

    /**
     * Crée un classifieur avec un générateur aléatoire fourni.
     * Utile pour les tests (graine fixe).
     * @param rand générateur aléatoire utilisé pour le tirage
     */
    public RandomClassification(Random rand) {
        this.rand = rand;
    }

    /**
     * Attribue à une Data une catégorie tirée au hasard parmi celles d'un DataSet.
     * La data passée en paramètre est modifiée via setCategory.
     * @param d  Data à classifier
     * @param ds DataSet fournissant les catégories possibles
     * @throws ClassificationException si ds ne contient aucune catégorie
     */
    @Override
    public void classify(Data d, DataSet ds) throws ClassificationException {
        List<String> categories = new ArrayList<>(ds.getCategories());
        if (categories.isEmpty()) {
            throw new ClassificationException("Aucune catégorie disponible.");
        }
        d.setCategory(categories.get(rand.nextInt(categories.size())));
    }
}

