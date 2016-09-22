package com.bsunk.myhome;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bsunk.myhome.data.MyHomeContract;
import com.bsunk.myhome.service.ConfigDataPullService;
import com.bsunk.myhome.service.EventSourceConnection;
import com.tylerjroach.eventsource.EventSource;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;
import static com.bsunk.myhome.data.MyHomeContract.MyHome.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    private EventSourceConnection sseHandler = new EventSourceConnection(this);
    private EventSource eventSource;
    String eventUrl = "http://192.168.10.101:8123/api/stream";
    Map<String, String> extraHeaderParameters = new HashMap<>();
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
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
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

        setNavigationViewItems();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                String title = item.getTitle().toString();
                if(title.equals(ConfigDataPullService.TYPE[0])) {
                    NAV_PAGE = MainActivityFragment.SENSORS_LOADER;
                }
                else if(title.equals(ConfigDataPullService.TYPE[1])) {
                    NAV_PAGE = MainActivityFragment.LIGHTS_LOADER;
                }
                else if(title.equals(ConfigDataPullService.TYPE[2])) {
                    NAV_PAGE = MainActivityFragment.MP_LOADER;
                }
                else if(title.equals("Home")) {
                    NAV_PAGE = MainActivityFragment.ALL_LOADER;
                }

                toolbarTitle.setText(item.getTitle());
                MainActivityFragment fragment = new MainActivityFragment();
                Bundle args = new Bundle();
                args.putInt(TYPE_KEY, NAV_PAGE);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, fragment)
                        .commit();

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

    public void setNavigationViewItems() {
        Menu menu = navigationView.getMenu();

        for(int i=0;i<ConfigDataPullService.TYPE.length;i++) {
            Cursor c = getContentResolver().query(CONTENT_URI, null, MyHomeContract.MyHome.COLUMN_TYPE + " = " + DatabaseUtils.sqlEscapeString(ConfigDataPullService.TYPE[i]), null, null);
            if(c.getCount() != 0) {
                String type = ConfigDataPullService.TYPE[i].replaceAll("\\s","").toLowerCase();
                int id = getResourceId(type, "drawable", getPackageName());
                menu.add(R.id.drawer_group_one, Menu.FLAG_APPEND_TO_GROUP, 0, ConfigDataPullService.TYPE[i]).setIcon(id);
            }
        }
    }

    public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
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
