package me.riseremi.utils;

import java.util.Random;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class NameGenerator {

    private static final Random rnd = new Random();
    private static final String[] names = {"File", "Edit", "View", "Run", "Debug",
        "Tools", "Window", "Help"};
    private static final String[] surnames = {"Navigate", "Source", "Refactor",
        "Profile", "Team", "Clean", "Build", "Save"};

    public static String getName() {
        return names[rnd.nextInt(names.length)] + " " + surnames[rnd.nextInt(surnames.length)];
    }

}
