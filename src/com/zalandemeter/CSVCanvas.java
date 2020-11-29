package com.zalandemeter;

import com.onlylemi.genetictsp.GeneticAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Az objektumok felülnézeti két dimenziós koordinátarendszerben való kijelzését megvalósító osztály.
 * @author zalandemeter
 */
public class CSVCanvas extends JComponent {
    /**
     * A vászon eltolása X irányban.
     */
    private double translateX;

    /**
     * A vászon eltolása Y irányban.
     */
    private double translateY;

    /**
     * A vászon nagyítása.
     */
    private double scale;

    /**
     * A nagyítás és eltolási értékekhez tartozó transzformáció.
     */
    private AffineTransform at;

    /**
     * A tárolt objektumok listája.
     */
    private final ArrayList<Item> objects;

    /**
     * A tárolt kék objektumok listája. Szükséges az objektumok sorbarendezéséhez
     */
    private final ArrayList<Item> blue;

    /**
     * A tárolt sárga objektumok listája. Szükséges az objektumok sorbarendezéséhez
     */
    private final ArrayList<Item> yellow;

    /**
     * A sorbarendezett kék objektumok indexeinek listája. Amennyiben nincsen sorbarendezés null értéket vesz fel.
     */
    private int[] idxBlue;

    /**
     * A sorbarendezett sárga objektumok indexeinek listája. Amennyiben nincsen sorbarendezés null értéket vesz fel.
     */
    private int[] idxYellow;

    /**
     * Az osztály konstruktora. Beállítja az eltolási értékeket 0-ra és a nagyítás értéket 1.1-re.
     * 1.1 szükséges alapértelmezettként, hogy ne tudjon 0-ra csökkenni a nagyítás érték.
     * Így minden esetben invertálható lesz a transzformáció.
     */
    public CSVCanvas() {
        translateX = 0;
        translateY = 0;
        scale = 1.1;
        objects = new ArrayList<>();
        blue = new ArrayList<>();
        yellow = new ArrayList<>();
    }

    /**
     * Sorbarendezi a kék és sárga objektumokat külön-külön. Elhelyezi ezeket az indexeket a megfelelő listába.
     */
    public void initCurve(){
        clearCurve();
        for (Item i: objects) {
            if (i.getColor() == 1){
                blue.add(i);
            }
            if (i.getColor()==2){
                yellow.add(i);
            }
        }
        if (blue.size()>2){
            GeneticAlgorithm ga1 = GeneticAlgorithm.getInstance();
            ga1.setMaxGeneration(1000);
            ga1.setAutoNextGeneration(true);
            idxBlue = ga1.tsp(GeneticAlgorithm.getDist(blue));
        }
        if (yellow.size()>2){
            GeneticAlgorithm ga2 = GeneticAlgorithm.getInstance();
            ga2.setMaxGeneration(1000);
            ga2.setAutoNextGeneration(true);
            idxYellow = ga2.tsp(GeneticAlgorithm.getDist(yellow));
        }
        repaint();
    }

    /**
     * A paraméterül kapott indexek alapján a paraméterül kapott listában összeköti az objektumokat és kirajzolja ezt.
     * @param ourGraphics grafika amire rajzol.
     * @param idx index lista ami alapján rajzol.
     * @param items objektumok amiket összeköt.
     * @param color szín amivel kirajzol.
     */
    public void drawCurve(Graphics2D ourGraphics, int[] idx, ArrayList<Item> items, Color color){
        if (idx != null){
            ourGraphics.setColor(color);
            for (int i = 0; i<idx.length; ++i){
                ourGraphics.setStroke(new BasicStroke(5));
                if (i == items.size()-1){
                    ourGraphics.draw(new Line2D.Double(items.get(idx[i]).getX()*Item.getObjectDistance(),items.get(idx[i]).getY()*Item.getObjectDistance(),items.get(idx[0]).getX()*Item.getObjectDistance(), items.get(idx[0]).getY()*Item.getObjectDistance()));
                } else {
                    ourGraphics.draw(new Line2D.Double(items.get(idx[i]).getX()*Item.getObjectDistance(),items.get(idx[i]).getY()*Item.getObjectDistance(),items.get(idx[i+1]).getX()*Item.getObjectDistance(), items.get(idx[i+1]).getY()*Item.getObjectDistance()));
                }
            }
        }
    }

    /**
     * Kirajzolja a vásznat és a tárolt objektumokat.
     * Amennyiben az index listák nem üresek, össze is köti a megfelelő objektumokat.
     * @param g a módosítandó grafika.
     */
    public void paintComponent(Graphics g) {

        Graphics2D ourGraphics = (Graphics2D)g.create();
        try {
            ourGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ourGraphics.setColor(new Color(48,48,48));
            ourGraphics.fillRect(0, 0, getWidth(), getHeight());

            at = new AffineTransform(ourGraphics.getTransform());
            at.translate(getWidth()/2.0, getHeight()/2.0);
            at.scale(scale, scale);
            at.translate(-getWidth()/2.0, -getHeight()/2.0);
            at.translate(translateX, translateY);
            ourGraphics.setTransform(at);

            drawCurve(ourGraphics, idxBlue, blue, Color.BLUE);
            drawCurve(ourGraphics, idxYellow, yellow, Color.YELLOW);

            for (Item i: objects) {
                i.paintComponent(ourGraphics);
            }
        } finally {
            ourGraphics.dispose();
        }
    }

    /**
     * Az X irányú eltoláshoz tartozó getter.
     * @return X irányú eltolás értéke.
     */
    public double getTranslateX() {
        return translateX;
    }

    /**
     * Az Y irányú eltoláshoz tartozó getter.
     * @return Y irányú eltolás értéke.
     */
    public double getTranslateY() {
        return translateY;
    }

    /**
     * Az X irányú eltoláshoz tartozó setter.
     * @param translateX a beállítandó eltolás érték.
     */
    public void setTranslateX(double translateX) {
        this.translateX = translateX;
    }

    /**
     * Az Y irányú eltoláshoz tartozó setter.
     * @param translateY a beállítandó eltolás érték.
     */
    public void setTranslateY(double translateY) {
        this.translateY = translateY;
    }

    /**
     * A transzformációhoz tartozó getter.
     * @return a használt transzformáció.
     */
    public AffineTransform getAt() {
        return at;
    }

    /**
     * A nagyításhoz tartozó getter.
     * @return a nagyítás értéke.
     */
    public double getScale() {
        return scale;
    }

    /**
     * A nagyításhoz tartozó setter.
     * @param scale a beállítandó nagyítás értéke.
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * A tárolt objektumokhoz tartozó getter.
     * @return a tárolt objektumok listája.
     */
    public ArrayList<Item> getObjects() {
        return objects;
    }

    /**
     * A tárolt kék objektumokhoz tartozó getter.
     * @return a tárolt kék objektumok listája.
     */
    public ArrayList<Item> getBlue() {
        return blue;
    }

    /**
     * A tárolt sárga objektumokhoz tartozó getter.
     * @return a tárolt sárga objektumok listája.
     */
    public ArrayList<Item> getYellow() {
        return yellow;
    }

    /**
     * A kék objektumok sorbarendezett indexeit tároló lista.
     * Amennyiben nem történt sorbarendezés, értéke null.
     * @return kék objektumok index listája.
     */
    public int[] getIdxBlue() {
        return idxBlue;
    }

    /**
     * A sárga objektumok sorbarendezett indexeit tároló lista.
     * Amennyiben nem történt sorbarendezés, értéke null.
     * @return sárga objektumok index listája.
     */
    public int[] getIdxYellow() {
        return idxYellow;
    }

    /**
     * Objektumot ad hozzá a vászonhoz. Hozzáfűzi az objektumok listájához.
     * @param i hozzá adni kívánt objektum.
     */
    public void addObject(Item i) {
        objects.add(i);
    }

    /**
     * Kitörli a vászon által tárolt objektumokat.
     */
    public void clearObjects() {
        objects.clear();
    }

    /**
     * Kitölri a vászon által tárolt íveket és a hozzá szükséges index listákat.
     */
    public void clearCurve() {
        blue.clear();
        yellow.clear();
        idxBlue = null;
        idxYellow = null;
    }
}