package utils;

import model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public class Parser {
    public static <T>void parseData(String path, ArrayList<T> list, Function<JSONObject, T> parseObjectT) {
        try (FileReader reader = new FileReader(path)) {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                list.add(parseObjectT.apply(jsonObject));
            }

        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
    }

    public static Request parseRequest(JSONObject jsonObject) {
        RequestTypes requestType = RequestTypes.valueOf((String) jsonObject.get("type"));
        String username = (String) jsonObject.get("username");
        String description = (String) jsonObject.get("description");
        String to = (String) jsonObject.get("to");
        String name = null;

        if (requestType == RequestTypes.MOVIE_ISSUE)
            name = (String) jsonObject.get("movieTitle");

        if (requestType == RequestTypes.ACTOR_ISSUE)
            name = (String) jsonObject.get("actorName");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime submissionDate = LocalDateTime.parse((String) jsonObject.get("createdDate"), formatter);

        return new Request(requestType, submissionDate, name, description, username, to);
    }

    public static Actor parseActor(JSONObject jsonObject) {
        String name = (String) jsonObject.get("name");
        String biography = (String) jsonObject.get("biography");

        JSONArray jsonArray = (JSONArray) jsonObject.get("performances");
        ArrayList<Pair<String, ProductionType>> filmography = new ArrayList<>();

        for (Object object : jsonArray) {
            JSONObject jsonPair = (JSONObject) object;

            String title = (String) jsonPair.get("title");
            ProductionType type = ProductionType.valueOf((String) jsonPair.get("type"));

            filmography.add(new Pair<>(title, type));
        }

        return new Actor(name, biography, filmography);
    }

    public static Production parseProduction(JSONObject jsonObject) {
        String title = (String) jsonObject.get("title");
        ProductionType productionType = ProductionType.valueOf((String) jsonObject.get("type"));
        ArrayList<String> directors = new ArrayList<>((JSONArray) jsonObject.get("directors"));
        ArrayList<String> actors = new ArrayList<>((JSONArray) jsonObject.get("actors"));

        JSONArray jsonArray = (JSONArray) jsonObject.get("genres");
        ArrayList<Genre> genres = new ArrayList<>();
        for (Object object : jsonArray) {
            String string = (String) object;
            Genre genre = Genre.valueOf(string);
            genres.add(genre);
        }

        String plot = (String) jsonObject.get("plot");
        double averageRating = (double) jsonObject.get("averageRating");

        JSONArray jsonArray1 = (JSONArray) jsonObject.get("ratings");
        ArrayList<Rating> ratings = new ArrayList<>();
        for (Object object : jsonArray1) {
            JSONObject jsonRating = (JSONObject) object;

            String username = (String) jsonRating.get("username");
            long rating = (long) jsonRating.get("rating");
            String comment = (String) jsonRating.get("comment");

            ratings.add(new Rating(username, (int) rating, comment));
        }

        if (productionType == ProductionType.Movie) {
            int releaseYear = jsonObject.containsKey("releaseYear") ? (int) ((long) jsonObject.get("releaseYear")) : -1;
            String durationString = jsonObject.containsKey("duration") ? (String) jsonObject.get("duration") : null;
            int duration = durationString != null ? Integer.parseInt(durationString.replaceAll("[^0-9]", "")) : -1;

            return new Movie(title, directors, actors, genres, ratings, plot, averageRating, duration, releaseYear);
        }

        int releaseYear = jsonObject.containsKey("releaseYear") ? (int) ((long) jsonObject.get("releaseYear")) : -1;
        int numSeasons = (int) ((long) jsonObject.get("numSeasons"));

        JSONObject jsonSeasons = (JSONObject) jsonObject.get("seasons");
        TreeMap<String, ArrayList<Episode>> episodes = new TreeMap<>();

        for (Object object : jsonSeasons.keySet()) {
            String seasonName = (String) object;
            JSONArray jsonEpisodes = (JSONArray) jsonSeasons.get(seasonName);

            ArrayList<Episode> episodeList = new ArrayList<>();
            for (Object episodeObject : jsonEpisodes) {
                JSONObject jsonEpisode = (JSONObject) episodeObject;

                String episodeName = (String) jsonEpisode.get("episodeName");
                String duration = (String) jsonEpisode.get("duration");
                int length = Integer.parseInt(duration.replaceAll("[^0-9]", ""));

                episodeList.add(new Episode(episodeName, length));
            }

            episodes.put(seasonName, episodeList);
        }

        return new Series(title, directors, actors, genres, ratings, plot, averageRating, releaseYear, numSeasons, episodes);
    }

    public static User parseUser(JSONObject jsonObject) {
        String username = (String) jsonObject.get("username");
        String experienceString = (String) jsonObject.get("experience");
        String userTypeString = (String) jsonObject.get("userType");

        JSONObject jsonInformation = (JSONObject) jsonObject.get("information");

        JSONObject jsonCredentials = (JSONObject) jsonInformation.get("credentials");

        String email = (String) jsonCredentials.get("email");
        String password = (String) jsonCredentials.get("password");

        String name = (String) jsonInformation.get("name");
        String country = (String) jsonInformation.get("country");
        long age = (long) jsonInformation.get("age");
        String genderString = (String) jsonInformation.get("gender");
        String birthDateString = (String) jsonInformation.get("birthDate");

        List<String> productionsContribution = (jsonObject.containsKey("productionsContribution") ?
                (List<String>) jsonObject.get("productionsContribution") : null);

        List<String> actorsContribution = (jsonObject.containsKey("actorsContribution") ?
                (List<String>) jsonObject.get("actorsContribution") : null);

        List<String> favoriteProductions = (jsonObject.containsKey("favoriteProductions") ?
                (List<String>) jsonObject.get("favoriteProductions") : null);

        List<String> favoriteActors = (jsonObject.containsKey("favoriteActors") ?
                (List<String>) jsonObject.get("favoriteActors") : null);

        int xp = experienceString != null ? Integer.parseInt(experienceString) : -1;
        AccountType accountType = AccountType.valueOf(userTypeString);
        char gender = genderString.charAt(0);
        LocalDateTime birthdate = LocalDate.parse(birthDateString, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();

        User.Information information = new User.Information.Builder(name, email, password).setBirthdate(birthdate)
                .setGender(gender).setAge((int) age).setCountry(country).build();

        TreeSet<ArtisticEntity> favorites = new TreeSet<>();
        TreeSet<ArtisticEntity> contributions = new TreeSet<>();
        ArrayList<String> notifications;

        if (jsonObject.containsKey("notifications")) {
            JSONArray notificationsArray = (JSONArray) jsonObject.get("notifications");
            notifications = new ArrayList<>(notificationsArray);
        }
        else notifications = new ArrayList<>();

        if (favoriteProductions != null) {
            for (String production : favoriteProductions) {
                favorites.add(IMDB.getInstance().findProduction(production));
            }
        }

        if (favoriteActors != null) {
            for (String actor : favoriteActors) {
                favorites.add(IMDB.getInstance().findActor(actor));
            }
        }

        if (productionsContribution != null) {
            for (String production : productionsContribution) {
                contributions.add(IMDB.getInstance().findProduction(production));
            }
        }

        if (actorsContribution != null) {
            for (String actor : actorsContribution) {
                contributions.add(IMDB.getInstance().findActor(actor));
            }
        }

        return UserFactory.createUser(information, accountType, username, xp, favorites, contributions, notifications);
    }
}
