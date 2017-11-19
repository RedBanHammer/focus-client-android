package edu.usc.csci310.focus.focus.presentation;


import android.app.usage.UsageStatsManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.Profile;
import edu.usc.csci310.focus.focus.dataobjects.ProfileStat;
import edu.usc.csci310.focus.focus.managers.ProfileManager;
import edu.usc.csci310.focus.focus.managers.StatsManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsageFragment extends Fragment {
    private BarChart profileBarChart;
    private BarChart appBarChart;
    private PieChart totalPieChart;
    private ArrayList<ProfileStat> profileStatArrayList;
    private final static long MINUTES_IN_WEEK = 10080;
    private ArrayList<Profile> profiles;

    public UsageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usage, container, false);
        profileBarChart = (BarChart) v.findViewById(R.id.profileBarChart);
        appBarChart = (BarChart) v.findViewById(R.id.appBarChart);
        totalPieChart = (PieChart) v.findViewById(R.id.totalPieChart);

        loadDataProfileBarChart();
        loadDataAppBarChart();
        loadDataTotalPieChart();

        // Inflate the layout for this fragment
        return v;
    }

    private void loadDataProfileBarChart() {

    }

    private void loadDataAppBarChart() {

    }


    private void loadDataTotalPieChart() {
        setUpUsageData();
        profileStatArrayList = StatsManager.getDefaultManager().getAllProfileStats();
        totalPieChart.clear();
        List<PieEntry> entries = new ArrayList<>();
        Long totalHours = new Long(0);
        for (int index = 0; index< profileStatArrayList.size(); index++){
            Calendar currDay = getLastWeek();
            Calendar endTime = (Calendar) currDay.clone();
            endTime.add(Calendar.DATE, 6);
            HashMap<Calendar, Long> profileWeek = profileStatArrayList.get(index).getFocusedIntervalsInInterval(currDay, MINUTES_IN_WEEK);
            Long profileTotal = new Long(0);
            for (Map.Entry<Calendar, Long> entry: profileWeek.entrySet()){
                profileTotal += entry.getValue();
                totalHours +=entry.getValue();
            }
            String profileName = getProfileName(profileStatArrayList.get(index));
            entries.add(new PieEntry(profileTotal.floatValue(), profileName));
        }
        PieDataSet set = new PieDataSet(entries, "Total HRS blocked: " + totalHours);
        int[] colors = {
                getResources().getColor(R.color.event_color_01),
                getResources().getColor(R.color.event_color_02),
                getResources().getColor(R.color.event_color_03),
                getResources().getColor(R.color.event_color_04),
                getResources().getColor(R.color.event_color_05),
        };
        ArrayList<Integer> colorList = new ArrayList<>();
        for (int index=0; index<profileStatArrayList.size(); index++) {
            colorList.add(colors[index % colors.length]);
        }
        set.setColors(colorList);
        PieData data = new PieData(set);
        //data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        totalPieChart.setData(data);
        totalPieChart.setUsePercentValues(false);
        totalPieChart.setDrawHoleEnabled(true);
        totalPieChart.setHoleColor(R.color.colorPrimaryDark);
        Description desc = new Description();
        desc.setText("Chart displaying breakdown of profile usage throughout the week");
        desc.setTextAlign(Paint.Align.CENTER);
        totalPieChart.setDescription(desc);
        Legend legend = totalPieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        legend.setWordWrapEnabled(true);
        totalPieChart.invalidate(); // refresh
    }

    public Calendar getLastWeek(){
        Calendar currDay = Calendar.getInstance();
        Date date = new Date();
        currDay.setTime(date);
        int i = currDay.get(Calendar.DAY_OF_WEEK) - currDay.getFirstDayOfWeek();
        currDay.add(Calendar.DATE, -i - 7);
        currDay.set(Calendar.HOUR_OF_DAY, 0);
        currDay.set(Calendar.MINUTE, 0);
        currDay.set(Calendar.SECOND, 0);
        currDay.set(Calendar.MILLISECOND, 0);
        return currDay;
    }
    private String getProfileName(ProfileStat profileStat){
        for (Profile p: profiles){
            if (p.getIdentifier().equals(profileStat.getIdentifier())){
                return p.getName();
            }
        }
        return "";
    }

    private void setUpUsageData(){
        ArrayList<Long> durations = new ArrayList<>();
        durations.add(new Long(30));
        durations.add(new Long(60));
        durations.add(new Long(10));
        durations.add(new Long(45));
        profiles = new ArrayList<>();

        for (int i = 0; i<5; i++){
            Profile usageProfile = new Profile("Usage Profile " + i);
            profiles.add(usageProfile);
        }
        Calendar currDay = getLastWeek();
        ProfileStat profileStat1 = new ProfileStat(profiles.get(0).getIdentifier());
        profileStat1.addFocusedInterval(currDay, durations.get(0));
        ProfileStat profileStat2 = new ProfileStat(profiles.get(1).getIdentifier());
        profileStat2.addFocusedInterval(currDay, durations.get(1));
        ProfileStat profileStat3 = new ProfileStat(profiles.get(2).getIdentifier());
        profileStat3.addFocusedInterval(currDay, durations.get(2));
        ProfileStat profileStat4 = new ProfileStat(profiles.get(3).getIdentifier());
        profileStat4.addFocusedInterval(currDay, durations.get(3));
        StatsManager.getDefaultManager().removeAllProfileStats();
        StatsManager.getDefaultManager().addFocusedIntervalWithProfileIdentifier(profileStat1.getIdentifier(), currDay, durations.get(0));
        StatsManager.getDefaultManager().addFocusedIntervalWithProfileIdentifier(profileStat2.getIdentifier(), currDay, durations.get(1));
        StatsManager.getDefaultManager().addFocusedIntervalWithProfileIdentifier(profileStat3.getIdentifier(), currDay, durations.get(2));
        StatsManager.getDefaultManager().addFocusedIntervalWithProfileIdentifier(profileStat4.getIdentifier(), currDay, durations.get(3));
    }


}
