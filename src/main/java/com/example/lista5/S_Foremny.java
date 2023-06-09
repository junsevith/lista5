package com.example.lista5;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.io.Serial;

public class S_Foremny extends Polygon implements Shaper {
    @Serial
    private static final long serialVersionUID = 4L;

    ShapeData shapeData = new ShapeData(this);

    /**
     * Ilość boków x-ścianu foremnego
     */
    protected int xgon;

    /**
     * Kąt środkowy wielokąta
     */
    protected double angle;

    /**
     * Minimalny promień generowanego wielokąta
     */
    protected double min = 7; //minimalny promień

    public S_Foremny() {
        xgon = 6;
        angle = (2 * Math.PI) / xgon;
        shapeData.color = generateColor();
    }

    public S_Foremny(int n) {
        xgon = n;
        angle = (2 * Math.PI) / xgon;
        shapeData.color = generateColor();
    }

    @Override
    public String toString() {
        return xgon + " - kąt foremny";
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
        return new S_Foremny(xgon);
    }

    @Override
    public void drawStart(double x, double y) {
        shapeData.setStart(x, y);

        this.setFill(shapeData.color);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(5/shapeData.scale.getX());

        this.getTransforms().addAll(shapeData.translate, shapeData.rotate, shapeData.scale);
        shapeData.rotate.setPivotX(x);
        shapeData.rotate.setPivotY(y);
        shapeData.scale.setPivotX(x);
        shapeData.scale.setPivotY(y);

        //this.getPoints().addAll(generateXgon(shapeData.startX + min, shapeData.startY + min));
        drawDraw(x, y);
    }

    @Override
    public void drawDraw(double x, double y) {
        shapeData.setEnd(x, y);

        //↓ pilnuje by kształt nie był za mały (promień < min)
        double radius = shapeData.getDist();
        if (radius < min) {
            if (radius == 0) {
                x += min;
            } else {
                double scalar = min / radius;
                x = (shapeData.startX - x) * scalar + shapeData.startX;
                y = (shapeData.startY - y) * scalar + shapeData.startY;
            }
        }

        this.getPoints().clear();
        this.getPoints().addAll(generateXgon(x, y));
    }

    @Override
    public boolean drawEnd() {
        return true;
    }

    @Override
    public void moveShape(double x, double y) {
        shapeData.endX = shapeData.endX - (shapeData.startX - x);
        shapeData.endY = shapeData.endY - (shapeData.startY - y);
        shapeData.startX = x;
        shapeData.startY = y;
        this.getPoints().clear();
        this.getPoints().addAll(generateXgon(shapeData.endX, shapeData.endY));
    }

    @Override
    public double getAnchorX() {
        return shapeData.startX;
    }

    @Override
    public double getAnchorY() {
        return shapeData.startY;
    }

    /**
     * Generuje wierzchołki wielokąta wykorzystując obrót liczby zespolonej (pierwszego wierzchołka)
     *
     * @param x x pierwszego wierzchołka
     * @param y y pierwszego wierzchołka
     * @return Tablica double z punktami wielokąta
     */
    public Double[] generateXgon(double x, double y) {
        Double[] vertexes = new Double[xgon * 2];
        vertexes[0] = x;
        vertexes[1] = y;

        for (int i = 1; i < xgon; i++) {
            vertexes[i * 2] = shapeData.startX + (x - shapeData.startX) * Math.cos(angle * i) - (y - shapeData.startY) * Math.sin(angle * i);
            vertexes[i * 2 + 1] = shapeData.startY + (x - shapeData.startX) * Math.sin(angle * i) + (y - shapeData.startY) * Math.cos(angle * i);
        }
        return vertexes;
    }

    /**
     * Generuje unikalny kolor dla wielokąta foremnego o ilości boków xgon
     *
     * @return Kolor dla ilości boków xgon
     */
    public Color generateColor() { //
        int colorNum = (xgon + "kabanos").hashCode();                           //tworzy hash liczby xgon
        colorNum = Math.abs(colorNum) % 16777215;                               //liczy modulo aby liczba nie była większa niż 0xFFFFFF
        String colorStr = Integer.toHexString(colorNum);                        //zamienia int na str
        String colorEnd = ("000000" + colorStr).substring(colorStr.length());   //dodaje wiodące zera
        return Color.web(colorEnd);
    }
}
