package com.toros.config;
import com.toros.core.Status;

import java.awt.*;


public class Config {
    public static final int BOX_SIZE = 10;
    public static final int VERTICAL_BOXES = 60;
    public static final int HORIZONTAL_BOXES = 60;
    public static final int FIELD_WIDTH = HORIZONTAL_BOXES * BOX_SIZE;
    public static final int FIELD_HEIGHT = VERTICAL_BOXES * BOX_SIZE;
    public static final int SLEEPMS = 10;
    public static final int TOOLBAR_WIDTH = 250;
    public static final int FRAME_WIDTH = FIELD_WIDTH + TOOLBAR_WIDTH;
    public static final int FRAME_HEIGHT = FIELD_HEIGHT;
    public static final Color TOOLBAR_COLOR = Color.DARK_GRAY;
    public static final Color TEXT_COLOR = Color.white;

    public static Color getColor(Status status){

        switch (status){
            case NONE: return Color.BLACK;
            case BORN: return Color.GRAY;
            case LIVE: return Color.WHITE;
            case DIED: return Color.DARK_GRAY;
            default: return Color.BLACK;
        }
    }
}
