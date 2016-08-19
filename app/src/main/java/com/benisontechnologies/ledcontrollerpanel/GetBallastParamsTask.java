package com.benisontechnologies.ledcontrollerpanel;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.eclipse.californium.core.CoapClient;

import java.net.URI;
import java.nio.ByteBuffer;

/**
 * Created by Ashank on 7/20/2016.
 */
public class GetBallastParamsTask extends AsyncTask<AsyncParam, Void, LED> {

    private View activity;


    public GetBallastParamsTask(View activity){
        this.activity = activity;
    }
    @Override
    protected LED doInBackground(AsyncParam... groups) {

        AsyncParam param = groups[0];
        //CoapHelper.getBallastParams(param.getCoapGroup(), param.getLedPosition());

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

        CoapHelper.parseAndUpdateBallastParams(param.getCoapGroup().getLeds().get(CoapHelper.currLedSelPos), payload);

        try{
            Thread.sleep(5000);
        }catch(Exception e){

        }

        LED updatedLED = param.getCoapGroup().getLeds().get(param.getLedPosition());
        return updatedLED;


    }

    @Override
    protected void onPostExecute(LED result) {
        TextView busVoltageText = (TextView) activity.findViewById(R.id.loading_1);
        TextView maxPowerText = (TextView) activity.findViewById(R.id.loading_2);
        TextView currentPowerText = (TextView) activity.findViewById(R.id.loading_3);
        TextView extTempText = (TextView) activity.findViewById(R.id.loading_4);
        TextView boardTempText = (TextView) activity.findViewById(R.id.loading_5);
        SeekBar dimSeekbar = (SeekBar) activity.findViewById(R.id.dim_seekbar);
        SeekBar fadeSeekbar = (SeekBar) activity.findViewById(R.id.fadetime_seekbar);
        SeekBar recovLightSeekbar = (SeekBar) activity.findViewById(R.id.recov_light_seekbar);
        SeekBar minLightSeekbar = (SeekBar) activity.findViewById(R.id.min_light_seekbar);
        SeekBar maxLightSeekbar = (SeekBar) activity.findViewById(R.id.max_light_seekbar);
        TextView dimText = (TextView) activity.findViewById(R.id.dimming_text);
        TextView fadeText = (TextView) activity.findViewById(R.id.fadetime_text);
        TextView recovLightText = (TextView) activity.findViewById(R.id.recov_light_text);
        TextView minLightText = (TextView) activity.findViewById(R.id.min_light_text);
        TextView maxLightText = (TextView) activity.findViewById(R.id.max_light_text);
        ImageView overTempIcon = (ImageView) activity.findViewById(R.id.over_temp_icon);
        ImageView lampStatusIcon = (ImageView) activity.findViewById(R.id.lamp_status_icon);
        ImageView shortCircuitIcon = (ImageView) activity.findViewById(R.id.short_circuit_icon);
        ImageView openCircuitIcon = (ImageView) activity.findViewById(R.id.open_circuit_icon);
        TextView overTempText = (TextView) activity.findViewById(R.id.over_temp_text);
        TextView lampStatusText = (TextView) activity.findViewById(R.id.lamp_status_text);
        TextView shortCircuitText = (TextView) activity.findViewById(R.id.short_circuit_text);
        TextView openCircuitText = (TextView) activity.findViewById(R.id.open_circuit_text);
        SwitchCompat isOnSwitch = (SwitchCompat) activity.findViewById(R.id.on_switch);
        
        LED myLed = CoapHelper.coapGroups.get(CoapHelper.currCoapGrpSelPos).getLeds().get(CoapHelper.currLedSelPos);

        int colorRed = Color.parseColor("#D32F2F");
        int colorGreen = Color.parseColor("#8BC34A");
        int colorBlack = Color.parseColor("#212121");
        int colorGray = Color.parseColor("#BDBDBD");

        busVoltageText.setText(Integer.toString(myLed.getBusVoltage()));
        maxPowerText.setText(Integer.toString(myLed.getMaxPower()));
        currentPowerText.setText(Integer.toString(myLed.getCurrentPower()));
        extTempText.setText(Integer.toString(myLed.getExtTemp()));
        boardTempText.setText(Integer.toString(myLed.getBoardTemp()));

        dimSeekbar.setProgress(myLed.getDimLevel());
        fadeSeekbar.setProgress(myLed.getFadeTime());
        recovLightSeekbar.setProgress(myLed.getRecovLightTime());
        minLightSeekbar.setProgress(myLed.getMinLightLevel());
        maxLightSeekbar.setProgress(myLed.getMaxLightLevel());

        dimText.setText(Integer.toString(myLed.getDimLevel()));
        fadeText.setText(Integer.toString(myLed.getFadeTime()));
        recovLightText.setText(Integer.toString(myLed.getRecovLightTime()));
        minLightText.setText(Integer.toString(myLed.getMinLightLevel()));
        maxLightText.setText(Integer.toString(myLed.getMaxLightLevel()));

        if(myLed.isLedOn()){
            lampStatusIcon.setColorFilter(colorGreen);
            lampStatusText.setTextColor(colorBlack);
            isOnSwitch.setChecked(true);
        } else{
            lampStatusIcon.setColorFilter(colorGray);
            lampStatusText.setTextColor(colorGray);
        }

        if(myLed.isOpenCircuit()){
            openCircuitIcon.setColorFilter(colorRed);
            openCircuitText.setTextColor(colorRed);
        } else{
            openCircuitIcon.setColorFilter(colorGray);
            openCircuitText.setTextColor(colorGray);
        }

        if(myLed.isShortCircuit()){
            shortCircuitIcon.setColorFilter(colorRed);
            shortCircuitText.setTextColor(colorRed);
        } else{
            shortCircuitIcon.setColorFilter(colorGray);
            shortCircuitText.setTextColor(colorGray);
        }

        if(myLed.isOverTemperature()){
            overTempIcon.setColorFilter(colorRed);
            overTempText.setTextColor(colorRed);
        } else{
            overTempIcon.setColorFilter(colorGray);
            overTempText.setTextColor(colorGray);
        }
    }

}
