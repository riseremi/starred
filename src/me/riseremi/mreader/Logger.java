package me.riseremi.mreader;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
class Logger {

    protected static void debug(String message, boolean debug) {
        if (debug) {
            System.out.println("[XMLMapReader] " + message);
        }
    }

}
