package com.l4a1n.bloodspire;

public class PathNode {
    public double x, y;
    public double gCost, hCost, fCost;
    public  PathNode parent;

    public PathNode(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void calculateCost(PathNode endNode){
        this.hCost = Math.abs(x - endNode.x) + Math.abs(y - endNode.y);
        this.fCost = gCost + hCost;
    }
}
