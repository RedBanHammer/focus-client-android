package edu.usc.csci310.focus.focus;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.blockers.LogEntry;
import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.managers.BlockingManager;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.presentation.NotificationListFragment;
import edu.usc.csci310.focus.focus.presentation.ProfileList;
import edu.usc.csci310.focus.focus.presentation.ProfileListViewAdapter;
import edu.usc.csci310.focus.focus.presentation.ScheduleList;

public class MainActivity extends AppCompatActivity {
    MyPagerAdapter viewPagerAdapter;

    ViewPager mViewPager;
    TabLayout tabLayout;

    // Tab titles
    private static String[] tabs = { "Profiles", "Schedules", "Notifications" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up managers
        BlockingManager.getDefaultManager();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int NUM_TABS = 3;
        private FragmentManager fm;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fm = fragmentManager;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_TABS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    ArrayList<Profile> profiles = ProfileManager.getDefaultManager().getAllProfiles();
                    return ProfileList.newInstance(0, tabs[0], profiles);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return ScheduleList.newInstance(1, tabs[1]);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return NotificationListFragment.newInstance(2, tabs[2]);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return tabs[0];
                case 1:
                    return tabs[1];
                case 2:
                    return tabs[2];
                default:
                    return null;
            }
        }
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
