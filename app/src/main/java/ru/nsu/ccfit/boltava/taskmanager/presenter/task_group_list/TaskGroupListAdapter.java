package ru.nsu.ccfit.boltava.taskmanager.presenter.task_group_list;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.nsu.ccfit.boltava.taskmanager.R;
import ru.nsu.ccfit.boltava.taskmanager.model.TaskGroup;

/**
 * Created by alexey on 20.10.17.
 */

public class TaskGroupListAdapter extends RecyclerView.Adapter<TaskGroupListAdapter.GroupListViewHolder> {

    private TaskGroup[] taskGroups;
    private TaskGroupListItemClickListener clickListener;
    private GroupListItemLongClickListener longClickListener;

    public TaskGroupListAdapter(TaskGroup[] taskGroups) {
        if (taskGroups == null) throw new IllegalArgumentException("Task groups cannot be null");
        this.taskGroups = taskGroups;
    }

    @Override
    public GroupListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_group_list_item, parent, false);
        return new GroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupListViewHolder holder, int position) {
        holder.bind(taskGroups[position]);
    }

    @Override
    public int getItemCount() {
        return taskGroups.length;
    }

    public void setClickListener(TaskGroupListItemClickListener listener) {
        clickListener = listener;
    }

    public void removeClickListener() {
        clickListener = null;
    }

    public void setLongClickListener(GroupListItemLongClickListener listener) {
        longClickListener = listener;
    }

    public void removeLongClickListener() {
        longClickListener = null;
    }

    public void setItemEditable(int position, boolean isEditable) {

    }

    class GroupListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TaskGroup taskGroup;
        TextView groupTitle;
        TextView groupSize;

        GroupListViewHolder(View itemView) {
            super(itemView);
            groupTitle = itemView.findViewById(R.id.tv_group_title);
            groupSize = itemView.findViewById(R.id.tv_group_size);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(TaskGroup taskGroup) {
            this.taskGroup = taskGroup;
            groupTitle.setText(taskGroup.getTitle());
            groupSize.setText(String.valueOf(taskGroup.getSize()));
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClicked(this.taskGroup, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.onItemLongClicked(this, getAdapterPosition());
            return false;
        }
    }


    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable divider;

        public DividerItemDecoration(Context context) {
            divider = ContextCompat.getDrawable(context, R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }

    }


    public interface TaskGroupListItemClickListener {
        void onItemClicked(TaskGroup taskGroup, int position);
    }

    public interface GroupListItemLongClickListener {
        void onItemLongClicked(GroupListViewHolder viewHolder, int position);
    }

}
