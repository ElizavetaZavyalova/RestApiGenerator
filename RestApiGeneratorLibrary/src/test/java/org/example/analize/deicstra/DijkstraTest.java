package org.example.analize.deicstra;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class DijkstraTest {
    static Map<String, List<String>> map = Map.of(
            "0", List.of("1","5"),
            "1", List.of("0","4","2"),
            "2", List.of("1"),
            "3", List.of("5","1","4"),
            "4", List.of("1","3"),
            "5", List.of("0","3"),
            "6", List.of("7"),
            "7", List.of("6"));

    @Test
    void pathTest() {
        Dijkstra dijkstra=new Dijkstra(map);
        log.info(dijkstra.findPath("2","5").toString());
        //log.info(dijkstra.findPath("3","5").toString());
    }
    @Test
    void NoPathTest() {
        Dijkstra dijkstra=new Dijkstra(map);
        log.info(dijkstra.findPath("2","7").toString());
        log.info(dijkstra.findPath("1","7").toString());
    }
    @Test
    void NoVarTest() {
        Dijkstra dijkstra=new Dijkstra(map);
        log.info(dijkstra.findPath("11","7").toString());
        log.info(dijkstra.findPath("7","11").toString());
        log.info(dijkstra.findPath("26","18").toString());
    }
}