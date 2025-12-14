package controller;

import model.*;
import java.util.*;
import model.LinkedList;

public class RouteManager {

    private List<Terminal> terminals;
    private LinkedList<Waypoint> allWaypoints;
    private Map<Terminal, LinkedList<Waypoint>.Node> terminalToWaypoint;

    public RouteManager() {
        terminals = new ArrayList<>();
        allWaypoints = new LinkedList<>();
        terminalToWaypoint = new HashMap<>();
        initializeData();
    }

    private void initializeData() {
        // Terminals
        Terminal t1 = new Terminal("City of Bislig, Surigao del Sur", 1101.6, 85.6);
        Terminal t2 = new Terminal("Monkayo, Davao de Oro", 975.2, 219.2);
        Terminal t3 = new Terminal("Nabunturan, Davao de Oro", 930.4, 304.0);
        Terminal t4 = new Terminal("Tagum City, Davao del Norte", 862.4, 366.4);
        Terminal t5 = new Terminal("Panabo City, Davao del Norte", 811.1103515625, 420.70941162109375);
        Terminal t6 = new Terminal("Davao City, Davao del Norte", 778.1490478515625, 495.0567626953125);
        Terminal t7 = new Terminal("Digos City, Davao del Sur", 660.9358520507812, 660.0277709960938);
        Terminal t8 = new Terminal("Lupon, Davao Oriental", 1010.4, 506.4);
        Terminal t9 = new Terminal("Banganga, Davao Oriental", 1103.2, 321.6);
        Terminal t10 = new Terminal("Pantukan, Davao de Oro", 949.6, 468.8);

        terminals.addAll(Arrays.asList(t1,t2,t3,t4,t5,t6,t7,t8,t9,t10));

        // Waypoints (simplified example, connect all terminals in sequence)
        addWaypoint(new Waypoint(t1.getX(), t1.getY()));
        addWaypoint(new Waypoint(1093.0, 114.0));
        addWaypoint(new Waypoint(1086.0, 110.0));
        addWaypoint(new Waypoint(1068.0, 119.0));
        addWaypoint(new Waypoint(1053.0, 118.0));
        addWaypoint(new Waypoint(1053.0, 128.0));
        addWaypoint(new Waypoint(1041.0, 142.0));
        addWaypoint(new Waypoint(1030.0, 142.0));
        addWaypoint(new Waypoint(1012.0, 156.0));
        addWaypoint(new Waypoint(1008.0, 151.0));
        addWaypoint(new Waypoint(986.0, 147.0));
        addWaypoint(new Waypoint(990, 168));
        addWaypoint(new Waypoint(983.4444580078125, 187.5416717529297));
        addWaypoint(new Waypoint(985.5277709960938, 202.125));
        addWaypoint(new Waypoint(t2.getX(), t2.getY()));
        addWaypoint(new Waypoint(966.0, 220.0));
        addWaypoint(new Waypoint(966.0556030273438, 230.59722900390625));
        addWaypoint(new Waypoint(946.1065063476562, 258.0856628417969));
        addWaypoint(new Waypoint(938.004638671875, 299.1736145019531));
        addWaypoint(new Waypoint(t3.getX(), t3.getY()));
        addWaypoint(new Waypoint(910.7474975585938, 345.6531982421875));
        addWaypoint(new Waypoint(906.3268432617188, 350.4757385253906));
        addWaypoint(new Waypoint(903.513671875, 346.0550842285156));
        addWaypoint(new Waypoint(891.4573364257812, 345.2513122558594));
        addWaypoint(new Waypoint(883.8217163085938, 351.27947998046875));
        addWaypoint(new Waypoint(881.8123168945312, 357.3076477050781));
        addWaypoint(new Waypoint(t4.getX(), t4.getY()));
        addWaypoint(new Waypoint(849.1932373046875, 384.2222595214844));
        addWaypoint(new Waypoint(841.2511596679688, 393.3817443847656));
        addWaypoint(new Waypoint(823.1160888671875, 396.9986572265625));
        addWaypoint(new Waypoint(t5.getX(), t5.getY()));
        addWaypoint(new Waypoint(793.4606323242188, 445.9197692871094));
        addWaypoint(new Waypoint(797.8009033203125, 480.15972900390625));
        addWaypoint(new Waypoint(776.49951171875, 496.62933349609375));
        addWaypoint(new Waypoint(t6.getX(), t6.getY()));
        addWaypoint(new Waypoint(759.8009033203125, 499.4305725097656));
        addWaypoint(new Waypoint(761.5370483398438, 509.84722900390625));
        addWaypoint(new Waypoint(726.236083984375, 529.5231323242188));
        addWaypoint(new Waypoint(708.5321655273438, 580.7119140625));
        addWaypoint(new Waypoint(675.3080444335938, 606.1748657226562));
        addWaypoint(new Waypoint(680.480712890625, 614.9344482421875));
        addWaypoint(new Waypoint(664.5150756835938, 639.8568725585938));
        addWaypoint(new Waypoint(t7.getX(), t7.getY()));
        //addWaypoint(new Waypoint());

        // Map terminals to nearest waypoint
        mapTerminalToWaypoint(t1, allWaypoints.getHead());
        mapTerminalToWaypoint(t2, allWaypoints.getHead().next.next);
        mapTerminalToWaypoint(t3, allWaypoints.getHead().next.next.next.next);
        mapTerminalToWaypoint(t4, allWaypoints.getHead().next.next.next.next.next.next);
        mapTerminalToWaypoint(t5, allWaypoints.getHead().next.next.next.next.next.next.next.next);
        mapTerminalToWaypoint(t6, allWaypoints.getHead().next.next.next.next.next.next.next.next.next.next);
    }

    // Find the LinkedList Node that matches a terminal's coordinates
    public LinkedList<Waypoint>.Node getNodeForTerminal(Terminal t) {
        LinkedList<Waypoint>.Node node = allWaypoints.getHead();
        while (node != null) {
            if (Math.abs(node.data.getX() - t.getX()) < 0.1 &&
                    Math.abs(node.data.getY() - t.getY()) < 0.1) {
                return node;
            }
            node = node.next;
        }
        return null;
    }


    public Terminal getTerminalByName(String name) {
        for (Terminal t : terminals) {
            if (t.getName().equals(name)) return t;
        }
        return null;
    }

    public List<String> getTerminalNames() {
        List<String> names = new ArrayList<>();
        for (Terminal t : terminals) names.add(t.getName());
        return names;
    }

    public LinkedList<Waypoint>.Node getStartNode(Terminal t) {
        return terminalToWaypoint.get(t);
    }

    public LinkedList<Waypoint>.Node getEndNode(Terminal t) {
        return terminalToWaypoint.get(t);
    }

    public LinkedList<Waypoint> getAllWaypoints() {
        return allWaypoints;
    }

    public List<Terminal> getAllTerminals() {
        return terminals;
    }

    public void addWaypoint(Waypoint wp) {
        allWaypoints.add(wp);
    }

    public void mapTerminalToWaypoint(Terminal t, LinkedList<Waypoint>.Node node) {
        terminalToWaypoint.put(t, node);
    }
}
