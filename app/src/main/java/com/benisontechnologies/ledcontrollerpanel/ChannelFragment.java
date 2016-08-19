package com.benisontechnologies.ledcontrollerpanel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Ashank on 7/30/2016.
 */
public class ChannelFragment extends Fragment {
    public ChannelFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.channel_tab_activity, container, false);

        final Spinner channelSpinner = (Spinner) rootView.findViewById(R.id.channel_spinner);

        CoapGroup currentCoapGroup = CoapHelper.coapGroups.get(CoapHelper.currCoapGrpSelPos);
        LED currentLED = currentCoapGroup.getLeds().get(CoapHelper.currLedSelPos);


        if(currentCoapGroup.isGroupOfLeds()){
            AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos, CoapHelper.currChannelSelPos);
            new GetChannelParamsTask(rootView).execute(asyncParam);
        } else{
            AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0, CoapHelper.currChannelSelPos);
            new GetChannelParamsTask(rootView).execute(asyncParam);
        }






        ArrayList<String> channelNumberArrayList = new ArrayList<String>();

        Bundle extras = getActivity().getIntent().getExtras();
        String currentLedName = extras.getString("CHANNEL_NAME");
        final int currentPosition = extras.getInt("CHANNEL_POSITION");
        CoapHelper.currChannelSelPos = currentPosition;


        for(int i = 0;  i<AppConfigurations.numberOfChannels; i++){
            channelNumberArrayList.add("Channel " + (i+1));
        }

        ArrayAdapter<String> moreLedAdapter = new ArrayAdapter<String>(getActivity(), R.layout.more_led_spinner_item, channelNumberArrayList);
        channelSpinner.setAdapter(moreLedAdapter);
        channelSpinner.setSelection(currentPosition, false);

        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);


        channelSpinner.post(new Runnable() {

            @Override
            public void run() {

                channelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(getActivity(), BallastActivity.class);
                        intent.putExtra("CHANNEL_NAME", parent.getItemAtPosition(position).toString());
                        intent.putExtra("CHANNEL_POSITION", position);

                        startActivity(intent);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        return rootView;
    }
}