package com.example.codepath_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDoNothing;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdvTasksAdapter extends RecyclerView.Adapter<AdvTasksAdapter.ViewHolder>
        implements SwipeableItemAdapter<AdvTasksAdapter.ViewHolder> {

    private static final String TAG = "advTasksAdapter";

    private interface Swipeable extends SwipeableItemConstants { }

    private Context context;
    private List<Task> tasks;
    private EventListener eventListener;
    private View.OnClickListener itemViewOnClickListener;
    private View.OnClickListener swipeViewOnClickListener;


    public interface EventListener {
        void onItemRemoved(int position);
        void onItemVerify(int position);
        void onItemViewClicked(View v);
    }


    public static class ViewHolder extends AbstractSwipeableItemViewHolder
    {
        public TextView txtTask;
        public TextView txtType;
        public TextView tvDueDate;
        public ImageView ivType;
        public FrameLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTask = itemView.findViewById(R.id.cbTask);
            txtType = itemView.findViewById(R.id.tvTaskCategory);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            ivType = itemView.findViewById(R.id.ivCategoryMarker);
            container = itemView.findViewById(R.id.frameLayout2);
        }

        @NonNull
        @Override
        public View getSwipeableContainerView() {
            return container;
        }

        public void bind(Task task) {
            // bind the task data to the task
            txtTask.setText(task.getDescription());
            if(task.getDueDate() != null)
            {
                tvDueDate.setText(formatDate(task.getDueDate()));
            }

            String type;
            int color;
            if(task.getRejected() == false)
            {
                type = task.isPublic() ? "Public" : "Private";
                color = task.isPublic() ? ContextCompat.getColor(container.getContext(), R.color.colorPurple) :
                        ContextCompat.getColor(container.getContext(), R.color.colorPrimary);
            }
            else
            {
                type = "Rejected";
                color = ContextCompat.getColor(container.getContext(), R.color.colorError);
            }
            txtType.setText(type);
            ivType.setColorFilter(color);
        }

        private String formatDate(Date dueDate) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("E");
            return dateFormat.format(dueDate);
        }
    }


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_task, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.container.setOnClickListener(swipeViewOnClickListener);
        holder.bind(task);
    }


    public AdvTasksAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        itemViewOnClickListener = v -> onItemViewClick(v);
        swipeViewOnClickListener = v -> onSwipeableViewContainerClick(v);
        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }


    private void onItemViewClick(View v) {
        if(eventListener != null)
        {
            // open editTask fragment
            eventListener.onItemViewClicked(v);
        }
    }


    private void onSwipeableViewContainerClick(View v)
    {
        if (eventListener != null) {
            eventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v));
        }
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public long getItemId(int position){
        return tasks.get(position).getLongId();
   }

    @Override
    public int onGetSwipeReactionType(@NonNull ViewHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_RIGHT;
    }

    @Override
    public void onSwipeItemStarted(@NonNull ViewHolder holder, int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onSetSwipeBackground(@NonNull ViewHolder holder, int position, int type) {
        int bgRes = 0;
        /*
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_right;
                break;
        }
         */
        holder.itemView.setBackgroundResource(bgRes);
    }


    @Nullable
    @Override
    public SwipeResultAction onSwipeItem(@NonNull ViewHolder holder, int position, int result) {

        if (result == Swipeable.RESULT_SWIPED_RIGHT) {
            return new SwipeRightResultAction(this, position) {
            };
        } else {
            return new SwipeResultActionDoNothing();
        }
    }


    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }


    public EventListener getEventListener()
    {
        return eventListener;
    }


    private static class SwipeRightResultAction extends SwipeResultActionRemoveItem {
        private AdvTasksAdapter adapter;
        private int position;

        SwipeRightResultAction(AdvTasksAdapter adapter, int position){
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            if(adapter.tasks.get(position).isPublic() == false)
            {
                adapter.notifyItemRemoved(position);
            }
            else
            {
                adapter.notifyItemChanged(position);
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if(adapter.eventListener != null)
            {
                if(adapter.tasks.get(position).isPublic() == false)
                {
                    adapter.eventListener.onItemRemoved(position);
                }
                else
                {
                    adapter.eventListener.onItemVerify(position);
                }
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            adapter = null;
        }
    }


}