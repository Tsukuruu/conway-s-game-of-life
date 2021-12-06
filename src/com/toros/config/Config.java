package com.toros.config;

import com.toros.core.Status;

import java.awt.*;
import java.util.prefs.Preferences;


public class Config {
    public static final int BOX_SIZE = 10;
    public static final int VERTICAL_BOXES = 60;
    public static final int HORIZONTAL_BOXES = 60;
    public static final int FIELD_WIDTH = HORIZONTAL_BOXES * BOX_SIZE;
    public static final int FIELD_HEIGHT = VERTICAL_BOXES * BOX_SIZE;
    public static final int TOOLBAR_WIDTH = 300;
    public static final int FRAME_WIDTH = FIELD_WIDTH + TOOLBAR_WIDTH;
    public static final int FRAME_HEIGHT = FIELD_HEIGHT;
    public static final Color TOOLBAR_COLOR = Color.DARK_GRAY;
    public static final Color TEXT_COLOR = Color.white;

    public static Color NONE_COLOR = Color.BLACK;
    public static Color BORN_COLOR = Color.GREEN;
    public static Color LIVE_COLOR = Color.WHITE;
    public static Color DIED_COLOR = new Color(242, 58, 58);

    public static int SLEEPMS = 10;
    public static double LIVE_CELL_CHANCE = 0.5f;
    public static final Preferences userPref = Preferences.userRoot().node("game-of-life-by-toros-pref");

    public static Color getColor(Status status) {

        switch (status) {
            case NONE:
                return NONE_COLOR;
            case BORN:
                return BORN_COLOR;
            case LIVE:
                return LIVE_COLOR;
            case DIED:
                return DIED_COLOR;
            default:
                return Color.BLACK;
        }
    }
}
