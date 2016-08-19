package com.benisontechnologies.ledcontrollerpanel;

import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Ashank on 8/4/2016.
 */
public class GetChannelParamsTask extends AsyncTask<AsyncParam, Void, LED> {

    private View activity;


    public GetChannelParamsTask(View activity){
        this.activity = activity;
    }
    @Override
    protected LED doInBackground(AsyncParam... groups) {

        AsyncParam param = groups[0];

        CoapHelper.getChannelParams(param.getCoapGroup(), param.getLedPosition(), param.getChannelPosition());




        LED updatedLED = param.getCoapGroup().getLeds().get(param.getLedPosition());
        return updatedLED;

    }

    @Override
    protected void onPostExecute(LED result) {
        EditText currentPower = (EditText) activity.findViewById(R.id.current_power_edit);
        EditText voltage = (EditText) activity.findViewById(R.id.voltage_edit);
        EditText maxPower = (EditText) activity.findViewById(R.id.max_power_edit);
        EditText peakCurrent = (EditText) activity.findViewById(R.id.peak_current_edit);
        EditText acOffset = (EditText) activity.findViewById(R.id.ac_offset_edit);

        LED myLed = CoapHelper.coapGroups.get(CoapHelper.currCoapGrpSelPos).getLeds().get(CoapHelper.currLedSelPos);
        Channel currentChannel = myLed.getChannels().get(CoapHelper.currChannelSelPos);
        currentPower.setText(currentChannel.getChannelCurrentPower());
        voltage.setText(currentChannel.getChannelVoltage());
        maxPower.setText(currentChannel.getChannelMaxPower());
        peakCurrent.setText(currentChannel.getChannelPeakCurrent());
        acOffset.setText(currentChannel.getChannelACOffset());

    }

}
