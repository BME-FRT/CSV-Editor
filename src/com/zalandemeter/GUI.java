package com.zalandemeter;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import javax.swing.*;

/**
 * A grafikus megjelenítésért felelős osztály.
 * Összeállítja a megjelenítést, hozzáadja a kezelőket a megjelenítés egyes elemeihez.
 * És létrehozza az egeret és fájlokat kezelő páldányokat.
 * @author zalandemeter
 */
public class GUI {

    /**
     * A grafikus megjelenítő keret-e.
     */
    private final JFrame frame;

    /**
     * A grafikus megjelenítő menü sávja.
     */
    private final CSVMenuBar menuBar;

    /**
     * A koordinátarendszert megjelenítő egységet tárolja.
     */
    private final CSVCanvas canvas;

    /**
     * A grafikus megjelenítő lábléce.
     */
    private final CSVFooter footer;

    /**
     * Az egérrel kapcsolatos műveleteket tárolja.
     */
    private final CSVMouseListener csvMouseListener;

    /**
     * A fájlokkal kapcsolatos műveleteket tárolja.
     */
    private final CSVFileHandler csvFileHandler;

    /**
     * A grafikus megjelenítés interakcióit tartalmazó példány.
     */
    private final GUIHandlers guiHandlers;

    /**
     * Az osztály konstruktora, inicializálja a grafikusmegjelenítés különböző részegységeit.
     * Létrehozza a fájlokat és az egeret kezelő példányokat is. Konfigurálja a megjelenítést.
     */
    public GUI(){
        frame = new JFrame("CSV-Editor");
        canvas = new CSVCanvas();
        csvFileHandler = new CSVFileHandler(canvas);
        csvMouseListener = new CSVMouseListener(this);
        guiHandlers = new GUIHandlers(this);
        menuBar = new CSVMenuBar(guiHandlers);
        footer = new CSVFooter(guiHandlers);
        configure();
    }

    /**
     * Beállítja a keret tulajdonságait és elrendezi a részegységeket és hozzárendeli a kezelőket.
     */
    public void configure(){
        /*
         * FRAME
         */
        frame.setMinimumSize(new Dimension(550,450));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new CSVWindowListener(frame,canvas,csvFileHandler));
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ImageIcon img = new ImageIcon("icon.png");
        frame.setIconImage(img.getImage());

        /*
         * CONTENT PANEL
         */
        canvas.addMouseListener(csvMouseListener);
        canvas.addMouseMotionListener(csvMouseListener);
        canvas.addMouseWheelListener(csvMouseListener);

        /*
         * ADDING LAYOUT
         */
        frame.add(menuBar, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Kerethez tartozó getter.
     * @return a grafikus megjelenítés kerete.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Vászonhoz tartozó getter.
     * @return a grafikus megjelenítés vászona.
     */
    public CSVCanvas getCanvas() {
        return canvas;
    }

    /**
     * Egeret kezelő getter.
     * @return az egeret kezelő példány.
     */
    public CSVMouseListener getCsvMouseListener() {
        return csvMouseListener;
    }

    /**
     * Fájlokat kezelő getter.
     * @return a fájlokat kezelő példány.
     */
    public CSVFileHandler getCsvFileHandler() {
        return csvFileHandler;
    }

    /**
     * A guin végrehajtott műveleteket kezelő getter.
     * @return gui kezelő példány.
     */
    public GUIHandlers getGuiHandlers() {
        return guiHandlers;
    }

    /**
     * A lábléc gettere.
     * @return a lábléc példánya.
     */
    public CSVFooter getFooter() {
        return footer;
    }

    /**
     * A menü sáv gettere.
     * @return a menü sáv példánya.
     */
    public CSVMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Az alkalmazást elindító fő függvény. Létrehozza a grafikus megjelenítést.
     * Meghívja a külső könyvtárként hozzáadott megjelenítési témát.
     * @param args a program elindítási argumentimai.
     */
    public static void main(String[] args){
        /*
         * Swing témát állít be, hogy ne az alapértelmezett megjelenítést használja a program.
         * https://www.formdev.com/flatlaf/
         */
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        GUI window = new GUI();
    }
}
