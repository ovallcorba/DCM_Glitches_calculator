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

public class Si111 implements CrystalCut {

    static double sqrt3;
    static double sqrt2;
    static String name = "Si111";
    static double dsp_hklref = 3.1356;
    static double si_cell_par_A = 5.43088;
    static int href=1;
    static int kref=1;
    static int lref=1;
    static Cell c;
    VavaLogger log = GlitchesMain.getVavaLogger("Si111");
    
    public Si111() {
        sqrt3 = FastMath.sqrt(3);
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
    public double calcThetaRefl(int h, int k, int l, double azimangleRad) {
        double num = (-h-k+2*l)*FastMath.cos(azimangleRad)+sqrt3*(h-k)*FastMath.sin(azimangleRad);
        double den = sqrt2*(h*h+k*k+l*l-h-k-l);
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