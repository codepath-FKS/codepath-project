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
import android.widget.Toast;

import com.example.codepath_project.BuddyTasksAdapter;
import com.example.codepath_project.R;
import com.example.codepath_project.Task;
import com.example.codepath_project.PublicPersonalTasksAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnpersonal = view.findViewById(R.id.btnpersonalpublic);
        btnbuddy = view.findViewById(R.id.btnBuddyTasks);

        btnpersonal.setOnClickListener(v -> {
            Fragment fragment = new PublicPersonalTasksFragment().newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("buddy_fragment").commit();
        });

        btnbuddy.setOnClickListener(v -> {
            Fragment fragment = new PublicBuddyTasksFragment().newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack("buddy_fragment").commit();

        });

    }

}
