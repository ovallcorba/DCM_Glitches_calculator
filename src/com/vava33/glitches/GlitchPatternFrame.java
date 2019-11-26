package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import java.awt.BorderLayout;

import javax.imageio.ImageIO;
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
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GlitchPatternFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    BasicPlotPanel glitch;
    private JTable table;
    VavaLogger log = GlitchesMain.getVavaLogger("Glitch_pattern");

    
    public GlitchPatternFrame() {
        setBounds(100, 100, 951, 638);
        contentPanel.setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
        {
            JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);
            {
                JMenu mnFile = new JMenu("File");
                menuBar.add(mnFile);
                {
                    JMenuItem mntmSave = new JMenuItem("Export as multicolumn ASCII...");
                    mntmSave.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            do_mntmSave_actionPerformed(e);
                        }
                    });
                    mnFile.add(mntmSave);
                }
                {
                    JMenuItem mntmPNG = new JMenuItem("Export as PNG image...");
                    mntmPNG.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            do_mntmPNG_actionPerformed(e);
                        }
                    });
                    mnFile.add(mntmPNG);
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
        splitPane.setLeftComponent(glitch);
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

    private void do_okButton_actionPerformed(ActionEvent e) {
        this.dispose();
    }
    public BasicPlotPanel getPlotPanel() {
        return glitch;
    }
    
    private void do_btnAddCommonEdges_actionPerformed(ActionEvent e) {
        EdgeSerie p = new EdgeSerie("Ti-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(4.366,0,p));
        glitch.addPlottable(p,false,false);

        p = new EdgeSerie("V-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(5.465,0,p));
        glitch.addPlottable(p,false,false);

        p = new EdgeSerie("Cr-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(5.989,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Fe-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(7.112,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Co-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(7.709,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Ni-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(9.333,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Cu-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(8.979,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Zn-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(9.659,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Ga-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(10.367,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Ge-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(11.103,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("As-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(11.867,0,p));
        glitch.addPlottable(p,false,false);

        p = new EdgeSerie("Se-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(12.658,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Br-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(13.474,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Rb-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(15.200,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Sr-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(16.105,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Y-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(17.038,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Zr-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(17.998,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Nb-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(18.986,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Mo-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(20.000,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Tc-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(21.011,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Ru-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(22.117,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Rh-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(23.320,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Pd-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(24.350,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Ag-K");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(25.514,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Ce-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(5.723,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Cs-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(5.012,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("La-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(5.891,0,p));
        glitch.addPlottable(p,false,false);

        p = new EdgeSerie("Ta-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(9.881,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("W-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(10.207,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Re-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(10.535,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Os-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(10.871,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Ir-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(11.215,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Pt-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(11.564,0,p));
        glitch.addPlottable(p,false,false);

        p = new EdgeSerie("Au-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(11.919,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Tl-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(12.658,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Pd-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(13.035,0,p));
        glitch.addPlottable(p,false,false);
        
        p = new EdgeSerie("Bi-L3");
        p.setShowInLegend(false);
        p.addPoint(new BasicPoint(13.419,0,p));
        glitch.addPlottable(p,false,false);
        
        glitch.actualitzaPlot();
        
    }
    
    private void do_btnAddCustomEdge_actionPerformed(ActionEvent e) {
        try {
            String newEdge = FileUtils.DialogAskForString(this, "Enter pair [Name Value] (no spaces in the name)", "Add Custom Edge", "Fe-K 7.112");
            String[] s = newEdge.split(" ");
            EdgeSerie p = new EdgeSerie(s[0].trim());
            p.addPoint(new BasicPoint(Double.parseDouble(s[1].trim()),0,p));
            glitch.addPlottable(p,true,false);
        }catch(Exception ex) {
            log.warning("Error reading custom Edge");
        }
    }
    
    //guardar la serie
    private void do_mntmSave_actionPerformed(ActionEvent e) {
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
            out = new PrintWriter(new BufferedWriter(new FileWriter(outf,true)));            //primer escribim els k-edges com a comentaris
            StringBuilder sb = new StringBuilder();
            int ngpat=1;
            for (Plottable p:ps) {
                
                switch (p.getSerieType()) {
                case vertical:
                    sb.append(String.format("# %s %.6f",p.getName(),p.getCorrectedPoint(0).getX()));
                    sb.append(GlitchesMain.lineSeparator);
                    break;
                case xy:
                    //es un glitch pattern, posem els valors discrets
                    sb.append(String.format("#%d Glitch positions for %s:", ngpat,p.getName()));
                    ngpat++;
                    if (p instanceof GlitchSerie) {
                        GlitchSerie gs = (GlitchSerie)p;
                        for (Double d:gs.discreetValuesOfGlitches) {
                            sb.append(String.format(" %.6f", d));    
                        }
                    }
                    sb.append(GlitchesMain.lineSeparator);
                    break;
                default:
                    break;
                
                }
                
            }
            
            String header = sb.toString().replace(",", ".");
            out.print(header);
            out.println("# Glitch Pattern(s) as [E(keV) vs Intensity1 vs Intensity2 vs Intensity3 ...]");
            List<Plottable> glitchPatterns = new ArrayList<Plottable>();
            for (Plottable p:ps) {
                if (p.getSerieType()==SerieType.xy) {
                    glitchPatterns.add(p);
                }
            }
            
            if (glitchPatterns.size()>0) {
                for (int i=0; i<glitchPatterns.get(0).getNPoints();i++){
                    Plottable_point pp = glitchPatterns.get(0).getCorrectedPoint(i);
                    sb = new StringBuilder();
                    sb.append(String.format(" %12.5e %12.5e",pp.getX(),pp.getY()));
                    for (int j=1;j<glitchPatterns.size();j++) {
                        pp = glitchPatterns.get(j).getCorrectedPoint(i);
                        sb.append(String.format(" %12.5e",pp.getY()));
                    }
                    String towrite = sb.toString(); 
                    towrite = towrite.replace(",", ".");
                    out.println(towrite);
                }
            }else {
                throw new Exception("no Glitch pattern(s) found");
            }
                        
        } catch (Exception ex) {
            ex.printStackTrace();
            written = false;
        }finally {
            if(out!=null)out.close();
        }
        return written;
    }
    
    
    private void do_mntmPNG_actionPerformed(ActionEvent e) {
        File fpng = FileUtils.fchooserSaveAsk(this, new File(GlitchesMain.userDir), null, "png");
        if (fpng!=null){
            int w = this.getPlotPanel().getGraphPanel().getSize().width;
            int h = this.getPlotPanel().getGraphPanel().getSize().height;
            String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Current plot size (Width x Heigth) is "+Integer.toString(w)+" x "+Integer.toString(h)+"pixels\n"
                            + "Scale factor to apply=",
                            "Apply scale factor",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                    "1.0");

            if ((s != null) && (s.length() > 0)) {
                float factor = 1.0f;
                try{
                    factor=Float.parseFloat(s);
                }catch(Exception ex){
                    log.warning("Error reading png scale factor");
                }
                //preguntar si volem fons transparent o no
                boolean transp = FileUtils.YesNoDialog(this, "Create PNG with transparent background?", "PNG transparent background");
                this.getPlotPanel().getGraphPanel().setTransp(transp);
                this.savePNG(fpng,factor);
                this.getPlotPanel().getGraphPanel().setTransp(false);//TODO revisar
            }
        }
    }
    private void savePNG(File fpng, float factor){
        if (!this.getPlotPanel().arePlottables())return;
        try {
            ImageIO.write(this.getPlotPanel().getGraphPanel().pintaPatterns(factor), "png", fpng);
        } catch (Exception ex) {
            log.warning(fpng.toString()+" error writting png file");
            return;
        }
        log.info(fpng.toString()+" written!");
    }
}
