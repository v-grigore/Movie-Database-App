package model;

public class RequestExperience implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 10;
    }
}
