package org.example.analize.deicstra;

import lombok.*;

import java.util.*;

import static org.example.analize.deicstra.Dijkstra.PathCost.*;


public class Dijkstra {

    private final Map<String, Vertex> graph = new HashMap<>();

    @Setter
    @Getter
    static class Vertex {
        private List<String> names;
        boolean visited = false;
        String namePrevious = "";
        int cost = INFINITY;

        Vertex(List<String> names) {
            this.names = names;
        }

        public void changeCost(String current, int costCurrent) {
            if (!visited && (cost > (costCurrent + PATH_COST))) {
                cost = costCurrent + PATH_COST;
                namePrevious = current;
            }
        }

        public void setDefault() {
            this.visited = false;
            this.namePrevious = "";
            this.cost = INFINITY;
        }
    }

    record PathCost() {
        static final int PATH_COST = 1;
        static final int NO_COST = 0;
        static final int FIRST_ELEMENT = 0;
        static final int INFINITY = Integer.MAX_VALUE - 1;
    }

    public Dijkstra(Map<String, List<String>> graphInfo) {
        graphInfo.forEach((k, v) -> graph.put(k, new Vertex(v)));
    }

    void path(String current) {
        Vertex vertexCurrent = graph.get(current);
        vertexCurrent.setVisited(true);
        vertexCurrent.getNames().forEach(v -> graph.get(v).changeCost(current, vertexCurrent.getCost()));
        String next = minFindMin();
        if (next.isEmpty()) {
            return;
        }
        path(next);
    }

    String minFindMin() {
        int min = INFINITY;
        String next = "";
        Set<String> keys = graph.keySet();
        for (var name : keys) {
            Vertex vertexCurrent = graph.get(name);
            if (!vertexCurrent.isVisited() && vertexCurrent.getCost() < min) {
                min = vertexCurrent.getCost();
                next = name;
            }
        }
        return next;
    }


    public List<String> findPath(String from, String to) {
        if (!graph.containsKey(from) || !graph.containsKey(to)) {
            return List.of(from, to);
        }
        graph.get(from).setCost(NO_COST);
        path(from);
        if (graph.get(to).cost == INFINITY) {
            //clean();
            return List.of(from, to);
        }
        List<String> list = new ArrayList<>();
        list.add(FIRST_ELEMENT, to);
        while (!list.get(FIRST_ELEMENT).equals(from)) {
            list.add(FIRST_ELEMENT, graph.get(list.get(FIRST_ELEMENT)).getNamePrevious());
        }
        // clean();
        return list;
    }

    @Deprecated(since = "Паралелить если endpoints")
    void clean() {
        graph.forEach((k, v) -> v.setDefault());
    }

}
