package com.benisontechnologies.ledcontrollerpanel;

import android.os.AsyncTask;

/**
 * Created by Ashank on 7/25/2016.
 */
public class ButtonCmdTask extends AsyncTask<AsyncParam, Void, LED> {
    @Override
    protected LED doInBackground(AsyncParam... params) {

        AsyncParam param =  params[0];
        LED updatedLED = param.getCoapGroup().getLeds().get(param.getLedPosition());
        boolean success = CoapHelper.putCommandBallast(param.getCoapGroup(), param.getLedPosition(), param.getCommand(),param.getContext(), param.getCommandType());
        if(success){
            if(param.getCommandType() == 1){
                updatedLED.setIsLedOn(true);
            }
            else if(param.getCommandType() == 2){
                updatedLED.setIsLedOn(false);
            }
        }

        return updatedLED;
    }

    @Override
    protected void onPostExecute(LED result){

    }
}
