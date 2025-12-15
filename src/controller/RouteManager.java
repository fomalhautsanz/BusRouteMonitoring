package controller;

import model.*;
import java.util.*;
import model.LinkedList;

public class RouteManager {

    private final List<Terminal> terminals = new ArrayList<>();

    private final LinkedList<Waypoint> mainRoad = new LinkedList<>();

    private final Map<Terminal, LinkedList<Waypoint>.Node> terminalMap = new HashMap<>();

    public RouteManager() {
        init();
    }

    private void init() {

        // -------- TERMINALS --------
        Terminal t1 = new Terminal("City of Bislig, Surigao del Sur", 1102.6, 89.56);
        Terminal t2 = new Terminal("Monkayo, Davao de Oro", 973.60, 221.60);
        Terminal t3 = new Terminal("Nabunturan, Davao de Oro", 930.40, 308.80);
        Terminal t4 = new Terminal("Tagum City, Davao del Norte", 862.40, 366.40);
        Terminal t5 = new Terminal("Panabo City, Davao del Norte", 811.11, 420.71);
        Terminal t6 = new Terminal("Davao City, Davao del Norte", 774.60, 513.18);
        Terminal t7 = new Terminal("Digos City, Davao del Sur", 664.00, 633.03);

        terminals.addAll(List.of(t1, t2, t3, t4, t5, t6, t7));

        Waypoint wp1 = new Waypoint(t1.getX(), t1.getY());
        terminalMap.put(t1, mainRoad.add(wp1));

        mainRoad.add(new Waypoint(1104.15, 101.32));
        mainRoad.add(new Waypoint(1093, 114));
        mainRoad.add(new Waypoint(1086, 110));
        mainRoad.add(new Waypoint(1068, 119));
        mainRoad.add(new Waypoint(1053, 118));
        mainRoad.add(new Waypoint(1053, 128));
        mainRoad.add(new Waypoint(1041, 142));
        mainRoad.add(new Waypoint(1030, 142));
        mainRoad.add(new Waypoint(1012, 156));
        mainRoad.add(new Waypoint(1008, 151));
        mainRoad.add(new Waypoint(986, 147));
        mainRoad.add(new Waypoint(990, 168));
        mainRoad.add(new Waypoint(983.44, 187.54));
        mainRoad.add(new Waypoint(985.52, 202.12));

        Waypoint wp2 = new Waypoint(t2.getX(), t2.getY());
        terminalMap.put(t2, mainRoad.add(wp2));

        mainRoad.add(new Waypoint(966, 220));
        mainRoad.add(new Waypoint(966.05, 230.59));
        mainRoad.add(new Waypoint(946.10, 258.08));
        mainRoad.add(new Waypoint(938.00, 299.17));

        Waypoint wp3 = new Waypoint(t3.getX(), t3.getY());
        terminalMap.put(t3, mainRoad.add(wp3));

        mainRoad.add(new Waypoint(918.98, 318.11));
        mainRoad.add(new Waypoint(916.58, 325.63));
        mainRoad.add(new Waypoint(910.02, 334.59));
        mainRoad.add(new Waypoint(910.66, 343.55));
        mainRoad.add(new Waypoint(906.98, 348.51));
        mainRoad.add(new Waypoint(902.82, 346.11));
        mainRoad.add(new Waypoint(894.50, 345.31));

        Waypoint wp4 = new Waypoint(t4.getX(), t4.getY());
        terminalMap.put(t4, mainRoad.add(wp4));

        mainRoad.add(new Waypoint(849.19, 384.22));
        mainRoad.add(new Waypoint(841.25, 393.38));
        mainRoad.add(new Waypoint(823.11, 396.99));

        Waypoint wp5 = new Waypoint(t5.getX(), t5.getY());
        terminalMap.put(t5, mainRoad.add(wp5));

        mainRoad.add(new Waypoint(793.46, 445.91));
        mainRoad.add(new Waypoint(797.80, 480.15));
        mainRoad.add(new Waypoint(776.49, 496.62));

        Waypoint wp6 = new Waypoint(t6.getX(), t6.getY());
        terminalMap.put(t6, mainRoad.add(wp6));

        mainRoad.add(new Waypoint(759.80, 499.43));
        mainRoad.add(new Waypoint(761.53, 509.84));
        mainRoad.add(new Waypoint(726.23, 529.52));
        mainRoad.add(new Waypoint(708.53, 580.71));
        mainRoad.add(new Waypoint(675.30, 606.17));
        mainRoad.add(new Waypoint(680.48, 614.93));

        Waypoint wp7 = new Waypoint(t7.getX(), t7.getY());
        terminalMap.put(t7, mainRoad.add(wp7));
    }

    public List<Waypoint> buildRoute(Terminal start, Terminal end) {

        LinkedList<Waypoint>.Node startNode = terminalMap.get(start);
        LinkedList<Waypoint>.Node endNode = terminalMap.get(end);

        Set<LinkedList<Waypoint>.Node> visited = new HashSet<>();
        List<Waypoint> path = new ArrayList<>();

        if (dfs(startNode, endNode, visited, path)) {
            return path;
        }
        return Collections.emptyList();
    }

    private boolean dfs(
            LinkedList<Waypoint>.Node current,
            LinkedList<Waypoint>.Node target,
            Set<LinkedList<Waypoint>.Node> visited,
            List<Waypoint> path) {

        if (current == null || visited.contains(current)) return false;

        visited.add(current);
        path.add(current.data);

        if (current == target) return true;

        boolean blockMainRoad =
                current == terminalMap.get(getTerminalByName("City of Bislig, Surigao del Sur"))
                && isEastRoadDestination(target);

        if (!blockMainRoad) {
            if (current.next != null && dfs(current.next, target, visited, path)) return true;
        }

        if (current.prev != null && dfs(current.prev, target, visited, path)) return true;

        for (LinkedList<Waypoint>.Node branch : current.data.getConnections()) {
            if (dfs(branch, target, visited, path)) return true;
        }

        path.remove(path.size() - 1);
        return false;
    }

    public List<Terminal> getAllTerminals() {
        return terminals;
    }

    public List<String> getTerminalNames() {
        List<String> names = new ArrayList<>();
        for (Terminal t : terminals) names.add(t.getName());
        return names;
    }

    public Terminal getTerminalByName(String name) {
        for (Terminal t : terminals) {
            if (t.getName().equals(name)) return t;
        }
        return null;
    }

    private boolean isEastRoadDestination(LinkedList<Waypoint>.Node target) {
        return target == terminalMap.get(getTerminalByName("Lupon, Davao Oriental"))
            || target == terminalMap.get(getTerminalByName("Pantukan, Davao de Oro"))
            || target == terminalMap.get(getTerminalByName("Banganga, Davao Oriental"));
    }

}
