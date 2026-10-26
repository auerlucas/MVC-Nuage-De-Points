package fr.univlille.iut.s302.model.classification;

import fr.univlille.iut.s302.exception.classification.ClassificationException;
import fr.univlille.iut.s302.model.Data;
import fr.univlille.iut.s302.model.DataSet;


/**
 * Stratégie de classification, attribue une catégorie à une Data selon un DataSet existant.
 */
public interface Classification{

    /**
     * Classifie la Data donnée en lui attribuant une catégorie.
     * @param d Data à classifier
     * @param ds DataSet de référence comportant les catégories disponibles.
     * @throws ClassificationException si le DataSet ne contient pas de catégorie.
     */
    public void classify(Data d , DataSet ds) throws ClassificationException;
    }

