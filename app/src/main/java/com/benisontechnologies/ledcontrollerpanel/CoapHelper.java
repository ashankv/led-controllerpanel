package com.benisontechnologies.ledcontrollerpanel;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;


import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Ashank on 7/18/2016.
 */
public class CoapHelper {

    private static CoapClient client;
    private static CoapResponse response;
    public static ArrayList<CoapGroup> coapGroups = new ArrayList<CoapGroup>();
    public static int currCoapGrpSelPos = 0;      //currentCoapGroupSelectedPosition in spinner
    public static int currLedSelPos = 0;          //currentLedSelectedPosition in spinner
    public static int currChannelSelPos = 0;      //currentChannelSelectedPosition in spinner


    //Getting Ballast Parameters From CoAP Server
    public static void getBallastParams(CoapGroup coapGroup, int ledPosition){  //position is the element from dropdown in group, by default it is 0(spinner)

        LED led = coapGroup.getLeds().get(ledPosition);

        URI ballastURI = null;
        try {
            ballastURI = new URI(led.getLedUri().toString() + AppConfigurations.BALLAST_PARAMS_URI);
        }catch(Exception e){
            e.printStackTrace();
        }

        client = new CoapClient(ballastURI);
        response = client.get();
        byte[] payload = response.getPayload();
        parseAndUpdateBallastParams(led, payload);


    }

    public static void parseAndUpdateBallastParams(LED led, byte[] payload){

        //STATS PAYLOAD FIELD

        if(new Byte(payload[0]).toString() == "1"){                                                 //LAMP  ON
            led.setIsLedOn(true);
        } else{
            led.setIsLedOn(false);
        }

        if(new Byte(payload[1]).toString() == "1"){                                                 //SHORT CIRCUIT
            led.setIsShortCircuit(true);
        } else{
            led.setIsShortCircuit(false);
        }

        if(new Byte(payload[2]).toString() == "1"){                                                 // OPEN  CIRCUIT
            led.setIsOpenCircuit(true);
        } else{
            led.setIsOpenCircuit(false);
        }

        if(new Byte(payload[3]).toString() == "1"){                                                 //OVER TEMPERATURE
            led.setIsOverTemperature(true);
        } else{
            led.setIsOverTemperature(false);
        }

        //MEASUREMENTS PAYLOAD FIELD

        byte[] array1 = {payload[4], payload[5], payload[6], payload[7]};                           //BUS VOLTAGE
        led.setBusVoltage(ByteBuffer.wrap(array1).getInt());

        byte[] array2 = {payload[8], payload[9], payload[10], payload[11]};                         //EXTERNAL TEMPERATURE
        led.setExtTemp(ByteBuffer.wrap(array2).getInt());

        byte[] array3 = {payload[12], payload[13], payload[14], payload[15]};                       //MCU TEMPERATURE
        led.setBoardTemp(ByteBuffer.wrap(array3).getInt());

        byte[] array4 = {payload[16], payload[17], payload[18], payload[19]};                       //CURRENT POWER
        led.setCurrentPower(ByteBuffer.wrap(array4).getInt());

        byte[] array5 = {payload[20], payload[21], payload[22], payload[23]};                       //TOTAL POWER
        led.setMaxPower(ByteBuffer.wrap(array5).getInt());

        //SETTINGS PAYLOAD FIELD

        byte[] array6 = {payload[24], payload[25], payload[26], payload[27]};                       //DIMMING
        led.setDimLevel(ByteBuffer.wrap(array6).getInt());

        byte[] array7 = {payload[28], payload[29], payload[30], payload[31]};                       //FADE TIME
        led.setFadeTime(ByteBuffer.wrap(array7).getInt());

        byte[] array8 = {payload[32], payload[33], payload[34], payload[35]};                       //RECOVERY LIGHT LEVEL
        led.setRecovLightTime(ByteBuffer.wrap(array8).getInt());

        byte[] array9 = {payload[36], payload[37], payload[38], payload[39]};                       //MAXIMUM LIGHT LEVEL
        led.setMaxLightLevel(ByteBuffer.wrap(array9).getInt());

        byte[] array10 = {payload[40], payload[41], payload[42], payload[43]};                      //MINIMUM LIGHT LEVEL
        led.setMinLightLevel(ByteBuffer.wrap(array10).getInt());


    }

    public static boolean putCommandBallast(CoapGroup coapGroup, int ledPosition, byte[] command, Context context, int commandType){

        LED led = coapGroup.getLeds().get(ledPosition);

        String finalSuccessMessage = null;
        String finalFailureMessage = null;

        switch(commandType){
            case 1:
                finalSuccessMessage = "LED Was Turned On Successfully";
                finalFailureMessage = "Failure In Turning On LED";
            case 2:
                finalSuccessMessage = "LED Was Turned Off Succesfully";
                finalFailureMessage = "Failure In Turning Off LED";
            case 3:
                finalSuccessMessage = "LED Is At Max";
                finalFailureMessage = "Failure In Setting LED to Max";
            case 4:
                finalSuccessMessage = "LED Is At Minimum";
                finalFailureMessage = "Failure In Setting LED to Min";
            case 5:
                finalSuccessMessage = "LED Stepped Up Succesfully";
                finalFailureMessage = "Failure In Stepping Up LED";
            case 6:
                finalSuccessMessage = "LED Stepped Down Successfully";
                finalFailureMessage = "Failure In Stepping Down LED";
        }

        URI commandURI = null;
        try{
            commandURI = new URI(led.getLedUri().toString() + AppConfigurations.COMMAND_URI);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context, finalFailureMessage, Toast.LENGTH_LONG).show();
            return false;
        }

        client = new CoapClient(commandURI);
        response = client.put(command, 0);
        if(response.isSuccess()){
            Toast.makeText(context, finalSuccessMessage, Toast.LENGTH_LONG).show();
            return true;
        }else{
            Toast.makeText(context, finalFailureMessage, Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public static boolean putSettingsBallast(CoapGroup coapGroup, int ledPosition, byte[] command, Context context, int commandType, int changedValue){

        String finalSuccessMessage = null;
        String finalFailureMessage = null;

        switch(commandType){
            case 1:
                finalSuccessMessage = "Dimming Level Changed Successfully";
                finalFailureMessage = "Failure In Changing Dimming Level";
            case 2:
                finalSuccessMessage = "Fade Time Changed Successfully";
                finalFailureMessage = "Failure In Changing Fade Time";
            case 3:
                finalSuccessMessage = "Recovery Light Time Changed Successfully";
                finalFailureMessage = "Failure In Changing Recovery Light Time";
            case 4:
                finalSuccessMessage = "Minimum Light Level CHanged Successfully";
                finalFailureMessage = "Failure In Changing Minimum Light Level";
            case 5:
                finalSuccessMessage = "Maximum Light Level Changed Successfully";
                finalFailureMessage = "Failure In Changing Maximum Light Level";
        }

        LED led = coapGroup.getLeds().get(ledPosition);

        URI settingsURI = null;
        try{
            settingsURI = new URI(led.getLedUri().toString() + AppConfigurations.SETTINGS_URI);
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context, finalFailureMessage, Toast.LENGTH_LONG).show();
            return false;
        }

        client = new CoapClient(settingsURI);
        response = client.put(command, 0);
        if(response.isSuccess()){
            Toast.makeText(context, finalSuccessMessage, Toast.LENGTH_LONG).show();
            return true;
        }else{
            Toast.makeText(context, finalFailureMessage, Toast.LENGTH_LONG).show();
            return false;
        }

    }

    //Getting Status Parameters From CoAP Server
    public static void getStatusParams(CoapGroup coapGroup, int position){

        LED led = coapGroup.getLeds().get(position);

        URI statusURI = null;
        try{
            statusURI = new URI(led.getLedUri().toString() + AppConfigurations.STATUS_PARAMS_URI);

        } catch(Exception e){
            e.printStackTrace();
        }

        client = new CoapClient(statusURI);
        response = client.get();
        byte[] payload = response.getPayload();
        //parseAndUpdateStatusParams(led, payload);



    }

    public static void parseAndUpdateStatusParams(LED led, byte[] payload){

        byte[] array1 = {payload[0], payload[1], payload[2], payload[3]};                           //HW REVISION
        led.setHwRevision(ByteBuffer.wrap(array1).getInt());

        byte[] array2 = {payload[4], payload[5], payload[6], payload[7]};                           //SW REVISION
        led.setSwRevision(ByteBuffer.wrap(array1).getInt());

        byte[] array3 = {payload[8], payload[9], payload[10], payload[11]};                         //LED ON MINUTES
        led.setLedOnMinutes(ByteBuffer.wrap(array1).getInt());

        byte[] array4 = {payload[12], payload[13], payload[14], payload[15]};                       //DRIVER ACT TIME
        led.setDriverActTime(ByteBuffer.wrap(array1).getInt());

        byte[] array5 = {payload[16], payload[17], payload[18], payload[19]};                       //MINUTIES ABOVE 115c
        led.setMinuties115c(ByteBuffer.wrap(array1).getInt());

        byte[] array6 = {payload[20], payload[21], payload[22], payload[23]};                       //MINUTIES ABOVE 110c
        led.setMinuties110c(ByteBuffer.wrap(array1).getInt());

        byte[] array7 = {payload[24], payload[25], payload[26], payload[27]};                       //MINUTIES ABOVE 105c
        led.setMinuties105c(ByteBuffer.wrap(array1).getInt());

        byte[] array8 = {payload[28], payload[29], payload[30], payload[31]};                       //MINUTIES ABOVE 100c
        led.setMinuties100c(ByteBuffer.wrap(array1).getInt());

        byte[] array9 = {payload[32], payload[33], payload[34], payload[35]};                       //MINUTIES ABOVE 95c
        led.setMinuties95c(ByteBuffer.wrap(array1).getInt());

        byte[] array10 = {payload[36], payload[37], payload[38], payload[39]};                      //MINUTIES ABOVE 90c
        led.setMinuties90c(ByteBuffer.wrap(array1).getInt());

        byte[] array11 = {payload[40], payload[41], payload[42], payload[43]};                      //MINUTIES ABOVE 85c
        led.setMinuties85c(ByteBuffer.wrap(array1).getInt());

        byte[] array12 = {payload[44], payload[45], payload[46], payload[47]};                      //MINUTIES ABOVE 80c
        led.setMinuties80c(ByteBuffer.wrap(array1).getInt());
    }



    //Getting Channel Parameters From CoAP Server
    public static void getChannelParams(CoapGroup coapGroup, int ledPosition, int channelNumber){

        LED led = coapGroup.getLeds().get(ledPosition);
        URI channelURI = null;
        try{
            channelURI = new URI(led.getLedUri().toString() + led.getChannels().get(channelNumber).getChannelSubURI());
        }catch(Exception e){
            e.printStackTrace();
        }

        client = new CoapClient(channelURI);
        response = client.get();
        byte[] payload = response.getPayload();
        parseAndUpdateChannelParams(led, payload, channelNumber);

    }

    public static void parseAndUpdateChannelParams(LED led, byte[] payload, int channelNumber){

        byte[] array1 = {payload[0], payload[1], payload[2], payload[3]};                           //BUS VOLTAGE
        led.getChannels().get(channelNumber).setChannelVoltage((ByteBuffer.wrap(array1).getInt()));

        byte[] array2 = {payload[4], payload[5], payload[6], payload[7]};                           //PEAK CURRENT
        led.getChannels().get(channelNumber).setChannelPeakCurrent((ByteBuffer.wrap(array1).getInt()));

        byte[] array3 = {payload[8], payload[9], payload[10], payload[11]};                         //PEAK AC OFFSET
        led.getChannels().get(channelNumber).setChannelACOffset((ByteBuffer.wrap(array1).getInt()));

        byte[] array4 = {payload[12], payload[13], payload[14], payload[15]};                       //TOTAL POWER
        led.getChannels().get(channelNumber).setChannelMaxPower((ByteBuffer.wrap(array1).getInt()));

        byte[] array5 = {payload[16], payload[17], payload[18], payload[19]};                       //CURRENT POWER
        led.getChannels().get(channelNumber).setChannelCurrentPower((ByteBuffer.wrap(array1).getInt()));
    }

}
