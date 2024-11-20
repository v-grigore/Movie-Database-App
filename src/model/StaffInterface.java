package model;

public interface StaffInterface {
    void addProductionSystem(Production p);

    void addActorSystem(Actor a);

    void removeProductionSystem(Production production);

    void removeActorSystem(Actor actor);

    void updateProduction(Production oldProduction, Production newProduction);

    void updateActor(Actor oldActor, Actor newActor);

    void solveRequest(Request request);
}
