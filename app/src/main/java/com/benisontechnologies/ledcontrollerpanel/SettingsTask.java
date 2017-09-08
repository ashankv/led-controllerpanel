package com.benisontechnologies.ledcontrollerpanel;

import android.os.AsyncTask;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Ashank on 7/25/2016.
 */
public class SettingsTask extends AsyncTask<AsyncParam, Void, LED> {

    private View activity;


    @Override
    protected LED doInBackground(AsyncParam... params) {

        AsyncParam param =  params[0];
        LED updatedLED = param.getCoapGroup().getLeds().get(param.getLedPosition());

        boolean success = CoapHelper.putSettingsBallast(param.getCoapGroup(), param.getLedPosition(), param.getCommand(), param.getContext(), param.getCommandType(), param.getChangedValue());
        int commandType = param.getCommandType();
        int changedValue = param.getChangedValue();

        if(success){
            switch(commandType){
                case 1:
                    updatedLED.setDimLevel(changedValue);
                case 2:
                    updatedLED.setFadeTime(changedValue);
                case 3:
                    updatedLED.setRecovLightTime(changedValue);
                case 4:
                    updatedLED.setMinLightLevel(changedValue);
                case 5:
                    updatedLED.setMaxLightLevel(changedValue);
            }
        }


        return updatedLED;
    }

    @Override
    protected void onPostExecute(LED result){


    }
}
