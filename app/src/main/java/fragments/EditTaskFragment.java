package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.codepath_project.R;
import com.example.codepath_project.Task;

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
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-DD-YYYY");

    public EditTaskFragment() {
        // Required empty public constructor
    }

    public static EditTaskFragment newInstance(Task task) {
        EditTaskFragment fragmentDemo = new EditTaskFragment();
        Bundle args = new Bundle();
       /* args.putString("description", task.getDescription());
        args.putString("dueDate", task.getDueDate().toString());
        args.putBoolean("publicity", task.getPublicity());

        */
        args.putParcelable("task", task);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
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
            etDate.setText(task.getDueDate().toString());
            calView.setDate(Long.parseLong(task.getDueDate().toString()));
        }
        switchPublic.setChecked(task.getPublicity());

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String dateStr = String.format("%02d-%02d-%04d", month, dayOfMonth, year);
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
               /* ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
                query.getInBackground(getArguments().getString(task.getObjectId()), (task, e) -> {
                    if (e == null) {
                        task.put(Task.KEY_DESCRIPTION, etEdit.getText());
                        if(etDate.getText() != null)
                        {
                            task.put(Task.KEY_DUEDATE, etDate.getText());
                        }
                        task.put(Task.KEY_PUBLICITY, switchPublic.isChecked());
                        task.saveInBackground();
                    } else {
                        // something went wrong
                        Log.e(TAG, "Error saving edits to task");
                    }

                */
                task.setDescription(etEdit.getText().toString());
                if(etDate.getText() != null) {
                    try {
                        task.setDueDate(dateFormat.parse(etDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                task.setPublicity(switchPublic.isChecked());
            }
        });
    }
}