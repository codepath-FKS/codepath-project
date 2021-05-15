package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.codepath_project.R;
import com.example.codepath_project.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuddyFragment extends Fragment {
    public static final String TAG ="BuddyFragment";
    private Button btnpersonal;
    private Button btnbuddy;
    private FragmentManager fragmentManager;

    public BuddyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager = (getActivity()).getSupportFragmentManager();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buddy, container, false);

    }

private ViewPager2 mViewPager;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.myPager);//Get ViewPager2 view
        mViewPager.setAdapter(new ViewPagerAdapter(getActivity()));//Attach the adapter with our ViewPagerAdapter passing the host activity

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, mViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(((ViewPagerAdapter)(mViewPager.getAdapter())).mFragmentNames[position]);//Sets tabs names as mentioned in ViewPagerAdapter fragmentNames array, this can be implemented in many different ways.
                    }
                }
        ).attach();
    }
}
