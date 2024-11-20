package utils;

import model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CLI {
    private static Scanner scanner = null;

    private static void printCommand(IMDB.Command command) {
        switch (command) {
            case User -> System.out.print("Add/Remove user");
            case Actors -> System.out.print("View actors details");
            case Logout -> System.out.print("Logout");
            case Rating -> System.out.print("Add/Remove rating");
            case Search -> System.out.print("Search for actor/movie/series");
            case Update -> System.out.print("Update actor/production details");
            case Request -> System.out.print("Add/Remove request");
            case Database -> System.out.print("Add/Remove actor/production from system");
            case Favorite -> System.out.print("Add/Remove actor/production from favorites");
            case Productions -> System.out.print("View productions details");
            case SolveRequest -> System.out.print("Solve a request");
            case Notifications -> System.out.print("View notifications");
        }
    }

    public static void displayMenu(User user) {
        System.out.println("\nUsername: " + user.getUsername());
        if (user.getXp() != -1)
            System.out.println("User xp: " + user.getXp());
        else
            System.out.println("User xp: -");
        System.out.println("Choose action:");

        switch (user.getAccountType()) {
            case Admin -> {
                for (int i = 0; i < IMDB.adminCommands.size(); i++) {
                    System.out.print((i + 1) + ") ");
                    printCommand(IMDB.adminCommands.get(i));
                    System.out.println();
                }
            }
            case Regular -> {
                for (int i = 0; i < IMDB.regularCommands.size(); i++) {
                    System.out.print((i + 1) + ") ");
                    printCommand(IMDB.regularCommands.get(i));
                    System.out.println();
                }
            }
            case Contributor -> {
                for (int i = 0; i < IMDB.contributorCommands.size(); i++) {
                    System.out.print((i + 1) + ") ");
                    printCommand(IMDB.contributorCommands.get(i));
                    System.out.println();
                }
            }
            default -> throw new IllegalArgumentException("Invalid account type: " + user.getAccountType());
        }
    }

    public static User login() {
        if (scanner == null)
            scanner = new Scanner(System.in);
        User user = null;

        System.out.println("Welcome back! Please sign in!\n");

        while (user == null) {
            System.out.print("email: ");

            String email = scanner.nextLine();

            System.out.print("password: ");

            String password = scanner.nextLine();

            user = IMDB.getInstance().findUser(email, password);

            if (user == null)
                System.out.println("\u001B[31mError: Incorrect email or password! Please try again!\u001B[0m\n");
        }

        System.out.println("\nWelcome back user " + user.getUsername() + "!");

        return user;
    }

    public static IMDB.Command readCommand(AccountType accountType) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int command = readInt(1, 12);

        if (command == 0)
            return IMDB.Command.Error;

        switch (accountType) {
            case Regular -> {
                if (command > IMDB.regularCommands.size()){
                    System.err.println("Invalid command index. Please enter a valid index.");
                    return IMDB.Command.Error;
                }
                return IMDB.regularCommands.get(command - 1);
            }
            case Contributor -> {
                if (command > IMDB.contributorCommands.size()){
                    System.err.println("Invalid command index. Please enter a valid index.");
                    return IMDB.Command.Error;
                }
                return IMDB.contributorCommands.get(command - 1);
            }
            case Admin -> {
                if (command > IMDB.adminCommands.size()){
                    System.err.println("Invalid command index. Please enter a valid index.");
                    return IMDB.Command.Error;
                }
                return IMDB.adminCommands.get(command - 1);
            }
            default -> {
                return IMDB.Command.Error;
            }
        }
    }

    public static boolean stopLoop() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        System.out.println("If you want to close the application type 'exit'");
        System.out.println("Otherwise, press <ENTER>");

        String option = scanner.nextLine();

        return option.equals("exit");
    }

    public static String readSearchQuery() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        System.out.print("Search: ");

        return scanner.nextLine();
    }

    public static int readFavoritesQuery() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int query;

        System.out.println("1) Add to favorites");
        System.out.println("2) Remove from favorites");
        System.out.println("3) View favorites");

        do {
            query = readInt(1, 3);
        } while (query == 0);

        return query;
    }

    public static int readRequestQuery() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int query;

        System.out.println("1) Create request");
        System.out.println("2) Remove request");
        System.out.println("3) View your requests");

        do {
            query = readInt(1, 3);
        } while (query == 0);

        return query;
    }

    public static int readDatabaseQuery() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int query;

        System.out.println("1) Add production/actor");
        System.out.println("2) Remove production/actor");

        do {
            query = readInt(1, 2);
        } while (query == 0);

        return query;
    }

    public static String readFavoriteObject() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        System.out.print("Enter name of production/actor: ");

        return scanner.nextLine();
    }

    public static Request readRequest() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        RequestTypes requestType = null;
        String name = null;
        String description;

        while (requestType == null) {
            System.out.print("Request type (<DELETE_ACCOUNT>/<ACTOR_ISSUE>/<MOVIE_ISSUE>/<OTHERS>): ");
            String input = scanner.nextLine();

            try {
                requestType = RequestTypes.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid request type. Please try again.");
                System.err.flush();
            }
        }

        if (requestType == RequestTypes.ACTOR_ISSUE || requestType == RequestTypes.MOVIE_ISSUE) {
            while (name == null) {
                System.out.print("Production/Actor name: ");
                name = scanner.nextLine();
                boolean validName = false;

                if (requestType == RequestTypes.ACTOR_ISSUE) {
                    for (Actor actor : IMDB.getInstance().getActors()) {
                        if (actor.getName().equalsIgnoreCase(name)) {
                            validName = true;
                            break;
                        }
                    }
                }

                if (requestType == RequestTypes.MOVIE_ISSUE) {
                    for (Production production : IMDB.getInstance().getProductions()) {
                        if (production.getName().equalsIgnoreCase(name)) {
                            validName = true;
                            break;
                        }
                    }
                }

                if (!validName) {
                    System.err.println("Invalid name. Please try again.");
                    System.err.flush();
                    name = null;
                }
            }
        }

        System.out.print("Describe your request: ");
        description = scanner.nextLine();

        System.out.println("Your request has been submitted.");

        return new Request(requestType, name, description);
    }

    public static int readRemoveRequest(int size) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        if (size == 0) {
            System.out.println("You have no requests active.");
            return -1;
        }

        int index;

        System.out.print("Remove request: ");

        do {
            index = readInt(1, size);
        } while (index == 0);

        return index - 1;
    }

    public static int readSolveQuery() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int query;

        System.out.println("1) Solve request");
        System.out.println("2) Reject request");
        System.out.println("3) View requests");

        do {
            query = readInt(1, 3);
        } while (query == 0);

        return query;
    }

    public static int readRatingQuery() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int query;

        System.out.println("1) Add rating");
        System.out.println("2) Remove rating");

        do {
            query = readInt(1, 2);
        } while (query == 0);

        return query;
    }

    public static int readUserQuery() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int query;

        System.out.println("1) Add user");
        System.out.println("2) Remove user");
        System.out.println("3) View users");

        do {
            query = readInt(1, 3);
        } while (query == 0);

        return query;
    }

    public static Production readRatedProduction(String username) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        System.out.print("Production name: ");

        String name = scanner.nextLine();
        Production production = IMDB.getInstance().findProduction(name);

        while (production == null || production.findRating(username) != null) {
            System.err.println("Production not found, or already rated. Try again");
            System.out.print("Production name: ");
            name = scanner.nextLine();
            production = IMDB.getInstance().findProduction(name);
        }

        return production;
    }

    public static User readUser() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int age;
        char gender;
        AccountType accountType = null;

        while (accountType == null) {
            System.out.print("Account type: ");
            String input = scanner.nextLine();

            try {
                accountType = AccountType.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid account type. Please try again.");
                System.err.flush();
            }
        }

        System.out.print("Name: ");
        String name = scanner.nextLine();

        String username = name.toLowerCase().replace(' ', '_');
        username = username + "_" + IMDB.getInstance().generateId(username, name);

        System.out.print("Email: ");
        String email = scanner.nextLine();

        String password = User.generatePassword(12);

        System.out.print("Country: ");
        String country = scanner.nextLine();

        System.out.print("Age: ");
        do {
            age = readInt(1, 150);
        } while (age == 0);

        System.out.print("Gender: ");
        String input = scanner.nextLine();
        while (input.isEmpty())
            input = scanner.nextLine();
        while (input.charAt(0) != 'F' && input.charAt(0) != 'M' && input.charAt(0) != 'N') {
            System.err.println("Invalid gender.");
            System.err.flush();
            do {
                input = scanner.nextLine();
            } while (input.isEmpty());
        }
        gender = input.charAt(0);

        LocalDateTime birthdate = readBirthdate();

        User.Information information = new User.Information.Builder(name, email, password).setBirthdate(birthdate)
                .setGender(gender).setAge(age).setCountry(country).build();

        return UserFactory.createUser(information, accountType, username, 0);
    }

    public static LocalDateTime readBirthdate() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (true) {
            try {
                System.out.print("Birthdate (yyyy-MM-dd HH:mm:ss): ");
                String input = scanner.nextLine();

                return LocalDateTime.parse(input, formatter);
            } catch (Exception e) {
                System.err.println("Error: Invalid date format. Please enter the date in the specified format.");
            }
        }
    }

    public static Rating readRating(String username) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int score;

        System.out.print("Score: ");

        do {
            score = readInt(1, 10);
        } while (score == 0);

        System.out.print("Comment: ");

        String comment = scanner.nextLine();

        return new Rating(username, score, comment);
    }

    public static int readRemoveSystem(int size) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        if (size == 0) {
            System.out.println("You have no contributions.");
            return -1;
        }

        int index;

        System.out.print("Remove production/actor: ");

        do {
            index = readInt(1, size);
        } while (index == 0);

        return index - 1;
    }

    public static int readRemoveRating(int size) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        if (size == 0) {
            System.out.println("You have no ratings.");
            return -1;
        }

        int index;

        System.out.print("Remove rating: ");

        do {
            index = readInt(1, size);
        } while (index == 0);

        return index - 1;
    }

    public static int readRemoveUser(int size) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        if (size == 0) {
            System.out.println("No users to remove.");
            return -1;
        }

        int index;

        System.out.print("Remove user: ");

        do {
            index = readInt(1, size);
        } while (index == 0);

        return index - 1;
    }

    public static ArtisticEntity readEntity() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int query;

        System.out.println("1) Read production");
        System.out.println("2) Read actor");

        do {
            query = readInt(1, 2);
        } while (query == 0);

        if (query == 1)
            return readProduction(false);

        return readActor(false);
    }

    public static Actor readActor(boolean update) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        String name;
        ProductionType productionType;
        Actor actor;
        String input;

        System.out.print("Name of actor: ");

        do {
            name = scanner.nextLine();
        } while (name.isEmpty());

        while (!update && IMDB.getInstance().findActor(name) != null) {
            System.err.println("Actor with name " + name + " already exists. Try another name.");
            System.out.print("Name of actor: ");
            do {
                name = scanner.nextLine();
            } while (name.isEmpty());
        }

        actor = new Actor(name);

        System.out.print("Biography: ");
        actor.setBiography(scanner.nextLine());

        System.out.print("Performance count: ");
        int performanceCount = readInt(1, 9999999);
        while (performanceCount == 0)
            performanceCount = readInt(1, 9999999);

        for (int i = 1; i <= performanceCount; i++) {
            productionType = null;

            System.out.print("Name: ");
            String productionName = scanner.nextLine();

            while (productionType == null) {
                System.out.print("Production type (<Movie>/<Series>): ");
                input = scanner.nextLine();

                try {
                    productionType = ProductionType.valueOf(input);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid production type. Please try again.");
                    System.err.flush();
                }
            }

            actor.addPerformance(productionName, productionType);

            Production production = IMDB.getInstance().findProduction(productionName);
            if (production != null) {
                production.addActor(name);
            }
        }

        return actor;
    }

    public static Production readProduction(boolean update) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        String name;
        ProductionType productionType = null;
        Production production;
        String input;

        System.out.print("Name of production: ");

        do {
            name = scanner.nextLine();
        } while (name.isEmpty());

        while (!update && IMDB.getInstance().findProduction(name) != null) {
            System.err.println("Production with name " + name + " already exists. Try another name.");
            System.out.print("Name of production: ");
            do {
                name = scanner.nextLine();
            } while (name.isEmpty());
        }

        while (productionType == null) {
            System.out.print("Production type (<Movie>/<Series>): ");
            input = scanner.nextLine();

            try {
                productionType = ProductionType.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid production type. Please try again.");
                System.err.flush();
            }
        }

        if (productionType == ProductionType.Movie)
            production = new Movie(name);
        else
            production = new Series(name);

        System.out.print("Description: ");
        production.setDescription(scanner.nextLine());

        System.out.print("Directors (press <ENTER> when done): ");

        while (true) {
            input = scanner.nextLine();
            if (input.isEmpty())
                break;

            production.addDirector(input);
        }

        System.out.print("Actors (press <ENTER> when done): ");

        while (true) {
            input = scanner.nextLine();
            if (input.isEmpty())
                break;

            production.addActor(input);
            Actor actor = IMDB.getInstance().findActor(input);

            if (actor != null)
                actor.addPerformance(name, productionType);
        }

        System.out.print("Genres (press <ENTER> when done): ");

        while (true) {
            input = scanner.nextLine();
            if (input.isEmpty())
                break;

            try {
                production.addGenre(Genre.valueOf(input));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid genre.");
                System.err.flush();
            }
        }

        if (productionType == ProductionType.Movie) {
            Movie movie = (Movie) production;

            System.out.print("Length (press <ENTER> to skip): ");

            movie.setLength(readOptionalInt());

            System.out.print("Release Year (press <ENTER> to skip): ");

            movie.setReleaseYear(readOptionalInt());

            return movie;
        }

        Series series = (Series) production;

        System.out.print("Release Year (press <ENTER> to skip): ");

        series.setReleaseYear(readOptionalInt());

        System.out.print("Number of seasons: ");

        int seasonCount = readInt(1, 9999999);
        while (seasonCount == 0)
            seasonCount = readInt(1, 9999999);

        series.setSeasonCount(seasonCount);

        for (int i = 1; i <= seasonCount; i++) {
            System.out.print("Season name: ");
            String season = scanner.nextLine();

            System.out.print("Number of episodes: ");
            int episodeCount = readInt(1, 9999999);
            while (episodeCount == 0)
                episodeCount = readInt(1, 9999999);

            for (int j = 1; j <= episodeCount; j++) {
                series.addEpisode(season, readEpisode());
            }
        }

        return series;
    }

    public static int readInt(int min, int max) {
        if (scanner == null)
            scanner = new Scanner(System.in);

        int num;

        try {
            num = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.err.println("Invalid input. Please enter a number.");
            scanner.nextLine();
            return min - 1;
        }

        if (num < min || num > max) {
            System.err.println("Invalid input. Please enter a number.");
            scanner.nextLine();
            return min - 1;
        }

        return num;
    }

    public static int readOptionalInt() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        if (input.isEmpty()) {
            return -1;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a valid integer.");
            return readOptionalInt();
        }
    }

    public static void displaySearchResults(ArrayList<ArtisticEntity> searchResults) {
        if (searchResults.isEmpty()) {
            System.out.println("Nothing found");
        }
        else {
            System.out.println("Search Results:");
            displayEntities(searchResults);
        }
    }

    public static Episode readEpisode() {
        if (scanner == null)
            scanner = new Scanner(System.in);

        System.out.print("Episode name: ");
        String name = scanner.nextLine();

        System.out.print("Length: ");
        int length = readInt(1, 9999999);
        while (length == 0)
            length = readInt(1, 9999999);

        return new Episode(name, length);
    }

    public static void displayProduction(Production production) {
        production.getRatings().sort(Comparator.comparingInt((Rating rating) -> IMDB.getInstance().findUser(rating.getUsername()).getXp()).reversed());

        System.out.println("Title: " + production.getName());

        System.out.println("Directors: {");

        for (String director : production.getDirectors())
            System.out.println("  " + director);

        System.out.println("}");
        System.out.println("Actors: {");

        for (String actor : production.getActors())
            System.out.println("  " + actor);

        System.out.println("}");
        System.out.println("Genres: {");

        for (Genre genre : production.getGenres())
            System.out.println("  " + genre);

        System.out.println("}");
        System.out.println("Ratings: {");

        for (Rating rating : production.getRatings())
            System.out.println(rating);

        System.out.println("}");
        System.out.println("Plot: " + production.getDescription());

        if (production.getIMDBRating() == -1)
            System.out.println("Average rating: -");
        else
            System.out.println("Average rating: " + production.getIMDBRating());

        production.displayInfo();

        System.out.println();
    }

    public static void displayUsers(ArrayList<User> users) {
        int index = 1;
        for (User user : users) {
            System.out.println(index++ + ")");
            System.out.println(user);
        }
    }

    public static void displayActor(Actor actor) {
        System.out.println("Name: " + actor.getName());
        System.out.println("Performances: {");

        for (Pair<String, ProductionType> pair : actor.getFilmography()) {
            System.out.println(pair);
        }

        System.out.println("}");
        System.out.println("Biography: " + actor.getBiography());

        System.out.println();
    }

    public static void displayEntities(ArrayList<? extends ArtisticEntity> entities) {
        int index = 1;
        for (ArtisticEntity entity : entities) {
            System.out.println(index++ + ")");
            if (entity instanceof Actor)
                displayActor((Actor) entity);
            else
                displayProduction((Production) entity);
        }
    }

    public static void displayNotifications(User user) {
        for (String notification : user.getNotifications()) {
            System.out.println(notification);
        }
    }

    public static void displayRequests(ArrayList<Request> requests) {
        int index = 1;
        for (Request request : requests) {
            System.out.println("Request " + index++ + "):");
            System.out.println(request);
        }
    }

    public static void displayUserRatings(HashMap<Rating, Production> ratings) {
        int index = 1;
        for (Rating rating : ratings.keySet()) {
            System.out.println(index++ + ") " + ratings.get(rating).getName());
            System.out.println("Score: " + rating.getScore());
            System.out.println("Comment: " + rating.getComments());
            System.out.println();
        }
    }
}
