package com.example.lista5;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class ShapeEdit extends ShapeDraw {
    @FXML
    public ToggleButton edit;

    /**
     * Czy tryb edycji jest aktywny
     */
    protected boolean editMode = false;

    /**
     * Aktualnie zaznaczony kształt
     */
    protected Shaper selected;

    /**
     * Kolor figury przed zaznaczeniem
     */
    protected Color defColor;

//    protected double xdif;
//    protected double ydif;

    protected double xclick;
    protected double yclick;

    /**
     * Funkcja wywoływana przez aktywacje menu wyboru kształtu
     * Wyłącza tryb edycji
     */
    @Override
    public void setShape() {
        super.setShape();
        disableEditMode();
    }

    /**
     * Przełącza tryb edycji
     */
    @FXML
    protected void setEditMode() {
        if (!editMode) {
            enableEditMode();
        } else {
            disableEditMode();
        }
    }

    /**
     * Włącza tryb edycji i ustawia listenery do edycji figur
     */
    protected void enableEditMode() { //aktywuje tryb edycji i przypisuje figurom Listenery
        canvas.setOnMousePressed(this::selectShape);
        canvas.setOnMouseDragged(this::moveShape);
        canvas.setOnMouseReleased(null);
        canvas.setOnScroll(this::scaleShape);
        canvas.getScene().setOnKeyPressed(this::setControlMode);
        canvas.getScene().setOnKeyReleased(this::setControlMode);
        editMode = true;
        edit.setSelected(true);
    }

    /**
     * Wyłącza tryb edycji i przywraca domyślne listenery
     */
    protected void disableEditMode() {
        canvas.setOnMousePressed(this::drawStart);
        canvas.setOnMouseDragged(this::drawDraw);
        canvas.setOnMouseReleased(this::drawEnd);
        if (selected != null) {
            selected.getShape().setFill(defColor);
        }
        selected = null;
        defColor = null;
        editMode = false;
        edit.setSelected(false);
    }

    /**
     * Sprawdza, czy jest wciśnięty klawisz control, jeśli tak, zmienia zachowanie scrolla (ze skalowania na obrót)
     * @param keyEvent Event wciśnięcia klawisza
     */
    protected void setControlMode(KeyEvent keyEvent) {
        if (keyEvent.isControlDown()) {
            canvas.setOnScroll(this::rotateShape);
        } else {
            canvas.setOnScroll(this::scaleShape);
        }
    }

    /**
     * Po kliknięciu przywraca poprzedni kształt <code>selected</code> do jego pierwotnego wyglądu (jeśli istnieje).
     * Następnie sprawdza, czy kliknięto kształt, jeśli tak, ustawia go do zmiennej <code>selected</code> i zmienia kolor na niebieski
     * @param mouseEvent Event kliknięcia myszy
     */
    public void selectShape(MouseEvent mouseEvent) {
        if (mouseEvent.getTarget() instanceof Shaper) {
            if (selected != mouseEvent.getTarget() && selected != null){
                selected.getShape().setFill(defColor);
            }
            selected = (Shaper) mouseEvent.getTarget();
            defColor = (Color) selected.getShape().getFill();
            selected.getShape().setFill(Color.BLUE);
            selected.getShape().toFront();

//            xdif = mouseEvent.getX() - selected.getAnchorX();
//            ydif = mouseEvent.getY() - selected.getAnchorY();

            xclick = mouseEvent.getX();
            yclick = mouseEvent.getY();
        }else {
            if (selected != null) {
                selected.getShape().setFill(defColor);
            }
            selected = null;
            defColor = null;
        }
    }

    /**
     * Przesuwa kształt <code>selected</code> o wielkość przesunięcia myszy od kliknięcia, lub ostatniego wywołania funkcji
     * @param mouseEvent Event przesunięcia myszy
     */
    public void moveShape(MouseEvent mouseEvent) { //poruszenie myszką -> zmienia położenie kształtu
        if (mouseEvent.getTarget() instanceof Shaper  && selected != null) {
            Translate translation = selected.getTranslation();
            translation.setX(translation.getX() + mouseEvent.getX() - xclick); //zmiana położenia figury o dystans jaki pokonała mysz od ostatniego uruchomienia funkcji
            translation.setY(translation.getY() + mouseEvent.getY() - yclick); // xyclick - punkt ostatniej zmiany położenia

            xclick = mouseEvent.getX();
            yclick = mouseEvent.getY();
        }
    }

    /**<p>Skaluje kształt <code>selected</code></p>
     * <p>ilość skalowania jest wprost proporcjonalna do przescrollowanej odległości, odwrotnie proporcjonalna do rzeczywistej wielkości figury (aby uzyskać płynne skalowanie)</p>
     * @param scrollEvent Event przesunięcia scrolla
     *
     */
    public void scaleShape(ScrollEvent scrollEvent) {
        if (scrollEvent.getTarget() instanceof Shaper && selected != null) {
            //Zmienna ustalająca ilość skalowania, wprost proporcjonalna do przescrollowanej odległości, odwrotnie proporcjonalna do rzeczywistej wielkości figury (aby uzyskać płynne skalowanie)
            double scalingConst = (1+(scrollEvent.getDeltaY() / (5 * selected.getShape().getBoundsInParent().getWidth())));
            Scale scalation = selected.getScalation();
            scalation.setX(scalation.getX() * scalingConst);
            scalation.setY(scalation.getY() * scalingConst);
            if (selected.getShape().getBoundsInParent().getHeight() < 16) {
                scalation.setX(scalation.getX() / scalingConst);
                scalation.setY(scalation.getY() / scalingConst);
            }
            selected.getShape().setStrokeWidth(5/scalation.getX());
        }
    }

    /**
     * Obraca kształt <code>selected</code> o wielkość przesunięcia scrolla
     * @param scrollEvent - Event przesunięcia scrolla
     */
    public void rotateShape(ScrollEvent scrollEvent) { //wciśnięcie control + obrócenie scrolla -> obraca kształt
        if (scrollEvent.getTarget() instanceof Shaper  && selected != null) {
            Rotate rotation = selected.getRotation();
            rotation.setAngle(rotation.getAngle() + scrollEvent.getDeltaY() / 16);
        }
    }
}
