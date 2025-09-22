package com.example.p1aloritmos.menu;

public class GameSettings {
    // Paths por defecto
    private static String backgroundPath = "/com/example/p1aloritmos/individuals/background_2.png";
    private static String cardBackPath   = "/com/example/p1aloritmos/individuals/cardBack/card_back.png";

    // Getters y setters
    public static String getBackgroundPath() {
        return backgroundPath;
    }

    public static void setBackgroundPath(String path) {
        backgroundPath = path;
    }

    public static String getCardBackPath() {
        return cardBackPath;
    }

    public static void setCardBackPath(String path) {
        cardBackPath = path;
    }
}
