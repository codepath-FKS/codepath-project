package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.codepath_project.MainActivity;
import com.example.codepath_project.R;
import com.example.codepath_project.Task;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditTaskFragment extends Fragment {
    final String TAG = "EditTaskFragment";
    private EditText etEdit;
    private EditText etDate;
    private Switch switchPublic;
    private CalendarView calView;
    private Button btnSave;
    private Task task;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    public EditTaskFragment() {
        // Required empty public constructor
    }

    public static EditTaskFragment newInstance(Task task) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable("task", task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEdit = view.findViewById(R.id.etEdit);
        etDate = view.findViewById(R.id.etDate);
        switchPublic = view.findViewById(R.id.switchPublic);
        calView = view.findViewById(R.id.calendarView);
        btnSave = view.findViewById(R.id.btnSave);

        // set task's previous values
        task = getArguments().getParcelable("task");
        etEdit.setText(task.getDescription());

        if(task.getDueDate() != null)
        {
            // 18 Mar 2021 at 07:00:00 UTC
            String dateStr = task.getDueDate().toString();
            etDate.setText(dateStr);
            calView.setDate(task.getDueDate().getTime());

        }
        switchPublic.setChecked(task.isPublic());

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String dateStr = String.format("%02d-%02d-%04d", month + 1, dayOfMonth, year);
                etDate.setText(dateStr);
                try {
                    Date date = dateFormat.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
                query.getInBackground(task.getObjectId(), (task, e) -> {
                    if (e == null) {
                        task.put(Task.KEY_DESCRIPTION, etEdit.getText().toString());
                        if (etDate.getText() != null) {
                            try {
                                task.put(Task.KEY_DUEDATE, dateFormat.parse(etDate.getText().toString()));
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                        }
                        task.put(Task.KEY_PUBLIC, switchPublic.isChecked());

                        task.saveInBackground();
                        Toast.makeText(v.getContext(),"Task was updated!", Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).onTaskUpdated();

                    } else {
                        // something went wrong
                        Log.e(TAG, "Error saving edits to task: " + e);
                    }
                });
            }
        });
    }

}