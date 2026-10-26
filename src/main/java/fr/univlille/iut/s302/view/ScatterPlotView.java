package fr.univlille.iut.s302.view;

import fr.univlille.iut.s302.controller.DataSetController;
import fr.univlille.iut.s302.model.Data;
import fr.univlille.iut.s302.model.DataSetObserver;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import java.util.Map;
import java.util.LinkedHashMap;
import javafx.scene.chart.XYChart;

/**
 * Composant graphique qui affiche le nuage de points et gère les couleurs et formes par catégorie
 * @author Lucas AUER / Kenan YUMER / Adam Ternoy / Issa LE GALL
 */
public class ScatterPlotView extends BorderPane implements DataSetObserver {
    private static final String[] COLORS = {
            "#1f77b4", "#ff7f0e", "#2ca02c", "#d62728",
            "#9467bd", "#8c564b", "#e377c2", "#7f7f7f"
    };
    private final Map<String, String> categroyColor = new LinkedHashMap<>();
    private int nextColorIndex = 0;
    private static final String[] SHAPES = {
            null, // cercle
            "M0,0 H10 V10 H0 Z", // carré
            "M5,0 L10,10 L0,10 Z", // triangle
            "M5,0 L10,5 L5,10 L0,5 Z" // losange
    };
    private final Map<String, String> categoryShape = new LinkedHashMap<>();
    private int nextShapeIndex = 0;

    protected String xAttribute;
    protected String yAttribute;
    protected DataSetController controller;
    protected ScatterChart<Number, Number> scatterChart;

    /**
     * Initialise le nuage de points avec ses axes et l'associe au contrôleur
     * @param xAttribute l'attribut affiché en abscisse
     * @param yAttribute l'attribut affiché en ordonnée
     * @param controller le contrôleur des données
     */
    public ScatterPlotView(String xAttribute, String yAttribute, DataSetController controller){
        this.xAttribute = xAttribute;
        this.yAttribute = yAttribute;
        this.controller = controller;

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        this.scatterChart = new ScatterChart<>(xAxis, yAxis);
        this.scatterChart.setTitle("Graphe de dispersion");

        this.setCenter(this.scatterChart);
        this.controller.addObserver(this);
    }

    /**
     * Met à jour l'affichage du graphique avec la méthode refresh
     */
    @Override
    public void update(){
        refresh();
    }


    /**
     * Modifie l'attribut affiché en abscisse et met à jour le graphique
     * @param xAttribute l'attribut affiché en abscisse
     */
    public void setXAttribute(String xAttribute) {
        this.xAttribute = xAttribute;
        this.scatterChart.getXAxis().setLabel(xAttribute);
        refresh();
    }

    /**
     * Modifie l'attribut affiché en ordonnée et met à jour le graphique
     * @param yAttribute l'attribut affiché en ordonnée
     */
    public void setYAttribute(String yAttribute) {
        this.yAttribute = yAttribute;
        this.scatterChart.getYAxis().setLabel(yAttribute);
        refresh();
    }

    /**
     * Attribue ou récupère la couleur associée à une catégorie
     * @param categorie le nom de la catégorie
     * @return le code couleur hexadécimal
     */
    private String colorFor(String categorie){
        String color = this.categroyColor.get(categorie);
        if(color == null) {
            color = this.COLORS[nextColorIndex % COLORS.length];
            this.categroyColor.put(categorie, color);
            this.nextColorIndex++;
        }
        return color;
    }

    /**
     * Attribue ou récupère la forme associée à une catégorie
     * @param categorie le nom de la catégorie
     * @return la chaîne de caractères CSS de la forme
     */
    private String shapeFor(String categorie){
        if(!this.categoryShape.containsKey(categorie)){
            String shape = this.SHAPES[this.nextShapeIndex % this.SHAPES.length];
            this.categoryShape.put(categorie, shape);
            this.nextShapeIndex++;
        }
        return this.categoryShape.get(categorie);
    }

    /**
     * Applique les couleurs et les formes personnalisées sur la légende du graphique
     */
    private void styleLegend(){
        Platform.runLater(() -> {
            for (Node legendItem : scatterChart.lookupAll(".chart-legend-item")) {
                if (legendItem instanceof Label) {
                    Label label = (Label) legendItem;
                    String cat = label.getText();

                    String color = categroyColor.get(cat);
                    if (color == null) continue; // sécurité si jamais ça matche rien

                    String shape = categoryShape.get(cat);
                    String style = "-fx-background-color: " + color + ";";
                    if (shape != null) {
                        style += " -fx-shape: \"" + shape + "\";";
                    }

                    Node symbol = label.getGraphic();
                    if (symbol != null) {
                        symbol.setStyle(style);
                    }
                }
            }
        });
    }

    /**
     * Recharge toutes les données sur le graphique avec leurs couleurs et forme
     */
    public void refresh() {
        scatterChart.getData().clear();

        Map<String, XYChart.Series<Number, Number>> seriesMap = new LinkedHashMap<>();
        Map<XYChart.Data<Number, Number>, Data> pointsData = new LinkedHashMap<>();

        for (Data d : controller.getDatas()) {
            if(d.isClassified()){
                Double x = d.getValues(xAttribute);
                Double y = d.getValues(yAttribute);
                if (x == null || y == null) continue;

                String cat = d.getCategory();

                XYChart.Series<Number, Number> series = seriesMap.get(cat);
                if (series == null) {
                    series = new XYChart.Series<>();
                    series.setName(cat);
                    seriesMap.put(cat, series);
                }

                XYChart.Data<Number, Number> point =new XYChart.Data<>(x, y);
                series.getData().add(point);
                pointsData.put(point, d);
            }
        }

        scatterChart.getData().addAll(seriesMap.values());

        for(Map.Entry<XYChart.Data<Number, Number>, Data> entry : pointsData.entrySet()){
            XYChart.Data<Number, Number> point = entry.getKey();
            Data d = entry.getValue();
            if(point.getNode() != null){
                String shape = shapeFor(d.getCategory());
                String color = colorFor(d.getCategory());

                String style = "-fx-background-color: " + color + ";";
                if(shape != null){
                    style += "-fx-shape: \"" + shape + "\";";
                }
                if(d.isAdded()){
                    style += "-fx-padding: 9px;";
                }
                point.getNode().setStyle(style);
                point.getNode().setOnMouseClicked(e -> showDetails(d));
            }
        }
        styleLegend();
    }

    /**
     * Affiche une fenêtre avec les détails complets d'un point
     * @param d l'objet Data dont on veut afficher les détails
     */
    private void showDetails(Data d){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détail du point");
        if(d.isClassified()){
            alert.setHeaderText(d.getCategory());
        } else{
            alert.setHeaderText("Non classifié");
        }
        String res = "";
        for (Map.Entry<String, Double> entry : d.getValues().entrySet()){
            res += entry.getKey() + " : " + entry.getValue() + "\n";
        }
        alert.setContentText(res);
        alert.show();
    }
}