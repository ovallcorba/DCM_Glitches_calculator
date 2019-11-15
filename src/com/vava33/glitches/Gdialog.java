package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.vava33.jutils.FileUtils;
import com.vava33.ovPlot.BasicPlotPanel;
import com.vava33.ovPlot.BasicPoint;
import com.vava33.ovPlot.Plottable;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Gdialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    BasicPlotPanel glitch;
    private JTable table;
    
    public Gdialog() {
        setBounds(100, 100, 951, 638);
        contentPanel.setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
        {
            JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);
            {
                JMenu mnFile = new JMenu("File");
                menuBar.add(mnFile);
                {
                    JMenuItem mntmSave = new JMenuItem("Export...");
                    mnFile.add(mntmSave);
                }
            }
        }
        getContentPane().add(contentPanel);
        {
            JPanel buttonPane = new JPanel();
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("Close");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        do_okButton_actionPerformed(e);
                    }
                });
                buttonPane.setLayout(new MigLayout("", "[][][grow]", "[25px]"));
                {
                    JButton btnAddCommonEdges = new JButton("Add Common Edges");
                    btnAddCommonEdges.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            do_btnAddCommonEdges_actionPerformed(e);
                        }
                    });
                    buttonPane.add(btnAddCommonEdges, "cell 0 0");
                }
                {
                    JButton btnAddCustomEdge = new JButton("Add custom edge");
                    btnAddCustomEdge.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            do_btnAddCustomEdge_actionPerformed(e);
                        }
                    });
                    buttonPane.add(btnAddCustomEdge, "cell 1 0");
                }
                okButton.setActionCommand("OK");
                buttonPane.add(okButton, "cell 2 0,alignx right,aligny top");
                getRootPane().setDefaultButton(okButton);
            }
        }
        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.9);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        contentPanel.add(splitPane, "cell 0 0,grow");
        glitch = new BasicPlotPanel(null);
        glitch.showHideButtonsPanel();
        glitch.setXlabel("Energy (keV)");
        glitch.setYlabel("Intensity");
        splitPane.setLeftComponent(glitch.getPlotPanel());
        {
            JScrollPane scrollPane = new JScrollPane();
            splitPane.setRightComponent(scrollPane);
            {
                table = glitch.getTablePlottables();
                scrollPane.setViewportView(table);
            }
        }
        
    }

    protected void do_okButton_actionPerformed(ActionEvent e) {
        this.dispose();
    }
    public BasicPlotPanel getPlotPanel() {
        return glitch;
    }
    
    protected void do_btnAddCommonEdges_actionPerformed(ActionEvent e) {
        EdgeSerie p = new EdgeSerie("Ti-K");
        p.addPoint(new BasicPoint(4.366,0,p));
        glitch.addPlottable(p);

        p = new EdgeSerie("V-K");
        p.addPoint(new BasicPoint(5.465,0,p));
        glitch.addPlottable(p);

        p = new EdgeSerie("Cr-K");
        p.addPoint(new BasicPoint(5.989,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Fe-K");
        p.addPoint(new BasicPoint(7.112,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Co-K");
        p.addPoint(new BasicPoint(7.709,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Ni-K");
        p.addPoint(new BasicPoint(9.333,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Cu-K");
        p.addPoint(new BasicPoint(8.979,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Zn-K");
        p.addPoint(new BasicPoint(9.659,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Ga-K");
        p.addPoint(new BasicPoint(10.367,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Ge-K");
        p.addPoint(new BasicPoint(11.103,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("As-K");
        p.addPoint(new BasicPoint(11.867,0,p));
        glitch.addPlottable(p);

        p = new EdgeSerie("Se-K");
        p.addPoint(new BasicPoint(12.658,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Br-K");
        p.addPoint(new BasicPoint(13.474,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Rb-K");
        p.addPoint(new BasicPoint(15.200,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Sr-K");
        p.addPoint(new BasicPoint(16.105,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Y-K");
        p.addPoint(new BasicPoint(17.038,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Zr-K");
        p.addPoint(new BasicPoint(17.998,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Nb-K");
        p.addPoint(new BasicPoint(18.986,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Mo-K");
        p.addPoint(new BasicPoint(20.000,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Tc-K");
        p.addPoint(new BasicPoint(21.011,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Ru-K");
        p.addPoint(new BasicPoint(22.117,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Rh-K");
        p.addPoint(new BasicPoint(23.320,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Pd-K");
        p.addPoint(new BasicPoint(24.350,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Ag-K");
        p.addPoint(new BasicPoint(25.514,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Ce-L3");
        p.addPoint(new BasicPoint(5.723,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Cs-L3");
        p.addPoint(new BasicPoint(5.012,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("La-L3");
        p.addPoint(new BasicPoint(5.891,0,p));
        glitch.addPlottable(p);

        p = new EdgeSerie("Ta-L3");
        p.addPoint(new BasicPoint(9.881,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("W-L3");
        p.addPoint(new BasicPoint(10.207,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Re-L3");
        p.addPoint(new BasicPoint(10.535,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Os-L3");
        p.addPoint(new BasicPoint(10.871,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Ir-L3");
        p.addPoint(new BasicPoint(11.215,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Pt-L3");
        p.addPoint(new BasicPoint(11.564,0,p));
        glitch.addPlottable(p);

        p = new EdgeSerie("Au-L3");
        p.addPoint(new BasicPoint(11.919,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Tl-L3");
        p.addPoint(new BasicPoint(12.658,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Pd-L3");
        p.addPoint(new BasicPoint(13.035,0,p));
        glitch.addPlottable(p);
        
        p = new EdgeSerie("Bi-L3");
        p.addPoint(new BasicPoint(13.419,0,p));
        glitch.addPlottable(p);
        
    }
    protected void do_btnAddCustomEdge_actionPerformed(ActionEvent e) {
        try {
            String newEdge = FileUtils.DialogAskForString(this, "Enter pair [Name Value] (no spaces in the name)", "Add Custom Edge", "Fe-K 7.112");
            String[] s = newEdge.split(" ");
            EdgeSerie p = new EdgeSerie(s[0].trim());
            p.addPoint(new BasicPoint(Double.parseDouble(s[1].trim()),0,p));
            glitch.addPlottable(p);
        }catch(Exception ex) {
            System.out.println("Error reading custom Edge");
            ex.printStackTrace();
        }
    }
}
