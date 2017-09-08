package com.benisontechnologies.ledcontrollerpanel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Ashank on 7/12/2016.
 */
public class BallastActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private MyPagerAdapter mAdapter;
    TextView ipText;
    Spinner ipSpinner;
    public boolean userIsInteracting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballast_activity);

        //Initializing
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        ipSpinner = (Spinner) findViewById(R.id.ip_address_spinner);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mPager = (ViewPager) findViewById(R.id.pager);
        mToolbar=(Toolbar) findViewById(R.id.ballast_bar);

        //ToolBar Stuff
        mToolbar.setTitle("Ballast Control Panel");
        setSupportActionBar(mToolbar);

        //Getting Intent Extras After OnClick of Spinner Items So That Correct Spinner Title Can Be Displayed
        Bundle extras = getIntent().getExtras();
        String currentIPAddress = extras.getString("IP_ADDRESS");
        final int currentPosition = extras.getInt("GROUP_POSITION");
        CoapHelper.currCoapGrpSelPos = currentPosition;

        //Method For Setting The Spinner Title
        ipSpinner.post(new Runnable() {
           @Override
           public void run() {
               ipSpinner.setSelection(currentPosition);
           }
        });

        //TabLayout Stuff
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));


        //Populating Spinner Item List Based On CoAP Groups
        ArrayList<String> ipAddressArrayList = new ArrayList<String>();

        for(int i = 0;  i<CoapHelper.coapGroups.size(); i++){
            ipAddressArrayList.add(CoapHelper.coapGroups.get(i).getIpAddress());
        }

        ArrayAdapter<String> ipAdapter = new ArrayAdapter<String>(this, R.layout.ip_spinner_item, ipAddressArrayList);

        ipSpinner.post(new Runnable() {
            @Override
            public void run() {
                ipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(BallastActivity.this, BallastActivity.class);
                        intent.putExtra("IP_ADDRESS", parent.getItemAtPosition(position).toString());
                        intent.putExtra("GROUP_POSITION", position);
                        if (userIsInteracting) {              //Check Method Below,
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        });

        ipSpinner.setAdapter(ipAdapter);


    }

    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        userIsInteracting = true;
    }
}

class MyPagerAdapter extends FragmentStatePagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                BallastFragment myBallastFragment = new BallastFragment();
                return myBallastFragment;
            case 1:
                ChannelFragment myChannelFragment = new ChannelFragment();
                return myChannelFragment;
            case 2:
                StatusFragment myStatusFragment = new StatusFragment();
                return myStatusFragment;
            default:
                return null;
        }




    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){

        if (position == 0) return "Ballast";
        if (position == 1) return "Channel";
        if (position == 2) return "Status";

        else return null;
    }
}

