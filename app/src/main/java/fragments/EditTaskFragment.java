package fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.codepath_project.R;

public class EditTaskFragment extends Fragment {
    private EditText etEdit;
    private Switch switchPublic;
    private CalendarView calView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEdit = view.findViewById(R.id.etEdit);
        switchPublic = view.findViewById(R.id.switchPublic);
        calView = view.findViewById(R.id.calendarView);

    }
}