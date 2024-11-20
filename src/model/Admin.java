package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class Admin extends Staff {
    public Admin(Information information, AccountType accountType, String username, int xp) {
        super(information, accountType, username, xp);
        this.setXp(-1);
    }

    public Admin(Information information, AccountType accountType, String username, int xp,
                 TreeSet<ArtisticEntity> favorites, TreeSet<ArtisticEntity> contributions,
                 ArrayList<String> notifications) {
        super(information, accountType, username, xp, favorites, contributions, notifications);
    }

    public void addUser(User user) {
        IMDB.getInstance().getUsers().add(user);
    }

    public void removeUser(User user) {
        ArrayList<User> users = IMDB.getInstance().getUsers();
        ArrayList<Request> requests = IMDB.getInstance().getRequests();
        ArrayList<Production> productions = IMDB.getInstance().getProductions();
        ArrayList<Actor> actors = IMDB.getInstance().getActors();

        users.remove(user);

        Iterator<Request> iterator = requests.iterator();
        while (iterator.hasNext()) {
            Request request = iterator.next();
            if (request.getSubmitterUsername().equals(user.getUsername()))
                iterator.remove();
            else if (request.getSolverUsername().equals(user.getUsername())) {
                request.setSolverUsername("ADMIN");
                RequestsHolder.addRequest(request);
            }
        }

        for (User user1 : users) {
            if (user1 instanceof Regular)
                continue;
            ((Staff) user1).getRequests().removeIf(request -> request.getSubmitterUsername().equals(user.getUsername()));
        }

        RequestsHolder.getRequests().removeIf(request -> request.getSubmitterUsername().equals(user.getUsername()));

        for (Production production : productions) {
            production.getRatings().removeIf(rating -> rating.getUsername().equals(user.getUsername()));
            if (user instanceof Contributor)
                production.setContributor("ADMIN");
        }

        if (user instanceof Contributor)
            for (Actor actor : actors)
                actor.setContributor("ADMIN");
    }

    public static class RequestsHolder {
        private static final ArrayList<Request> requests = new ArrayList<>();

        public static void addRequest(Request request) {
            requests.add(request);
        }

        public static void removeRequest(Request request) {
            requests.remove(request);
        }

        public static ArrayList<Request> getRequests() {
            return requests;
        }

        public static void notifyAdmins(String notification) {
            for (User user : IMDB.getInstance().getUsers()) {
                if (user instanceof Admin)
                    user.update(notification);
            }
        }
    }
}
