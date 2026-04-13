package com.example.electroniccemetery.util;

import com.example.electroniccemetery.model.*;

public class SelectedContext {

    private static Cemetery currentCemetery;
    private static Section currentSection;
    private static Plot currentPlot;
    private static Grave currentGrave;

    // Кто ты и с какого кладбища, как вообще зовут
    private static String currentUserRole;
    private static Integer currentUserCemeteryId;
    private static String currentUserLogin;

    // Гетеры и сетеры
    public static Cemetery getCemetery() { return currentCemetery; }
    public static void setCemetery(Cemetery cemetery) { currentCemetery = cemetery; }

    public static Section getSection() { return currentSection; }
    public static void setSection(Section section) { currentSection = section; }

    public static Plot getPlot() { return currentPlot; }
    public static void setPlot(Plot plot) { currentPlot = plot; }

    public static Grave getGrave() { return currentGrave; }
    public static void setGrave(Grave grave) { currentGrave = grave; }

    public static String getCurrentUserRole() { return currentUserRole; }
    public static void setCurrentUserRole(String role) { currentUserRole = role; }

    public static Integer getCurrentUserCemeteryId() { return currentUserCemeteryId; }
    public static void setCurrentUserCemeteryId(Integer cemeteryId) { currentUserCemeteryId = cemeteryId; }

    public static String getCurrentUserLogin() { return currentUserLogin; }
    public static void setCurrentUserLogin(String login) { currentUserLogin = login; }

    // очистка контекста при выходе
    public static void clear() {
        currentCemetery = null;
        currentSection = null;
        currentPlot = null;
        currentGrave = null;
        currentUserRole = null;
        currentUserCemeteryId = null;
        currentUserLogin = null;
    }
}