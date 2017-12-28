package ru.nsu.ccfit.boltava.taskmanager.presenter.task_group;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import ru.nsu.ccfit.boltava.taskmanager.R;
import ru.nsu.ccfit.boltava.taskmanager.model.Task;
import ru.nsu.ccfit.boltava.taskmanager.model.TaskGroup;

/**
 * Created by alexey on 20.10.17.
 */

/**
 * TaskGroupAdapter is used to display a list of user created tasks.
 */
public class TaskGroupAdapter extends RecyclerView.Adapter<TaskGroupAdapter.TaskListViewHolder>
                            implements Filterable {

    private TaskGroup taskGroup;
    private ArrayList<Task> taskList;
    private ArrayList<Task> filteredTaskList = new ArrayList<>();
    private Context context;

    private HashSet<TaskGroupClickListener> listeners = new HashSet<>();

    public TaskGroupAdapter(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
        taskList = taskGroup.getTasks();
        filteredTaskList.addAll(taskList);
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_list_item, parent, false);
        return new TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder holder, int position) {
        holder.bind(filteredTaskList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredTaskList.size();
    }

    public void addOnClickListener(TaskGroupClickListener listener) {
        this.listeners.add(listener);
    }

    public void removeOnClickListener(TaskGroupClickListener listener) {
        this.listeners.remove(listener);
    }

    private void notifyOnTaskClickListeners(final Task task, int position) {
        for (TaskGroupClickListener listener : listeners) {
            listener.onItemClicked(task, position);
        }
    }

    public void appendTask(Task task) {
        taskList.add(task);
        updateFilteredList();
        notifyItemInserted(taskList.size()-1);
    }

    public void updateTask(Task task, int position) {
        taskList.set(position, task);
        updateFilteredList();
        notifyItemChanged(position);
    }

    public void removeTask(int position) {
        taskList.remove(position);
        updateFilteredList();
        notifyItemRemoved(position);
    }

    private void updateFilteredList() {
        filteredTaskList.clear();
        filteredTaskList.addAll(taskList);
    }

    @Override
    public Filter getFilter() {
        return new TasksFilter(this, taskList);
    }

    private static class TasksFilter extends Filter {

        private TaskGroupAdapter adapter;
        ArrayList<Task> tasks;
        ArrayList<Task> filteredList = new ArrayList<>();

        TasksFilter(TaskGroupAdapter adapter, ArrayList<Task> tasks) {
            this.adapter = adapter;
            this.tasks = tasks;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(tasks);
            } else {
                for (Task task : tasks) {
                    if (task.getTitle().contains(constraint)) {
                        filteredList.add(task);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredTaskList.clear();
            adapter.filteredTaskList.addAll((ArrayList<Task>)results.values);
            adapter.notifyDataSetChanged();
        }
    }

    class TaskListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Task task;
        TextView title;
        ImageView priority;

        TaskListViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_task_title);
            priority = itemView.findViewById(R.id.iv_task_priority);
            itemView.setOnClickListener(this);
        }

        void bind(Task task) {
            final int NO_BADGE = -1;
            title.setText(task.getTitle());
            Drawable priorityBadge;
            int priorityBadgeId = NO_BADGE;
            if (task.getPriority() != null) {
                switch (task.getPriority()) {
                    case LOW:
                        priorityBadgeId = R.drawable.task_priority_badge_low;
                        break;
                    case MEDIUM:
                        priorityBadgeId = R.drawable.task_priority_badge_normal;
                        break;
                    case HIGH:
                        priorityBadgeId = R.drawable.task_priority_badge_high;
                        break;
                    case DEFAULT:
                        break;
                    default:
                        throw new IllegalStateException("Unknown task priority type: " + task.getPriority().toString());
                }
                if (priorityBadgeId != NO_BADGE) {
                    priorityBadge = ContextCompat.getDrawable(context, priorityBadgeId);
                    priority.setImageDrawable(priorityBadge);
                }
            }
        }

        @Override
        public void onClick(View v) {
            notifyOnTaskClickListeners(taskList.get(getAdapterPosition()), getAdapterPosition());
        }

    }


    /**
     * This class is used for attaching a divider line to list items
     */
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

    public interface TaskGroupClickListener {
        void onItemClicked(Task task, int position);
    }

}
