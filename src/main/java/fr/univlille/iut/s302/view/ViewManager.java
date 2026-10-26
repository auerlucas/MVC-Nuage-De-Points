package fr.univlille.iut.s302.view;

import fr.univlille.iut.s302.controller.DataSetController;
import fr.univlille.iut.s302.exception.classification.ClassificationException;
import fr.univlille.iut.s302.model.Data;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File ;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import fr.univlille.iut.s302.exception.csvReader.CSVFormatException;
import javafx.stage.FileChooser;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/**
 * Classe gestionnaire de la vue principale, responsable de l'interface et des fenêtres secondaires
 * @author Lucas AUER / Kenan YUMER / Adam Ternoy / Issa LE GALL
 */
public class ViewManager {
    private final DataSetController controller = new DataSetController();
    private final Map<Data, String> nomSymbolique = new LinkedHashMap<>();

    /**
     * Lance l'affichage de la fenêtre principale
     * @param stage la fenêtre principale
     */
    public void addView(Stage stage) {
        openWindow(stage);
    }

    /**
     * Crée les éléments de la fenêtre principale
     * @param stage la fenêtre à ouvrir
     */
    private void openWindow(Stage stage){
        ComboBox<String> choiceX = new ComboBox<>();
        ComboBox<String> choiceY = new ComboBox<>();

        BorderPane root = new BorderPane();
        ScatterPlotView scatterPlotView = new ScatterPlotView("X", "Y", this.controller);
        root.setCenter(scatterPlotView);

        defaultAxes(choiceX, choiceY, scatterPlotView);

        Button importButton = new Button("Importer jeu de donnée");
        importButton.setOnAction(e -> {
            File f = new FileChooser().showOpenDialog(stage);
            if (f == null) return;
            try {
                controller.loadFile(f.getAbsolutePath());
                defaultAxes(choiceX, choiceY, scatterPlotView);
            }catch (CSVFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de chargement");
                alert.setHeaderText("Fichier invalide");
                alert.setContentText(ex.getMessage());
                alert.show();
            }
        });

        Button classifyButton = new Button("Classifier les données");
        classifyButton.setOnAction(e -> openClassifyButton(stage));

        Button addButton = new Button("Ajouter une donnée");
        addButton.setOnAction(e -> openAddButton(stage));

        Button addViewButton = new Button("Nouvelle page");
        addViewButton.setOnAction(e -> openWindow(new Stage()));

        HBox topBar = new HBox(10,classifyButton,addButton,importButton, addViewButton);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(10));
        root.setTop(topBar);

        choiceX.setOnAction(e -> scatterPlotView.setXAttribute(choiceX.getValue()));
        choiceY.setOnAction(e -> scatterPlotView.setYAttribute(choiceY.getValue()));
        root.setLeft(choiceY);
        BorderPane.setAlignment(choiceY,Pos.TOP_LEFT);
        HBox bottomBar = new HBox(choiceX);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(10));
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 1000, 1000);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> this.controller.removeObserver(scatterPlotView));
    }

    /**
     * Configure les axes par défaut avec les premiers attributs disponibles
     * @param choiceX le menu déroulant de l'axe X
     * @param choiceY le menu déroulant de l'axe Y
     * @param view la vue du nuage de points
     */
    private void defaultAxes(ComboBox<String> choiceX, ComboBox<String> choiceY, ScatterPlotView view){
        List<String> attributes = this.controller.getAttributeNames();
        choiceX.getItems().setAll(attributes);
        choiceY.getItems().setAll(attributes);
        if(attributes.size() >= 2){
            choiceX.setValue(attributes.get(0));
            choiceY.setValue(attributes.get(1));
            view.setXAttribute(attributes.get(0));
            view.setYAttribute(attributes.get(1));
        }
    }

    /**
     * Ouvre la fenêtre pour classifier les données non classées
     * @param stage la fenêtre parente
     */
    private void openClassifyButton(Stage stage){
        Stage window = new Stage();
        window.initOwner(stage);
        window.setTitle("Classifier les données");

        VBox ligne = new VBox(10);
        Map<CheckBox, Data> toCheck = new LinkedHashMap<>();
        for(Data d : this.controller.getUnclassifiedDatas()){
            CheckBox cb = new CheckBox(label(d));
            toCheck.put(cb, d);
            ligne.getChildren().add(cb);
        }

        Label error = new Label();
        error.setStyle("-fx-text-fill: red");

        Button classerBtn = new Button("Classer les données");
        classerBtn.setOnAction(e -> {
            error.setText("");
            for(Map.Entry<CheckBox, Data> entry : toCheck.entrySet()){
                if(entry.getKey().isSelected()){
                    try {
                        this.controller.classify(entry.getValue());
                    } catch (ClassificationException ex) {
                        error.setText("Erreur de classification : " + ex.getMessage());
                    }
                }
            }
            if(error.getText().isEmpty()) window.close();
        });

        HBox button = new HBox(classerBtn);
        button.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, ligne,error, button);
        root.setPadding(new Insets(10));
        window.setScene(new Scene(root, 400, 400));
        window.show();
    }

    /**
     * Crée le texte descriptif d'une donnée pour les listes
     * @param d une donnée
     * @return la chaîne de caractères représentant la donnée
     */
    private String label(Data d){
        String res = "";
        String name = this.nomSymbolique.get(d);
        if(name != null && !name.isBlank()){
            res += name;
        }
        for(Map.Entry<String, Double> entry : d.getValues().entrySet()){
            res += entry.getKey() + " = " + entry.getValue() + " | ";
        }
        return res;
    }

    /**
     * Ouvre la fenêtre pour ajouter manuellement une nouvelle donnée
     * @param stage la fenêtre parente
     */
    private void openAddButton(Stage stage){
        Stage window = new Stage();
        window.initOwner(stage);
        window.setTitle("Ajouter une donnée");

        List<String> attributes = this.controller.getAttributeNames();

        GridPane grid = new GridPane(10, 5);

        TextField fieldName = new TextField();
        fieldName.setPromptText("Nom");
        grid.add(fieldName, 0, 0);

        ComboBox<String> attributesCombo = new ComboBox<>();
        attributesCombo.getItems().setAll(attributes);
        grid.add(attributesCombo, 1, 0);

        TextField valueField = new TextField();
        valueField.setPromptText("Valeur");
        grid.add(valueField, 2, 0);

        Button plusButton = new Button("+");
        grid.add(plusButton, 3, 0);

        Map<String, Double> enteredValues = new LinkedHashMap<>();

        Label error = new Label();
        error.setStyle("-fx-text-fill: red");

        plusButton.setOnAction(e -> {
            error.setText("");
            addAttribute(attributesCombo, valueField, grid, error, enteredValues);
        });

        Button addAndClassifyButton = new Button("Ajouter et classifier");
        addAndClassifyButton.setOnAction(e -> {
            error.setText("");
            if(!addAttribute(attributesCombo, valueField, grid, error, enteredValues)) return;
            try {
                this.controller.addAndClassify(enteredValues);
                window.close();
            } catch (ClassificationException ex) {
                error.setText("Erreur de classification : " + ex.getMessage());
            }
        });

        Button addOnlyButton = new Button("Ajouter seulement");
        addOnlyButton.setOnAction(e -> {
            error.setText("");
            if(!addAttribute(attributesCombo, valueField, grid, error, enteredValues)) return;
            this.controller.addPoint(enteredValues);
            List<Data> datas = this.controller.getDatas();
            Data last = datas.getLast();
            if(!fieldName.getText().isBlank()){
                this.nomSymbolique.put(last, fieldName.getText());
            }
            window.close();
        });

        HBox buttons = new HBox(10, addAndClassifyButton, addOnlyButton);
        VBox root = new VBox(10, grid, error, buttons);
        root.setPadding(new Insets(10));
        window.setScene(new Scene(root));
        window.show();
    }

    /**
     * Ajoute un attribut et sa valeur à la saisie
     * @param attributesCombo le menu des attributs
     * @param valueField le champ de saisie de la valeur
     * @param grid la grille d'affichage
     * @param error le label pour afficher les erreurs
     * @param enteredValues la map qui stocke les valeurs saisies
     * @return true si l'ajout a fonctionné et  false sinon
     */
    private boolean addAttribute(ComboBox<String> attributesCombo, TextField valueField, GridPane grid, Label error,
                                 Map<String, Double> enteredValues){
        String attribute = attributesCombo.getValue();
        if(attribute == null) {
            error.setText("Choisissez un attribut");
            return false;
        }
        try {
            double value = Double.parseDouble(valueField.getText().trim());
            int row = enteredValues.size() + 1;
            enteredValues.put(attribute, value);
            grid.add(new Label(attribute+ " : " + value), 1, row);
            attributesCombo.getItems().remove(attribute);
            attributesCombo.setValue(null);
            valueField.clear();
            return true;
        } catch (NumberFormatException ex){
            error.setText("Valeur invalide pour " + attribute);
            return false;
        }
    }
}