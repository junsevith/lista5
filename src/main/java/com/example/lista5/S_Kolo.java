package com.example.lista5;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.io.Serial;

public class S_Kolo extends Circle implements Shaper {
    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Minimalny promień generowanego koła
     */
    public static final int min = 7; //minimalny promień
    public ShapeData shapeData = new ShapeData(this);

    @Override
    public String toString() {
        return "Koło";
    }

    @Override
    public ShapeData getData() {
        return shapeData;
    }

    @Override
    public Shape getShape() {
        return this;
    }

    @Override
    public Shaper newShape() {
        return new S_Kolo();
    }

    public S_Kolo() {
        shapeData.color = Color.ORANGERED;
    }

    @Override
    public void drawStart(double x, double y) {
        shapeData.setStart(x, y);

        this.setFill(shapeData.color);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(5/shapeData.scale.getY());

        this.setCenterX(x);
        this.setCenterY(y);

        this.getTransforms().addAll(shapeData.translate, shapeData.scale, shapeData.rotate);
        shapeData.rotate.setPivotX(this.getCenterX());
        shapeData.rotate.setPivotY(this.getCenterY());
        shapeData.scale.setPivotX(this.getCenterX());
        shapeData.scale.setPivotY(this.getCenterY());

        drawDraw(x, y);
    }


    @Override
    public void drawDraw(double x, double y) {
        shapeData.setEnd(x, y);

        //↓ ustawia promień jako odległość ze środka do punktu (x,y)
        double radius = shapeData.getDist();
        if (radius < min) {
            radius = min;
        }

        this.setRadius(radius);

    }

    @Override
    public boolean drawEnd() {
        return true;
    }

    @Override
    public void moveShape(double x, double y) {
        this.setCenterX(x);
        this.setCenterY(y);
    }

    @Override
    public double getAnchorX() {
        return this.getCenterX();
    }

    @Override
    public double getAnchorY() {
        return this.getCenterY();
    }
}
