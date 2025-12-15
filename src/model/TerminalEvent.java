package model;

import java.util.List;

public class TerminalEvent {

    public enum Type {
        NORMAL,
        DISABLED
    }

    private final String title;
    private final String message;
    private final String iconPath;
    private final Type type;

    public TerminalEvent(String title, String message, String iconPath, Type type) {
        this.title = title;
        this.message = message;
        this.iconPath = iconPath;
        this.type = type;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getIconPath() { return iconPath; }
    public Type getType() { return type; }

    // List of all events
    private static final List<TerminalEvent> EVENTS = List.of(
        new TerminalEvent(
            "Normal Operation",
            "Terminal is operating normally.",
            "assets/img/ok.png",
            Type.NORMAL
        ),
        new TerminalEvent(
            "Terminal Under Attack",
            "Security threat detected. Terminal is temporarily disabled.",
            "assets/img/terrorist.png",
            Type.DISABLED
        ),
        new TerminalEvent(
            "Budget Reduced",
            "Budget cuts force terminal shutdown.",
            "assets/img/budget_cut.png",
            Type.DISABLED
        )
    );

    private static int disabledIndex = 0;
    private static int normalIndex = 0;

    public static TerminalEvent normalEvent() {
        List<TerminalEvent> normalEvents = EVENTS.stream()
                .filter(e -> e.type == Type.NORMAL)
                .toList();
        if (normalEvents.isEmpty()) return EVENTS.get(0);

        TerminalEvent event = normalEvents.get(normalIndex);
        normalIndex = (normalIndex + 1) % normalEvents.size();
        return event;
    }

    public static TerminalEvent disabledEvent() {
        List<TerminalEvent> disabledEvents = EVENTS.stream()
                .filter(e -> e.type == Type.DISABLED)
                .toList();
        if (disabledEvents.isEmpty()) return EVENTS.get(0);

        TerminalEvent event = disabledEvents.get(disabledIndex);
        disabledIndex = (disabledIndex + 1) % disabledEvents.size();
        return event;
    }

}
