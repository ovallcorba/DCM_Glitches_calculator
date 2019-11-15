package com.vava33.glitches;

/**
 * 
 * @author Oriol Vallcorba
 * Licence: GPLv3
 * 
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.vava33.ovPlot.Plottable;
import com.vava33.ovPlot.Plottable_point;
import com.vava33.ovPlot.SerieType;

public class EdgeSerie implements Plottable{

    private List<Plottable_point> points;
    private String name;
    private boolean plotThis;
    private float lineWidth;
    private float markerSize;
    private Color col;
    
    
    public EdgeSerie(String name) {
        points = new ArrayList<Plottable_point>();
        this.name = name;
        this.col=SerieType.getDefColor(SerieType.vertical);
        this.lineWidth=SerieType.getDefLineWidth(SerieType.vertical);
        this.markerSize=SerieType.getDefMarkerSize(SerieType.vertical);
        this.plotThis=true;
    }

    public void addPoint(Plottable_point pp) {
        this.points.add(pp);
    }
    
    @Override
    public List<Plottable_point> getPoints() {
        return points;
    }

    @Override
    public Plottable_point getPoint(int index) {
        return points.get(index);
    }

    @Override
    public Plottable_point getScaledPoint(int index, float scaleFactor) {
        return points.get(index);
    }

    @Override
    public int getNPoints() {
        return points.size();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public boolean isPlotThis() {
        return this.plotThis;
    }

    @Override
    public void setPlotThis(boolean plotit) {
        this.plotThis=plotit;
        
    }

    @Override
    public boolean isEmpty() {
        if (this.getNPoints()<=0)return true;
        return false;
    }

    @Override
    public Color getColor() {
        return col;
    }

    @Override
    public void setColor(Color c) {
        this.col=c;
        
    }

    @Override
    public double[] getPuntsMaxXMinXMaxYMinY() {
        if (points!=null){
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;
            for (int i=0;i<points.size();i++){
                Plottable_point punt = this.getPoint(i);
                if (punt.getX() < minX){
                    minX = punt.getX();
                }
                if (punt.getX() > maxX){
                    maxX = punt.getX();
                }
                if (punt.getY() < minY){
                    minY = punt.getY();
                }
                if (punt.getY() > maxY){
                    maxY = punt.getY();
                }
            }
            return new double[]{maxX,minX,maxY,minY};
        }else{
            return null;
        }
    }

    @Override
    public float getLineWidth() {
        return this.lineWidth;
    }

    @Override
    public float getMarkerSize() {
        return this.markerSize;
    }

    @Override
    public void setLineWidth(float value) {
        this.lineWidth=value;
        
    }

    @Override
    public void setMarkerSize(float value) {
        this.markerSize=value;
        
    }

    @Override
    public SerieType getSerieType() {
        return SerieType.vertical;
    }

}
