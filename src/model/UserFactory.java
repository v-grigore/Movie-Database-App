package model;

import java.util.ArrayList;
import java.util.TreeSet;

public class UserFactory {
    public static User createUser(User.Information information, AccountType accountType, String username, int xp) {
        switch (accountType) {
            case Regular -> {
                return new Regular(information, accountType, username, xp);
            }
            case Contributor -> {
                return new Contributor(information, accountType, username, xp);
            }
            case Admin -> {
                return new Admin(information, accountType, username, xp);
            }
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        }
    }

    public static User createUser(User.Information information, AccountType accountType, String username, int xp,
                                  TreeSet<ArtisticEntity> favorites, TreeSet<ArtisticEntity> contributions,
                                  ArrayList<String> notifications) {
        switch (accountType) {
            case Regular -> {
                return new Regular(information, accountType, username, xp, favorites, notifications);
            }
            case Contributor -> {
                return new Contributor(information, accountType, username, xp, favorites, contributions, notifications);
            }
            case Admin -> {
                return new Admin(information, accountType, username, xp, favorites, contributions, notifications);
            }
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        }
    }
}
