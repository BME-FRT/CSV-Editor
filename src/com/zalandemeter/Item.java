package com.zalandemeter;

import java.awt.*;
import java.awt.geom.Ellipse2D;


/**
 * A koordináta rendszerben elhelyezett objektumokat valósítja meg.
 * @author zalandemeter
 */
public class Item {

    /**
     * Az objektum X koordinátája.
     */
    private double x;

    /**
     * Az objektum Y koordinátája.
     */
    private double y;

    /**
     * Az objektum színét azonosító kódszám.<br>
     * ( 0 - narancssárga; 1 - kék; 2 - sárga; 3 - piros; 4 - fehér )
     */
    private int color;

    /**
     * Megadja, hogy az objektum ki van-e jelölve. Más megjelenítés tartozik a kijelölt objektumhoz.
     */
    private boolean selected;

    /**
     * Az objektumok kirajzolási mérete, alapártelmezetten {@value}.
     */
    private static int objectSize = 25;

    /**
     * Az objektumok kirajzolásakor a közöttük lévő távolság, alapártelmezetten {@value}.
     */
    private static int objectDistance = 50;

    /**
     * Az objektumok konstruktora.
     * @param x x koordináta
     * @param y y koordináta
     * @param color színkód
     */
    public Item(double x, double y, int color){
        this.x = x;
        this.y = y;
        this.color = color;
        selected = false;
    }

    /**
     * A paraméterül kapott Graphics2D változóra kirajzolja az objektumot.
     * @param ourGraphics kirajzolás helye
     */
    public void paintComponent(Graphics2D ourGraphics){
        /*
         * Az értékek tárolási módjából adódóan a színeket egész számokra kódolva tároljuk.
         * Itt történik a szín dekódolása.
         */
        switch (color){
            case 0: ourGraphics.setColor(new Color(255,110,0)); break;
            case 1: ourGraphics.setColor(Color.BLUE); break;
            case 2: ourGraphics.setColor(Color.YELLOW); break;
            case 3: ourGraphics.setColor(Color.RED); break;
            case 4: ourGraphics.setColor(Color.WHITE); break;
            default: break;
        }
        ourGraphics.fill(new Ellipse2D.Double((x*objectDistance)-(objectSize/2.0),(y*objectDistance)-(objectSize/2.0),objectSize,objectSize));
        if (selected) {
            ourGraphics.setColor(new Color(48,48,48));
            ourGraphics.fill(new Ellipse2D.Double((x*objectDistance)-(objectSize*0.5/2.0),(y*objectDistance)-(objectSize*0.5/2.0),objectSize*0.5,objectSize*0.5));
        }
    }

    /**
     * X koordináta getter.
     * @return az objektum X koordinátája.
     */
    public double getX() {
        return x;
    }

    /**
     * Y koordináta getter.
     * @return az objektum Y koordinátája.
     */
    public double getY() {
        return y;
    }

    /**
     * Színkód getter.
     * @return az objektum színkódja.
     */
    public int getColor() {
        return color;
    }

    /**
     * Az objektumok kirajzolási nagyságához tartozó getter.
     * @return az objektumok kirajzolási nagysága.
     */
    public static int getObjectDistance() {
        return objectDistance;
    }

    /**
     * Az objektumok kirajzolási távolságához tartozó getter.
     * @return az objektumok kirajzolási távolsága.
     */
    public static int getObjectSize() {
        return objectSize;
    }

    /**
     * X koordináta setter.
     * @param x beállítandó X koordináta érték.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Y koordináta setter.
     * @param y beállítandó X koordináta érték.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Színkód setter.
     * @param color beállítandó színkód érték.
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Kijelöl egy objektumot, vagy törli a kijelölést.
     * @param selected igaz, ha az objektumot ki szeretnénk jelölni.
     */
    public void setSelected(boolean selected){
        this.selected = selected;
    }

    /**
     * Az objektum kijelöltségi állapotát kérdezi le.
     * @return ki van-e jelölve az objektum.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Beállítja az objektumok kirajzolási távolságát.
     * @param objectDistance a beállítandó objektum távolsági érték.
     */
    public static void setObjectDistance(int objectDistance) {
        Item.objectDistance = objectDistance;
    }

    /**
     * Beállítja az objektumok kirajzolási méretét.
     * @param objectSize a beállítandó objektum méret.
     */
    public static void setObjectSize(int objectSize) {
        Item.objectSize = objectSize;
    }

    /**
     * Két objektum távolságát számoló függvény.
     * @param p1 az első objektum.
     * @param p2 a második objektum.
     * @return a két objektum távolsága.
     */
    public static float getDistance(Item p1, Item p2) {
        return (float) Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
    }

    /**
     * Két objektum távolságát számoló függvény.
     * @param x1 az első objektum X koordinátája.
     * @param y1 az első objektum Y koordinátája.
     * @param x2 a második objektum X koordinátája.
     * @param y2 a második objektum Y koordinátája.
     * @return a két objektum távolsága.
     */
    public static double getDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
    }

}

