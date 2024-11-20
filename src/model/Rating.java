package model;

public class Rating extends Subject{
    private final String username;
    private final int score;
    private final String comments;

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

    public Rating(String username, int score, String comments) {
        this.username = username;
        this.score = score;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "  {\n    Username: " + this.username + "\n    Rating: " + this.score + "\n    Comment: " +
                this.comments + "\n  }";
    }
}
