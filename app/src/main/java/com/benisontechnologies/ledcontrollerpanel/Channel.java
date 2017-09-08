package com.benisontechnologies.ledcontrollerpanel;

import java.net.URI;

/**
 * Created by Ashank on 7/18/2016.
 */
public class Channel {

    int channelCurrentPower;
    int channelVoltage;
    int channelMaxPower;
    int channelPeakCurrent;
    int channelACOffset;
    String channelSubURI;




    public int getChannelCurrentPower() {
        return channelCurrentPower;
    }

    public void setChannelCurrentPower(int channelCurrentPower) {
        this.channelCurrentPower = channelCurrentPower;
    }

    public int getChannelVoltage() {
        return channelVoltage;
    }

    public void setChannelVoltage(int channelVoltage) {
        this.channelVoltage = channelVoltage;
    }

    public int getChannelMaxPower() {
        return channelMaxPower;
    }

    public void setChannelMaxPower(int channelMaxPower) {
        this.channelMaxPower = channelMaxPower;
    }

    public int getChannelPeakCurrent() {
        return channelPeakCurrent;
    }

    public void setChannelPeakCurrent(int channelPeakCurrent) {
        this.channelPeakCurrent = channelPeakCurrent;
    }

    public int getChannelACOffset() {
        return channelACOffset;
    }

    public void setChannelACOffset(int channelACOffset) {
        this.channelACOffset = channelACOffset;
    }

    public String getChannelSubURI() {
        return channelSubURI;
    }

    public void setChannelSubURI(String channelSubURI) {
        this.channelSubURI = channelSubURI;
    }
}
