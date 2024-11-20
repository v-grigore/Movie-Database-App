package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

public class Contributor extends Staff implements RequestsManager{
    @Override
    public void createRequest(Request request) {
        request.setSubmissionDate(LocalDateTime.now());
        request.setSubmitterUsername(this.getUsername());

        Staff solver = IMDB.getInstance().findSolver(request);

        if (solver != null && solver.getUsername().equals(this.getUsername())) {
            System.err.println("Invalid request: cannot make request for your contribution");
            System.err.flush();
            return;
        }

        request.addObserver(this);

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

    public Contributor(Information information, AccountType accountType, String username, int xp) {
        super(information, accountType, username, xp);
    }

    public Contributor(Information information, AccountType accountType, String username, int xp,
                       TreeSet<ArtisticEntity> favorites, TreeSet<ArtisticEntity> contributions,
                       ArrayList<String> notifications) {
        super(information, accountType, username, xp, favorites, contributions, notifications);
    }
}
