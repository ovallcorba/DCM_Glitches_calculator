package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import com.vava33.cellsymm.HKLrefl;

public interface CrystalCut {

    public Spagetti calcSpagetti(int maxIndex, double azimIniDeg, double azimFinDeg, double azimStep, double eKevMin, double eKevMax);
    public String getName();
    public HKLrefl getHKLreflPlane();
    
    
}
