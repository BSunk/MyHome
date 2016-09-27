package com.bsunk.myhome;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bsunk.myhome.data.MyHomeContract;
import com.bsunk.myhome.service.ConfigDataPullService;
import com.bsunk.myhome.service.EventSourceConnection;
import com.tylerjroach.eventsource.EventSource;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bsunk.myhome.data.MyHomeContract.MyHome.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    private EventSourceConnection sseHandler = new EventSourceConnection(this);
    private EventSource eventSource;
    public static final String TYPE_KEY = "type";
    static final String NAV_PAGE_KEY = "page";
    int NAV_PAGE=0;

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.nvView) NavigationView navigationView;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  drawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerToggle.syncState();

        if(savedInstanceState!=null) {
            NAV_PAGE = savedInstanceState.getInt(NAV_PAGE_KEY);
            toolbarTitle.setText(savedInstanceState.getCharSequence("toolbar_title"));
        }
        else {
            Intent service = new Intent(this, ConfigDataPullService.class);
            startService(service);
        }
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_KEY, NAV_PAGE);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_fragment_container, fragment)
                .commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                MainActivityFragment fragment = new MainActivityFragment();
                Bundle args = new Bundle();

                switch(item.getItemId()) {
                    case R.id.nav_home:
                        NAV_PAGE = MainActivityFragment.ALL_LOADER;
                        toolbarTitle.setText(item.getTitle());
                        args.putInt(TYPE_KEY, NAV_PAGE);
                        fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.main_fragment_container, fragment)
                                .commit();
                        return true;
                    case R.id.sensors_nav:
                        NAV_PAGE = MainActivityFragment.SENSORS_LOADER;
                        toolbarTitle.setText(item.getTitle());
                        args.putInt(TYPE_KEY, NAV_PAGE);
                        fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.main_fragment_container, fragment)
                                .commit();
                        return true;
                    case R.id.lights_nav:
                        NAV_PAGE = MainActivityFragment.LIGHTS_LOADER;
                        toolbarTitle.setText(item.getTitle());
                        args.putInt(TYPE_KEY, NAV_PAGE);
                        fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.main_fragment_container, fragment)
                                .commit();
                        return true;
                    case R.id.mp_nav:
                        NAV_PAGE = MainActivityFragment.MP_LOADER;
                        toolbarTitle.setText(item.getTitle());
                        args.putInt(TYPE_KEY, NAV_PAGE);
                        fragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.main_fragment_container, fragment)
                                .commit();
                        return true;
                    case R.id.nav_settings:
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(NAV_PAGE_KEY, NAV_PAGE);
        savedInstanceState.putCharSequence("toolbar_title", toolbarTitle.getText());
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onPause() {
        super.onPause();
        stopEventSource();
    }

    protected void onResume() {
        super.onResume();
        startEventSource();
        Intent service = new Intent(this, ConfigDataPullService.class);
        startService(service);
    }

    public void settingsOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void startEventSource() {
        String url;
        String baseURL = Utility.buildConnectionBaseURL(getApplicationContext());
        final String pw = Utility.getPW(getApplicationContext());

        if (baseURL!=null) {
            url = baseURL + "/api/stream";
            Map<String, String> extraHeaderParameters = new HashMap<>();
            extraHeaderParameters.put("Content-Type", "application/json");
            extraHeaderParameters.put("x-ha-access", pw);
            eventSource = new EventSource.Builder(url)
                    .eventHandler(sseHandler)
                    .headers(extraHeaderParameters)
                    .build();
            eventSource.connect();
        }
    }

    private void stopEventSource() {
        if (eventSource != null)
            eventSource.close();
        sseHandler = null;
    }
}
