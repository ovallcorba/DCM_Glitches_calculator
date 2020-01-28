package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import com.vava33.BasicPlotPanel.BasicPoint;
import com.vava33.BasicPlotPanel.BasicSerie;
import com.vava33.BasicPlotPanel.core.SerieType;

public class EdgeSerie extends BasicSerie<BasicPoint>{    
    
    public EdgeSerie(String name) {
        super(name,SerieType.ref);
    }

}
