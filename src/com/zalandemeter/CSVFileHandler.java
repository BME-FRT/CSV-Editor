package com.zalandemeter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 * A program által megnyitott és szerkesztett fájlokkal kapcsolatos műveleteket megvalósító osztály.
 * @author zalandemeter
 */
public class CSVFileHandler {

    /**
     * A koordinátarendszert megjelenítő egységet tárolja.
     */
    private final CSVCanvas canvas;

    /**
     * Az aktuálisan megnyitott fájl abszolút elérési útvonala. Felvehet null értéket is.
     */
    private String currentFile;

    /**
     * Megadja, hogy az aktuálisan megnyitott fájl szerkesztve volt vagy sem.
     */
    private boolean edited;

    /**
     * Az osztály konstruktora.
     * @param canvas a kezelt vászon.
     */
    public CSVFileHandler(CSVCanvas canvas){
        this.canvas = canvas;
        edited = false;
    }

    /**
     * CSV file beolvasását végző függvény. A beolvasott objektumokat a kezelt vászon objects listájába helyezi el.
     * @param filename beolvasandó file neve.
     */
    public void parseCSV(String filename){
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                /*
                 * BigDecimal osztály használata, a lebegőpontos értékek kezeléséből adódó pontatlantásgok kiküszöbölésére.
                 */
                String[] line = scanner.next().split(",");
                BigDecimal bdX = new BigDecimal(String.valueOf(Double.parseDouble(line[0])));
                BigDecimal bdY = new BigDecimal(String.valueOf(Double.parseDouble(line[1])));
                bdX = bdX.setScale(8, RoundingMode.HALF_UP);
                bdY = bdY.setScale(8, RoundingMode.HALF_UP);
                canvas.addObject(new Item(bdX.doubleValue(), bdY.doubleValue(), Integer.parseInt(line[2])));
            }
            scanner.close();
            edited = false;
            currentFile = filename;
            canvas.repaint();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * CSV file-ba mentést végző függvény. A kezelt vászon objects listájában található objektumokat menti el.
     * Minden sorba egy objektum kerül [x,y,szín] formátumban
     * @param filename a menteni kívánt fájl neve.
     */
    public void saveCSV(String filename){
        try {
            FileWriter fileWriter = new FileWriter(filename);
            for (Item i: canvas.getObjects()) {
                /*
                 * BigDecimal osztály használata, a lebegőpontos értékek kezeléséből adódó pontatlantásgok kiküszöbölésére.
                 */
                BigDecimal bdX = new BigDecimal(String.valueOf(i.getX()));
                BigDecimal bdY = new BigDecimal(String.valueOf(i.getY()));
                bdX = bdX.setScale(8, RoundingMode.HALF_UP);
                bdY = bdY.setScale(8, RoundingMode.HALF_UP);
                fileWriter.write(bdX.doubleValue() + "," + bdY.doubleValue() + "," + i.getColor() + System.getProperty("line.separator"));
            }
            fileWriter.close();
            edited = false;
            currentFile = filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fájl kiválasztáskor használandó. Eldönti, hogy megnyitásra vagy mentésre szeretnénk fájlt kiválasztani.
     */
    enum ChooseType{
        open, save
    }

    /**
     * Fájl kiválasztás menüt hoz elő.
     * Megynitás esetén meghívja a parseCSV függvényt a kijelölt fájlra.
     * Mentés esetén meghívja a saveCSV függvényt a kijelölt fájlra.
     * @param canvas kezelendő vászon.
     * @param type kiválasztás típusa (megnyitás,mentés).
     */
    public void selectFile(CSVCanvas canvas, ChooseType type) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files", "csv");
        chooser.setFileFilter(csvFilter);
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        if (type.equals(ChooseType.open)){
            chooser.setApproveButtonText("Open");
            if (chooser.showOpenDialog(canvas) == JFileChooser.APPROVE_OPTION) {
                canvas.clearObjects();
                canvas.clearCurve();
                parseCSV(chooser.getSelectedFile().getAbsolutePath());
            }
        }

        else if (type.equals(ChooseType.save)) {
            chooser.setApproveButtonText("Save");
            if (currentFile != null) {
                chooser.setSelectedFile(new File(currentFile));
            }
            if (chooser.showOpenDialog(canvas) == JFileChooser.APPROVE_OPTION) {
                saveCSV(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    /**
     * Aktuálisan megnyitott fájlhoz tartozó getter.
     * @return az aktuális fájl abszolút elérési útvonala.
     */
    public String getCurrentFile() {
        return currentFile;
    }

    /**
     * Beállítja az aktuálisan kezelt fájl elérési útvonalát.
     * @param currentFile beállítandó elérési útvonal.
     */
    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * A megnyitott fájl szerkesztettségi állapotát kérdezi le.
     * @return szerkesztve van-e a megnyitott fájl.
     */
    public boolean isEdited() {
        return edited;
    }

    /**
     * Beállítja a megnyitott fájlt szerkesztettnek, vagy törli a szerkesztettséget.
     * @param edited igaz, ha a fájlt szerkesztették.
     */
    public void setEdited(boolean edited) {
        this.edited = edited;
    }
}
