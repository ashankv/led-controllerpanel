package com.benisontechnologies.ledcontrollerpanel;

import java.util.ArrayList;

/**
 * Created by Ashank on 7/18/2016.
 */
public class AppConfigurations {

    public static final String BALLAST_PARAMS_URI = "/led/ballastparams";
    public static final String CHANNEL_PARAMS_URI = "/led/channel";
    public static final String STATUS_PARAMS_URI = "/led/status";
    public static final String COMMAND_URI = "/led/cmd";
    public static final String SETTINGS_URI = "/led/settings";
    public static final String DEVICE_COUNT_URI = "/light/devicecount";

    public static final int TURN_ON_CMD = 1;
    public static final int TURN_OFF_CMD = 2;
    public static final int GO_MAX_CMD = 3;
    public static final int GO_MIN_CMD = 4;
    public static final int STEP_UP_CMD = 5;
    public static final int STEP_DOWN_CMD = 6;

    public static final int DIM_SETTING  = 1;
    public static final int FADE_TIME_SETTING = 2;
    public static final int RECOV_TIME_SETTING = 3;
    public static final int MIN_LIGHT_SETTING = 4;
    public static final int MAX_LIGHT_SETTING = 5;


    public static int numberOfChannels = 4;






}
