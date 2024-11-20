package model;

import java.util.ArrayList;

public class Movie extends Production{
    private int length;
    private int releaseYear;

    public void setLength(int length) {
        this.length = length;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getLength() {
        return length;
    }

    public Movie(String name) {
        super(name);
        length = -1;
        releaseYear = -1;
        this.setContributor("ADMIN");
    }

    public Movie(String name, int length, int releaseYear) {
        super(name);
        this.releaseYear = releaseYear;
        this.length = length;
        this.setContributor("ADMIN");
    }

    public Movie(String name, ArrayList<String> directors, ArrayList<String> actors, ArrayList<Genre> genres,
                 ArrayList<Rating> ratings, String description, double IMDBRating, int length, int releaseYear) {
        super(name, directors, actors, genres, ratings, description, IMDBRating);
        this.length = length;
        this.releaseYear = releaseYear;
        this.setContributor("ADMIN");
    }

    @Override
    public void displayInfo() {
        if (length != -1)
            System.out.println("Duration: " + length);

        if (releaseYear != -1)
            System.out.println("Release year: " + releaseYear);
    }

    @Override
    public String toString() {
        return super.toString() + "\nLength: " + this.length + "\nReleaseYear: " + this.releaseYear;
    }
}
