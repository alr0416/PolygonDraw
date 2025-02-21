package ressler.polygondraw;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.scene.paint.Color.BLACK;

public class PollygonController {

    boolean canDrawPoints = false;

    boolean isThin = true;


    @FXML
    private Pane drawPane;

    @FXML
    private Button clearButton;

    @FXML
    private ComboBox<String> colorSelecter;

    @FXML
    private Button deleteButton;

    @FXML
    private Button drawButton;

    @FXML
    private RadioButton thickButton;

    @FXML
    private RadioButton thinButton;

    @FXML
    private ToggleGroup thicknessGroup;

    Polygon currentPolygon = null;
    Polygon selectedPolygon = null;

    boolean canSelectPolygon = true;

    private double dragDeltaX;
    private double dragDeltaY;

    Color currentColor;



    @FXML
    public void initialize() {

        drawPane.setOnMouseClicked(this::panePointClicked);
        setButtons();

        colorSelecter.getItems().addAll("White", "Green", "Blue", "Yellow");

        currentColor = Color.WHITE;

        // Set an event handler for when a color is selected
        colorSelecter.setOnAction(event -> handleColorSelection());
    }

    private void handleColorSelection() {
        //Color selectedColor = (Color) colorSelecter.getValue();
        String selectedColor = colorSelecter.getValue();

        if (selectedColor == "White") {
            currentColor = Color.WHITE;
        }
        else if (selectedColor == "Green") {
            currentColor = Color.GREEN;
        }
        else if (selectedColor == "Blue") {
            currentColor = Color.BLUE;
        }
        else if (selectedColor == "Yellow") {
            currentColor = Color.YELLOW;
        }
    }

    private void polygonMousePressed(MouseEvent event) {
        if (selectedPolygon != null) {
            dragDeltaX = event.getSceneX() - selectedPolygon.getTranslateX();
            dragDeltaY = event.getSceneY() - selectedPolygon.getTranslateY();
        }
    }

    private void polygonMouseDragged(MouseEvent event) {

        if (selectedPolygon != null) {

            double newX = event.getSceneX() - dragDeltaX;
            double newY = event.getSceneY() - dragDeltaY;

            if (newX >= -selectedPolygon.getBoundsInLocal().getMinX() && newX <= 900 - selectedPolygon.getBoundsInLocal().getMaxX()) {
                selectedPolygon.setTranslateX(newX);
            }

            if (newY >= -selectedPolygon.getBoundsInLocal().getMinY() && newY <= 600 - selectedPolygon.getBoundsInLocal().getMaxY()) {
                selectedPolygon.setTranslateY(newY);
            }

            System.out.println("currrentx: " + newX);
            System.out.println("currrenty: " + newY);
        }
    }

    private void polygonMouseReleased(MouseEvent event) {
        // Additional logic after releasing the mouse button (if needed)
    }


    @FXML
    void clearButtonClicked(ActionEvent event) {
        drawPane.getChildren().clear();
    }

    @FXML
    void colorSelecterClicked(ActionEvent event) {

    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {

        if (selectedPolygon != null) {
            drawPane.getChildren().remove(selectedPolygon);
            selectedPolygon = null;
        }
        setButtons();
    }

    @FXML
    void drawButtonClicked(ActionEvent event) {

        canDrawPoints = true;
        canSelectPolygon = false;

        disableButtons();


        currentPolygon = new Polygon();

        if (isThin) {
            currentPolygon.setStrokeWidth(2);
        } else {
            currentPolygon.setStrokeWidth(6);
        }

        currentPolygon.setStroke(Color.RED);
        currentPolygon.setFill(Color.TRANSPARENT);

    }


    @FXML
    void thickButtonClicked(ActionEvent event) {
        isThin = false;
    }

    @FXML
    void thinButtonClicked(ActionEvent event) {
        isThin = true;
    }

    @FXML
    void panePointClicked(MouseEvent event) {

        if (event.getClickCount() == 1 && canDrawPoints) {
            // Single click - add the point to the currentPolygon

            double x = event.getX();
            double y = event.getY();

            // Add the clicked point to the polygon
            currentPolygon.getPoints().addAll(x, y);

            // Update the pane with the modified polygon
            drawPane.getChildren().add(currentPolygon);

        }
        else if (event.getClickCount() == 2 && canDrawPoints) {
            // Double click - finish the polygon
            // You might want to close the polygon or take other actions here

            finishPolygon(event);



        }
    }

    public void finishPolygon(MouseEvent event) {

        currentPolygon.getPoints().addAll(currentPolygon.getPoints().get(0), currentPolygon.getPoints().get(1));
        System.out.print("Polygon Completed");

        Polygon completedPolygon = currentPolygon;
        currentPolygon.setStroke(BLACK);
        currentPolygon.setFill(currentColor);

        currentPolygon = null;

        currentPolygon = null;
        canDrawPoints = false;
        canSelectPolygon = true;

        setButtons();


        completedPolygon.setOnMouseClicked(this::polygonMouseClicked);
        completedPolygon.setOnMousePressed(this::polygonMousePressed);
        completedPolygon.setOnMouseDragged(this::polygonMouseDragged);
        completedPolygon.setOnMouseReleased(this::polygonMouseReleased);


    }


    void polygonMouseClicked(MouseEvent event) {


        // Get the selected polygon
        if (selectedPolygon == (Polygon) event.getSource()) {
            selectedPolygon.setStroke(BLACK);
            selectedPolygon = null;
            setButtons();
            return;

        }

        if (selectedPolygon != null) {
            selectedPolygon.setStroke(BLACK);
            selectedPolygon = null;
        }

        if (canSelectPolygon) {
            selectedPolygon = (Polygon) event.getSource();

            // Highlight the selected polygon
            selectedPolygon.setStroke(Color.RED);

            disableButtons();
            deleteButton.setDisable(false);
        }

    }

    public void disableButtons() {
        clearButton.setDisable(true);
        drawButton.setDisable(true);
        thickButton.setDisable(true);
        thinButton.setDisable(true);
        deleteButton.setDisable(true);
        colorSelecter.setDisable(true);

    }

    public void setButtons() {
        clearButton.setDisable(false);
        drawButton.setDisable(false);
        thickButton.setDisable(false);
        thinButton.setDisable(false);
        deleteButton.setDisable(true);
        colorSelecter.setDisable(false);

    }
    public void enableButtons() {
        clearButton.setDisable(false);
        drawButton.setDisable(false);
        thickButton.setDisable(false);
        thinButton.setDisable(false);
        deleteButton.setDisable(false);
        colorSelecter.setDisable(false);

    }




}








