package com.zalandemeter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * A grafikus megjelenítéssel kapcsolatos interakciókat tesztelő osztály.
 * @author zalandemeter
 */
public class GUIHandlersTest {

    /**
     * A kezelendő grafikus megjelenítőt tárolja.
     */
    private GUI gui;

    /**
     * A grafikus megjelenítés interakcióit tartalmazó példány.
     */
    private GUIHandlers guiHandlers;

    /**
     * A koordinátarendszert megjelenítő egységet tárolja.
     */
    private CSVCanvas canvas;

    /**
     * A fájlokkal kapcsolatos műveleteket tárolja.
     */
    private CSVFileHandler csvFileHandler;

    /**
     * Minden teszt elején létrehozunk egy GUI-t,
     * és elmentjük a hozzá tartozó interakció kezelő, vászon és fájlkezelő példányt.
     */
    @Before
    public void setUp(){
        gui = new GUI();
        guiHandlers = gui.getGuiHandlers();
        canvas = gui.getCanvas();
        csvFileHandler = gui.getCsvFileHandler();
    }

    /**
     * Üres fájl létrehozását tesztelő metódus.
     */
    @Test
    public void newFile() {
        guiHandlers.newFile();
        Assert.assertEquals("CSV Editor",gui.getFrame().getTitle());
        Assert.assertTrue(canvas.getObjects().isEmpty());
        Assert.assertTrue(canvas.getBlue().isEmpty());
        Assert.assertTrue(canvas.getYellow().isEmpty());
        Assert.assertNull(canvas.getIdxBlue());
        Assert.assertNull(canvas.getIdxYellow());
    }

    /**
     * Objektum vászonhoz való hozzáadását tesztelő metódus.
     */
    @Test
    public void addItem() {
        guiHandlers.addItem();
        Assert.assertEquals(1,canvas.getObjects().size());
        Assert.assertTrue(csvFileHandler.isEdited());
    }

    /**
     * A kijelölt elem törlését tesztelő metődus.
     */
    @Test
    public void deleteItem() {
        guiHandlers.addItem();
        gui.getCsvMouseListener().setSelected(canvas.getObjects().get(0));
        guiHandlers.deleteItem();
        Assert.assertEquals(0,canvas.getObjects().size());
        Assert.assertTrue(csvFileHandler.isEdited());
    }

    /**
     * A nézet alapértelmezett értékének visszaállítását tesztelő metódus.
     */
    @Test
    public void resetViewport() {
        guiHandlers.resetViewport();
        Assert.assertEquals(0,canvas.getTranslateX(),0);
        Assert.assertEquals(0,canvas.getTranslateX(),0);
        Assert.assertEquals(1.1,canvas.getScale(),0);
        Assert.assertEquals(110,gui.getFooter().getScaleSlider().getValue());
        Assert.assertEquals("100",gui.getFooter().getScaleText().getText());
    }

    /**
     * Az aktuálisan kijelölt objektum színének megváltoztatását tesztelő metódus.
     */
    @Test
    public void changeColor() {
        guiHandlers.addItem();
        Assert.assertEquals(4,canvas.getObjects().get(0).getColor());
        gui.getCsvMouseListener().setSelected(canvas.getObjects().get(0));
        gui.getMenuBar().getSpinnerColor().setValue(gui.getMenuBar().getSpinnerTypes()[0]);
        guiHandlers.changeColor();
        Assert.assertEquals(0,canvas.getObjects().get(0).getColor());
    }

    /**
     * Az objektumok megjelenítési méretének beállítását tesztelő metódus.
     */
    @Test
    public void setObjectSize() {
        Assert.assertEquals(25,Item.getObjectSize());
        guiHandlers.setObjectSize(75);
        Assert.assertEquals(75,Item.getObjectSize());
    }

    /**
     * Az objektumok megjelenítési távolságának beállítását tesztelő metódus.
     */
    @Test
    public void setObjectDistance() {
        Assert.assertEquals(50,Item.getObjectDistance());
        guiHandlers.setObjectDistance(75);
        Assert.assertEquals(75,Item.getObjectDistance());
    }

    /**
     * A vászon nagyítási értékének beállítását tesztelő metódus.
     */
    @Test
    public void setScale() {
        guiHandlers.setScale(1.5);
        Assert.assertEquals(1.5,canvas.getScale(),0);
        Assert.assertEquals(150,gui.getFooter().getScaleSlider().getValue());
        Assert.assertEquals("140",gui.getFooter().getScaleText().getText());
    }
}