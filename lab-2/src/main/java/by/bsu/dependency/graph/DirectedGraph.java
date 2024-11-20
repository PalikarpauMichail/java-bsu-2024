package by.bsu.dependency.graph;

import java.util.*;

public class DirectedGraph {
    List<Set<Integer>> adjacencyList;
    int verticesNumber;

    enum Colour {
        WHITE,
        GRAY,
        BLACK,
    }

    public DirectedGraph(int verticesNumber) {
        this.verticesNumber = verticesNumber;
        adjacencyList = new ArrayList<>(verticesNumber);
        for (int i = 0; i < verticesNumber; i++) {
            adjacencyList.add(i, new HashSet<>());
        }
    }

    public void addEdge(int u, int v) {
        adjacencyList.get(u).add(v);
    }

    public boolean hasCycle() {
        List<Colour> verticesColours = new ArrayList<>(verticesNumber);
        for (int i = 0; i < verticesNumber; i++) {
            verticesColours.add(Colour.WHITE);
        }
        for (int u = 0; u < verticesNumber; u++) {
            if (verticesColours.get(u) == Colour.WHITE && detectCyclesDFS(u, verticesColours)) {
                return true;
            }

        }
        return false;
    }

    private boolean detectCyclesDFS(int u, List<Colour> verticesColours) {
        verticesColours.set(u, Colour.GRAY);
        for (int v : adjacencyList.get(u)) {
            switch(verticesColours.get(v)) {
                case BLACK -> {}
                case WHITE -> {
                    if (detectCyclesDFS(v, verticesColours)) {
                        return true;
                    }
                }
                case GRAY -> {
                    return true;
                }
            }
        }
        verticesColours.set(u, Colour.BLACK);
        return false;
    }
}
