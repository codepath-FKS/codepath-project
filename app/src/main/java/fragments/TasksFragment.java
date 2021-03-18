package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.codepath_project.AdvTasksAdapter;
import com.example.codepath_project.MainActivity;
import com.example.codepath_project.R;
import com.example.codepath_project.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {
    public static final String TAG ="TasksFragment";
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewSwipeManager swipeManager;
    private RecyclerViewTouchActionGuardManager actionGuardManager;
    private RecyclerView.Adapter wrappedAdapter;
    private RecyclerView rvTasks;
    private AdvTasksAdapter adapter;
    FloatingActionButton btnAdd;
    EditText etTask;
    private List<Task> allTasks;


    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTasks = view.findViewById(R.id.rvTasks);
        btnAdd = view.findViewById(R.id.faBtnAdd);
        etTask = view.findViewById(R.id.etAddTask);
        layoutManager = new LinearLayoutManager(requireContext(),  RecyclerView.VERTICAL, false);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        actionGuardManager = new RecyclerViewTouchActionGuardManager();
        actionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        actionGuardManager.setEnabled(true);

        swipeManager = new RecyclerViewSwipeManager();

        allTasks = new ArrayList<>();
        adapter = new AdvTasksAdapter(getContext(), allTasks);

        adapter.setEventListener(new AdvTasksAdapter.EventListener() {
            @Override
            public void onItemRemoved(int position) {
                // swipe doesn't work so this doesn't happen :(
                Task task = allTasks.get(position);
                if(!task.isPublic())
                {
                    deleteTask(task);
                    allTasks.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                else
                {
                    task.setComplete(true);
                    adapter.notifyItemChanged(position);
                }
                // TODO: give food for pet if private, else put task in buddies screen for verification

                Toast.makeText(getContext(),"Item was completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemViewClicked(View v) {
                int position = rvTasks.getChildAdapterPosition(v);
                if(position != rvTasks.NO_POSITION)
                {
                    Task task = allTasks.get(position);
                    ((MainActivity)getActivity()).onTaskClicked(task);
                    Log.i(TAG, "onItemViewClicked position is " + position);
                }
                else
                {
                    Log.i(TAG, "onItemViewClicked position is null");
                }
            }
        });

        wrappedAdapter = swipeManager.createWrappedAdapter(adapter);
        // set the adapter on the recycler view
        rvTasks.setAdapter(wrappedAdapter);
        // set the layout manager on the recycler view
        rvTasks.setLayoutManager(layoutManager);
        ((SimpleItemAnimator) rvTasks.getItemAnimator()).setSupportsChangeAnimations(false);
        swipeManager.attachRecyclerView(rvTasks);
        fetchTasks();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).launchCreateTask();
                /*
                Task task = new Task();
                task.setDescription(etTask.getText().toString());
                task.setAuthor(ParseUser.getCurrentUser());
                // add item to model
                allTasks.add(task);
                adapter.notifyDataSetChanged();
                etTask.setText("");
                Toast.makeText(v.getContext(),"Task was added", Toast.LENGTH_SHORT).show();
                task.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.e(TAG, "Error while saving task", e);
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                 */
            }
        });

    }

    private void editTask(int position) {
        EditTaskFragment childFragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putString("task_id", allTasks.get(position).getObjectId());
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, childFragment).commit();
    }

    private void fetchTasks() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        // get user's uncompleted tasks
        query.whereEqualTo(Task.KEY_AUTHOR, ParseUser.getCurrentUser());
        query.whereNotEqualTo(Task.KEY_COMPLETE, true);
        query.orderByAscending(Task.KEY_DUEDATE);

        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if(e != null){
                    Log.e(TAG,"Issue with getting tasks", e);
                    return;
                }

                allTasks.addAll(tasks);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void deleteTask(Task task) {

        ParseQuery<Task> query = ParseQuery.getQuery("Task");

        // Retrieve the object by id
        query.getInBackground(task.getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was fetched
                //Deletes the fetched ParseObject from the database
                object.deleteInBackground(e2 -> {
                    if(e2==null){
                        Log.i(TAG, "Delete Successful");
                    }
                    else
                        {
                        //Something went wrong while deleting the Object
                        Toast.makeText(getContext(), "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                //Something went wrong
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
