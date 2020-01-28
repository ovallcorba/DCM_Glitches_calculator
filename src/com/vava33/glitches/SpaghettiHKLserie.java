package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import com.vava33.BasicPlotPanel.BasicPoint;
import com.vava33.BasicPlotPanel.BasicSerie;
import com.vava33.cellsymm.HKLrefl;

public class SpaghettiHKLserie extends BasicSerie<BasicPoint>{
    
    private HKLrefl hkl;

    public SpaghettiHKLserie(HKLrefl hkl) {
        super(hkl.toString());
        this.hkl = hkl;
    }

    public HKLrefl getHKL() {
        return hkl;
    }

    public boolean isEqualTo(SpaghettiHKLserie otherSpaghettiSerie) {
        boolean iguals = true;
        for (int i=0; i<this.getNPoints(); i++) {
            if (this.getRawPoint(i).compareTo(otherSpaghettiSerie.getRawPoint(i))!=0) {
                iguals=false;
                break;
            }
        }
        return iguals;
    }
}
