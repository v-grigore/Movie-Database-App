package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

public class Regular extends User implements RequestsManager{
    public void addRating(Rating rating, Production production) {
        String type = production instanceof Movie ? "movie" : "series";

        for (Rating rating1 : production.getRatings()) {
            rating1.notifyObservers("The " + type + " '" + production.getName() +
                    "' that you rated received a new rating from user " + rating.getUsername() + " : " + rating.getScore());
        }
        rating.addObserver(IMDB.getInstance().findUser(rating.getUsername()));
        production.addRating(rating);

        User contributor = IMDB.getInstance().findUser(production.getContributor());
        if (contributor != null)
            contributor.update("The " + type + " '" + production.getName() +
                    "' that you added received a new rating from user " + rating.getUsername() + " : " + rating.getScore());

        for (User user : IMDB.getInstance().getUsers()) {
            if (user.getFavorites().contains(production))
                user.update("The " + type + " '" + production.getName() +
                        "' from your favorites received a new rating from user " + rating.getUsername() + " : " + rating.getScore());
        }

        setExperienceStrategy(new RatingExperience());
        setXp(getXp() + getExperienceStrategy().calculateExperience());
    }

    @Override
    public void createRequest(Request request) {
        request.addObserver(this);

        request.setSubmissionDate(LocalDateTime.now());
        request.setSubmitterUsername(this.getUsername());

        Staff solver = IMDB.getInstance().findSolver(request);
        if (solver != null) {
            request.setSolverUsername(solver.getUsername());
            solver.addRequest(request);
            solver.update("You received a new request: " + request.getDescription());
        }
        else {
            request.setSolverUsername("ADMIN");
            Admin.RequestsHolder.addRequest(request);
            Admin.RequestsHolder.notifyAdmins("You received a new request: " + request.getDescription());
        }

        IMDB.getInstance().addRequest(request);
    }

    @Override
    public void removeRequest(Request request) {
        IMDB.getInstance().removeRequest(request);

        if (request.getSolverUsername().equals("ADMIN")) {
            Admin.RequestsHolder.removeRequest(request);
            return;
        }

        Staff solver = (Staff) IMDB.getInstance().findUser(request.getSolverUsername());

        solver.clearRequest(request);
    }

    public Regular(Information information, AccountType accountType, String username, int xp) {
        super(information, accountType, username, xp);
    }

    public Regular(Information information, AccountType accountType, String username, int xp,
                   TreeSet<ArtisticEntity> favorites, ArrayList<String> notifications) {
        super(information, accountType, username, xp, favorites, notifications);
    }
}
