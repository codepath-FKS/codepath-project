package com.example.codepath_project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import fragments.PublicBuddyTasksFragment;
import fragments.PublicPersonalTasksFragment;

//Source: https://stackoverflow.com/questions/60957775/howto-nest-viewpager2-within-a-fragment

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final Fragment[] mFragments = new Fragment[] {//Initialize fragments views
    //Fragment views are initialized like any other fragment (Extending Fragment)
            new PublicBuddyTasksFragment(),//First fragment to be displayed within the pager tab number 1
            new PublicPersonalTasksFragment(),
    };
    public final String[] mFragmentNames = new String[] {//Tabs names array
            "Buddy Tasks",
            "Your Tasks"
    };

    public ViewPagerAdapter(FragmentActivity fa){//Pager constructor receives Activity instance
        super(fa);
    }

    @Override
    public int getItemCount() {
        return mFragments.length;//Number of fragments displayed
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments[position];
    }
}