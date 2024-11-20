package model;

import java.util.ArrayList;
import java.util.TreeMap;

public class Series extends Production{
    private int releaseYear;
    private int seasonCount;
    private final TreeMap<String, ArrayList<Episode>> episodes;

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public int getEpisodeCount() {
        int epCount = 0;
        for (String season : episodes.keySet()) {
            epCount += episodes.get(season).size();
        }
        return epCount;
    }

    public TreeMap<String, ArrayList<Episode>> getEpisodes() {
        return episodes;
    }

    public void addEpisode(String season, Episode episode) {
        if (!episodes.containsKey(season)) {
            ArrayList<Episode> list = new ArrayList<>();
            list.add(episode);
            episodes.put(season, list);
            return;
        }

        episodes.get(season).add(episode);
    }

    public Series(String name) {
        super(name);
        releaseYear = -1;
        seasonCount = -1;
        episodes = new TreeMap<>();
        this.setContributor("ADMIN");
    }

    @SuppressWarnings("unused")
    public Series(String name, int releaseYear, int seasonCount) {
        super(name);
        this.releaseYear = releaseYear;
        this.seasonCount = seasonCount;
        episodes = new TreeMap<>();
        this.setContributor("ADMIN");
    }

    public Series(String name, ArrayList<String> directors, ArrayList<String> actors, ArrayList<Genre> genres,
                  ArrayList<Rating> ratings, String description, double IMDBRating, int releaseYear, int seasonCount,
                  TreeMap<String, ArrayList<Episode>> episodes) {
        super(name, directors, actors, genres, ratings, description, IMDBRating);
        this.releaseYear = releaseYear;
        this.seasonCount = seasonCount;
        this.episodes = episodes;
        this.setContributor("ADMIN");
    }

    @Override
    public void displayInfo() {
        if (releaseYear != -1)
            System.out.println("Release year: " + releaseYear);

        System.out.println("Number of Seasons: " + seasonCount);
        System.out.println("Seasons: {");

        for (String key : episodes.keySet()) {
            System.out.println("  " + key + ": {");

            for (Episode episode : episodes.get(key)) {
                System.out.println(episode);
            }

            System.out.println("  }");
        }

        System.out.println("}");
    }

    @Override
    public String toString() {
        return super.toString() + "\nReleaseYear: " + this.releaseYear + "\nSeasonCount: " + this.seasonCount
                + "\nEpisodes: " + this.episodes;
    }
}
