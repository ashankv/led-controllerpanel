package com.benisontechnologies.ledcontrollerpanel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Ashank on 7/30/2016.
 */
public class BallastFragment extends Fragment {

    public BallastFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.ballast_tab_activity, container, false);
        final SeekBar dimSeekbar = (SeekBar) rootView.findViewById(R.id.dim_seekbar);
        final SeekBar fadeSeekbar = (SeekBar) rootView.findViewById(R.id.fadetime_seekbar);
        final SeekBar recovLightSeekbar = (SeekBar) rootView.findViewById(R.id.recov_light_seekbar);
        final SeekBar minLightSeekbar = (SeekBar) rootView.findViewById(R.id.min_light_seekbar);
        final SeekBar maxLightSeekbar = (SeekBar) rootView.findViewById(R.id.max_light_seekbar);
        ImageView overTempIcon = (ImageView) rootView.findViewById(R.id.over_temp_icon);
        ImageView lampStatusIcon = (ImageView) rootView.findViewById(R.id.lamp_status_icon);
        ImageView shortCircuitIcon = (ImageView) rootView.findViewById(R.id.short_circuit_icon);
        ImageView openCircuitIcon = (ImageView) rootView.findViewById(R.id.open_circuit_icon);
        TextView overTempText = (TextView) rootView.findViewById(R.id.over_temp_text);
        TextView lampStatusText = (TextView) rootView.findViewById(R.id.lamp_status_text);
        TextView shortCircuitText = (TextView) rootView.findViewById(R.id.short_circuit_text);
        TextView openCircuitText = (TextView) rootView.findViewById(R.id.open_circuit_text);
        CardView moreLedCard = (CardView) rootView.findViewById(R.id.more_leds_card);
        final Spinner moreLedSpinner = (Spinner) rootView.findViewById(R.id.more_leds_spinner);

        Button turnOn = (Button) rootView.findViewById(R.id.on_btn);
        Button turnOff = (Button) rootView.findViewById(R.id.off_btn);
        Button stepUp = (Button) rootView.findViewById(R.id.step_up_btn);
        Button stepDown = (Button) rootView.findViewById(R.id.step_down_btn);
        Button goMax = (Button) rootView.findViewById(R.id.go_max_btn);
        Button goMin = (Button) rootView.findViewById(R.id.go_min_btn);

        Button setDim = (Button) rootView.findViewById(R.id.set_dim);
        Button setFade = (Button) rootView.findViewById(R.id.set_fade);
        Button setRecov = (Button) rootView.findViewById(R.id.set_recov);
        Button setMin = (Button) rootView.findViewById(R.id.set_min);
        Button setMax = (Button) rootView.findViewById(R.id.set_max);

        final CoapGroup currentCoapGroup = CoapHelper.coapGroups.get(CoapHelper.currCoapGrpSelPos);  //Get Current Coap Group Based on Spinner
        ArrayList<String> ledNameArrayList = null;


        //Check to See If The Current CoapGroup is Actually a Group of LEDs and Set View of Card Based On That
        if(currentCoapGroup.isGroupOfLeds()){

            ledNameArrayList = new ArrayList<String>();

            Bundle extras = getActivity().getIntent().getExtras();
            String currentLedName = extras.getString("LED_NAME");
            final int currentPosition = extras.getInt("LED_POSITION");
            CoapHelper.currLedSelPos = currentPosition;


            for(int i = 0;  i<currentCoapGroup.getLeds().size(); i++){
                ledNameArrayList.add(currentCoapGroup.getLeds().get(i).getLedName());
            }

            ArrayAdapter<String> moreLedAdapter = new ArrayAdapter<String>(getActivity(), R.layout.more_led_spinner_item, ledNameArrayList);
            moreLedSpinner.setAdapter(moreLedAdapter);
            moreLedSpinner.setSelection(currentPosition, false);

            moreLedSpinner.post(new Runnable() {

                @Override
                public void run() {

                    moreLedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(getActivity(), BallastActivity.class);
                            intent.putExtra("LED_NAME", parent.getItemAtPosition(position).toString());
                            intent.putExtra("LED_POSITION", position);
                            startActivity(intent);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            });

        } else{
            moreLedCard.setVisibility(View.GONE);
        }


        //Default Progress of Seekbars
        dimSeekbar.setProgress(50);
        fadeSeekbar.setProgress(50);
        recovLightSeekbar.setProgress(50);
        minLightSeekbar.setProgress(50);
        maxLightSeekbar.setProgress(50);

        //Execute Ballast Params Async Task
        if(currentCoapGroup.isGroupOfLeds()){
            AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos);
            new GetBallastParamsTask(rootView).execute(asyncParam);
        } else{
            AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0);
            new GetBallastParamsTask(rootView).execute(asyncParam);
        }



        //Colors Used
        int colorRed = Color.parseColor("#D32F2F");
        int colorGreen = Color.parseColor("#8BC34A");
        int colorBlack = Color.parseColor("#212121");
        int colorGray = Color.parseColor("#BDBDBD");

        //Default Colors For Icons
        overTempIcon.setColorFilter(colorGray);
        overTempText.setTextColor(colorGray);
        lampStatusIcon.setColorFilter(colorGray);
        lampStatusText.setTextColor(colorGray);
        openCircuitIcon.setColorFilter(colorGray);
        openCircuitText.setTextColor(colorGray);
        shortCircuitIcon.setColorFilter(colorGray);
        shortCircuitText.setTextColor(colorGray);


        //Finding Modifiable  Seekbar Texts
        final TextView dimText = (TextView) rootView.findViewById(R.id.dimming_text);
        final TextView fadeText = (TextView) rootView.findViewById(R.id.fadetime_text);
        final TextView recovLightText = (TextView) rootView.findViewById(R.id.recov_light_text);
        final TextView minLightText = (TextView) rootView.findViewById(R.id.min_light_text);
        final TextView maxLightText = (TextView) rootView.findViewById(R.id.max_light_text);

        //Seekbar Listeners To Update Number While Progress Changes
        dimSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                dimText.setText(String.valueOf(new Integer(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        fadeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                fadeText.setText(String.valueOf(new Integer(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        recovLightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                recovLightText.setText(String.valueOf(new Integer(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        minLightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                minLightText.setText(String.valueOf(new Integer(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        maxLightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                maxLightText.setText(String.valueOf(new Integer(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //Need Context Variable For Following

        final Context context = getActivity();

        //Execute ButtonCmdTask Everytime Button Is Clicked
        turnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] array = {(byte)0x1}; //To Be Changed
                 //To Be Changed

                if(currentCoapGroup.isGroupOfLeds()){
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, array, context, AppConfigurations.TURN_OFF_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                } else{
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, array, context, AppConfigurations.TURN_ON_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                }
            }
        });

        turnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] array = {(byte)0x2}; //To Be Changed
                //To Be Changed

                if(currentCoapGroup.isGroupOfLeds()){
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, array, context, AppConfigurations.TURN_ON_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                } else{
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, array, context, AppConfigurations.TURN_OFF_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                }
            }
        });

        stepUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] array = {(byte)0x3}; //To Be Changed
                //To Be Changed

                if(currentCoapGroup.isGroupOfLeds()){
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, array, context, AppConfigurations.STEP_UP_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                } else{
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, array, context, AppConfigurations.STEP_UP_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                }
            }
        });

        stepDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] array = {(byte)0x4}; //To Be Changed
                //To Be Changed

                if(currentCoapGroup.isGroupOfLeds()){
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, array, context, AppConfigurations.STEP_DOWN_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                } else{
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, array, context, AppConfigurations.STEP_DOWN_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                }
            }
        });

        goMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] array = {(byte)0x5}; //To Be Changed
                //To Be Changed

                if (currentCoapGroup.isGroupOfLeds()) {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, array, context, AppConfigurations.GO_MAX_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                } else {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, array, context, AppConfigurations.GO_MAX_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                }
            }
        });

        goMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] array = {(byte)0x6}; //To Be Changed


                if (currentCoapGroup.isGroupOfLeds()) {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, array, context, AppConfigurations.GO_MIN_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                } else {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, array, context, AppConfigurations.GO_MIN_CMD);
                    new ButtonCmdTask().execute(asyncParam);
                }
            }
        });


        //Execute Settings Task Everytime Set Is Clicked
        setDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] id = {(byte)0x1};                                                            //SETTING ID
                byte[] value = ByteBuffer.allocate(4).putInt(dimSeekbar.getProgress()).array();     //SETTING VALUE

                byte[] payload = new byte[id.length + value.length];                                //SETTING ID + SETTING VALUE

                for(int i=0;  i<payload.length; i++){
                    payload[i] = i<id.length ? id[i] : value[i - id.length];
                }

                if (currentCoapGroup.isGroupOfLeds()) {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, payload, context, AppConfigurations.DIM_SETTING, dimSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                } else {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, payload, context, AppConfigurations.DIM_SETTING, dimSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                }
            }
        });

        setFade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] id = {(byte)0x2};                                                            //SETTING ID
                byte[] value = ByteBuffer.allocate(4).putInt(fadeSeekbar.getProgress()).array();    //SETTING VALUE

                byte[] payload = new byte[id.length + value.length];                                //SETTING ID + SETTING VALUE

                for(int i=0;  i<payload.length; i++){
                    payload[i] = i<id.length ? id[i] : value[i - id.length];
                }
                if (currentCoapGroup.isGroupOfLeds()) {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, payload, context, AppConfigurations.FADE_TIME_SETTING, fadeSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                } else {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, payload, context, AppConfigurations.FADE_TIME_SETTING, fadeSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                }
            }
        });

        setRecov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                byte[] id = {(byte)0x3};                                                                    //SETTING ID
                byte[] value = ByteBuffer.allocate(4).putInt(recovLightSeekbar.getProgress()).array();      //SETTING VALUE

                byte[] payload = new byte[id.length + value.length];                                        //SETTING ID + SETTING VALUE

                for(int i=0;  i<payload.length; i++){
                    payload[i] = i<id.length ? id[i] : value[i - id.length];
                }


                if (currentCoapGroup.isGroupOfLeds()) {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, payload, context, AppConfigurations.RECOV_TIME_SETTING, recovLightSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                } else {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, payload, context, AppConfigurations.RECOV_TIME_SETTING, recovLightSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                }
            }
        });

        setMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] id = {(byte)0x4};                                                                 //SETTING ID
                byte[] value = ByteBuffer.allocate(4).putInt(minLightSeekbar.getProgress()).array();     //SETTING VALUE

                byte[] payload = new byte[id.length + value.length];                                     //SETTING ID + SETTING VALUE

                for(int i=0;  i<payload.length; i++){
                    payload[i] = i<id.length ? id[i] : value[i - id.length];
                }

                if (currentCoapGroup.isGroupOfLeds()) {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, payload, context, AppConfigurations.MIN_LIGHT_SETTING, minLightSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                } else {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, payload, context, AppConfigurations.MIN_LIGHT_SETTING, minLightSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                }
            }
        });

        setMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] id = {(byte)0x5};                                                                 //SETTING ID
                byte[] value = ByteBuffer.allocate(4).putInt(maxLightSeekbar.getProgress()).array();     //SETTING VALUE

                byte[] payload = new byte[id.length + value.length];                                     //SETTING ID + SETTING VALUE

                for(int i=0;  i<payload.length; i++){
                    payload[i] = i<id.length ? id[i] : value[i - id.length];
                }

                if (currentCoapGroup.isGroupOfLeds()) {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, payload, context, AppConfigurations.MAX_LIGHT_SETTING, maxLightSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                } else {
                    AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, payload, context, AppConfigurations.MAX_LIGHT_SETTING, maxLightSeekbar.getProgress());
                    new SettingsTask().execute(asyncParam);
                }
            }
        });

        return rootView;
    }
}