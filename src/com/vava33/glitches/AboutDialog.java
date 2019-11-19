package com.vava33.glitches;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Color;

public class AboutDialog {
	
	private JDialog aboutDialog;
    private JPanel contentPanel;
    private JLabel lblTalplogo;
    private JLabel lblLogoalba;
    private JTextArea txtrNuclearInstrumentsAnd;

    
    /**
     * Create the dialog.
     */
    public AboutDialog(JFrame parent) {
    	this.aboutDialog = new JDialog(parent,"About this program",true);
    	this.contentPanel = new JPanel();
    	aboutDialog.setIconImage(GlitchesMain.getIcon());
    	aboutDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 400;
        int height = 200;
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        aboutDialog.setBounds(x, y, 608, 237);
        aboutDialog.getContentPane().setLayout(new BorderLayout());
        this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        aboutDialog.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new MigLayout("", "[grow][grow]", "[][grow]"));
        
                {
                    lblLogoalba = new JLabel("");
                    lblLogoalba.setIcon(new ImageIcon(AboutDialog.class.getResource("/com/vava33/glitches/ALBALogo.png")));
                    contentPanel.add(lblLogoalba, "cell 0 0,alignx center");
                }
        {
            JPanel buttonPane = new JPanel();
            aboutDialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
            
            JButton okButton = new JButton("Close");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    do_okButton_actionPerformed(arg0);
                }
            });
            okButton.setActionCommand("OK");
            buttonPane.add(okButton, "cell 1 0,alignx right,aligny top");
            aboutDialog.getRootPane().setDefaultButton(okButton);
        }

        // posem el logo escalat
        Image img = GlitchesMain.getIcon();
        Image newimg = img.getScaledInstance(-100, 64, java.awt.Image.SCALE_SMOOTH);
        ImageIcon logo = new ImageIcon(newimg);
        {
            lblTalplogo = new JLabel("** LOGO **");
            contentPanel.add(lblTalplogo, "flowx,cell 1 0,alignx center,aligny center");
        }
        lblTalplogo.setText("");
        lblTalplogo.setIcon(logo);
        
        txtrNuclearInstrumentsAnd = new JTextArea();
        txtrNuclearInstrumentsAnd.setForeground(Color.YELLOW);
        txtrNuclearInstrumentsAnd.setBackground(Color.BLACK);
        txtrNuclearInstrumentsAnd.setLineWrap(true);
        txtrNuclearInstrumentsAnd.setText("Calculation of Glitches for the NOTOS DCM (Si111 and Si220) by O.Vallcorba. (19/11/2019)\nBased on:\n\"DETERMINATION OF GLITCHES IN SOFT X-RAY MONOCHROMATOR CRYSTALS\" by Van der Laan and Thole, Nuclear Instruments and Methods in Physics Research, A263 (1988) 515-521.\n");
        contentPanel.add(txtrNuclearInstrumentsAnd, "cell 0 1 2 1,grow");
       
    }
    
    private void do_okButton_actionPerformed(ActionEvent arg0) {
        this.tanca();
    }
    
    public void tanca() {
    	aboutDialog.dispose();
    }

	public void visible(boolean vis) {
		aboutDialog.setVisible(vis);
	}
}
