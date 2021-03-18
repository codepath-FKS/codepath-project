package com.example.codepath_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.util.List;

public class BuddyTasksAdapter extends RecyclerView.Adapter<BuddyTasksAdapter.ViewHolder> {
    private Context context;
    private List<Task> tasks;

    public BuddyTasksAdapter(Context context, List<Task> tasks) {
        Log.d("BuddyFragment","ho ho ho");
        this.context = context;
        this.tasks = tasks;
        Log.d("BuddyFragment","no no no");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Log.d("BuddyFragment","whats wrong :(");
        View view = LayoutInflater.from(context).inflate(R.layout.item_buddy_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("BuddyFragment","plz sir");
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {

        return tasks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView taskdesc;
        private Button btnApprove;
        private Button btnDeny;
        private ImageView ivpostimg;
        private ImageView ivpfp;
        private TextView tvusername;
        private TextView tvCreationDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("BuddyFragment","View holder buddy");
            taskdesc = itemView.findViewById(R.id.taskdesc);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnDeny = itemView.findViewById(R.id.btnDeny);
            ivpostimg = itemView.findViewById(R.id.ivbuddypostimg);
            ivpfp = itemView.findViewById(R.id.ivbuddypfp);
            tvusername = itemView.findViewById(R.id.tvbuddyusername);
            tvCreationDate = itemView.findViewById(R.id.tvBuddyTaskCreation);
        }

        public void bind(Task task) {
            // bind the task data to the task
            Log.d("BuddyFragment","binding buddy fragment");
            taskdesc.setText(task.getDescription());
            ParseFile image = task.getPhoto();
            if(image != null){
                Glide.with(context).load(task.getPhoto().getUrl()).into(ivpostimg);
            }
            tvusername.setText(task.getAuthor().getUsername());
            //tvCreationDate.setText(task.getCreateDate().toString());
            ParseFile image2 = task.getAuthor().getParseFile("pfp");
            if(image2 != null){
                Glide.with(context).load(task.getAuthor().getParseFile("pfp").getUrl()).into(ivpfp);
            }
            Log.d("BuddyFragment","I hope i get here");

            btnApprove.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    task.setApproval(true);
                    task.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null) {
                                Log.e("BuddyFragment", "Error while saving task", e);
                            }
                        }
                    });
                    tasks.remove(task);
                    Toast.makeText(view.getContext(),"Item approved!", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });

            btnDeny.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    task.setCompleted(false);
                    task.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null) {
                                Log.e("BuddyFragment", "Error while saving task", e);
                            }
                        }
                    });
                    tasks.remove(task);
                    Toast.makeText(view.getContext(),"Item denied!", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
