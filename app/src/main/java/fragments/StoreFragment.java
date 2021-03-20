package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.codepath_project.StoreItem;
import com.example.codepath_project.StoreItemAdapter;
import com.example.codepath_project.R;

import java.util.Arrays;
import java.util.List;

//https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
public class StoreFragment extends Fragment implements StoreItemAdapter.ItemClickListener{
    StoreItemAdapter adapter;
    private RecyclerView rvNumbers;
    private TextView tvUserPointTotal;


    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance(int points) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putInt("points", points);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();
        int points = bundle.getInt("points", 0);
        tvUserPointTotal = view.findViewById(R.id.tvUserPointTotal);
        tvUserPointTotal.setText(String.valueOf(points));

        rvNumbers = view.findViewById(R.id.rvNumbers);

        // data to populate the RecyclerView with
        StoreItem[] sData = {
                            new StoreItem(R.drawable.bg_outdoor_winter, 23, "Winterland"),
                            new StoreItem(R.drawable.bg_outdoor_abstract, 20, "Abstract")};

        // set up the RecyclerView
        int numberOfColumns = 2;
        rvNumbers.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        adapter = new StoreItemAdapter(getContext(), Arrays.asList(sData));
        adapter.setClickListener(this);
        rvNumbers.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }

}
