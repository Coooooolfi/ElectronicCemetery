package com.example.electroniccemetery.util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    private static final Logger LOGGER = Logger.getLogger("ElectronicCemetery");
    private static boolean initialized = false;

    private static synchronized Logger init() {
        if (!initialized) {
            try {
                new java.io.File("logs").mkdirs();
                FileHandler fh = new FileHandler("logs/app.log", 5_000_000, 3, true);
                fh.setFormatter(new SimpleFormatter());
                fh.setLevel(Level.INFO);
                LOGGER.addHandler(fh);
                LOGGER.setLevel(Level.INFO);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initialized = true;
        }
        return LOGGER;
    }

    public static void info(String msg)    { init().info(withUser(msg)); }
    public static void warning(String msg) { init().warning(withUser(msg)); }
    public static void error(String msg, Throwable t) { init().log(Level.SEVERE, withUser(msg), t); }

    private static String withUser(String msg) {
        String user;
        try {
            user = SelectedContext.getCurrentUserLogin();
            if (user == null) user = "visitor";
        } catch (Exception e) {
            user = "unknown";
        }
        return "[" + user + "] " + msg;
    }
}