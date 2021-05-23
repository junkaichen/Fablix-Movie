package edu.uci.ics.fabflixmobile;

public class Actor {
    private final String name;
    private final short year;

    public Actor(String name, short year) {
        this.name = name;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public short getYear() {
        return year;
    }
}
