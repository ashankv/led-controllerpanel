package com.benisontechnologies.ledcontrollerpanel;

import android.os.StrictMode;
import android.util.Log;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Ashank on 8/7/2016.
 */
public class DiscoveryHelper {

    private static CoapClient client;
    private static CoapResponse response;

    public static void sendMulticastRequest(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        String msg = "Hello";

        short[] hexArray = {0x42,0x01,0x61,0x25,0x15,0xF5,0xbb,
                0x2e,0x77,0x65,0x6c,0x6c,0x2d,0x6b,0x6e,0x6f,
                0x77,0x6e,0x04,0x63,0x6f,0x72,0x65,0x05,0x6c,
                0x69,0x67,0x68,0x74,0x11,0x2a,0xb2,0x01,0x62};
        byte[] hexValues = new byte[hexArray.length];
        for(int i = 0; i < hexValues.length; i++){
            hexValues[i] = (byte) hexArray[i];
        }

        try{
            InetAddress group = InetAddress.getByName("224.0.0.1");
            MulticastSocket mSocket = new MulticastSocket(5683);
            mSocket.joinGroup(group);
            DatagramPacket mPacket = new DatagramPacket(hexValues, hexValues.length, group, 5683);
            //mPacket.setData(hexValues);
            mSocket.send(mPacket);
            byte[] buf = new byte[1000];
            DatagramPacket respPacket = new DatagramPacket(buf, buf.length);

            while(true){

                mSocket.receive(respPacket);                                                        //Do We Need To Add 1-2 Second Delay To Ensure We Get All Responses?

                if(respPacket == null){
                    Log.d("DISCOVERY", "No More Devices To Be Found");
                    break;
                }

                String tempIpAddress = respPacket.getAddress().toString();


                boolean doesDeviceExist = false;

                for(CoapGroup coapGroup : CoapHelper.coapGroups){                                   //Check If It Is Duplicate Response
                    if(tempIpAddress == coapGroup.getIpAddress()){

                        doesDeviceExist = true;
                        break;
                    }
                }

                if(doesDeviceExist  == false) {

                    String tempCoapUrl = "coap://" + tempIpAddress + ":5683";
                    CoapGroup tempCoapGroup = new CoapGroup();
                    tempCoapGroup.setIpAddress(tempIpAddress);
                    tempCoapGroup.setCoapGroupUrl(tempCoapUrl);

                    CoapHelper.coapGroups.add(tempCoapGroup);
                }
            }

        }catch(Exception e){
            e.printStackTrace();

        }
    }

    public static void getLedIdentifiers(){

        for(CoapGroup coapGroup : CoapHelper.coapGroups) {

            URI deviceCountURI = null;
            try {
                deviceCountURI = new URI(coapGroup.getCoapGroupUrl() + AppConfigurations.DEVICE_COUNT_URI);
            } catch (Exception e) {
                e.printStackTrace();
            }

            client = new CoapClient(deviceCountURI);
            response = client.get();
            byte[] payload = response.getPayload();

            ArrayList<Integer> deviceCountPayload = new ArrayList<Integer>();

            for(byte a : payload){
                byte[] tempArray = {a};
                ByteBuffer wrapped = ByteBuffer.wrap(tempArray); // big-endian by default
                int convertedInt = wrapped.getInt();
                Integer convertedInteger = new Integer(convertedInt);
                deviceCountPayload.add(convertedInteger);
            }

            ArrayList<LED> ledArrayList = new ArrayList<LED>();
            int numberOfDevices = deviceCountPayload.get(0);

            coapGroup.setNumberOfLeds(numberOfDevices);

            if(numberOfDevices > 1){

                coapGroup.setIsGroupOfLeds(true);

                for(int i = 1; i<numberOfDevices; i++){                                             //Identifier Starts on 2nd Integer

                    String tempIdentifier = Integer.toString(deviceCountPayload.get(i));
                    LED tempLed = new LED();
                    tempLed.setIdentifier(tempIdentifier);

                    URI tempURI = null;
                    try{
                        tempURI = new URI(coapGroup.getCoapGroupUrl() + "?" + tempIdentifier);
                    }catch(Exception e){

                    }

                    tempLed.setLedUri(tempURI);
                    ledArrayList.add(tempLed);

                }
            } else if(numberOfDevices == 1){
                coapGroup.setIsGroupOfLeds(false);
                LED led = new LED();
                String tempIdentifier = Integer.toString(deviceCountPayload.get(1));

                led.setIdentifier(tempIdentifier);

                URI tempURI = null;
                try{
                    tempURI = new URI(coapGroup.getCoapGroupUrl() + "?" + tempIdentifier);
                }catch(Exception e){

                }

                led.setLedUri(tempURI);
                ledArrayList.add(led);
            }  else{
                Log.d("DISCOVERY", "Invalid Value of Number of Devices");
            }

            ArrayList<Channel> channelArrayList = new ArrayList<Channel>();



            for(int i = 0; i<AppConfigurations.numberOfChannels; i++){
                Channel newChannel = new Channel();

                for(int x = 0; i<AppConfigurations.numberOfChannels; x++){                          ///Set Channel URI (/led/channel0, etc)

                    newChannel.setChannelSubURI(AppConfigurations.CHANNEL_PARAMS_URI + Integer.toString(x));
                }

                channelArrayList.add(newChannel);
            }






            for(LED led : ledArrayList){
                led.setChannels(channelArrayList);
            }

            coapGroup.setLeds(ledArrayList);



        }
    }
}
