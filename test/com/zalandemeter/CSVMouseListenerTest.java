package com.zalandemeter;
import java.awt.geom.Point2D;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Az egérrel kapcsolatos műveleteket tesztelő osztály.
 * @author zalandemeter
 */
public class CSVMouseListenerTest {

    /**
     * A kezelendő grafikus megjelenítőt tárolja.
     */
    private GUI gui;

    /**
     * Az egérműveletek teszteléséhez használt teszt objektum.
     */
    private Item item;

    /**
     * Minden teszt elején létrehozunk egy GUI-t, és hozzáadunk egy teszt objektumot.
     */
    @Before
    public void setUp() {
        gui = new GUI();
        item = new Item(0,0,0);
        gui.getCanvas().addObject(item);
    }

    /**
     * Az egér gomb lenyomásakor meghívódó kezelő függvényt tesztelő metődus.
     */
    @Test
    public void mousePressed() {
        gui.getCanvas().paintComponent(gui.getCanvas().getGraphics());
        Point2D eventPoint = new Point2D.Double(0,0);
        gui.getCanvas().getAt().transform(eventPoint, eventPoint);
        gui.getCsvMouseListener().handlePressed(eventPoint,true);

        Assert.assertTrue(gui.getCanvas().getObjects().get(0).isSelected());
        Assert.assertEquals(item,gui.getCsvMouseListener().getSelected());
        Assert.assertEquals("orange",gui.getMenuBar().getSpinnerColor().getValue());

        gui.getCanvas().getObjects().get(0).setColor(1);
        gui.getCanvas().getObjects().get(0).setSelected(false);
        eventPoint = new Point2D.Double((Item.getObjectSize()/2.0)-2,0);
        gui.getCanvas().getAt().transform(eventPoint, eventPoint);
        gui.getCsvMouseListener().handlePressed(eventPoint, true);

        Assert.assertTrue(gui.getCanvas().getObjects().get(0).isSelected());
        Assert.assertEquals(item,gui.getCsvMouseListener().getSelected());
        Assert.assertEquals("blue",gui.getMenuBar().getSpinnerColor().getValue());
    }

    /**
     * Az egér kattintás húzásakor meghívódó kezelő függvényt tesztelő metődus,
     * amennyiben le van nyomva a SHIFT billentyű.
     */
    @Test
    public void mouseDraggedShift() {
        Point2D eventPoint = new Point2D.Double(30,50);
        Point2D clickPoint = new Point2D.Double(0,0);

        item.setSelected(true);
        gui.getCsvMouseListener().setSelected(item);
        gui.getCanvas().paintComponent(gui.getCanvas().getGraphics());
        gui.getCsvMouseListener().setReferenceX(clickPoint.getX());
        gui.getCsvMouseListener().setReferenceY(clickPoint.getY());

        double deltaX = (eventPoint.getX() - gui.getCsvMouseListener().getReferenceX())/gui.getCanvas().getScale();
        double deltaY = (eventPoint.getY() - gui.getCsvMouseListener().getReferenceY())/gui.getCanvas().getScale();

        gui.getCsvMouseListener().handleDragged(eventPoint,true);
        Assert.assertEquals(deltaX/Item.getObjectDistance(),item.getX(),0);
        Assert.assertEquals(deltaY/Item.getObjectDistance(),item.getY(),0);
        Assert.assertEquals(0,gui.getCanvas().getTranslateX(),0);
        Assert.assertEquals(0,gui.getCanvas().getTranslateY(),0);
        Assert.assertTrue(gui.getCanvas().getBlue().isEmpty());
        Assert.assertTrue(gui.getCanvas().getYellow().isEmpty());
        Assert.assertTrue(gui.getCsvFileHandler().isEdited());
    }

    /**
     * Az egér kattintás húzásakor meghívódó kezelő függvényt tesztelő metődus,
     * amennyiben nincs le nyomva a SHIFT billentyű.
     */
    @Test
    public void mouseDraggedNoShift() {
        Point2D eventPoint = new Point2D.Double(30,50);
        Point2D clickPoint = new Point2D.Double(0,0);

        gui.getCanvas().paintComponent(gui.getCanvas().getGraphics());
        gui.getCsvMouseListener().setReferenceX(clickPoint.getX());
        gui.getCsvMouseListener().setReferenceY(clickPoint.getY());

        double deltaX = (eventPoint.getX() - gui.getCsvMouseListener().getReferenceX())/gui.getCanvas().getScale();
        double deltaY = (eventPoint.getY() - gui.getCsvMouseListener().getReferenceY())/gui.getCanvas().getScale();

        gui.getCsvMouseListener().handleDragged(eventPoint,false);
        Assert.assertEquals(0,item.getX(),0);
        Assert.assertEquals(0,item.getY(),0);
        Assert.assertEquals(deltaX,gui.getCanvas().getTranslateX(),0);
        Assert.assertEquals(deltaY,gui.getCanvas().getTranslateY(),0);
        Assert.assertFalse(gui.getCsvFileHandler().isEdited());
    }

    /**
     * Az egér mozgatásakor meghívódó kezelő függvényt tesztelő metődus.
     */
    @Test
    public void mouseWheelMoved() {
        gui.getCsvMouseListener().handleWheelMoved(1,false);
        Assert.assertEquals(1.0,gui.getCanvas().getScale(),0);
        gui.getCsvMouseListener().handleWheelMoved(1,true);
        Assert.assertEquals(1.0,gui.getCanvas().getScale(),0);
        for (int i = 0; i<25; ++i){
            gui.getCsvMouseListener().handleWheelMoved(1,false);
        }
        Assert.assertEquals(0.1,gui.getCanvas().getScale(),0);

        gui.getCanvas().setScale(1.1);
        gui.getCsvMouseListener().handleWheelMoved(-1,false);
        Assert.assertEquals(1.2,gui.getCanvas().getScale(),0);
        gui.getCsvMouseListener().handleWheelMoved(-1,true);
        Assert.assertEquals(1.2,gui.getCanvas().getScale(),0);
        for (int i = 0; i<25; ++i){
            gui.getCsvMouseListener().handleWheelMoved(-1,false);
        }
        Assert.assertEquals(2.1,gui.getCanvas().getScale(),0);
    }
}