package com.benisontechnologies.ledcontrollerpanel;

import android.content.Context;

/**
 * Created by Ashank on 7/20/2016.
 */
public class AsyncParam {

    CoapGroup coapGroup;
    int ledPosition;
    byte[] command;
    Context context;
    int commandType;
    int changedValue;
    int channelPosition;


    public AsyncParam(CoapGroup coapGroup, int ledPosition) {
        this.coapGroup = coapGroup;
        this.ledPosition = ledPosition;
    }

    public AsyncParam(CoapGroup coapGroup, int ledPosition, int channelPosition) {
        this.coapGroup = coapGroup;
        this.ledPosition = ledPosition;
        this.channelPosition = channelPosition;
    }



    public AsyncParam(CoapGroup coapGroup, int ledPosition, byte[] command, Context context, int commandType) {
        this.coapGroup = coapGroup;
        this.ledPosition = ledPosition;
        this.command = command;
        this.context = context;
        this.commandType = commandType;
    }

    public AsyncParam(CoapGroup coapGroup, int ledPosition, byte[] command, Context context, int commandType, int changedValue) {
        this.coapGroup = coapGroup;
        this.ledPosition = ledPosition;
        this.command = command;
        this.context = context;
        this.commandType = commandType;
        this.changedValue = changedValue;
    }



    public CoapGroup getCoapGroup() {
        return coapGroup;
    }

    public void setCoapGroup(CoapGroup coapGroup) {
        this.coapGroup = coapGroup;
    }

    public int getLedPosition() {
        return ledPosition;
    }

    public void setLedPosition(int ledPosition) {
        this.ledPosition = ledPosition;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public byte[] getCommand() {
        return command;
    }

    public void setCommand(byte[] command) {
        this.command = command;
    }

    public void setCommandType(int commandType){
        this.commandType = commandType;
    }

    public int getCommandType(){
        return commandType;
    }

    public int getChangedValue() {
        return changedValue;
    }

    public void setChangedValue(int changedValue) {
        this.changedValue = changedValue;
    }

    public int getChannelPosition() {
        return channelPosition;
    }

    public void setChannelPosition(int channelPosition) {
        this.channelPosition = channelPosition;
    }
}
