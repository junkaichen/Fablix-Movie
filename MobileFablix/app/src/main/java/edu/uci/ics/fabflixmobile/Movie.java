package edu.uci.ics.fabflixmobile;

public class Movie {
    private final String name;
    private final short year;
    private final String director;

    public Movie(String name, short year,String director) {
        this.name = name;
        this.year = year;
        this.director = director;
    }

    public String getName() {
        return name;
    }

    public short getYear() {
        return year;
    }
}