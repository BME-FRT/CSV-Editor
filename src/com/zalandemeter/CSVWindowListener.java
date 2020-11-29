package com.zalandemeter;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * WindowListener-t megvalósító osztály. Kezeli az ablak bezárásakor végrehajtandó műveleteket.
 * @author zalandemeter
 */
public class CSVWindowListener implements WindowListener {

    /**
     * A grafikus megjelenítő keret-e.
     */
    private final JFrame frame;

    /**
     * A koordinátarendszert megjelenítő egységet tárolja.
     */
    private final CSVCanvas canvas;

    /**
     * A fájlokkal kapcsolatos műveleteket tárolja.
     */
    private final CSVFileHandler csvFileHandler;

    /**
     * Az osztály konstruktora. Beállítja a kezelendő grafikus elemeket és kezelőket.
     * @param frame a kezelendő keret.
     * @param canvas a kezelendő vászon.
     * @param csvFileHandler a fájlokat kezelő példány.
     */
    public CSVWindowListener(JFrame frame, CSVCanvas canvas, CSVFileHandler csvFileHandler){
        this.frame = frame;
        this.canvas = canvas;
        this.csvFileHandler = csvFileHandler;
    }

    /**
     * Az ablak bezárásakor amennyiben a megnyitott fájl szerkesztve van, megerősítést kér a felhasználótól a bezárásra.
     * Felajánlja a mentés lehetőségét, mentés nélküli kilépést és megszakítást.
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        if (csvFileHandler.isEdited()){
            Object[] options = {"Close without saving","Save","Cancel"};
            int answer = JOptionPane.showOptionDialog(frame,
                    "Would you like to close without saving?",
                    "Confirm exit",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (answer == JOptionPane.YES_OPTION) {
                frame.dispose();
            }
            if (answer == JOptionPane.NO_OPTION){
                if (csvFileHandler.getCurrentFile() != null){
                    csvFileHandler.saveCSV(csvFileHandler.getCurrentFile());
                } else {
                    csvFileHandler.selectFile(canvas,  CSVFileHandler.ChooseType.save);
                }
            }
        } else {
            frame.dispose();
        }
    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
