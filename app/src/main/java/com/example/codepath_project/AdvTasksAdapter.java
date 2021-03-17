package com.example.codepath_project;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDoNothing;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;

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
        void onItemViewClicked(View v);
    }

    public static class ViewHolder extends AbstractSwipeableItemViewHolder
    {
        private TextView txtTask;
        View containerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTask = itemView.findViewById(R.id.cbTask);
            containerView = itemView.findViewById(R.id.divider);
        }

        @NonNull
        @Override
        public View getSwipeableContainerView() {
            return containerView;
        }

        public void bind(Task task) {
            // bind the task data to the task
            txtTask.setText(task.getDescription());
        }
    }

    public AdvTasksAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        itemViewOnClickListener = v -> onItemViewClick(v);
        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);

    }

    private void onItemViewClick(View v) {
        if(eventListener != null)
        {
            // open editTask fragment
            eventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v));
            Toast.makeText(v.getContext(),"Item was clicked in adapter", Toast.LENGTH_SHORT).show();
        }
    }


    @NonNull
    @Override
    public AdvTasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new AdvTasksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.itemView.setOnClickListener(itemViewOnClickListener);
        holder.containerView.setOnClickListener(swipeViewOnClickListener);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public String getTaskId(int position){
        return tasks.get(position).getObjectId();
   }

    @Override
    public int onGetSwipeReactionType(@NonNull ViewHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_LEFT;
    }

    @Override
    public void onSwipeItemStarted(@NonNull ViewHolder holder, int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onSetSwipeBackground(@NonNull ViewHolder holder, int position, int type) {
        // You can set background color/resource to holder.itemView.
        // The argument "type" can be one of the followings;
        // - Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND
        // - Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND
        // (- Swipeable.DRAWABLE_SWIPE_UP_BACKGROUND)
        // (- Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND)
        // (- Swipeable.DRAWABLE_SWIPE_DOWN_BACKGROUND)

        if (type == Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND) {
            holder.itemView.setBackgroundColor(Color.YELLOW);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Nullable
    @Override
    public SwipeResultAction onSwipeItem(@NonNull ViewHolder holder, int position, int result) {
        // Return sub class of the SwipeResultAction.
        //
        // Available base (abstract) classes are;
        // - SwipeResultActionDefault
        // - SwipeResultActionMoveToSwipedDirection
        // - SwipeResultActionRemoveItem
        // - SwipeResultActionDoNothing

        // The argument "result" can be one of the followings;
        //
        // - Swipeable.RESULT_CANCELED
        // - Swipeable.RESULT_SWIPED_LEFT
        // (- Swipeable.RESULT_SWIPED_UP)
        // (- Swipeable.RESULT_SWIPED_RIGHT)
        // (- Swipeable.RESULT_SWIPED_DOWN)

        if (result == Swipeable.RESULT_SWIPED_RIGHT) {
            return new SwipeResultActionMoveToSwipedDirection() {
                // Optionally, you can override these three methods
                // - void onPerformAction()
                // - void onSlideAnimationEnd()
                // - void onCleanUp()
            };
        }
        else {
            return new SwipeResultActionDoNothing();
        }
    }

    private static class SwipeRightAction extends SwipeResultActionMoveToSwipedDirection {
        private AdvTasksAdapter adapter;
        private int position;

        SwipeRightAction(AdvTasksAdapter adapter, int position){
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            adapter.tasks.remove(position);
            adapter.notifyItemRemoved(position);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            if(adapter.eventListener != null)
            {
                adapter.eventListener.onItemRemoved(position);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            adapter = null;
        }
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public EventListener getEventListener()
    {
        return eventListener;
    }
}