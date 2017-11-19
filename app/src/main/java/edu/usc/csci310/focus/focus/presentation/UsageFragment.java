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
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.Date;
import java.util.Map;



import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import edu.usc.csci310.focus.focus.dataobjects.App;
import edu.usc.csci310.focus.focus.dataobjects.Profile;

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
    private PieChart totalPieChart;
    private ArrayList<ProfileStat> profileStatArrayList;
    private final static long MINUTES_IN_WEEK = 10080;
    private ArrayList<Profile> profiles = new ArrayList<>();
    private ListView mostUsedProfiles;
    private appViewAdapter appViewAdapter;


    public UsageFragment() {
        // Required empty public constructor
        profileStatArrayList = StatsManager.getDefaultManager().getAllProfileStats();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usage, container, false);
        mostUsedProfiles = (ListView) v.findViewById(R.id.mostUsedProfiles);
        profileBarChart = (BarChart) v.findViewById(R.id.profileBarChart);
        totalPieChart = (PieChart) v.findViewById(R.id.totalPieChart);


        loadDataProfileBarChart();
        loadDataAppList();
        loadDataTotalPieChart();

        // Inflate the layout for this fragment
        return v;
    }

    private void loadDataProfileBarChart() {
        //find day of week string
        final String [] dayOfWeek = {" ", "S", "M", "T", "W", "Th", "F", "Sa", " "};

        Legend l = profileBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
        l.setYEntrySpace(6f);

        profileBarChart.getDescription().setEnabled(false);
        profileBarChart.setPinchZoom(false);

        profileBarChart.setDrawGridBackground(false);
        profileBarChart.setDrawBarShadow(false);

        profileBarChart.setDrawValueAboveBar(false);
        profileBarChart.setHighlightFullBarEnabled(false);

        YAxis leftAxis = profileBarChart.getAxisLeft();
        //leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        profileBarChart.getAxisRight().setEnabled(false);

        XAxis xLabels = profileBarChart.getXAxis();
        XAxis xAxis = profileBarChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
//        xAxis.setLabelCount(7, true);


        profileBarChart.setExtraBottomOffset(-50f);

        //week starts at sunday
        //go through the array list, pull out profiles from the last few days (excluding current day)
        int i, counter = 0;
        //List<BarEntry> entries = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

        ArrayList<String> labels = new ArrayList<>();

        for (ProfileStat stat : profileStatArrayList) {
            String profileID = stat.getIdentifier();
            String profileName = ProfileManager.getDefaultManager().getProfileWithIdentifier(profileID).getName();
            labels.add(profileName);
        }

        for(i = 1; i < calendar.DAY_OF_WEEK+1; i++) {
            float[] times = new float[labels.size()];

            int statIndex = 0;
            for (ProfileStat ps : profileStatArrayList) {
                //for each profileStat, extract the hours on each day.
                //set date, month, year, (start time, hour and minute)
                Float totalTime = 0f;

                Calendar extractCal = Calendar.getInstance();
                extractCal.set(Calendar.HOUR_OF_DAY, 0);
                extractCal.set(Calendar.MINUTE, 0);
                extractCal.set(Calendar.SECOND, 0);
                extractCal.set(Calendar.MILLISECOND, 0);

                extractCal.add(Calendar.DATE, -(calendar.DAY_OF_WEEK - i));
                Long duration = 24l*60l; // one day

                HashMap<Calendar, Long> intervalsInInterval = ps.getFocusedIntervalsInInterval(extractCal, duration);

                for (Calendar c : intervalsInInterval.keySet()) {
                    Long addTime = intervalsInInterval.get(c);
                    totalTime += addTime;
                }

                if (totalTime > 0) {
                    times[statIndex] = (totalTime / 60); //store hours, not minutes
                }

                statIndex++;
            }

            // Add to dataset
            barEntries.add(new BarEntry(i, times));
        }
        BarDataSet barDataSet;
        if (profileBarChart.getData() != null && profileBarChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) profileBarChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(barEntries);
        } else {
            barDataSet = new BarDataSet(barEntries, " ");
            barDataSet.setDrawIcons(false);
            barDataSet.setColors(getColors());
            if(labels.size() > 0)
            {
                barDataSet.setStackLabels(labels.toArray(new String[labels.size()]));
            }

            dataSets.add(barDataSet);
        }

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.9f);
        data.setValueFormatter(new MyValueFormatter());
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dayOfWeek[(int) value];
            }
        });

        profileBarChart.setData(data);

        profileBarChart.invalidate();
    }
    

    private void loadDataAppList() {
        //change this to a list? or a pie chart?
        //get the applist of apps to pass to the adapter
        //most used apps for THE PAST WEEK
        ArrayList<Profile> maxProfileList = new ArrayList<>();
        ArrayList<App> appList = new ArrayList<>();
        Profile maxProfile = null;
        long maxProfileTime = 0;

        for (ProfileStat ps : profileStatArrayList) {
            //find most used profile, list those apps.
            long totalTime = 0;

            Calendar extractCal = Calendar.getInstance();
            extractCal.set(Calendar.HOUR_OF_DAY, 0);
            extractCal.set(Calendar.MINUTE, 0);
            extractCal.set(Calendar.SECOND, 0);
            extractCal.set(Calendar.MILLISECOND, 0);

            extractCal.add(Calendar.DATE, -7);
            Long duration = 24l*60l * 7l; // minutes in one week

            HashMap<Calendar, Long> intervalsInInterval = ps.getFocusedIntervalsInInterval(extractCal, duration);

            for (Calendar c : intervalsInInterval.keySet()) {
                Long addTime = intervalsInInterval.get(c);
                totalTime += addTime;
            }

            if(totalTime > maxProfileTime)
            {
                //found a new max profile
                maxProfileTime = totalTime;
                maxProfile = ProfileManager.getDefaultManager().getProfileWithIdentifier(ps.getIdentifier());
                //maxProfileList.add(ProfileManager.getDefaultManager().getProfileWithIdentifier(ps.getIdentifier()));
            }

        }
        for(App a : maxProfile.getApps())
        {
            appList.add(a);
        }

        appViewAdapter = new appViewAdapter(this.getContext(), appList);
        mostUsedProfiles.setAdapter(appViewAdapter);

    }


    private void loadDataTotalPieChart() {
//        setUpUsageData();
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

    private int[] getColors() {

        int stacksize = profileStatArrayList.size();

        // have as many colors as stack-values per entry
        int[] colors = new int[Math.max(1, stacksize)];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
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
