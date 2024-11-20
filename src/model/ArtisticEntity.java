package model;

public abstract class ArtisticEntity implements Comparable<ArtisticEntity>{
    private String contributor;
    private int id;

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getContributor() {
        return contributor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract int calculateRelevance(String searchQuery);

    public abstract String getName();
}
