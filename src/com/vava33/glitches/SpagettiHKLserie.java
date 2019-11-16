package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import com.vava33.cellsymm.HKLrefl;
import com.vava33.ovPlot.BasicSerie;

public class SpagettiHKLserie extends BasicSerie{
    
    private HKLrefl hkl;

    public SpagettiHKLserie(HKLrefl hkl) {
        super(hkl.toString());
        this.hkl = hkl;
    }

    public HKLrefl getHKL() {
        return hkl;
    }


}
