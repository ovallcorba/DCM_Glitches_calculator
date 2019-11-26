package com.vava33.glitches;

/**
 * Program to calculate glitches "spaghetti" plots given a DCM Si111 or Si220
 * crystal orientation.
 * 
 * Calculations are based on the following publication:
 * 
 * DETERMINATION OF GLITCHES IN SOFT X-RAY MONOCHROMATOR CRYSTALS
 * by Van der Laan and Thole, 
 * Nuclear Instruments and Methods in Physics Research, A263 (1988) 515-521.
 * 
 * It uses the following libraries from the same author:
 *  - com.vava33.BasicPlotPanel
 *  - com.vava33.cellsymm
 *  - com.vava33.vavaUtils
 *
 * And the following 3rd party libraries:
 *  - net.miginfocom.swing.MigLayout
 *  - org.apache.commons.math3.util.FastMath
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.vava33.jutils.FileUtils;
import com.vava33.jutils.VavaLogger;
import com.vava33.ovPlot.BasicPlotPanel;
import com.vava33.ovPlot.Plottable;
import com.vava33.ovPlot.Plottable_point;
import com.vava33.ovPlot.SerieType;

import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import com.vava33.jutils.LogJTextArea;
import java.awt.Insets;

public class GlitchesMain {
    private static String LandF = "system";
    public static final String userDir = System.getProperty("user.dir");
    public static final int build_date = 191121;
    public static final String welcomeMSG = "NOTOS glitches v"+build_date+" by OV\n";
    public static final String lineSeparator = System.getProperty("line.separator");
//    public static boolean logging = true;
    public static String loglevel = "info";
    public static boolean logConsole = false;
    public static boolean logTA = true;
    private GlitchPatternFrame gPattFrame;
    
    public static VavaLogger getVavaLogger(String name){
        VavaLogger l = new VavaLogger(name,logConsole,false,logTA);
        l.setLogLevel(loglevel);
        if (isAnyLogging()) {
            l.enableLogger(true);
        }else {
            l.enableLogger(false);
        }
        return l;
    }
    public static boolean isAnyLogging() {
        if (logConsole || logTA) return true;
        return false;
    }
    
    static VavaLogger log = GlitchesMain.getVavaLogger("NOTOS_glitches");

    public static Image getIcon(){
        return Toolkit.getDefaultToolkit().getImage(GlitchesMain.class.getResource("/com/vava33/glitches/glitches2.png"));
    }
    
    public JFrame mainFrame;
    BasicPlotPanel spagettiPlot;
    private JTextField txtMinE;
    private JTextField txtMaxE;
    private JTextField txtMinazim;
    private JTextField txtMaxazim;
    private JTextField texthklmax;
    private JTextField txtAzimGlitch;
    private JComboBox<String> comboSi;
    Spagetti currentSpagetti; 
    private JTable table;
    private JLabel lblGenerated;
    private JTextField txtFwhm;
    private LogJTextArea tAOut;
    
    private static final double def_stepSizeAzim=0.02; //deg
    
    
    public static void main(String[] args) {
        
        FileUtils.getOS();
        FileUtils.setLocale(null);
        System.out.println(log.logStatus());

        
        try {
            if (FileUtils.containsIgnoreCase(LandF, "system")){
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            if (FileUtils.containsIgnoreCase(LandF, "gtk")){
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            }
            if (FileUtils.containsIgnoreCase(LandF, "metal")){
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
        } catch (Throwable e) {
            log.warning("Error initializing System look and feel");
        }


        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    GlitchesMain frame = new GlitchesMain();
                    frame.showMainFrame();
                } catch (Exception e) {
                    log.severe("Error initializing main window");
                }
            }
        });
        
        
    }

    protected void do_mainFrame_windowClosing(WindowEvent e) {
            mainFrame.dispose();
            System.exit(0);
    }
    
    public void showMainFrame(){
        mainFrame.setVisible(true);
    }
    
    public GlitchesMain() {
        initialize();
        this.spagettiPlot.showHideButtonsPanel();//amagara el menu lateral
    }

    private void initialize() {
        mainFrame = new JFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                do_mainFrame_windowClosing(e);
            }
        });
        mainFrame.setTitle("NOTOS glitch calculator");
        mainFrame.getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setOneTouchExpandable(true);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.8);
        mainFrame.getContentPane().add(splitPane, "cell 0 0,grow");
        
        JPanel panelTop = new JPanel();
        splitPane.setLeftComponent(panelTop);
        panelTop.setLayout(new MigLayout("", "[grow]", "[][grow]"));

        spagettiPlot = new BasicPlotPanel(null);

        panelTop.add(spagettiPlot,"cell 0 1, grow");
        
        JPanel panelOpts = new JPanel();
        panelTop.add(panelOpts, "cell 0 0, grow");
        panelOpts.setLayout(new MigLayout("", "[][][][][grow][grow][][grow][grow][][grow][][][][grow][][grow][]", "[][]"));
        
        JLabel lblCrystal = new JLabel("Crystal");
        panelOpts.add(lblCrystal, "cell 0 0,alignx trailing");
        
        comboSi = new JComboBox<String>();
        comboSi.setModel(new DefaultComboBoxModel<String>(new String[] {"Si111", "Si220"}));
        panelOpts.add(comboSi, "cell 1 0,growx");
        
        JButton btnShowOrientation = new JButton("?");
        btnShowOrientation.setMargin(new Insets(2, 4, 2, 4));
        btnShowOrientation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_btnShowOrientation_actionPerformed(e);
            }
        });
        panelOpts.add(btnShowOrientation, "cell 2 0,growx");
        
        JLabel lblEnergyRange = new JLabel("E Range (keV)");
        panelOpts.add(lblEnergyRange, "cell 3 0,alignx trailing");
        
        txtMinE = new JTextField();
        txtMinE.setText("4.5");
        panelOpts.add(txtMinE, "cell 4 0,growx");
        txtMinE.setColumns(5);
        
        txtMaxE = new JTextField();
        txtMaxE.setText("20");
        panelOpts.add(txtMaxE, "cell 5 0,growx");
        txtMaxE.setColumns(5);
        
        JLabel lblAzimuthalRangedeg = new JLabel("Azim Range (º)");
        panelOpts.add(lblAzimuthalRangedeg, "cell 6 0,alignx trailing");
        
        txtMinazim = new JTextField();
        txtMinazim.setText("-30");
        panelOpts.add(txtMinazim, "cell 7 0,growx");
        txtMinazim.setColumns(5);
        
        txtMaxazim = new JTextField();
        txtMaxazim.setText("30");
        panelOpts.add(txtMaxazim, "cell 8 0,growx");
        txtMaxazim.setColumns(5);
        
        JLabel lblMaxHklIndices = new JLabel("Max HKL index");
        panelOpts.add(lblMaxHklIndices, "cell 9 0,alignx trailing");
        
        texthklmax = new JTextField();
        texthklmax.setText("5");
        panelOpts.add(texthklmax, "cell 10 0,growx");
        texthklmax.setColumns(5);
        
        JButton btnGenerate = new JButton("Spaghetti Plot\n");
        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_btnGenerate_actionPerformed(e);
            }
        });
        panelOpts.add(btnGenerate, "cell 11 0,grow");
        
        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        panelOpts.add(separator, "cell 12 0,grow");
        
        JLabel lblGlitchPatternAt = new JLabel("Glitches at Az (º)=");
        panelOpts.add(lblGlitchPatternAt, "cell 13 0");
        
        txtAzimGlitch = new JTextField();
        panelOpts.add(txtAzimGlitch, "cell 14 0,growx");
        txtAzimGlitch.setText("0");
        txtAzimGlitch.setColumns(5);
        
        JLabel lblFwhm = new JLabel("Delta Azim (º)=");
        panelOpts.add(lblFwhm, "cell 15 0,alignx trailing,aligny center");
        
        txtFwhm = new JTextField();
        txtFwhm.setText("0.1");
        panelOpts.add(txtFwhm, "cell 16 0,growx");
        txtFwhm.setColumns(5);
        
        JButton btnGenerate_1 = new JButton("Glitch Pattern");
        panelOpts.add(btnGenerate_1, "cell 17 0,alignx right");
        btnGenerate_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_btnGenerate_1_actionPerformed(e);
            }
        });
        
        lblGenerated = new JLabel("No plot generated yet");
        panelOpts.add(lblGenerated, "cell 0 1 12 1,alignx left");

        JSplitPane splitPane2 = new JSplitPane();
        splitPane2.setResizeWeight(0.5);
        
        JScrollPane scrollPaneTable = new JScrollPane();
        JScrollPane scrollPane = new JScrollPane();
        tAOut = new LogJTextArea();
        scrollPane.setViewportView(tAOut);
        
        splitPane2.setLeftComponent(scrollPaneTable);
        splitPane2.setRightComponent(scrollPane);
        splitPane.setRightComponent(splitPane2);
        
        table = spagettiPlot.getTablePlottables();
        scrollPaneTable.setViewportView(table);
        
        JMenuBar menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);
        
        JMenu mnExport = new JMenu("File");
        menuBar.add(mnExport);
        
        JMenuItem mntmExportCurrentView = new JMenuItem("Export Current View as PNG");
        mntmExportCurrentView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_mntmExportCurrentView_actionPerformed(e);
            }
        });
        mnExport.add(mntmExportCurrentView);
        
        JMenuItem mntmExportMulticolumnFile = new JMenuItem("Export as ASCII CSV file");
        mntmExportMulticolumnFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_mntmExportMulticolumnFile_actionPerformed(e);
            }
        });
        mnExport.add(mntmExportMulticolumnFile);
        
        JMenuItem mntmQuit = new JMenuItem("Quit");
        mntmQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_mntmQuit_actionPerformed(e);
            }
        });
        mnExport.add(mntmQuit);
        
        JMenuItem mntmAbout = new JMenuItem("About");
        mntmAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_mntmAbout_actionPerformed(e);
            }
        });
        menuBar.add(mntmAbout);

        
        inicia();

    }
    private void inicia() {

        if (logTA)VavaLogger.setTArea(tAOut);
        BasicPlotPanel.setVavaLogger(GlitchesMain.getVavaLogger("Glitches_PlotPanel"));
        log.info(welcomeMSG);
        mainFrame.setIconImage(getIcon());

        mainFrame.setSize(new Dimension(1350,900)); //ho centra el metode main

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        while(mainFrame.getWidth()>screenSize.width){
            mainFrame.setSize(mainFrame.getWidth()-100, mainFrame.getHeight());
        }
        while(mainFrame.getHeight()>screenSize.height){
            mainFrame.setSize(mainFrame.getWidth(), mainFrame.getHeight()-100);
        }

        spagettiPlot.setShowLegend(false);
        spagettiPlot.setLightTheme(false);
        
    }
    
    protected void do_btnShowOrientation_actionPerformed(ActionEvent e) {
        JDialog dialog = new JDialog();
        Image ii = new ImageIcon(BasicPlotPanel.class.getResource("/com/vava33/glitches/axis.png")).getImage().getScaledInstance(-100, 500, java.awt.Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(ii));
        dialog.getContentPane().add( label );
        dialog.pack();
        dialog.setVisible(true);
    }
    
    protected void do_btnGenerate_actionPerformed(ActionEvent e) {
        int plane = comboSi.getSelectedIndex();
        
        //read options
        double minE = Double.parseDouble(txtMinE.getText());
        double maxE = Double.parseDouble(txtMaxE.getText());
        double minAzim = Double.parseDouble(txtMinazim.getText());
        double maxAzim = Double.parseDouble(txtMaxazim.getText());
        int hklMax = Integer.parseInt(texthklmax.getText());

        switch (plane) {
        case 0://si111
            currentSpagetti=new Spagetti(new Si111(),minE,maxE, minAzim, maxAzim, def_stepSizeAzim,hklMax);
            break;
        case 1://si220;
            currentSpagetti=new Spagetti(new Si220(),minE,maxE, minAzim, maxAzim, def_stepSizeAzim,hklMax);
            break;
        }
        
        this.plot(currentSpagetti);
        lblGenerated.setText(String.format("Spaghetti plot for %s",currentSpagetti.crystal.getName()));
    }
    
    private void plot(Spagetti s) {
        spagettiPlot.removeAllPlottables();
        spagettiPlot.setXlabel("Azimuthal angle (º)");
        spagettiPlot.setYlabel("Energy (keV)");
        for (int i=0;i<s.getNseries();i++) {
            spagettiPlot.addPlottable(s.getSerie(i),false,true);    
        }
        spagettiPlot.actualitzaPlot();
    }

    
    protected void do_btnGenerate_1_actionPerformed(ActionEvent e) {
        
        if (currentSpagetti!=null) {
            
            double azim = Double.parseDouble(txtAzimGlitch.getText());
            double fwhm = Double.parseDouble(txtFwhm.getText()); //azimuthal "aperture" in degrees
            
            if ((fwhm/2.)<def_stepSizeAzim) {
                fwhm=def_stepSizeAzim*2.5;
                txtFwhm.setText(String.format("%.3f", fwhm));
            }
                    
            GlitchSerie ser = currentSpagetti.getGlitchCut2(azim,fwhm);
            
            if (gPattFrame!=null) {
                if (gPattFrame.isVisible()) {
                    //ask if add to current Glitch pattern
                    boolean yes = FileUtils.YesNoDialog(this.mainFrame, "Add to current Glitch Pattern?", "Add to existing plot");
                    if (yes) {
                        //L'afegim per sobre les verticals (edges)
                        int pos = gPattFrame.getPlotPanel().getNplottablesOfSerieTypes(false, SerieType.xy);
                        gPattFrame.getPlotPanel().addPlottable(ser, pos, true,true);
                        gPattFrame.getPlotPanel().setPlotTitle("");
                        gPattFrame.getPlotPanel().setShowLegend(true);
                        gPattFrame.setTitle("Glitch patterns");
                        return;
                    }else {
//                        gPattFrame.dispose();
                    }
                }
            }
            //si estem aqui o bé es el primer que generem o be no volem afegir-lo a l'actual
            gPattFrame = new GlitchPatternFrame();
            gPattFrame.getPlotPanel().addPlottable(ser,true,true);
            gPattFrame.getPlotPanel().setShowLegend(false);
            gPattFrame.getPlotPanel().setPlotTitle(ser.getName());
            gPattFrame.setTitle(ser.getName());
            gPattFrame.setVisible(true);
        }
        
        
    }
    protected void do_mntmExportCurrentView_actionPerformed(ActionEvent e) {
        File fpng = FileUtils.fchooserSaveAsk(mainFrame, new File(userDir), null, "png");
        if (fpng!=null){
            int w = spagettiPlot.getGraphPanel().getSize().width;
            int h = spagettiPlot.getGraphPanel().getSize().height;
            String s = (String)JOptionPane.showInputDialog(
                    mainFrame,
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
                this.savePNG(fpng,factor);
            }
        }
    }
    
    private void savePNG(File fpng, float factor){
        if (!spagettiPlot.arePlottables())return;
        try {
            ImageIO.write(spagettiPlot.getGraphPanel().pintaPatterns(factor), "png", fpng);
        } catch (Exception ex) {
            log.warning(fpng.toString()+" error writting png file");
            return;
        }
        log.info(fpng.toString()+" written!");
    }
    
    protected void do_mntmExportMulticolumnFile_actionPerformed(ActionEvent e) {
        if (spagettiPlot.getTablePlottables().getRowCount()<=0)return;

        List<Plottable> dss = spagettiPlot.getAllPlottables();

        if (dss.size()==1) {
            //normal save
            File datFile = FileUtils.fchooserSaveAsk(mainFrame,new File(userDir), null, null);
            if (datFile == null){
                log.warning("No data file selected");
                return;
            }

            boolean ok = this.writeDAT(dss.get(0), datFile, true);
            if (ok) {
                log.info(datFile.toString()+" written!");
            }else {
                log.info("error writting "+datFile.toString());
            }
        }else { //more than one (always will be like this..
            File datFile = FileUtils.fchooserSaveAsk(mainFrame,new File(userDir), null, null);
            if (datFile == null){
                log.warning("No file selected");
                return;
            }
            boolean ok = this.writeMDAT(dss,datFile, true);
            if (ok) {
                log.info(datFile.toString()+" written!");    
            }else {
                log.warning("error writting file");
            }
        }
    }
    
    
    private boolean writeDAT(Plottable ds, File outf, boolean overwrite){
        if (outf.exists()&&!overwrite)return false;
        if (outf.exists()&&overwrite)outf.delete();
        
        boolean written = true;
        
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(outf,true)));            //primer escribim els comentaris
            out.println("# "+ds.getName());
            out.println("# "+currentSpagetti.toString());
            
            for (int i=0; i<ds.getNPoints();i++){
                Plottable_point pp = ds.getCorrectedPoint(i);
                String towrite = String.format(" %10.7e  %10.7e",pp.getX(),pp.getY());
                towrite = towrite.replace(",", ".");
                out.println(towrite);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            written = false;
        }finally {
            if(out!=null)out.close();
        }
        return written;
    }
    
    public boolean writeMDAT(List<Plottable> dss, File outf, boolean overwrite){
        if (outf.exists()&&!overwrite)return false;
        if (outf.exists()&&overwrite)outf.delete();
        
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(outf,true)));            
            //primer escribim tantes linies de comentari com noms de series (o fitxers)
            StringBuilder header = new StringBuilder();
            header.append(String.format("%s,", "Azim(º)"));
            for (int ids=0;ids<dss.size();ids++) {
                header.append(String.format("%s,", "["+dss.get(ids).getName()+"]"));
            }
            out.println(header.toString());

            //ATENCIO les series no son continues, cal anar azimuth per azimuth i no agafar la primera com a ref
            double azim = currentSpagetti.minAzim;
            double tol = currentSpagetti.stepSizeAzim/2.;
            while (azim<currentSpagetti.maxAzim) {
                StringBuilder line = new StringBuilder();
                    line.append(String.format("%12.5e,",azim));    
                for (Plottable p:dss) {
                    Plottable_point pp = p.getPoint(azim,tol);
                    if (pp!=null) {
                        line.append(String.format("%12.5e,",pp.getY()));    
                    }else {
                        line.append(String.format("%s"," ,"));
                    }
                }
                out.println(line.toString());
                azim = azim+currentSpagetti.stepSizeAzim;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }finally {
            if(out!=null)out.close();
        }
        return true;
    }
    
    protected void do_mntmQuit_actionPerformed(ActionEvent e) {
        this.do_mainFrame_windowClosing(null);
    }
    protected void do_mntmAbout_actionPerformed(ActionEvent e) {
        AboutDialog about = new AboutDialog(this.mainFrame);
        about.visible(true);
    }
}
