package model;

public class Episode {
    private final String name;
    private final int length;

    public Episode(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "    {\n      Episode name: " + name + "\n      Duration: " + length + "\n    }";
    }
}
