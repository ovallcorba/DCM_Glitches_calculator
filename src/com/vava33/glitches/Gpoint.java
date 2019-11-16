package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import com.vava33.ovPlot.BasicPoint;

public class Gpoint extends BasicPoint {
    
    public Gpoint(double energyKeV, double intensity, GlitchSerie parent) {
        super(energyKeV,intensity,parent);
    }
    
    @Override
    public String getInfo() {
        return "glitch point";
    }

}
