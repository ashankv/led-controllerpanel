package com.benisontechnologies.ledcontrollerpanel;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ashank on 7/23/2016.
 */
public class GetStatusParamsTask extends AsyncTask<AsyncParam, Void, LED> {

    private View activity;

    public GetStatusParamsTask(View activity){
        this.activity = activity;
    }

    @Override
    protected LED doInBackground(AsyncParam... groups) {

        AsyncParam param = groups[0];

        byte[] payload = {(byte)0x1, (byte)0x0, (byte)0x1, (byte)0x1,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x2,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x28,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x1E,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x19,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x32,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x4B,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x64,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x5E,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x25,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x2A,
                        (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x38};

        //CoapHelper
        CoapHelper.parseAndUpdateStatusParams(param.getCoapGroup().getLeds().get(CoapHelper.currLedSelPos), payload);

        try{
            Thread.sleep(5000);
        }catch(Exception e){

        }

        LED updatedLED = param.getCoapGroup().getLeds().get(param.getLedPosition());
        return updatedLED;

    }

    @Override
    protected void onPostExecute(LED result) {
        TextView hwRevision = (TextView) activity.findViewById(R.id.stat_loading_1);
        TextView swRevision = (TextView) activity.findViewById(R.id.stat_loading_2);
        TextView ledOnMinutes = (TextView) activity.findViewById(R.id.stat_loading_3);
        TextView driverActTime = (TextView) activity.findViewById(R.id.stat_loading_4);
        TextView minuties115c = (TextView) activity.findViewById(R.id.stat_loading_5);
        TextView minuties110c = (TextView) activity.findViewById(R.id.stat_loading_6);
        TextView minuties105c = (TextView) activity.findViewById(R.id.stat_loading_7);
        TextView minuties100c = (TextView) activity.findViewById(R.id.stat_loading_8);
        TextView minuties95c = (TextView) activity.findViewById(R.id.stat_loading_9);
        TextView minuties90c = (TextView) activity.findViewById(R.id.stat_loading_10);
        TextView minuties85c = (TextView) activity.findViewById(R.id.stat_loading_11);
        TextView minuties80c = (TextView) activity.findViewById(R.id.stat_loading_12);

        LED myLed = CoapHelper.coapGroups.get(CoapHelper.currCoapGrpSelPos).getLeds().get(CoapHelper.currLedSelPos);

        hwRevision.setText(Integer.toString(myLed.getHwRevision()));
        swRevision.setText(Integer.toString(myLed.getSwRevision()));
        ledOnMinutes.setText(Integer.toString(myLed.getLedOnMinutes()));
        driverActTime.setText(Integer.toString(myLed.getDriverActTime()));
        minuties115c.setText(Integer.toString(myLed.getMinuties115c()));
        minuties110c.setText(Integer.toString(myLed.getMinuties110c()));
        minuties105c.setText(Integer.toString(myLed.getMinuties105c()));
        minuties100c.setText(Integer.toString(myLed.getMinuties100c()));
        minuties95c.setText(Integer.toString(myLed.getMinuties95c()));
        minuties90c.setText(Integer.toString(myLed.getMinuties90c()));
        minuties85c.setText(Integer.toString(myLed.getMinuties85c()));
        minuties80c.setText(Integer.toString(myLed.getMinuties80c()));
    }


}
