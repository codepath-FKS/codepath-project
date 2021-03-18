package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.codepath_project.BuddyTasksAdapter;
import com.example.codepath_project.R;
import com.example.codepath_project.Task;
import com.example.codepath_project.TasksAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuddyFragment extends Fragment {
    public static final String TAG ="BuddyFragment";
    private RecyclerView rvTasks;
    private List<Task> allTasks;
    private BuddyTasksAdapter adapter;

    private List<Task> personalTasks;
    private RecyclerView rvPersonalPublicTasks;
    private TasksAdapter personaladapter;

    public BuddyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buddy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTasks = view.findViewById(R.id.rvBuddyTasks);
        rvPersonalPublicTasks = view.findViewById(R.id.rvPersonalPublicTasks);

        TasksAdapter.OnClickListener onClickListener = new TasksAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("TasksFragment","Single click at position " + position);
                setIncomplete(personalTasks.get(position));
                personalTasks.remove(position);
                // notify adapter
                //personaladapter.notifyItemRemoved(position);
                //personaladapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"Item marked incomplete", Toast.LENGTH_SHORT).show();
            }
        };

        allTasks = new ArrayList<>();
        adapter = new BuddyTasksAdapter(getContext(), allTasks);

        personalTasks = new ArrayList<>();
        personaladapter = new TasksAdapter(getContext(), personalTasks, onClickListener);

        // set the adapter on the recycler view
        rvTasks.setAdapter(adapter);
        rvPersonalPublicTasks.setAdapter(personaladapter);

        // set the layout manager on the recycler view
        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPersonalPublicTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchTasks();
        fetchPersonalTasks();
    }

    private void fetchTasks() {

        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_AUTHOR);
        query.whereNotEqualTo(Task.KEY_AUTHOR, ParseUser.getCurrentUser());
        query.whereEqualTo("approved", false);
        query.whereEqualTo("complete", true);
        query.whereEqualTo("public", true);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if(e != null){
                    Log.e(TAG,"Issue with getting tasks", e);
                    return;
                }

                allTasks.addAll(tasks);
                adapter.notifyDataSetChanged();
                //Log.d("BuddyFragment","wee snaw");
            }
        });
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
                    Log.e(TAG,"Issue with getting tasks", e);
                    return;
                }

                personalTasks.addAll(tasks);
                personaladapter.notifyDataSetChanged();
                //Log.d("BuddyFragment","wee snaw");
            }
        });
    }

    public void setIncomplete(Task task) {

        Log.d("TasksAdapter", "going to set task completed to false");
        ParseQuery<Task> query = ParseQuery.getQuery("Task");
        query.getInBackground(task.getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was fetched
                object.setCompleted(false);
            }else{
                //Something went wrong
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        task.setCompleted(false);
        personaladapter.notifyDataSetChanged();
        Log.d("TasksAdapter", "set task completed to false");

    }


}
