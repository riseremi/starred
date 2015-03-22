package me.riseremi.utils;

import java.util.Random;

/**
 *
 * @author Remi Weiss <riseremi at icloud.com>
 */
public class NameGenerator {

    private static final Random rnd = new Random();
    private static final String[] names = {"Timmy", "Jimmy", "Oak", "Remi", "Tree",
        "Rise", "Shutker", "Shuttgard", "Town of", "You Are", "kekker", "The Debil Wears",
        "system", "dot", "ebax", "Now I"};
    private static final String[] surnames = {" Parker", " Johnson", ".png", ".jpg",
        ".crx", ".net", " Fag", " Prada", " Red", " See"};

    public static String getName() {
        return names[rnd.nextInt(names.length)]
                + surnames[rnd.nextInt(surnames.length)];
    }

}
