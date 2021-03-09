package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.codepath_project.R;

import java.util.Date;

public class EditTaskFragment extends Fragment {
    private EditText etEdit;
    private EditText etDate;
    private Switch switchPublic;
    private CalendarView calView;
    private Button btnSave;

    public EditTaskFragment() {
        // Required empty public constructor
    }

    public static EditTaskFragment newInstance(String description, Date dueDate, boolean publicity)
    {
        EditTaskFragment taskFragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putString("description", description);
        args.putSerializable("dueDate", dueDate);
        args.putBoolean("publicity", publicity);
        return taskFragment;
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
        etEdit.setText(getArguments().getString("description"));
        etDate.setText(getArguments().getSerializable("dueDate").toString());
        switchPublic.setChecked(getArguments().getBoolean("publicity"));


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}