package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import com.vava33.ovPlot.Plottable_point;

public class Gpoint implements Plottable_point{
    double x_E;
    double y_inten;
    GlitchSerie parent;
    
    public Gpoint(double energyKeV, double intensity, GlitchSerie parent) {
        this.x_E=energyKeV;
        this.y_inten=intensity;
        this.parent=parent;
    }

    public Gpoint(Gpoint gp, float scalef) {
        this.x_E= gp.x_E;
        this.y_inten=gp.y_inten*scalef;
        this.parent=gp.parent;
    }
    
    @Override
    public int compareTo(Plottable_point o) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getX() {
        return x_E;
    }

    @Override
    public double getY() {
        return y_inten;
    }

    @Override
    public double getZ() {
        return 0;
    }

    @Override
    public double getSdy() {
        return 0;
    }

    @Override
    public void setX(double x) {
        this.x_E=x;
    }

    @Override
    public void setY(double y) {
        this.y_inten=y;
        
    }

    @Override
    public void setZ(double z) {
        return;
    }

    @Override
    public void addY(double y) {
        this.y_inten=this.y_inten+y;
    }

    @Override
    public void setSdy(double sdy) {
        return;
    }

    @Override
    public String getInfo() {
        return "glitch point";
    }
}
