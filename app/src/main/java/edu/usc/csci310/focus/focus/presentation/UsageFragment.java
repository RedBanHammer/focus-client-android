package edu.usc.csci310.focus.focus.presentation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import edu.usc.csci310.focus.focus.R;
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

    public UsageFragment() {
        // Required empty public constructor
        profileStatArrayList = StatsManager.getDefaultManager().getAllProfileStats();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usage, container, false);
        profileBarChart = (BarChart) v.findViewById(R.id.profileBarChart);
        appBarChart = (BarChart) v.findViewById(R.id.appBarChart);
        totalPieChart = (PieChart) v.findViewById(R.id.totalPieChart);

        //test data
        for(int i = 1; i < 8; i++)
        {
            ProfileStat stat = new ProfileStat(ProfileManager.getDefaultManager().getAllProfiles().get(0).getIdentifier());
            Calendar cal = new GregorianCalendar();
            cal.add(Calendar.DATE, -(Calendar.DAY_OF_WEEK - i));

            stat.addFocusedInterval(cal, 10l);
        }

        loadDataProfileBarChart();
        loadDataAppBarChart();
        loadDataTotalPieChart();

        // Inflate the layout for this fragment
        return v;
    }

    private void loadDataProfileBarChart() {
        //find day of week string
        final String [] dayOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        Legend l = profileBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

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
        xAxis.setEnabled(false);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setLabelCount(7, true);
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return dayOfWeek[(int) value];
//            }
//        });

        profileBarChart.setExtraBottomOffset(-50f);

        //week starts at sunday
        //go through the array list, pull out profiles from the last few days (excluding current day)
        int i, j, k, counter = 0;
        //List<BarEntry> entries = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Calendar extractCal = Calendar.getInstance();
        extractCal.set(Calendar.HOUR_OF_DAY, 0);
        extractCal.set(Calendar.MINUTE, 0);
        extractCal.set(Calendar.SECOND, 0);
        extractCal.set(Calendar.MILLISECOND, 0);

        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();


        for(i = 1; i < calendar.DAY_OF_WEEK+1; i++) {
            String[] labels = new String[profileStatArrayList.size()]; //this might not work
            float[] times = new float[profileStatArrayList.size()]; //this might not work

            for (ProfileStat ps : profileStatArrayList) {
                //for each profileStat, extract the hours on each day.
                //set date, month, year, (start time, hour and minute)
                Float totalTime = 0f;
//                extractCal.set(Calendar.DAY_OF_WEEK, i);
                extractCal.add(Calendar.DATE, -(calendar.DAY_OF_WEEK - i));
                Long duration = 24l*60l; // one day

                HashMap<Calendar, Long> intervalsInInterval = ps.getFocusedIntervalsInInterval(extractCal, duration);

                for (Calendar c : intervalsInInterval.keySet()) {
                    Long addTime = intervalsInInterval.get(c);
                    totalTime += addTime;
                }

                if (totalTime > 0) {
                    //add it to the bar graph, find the profile name
                    String profileID = ps.getIdentifier();
                    String profileName = ProfileManager.getDefaultManager().getProfileWithIdentifier(profileID).getName();
                    //name is profileName, hours is in totalTime, day is in i. package it up
                    labels[counter] = profileName;
                    times[counter] = totalTime / 60; //store hours, not minutes
                }

            }

            // Add to
            barEntries.add(new BarEntry(i, times));

            //for testing
            //barEntries.add(new BarEntry(i, 5));
            //barEntries.add(new BarEntry(i, 2));


            BarDataSet barDataSet;
            if (profileBarChart.getData() != null &&
                    profileBarChart.getData().getDataSetCount() > 0) {
                barDataSet = (BarDataSet) profileBarChart.getData().getDataSetByIndex(0);
                barDataSet.setValues(barEntries);
                //profileBarChart.getData().notifyDataChanged();
                //profileBarChart.notifyDataSetChanged();
            } else {
                //second parameter shld be day of the week
                barDataSet = new BarDataSet(barEntries, dayOfWeek[i-1]);
                barDataSet.setDrawIcons(false);
                //barDataSet.setColors(getColors());
                barDataSet.setStackLabels(labels);

                dataSets.add(barDataSet);

            }
        }

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.9f);
        data.setValueFormatter(new MyValueFormatter());
        profileBarChart.setData(data);

        profileBarChart.setFitBars(true);
        profileBarChart.invalidate();

    }

    //

    private void loadDataAppBarChart() {

    }

    private void loadDataTotalPieChart() {

    }

}
