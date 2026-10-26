package fr.univlille.iut.s302.util;

import fr.univlille.iut.s302.exception.csvReader.CSVFormatException;
import fr.univlille.iut.s302.model.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Classe de test de CSVReader.
 */
public class CSVReaderTest {
    @TempDir
    Path dir; //Génère un dossier temporaire pour un fichier csv de test.

    private String csv(String contenu) throws IOException{
        Path p = dir.resolve("test.csv");
        Files.writeString(p, contenu);
        return p.toString();
    }

    @Test
    void readCSVValide() throws Exception{
        String path = csv("x;y;classe\n1.0;2.0;A\n3.5;4.5;B\n");

        List<Data> datas = CSVReader.read(path);
        assertEquals(2,datas.size());
        assertEquals(1.0,datas.get(0).getValues("x"));
        assertEquals("A",datas.get(0).getCategory());
        assertEquals("B",datas.get(1).getCategory());
    }

    @Test
    void readIgnoreLesLignesVides() throws Exception{
        String path = csv("x;y;classe\n1.0;2.0;A\n\n3.0;4.0;B\n");
        assertEquals(2,CSVReader.read(path).size());
    }

    @Test
    void readFichierIntrouvable(){
        assertThrows(CSVFormatException.class , () -> CSVReader.read(dir.resolve("inexistant.csv").toString()));
    }

    @Test
    void readFichierVide() throws Exception{
        String path = csv("");
        assertThrows(CSVFormatException.class, () -> CSVReader.read(path));
    }

    @Test
    void readMauvaisNombresDeColonnes() throws Exception{
        String path = csv("x;y;classe\n1.0;2.0\n");
        assertThrows(CSVFormatException.class ,() -> CSVReader.read(path));
    }

    @Test
    void readValeurNonNumerique() throws Exception {
        String path = csv("x;y;classe\nabc;2.0;A\n");
        assertThrows(CSVFormatException.class, () -> CSVReader.read(path));
    }

    @Test
    void readEnTeteSansLaDerniereColonne() throws Exception {
        String path = csv("x;y;classe\n1;2;A\n");

        assertEquals(List.of("x", "y"), CSVReader.readHeader(path));
    }


}
