package model;

import java.util.ArrayList;
import java.util.TreeSet;

public abstract class Staff extends User implements StaffInterface{
    private final ArrayList<Request> requests;
    private final TreeSet<ArtisticEntity> contributions;

    @Override
    public void addProductionSystem(Production p) {
        contributions.add(p);
        IMDB.getInstance().addProduction(p);
    }

    @Override
    public void addActorSystem(Actor a) {
        contributions.add(a);
        IMDB.getInstance().addActor(a);
    }

    @Override
    public void removeProductionSystem(Production production) {
        IMDB.getInstance().removeProduction(production);
        contributions.remove(production);
    }

    @Override
    public void removeActorSystem(Actor actor) {
        IMDB.getInstance().removeActor(actor);
        contributions.remove(actor);
    }

    @Override
    public void updateProduction(Production oldProduction, Production newProduction) {
        newProduction.setId(oldProduction.getId());

        for (User user : IMDB.getInstance().getUsers()) {
            if (user.getFavorites().contains(oldProduction)) {
                user.removeFromFavorites(oldProduction);
                user.addToFavorites(newProduction);
            }
        }

        removeProductionSystem(oldProduction);
        for (Actor actor : IMDB.getInstance().getActors())
            actor.removePerformance(oldProduction.getName());
        if (newProduction.getContributor().equals("ADMIN"))
            IMDB.getInstance().addProduction(newProduction);
        else
            this.addProductionSystem(newProduction);

    }

    @Override
    public void updateActor(Actor oldActor, Actor newActor) {
        newActor.setId(oldActor.getId());

        for (User user : IMDB.getInstance().getUsers()) {
            if (user.getFavorites().contains(oldActor)) {
                user.removeFromFavorites(oldActor);
                user.addToFavorites(newActor);
            }
        }

        removeActorSystem(oldActor);
        for (Production production : IMDB.getInstance().getProductions()) {
            production.removeActor(oldActor.getName());
        }
        if (newActor.getContributor().equals("ADMIN"))
            IMDB.getInstance().addActor(newActor);
        else
            this.addActorSystem(newActor);
    }

    @Override
    public void solveRequest(Request request) {
        if (request.getSolverUsername().equals("ADMIN"))
            Admin.RequestsHolder.removeRequest(request);
        requests.remove(request);
        IMDB.getInstance().removeRequest(request);
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public TreeSet<ArtisticEntity> getContributions() {
        return contributions;
    }

    public ArtisticEntity findContribution(String name) {
        for (ArtisticEntity entity : contributions) {
            if (entity.getName().equalsIgnoreCase(name)) {
                return entity;
            }
        }
        return null;
    }

    public void addRequest(Request request) {
        this.requests.add(request);
    }

    public void clearRequest(Request request) {
        this.requests.remove(request);
    }

    public Staff(Information information, AccountType accountType, String username, int xp) {
        super(information, accountType, username, xp);
        requests = new ArrayList<>();
        contributions = new TreeSet<>();
    }

    public Staff(Information information, AccountType accountType, String username, int xp,
                 TreeSet<ArtisticEntity> favorites, TreeSet<ArtisticEntity> contributions,
                 ArrayList<String> notifications) {
        super(information, accountType, username, xp, favorites, notifications);
        requests = new ArrayList<>();
        this.contributions = contributions;
    }

    @Override
    public String toString() {
        StringBuilder contributions = new StringBuilder();
        for (ArtisticEntity entity : this.contributions) {
            contributions.append("\n  ").append(entity.getName());
        }

        return super.toString() + "\nContributions: {" + contributions + "\n}";
    }
}
