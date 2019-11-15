package com.vava33.glitches;

/**
 * Program to calculate glitches "spaghetti" plots given a DCM Si111 or Si220
 * crystal orientation.
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
import com.vava33.ovPlot.Plottable_point;

import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Font;

public class GlitchesMain {
    private static String LandF = "system";
    public JFrame mainFrame;
    static VavaLogger log;
    BasicPlotPanel spagettiPlot;
    private JTextField txtMinE;
    private JTextField txtMaxE;
    private JTextField txtMinazim;
    private JTextField txtMaxazim;
    private JTextField texthklmax;
    private JTextField txtAzimGlitch;
    private JTextField txtdeltaAzim;
    private JComboBox<String> comboSi;
    Spagetti currentSpagetti; 
    private JTable table;
    private JLabel lblGenerated;
    private JTextField txtFwhm;
    
    public static void main(String[] args) {
        //first thing to do is read PAR files if exist
        FileUtils.getOS();
        FileUtils.setLocale(null);

        //LOGGER
        log = new VavaLogger("glitch_notos",true,false,false);
        log.setLogLevel("config");
        log.enableLogger(true);
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
        splitPane.setResizeWeight(0.7);
        splitPane.setOneTouchExpandable(true);
        mainFrame.getContentPane().add(splitPane, "cell 0 0,grow");
        
        JPanel panelTop = new JPanel();
        splitPane.setLeftComponent(panelTop);
        panelTop.setLayout(new MigLayout("", "[grow]", "[][grow]"));

        spagettiPlot = new BasicPlotPanel(null);

        panelTop.add(spagettiPlot.getPlotPanel(),"cell 0 1, grow");
        
        JPanel panelOpts = new JPanel();
        panelTop.add(panelOpts, "cell 0 0, grow");
        panelOpts.setLayout(new MigLayout("", "[][][][grow][grow][][grow][grow][][][grow][]", "[][][]"));
        
        JLabel lblCrystal = new JLabel("Crystal");
        panelOpts.add(lblCrystal, "cell 0 0,alignx trailing");
        
        comboSi = new JComboBox<String>();
        comboSi.setModel(new DefaultComboBoxModel<String>(new String[] {"Si111", "Si220"}));
        panelOpts.add(comboSi, "cell 1 0,growx");
        
        JButton btnShowOrientation = new JButton("show orientation");
        btnShowOrientation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_btnShowOrientation_actionPerformed(e);
            }
        });
        
        JLabel lblEnergyRange = new JLabel("Energy Range (keV)");
        panelOpts.add(lblEnergyRange, "cell 2 0,alignx trailing");
        
        txtMinE = new JTextField();
        txtMinE.setText("4.5");
        panelOpts.add(txtMinE, "cell 3 0,growx");
        txtMinE.setColumns(5);
        
        txtMaxE = new JTextField();
        txtMaxE.setText("20");
        panelOpts.add(txtMaxE, "cell 4 0,growx");
        txtMaxE.setColumns(5);
        
        JLabel lblMaxHklIndices = new JLabel("Max HKL index");
        panelOpts.add(lblMaxHklIndices, "cell 5 0,alignx trailing");
        
        texthklmax = new JTextField();
        texthklmax.setText("5");
        panelOpts.add(texthklmax, "cell 6 0,growx");
        texthklmax.setColumns(5);
        
        JButton btnGenerate = new JButton("<html><center>Generate<br> Spaghetti<br> Plot </center></html>");
        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_btnGenerate_actionPerformed(e);
            }
        });
        panelOpts.add(btnGenerate, "cell 7 0 1 2,grow");
        
        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        panelOpts.add(separator, "cell 8 0 1 2,grow");
        
        JLabel lblGlitchPatternAt = new JLabel("Glitches at Az (º)=");
        panelOpts.add(lblGlitchPatternAt, "cell 9 0");
        
        txtAzimGlitch = new JTextField();
        panelOpts.add(txtAzimGlitch, "cell 10 0,growx");
        txtAzimGlitch.setText("0");
        txtAzimGlitch.setColumns(5);
        
        JButton btnGenerate_1 = new JButton("<html><center>Generate<br> Glitch<br> Pattern </center></html>");
        panelOpts.add(btnGenerate_1, "cell 11 0 1 2,alignx right");
        btnGenerate_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_btnGenerate_1_actionPerformed(e);
            }
        });
        panelOpts.add(btnShowOrientation, "cell 0 1 2 1,growx");
        
        JLabel lblAzimuthalRangedeg = new JLabel("Azimuthal Range (deg)");
        panelOpts.add(lblAzimuthalRangedeg, "cell 2 1,alignx trailing");
        
        txtMinazim = new JTextField();
        txtMinazim.setText("-30");
        panelOpts.add(txtMinazim, "cell 3 1,growx");
        txtMinazim.setColumns(5);
        
        txtMaxazim = new JTextField();
        txtMaxazim.setText("30");
        panelOpts.add(txtMaxazim, "cell 4 1,growx");
        txtMaxazim.setColumns(5);
        
        JLabel lblDeltaAzim = new JLabel("Delta Azim (º)");
        panelOpts.add(lblDeltaAzim, "cell 5 1,alignx trailing");
        
        txtdeltaAzim = new JTextField();
        txtdeltaAzim.setText("0.05");
        panelOpts.add(txtdeltaAzim, "cell 6 1,growx");
        txtdeltaAzim.setColumns(5);
        
        JLabel lblFwhm = new JLabel("Glitch FWHM (eV)=");
        panelOpts.add(lblFwhm, "cell 9 1,alignx trailing,aligny center");
        
        txtFwhm = new JTextField();
        txtFwhm.setText("5");
        panelOpts.add(txtFwhm, "cell 10 1,growx");
        txtFwhm.setColumns(10);
        
        lblGenerated = new JLabel("");
        lblGenerated.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
        panelOpts.add(lblGenerated, "cell 0 2 8 1,alignx center");
        
        JScrollPane scrollPaneTable = new JScrollPane();
        splitPane.setRightComponent(scrollPaneTable);
        
        table = spagettiPlot.getTablePlottables();
        scrollPaneTable.setViewportView(table);

        
        inicia();

    }
    private void inicia() {

        mainFrame.setSize(new Dimension(1350,900));

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
        double deltaAzim = Double.parseDouble(txtdeltaAzim.getText());

        switch (plane) {
        case 0://si111
            Si111 si111 = new Si111();
            currentSpagetti=si111.calcSpagetti(hklMax, minAzim, maxAzim, deltaAzim, minE, maxE);
            break;
        case 1://si220;
            Si220 si220 = new Si220();
            currentSpagetti=si220.calcSpagetti(hklMax, minAzim, maxAzim, deltaAzim, minE, maxE);
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
            s.getSerie(i).setColor(spagettiPlot.getNextColor());
            spagettiPlot.addPlottable(s.getSerie(i));    
        }
        spagettiPlot.actualitzaPlot();
    }

    
    protected void do_btnGenerate_1_actionPerformed(ActionEvent e) {
        
        if (currentSpagetti!=null) {
            
            double azim = Double.parseDouble(txtAzimGlitch.getText());
            double fwhm = Double.parseDouble(txtFwhm.getText());
                    
            GlitchSerie ser = currentSpagetti.getGlitchCut(azim,fwhm/1000.);
            
            Gdialog gd = new Gdialog();
            gd.getPlotPanel().addPlottable(ser);
            gd.setVisible(true);
            
        }
        
        
    }
}
