package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.codepath_project.PhotoActivity;
import com.example.codepath_project.StoreItem;
import com.example.codepath_project.StoreItemAdapter;
import com.example.codepath_project.R;
import com.example.codepath_project.StoreItemDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

//https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
public class StoreFragment extends Fragment implements StoreItemAdapter.ItemClickListener{
    StoreItemAdapter adapter;
    private RecyclerView rvNumbers;
    private TextView tvUserPointTotal;
    private int userPointTotal;
    private List<StoreItem> storeItemData = StoreItem.storeItemData();


    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance(int points, ArrayList<Integer> purchases) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putInt("points", points);
        args.putIntegerArrayList("purchases", purchases);
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
        userPointTotal = bundle.getInt("points", 0);
        tvUserPointTotal = view.findViewById(R.id.tvUserPointTotal);
        tvUserPointTotal.setText(String.valueOf(userPointTotal));

        rvNumbers = view.findViewById(R.id.rvNumbers);



        // set up the RecyclerView
        int numberOfColumns = 2;
        rvNumbers.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        adapter = new StoreItemAdapter(getContext(), this.storeItemData, bundle.getIntegerArrayList("purchases"));
        adapter.setClickListener(this);
        rvNumbers.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e("Store Fragment", "You clicked number " + adapter.getItem(position).getName() + ", which is at cell position " + position);
        Intent i = new Intent(getContext(), StoreItemDetailActivity.class);
        i.putExtra("storeItem", (Parcelable) adapter.getItem(position));
        i.putExtra("userPointTotal", userPointTotal);
        i.putExtra("itemPosition", position);

        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                StoreItem storeItem = data.getParcelableExtra("storeItem");
                adapter.setItemPurchased(storeItem.getId(), position);


            }
        }
    }

}
