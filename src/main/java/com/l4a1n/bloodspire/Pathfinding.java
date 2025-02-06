package com.l4a1n.bloodspire;

import java.util.*;

public class Pathfinding {
    private static final double STEP_SIZE = 20;
    public static List<PathNode> findPath(double startX, double startY, double endX, double endY, List<Wall> walls){
        List<PathNode> openSet = new ArrayList<>();
        List<PathNode> closedSet = new ArrayList<>();

        PathNode startNode = new PathNode(startX, startY);
        PathNode endNode = new PathNode(endX, endY);
        openSet.add(startNode);

        while(!openSet.isEmpty()){
            PathNode currentNode = getLowestFCostNode(openSet);

            if (distance(currentNode, endNode) < STEP_SIZE){
                return constructPath(currentNode);
            }

            openSet.remove(currentNode);
            closedSet.add(currentNode);

            for (PathNode neighbor : getNeighbors(currentNode, walls)){
                if (closedSet.contains(neighbor)) continue;

                double newGCost = currentNode.gCost + STEP_SIZE;
                if (newGCost < neighbor.gCost || !openSet.contains(neighbor)){
                    neighbor.gCost = newGCost;
                    neighbor.calculateCost(endNode);
                    neighbor.parent = currentNode;

                    if (!openSet.contains(neighbor)){
                        openSet.add(neighbor);
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    private static PathNode getLowestFCostNode(List<PathNode> list){
        return list.stream().min(Comparator.comparingDouble(n -> n.fCost)).orElse(null);
    }

    public static List<PathNode> getNeighbors(PathNode node, List<Wall> walls){
        List<PathNode> neighbors = new ArrayList<>();
        double[][] directions = { {STEP_SIZE, 0}, {-STEP_SIZE, 0}, {0, STEP_SIZE}, {0, -STEP_SIZE}};

        for (double[] dir : directions){
            double newX = node.x + dir[0];
            double newY = node.y + dir[1];

            if (!collidesWithWalls(newX, newY, walls)){
                neighbors.add(new PathNode(newX, newY));
            }
        }
        return neighbors;
    }

    private static boolean collidesWithWalls(double x, double y, List<Wall> walls){
        for (Wall wall : walls){
            if (wall.collidesWith(x, y, 20)){
                return true;
            }
        }
        return false;
    }
    private static double distance(PathNode a, PathNode b){
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private static List<PathNode> constructPath(PathNode node){
        List<PathNode> path = new ArrayList<>();
        while (node != null){
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
