package com.benisontechnologies.ledcontrollerpanel;

import java.util.HashMap;

/**
 * Created by Ashank on 7/14/2016.
 */
public class Dali {

    public static HashMap<String, String> daliCommands;

    public static void setDaliCommands(){
        daliCommands.put("OFF", "00000000");
        daliCommands.put("STEP UP", "00000011");
        daliCommands.put("STEP DOWN", "00000100");
        daliCommands.put("ON", "00001000");
    }
}
