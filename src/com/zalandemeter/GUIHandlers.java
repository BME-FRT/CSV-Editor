package com.zalandemeter;

import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A GUI-val való interakciók során meghívott fügvényeket csoportosító osztály.
 * @author zalandemeter
 */
public class GUIHandlers {

    /**
     * A kezelendő grafikus megjelenítőt tárolja.
     */
    private final GUI gui;

    /**
     * Az egérrel kapcsolatos műveleteket tárolja.
     */
    private final CSVMouseListener csvMouseListener;

    /**
     * A fájlokkal kapcsolatos műveleteket tárolja.
     */
    private final CSVFileHandler csvFileHandler;

    /**
     * A grafikus megjelenítő keret-e.
     */
    private final JFrame frame;

    /**
     * A koordinátarendszert megjelenítő egységet tárolja.
     */
    private final CSVCanvas canvas;

    /**
     * Az osztály konstruktora. A paraméterül kapott GUI-ból inicializálja az alegységeket.
     * @param gui a kezelendő grafikus megjelenítő.
     */
    public GUIHandlers(GUI gui){
        this.gui = gui;

        csvFileHandler = gui.getCsvFileHandler();
        csvMouseListener = gui.getCsvMouseListener();
        canvas = gui.getCanvas();
        frame = gui.getFrame();
    }

    /**
     * Változtatás nélküli továbblépéskor megerősítést kér a felhasználótól.
     * @return a válasz értéke.
     */
    public int promptSave(){
        Object[] options = {"Continue without saving","Save","Cancel"};
        int answer = JOptionPane.showOptionDialog(frame,
                "Would you like to continue without saving?",
                "Confirm exit",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (answer == JOptionPane.NO_OPTION){
            if (csvFileHandler.getCurrentFile() != null){
                csvFileHandler.saveCSV(csvFileHandler.getCurrentFile());
            } else {
                csvFileHandler.selectFile(canvas,  CSVFileHandler.ChooseType.save);
            }
        }
        return answer;
    }

    /**
     * Üres szerkesztőablakot hoz létre, új fájlt nyit.
     */
    public void newFile(){
        int answer = -1;
        if (csvFileHandler.isEdited()){
            answer = promptSave();
        }
        if ((!csvFileHandler.isEdited()) || answer == JOptionPane.YES_OPTION){
            csvFileHandler.setCurrentFile(null);
            csvFileHandler.setEdited(false);
            frame.setTitle("CSV Editor");
            canvas.clearObjects();
            canvas.clearCurve();
            canvas.repaint();
        }
    }

    /**
     * Meglévő fájl tölt be, nyit meg.
     */
    public void openFile(){
        int answer = -1;
        if (csvFileHandler.isEdited()){
            answer = promptSave();
        }
        if ((!csvFileHandler.isEdited()) || answer == JOptionPane.YES_OPTION) {
            csvFileHandler.selectFile(canvas, CSVFileHandler.ChooseType.open);
            if (csvFileHandler.getCurrentFile() != null){
                frame.setTitle("CSV Editor - " + csvFileHandler.getCurrentFile());
            }
        }
    }

    /**
     * Elmenti az aktuálisan betöltött fájl módosításait.
     */
    public void saveFile(){
        if (csvFileHandler.getCurrentFile() != null){
            csvFileHandler.saveCSV(csvFileHandler.getCurrentFile());
        } else {
            csvFileHandler.selectFile(canvas,  CSVFileHandler.ChooseType.save);
            if (csvFileHandler.getCurrentFile() != null){
                frame.setTitle("CSV Editor - " + csvFileHandler.getCurrentFile());
            }
        }
    }

    /**
     * Elmenti az aktuálisan betöltött fájl módosításait és lehetőséget új fájl mentésére.
     */
    public void saveFileAs(){
        csvFileHandler.selectFile(canvas,  CSVFileHandler.ChooseType.save);
        if (csvFileHandler.getCurrentFile() != null){
            frame.setTitle("CSV Editor - " + csvFileHandler.getCurrentFile());
        }
    }

    /**
     * A (0,0) koordinátára egy alapértelezetten fehér színű új elemet helyez el.
     */
    public void addItem(){
        csvFileHandler.setEdited(true);
        canvas.getObjects().add(new Item(0,0,0));
        canvas.repaint();
    }

    /**
     * Kitörli a vászonban aktuálisan kijelölt objektumot.
     */
    public void deleteItem(){
        if (csvMouseListener.getSelected() != null){
            csvFileHandler.setEdited(true);
            canvas.getObjects().remove(csvMouseListener.getSelected());
            canvas.clearCurve();
            canvas.repaint();
        }
    }

    /**
     * Visszaállítja a megjelenítés eltolását és méretét alaphelyzetbe.
     */
    public void resetViewport(){
        canvas.setTranslateX(0);
        canvas.setTranslateY(0);
        setScale(1.1);
    }


    /**
     * A GUI színváltoztató menüpontjában beállított szín értéket beállítja az aktuálisan kijelölt objektumnak.
     */
    public void changeColor(){
        if (csvMouseListener.getSelected() != null){
            switch((String)gui.getMenuBar().getSpinnerColor().getValue()){
                case "white": csvMouseListener.getSelected().setColor(0); break;
                case "blue": csvMouseListener.getSelected().setColor(1); break;
                case "yellow": csvMouseListener.getSelected().setColor(2); break;
                case "red": csvMouseListener.getSelected().setColor(3); break;
                case "orange": csvMouseListener.getSelected().setColor(4); break;
                case "null": gui.getMenuBar().getSpinnerColor().setValue(gui.getMenuBar().getSpinnerTypes()[4]);
                default: break;
            }
            canvas.clearCurve();
            csvFileHandler.setEdited(true);
            canvas.repaint();
        } else {
            /*
             * Ha nincsen kijelölt objektum, null értékre állítja a választó menüt.
             */
            gui.getMenuBar().getSpinnerColor().setValue(gui.getMenuBar().getSpinnerTypes()[5]);
        }
    }

    /**
     * Kirajzolja a kék és sárga objektumokat külön külön összekötő legrövidebb kört.
     * A legrövidebb körök számolását és az objektumok sorbarendezését egy online elérhető genetikus algoritmust használó TSP solver
     * @see <a href="https://github.com/onlylemi/GeneticTSP">GitHUB kód</a>
     * @see com.onlylemi.genetictsp.GeneticAlgorithm
     */
    public void showCurve(){
        canvas.initCurve();
        canvas.repaint();
        csvFileHandler.saveOrderedCSV(csvFileHandler.getCurrentFile()+".ordered.csv");
    }

    /**
     * Beállítja az objektumok kirajzolási méretét.
     * @param objectSize a beállítandó méret.
     */
    public void setObjectSize(int objectSize){
        Item.setObjectSize(objectSize);
        canvas.repaint();
    }

    /**
     * Beállítja az objektumok kirajzolási távolságát.
     * @param objectDistance a beállítandó távolság.
     */
    public void setObjectDistance(int objectDistance){
        Item.setObjectDistance(objectDistance);
        canvas.repaint();
    }

    /**
     * Beállítja a vászon nagyítását.
     * @param scale a beállítandó nagyítás.
     */
    public void setScale(double scale){
        if (scale>=0.1 && scale<=2.1){
            /*
             * BigDecimal osztály használata, a lebegőpontos értékek kezeléséből adódó pontatlantásgok kiküszöbölésére.
             */
            BigDecimal bd = new BigDecimal(String.valueOf(scale));
            bd = bd.setScale(8, RoundingMode.HALF_UP);
            scale = bd.doubleValue();
            gui.getCanvas().setScale(scale);
            gui.getFooter().setScale(scale);
            canvas.repaint();
        }
    }

    /**
     * Hozzáad a vászon nagyításához.
     * @param scale a hozzá adandó nagyítás.
     */
    public void addScale(double scale){
        scale = scale + canvas.getScale();
        setScale(scale);
    }

}
