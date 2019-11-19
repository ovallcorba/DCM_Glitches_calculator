package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.vava33.jutils.FileUtils;
import com.vava33.jutils.VavaLogger;
import com.vava33.ovPlot.BasicPlotPanel;
import com.vava33.ovPlot.BasicPoint;
import com.vava33.ovPlot.Plottable;
import com.vava33.ovPlot.Plottable_point;
import com.vava33.ovPlot.SerieType;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GlitchPatternFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    BasicPlotPanel glitch;
    private JTable table;
    VavaLogger log = GlitchesMain.getVavaLogger("Glitch_pattern");
    Spagetti spag;

    public GlitchPatternFrame(Spagetti sp) {
        this.spag=sp;
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
                    mntmSave.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            do_mntmSave_actionPerformed(e);
                        }
                    });
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
        glitch.setYlabel("Intensity (a.u.)");
        splitPane.setLeftComponent(glitch.getPlotPanel());
        {
            JScrollPane scrollPane = new JScrollPane();
            splitPane.setRightComponent(scrollPane);
            {
                table = glitch.getTablePlottables();
                scrollPane.setViewportView(table);
            }
        }
        
        
        this.setIconImage(GlitchesMain.getIcon());
        
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
        glitch.addPlottable(p,false);

        p = new EdgeSerie("V-K");
        p.addPoint(new BasicPoint(5.465,0,p));
        glitch.addPlottable(p,false);

        p = new EdgeSerie("Cr-K");
        p.addPoint(new BasicPoint(5.989,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Fe-K");
        p.addPoint(new BasicPoint(7.112,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Co-K");
        p.addPoint(new BasicPoint(7.709,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Ni-K");
        p.addPoint(new BasicPoint(9.333,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Cu-K");
        p.addPoint(new BasicPoint(8.979,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Zn-K");
        p.addPoint(new BasicPoint(9.659,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Ga-K");
        p.addPoint(new BasicPoint(10.367,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Ge-K");
        p.addPoint(new BasicPoint(11.103,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("As-K");
        p.addPoint(new BasicPoint(11.867,0,p));
        glitch.addPlottable(p,false);

        p = new EdgeSerie("Se-K");
        p.addPoint(new BasicPoint(12.658,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Br-K");
        p.addPoint(new BasicPoint(13.474,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Rb-K");
        p.addPoint(new BasicPoint(15.200,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Sr-K");
        p.addPoint(new BasicPoint(16.105,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Y-K");
        p.addPoint(new BasicPoint(17.038,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Zr-K");
        p.addPoint(new BasicPoint(17.998,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Nb-K");
        p.addPoint(new BasicPoint(18.986,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Mo-K");
        p.addPoint(new BasicPoint(20.000,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Tc-K");
        p.addPoint(new BasicPoint(21.011,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Ru-K");
        p.addPoint(new BasicPoint(22.117,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Rh-K");
        p.addPoint(new BasicPoint(23.320,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Pd-K");
        p.addPoint(new BasicPoint(24.350,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Ag-K");
        p.addPoint(new BasicPoint(25.514,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Ce-L3");
        p.addPoint(new BasicPoint(5.723,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Cs-L3");
        p.addPoint(new BasicPoint(5.012,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("La-L3");
        p.addPoint(new BasicPoint(5.891,0,p));
        glitch.addPlottable(p,false);

        p = new EdgeSerie("Ta-L3");
        p.addPoint(new BasicPoint(9.881,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("W-L3");
        p.addPoint(new BasicPoint(10.207,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Re-L3");
        p.addPoint(new BasicPoint(10.535,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Os-L3");
        p.addPoint(new BasicPoint(10.871,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Ir-L3");
        p.addPoint(new BasicPoint(11.215,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Pt-L3");
        p.addPoint(new BasicPoint(11.564,0,p));
        glitch.addPlottable(p,false);

        p = new EdgeSerie("Au-L3");
        p.addPoint(new BasicPoint(11.919,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Tl-L3");
        p.addPoint(new BasicPoint(12.658,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Pd-L3");
        p.addPoint(new BasicPoint(13.035,0,p));
        glitch.addPlottable(p,false);
        
        p = new EdgeSerie("Bi-L3");
        p.addPoint(new BasicPoint(13.419,0,p));
        glitch.addPlottable(p,false);
        
        glitch.actualitzaPlot();
        
    }
    
    protected void do_btnAddCustomEdge_actionPerformed(ActionEvent e) {
        try {
            String newEdge = FileUtils.DialogAskForString(this, "Enter pair [Name Value] (no spaces in the name)", "Add Custom Edge", "Fe-K 7.112");
            String[] s = newEdge.split(" ");
            EdgeSerie p = new EdgeSerie(s[0].trim());
            p.addPoint(new BasicPoint(Double.parseDouble(s[1].trim()),0,p));
            glitch.addPlottable(p,true);
        }catch(Exception ex) {
            log.warning("Error reading custom Edge");
        }
    }
    
    //guardar la serie
    protected void do_mntmSave_actionPerformed(ActionEvent e) {
        File fout = FileUtils.fchooserSaveAsk(this, new File(GlitchesMain.userDir), null, null);
        if (fout!=null) {
            boolean ok = this.writeDAT(fout,this.getPlotPanel().getAllPlottables(),true);
            if (!ok) log.warning("Error writting file");
        }

    }
    
    
    //hi haura una bona i la resta verticals
    private boolean writeDAT(File outf, List<Plottable> ps, boolean overwrite) {
        if (outf.exists()&&!overwrite)return false;
        if (outf.exists()&&overwrite)outf.delete();
        
        boolean written = true;
        
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(outf,true)));            //primer escribim els k-edges de comentaris els comentaris
            StringBuilder sb = new StringBuilder();
            sb.append("# ");
            sb.append(this.getPlotPanel().getPlotTitle());
            sb.append(GlitchesMain.lineSeparator);
            
            //now list of individual glitches positions
            sb.append("# Glitch positions:");
            for (Double d: spag.discreetValuesOfGlitches) {
                sb.append(String.format(" %.6f", d));
            }
            sb.append(GlitchesMain.lineSeparator);
            Plottable glitch = null;
            for (Plottable p:ps) {
                if (p.getSerieType()==SerieType.vertical) {
                    sb.append(String.format("# %s %.6f",p.getName(),p.getPoint(0).getX()));
                    sb.append(GlitchesMain.lineSeparator);
                }
                if (p.getSerieType()==SerieType.xy) {
                    glitch = p;
                }
            }
            String header = sb.toString().replace(",", ".");
            out.print(header);
            out.println("# Glitch Pattern as XY data serie [E(keV) vs Intensity]");
            if (glitch!=null) {
                for (int i=0; i<glitch.getNPoints();i++){
                    Plottable_point pp = glitch.getPoint(i);
                    String towrite = String.format(" %10.7e  %10.7e",pp.getX(),pp.getY());
                    towrite = towrite.replace(",", ".");
                    out.println(towrite);
                }
            }else {
                throw new Exception("no Glitch pattern found");
            }
                        
        } catch (Exception ex) {
            ex.printStackTrace();
            written = false;
        }finally {
            if(out!=null)out.close();
        }
        return written;
    }
    
    
}
