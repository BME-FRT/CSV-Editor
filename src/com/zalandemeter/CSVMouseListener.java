package com.zalandemeter;

import java.awt.event.*;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A program bemeneteként kezelt egérrel kapcsolatos műveleteket megvalósító osztály.
 * @author zalandemeter
 */
public class CSVMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener {

    /**
     * A kezelendő grafikus megjelenítőt tárolja.
     */
    private final GUI gui;

    /**
     * Az egér kattintás húzásához felhasznált X referencia koordináta érték, ahova kattintott a felhasználó.
     */
    private double referenceX;

    /**
     * Az egér kattintás húzásához felhasznált Y referencia koordináta érték, ahova kattintott a felhasználó.
     */
    private double referenceY;

    /**
     * Az aktuálisan kijelölt objektum. Értéke lehet null is.
     */
    private Item selected;

    /**
     * Az osztály konstruktora.
     * @param _gui a kezelt grafikus megjelenítő.
     */
    public CSVMouseListener(GUI _gui){
        gui = _gui;
    }

    /**
     * Az egér lenyomásakor meghívott függvény.
     * Kitörli az aktuálisan kijelölt objektumot.
     * Ha le van nyomva a shift beállítja az újonnan kijelölt objektumot.
     * @param eventPoint az egér eseményhez tartozó koordináta pár.
     * @param shiftDown le van e nyomva a SHIFT billentyű.
     */
    public void handlePressed(Point2D eventPoint, boolean shiftDown){
        referenceX = eventPoint.getX();
        referenceY = eventPoint.getY();

        if (selected != null) {
            selected.setSelected(false);
            selected = null;
            gui.getMenuBar().getSpinnerColor().setValue(gui.getMenuBar().getSpinnerTypes()[5]);
        }

        if (shiftDown) {
            try {
                Point2D relative = gui.getCanvas().getAt().inverseTransform(eventPoint, null);
                for (Item i : gui.getCanvas().getObjects()) {
                    if (Item.getDistance(relative.getX(),relative.getY(),i.getX()*Item.getObjectDistance(),i.getY()*Item.getObjectDistance()) < Item.getObjectSize()/2.0) {
                        if (selected != null){
                            selected.setSelected(false);
                        }
                        selected = i;
                        selected.setSelected(true);
                        gui.getMenuBar().getSpinnerColor().setValue(gui.getMenuBar().getSpinnerTypes()[selected.getColor()]);
                    }
                }
            } catch (NoninvertibleTransformException noninvertibleTransformException) {
                noninvertibleTransformException.printStackTrace();
            }
        }
    }

    /**
     * Az egérgomb lenyomását kezelő függvény.
     * @param e egér eseméy.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point2D eventPoint = e.getPoint();
        boolean shiftDown = e.isShiftDown();
        handlePressed(eventPoint,shiftDown);
        gui.getCanvas().repaint();
    }

    /**
     * Az egér kattintás húzásakor meghívott függvény.
     * Ha nincs lenyomva a SHIFT eltolja a vásznat a megfelelő irányba.
     * Ha le van nyomva a SHIFT akkor a kijelölt pontot átmozhatja a megfelelő koordinátákra.
     * @param eventPoint az egér eseményhez tartozó koordináta pár.
     * @param shiftDown le van e nyomva a SHIFT billentyű.
     */
    public void handleDragged(Point2D eventPoint, boolean shiftDown){
        double deltaX = (eventPoint.getX() - referenceX)/gui.getCanvas().getScale();
        double deltaY = (eventPoint.getY() - referenceY)/gui.getCanvas().getScale();
        referenceX = eventPoint.getX();
        referenceY = eventPoint.getY();
        if (shiftDown) {
            if (selected != null){
                deltaX /= Item.getObjectDistance();
                deltaY /= Item.getObjectDistance();
                selected.setX(selected.getX()+deltaX);
                selected.setY(selected.getY()+deltaY);
                gui.getCsvFileHandler().setEdited(true);
                gui.getCanvas().clearCurve();
            }
            setRelativeCoords(eventPoint);
        } else {
            gui.getCanvas().setTranslateX(gui.getCanvas().getTranslateX() + deltaX);
            gui.getCanvas().setTranslateY(gui.getCanvas().getTranslateY() + deltaY);
        }
    }

    /**
     * Az egér kattintás húzását kezelő függvény.
     * @param e egér eseméy.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point2D eventPoint = e.getPoint();
        boolean shiftDown = e.isShiftDown();
        handleDragged(eventPoint,shiftDown);
        gui.getCanvas().repaint();
    }

    /**
     * Beállítja a grafikus megjelenítésen az egérmutatóhoz tartozó koordináta értékek kijelzését.
     * @param eventPoint
     */
    public void setRelativeCoords(Point2D eventPoint){
        try {
            Point2D relative = gui.getCanvas().getAt().inverseTransform(eventPoint, null);
            gui.getFooter().setRelativeCoords("x: " + (int)relative.getX() + "    y: " + (int)relative.getY());
        } catch (NoninvertibleTransformException | NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Az egérmutató helyzetének megváltozását kezelő függvény.
     * @param e egér eseméy.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        Point2D eventPoint = e.getPoint();
        setRelativeCoords(eventPoint);
    }

    /**
     * Az egér görgő mozgatásakor meghívott függvény.
     * Ha a SHIFT gomb nincsen lenyomva a görgő mozgatási irányával arányosan megváltoztatja a vászon nagyítását.
     * @param mouseWheelRotation az egér görgő mozgatásához rendelt egész érték.
     * @param shiftDown le van e nyomva a SHIFT billentyű.
     */
    public void handleWheelMoved(int mouseWheelRotation,boolean shiftDown){
        if (!shiftDown) {
            if (mouseWheelRotation > 0) {
                if(gui.getCanvas().getScale()>=0.2){
                    BigDecimal bd = new BigDecimal(Double.toString(gui.getCanvas().getScale()-0.1));
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    gui.getCanvas().setScale(bd.doubleValue());
                    gui.getFooter().setScale(bd.doubleValue());
                }
            } else {
                if(gui.getCanvas().getScale()<=2.0){
                    BigDecimal bd = new BigDecimal(Double.toString(gui.getCanvas().getScale()+0.1));
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    gui.getCanvas().setScale(bd.doubleValue());
                    gui.getFooter().setScale(bd.doubleValue());
                }
            }
        }
    }

    /**
     * Az egér görgő mozgatását kezelő függvény.
     * @param e egér eseméy.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int mouseWheelRotation = e.getWheelRotation();
        boolean shiftDown = e.isShiftDown();
        handleWheelMoved(mouseWheelRotation,shiftDown);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * A referencia X koordinátához tartozó getter.
     * @return a referencia X koordináta érték.
     */
    public double getReferenceX() {
        return referenceX;
    }

    /**
     * A referencia Y koordinátához tartozó getter.
     * @return a referencia Y koordináta érték.
     */
    public double getReferenceY() {
        return referenceY;
    }

    /**
     * A referencia X koordinátához tartozó setter.
     * @param referenceX a beállítandó X referencia érték.
     */
    public void setReferenceX(double referenceX) {
        this.referenceX = referenceX;
    }

    /**
     * A referencia Y koordinátához tartozó setter.
     * @param referenceY a beállítandó Y referencia érték.
     */
    public void setReferenceY(double referenceY) {
        this.referenceY = referenceY;
    }

    /**
     * A kijelölt objektumhoz tartozó getter.
     * @return az aktuálisan kijelölt objektum.
     */
    public Item getSelected() {
        return selected;
    }

    /**
     * A kijelölt objektumhoz tartozó setter.
     * @param selected a kijelölni kívánt objektum.
     */
    public void setSelected(Item selected) {
        this.selected = selected;
    }

}
