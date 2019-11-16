package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import com.vava33.ovPlot.BasicPoint;

public class Spoint extends BasicPoint{

    public double dwidth=0.005; //TEMP
    
    public Spoint(double azim, double energyKeV, double intensity, SpagettiHKLserie parent) {
        super(azim,energyKeV,intensity,0,parent);
    }

    @Override
    public String getInfo() {
        return getPlottable().getName(); //ens dira l'hkl
    }
}
