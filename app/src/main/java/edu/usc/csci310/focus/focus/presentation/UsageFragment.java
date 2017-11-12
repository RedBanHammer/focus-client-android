package edu.usc.csci310.focus.focus.presentation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.usc.csci310.focus.focus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsageFragment extends Fragment {


    public UsageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_usage, container, false);
    }

}
