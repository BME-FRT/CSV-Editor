package com.zalandemeter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * A grafikus megjelenítés menü sávját megvalósító osztály.
 * Tartalmazza a fájl menüt a megjelenítés menüt és a szerkesztés menüt.
 * @author zalandemeter
 */
public class CSVMenuBar extends JMenuBar {
    /**
     * Az objektumok színét beállító menüpont által felvehető értékeket tárolja.
     */
    private final String[] spinnerTypes;

    /**
     * Az objektumok színét beállító menüpont.
     */
    private final JSpinner spinnerColor;

    /**
     * A grafikus megjelenítés interakcióit tartalmazó példány.
     */
    private final GUIHandlers guiHandlers;

    /**
     * Az osztály konstruktora. Iniicializálja a kívülről elérendő menüpontokat. Konfigurálja a megjelenítést.
     * @param guiHandlers A GUI interakcióit tartalmazó példány.
     */
    public CSVMenuBar(GUIHandlers guiHandlers) {
        spinnerTypes = new String[]{"orange", "blue", "yellow", "red", "white", "null"};
        SpinnerListModel listModel = new SpinnerListModel(spinnerTypes);
        spinnerColor = new JSpinner(listModel);
        this.guiHandlers = guiHandlers;
        configure();
    }

    /**
     * Létrehoz egy paraméterként kapott szélességű elosztó elemet.
     * @param w szélesség
     * @return a létrehozott elosztó.
     */
    public JSeparator createSeparator(int w){
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(w,1));
        separator.setForeground(Color.WHITE);
        return separator;
    }

    /**
     * Létrehozza a menüpontokat és beállítja a megjelenítési tulajdonságokat, és hozzárendeli a kezelőket.
     */
    public void configure(){
        /*
         * INITIALIZING
         */
        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveAs = new JMenuItem("Save as");
        JMenuItem newFile = new JMenuItem("New");

        JMenu editMenu = new JMenu("Edit");
        JMenuItem add = new JMenuItem("Add");
        JMenuItem delete = new JMenuItem("Delete");

        JMenu viewMenu = new JMenu("View");
        JMenuItem viewport = new JMenuItem("Reset viewport");
        JMenuItem showCurve = new JMenuItem("Show curve");

        JPanel editor = new JPanel();
        JLabel labelColor = new JLabel("color: ");
        SpinnerNumberModel spinnerNumberModel1 = new SpinnerNumberModel(Item.getObjectSize(),5,100,5);
        JSpinner spinnerSize = new JSpinner(spinnerNumberModel1);
        JLabel labelSize = new JLabel("size: ");
        SpinnerNumberModel spinnerNumberModel2 = new SpinnerNumberModel(Item.getObjectDistance(),5,100,5);
        JSpinner spinnerDistance = new JSpinner(spinnerNumberModel2);
        JLabel labelDistance = new JLabel("distance: ");

        /*
         * FILE SUB-MENU
         */
        newFile.setActionCommand("newFile");
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newFile.addActionListener(e -> guiHandlers.newFile());

        open.setActionCommand("open");
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        open.addActionListener(e -> guiHandlers.openFile());

        save.setActionCommand("save");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        save.addActionListener(e -> guiHandlers.saveFile());

        saveAs.setActionCommand("save as");
        saveAs.addActionListener(e -> guiHandlers.saveFileAs());

        /*
         * EDIT-SUBMENU
         */
        add.setActionCommand("add");
        add.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        add.addActionListener(e -> guiHandlers.addItem());

        delete.setActionCommand("delete");
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
        delete.addActionListener(e -> guiHandlers.deleteItem());

        /*
         * VIEW-SUBMENU
         */
        viewport.setActionCommand("viewport");
        viewport.addActionListener(e -> guiHandlers.resetViewport());

        showCurve.setActionCommand("curve");
        showCurve.addActionListener(e -> guiHandlers.showCurve());

        /*
         * RIGHT-MENUBAR
         */
        FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
        flowLayout.setHgap(0);
        editor.setLayout(flowLayout);
        editor.setBackground(Color.WHITE);

        Component[] comps1 = spinnerColor.getEditor().getComponents();
        for (Component component : comps1) {
            component.setFocusable(false);
        }
        spinnerColor.setPreferredSize(spinnerColor.getMinimumSize());
        spinnerColor.setValue(spinnerTypes[5]);
        spinnerColor.addChangeListener(e -> guiHandlers.changeColor());

        Component[] comps2 = spinnerSize.getEditor().getComponents();
        for (Component component : comps2) {
            component.setFocusable(false);
        }
        spinnerSize.addChangeListener(e -> guiHandlers.setObjectSize((int)spinnerSize.getValue()));

        Component[] comps3 = spinnerDistance.getEditor().getComponents();
        for (Component component : comps3) {
            component.setFocusable(false);
        }
        spinnerDistance.addChangeListener(e -> guiHandlers.setObjectDistance((int)spinnerDistance.getValue()));

        /*
         * ADDING LAYOUT
         */
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(newFile);

        editMenu.add(add);
        editMenu.add(delete);

        viewMenu.add(viewport);
        viewMenu.add(showCurve);

        editor.add(labelColor);
        editor.add(spinnerColor);
        editor.add(createSeparator(10));

        editor.add(labelSize);
        editor.add(spinnerSize);
        editor.add(createSeparator(10));

        editor.add(labelDistance);
        editor.add(spinnerDistance);
        editor.add(createSeparator(10));

        add(fileMenu);
        add(editMenu);
        add(viewMenu);
        add(editor);
    }

    /**
     * A színválasztó menüponthoz tartozó getter.
     * @return a színvűlasztó menüpont.
     */
    public JSpinner getSpinnerColor() {
        return spinnerColor;
    }

    /**
     * A színválasztó menüpont feévehető értékeihez tartozó getter.
     * @return a színválasztó menüpont feévehető értékei.
     */
    public String[] getSpinnerTypes() {
        return spinnerTypes;
    }
}
