package com.benisontechnologies.ledcontrollerpanel;

import android.net.Uri;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Ashank on 7/14/2016.
 */
public class LED {

    int busVoltage;
    int maxPower;
    int currentPower;
    int extTemp;
    int boardTemp;
    int dimLevel;           //BALLAST PARAMS
    int fadeTime;
    int recovLightTime;
    int minLightLevel;
    int maxLightLevel;


    int hwRevision;
    int swRevision;
    int ledOnMinutes;
    int driverActTime;
    int minuties115c;
    int minuties110c;
    int minuties105c;       //CHANNEL PARAMS
    int minuties100c;
    int minuties95c;
    int minuties90c;
    int minuties85c;
    int minuties80c;

    boolean isLedOn;
    boolean isShortCircuit;
    boolean isOpenCircuit;
    boolean isOverTemperature;

    String ledName;
    String identifier;
    URI ledUri;
    ArrayList<Channel> channels = new ArrayList<Channel>();


    public LED(int busVoltage, int maxPower, int currentPower, int extTemp, int boardTemp, int dimLevel, int fadeTime, int recovLightTime, int minLightLevel, int maxLightLevel, String ledName) {
        this.busVoltage = busVoltage;
        this.maxPower = maxPower;
        this.currentPower = currentPower;
        this.extTemp = extTemp;
        this.boardTemp = boardTemp;
        this.dimLevel = dimLevel;
        this.fadeTime = fadeTime;
        this.recovLightTime = recovLightTime;
        this.minLightLevel = minLightLevel;
        this.maxLightLevel = maxLightLevel;
        this.ledName = ledName;
    }

    public LED(){

    }

    public int getBusVoltage() {
        return busVoltage;
    }

    public void setBusVoltage(int busVoltage) {
        this.busVoltage = busVoltage;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
    }

    public int getExtTemp() {
        return extTemp;
    }

    public void setExtTemp(int extTemp) {
        this.extTemp = extTemp;
    }

    public int getBoardTemp() {
        return boardTemp;
    }

    public void setBoardTemp(int boardTemp) {
        this.boardTemp = boardTemp;
    }

    public int getDimLevel() {
        return dimLevel;
    }

    public void setDimLevel(int dimLevel) {
        this.dimLevel = dimLevel;
    }

    public int getFadeTime() {
        return fadeTime;
    }

    public void setFadeTime(int fadeTime) {
        this.fadeTime = fadeTime;
    }

    public int getRecovLightTime() {
        return recovLightTime;
    }

    public void setRecovLightTime(int recovLightTime) {
        this.recovLightTime = recovLightTime;
    }

    public int getMinLightLevel() {
        return minLightLevel;
    }

    public void setMinLightLevel(int minLightLevel) {
        this.minLightLevel = minLightLevel;
    }

    public int getMaxLightLevel() {
        return maxLightLevel;
    }

    public void setMaxLightLevel(int maxLightLevel) {
        this.maxLightLevel = maxLightLevel;
    }

    public int getHwRevision() {
        return hwRevision;
    }

    public void setHwRevision(int hwRevision) {
        this.hwRevision = hwRevision;
    }

    public int getSwRevision() {
        return swRevision;
    }

    public void setSwRevision(int swRevision) {
        this.swRevision = swRevision;
    }

    public int getLedOnMinutes() {
        return ledOnMinutes;
    }

    public void setLedOnMinutes(int ledOnMinutes) {
        this.ledOnMinutes = ledOnMinutes;
    }

    public int getDriverActTime() {
        return driverActTime;
    }

    public void setDriverActTime(int driverActTime) {
        this.driverActTime = driverActTime;
    }

    public int getMinuties115c() {
        return minuties115c;
    }

    public void setMinuties115c(int minuties115c) {
        this.minuties115c = minuties115c;
    }

    public int getMinuties110c() {
        return minuties110c;
    }

    public void setMinuties110c(int minuties110c) {
        this.minuties110c = minuties110c;
    }

    public int getMinuties105c() {
        return minuties105c;
    }

    public void setMinuties105c(int minuties105c) {
        this.minuties105c = minuties105c;
    }

    public int getMinuties100c() {
        return minuties100c;
    }

    public void setMinuties100c(int minuties100c) {
        this.minuties100c = minuties100c;
    }

    public int getMinuties95c() {
        return minuties95c;
    }

    public void setMinuties95c(int minuties95c) {
        this.minuties95c = minuties95c;
    }

    public int getMinuties90c() {
        return minuties90c;
    }

    public void setMinuties90c(int minuties90c) {
        this.minuties90c = minuties90c;
    }

    public int getMinuties85c() {
        return minuties85c;
    }

    public void setMinuties85c(int minuties85c) {
        this.minuties85c = minuties85c;
    }

    public int getMinuties80c() {
        return minuties80c;
    }

    public void setMinuties80c(int minuties80c) {
        this.minuties80c = minuties80c;
    }

    public void setLedUri(URI ledUri) {
        this.ledUri = ledUri;
    }

    public URI getLedUri() {
        return ledUri;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    public String getLedName() {
        return ledName;
    }

    public void setLedName(String ledName) {
        this.ledName = ledName;
    }

    public boolean isLedOn() {
        return isLedOn;
    }

    public void setIsLedOn(boolean isLedOn) {
        this.isLedOn = isLedOn;
    }

    public boolean isShortCircuit() {
        return isShortCircuit;
    }

    public void setIsShortCircuit(boolean isShortCircuit) {
        this.isShortCircuit = isShortCircuit;
    }

    public boolean isOpenCircuit() {
        return isOpenCircuit;
    }

    public void setIsOpenCircuit(boolean isOpenCircuit) {
        this.isOpenCircuit = isOpenCircuit;
    }

    public boolean isOverTemperature() {
        return isOverTemperature;
    }

    public void setIsOverTemperature(boolean isOverTemperature) {
        this.isOverTemperature = isOverTemperature;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
