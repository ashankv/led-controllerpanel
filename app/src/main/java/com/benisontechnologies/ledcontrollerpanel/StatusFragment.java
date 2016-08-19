package com.benisontechnologies.ledcontrollerpanel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ashank on 7/30/2016.
 */
public class StatusFragment extends Fragment {
    public StatusFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.status_tab_activity, container, false);

        CoapGroup currentCoapGroup = CoapHelper.coapGroups.get(CoapHelper.currCoapGrpSelPos);
        if(currentCoapGroup.isGroupOfLeds()){
            AsyncParam asyncParam = new AsyncParam(currentCoapGroup, CoapHelper.currLedSelPos);
            new GetStatusParamsTask(rootView).execute(asyncParam);
        } else{
            AsyncParam asyncParam = new AsyncParam(currentCoapGroup, 0);
            new GetStatusParamsTask(rootView).execute(asyncParam);
        }
        return rootView;
    }
}