package fragments;

import android.os.Bundle;
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

import com.example.codepath_project.User;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateTaskFragment extends Fragment {

    private EditText etAddTask;
    private EditText etDate;
    private Switch switchPublic;
    private CalendarView calView;
    private Button btnAdd;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    public CreateTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etAddTask = view.findViewById(R.id.etAddTask);
        etDate = view.findViewById(R.id.etDate);
        switchPublic = view.findViewById(R.id.switchPublic);
        calView = view.findViewById(R.id.calendarView);
        btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setAuthor(ParseUser.getCurrentUser());
                task.setDescription(etAddTask.getText().toString());
                task.setPublic(switchPublic.isChecked());
                task.setComplete(false);
                task.setApproved(false);
                // try to save date
                if(etDate.getText() != null)
                {
                    try {
                        task.setDueDate(dateFormat.parse(etDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                task.saveInBackground();
                // lowering the users health after task creation
                // Todo: change the other instances of ParseUser to simply be the custom User class

                User.setHealth(User.getHealth()-5);
                Toast.makeText(v.getContext(),"Task was added!", Toast.LENGTH_SHORT).show();
                // launch taskFragment
                ((MainActivity)getActivity()).onTaskUpdated();
            }
        });

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
    }


}