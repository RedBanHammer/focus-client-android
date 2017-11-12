package edu.usc.csci310.focus.focus.presentation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.ProfileStat;
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

    }

}
