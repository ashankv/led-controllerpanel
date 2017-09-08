package com.benisontechnologies.ledcontrollerpanel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button nextButton;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.discovery_bar);
        mToolbar.setTitle("Discover Your Devices");
        setSupportActionBar(mToolbar);

        LED firstLED = new LED(844,397,289,589,489,49,59,22,34,10, "Living Room LED");                                  //SIMULATED DATA
        LED anotherFirstLED = new LED(123, 421, 524, 983, 120, 79, 42, 100, 28, 38, "Family Room LED");
        ArrayList<LED> firstLEDArrayList = new ArrayList<LED>();
        ArrayList<Channel> channelArrayList = new ArrayList<Channel>();

        firstLED.setChannels(channelArrayList);
        anotherFirstLED.setChannels(channelArrayList);

        for(int i = 0; i<AppConfigurations.numberOfChannels; i++){
            Channel newChannel = new Channel();
            channelArrayList.add(newChannel);
        }

        firstLEDArrayList.add(firstLED);
        firstLEDArrayList.add(anotherFirstLED);
        final CoapGroup firstCoapGroup = new CoapGroup(firstLEDArrayList, "192.168.1.220", "coap://192.168.1.220");
        if(firstCoapGroup.getLeds().size() > 1){
            firstCoapGroup.setIsGroupOfLeds(true);
        }else{
            firstCoapGroup.setIsGroupOfLeds(false);
        }

        LED secondLED = new LED(11,12,13,14,15,16,17,18,19,20, "Random LED");
        secondLED.setChannels(channelArrayList);
        ArrayList<LED> secondLEDArrayList = new ArrayList<LED>();
        secondLEDArrayList.add(secondLED);
        final CoapGroup secondCoapGroup = new CoapGroup(secondLEDArrayList, "192.168.2.234", "coap://192.168.2.234");
        if(secondCoapGroup.getLeds().size() > 1){
            secondCoapGroup.setIsGroupOfLeds(true);
        } else{
            secondCoapGroup.setIsGroupOfLeds(false);
        }

        CoapHelper.coapGroups.add(firstCoapGroup);
        CoapHelper.coapGroups.add(secondCoapGroup);                   //SIMULATED DATA



        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DiscoveryHelper.sendMulticastRequest(MainActivity.this);                         //DISCOVERY PT 1
                //boolean successInMakingCoapGroups = DiscoveryHelper.getLedIdentifiers();                            //DISCOVERY PT 2
                Intent intent = new Intent(MainActivity.this, BallastActivity.class);

                //if(successInMakingCoapGroups) {
                    intent.putExtra("IP_ADDRESS", CoapHelper.coapGroups.get(0).getIpAddress());
                    intent.putExtra("GROUP_POSITION", 0);
                    intent.putExtra("LED_NAME", CoapHelper.coapGroups.get(0).getLeds().get(0).getLedName());
                    intent.putExtra("LED_POSITION", 0);
                    startActivity(intent);
                //}else{

                //}

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
