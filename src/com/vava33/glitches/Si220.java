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
import com.vava33.jutils.VavaLogger;

public class Si220 implements CrystalCut {

    static double sqrt2;// = FastMath.sqrt(2);
    static String name = "Si220";
    static double dsp_hklref = 1.9201557;
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
        double lambda = 2*dsp_hklref*FastMath.sin(thetaRad);
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
