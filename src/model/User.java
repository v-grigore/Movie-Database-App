package model;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

abstract public class User implements Observer{
    @Override
    public void update(String notification) {
        this.notifications.add(notification);
    }

    public static String generatePassword(int length) {
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String digitChars = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";

        String allChars = uppercaseChars + lowercaseChars + digitChars + specialChars;

        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allChars.length());
            char randomChar = allChars.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }

    public static class Information {
        private final Credentials credentials;
        private final String name;
        private final String country;
        private final int age;
        private final char gender;
        private final LocalDateTime birthdate;

        public Information(Builder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthdate = builder.birthdate;
        }

        public static class Builder {
            private final Credentials credentials;
            private final String name;
            private String country;
            private int age;
            private char gender;
            private LocalDateTime birthdate;

            public Builder(String name, String email, String password) {
                this.credentials = new Credentials(email, password);
                this.name = name;
            }

            public Builder setCountry(String country) {
                this.country = country;
                return this;
            }

            public Builder setAge(int age) {
                this.age = age;
                return this;
            }

            public Builder setGender(char gender) {
                this.gender = gender;
                return this;
            }

            public Builder setBirthdate(LocalDateTime birthdate) {
                this.birthdate = birthdate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }

        public String getName() {
            return name;
        }
    }

    private final Information information;
    private final AccountType accountType;
    private final String username;
    private int xp;
    private final ArrayList<String> notifications;
    private final TreeSet<ArtisticEntity> favorites;
    private ExperienceStrategy experienceStrategy;

    public void addToFavorites(ArtisticEntity object) {
        if (object != null)
            favorites.add(object);
    }

    public void removeFromFavorites(ArtisticEntity object) {
        if (object != null)
            favorites.remove(object);
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setExperienceStrategy(ExperienceStrategy experienceStrategy) {
        this.experienceStrategy = experienceStrategy;
    }

    public ExperienceStrategy getExperienceStrategy() {
        return experienceStrategy;
    }

    public void logout() {
        IMDB.getInstance().userLogout();
    }

    public boolean correctCredentials(String email, String password) {
        return this.information.credentials.email.equals(email) && this.information.credentials.password.equals(password);
    }

    public String getUsername() {
        return username;
    }

    public int getXp() {
        return xp;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public TreeSet<ArtisticEntity> getFavorites() {
        return favorites;
    }

    public String getName() {
        return information.name;
    }

    public String getCountry() {
        return information.country;
    }

    public int getAge() {
        return information.age;
    }

    public char getGender() {
        return information.gender;
    }

    public LocalDateTime getBirthdate() {
        return information.birthdate;
    }

    static class Credentials {
        private final String email;
        private final String password;

        Credentials(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public User(Information information, AccountType accountType, String username, int xp) {
        this.information = information;
        this.accountType = accountType;
        this.username = username;
        this.xp = xp;
        notifications = new ArrayList<>();
        favorites = new TreeSet<>();
    }

    public User(Information information, AccountType accountType, String username, int xp,
                TreeSet<ArtisticEntity> favorites, ArrayList<String> notifications) {
        this.information = information;
        this.accountType = accountType;
        this.username = username;
        this.xp = xp;
        this.notifications = notifications;
        this.favorites = favorites;
    }

    public void deleteNotification(String notification) {
        this.notifications.remove(notification);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = this.information.birthdate.format(dateFormatter);

        StringBuilder favorites = new StringBuilder();
        for (ArtisticEntity entity : this.favorites) {
            favorites.append("\n  ").append(entity.getName());
        }

        StringBuilder notifications = new StringBuilder();
        for (String notification : this.notifications) {
            notifications.append("\n  ").append(notification);
        }

        String xp = this.xp != -1 ? String.valueOf(this.xp) : "-";

        return "Username: " + this.username + "\nXp: " + xp + "\nInformation: {\n  Credentials: {\n    Email: "
                + this.information.credentials.email + "\n    Password: " + this.information.credentials.password
                + "\n  }\n  Name: " + this.information.name + "\n  Country: " + this.information.country + "\n  Age: "
                + this.information.age + "\n  Gender: " + this.information.gender + "\n  Birthdate: " + formattedDateTime
                + "\n}\nUserType: " + this.accountType + "\nNotifications: {" + notifications + "\n}" + "\nFavorites: {"
                + favorites + "\n}";
    }
}
