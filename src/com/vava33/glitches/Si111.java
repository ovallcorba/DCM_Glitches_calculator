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

public class Si111 implements CrystalCut {

    static double sqrt3;
    static double sqrt2;
    static String name = "Si111";
    static double d_si111_A = 3.1356;
    static double si_cell_par_A = 5.43088;
    static HKLrefl hkl_reflplane;
    static int href=1;
    static int kref=1;
    static int lref=1;
    static Cell c;
    VavaLogger log;
    
    public Si111() {
        sqrt3 = FastMath.sqrt(3);
        sqrt2 = FastMath.sqrt(2);
        double displ = 0.1;
        Atom si = new Atom("Si", "Si", 0.125, 0.125, 0.125, 1, displ);
        List<Atom> cellContent = new ArrayList<Atom>();
        cellContent.add(si);
        c = new Cell(si_cell_par_A,si_cell_par_A,si_cell_par_A, 90,90,90, true, CellSymm_global.getSpaceGroupByNum(227));
        c.setAtoms(cellContent);
        log = new VavaLogger("si111",true,true,false);
        log.setLogLevel("config");
        log.enableLogger(true);
    }
    
    @Override
    public Spagetti calcSpagetti(int maxIndex, double azimIniDeg, double azimFinDeg, double azimStep, double eKevMin, double eKevMax) {
        Spagetti s = new Spagetti("Si111", eKevMin, eKevMax, azimIniDeg, azimFinDeg, azimStep, maxIndex,this);
        List<HKLrefl> listhkl = c.generateHKLsCrystalFamily(calcQmaxFromHKLmax(maxIndex), true,true,true,true,true);
        c.calcInten(true,false);
        
        for (HKLrefl hkl: listhkl) {
            if (FastMath.abs(hkl.getH())>maxIndex)continue;
            if (FastMath.abs(hkl.getK())>maxIndex)continue;
            if (FastMath.abs(hkl.getL())>maxIndex)continue;
            if ((hkl.getH()==href)&&(hkl.getK()==kref)&&(hkl.getL()==lref)) {
                hkl_reflplane = hkl;
            }
            
            log.info(hkl.toString());
            
            SpagettiHKLserie spHKL = new SpagettiHKLserie(hkl);
            double azim=azimIniDeg;
            while (azim<azimFinDeg) {
                double thetaRad = this.calcThetaRefl(hkl.getH(), hkl.getK(), hkl.getL(), FastMath.toRadians(azim));
                double ekev = this.getEnergyKeV(FastMath.abs(thetaRad));
                if (Double.isFinite(ekev)) {
                    if ((ekev<=eKevMax)&&(ekev>=eKevMin)){
                        double intenlor = hkl.getYcalc()*(1/(FastMath.sin(thetaRad*2)*FastMath.sin(thetaRad)));                  //corregeixo de lonrentz la intensitat de la reflexio
                        spHKL.addPoint(new Spoint(azim,ekev,intenlor,spHKL));
                        double dwidth = calcDwidth2(ekev,hkl);
                        ((Spoint)(spHKL.getPoint(spHKL.getNPoints()-1))).dwidth=dwidth;
                    }
                }
                azim=azim+azimStep;
            }
            if (spHKL.getNPoints()==0)continue;
            s.addSerie(spHKL);
        }
        return s;
        
    }
    
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
    
    public double calcDwidth2(double ekev, HKLrefl hkl) {
        double eradius = 2.8179403227e-15;//m
        double eradius_Ang = eradius*1e10;
        double fh = FastMath.sqrt(hkl.getYcalc());
        double dwidth = (4/FastMath.PI)*(hkl.getDsp()*hkl.getDsp())*((eradius_Ang*fh)/(si_cell_par_A*si_cell_par_A+si_cell_par_A));
        dwidth = dwidth * (3/(2*sqrt2));
        return dwidth*ekev;
    }
    
    
    public double calcQmaxFromHKLmax(int hklmaxIndex) {
        double dsp = c.calcDspHKL(hklmaxIndex, hklmaxIndex, hklmaxIndex);
        return 1/(dsp*dsp);
    }

    //NOTOS orientation as phi zero (old 30º)
    private double calcThetaRefl(int h, int k, int l, double azimangleRad) {
        double num = (-h-k+2*l)*FastMath.cos(azimangleRad)+sqrt3*(h-k)*FastMath.sin(azimangleRad);
        double den = sqrt2*(h*h+k*k+l*l-h-k-l);
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
    