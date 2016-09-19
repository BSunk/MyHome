package com.bsunk.myhome;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.bsunk.myhome.service.ConfigDataPullService;
import com.bsunk.myhome.service.EventSourceConnection;
import com.tylerjroach.eventsource.EventSource;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EventSourceConnection sseHandler = new EventSourceConnection(this);
    private EventSource eventSource;
    String eventUrl = "http://192.168.10.101:8123/api/stream";
    Map<String, String> extraHeaderParameters = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  drawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        Intent service = new Intent(this, ConfigDataPullService.class);
        startService(service);

        startEventSource();

        MainActivityFragment fragment = new MainActivityFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_fragment_container, fragment)
                .commit();

    }

    private void startEventSource() {
        //extraHeaderParameters.put("Content-Type", "application/json");
        eventSource = new EventSource.Builder(eventUrl)
                .eventHandler(sseHandler)
            //    .headers(extraHeaderParameters)
                .build();
        eventSource.connect();
    }

    private void stopEventSource() {
        if (eventSource != null)
            eventSource.close();
        sseHandler = null;
    }
}
