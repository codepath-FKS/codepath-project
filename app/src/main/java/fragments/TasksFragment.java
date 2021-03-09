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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.codepath_project.R;
import com.example.codepath_project.Task;
import com.example.codepath_project.TasksAdapter;
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
public class TasksFragment extends Fragment {
    public static final String TAG ="TasksFragment";
    private RecyclerView rvTasks;
    private TasksAdapter adapter;
    ImageButton btnAdd;
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
        btnAdd = view.findViewById(R.id.btnAddTask);
        etTask = view.findViewById(R.id.etAddTask);

        TasksAdapter.OnClickListener onClickListener = new TasksAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("TasksFragment","Single click at position " + position);
                deleteTask(allTasks.get(position));
                allTasks.remove(position);
                // notify adapter
                adapter.notifyItemRemoved(position);
                Toast.makeText(getContext(),"Item was completed", Toast.LENGTH_SHORT).show();
                // TODO: give food for pet if private, else put task in buddies screen for verification

                // edit task test

            }
        };

        allTasks = new ArrayList<>();
        adapter = new TasksAdapter(getContext(), allTasks, onClickListener);
        // set the adapter on the recycler view
        rvTasks.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchTasks();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setDescription(etTask.getText().toString());
                task.setAuthor(ParseUser.getCurrentUser());
                // add item to model
                allTasks.add(task);
                adapter.notifyDataSetChanged();
                etTask.setText("");
                Toast.makeText(v.getContext(),"Item was added", Toast.LENGTH_SHORT).show();
                task.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.e(TAG, "Error while saving task", e);
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void fetchTasks() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.include(Task.KEY_AUTHOR);
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
                        Toast.makeText(getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
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
