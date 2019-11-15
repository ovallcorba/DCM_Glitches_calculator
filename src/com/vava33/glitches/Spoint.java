package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import com.vava33.ovPlot.Plottable_point;

public class Spoint implements Plottable_point{
    double azim;
    double energyKeV;
    double intensity;
    SpagettiHKLserie parent;
    public double dwidth=0.005; //TEMP
    
    public Spoint(double azim, double energyKeV, double intensity, SpagettiHKLserie parent) {
        this.azim=azim;
        this.energyKeV=energyKeV;
        this.intensity=intensity;
        this.parent=parent;
    }
    public Spoint(Spoint sp, float scalef) {
        this.azim=sp.azim;
        this.energyKeV=sp.energyKeV;
        this.intensity=sp.intensity*scalef;
        this.parent=sp.parent;
    }

    @Override
    public int compareTo(Plottable_point o) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getX() {
        return azim;
    }

    @Override
    public double getY() {
        return energyKeV;
    }

    @Override
    public double getZ() {
        return intensity;
    }

    @Override
    public double getSdy() {
        return 0;
    }

    @Override
    public void setX(double x) {
        this.azim=x;
    }

    @Override
    public void setY(double y) {
        this.energyKeV=y;
        
    }

    @Override
    public void setZ(double z) {
        this.intensity=z;
    }

    @Override
    public void addY(double y) {
        this.energyKeV=this.energyKeV+y;
    }

    @Override
    public void setSdy(double sdy) {
        //nothing
    }

    @Override
    public String getInfo() {
        return parent.getHKL().toString();
    }
}
