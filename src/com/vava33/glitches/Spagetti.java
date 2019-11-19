package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.util.FastMath;

import com.vava33.cellsymm.HKLrefl;
import com.vava33.jutils.VavaLogger;
import com.vava33.ovPlot.Plottable_point;

public class Spagetti {

    ArrayList<SpagettiHKLserie> series;
    String name;
    double minE,maxE,minAzim,maxAzim,deltaAzim;
    int maxHKLindex;
    CrystalCut crystal;
    HKLrefl hkl_reflplane;
    static double stepEglitch = 0.001; //1eV
    VavaLogger log = GlitchesMain.getVavaLogger("Spagetti");
    //test:
    ArrayList<Double> discreetValuesOfGlitches;
    
    public Spagetti(CrystalCut crys, double minE, double maxE, double minAzim, double maxAzim, double deltaAzim, int maxHKLindex) {
        series = new ArrayList<SpagettiHKLserie>();
        this.name=crys.getName();
        this.minE=minE;
        this.maxE=maxE;
        this.minAzim=minAzim;
        this.maxAzim=maxAzim;
        this.deltaAzim=deltaAzim;
        this.maxHKLindex=maxHKLindex;
        this.crystal=crys;
        this.calcSpagetti();
        this.discreetValuesOfGlitches=new ArrayList<Double>();
    }

    public void addSerie(SpagettiHKLserie spHKL) {
        this.series.add(spHKL);
    }

    public SpagettiHKLserie getSerie(int i) {
        return this.series.get(i);
    }
    
    public int getNseries() {
        return this.series.size();
    }
    
    public double calcQmaxFromHKLmax(int hklmaxIndex) {
        double dsp = crystal.getCell().calcDspHKL(hklmaxIndex, hklmaxIndex, hklmaxIndex);
        return 1/(dsp*dsp);
    }
    
    public void calcSpagetti() {
        List<HKLrefl> listhkl = crystal.getCell().generateHKLsCrystalFamily(calcQmaxFromHKLmax(maxHKLindex), true,true,true,true,true);
        crystal.getCell().calcInten(true,false);
        
        for (HKLrefl hkl: listhkl) {
            if (FastMath.abs(hkl.getH())>maxHKLindex)continue;
            if (FastMath.abs(hkl.getK())>maxHKLindex)continue;
            if (FastMath.abs(hkl.getL())>maxHKLindex)continue;
            if ((hkl.getH()==crystal.getHref())&&(hkl.getK()==crystal.getKref())&&(hkl.getL()==crystal.getLref())) {
                hkl_reflplane = hkl;
            }
            
            SpagettiHKLserie spHKL = new SpagettiHKLserie(hkl);
            spHKL.setToleranceContinuous(2*deltaAzim);
            double azim=minAzim;
            while (azim<maxAzim) {
                double thetaRad = crystal.calcThetaRefl(hkl.getH(), hkl.getK(), hkl.getL(), FastMath.toRadians(azim));
                double ekev = crystal.getEnergyKeV(FastMath.abs(thetaRad));
                if (Double.isFinite(ekev)) {
                    if ((ekev<=maxE)&&(ekev>=minE)){
                        double intenlor = hkl.getYcalc()*(1/(FastMath.sin(thetaRad*2)*FastMath.sin(thetaRad)));
                        spHKL.addPoint(new Spoint(azim,ekev,intenlor,spHKL));
                        
                        //test
                        double dwidth = crystal.calcDwidth(12.398/ekev,hkl,thetaRad*2);
                        ((Spoint)(spHKL.getPoint(spHKL.getNPoints()-1))).dwidth=dwidth;
                    }
                }
                azim=azim+deltaAzim;
            }
            if (spHKL.getNPoints()==0)continue;
            this.addSerie(spHKL);
        }
    }
    
    public GlitchSerie getGlitchCut(double azimValue, double fwhmKEV) {
        discreetValuesOfGlitches.clear();
        
        GlitchSerie ser = new GlitchSerie(String.format("%s glitch pattern at azim=%.2fº (delta %.3f)", crystal.getName(),azimValue,deltaAzim));
        //intensitat de base
        double baseInt = 0;
        if(this.hkl_reflplane!=null) {
            baseInt = this.hkl_reflplane.getYcalc();
        }
        
        double currE=minE;
        while (currE<maxE) {
            ser.addPoint(new Gpoint(currE,baseInt,ser));
            currE=currE+stepEglitch;
        }
        
        double minAz=azimValue-deltaAzim/2.;
        double maxAz=azimValue+deltaAzim/2.;
        int nfwhm = 30;
        //ser ha de ser per tot el rang energetic, el fem segons azimDelta
        for (SpagettiHKLserie hkl :this.series) {
            //he de mirar cada spagetti serie a les azim [-delta, azim, +delta] quina E tenen i poblar el vector ser
            for (Plottable_point sp:hkl.getPoints()) {
                double az = sp.getX();
                if (az<minAz)continue;
                if (az>minAz) {
                    if (az<maxAz) {
                        //PUNT a considerar
                        double ekev = sp.getY();
                        if (ekev>maxE)continue;
                        int pos = (int)((ekev-minE)/stepEglitch);
                        
                        //faig pseudovoigt centrada aqui
                        discreetValuesOfGlitches.add(ekev);
                        if (fwhmKEV<0) {//darwin width
                            fwhmKEV = ((Spoint)(sp)).dwidth/1000.;    
                        }//otherwise we take directly the entered value
                        Lorentzian lor = new Lorentzian (ekev,fwhmKEV); 
                        int npunts = (int) ((fwhmKEV/(double)stepEglitch) * nfwhm/2.);
                        
                        for (int j=pos-npunts; j<pos+npunts-2; j++) {
                            if (j<0)continue;
                            if (j>ser.getNPoints())continue;
                            //calculem valor lor a la posicio actual
                            double inten = sp.getZ()*lor.eval(ser.getPoint(j).getX());
                            ser.getPoint(j).addY(-inten);
                        }
                    }else {
                        break; //s'ha acabat per la serie
                    }
                }
            }
        }
        
        ArrayList<Double> cleanList = new ArrayList<Double>();
        double tol = 0.0001; //0.1eV
        for (Double d1: discreetValuesOfGlitches) {
            boolean trobat = false;
            for (Double d2:cleanList) {
                if (FastMath.abs(d1-d2)<tol) {
                    //son iguals
                    trobat=true;
                    break;
                }
            }
            if (!trobat) {
                cleanList.add(d1);
            }
        }
        Collections.sort(cleanList);
        discreetValuesOfGlitches=cleanList;
        return ser;
        
    }
    
    public class Lorentzian{
        double mean, fwhm;
        
        public Lorentzian(double mean, double fwhm) {
            this.mean=mean;
            this.fwhm=fwhm;
        }
        
        public double eval(double x) {
            return 2./FastMath.PI/(1+4*(x-mean)*(x-mean)/fwhm/fwhm);
        }
    }

    @Override
    public String toString() {
        return String.format("%s maxHKLindex=%d  ", crystal.getName(),maxHKLindex);
    }
    
    
}
