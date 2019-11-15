package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import java.util.ArrayList;

import org.apache.commons.math3.util.FastMath;
import com.vava33.jutils.VavaLogger;
import com.vava33.ovPlot.Plottable_point;

public class Spagetti {

    ArrayList<SpagettiHKLserie> series;
    String name;
    double minE,maxE,minAzim,maxAzim,deltaAzim;
    int maxHKLindex;
    CrystalCut crystal;
    static double stepEglitch = 0.001; //1eV
    VavaLogger log = new VavaLogger("spagetti");
    
    public Spagetti(String name, double minE, double maxE, double minAzim, double maxAzim, double deltaAzim, int maxHKLindex, CrystalCut crys) {
        series = new ArrayList<SpagettiHKLserie>();
        this.name=name;
        this.minE=minE;
        this.maxE=maxE;
        this.minAzim=minAzim;
        this.maxAzim=maxAzim;
        this.deltaAzim=deltaAzim;
        this.maxHKLindex=maxHKLindex;
        this.crystal=crys;
        log.enableLogger();
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
    
    public GlitchSerie getGlitchCut(double azimValue, double fwhmKEV) {
        
        GlitchSerie ser = new GlitchSerie(String.format("%s glitch pattern at azim=%.2fº (delta %.3f)", crystal.getName(),azimValue,deltaAzim));
        //intensitat de base
        double baseInt = 0;
        if(crystal.getHKLreflPlane()!=null) {
            baseInt = crystal.getHKLreflPlane().getYcalc();
        }
        
        double currE=minE;
        while (currE<maxE) {
            ser.addPoint(new Gpoint(currE,baseInt,ser));
            currE=currE+stepEglitch;
        }
        
        double minAz=azimValue-deltaAzim/2.;
        double maxAz=azimValue+deltaAzim/2.;
        int nfwhm = 30;
        log.info("size="+ser.getNPoints());
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
                        fwhmKEV = ((Spoint)(sp)).dwidth;
                        log.info(sp.getInfo()+" E(keV)="+ekev+" dwidth(fwhm keV)="+fwhmKEV);
                        Lorentzian lor = new Lorentzian (ekev,fwhmKEV); 
                        int npunts = (int) ((fwhmKEV/(double)stepEglitch) * nfwhm/2.);
                        
                        log.infof("az=%.2f ekev=%.2f pos=%d npunts=%d",az,ekev,pos,npunts);
                        
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
}
