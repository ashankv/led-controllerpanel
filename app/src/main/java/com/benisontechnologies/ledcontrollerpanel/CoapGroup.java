package com.benisontechnologies.ledcontrollerpanel;

import java.util.ArrayList;

/**
 * Created by Ashank on 7/18/2016.
 */
public class CoapGroup {

    private ArrayList<LED> leds = new ArrayList<LED>();
    private String ipAddress;
    private String coapGroupUrl;
    private boolean isGroupOfLeds;
    private int numberOfLeds;

    public CoapGroup(ArrayList<LED> leds, String ipAddress, String coapGroupUrl) {
        this.leds = leds;
        this.ipAddress = ipAddress;
        this.coapGroupUrl = coapGroupUrl;
    }

    public CoapGroup(){

    }

    public ArrayList<LED> getLeds() {
        return leds;
    }

    public String getCoapGroupUrl() {
        return coapGroupUrl;
    }

    public void setCoapGroupUrl(String coapGroupUrl) {
        this.coapGroupUrl = coapGroupUrl;
    }

    public void setLeds(ArrayList<LED> leds) {
        this.leds = leds;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isGroupOfLeds() {
        return isGroupOfLeds;
    }

    public void setIsGroupOfLeds(boolean isGroupOfLeds) {
        this.isGroupOfLeds = isGroupOfLeds;
    }

    public int getNumberOfLeds() {
        return numberOfLeds;
    }

    public void setNumberOfLeds(int numberOfLeds) {
        this.numberOfLeds = numberOfLeds;
    }
}
