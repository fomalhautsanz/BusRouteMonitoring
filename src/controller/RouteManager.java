package controller;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class RouteManager {

    private List<Terminal> terminals;
    private List<Route> routes;

    public RouteManager() {
        terminals = new ArrayList<>();
        routes = new ArrayList<>();
        initializeData();
    }

    /*
        Boss Gemina, ang program magadd autoamticlaly 
        sa mga makrers sa route ndoepnde sa start and 
        end terminal, pagadd cguro kay mahide to sila
    */
    private void initializeData() {
        // By X and Y pixel coordinates ni
        // Naa sad on click sa map para sa x and y apra dali ra makitau nsa coords
        Terminal t1  = new Terminal("City of Bislig, Surigao del Sur",           1101.6,  85.6);
        Terminal t2  = new Terminal("Malaybalay, Bukidnon",                      618.4,   61.6);
        Terminal t3  = new Terminal("Marawi, Lanao del Sur",                     290.4,   88.8);
        Terminal t4  = new Terminal("Valencia, Bukidnon",                        597.6,  159.2);
        Terminal t5  = new Terminal("Maramag, Bukidnon",                         546.4,  180.8);
        Terminal t6  = new Terminal("Monkayo, Davao de Oro",                     975.2,  219.2);
        Terminal t7  = new Terminal("Banganga, Davao Oriental",                 1103.2,  321.6);
        Terminal t8  = new Terminal("Quezon, Bukidnon",                          621.6,  232.0);
        Terminal t9  = new Terminal("Nabunturan, Davao de Oro",                  930.4,  304.0);

        Terminal t10 = new Terminal("Tagum City, Davao del Norte",               862.4,  366.4);
        Terminal t11 = new Terminal("Panabo City, Davao del Norte",              810.4,  420.0);
        Terminal t12 = new Terminal("Pantukan, Davao de Oro",                    949.6,  468.8);

        Terminal t13 = new Terminal("Lupon, Davao Oriental",                    1010.4,  506.4);
        Terminal t14 = new Terminal("City of Mati, Davao Oriental",              984.8,  569.6);

        Terminal t15 = new Terminal("Cotabato City, Maguindanao del Norte",      237.6,  415.2);
        Terminal t16 = new Terminal("Pikit, Cotabato",                            398.4,  469.6);
        Terminal t17 = new Terminal("Kabacan, Cotabato",                          472.8,  470.4);
        Terminal t18 = new Terminal("Kidapawan City, Cotabato",                   570.4,  512.0);
        Terminal t19 = new Terminal("Makilala, Cotabato",                         564.0,  551.2);

        Terminal t20 = new Terminal("Davao City, Davao del Sur",                  778.4,  509.6);
        Terminal t21 = new Terminal("Digos City, Davao del Sur",                  664.8,  630.4);

        Terminal t22 = new Terminal("Tacurong City, Sultan Kudarat",              399.2,  631.2);
        Terminal t23 = new Terminal("Isulan, Sultan Kudarat",                     364.0,  649.6);
        Terminal t24 = new Terminal("Lebak, Sultan Kudarat",                      158.4,  630.4);

        terminals.addAll(List.of(
            t1, t2, t3, t4, t5, t6, t7, t8, t9,
            t10, t11, t12, t13, t14,
            t15, t16, t17, t18, t19,
            t20, t21, t22, t23, t24
        ));

        // IEDIT NI BOSS  =====
        routes.add(new Route(t10, t20)); // Tagum → Davao
        routes.add(new Route(t11, t20)); // Panabo → Davao
        routes.add(new Route(t20, t21)); // Davao → Digos
        routes.add(new Route(t18, t20)); // Kidapawan → Davao
        routes.add(new Route(t14, t20)); // Mati → Davao
    }


    public Terminal getTerminalByName(String name) {
        for (Terminal t : terminals) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    public List<String> getTerminalNames() {
        List<String> names = new ArrayList<>();
        for (Terminal t : terminals) {
            names.add(t.getName());
        }
        return names;
    }

    public Route findRoute(Terminal start, Terminal end) {
        for (Route r : routes) {
            if (r.getStart().equals(start) && r.getEnd().equals(end)) {
                return r; 
            }
        }
        return null;
    }

    public List<Route> getAllRoutes() {
        return routes;
    }

    public List<Terminal> getAllTerminals() {
        return terminals;
    }

}
