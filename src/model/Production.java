package model;

import java.util.ArrayList;
import java.util.Arrays;

abstract public class Production extends ArtisticEntity {
    private final String name;
    private final ArrayList<String> directors;
    private final ArrayList<String> actors;
    private final ArrayList<Genre> genres;
    private final ArrayList<Rating> ratings;
    private String description;
    private double IMDBRating;

    public double getIMDBRating() {
        return IMDBRating;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public ArrayList<String> getDirectors() {
        return directors;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addDirector(String director) {
        directors.add(director);
    }

    public void addActor(String actor) {
        actors.add(actor);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
        IMDBRating = calculateIMDBRating();
    }

    public void removeRating(Rating rating) {
        ratings.remove(rating);
        IMDBRating = calculateIMDBRating();
    }

    public double calculateIMDBRating() {
        int sum = 0;
        for (Rating rating : ratings)
            sum += rating.getScore();
        return (double) sum / ratings.size();
    }

    public Rating findRating(String username) {
        for (Rating rating : ratings)
            if (rating.getUsername().equals(username))
                return rating;
        return null;
    }

    public void removeActor(String actor) {
        actors.remove(actor);
    }

    public Production(String name) {
        this.name = name;
        directors = new ArrayList<>();
        actors = new ArrayList<>();
        genres = new ArrayList<>();
        ratings = new ArrayList<>();
        description = null;
        IMDBRating = -1;
    }

    public Production(String name, ArrayList<String> directors, ArrayList<String> actors, ArrayList<Genre> genres,
                      ArrayList<Rating> ratings, String description, double IMDBRating) {
        this.name = name;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.IMDBRating = IMDBRating;
    }

    public abstract void displayInfo();

    @Override
    public int calculateRelevance(String searchQuery) {
        int relevanceScore = 0;
        String[] words = searchQuery.toLowerCase().split("\\s+");

        if (this.name.equalsIgnoreCase(searchQuery)) {
            relevanceScore += 6;
        }

        if (this.name.toLowerCase().contains(searchQuery.toLowerCase()) ||
                searchQuery.toLowerCase().contains(this.getName().toLowerCase())) {
            relevanceScore += 3;
        }

        if (Arrays.stream(words).allMatch(word -> this.name.toLowerCase().contains(word))) {
            relevanceScore += 5;
        }

        if (this.description.toLowerCase().contains(searchQuery.toLowerCase())) {
            relevanceScore += 3;
        }

        if (Arrays.stream(words).allMatch(word -> this.description.toLowerCase().contains(word))) {
            relevanceScore += 2;
        }

        if (this.genres.stream().anyMatch(genre -> searchQuery.toLowerCase().contains(genre.name().toLowerCase()))) {
            relevanceScore += 1;
        }

        if (this.directors.stream().anyMatch(director -> searchQuery.toLowerCase().contains(director.toLowerCase())) ||
                this.actors.stream().anyMatch(actor -> searchQuery.toLowerCase().contains(actor.toLowerCase()))) {
            relevanceScore += 1;
        }

        return relevanceScore;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ArtisticEntity object) {
        return this.name.compareTo(object.getName());
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nDirectors: " + this.directors + "\nActors: " + this.actors + "\nGenres: "
                + this.genres + "\nRatings: " + this.ratings + "\nDescription: " + this.description + "\nIMDBRating: "
                + this.IMDBRating;
    }
}
