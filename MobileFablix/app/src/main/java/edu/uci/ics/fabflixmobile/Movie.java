package edu.uci.ics.fabflixmobile;

import java.util.ArrayList;

public class Movie {
    private final String name;
    private final short year;
    private final String director;
    private final String movieid;
    private ArrayList<String> moviestars = new ArrayList<String>();
    private ArrayList<String> moviegenres = new ArrayList<String>();

    public Movie(String name, short year, String director, ArrayList<String> moviestars, ArrayList<String> moviegenres, String movieid) {
        this.name = name;
        this.year = year;
        this.director = director;
        this.moviestars = moviestars;
        this.moviegenres = moviegenres;
        this.movieid = movieid;
    }

    public String getName() {
        return name;
    }

    public short getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public ArrayList<String> getMoviestars() { return moviestars; }

    public ArrayList<String> getGenres() { return moviegenres; }

    public String getMovieid() {
        return movieid;
    }

    }
