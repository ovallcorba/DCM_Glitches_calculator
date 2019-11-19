package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.FastMath;
import com.vava33.cellsymm.Atom;
import com.vava33.cellsymm.Cell;
import com.vava33.cellsymm.CellSymm_global;
import com.vava33.cellsymm.HKLrefl;
import com.vava33.jutils.VavaLogger;

public class Si220 implements CrystalCut {

    static double sqrt2;// = FastMath.sqrt(2);
    static String name = "Si220";
    static double d_si111_A = 1.9201557;
    static double si_cell_par_A = 5.43088;
    static int href=2;
    static int kref=2;
    static int lref=0;
    static Cell c;
    VavaLogger log = GlitchesMain.getVavaLogger("Si220");

    
    public Si220() {
        sqrt2 = FastMath.sqrt(2);
        double displ = 0.1;
        Atom si = new Atom("Si", "Si", 0.125, 0.125, 0.125, 1, displ);
        List<Atom> cellContent = new ArrayList<Atom>();
        cellContent.add(si);
        c = new Cell(si_cell_par_A,si_cell_par_A,si_cell_par_A, 90,90,90, true, CellSymm_global.getSpaceGroupByNum(227));
        c.setAtoms(cellContent);
    }
    
    @Override
    public double calcDwidth(double wave, HKLrefl hkl, double tthrad) {
        double eradius = 2.8179403227e-15;//m
        double atomicDensity = 5e25;// atoms/m3
        double fh = FastMath.sqrt(hkl.getYcalc());
        
        double dwidth = (wave/2)*(wave/2)*eradius*2.12;
        dwidth = dwidth * ((atomicDensity*fh)/(FastMath.PI*FastMath.sin(tthrad)));
        
        //1 arcsecond ≈ 4.85E-6 radians
        dwidth = dwidth * 4.85e-6;
        double thetaRad = tthrad/2;
        
        double elow = getEnergyKeV(thetaRad-dwidth/2);
        double ehigh = getEnergyKeV(thetaRad+dwidth/2);
        
        return FastMath.abs(elow-ehigh); //darwin width in kev
    }
    
    
    public double calcQmaxFromHKLmax(int hklmaxIndex) {
        double dsp = c.calcDspHKL(hklmaxIndex, hklmaxIndex, hklmaxIndex);
        return 1/(dsp*dsp);
    }

    //NOTOS orientation as phi zero (old 30º)
    @Override
    public double calcThetaRefl(int h, int k, int l, double azimangleRad) {
        double num = 2* (sqrt2*l*FastMath.sin(azimangleRad)+(h-k)*FastMath.cos(azimangleRad));
        double den = h*h+k*k+l*l-2*h-2*k;
        double atan = FastMath.atan(num/den);
        return atan;
    }

    @Override
    public double getEnergyKeV(double thetaRad) {
        //λ(A) = 12.398/E(keV).
        double lambda = 2*d_si111_A*FastMath.sin(thetaRad);
        return 12.398/lambda;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Cell getCell() {
        return c;
    }

    @Override
    public int getHref() {
        return href;
    }

    @Override
    public int getKref() {
        return kref;
    }

    @Override
    public int getLref() {
        return lref;
    }


}
