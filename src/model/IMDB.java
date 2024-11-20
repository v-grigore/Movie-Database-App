package model;

import gui.GUI;
import gui.LoginForm;
import utils.CLI;
import utils.Parser;

import javax.swing.*;
import java.util.*;

public class IMDB {
    public enum Command {
        Productions,
        Actors,
        Notifications,
        Search,
        Favorite,
        Request,
        Database,
        SolveRequest,
        Update,
        Rating,
        User,
        Logout,
        Error
    }

    public static ArrayList<Command> adminCommands = new ArrayList<>(Arrays.asList(Command.Productions, Command.Actors,
            Command.Notifications, Command.Search, Command.Favorite, Command.Database, Command.SolveRequest,
            Command.Update, Command.User, Command.Logout));

    public static ArrayList<Command> contributorCommands = new ArrayList<>(Arrays.asList(Command.Productions,
            Command.Actors, Command.Notifications, Command.Search, Command.Favorite, Command.Request, Command.Database,
            Command.SolveRequest, Command.Update, Command.Logout));

    public static ArrayList<Command> regularCommands = new ArrayList<>(Arrays.asList(Command.Productions,
            Command.Actors, Command.Notifications, Command.Search, Command.Favorite, Command.Request, Command.Rating,
            Command.Logout));

    private static IMDB instance = null;

    private final ArrayList<User> users;
    private final ArrayList<Actor> actors;
    private final ArrayList<Request> requests;
    private final ArrayList<Production> productions;
    private User activeUser;
    private int nextId = 1;

    private IMDB() {
        users = new ArrayList<>();
        actors = new ArrayList<>();
        requests = new ArrayList<>();
        productions = new ArrayList<>();
        activeUser = null;
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }

        return instance;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void removeRequest(Request request) {
        requests.remove(request);
    }

    public void addProduction(Production production) {
        productions.add(production);
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public void removeProduction(Production production) {
        productions.remove(production);
        for (User user : users) {
            if (user.getFavorites().contains(production))
                user.removeFromFavorites(production);
        }
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
        for (User user : users) {
            if (user.getFavorites().contains(actor))
                user.removeFromFavorites(actor);
        }
    }

    public Production findProduction(String name) {
        if (name == null)
            return null;

        for (Production production : productions) {
            if (production.getName().equalsIgnoreCase(name))
                return production;
        }
        return null;
    }

    public Actor findActor(String name) {
        if (name == null)
            return null;

        for (Actor actor : actors) {
            if (actor.getName().equalsIgnoreCase(name))
                return actor;
        }
        return null;
    }

    public User findUser(String email, String password) {
        for (User user : users) {
            if (user.correctCredentials(email, password))
                return user;
        }
        return null;
    }

    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public ArrayList<Production> getProductions() {
        return productions;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public int generateId(String username, String name) {
        int id = 1;
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name))
                id++;
            if (user.getUsername().equalsIgnoreCase(username))
                id++;
        }
        return id;
    }

    public Staff findSolver(Request request) {
        if (request.getName() == null)
            return null;

        for (User user : users) {
            if (user instanceof Regular)
                continue;

            if (((Staff) user).findContribution(request.getName()) != null) {
                return (Staff) user;
            }
        }
        return null;
    }

    public ArrayList<ArtisticEntity> searchResults(String searchQuery) {
        ArrayList<ArtisticEntity> searchResults = new ArrayList<>();

        for (ArtisticEntity entity : productions) {
            if (entity.calculateRelevance(searchQuery) != 0) {
                searchResults.add(entity);
            }
        }

        for (ArtisticEntity entity : actors) {
            if (entity.calculateRelevance(searchQuery) != 0) {
                searchResults.add(entity);
            }
        }

        searchResults.sort(Comparator.comparingInt((ArtisticEntity entity) -> entity.calculateRelevance(searchQuery)).reversed());

        return searchResults;
    }

    void userLogout() {
        this.activeUser = null;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public ArrayList<ArtisticEntity> getMyContributions() {
        if (activeUser instanceof Regular)
            return new ArrayList<>();

        ArrayList<ArtisticEntity> myContributions = new ArrayList<>(((Staff) activeUser).getContributions());

        if (activeUser instanceof Admin) {
            for (Production production : productions) {
                if (production.getContributor().equals("ADMIN"))
                    myContributions.add(production);
            }
            for (Actor actor : actors) {
                if (actor.getContributor().equals("ADMIN"))
                    myContributions.add(actor);
            }
        }
        return myContributions;
    }

    public ArrayList<Request> getMySentRequests() {
        ArrayList<Request> myRequests = new ArrayList<>();
        for (Request request : requests)
            if (request.getSubmitterUsername().equals(activeUser.getUsername()))
                myRequests.add(request);
        return myRequests;
    }

    public ProductionType getProductionType(Production production) {
        if (production instanceof Movie)
            return ProductionType.Movie;
        return ProductionType.Series;
    }

    public void addRating(Rating rating, Production production) {
        Rating oldRating = production.findRating(activeUser.getUsername());
        if (oldRating != null)
            removeRating(oldRating, production);

        ((Regular) activeUser).addRating(rating, production);
    }

    public void removeRating(Rating rating, Production production) {
        production.removeRating(rating);
        activeUser.setExperienceStrategy(new RatingExperience());
        activeUser.setXp(activeUser.getXp() - activeUser.getExperienceStrategy().calculateExperience());
    }

    public void run(Mode mode) {
        Parser.parseData("resources/production.json", this.productions, Parser::parseProduction);
        Parser.parseData("resources/actors.json", this.actors, Parser::parseActor);
        Parser.parseData("resources/accounts.json", this.users, Parser::parseUser);
        Parser.parseData("resources/requests.json", this.requests, Parser::parseRequest);

        for (Production production : productions)
            production.setId(nextId++);

        for (Actor actor : actors)
            actor.setId(nextId++);

        for (Production production : productions) {
            for (String actorName : production.getActors()) {
                if (findActor(actorName) == null) {
                    Actor actor = new Actor(actorName);
                    actor.setId(nextId++);
                    for (Production production1 : productions) {
                        if (production1.getActors().contains(actorName)) {
                            actor.addPerformance(production1.getName(), getProductionType(production1));
                        }
                    }
                    actors.add(actor);
                }
            }
        }

        for (User user : users) {
            if (user instanceof Staff) {
                Staff staff = (Staff) user;
                for (ArtisticEntity contribution : staff.getContributions()) {
                    contribution.setContributor(staff.getUsername());
                }
            }
        }

        for (Request request : requests) {
            request.addObserver(findUser(request.getSubmitterUsername()));
            if (request.getSolverUsername().equals("ADMIN")) {
                Admin.RequestsHolder.addRequest(request);
                Admin.RequestsHolder.notifyAdmins("You received a new request: " + request.getDescription());
            }
            else {
                for (User user : users) {
                    if (request.getSolverUsername().equals(user.getUsername())) {
                        ((Staff) user).getRequests().add(request);
                        user.update("You received a new request: " + request.getDescription());
                    }
                }
            }
        }

        if (mode == Mode.GUI) {
            SwingUtilities.invokeLater(() -> {
                GUI gui = new GUI();
            });
            return;
        }

        boolean loop = true;

        while (loop) {
            if (activeUser == null)
                this.activeUser = CLI.login();

            CLI.displayMenu(activeUser);

            Command command = CLI.readCommand(activeUser.getAccountType());

            switch (command) {
                case Productions -> CLI.displayEntities(this.productions);
                case Actors -> CLI.displayEntities(this.actors);
                case Notifications -> CLI.displayNotifications(activeUser);
                case Search -> {
                    String searchQuery = CLI.readSearchQuery();

                    ArrayList<ArtisticEntity> searchResults = searchResults(searchQuery);

                    CLI.displaySearchResults(searchResults);
                }
                case Favorite -> {
                    int query;
                    do {
                        query = CLI.readFavoritesQuery();
                    } while (query == -1);

                    ArtisticEntity entity = null;

                    if (query != 3) {
                        String name = CLI.readFavoriteObject();
                        entity = findActor(name);
                        if (entity == null)
                            entity = findProduction(name);
                    }

                    switch (query) {
                        case 1 -> activeUser.addToFavorites(entity);
                        case 2 -> activeUser.removeFromFavorites(entity);
                        case 3 -> CLI.displayEntities(new ArrayList<>(activeUser.getFavorites()));
                    }
                }
                case Request -> {
                    int query;
                    ArrayList<Request> myRequests = null;
                    do {
                        query = CLI.readRequestQuery();
                    } while (query == -1);

                    if (query != 1) {
                        myRequests = new ArrayList<>();
                        for (Request request : requests)
                            if (request.getSubmitterUsername().equals(activeUser.getUsername()))
                                myRequests.add(request);
                        CLI.displayRequests(myRequests);
                    }

                    switch (query) {
                        case 1 -> ((RequestsManager) activeUser).createRequest(CLI.readRequest());
                        case 2 -> {
                            int index = CLI.readRemoveRequest(myRequests.size());
                            if (index == -1)
                                break;

                            Request request = myRequests.get(index);
                            ((RequestsManager) activeUser).removeRequest(request);
                        }
                    }

                }
                case Database -> {
                    int query;

                    do {
                        query = CLI.readDatabaseQuery();
                    } while (query == -1);

                    switch (query) {
                        case 1 -> {
                            ArtisticEntity entity = CLI.readEntity();
                            entity.setId(nextId++);
                            if (entity instanceof Production)
                                ((Staff) activeUser).addProductionSystem((Production) entity);
                            else
                                ((Staff) activeUser).addActorSystem((Actor) entity);

                            if (activeUser instanceof Admin)
                                break;
                            activeUser.setExperienceStrategy(new ContributionExperience());
                            activeUser.setXp(activeUser.getXp() + activeUser.getExperienceStrategy().calculateExperience());
                        }
                        case 2 -> {
                            ArrayList<ArtisticEntity> myContributions = getMyContributions();

                            CLI.displayEntities(myContributions);

                            int index = CLI.readRemoveSystem(myContributions.size());
                            if (index == -1)
                                break;

                            ArtisticEntity entity = myContributions.get(index);
                            if (entity instanceof Production)
                                ((Staff) activeUser).removeProductionSystem((Production) entity);
                            else
                                ((Staff) activeUser).removeActorSystem((Actor) entity);

                            if (activeUser instanceof Admin)
                                break;
                            activeUser.setExperienceStrategy(new ContributionExperience());
                            activeUser.setXp(activeUser.getXp() - activeUser.getExperienceStrategy().calculateExperience());
                        }
                    }
                }
                case SolveRequest -> {
                    ArrayList<Request> myRequests = new ArrayList<>(((Staff) activeUser).getRequests());

                    if (activeUser instanceof Admin) {
                        myRequests.addAll(Admin.RequestsHolder.getRequests());
                    }

                    int query = CLI.readSolveQuery();

                    CLI.displayRequests(myRequests);

                    if (query == 3)
                        break;

                    int index = CLI.readRemoveRequest(myRequests.size());
                    if (index == -1)
                        break;
                    Request request = myRequests.get(index);
                    ((Staff) activeUser).solveRequest(request);

                    if (query == 2) {
                        request.notifyObservers("Your request has been rejected: " + request.getDescription());
                        break;
                    }

                    request.notifyObservers("Your request has been solved: " + request.getDescription());

                    if (activeUser instanceof Admin)
                        break;

                    if (request.getRequestType() == RequestTypes.ACTOR_ISSUE || request.getRequestType() == RequestTypes.MOVIE_ISSUE) {
                        User user = findUser(request.getSubmitterUsername());
                        user.setExperienceStrategy(new RequestExperience());
                        user.setXp(user.getXp() + user.getExperienceStrategy().calculateExperience());
                    }
                }
                case Update -> {
                    ArrayList<ArtisticEntity> myContributions = getMyContributions();

                    CLI.displayEntities(myContributions);

                    int index = CLI.readRemoveSystem(myContributions.size());
                    if (index == -1)
                        break;

                    ArtisticEntity oldEntity = myContributions.get(index);
                    ArtisticEntity newEntity;
                    if (oldEntity instanceof Production) {
                        newEntity = CLI.readProduction(true);
                        ((Staff) activeUser).updateProduction((Production) oldEntity, (Production) newEntity);
                    }
                    else {
                        newEntity = CLI.readActor(true);
                        ((Staff) activeUser).updateActor((Actor) oldEntity, (Actor) newEntity);
                    }

                }
                case Rating -> {
                    int query = CLI.readRatingQuery();

                    if (query == 1) {
                        Production production = CLI.readRatedProduction(activeUser.getUsername());
                        Rating rating = CLI.readRating(activeUser.getUsername());
                        ((Regular) activeUser).addRating(rating, production);
                        break;
                    }

                    LinkedHashMap<Rating, Production> map = new LinkedHashMap<>();
                    LinkedList<Rating> myRatings = new LinkedList<>();
                    for (Production production : productions) {
                        Rating rating = production.findRating(activeUser.getUsername());
                        if (rating == null)
                            continue;

                        map.put(rating, production);
                        myRatings.add(rating);
                    }

                    CLI.displayUserRatings(map);

                    int index = CLI.readRemoveRating(myRatings.size());
                    if (index == -1)
                        break;

                    Rating rating = myRatings.get(index);

                    map.get(rating).removeRating(rating);

                    activeUser.setExperienceStrategy(new RatingExperience());
                    activeUser.setXp(activeUser.getXp() - activeUser.getExperienceStrategy().calculateExperience());
                }
                case User -> {
                    int query = CLI.readUserQuery();

                    if (query == 1) {
                        User user = CLI.readUser();
                        ((Admin) activeUser).addUser(user);
                        break;
                    }

                    if (query == 3) {
                        CLI.displayUsers(users);
                        break;
                    }

                    ArrayList<User> nonAdmins = new ArrayList<>();
                    for (User user : users) {
                        if (user instanceof Admin)
                            continue;
                        nonAdmins.add(user);
                    }

                    CLI.displayUsers(nonAdmins);

                    int index = CLI.readRemoveUser(nonAdmins.size());
                    if (index == -1)
                        break;
                    ((Admin) activeUser).removeUser(nonAdmins.get(index));
                }
                case Logout -> {
                    activeUser.logout();
                    if (CLI.stopLoop())
                        loop = false;
                }
            }
        }
    }
}
