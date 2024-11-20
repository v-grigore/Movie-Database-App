package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Actor extends ArtisticEntity{
    private final String name;
    private final ArrayList<Pair<String, ProductionType>> filmography;
    private String biography;

    public Actor(String name) {
        this.name = name;
        this.biography = "";
        filmography = new ArrayList<>();
        this.setContributor("ADMIN");
    }

    public Actor(String name, String biography, ArrayList<Pair<String, ProductionType>> filmography) {
        this.name = name;
        this.biography = biography;
        this.filmography = filmography;
        this.setContributor("ADMIN");
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public ArrayList<Pair<String, ProductionType>> getFilmography() {
        return filmography;
    }

    public void addPerformance(String name, ProductionType productionType) {
        filmography.add(new Pair<>(name, productionType));
    }

    public void removePerformance(String name) {
        filmography.removeIf(pair -> pair.first.equals(name));
    }

    @Override
    public int calculateRelevance(String searchQuery) {
        String[] words = searchQuery.toLowerCase().split("\\s+");
        int relevanceScore = 0;

        if (searchQuery.equalsIgnoreCase(this.name)) {
            relevanceScore += 5;
        }

        if (this.name.toLowerCase().contains(searchQuery.toLowerCase()) ||
                searchQuery.toLowerCase().contains(this.getName().toLowerCase())) {
            relevanceScore += 3;
        }

        if (Arrays.stream(words).allMatch(word -> this.name.toLowerCase().contains(word))) {
            relevanceScore += 5;
        }

        for (String actorWord : this.name.toLowerCase().split("\\s+")) {
            if (Arrays.asList(words).contains(actorWord)) {
                relevanceScore += 2;
                break;
            }
        }

        if (this.biography != null && this.biography.toLowerCase().contains(searchQuery.toLowerCase())) {
            relevanceScore += 2;
        }

        if (this.biography != null && Arrays.stream(words).allMatch(word -> this.biography.toLowerCase().contains(word))) {
            relevanceScore += 2;
        }

        for (Pair<String, ProductionType> production : this.filmography) {
            if (searchQuery.toLowerCase().contains(production.first.toLowerCase())) {
                relevanceScore += 1;
                break;
            }
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
        return "Name: " + this.name + "\nBiography: " + this.biography + "\nFilmography: " + this.filmography;
    }
}
