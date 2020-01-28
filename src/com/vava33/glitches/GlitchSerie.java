package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import java.util.ArrayList;

import com.vava33.BasicPlotPanel.BasicPoint;
import com.vava33.BasicPlotPanel.BasicSerie;

public class GlitchSerie extends BasicSerie<BasicPoint>{
    
    ArrayList<Double> discreetValuesOfGlitches;
    
    public GlitchSerie(String name) {
        super(name);
        discreetValuesOfGlitches =new ArrayList<Double>();
    }
}
