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
    double minE,maxE,minAzim,maxAzim,stepSizeAzim;
    int maxHKLindex;
    CrystalCut crystal;
    HKLrefl hkl_reflplane;
    static double stepEglitch = 0.001; //1eV
    static double minFWHM=0.005;//5eV
    VavaLogger log = GlitchesMain.getVavaLogger("Spagetti");
    //test:
    
    public Spagetti(CrystalCut crys, double minE, double maxE, double minAzim, double maxAzim, double stepAzim, int maxHKLindex) {
        series = new ArrayList<SpagettiHKLserie>();
        this.name=crys.getName();
        this.minE=minE;
        this.maxE=maxE;
        this.minAzim=minAzim;
        this.maxAzim=maxAzim;
        this.stepSizeAzim=stepAzim;
        this.maxHKLindex=maxHKLindex;
        this.crystal=crys;
        this.calcSpagetti();
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
            spHKL.setToleranceContinuous(2*stepSizeAzim);
            double azim=minAzim;
            while (azim<maxAzim) {
                double thetaRad = crystal.calcThetaRefl(hkl.getH(), hkl.getK(), hkl.getL(), FastMath.toRadians(azim));
                double ekev = crystal.getEnergyKeV(FastMath.abs(thetaRad));
                if (Double.isFinite(ekev)) {
                    if ((ekev<=maxE)&&(ekev>=minE)){
                        double intenlor = hkl.getYcalc()*(1/(FastMath.sin(thetaRad*2)*FastMath.sin(thetaRad)));
                        spHKL.addPoint(new Spoint(azim,ekev,intenlor,spHKL));
                    }
                }
                azim=azim+stepSizeAzim;
            }
            if (spHKL.getNPoints()==0)continue;
            this.addSerie(spHKL);
        }
        
        //mirem quines series coincideixen i posar un offset al nom... //TODO millorable
        for (int i=0;i<this.getNseries();i++) {
            SpagettiHKLserie s = this.getSerie(i);
            for (int j=i+1;j<this.getNseries();j++) {
                SpagettiHKLserie s2 = this.getSerie(j);                
                if (s.isEqualTo(s2)) {
                    s2.setName("            "+s2.getName());
                }
            }
        }

    }
    
 
    /*
     * A cada linia que passa per la E que estem mirant hem de considerar un FWHM(keV) 
     * tal que agafi com a Emin = E(azim-fwhmDAzim/2) i Emax= E(azim+fwhmDAzim/2)
     */
    
    public GlitchSerie getGlitchCut2(double azimValue, double fwhmDAzim) {
        ArrayList<Double> discreetValuesOfGlitches = new ArrayList<Double>();
        
        GlitchSerie ser = new GlitchSerie(String.format("%s glitch pattern at azim=%.2fº (delta %.3f)", crystal.getName(),azimValue,fwhmDAzim));
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
        
        //Aquest criteri es bo per tenir un unic punt azimutal (agafa half the stepsize de l'spagetti, per tant una seria hkl nomes estarà un cop dins min-max)
        double minAz=azimValue-stepSizeAzim/2.;
        double maxAz=azimValue+stepSizeAzim/2.;

        int nfwhm = 30;
        double fwhmKEV=minFWHM; //default TODO posar de parametre?
        
        //ser ha de ser per tot el rang energetic, el fem segons azimDelta
        for (SpagettiHKLserie hkl :this.series) {
            //he de mirar cada spagetti serie a les azim [-delta, azim, +delta] quina E tenen i poblar el vector ser
            for (Plottable_point sp:hkl.getPoints()) {
                double az = sp.getX();
                if (az<minAz)continue;
                if (az>minAz) {
                    if (az<maxAz) {
                        //PUNT a considerar
                        double ekev = sp.getY(); //--> energia central del glitch
                        if (ekev>maxE)continue;
                        int pos = (int)((ekev-minE)/stepEglitch); //--> posicio al vector ser (glitch pattern)
                        
                        //ara cal mirar la fwhm en E segons el "pendent" de cada glitch
                        if ((fwhmDAzim/2.)>(stepSizeAzim)) { //condicio perque hi hagi amplada diferent, sino agafo directament una fwhm en eV entrada fixa per totes
                            int index = hkl.getPoints().indexOf(sp);
                            //deltaAzim es el que incrementa cada index, i volem saber quants indexs equivalen a fwhmDAzim/2
                            int deltaIndex = (int) FastMath.round((fwhmDAzim/2.)/stepSizeAzim);
                            double eminfwhm=sp.getY();
                            if (index-deltaIndex>0) {
                                eminfwhm = hkl.getPoints().get(index-deltaIndex).getY();
                            }else {
                                 eminfwhm = hkl.getPoints().get(0).getY();
                            }
                            double emaxfwhm=sp.getY();
                            if (index+deltaIndex<hkl.getNPoints()-1) {
                                emaxfwhm = hkl.getPoints().get(index+deltaIndex).getY();
                            }else {
                                emaxfwhm = hkl.getPoints().get(hkl.getNPoints()-1).getY();
                            }
                            fwhmKEV=FastMath.abs(emaxfwhm-eminfwhm);
                            if (fwhmKEV<minFWHM)fwhmKEV=minFWHM;
                            
                        }else {
                            fwhmKEV=minFWHM;
                        }
                        
                       
                        //faig pseudovoigt centrada aqui
                        discreetValuesOfGlitches.add(ekev);
                        Lorentzian lor = new Lorentzian (ekev,fwhmKEV); 
                        int npunts = (int) ((fwhmKEV/(double)stepEglitch) * nfwhm/2.);
                        
                        for (int j=pos-npunts; j<pos+npunts-2; j++) {
                            if (j<0)continue;
                            if (j>=ser.getNPoints())continue;
                            //calculem valor lor a la posicio actual
                            double inten=0;
                            try {
                                inten = sp.getZ()*lor.eval(ser.getRawPoint(j).getX());    
                            }catch(Exception ex) {
                                ex.printStackTrace();
                            }
                            
                            ser.getRawPoint(j).addY(-inten);
                            if (j==pos)ser.getRawPoint(j).setLabel(hkl.getName());
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
        ser.discreetValuesOfGlitches=discreetValuesOfGlitches;
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
