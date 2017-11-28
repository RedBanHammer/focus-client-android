package edu.usc.csci310.focus.focus.presentation;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.usc.csci310.focus.focus.R;
import edu.usc.csci310.focus.focus.dataobjects.AchievementStat;
import edu.usc.csci310.focus.focus.managers.StatsManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementFragment extends Fragment {
    public static final String ACHIEVEMENTS = "achievements";
    private GridView gridView;
    private TextView dayCount, weekCount, monthCount, yearCount;
    private AchievementsAdapter achievementsAdapter;
    private ArrayList<AchievementStat> achievementStats;
    private int [] imageIds = {R.drawable.blue_badge, R.drawable.brown_badge, R.drawable.green_badge,
            R.drawable.light_green_badge, R.drawable.magenta_badge, R.drawable.pink_badge,
            R.drawable.red_badge, R.drawable.sky_blue_badge, R.drawable.yellow_badge, R.drawable.blank_achievement};
    private static final int NUM_ACHIEVEMENTS = 9;
    int [] dayCounts = {5, 7, 14, 21, 30, 60, 90, 365, 730};
    public AchievementFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(ArrayList<AchievementStat> achievementStats) {
        Bundle args = new Bundle();
        args.putSerializable(ACHIEVEMENTS, achievementStats);
        AchievementFragment achievementFragment = new AchievementFragment();
        achievementFragment.setArguments(args);
        return achievementFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        achievementStats = (ArrayList<AchievementStat>) getArguments().getSerializable(ACHIEVEMENTS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_achievement, container, false);
        gridView = v.findViewById(R.id.achievements_grid_view);
        dayCount = v.findViewById(R.id.day_counter_text);
        weekCount = v.findViewById(R.id.week_counter_text);
        monthCount = v.findViewById(R.id.month_counter_text);
        yearCount = v.findViewById(R.id.year_counter_text);
        int totalDays = StatsManager.getDefaultManager().getDailyStreak();
        int days =0; int weeks=0; int months=0; int years=0;
        while(totalDays-365>=0){
            totalDays -=365;
            years++;
        }
        while (totalDays-30>=0){
            totalDays -=30;
            months++;
        }
        while (totalDays-7>=0){
            totalDays -=7;
            weeks++;
        }
        days = totalDays;
        dayCount.setText(Integer.toString(days));
        weekCount.setText(Integer.toString(weeks));
        monthCount.setText(Integer.toString(months));
        yearCount.setText(Integer.toString(years));

        achievementsAdapter = new AchievementsAdapter(getContext(), achievementStats);
        gridView.setAdapter(achievementsAdapter);
        return v;
    }



    private class AchievementsAdapter extends BaseAdapter{
        private Context context;
        private List<AchievementStat> achievementsList;

        private AchievementsAdapter(Context context, List<AchievementStat> achievementsList){
            this.context = context;
            this.achievementsList = achievementsList;
        }

        @Override
        public int getCount() {
            return achievementsList.size();
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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;

            final AchievementStat achievementStat = achievementsList.get(position);

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder = null; // view lookup cache stored in tag
            //create new row view if null
            if (view == null) {
                // If there's no view to re-use, inflate a brand new view for row
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.achievement_grid_item, viewGroup, false);
                viewHolder = new ViewHolder(view);
                // Cache the viewHolder object inside the fresh view
                view.setTag(viewHolder);
            } else {
                // View is being recycled, retrieve the viewHolder object from tag
                viewHolder = (ViewHolder) view.getTag();
            }
            // Populate the data from the data object via the viewHolder object
            // into the template view.
            viewHolder.achievementName.setText(achievementStat.getName());
            viewHolder.achievementDesc.setText(achievementStat.getDescription());

            if (StatsManager.getDefaultManager().getDailyStreak() >= dayCounts[position]){
                viewHolder.achievementImage.setImageResource(imageIds[position]);
            }else{
                viewHolder.achievementImage.setImageResource(imageIds[NUM_ACHIEVEMENTS]);
                viewHolder.achievementImage.setEnabled(false);
            }

            viewHolder.achievementImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //this opens a sharing page for apps that are on the emulator
                    //if there is only 1 app it automatically chooses to share.
                    final String MESSAGE = achievementStat.getDescription();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);

                    // change the type of data you need to share,
                    // for image use "image/*"
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, MESSAGE);
                    startActivity(Intent.createChooser(intent, "Share"));
                }
            });

            // Return the completed view to render on screen
            return view;
        }
        // View lookup cache that populates the gridView
        private class ViewHolder {
            TextView achievementName;
            TextView achievementDesc;
            ImageButton achievementImage;

            private ViewHolder(View v) {
                achievementName = v.findViewById(R.id.achievement_name);
                achievementDesc = v.findViewById(R.id.achievement_desc);
                achievementImage = v.findViewById(R.id.achievement_image);
            }
        }
    }
}
