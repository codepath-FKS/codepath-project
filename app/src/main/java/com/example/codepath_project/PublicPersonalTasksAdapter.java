package com.example.codepath_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// THIS HAS BEEN REPLACED BY ADVTASKSADAPTER - Faiza

public class PublicPersonalTasksAdapter extends RecyclerView.Adapter<PublicPersonalTasksAdapter.ViewHolder> {

    private Context context;
    private List<Task> tasks;
    OnClickListener clickListener;

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public PublicPersonalTasksAdapter(Context context, List<Task> tasks, OnClickListener clickListener) {
        this.context = context;
        this.tasks = tasks;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.personal_item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicPersonalTasksAdapter.ViewHolder holder, int position) {
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
