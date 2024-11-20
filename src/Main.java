import gui.LoginForm;
import model.IMDB;
import model.Mode;
import utils.CLI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        IMDB.getInstance().run(Mode.GUI);
    }
}