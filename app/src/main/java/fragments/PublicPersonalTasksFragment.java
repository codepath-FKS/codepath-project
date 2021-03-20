package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codepath_project.BuddyTasksAdapter;
import com.example.codepath_project.PublicPersonalTasksAdapter;
import com.example.codepath_project.R;
import com.example.codepath_project.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PublicPersonalTasksFragment  extends Fragment {
    private List<Task> personalTasks;
    private RecyclerView rvPersonalPublicTasks;
    private PublicPersonalTasksAdapter personaladapter;

    public PublicPersonalTasksFragment() {
        // Required empty public constructor
    }

    public static PublicPersonalTasksFragment newInstance() {
        PublicPersonalTasksFragment fragment = new PublicPersonalTasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public_personal_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPersonalPublicTasks = view.findViewById(R.id.rvPersonalPublicTasks);

        PublicPersonalTasksAdapter.OnClickListener onClickListener = new PublicPersonalTasksAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("PersonalBuddyFragment","Single click at position " + position);
                setIncomplete(personalTasks.get(position));
                personalTasks.remove(position);
                Toast.makeText(getContext(),"Item marked incomplete", Toast.LENGTH_SHORT).show();
            }
        };

        personalTasks = new ArrayList<>();
        personaladapter = new PublicPersonalTasksAdapter(getContext(), personalTasks, onClickListener);

        // set the adapter on the recycler view
        rvPersonalPublicTasks.setAdapter(personaladapter);

        // set the layout manager on the recycler view
        rvPersonalPublicTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchPersonalTasks();
    }

    private void fetchPersonalTasks() {

        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_AUTHOR);
        query.whereEqualTo(Task.KEY_AUTHOR, ParseUser.getCurrentUser());
        query.whereEqualTo("approved", false);
        query.whereEqualTo("complete", true);
        query.whereEqualTo("public", true);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if(e != null){
                    Log.e("PersonalBuddyFragment","Issue with getting tasks", e);
                    return;
                }

                personalTasks.addAll(tasks);
                personaladapter.notifyDataSetChanged();
                //Log.d("BuddyFragment","wee snaw");
            }
        });
    }

    public void setIncomplete(Task task) {

        Log.d("PersonalBuddyFragment", "going to set task completed to false");
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.getInBackground(task.getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was fetched
                object.setCompleted(false);
            } else {
                //Something went wrong
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        task.setCompleted(false);
        task.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("PersonalBuddyFragment", "Error while saving task", e);
                }
            }
        });
        personaladapter.notifyDataSetChanged();
        Log.d("PersonalBuddyFragment", "set task completed to false");


    }
}
