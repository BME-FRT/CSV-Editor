package com.zalandemeter;

import javax.swing.*;
import java.awt.*;

public class CSVFooter extends JPanel {
    /**
     * A vászon aktuális nagyítását kijelző szövegmező.
     */
    private final JTextField scaleText;

    /**
     * A vászon nagyítását állítani képes csúszka.
     */
    private final JSlider scaleSlider;

    /**
     * A kurzor aktuális helyzetéhez tartozó koordináta transzformált koordináták kijelzése.
     */
    private final JLabel relativeCoords;

    /**
     * A grafikus megjelenítés interakcióit tartalmazó példány.
     */
    private final GUIHandlers guiHandlers;

    /**
     * Az osztály konstruktora. Iniicializálja a kívülről elérendő menüpontokat. Konfigurálja a megjelenítést.
     * @param guiHandlers A GUI interakcióit tartalmazó példány.
     */
    public CSVFooter(GUIHandlers guiHandlers){
        scaleSlider = new JSlider(JSlider.HORIZONTAL, 10, 210, 110);
        scaleText = new JTextField("100");
        relativeCoords = new JLabel("x: 0    y: 0");
        this.guiHandlers = guiHandlers;
        configure();
    }

    /**
     * Létrehozza a menüpontokat és beállítja a megjelenítési tulajdonságokat, és hozzárendeli a kezelőket.
     */
    public void configure(){
        /*
         * INITIALIZING
         */
        JPanel leftFooter = new JPanel();
        JPanel rightFooter = new JPanel();
        JButton defaultScale = new JButton("Default");
        JButton minus = new JButton("-");
        JButton plus = new JButton("+");

        /*
         * FOOTER PANEL
         */
        setLayout(new BorderLayout());
        leftFooter.setLayout(new FlowLayout(FlowLayout.LEFT));
        rightFooter.setLayout(new FlowLayout(FlowLayout.RIGHT));

        scaleText.setEditable(false);
        scaleText.setFocusable(false);

        scaleSlider.setMajorTickSpacing(50);
        scaleSlider.setMinorTickSpacing(10);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setFocusable(false);
        scaleSlider.setSnapToTicks(true);
        scaleSlider.addChangeListener(e -> guiHandlers.setScale((double)scaleSlider.getValue()/100));

        defaultScale.setFocusable(false);
        defaultScale.addActionListener(e -> guiHandlers.setScale(1.1));

        minus.setFocusable(false);
        minus.addActionListener(e -> guiHandlers.addScale(-0.1));

        plus.setFocusable(false);
        plus.addActionListener(e -> guiHandlers.addScale(+0.1));

        add(leftFooter, BorderLayout.WEST);
        add(rightFooter, BorderLayout.EAST);

        leftFooter.add(relativeCoords);

        rightFooter.add(scaleText);
        rightFooter.add(minus);
        rightFooter.add(scaleSlider);
        rightFooter.add(plus);
        rightFooter.add(defaultScale);
    }

    /**
     * Beállítja paraméterként kapott értékre a nagyítást megjelenítő elemeket.
     * @param scale a beállítandó méret.
     */
    public void setScale(double scale){
        scaleSlider.setValue((int)(scale*100));
        scaleText.setText(String.valueOf((int)(scale*100)-10));
    }

    /**
     * Nagyítás szövegmenzőhöz tartozó getter.
     * @return nagyítás szövegmező.
     */
    public JTextField getScaleText() {
        return scaleText;
    }

    /**
     * Nagyítás csúszkához tartozó getter.
     * @return nagyítás csúszka.
     */
    public JSlider getScaleSlider() {
        return scaleSlider;
    }

    /**
     * Beállítja a kurzor pozíciót megjelenítő szöveget.
     * @param text a beállítandó szöveg.
     */
    public void setRelativeCoords(String text) {
        relativeCoords.setText(text);
    }
}
