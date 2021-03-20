package com.example.codepath_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDoNothing;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
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
        public TextView txtTask;
        public FrameLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTask = itemView.findViewById(R.id.cbTask);
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
            // TODO: set color according to Public/Private/Rejected
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
        /*
        final SwipeableItemState swipeState = holder.getSwipeState();

        if (swipeState.isUpdated()) {
            int bgResId;
            if (swipeState.isActive()) {
                bgResId = R.drawable.ic_check_circle;
            } else if (swipeState.isSwiping()) {
                bgResId = R.drawable.ic_check_circle;
            } else {
                bgResId = R.drawable.ic_launcher_foreground;
            }
            holder.container.setBackgroundResource(bgResId);
        }

         */

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
            // Toast.makeText(v.getContext(),"Item was clicked in adapter", Toast.LENGTH_SHORT).show();
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
            adapter.notifyItemRemoved(position);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if(adapter.eventListener != null)
            {
                // delete on Parse
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


}