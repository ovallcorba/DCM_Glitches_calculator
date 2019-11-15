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
    static HKLrefl hkl_reflplane;
    static int href=2;
    static int kref=2;
    static int lref=0;
    static Cell c;
    VavaLogger log;

    
    public Si220() {
        sqrt2 = FastMath.sqrt(2);
        double displ = 0.1;
        Atom si = new Atom("Si", "Si", 0.125, 0.125, 0.125, 1, displ);
        List<Atom> cellContent = new ArrayList<Atom>();
        cellContent.add(si);
        c = new Cell(si_cell_par_A,si_cell_par_A,si_cell_par_A, 90,90,90, true, CellSymm_global.getSpaceGroupByNum(227));
        c.setAtoms(cellContent);
        log = new VavaLogger("si220",true,true,false);
        log.setLogLevel("config");
        log.enableLogger(true);
    }
    
    @Override
    public Spagetti calcSpagetti(int maxIndex, double azimIniDeg, double azimFinDeg, double azimStep, double eKevMin, double eKevMax) {
        Spagetti s = new Spagetti("Si220", eKevMin, eKevMax, azimIniDeg, azimFinDeg, azimStep, maxIndex,this);
        List<HKLrefl> listhkl = c.generateHKLsCrystalFamily(calcQmaxFromHKLmax(maxIndex), true,true,true,true,true);
        c.calcInten(true,false);
        
        for (HKLrefl hkl: listhkl) {
            if (FastMath.abs(hkl.getH())>maxIndex)continue;
            if (FastMath.abs(hkl.getK())>maxIndex)continue;
            if (FastMath.abs(hkl.getL())>maxIndex)continue;
            if ((hkl.getH()==href)&&(hkl.getK()==kref)&&(hkl.getL()==lref)) {
                hkl_reflplane = hkl;
            }
            SpagettiHKLserie spHKL = new SpagettiHKLserie(hkl);
            double azim=azimIniDeg;
            while (azim<azimFinDeg) {
                double thetaRad = this.calcThetaRefl(hkl.getH(), hkl.getK(), hkl.getL(), FastMath.toRadians(azim));
                double ekev = this.getEnergyKeV(FastMath.abs(thetaRad));
                if (Double.isFinite(ekev)) {
                    if ((ekev<=eKevMax)&&(ekev>=eKevMin)){
                        double intenlor = hkl.getYcalc()*(1/(FastMath.sin(thetaRad*2)*FastMath.sin(thetaRad)));                  //corregeixo de lonrentz la intensitat de la reflexio
                        spHKL.addPoint(new Spoint(azim,ekev,intenlor,spHKL));
                    }
                }
                azim=azim+azimStep;
            }
            if (spHKL.getNPoints()==0)continue;
            s.addSerie(spHKL);
        }
        return s;
        
    }
    
    public double calcQmaxFromHKLmax(int hklmaxIndex) {
        double dsp = c.calcDspHKL(hklmaxIndex, hklmaxIndex, hklmaxIndex);
        return 1/(dsp*dsp);
    }

    //NOTOS orientation as phi zero (old 30º)
    private double calcThetaRefl(int h, int k, int l, double azimangleRad) {
        double num = 2* (sqrt2*l*FastMath.sin(azimangleRad)+(h-k)*FastMath.cos(azimangleRad));
        double den = h*h+k*k+l*l-2*h-2*k;
        double atan = FastMath.atan(num/den);
        return atan;
    }

    
    private double getEnergyKeV(double thetaRad) {
        //λ(A) = 12.398/E(keV).
        double lambda = 2*d_si111_A*FastMath.sin(thetaRad);
        return 12.398/lambda;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public HKLrefl getHKLreflPlane() {
        return hkl_reflplane;
    }

}
