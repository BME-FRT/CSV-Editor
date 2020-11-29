package com.zalandemeter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * A fájlokkal kapcsolatos műveleteket tesztelő osztály.
 * @author zalandemeter
 */
public class CSVFileHandlerTest {

    /**
     * A fájlokkal kapcsolatos műveleteket tárolja.
     */
    private CSVFileHandler csvFileHandler;

    /**
     * Tesztelendő CSV fájl.
     */
    private File file;

    /**
     * Minden teszt elején létrehozunk egy GUI-t, a hozzá tartozó fájlkezelőt és egy teszt file-t.
     */
    @Before
    public void setUp() {
        GUI gui = new GUI();
        csvFileHandler = gui.getCsvFileHandler();
        file = new File("test/test.csv");
    }

    /**
     * CSV fájl beolvasását tesztelő metődus.
     */
    @Test
    public void parseCSV() {
        csvFileHandler.parseCSV(file.getAbsolutePath());
        csvFileHandler.setEdited(true);
        csvFileHandler.saveCSV(file.getAbsolutePath());
        Assert.assertFalse(csvFileHandler.isEdited());
        Assert.assertEquals(file.getAbsolutePath(), csvFileHandler.getCurrentFile());
    }

    /**
     * CSV fájlba írását tesztelő metődus.
     */
    @Test
    public void saveCSV() {
        csvFileHandler.parseCSV(file.getAbsolutePath());
        csvFileHandler.setEdited(true);
        csvFileHandler.saveCSV(file.getAbsolutePath());
        Assert.assertFalse(csvFileHandler.isEdited());
        Assert.assertEquals(file.getAbsolutePath(), csvFileHandler.getCurrentFile());
    }
}