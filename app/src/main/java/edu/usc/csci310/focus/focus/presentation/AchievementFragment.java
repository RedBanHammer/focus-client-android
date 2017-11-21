package edu.usc.csci310.focus.focus.presentation;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import edu.usc.csci310.focus.focus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementFragment extends Fragment {
    private GridView gridView;
    private AchievementsAdapter achievementsAdapter;

    public AchievementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_achievement, container, false);
        gridView = v.findViewById(R.id.achievements_grid_view);
        return v;
    }

    private class AchievementsAdapter extends BaseAdapter{

        private AchievementsAdapter(Context context, Achievements)
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
