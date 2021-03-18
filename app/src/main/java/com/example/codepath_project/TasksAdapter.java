package com.example.codepath_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private Context context;
    private List<Task> tasks;
    OnClickListener clickListener;

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public TasksAdapter(Context context, List<Task> tasks, OnClickListener clickListener) {
        this.context = context;
        this.tasks = tasks;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {

        return tasks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private CheckBox cbTask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbTask = itemView.findViewById(R.id.cbTask);
            cbTask.setChecked(false);
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                    cbTask.setChecked(true);
                }
            }); */
        }

        public void bind(Task task) {
            // bind the task data to the task
            cbTask.setText(task.getDescription());
           cbTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                    cbTask.setChecked(true);
                }
            });
        }
    }
}
