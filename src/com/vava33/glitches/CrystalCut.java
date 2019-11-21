package com.vava33.glitches;
/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import com.vava33.cellsymm.Cell;

public interface CrystalCut {

    public String getName();
    public double calcThetaRefl(int h, int k, int l, double azimangleRad);
    public double getEnergyKeV(double thetaRad);
    public Cell getCell();
    public int getHref();
    public int getKref();
    public int getLref();
    
}
