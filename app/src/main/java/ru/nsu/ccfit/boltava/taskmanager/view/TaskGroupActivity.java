package ru.nsu.ccfit.boltava.taskmanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.nsu.ccfit.boltava.taskmanager.R;
import ru.nsu.ccfit.boltava.taskmanager.model.Task;
import ru.nsu.ccfit.boltava.taskmanager.model.TaskGroup;
import ru.nsu.ccfit.boltava.taskmanager.presenter.task_group.TaskGroupAdapter;
import ru.nsu.ccfit.boltava.taskmanager.presenter.task_group.TaskGroupPresenter;

public class TaskGroupActivity extends AppCompatActivity
        implements TaskGroupAdapter.TaskGroupClickListener, SearchView.OnQueryTextListener {

    private RecyclerView taskList;
    private TaskGroupPresenter presenter;
    private TaskGroupAdapter adapter;
    private TaskGroup group;

    private int lastClickedItemPosition;
    private int groupId = -1;

    public static final int CREATE_TASK_REQUEST_CODE = 1;
    public static final int EDIT_TASK_REQUEST_CODE = 2;

    public static final String TASK_TITLE_EXTRA = "TASK_TITLE_EXTRA";
    public static final String TASK_DESCRIPTION_EXTRA = "TASK_DESCRIPTION_EXTRA";
    public static final String TASK_PRIORITY_EXTRA = "TASK_PRIORITY_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewTaskIntent = new Intent(TaskGroupActivity.this, TaskActivity.class);
                startActivityForResult(createNewTaskIntent, CREATE_TASK_REQUEST_CODE);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getIntExtra(TaskGroupListActivity.GROUP_ID_EXTRA, -1);
        }

        presenter = new TaskGroupPresenter(this, groupId);
        group = presenter.getTaskGroup();
        this.setTitle(group.getTitle());

        adapter = presenter.getGroupAdapter(group);
        adapter.addOnClickListener(this);

        taskList = findViewById(R.id.rv_task_list);
        taskList.setAdapter(adapter);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.addItemDecoration(new TaskGroupAdapter.DividerItemDecoration(this));

        attachSwipeListener();
    }

    private void attachSwipeListener() {
        final int dragDirections = 0;
        final int swipeDirections = ItemTouchHelper.LEFT;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                dragDirections, swipeDirections) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (presenter.deleteTask(group.getTasks().get(position))) {
                    adapter.removeTask(position);
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(taskList);
    }

    @Override
    public void onItemClicked(Task task, int position) {
        if (task == null) throw new IllegalArgumentException("Received null task");
        Intent intent = new Intent(TaskGroupActivity.this, TaskActivity.class);
        intent.putExtra(TASK_TITLE_EXTRA, task.getTitle());
        intent.putExtra(TASK_PRIORITY_EXTRA, task.getPriority());
        intent.putExtra(TASK_DESCRIPTION_EXTRA, task.getDescription());
        lastClickedItemPosition = position;
        startActivityForResult(intent, EDIT_TASK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_TASK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String taskTitle = data.getStringExtra(TASK_TITLE_EXTRA);
                String taskDescription = data.getStringExtra(TASK_DESCRIPTION_EXTRA);
                Task.TaskPriority taskPriority = (Task.TaskPriority) data.getSerializableExtra(TASK_PRIORITY_EXTRA);

                Task task = Task.getTaskBuilder(taskTitle, groupId)
                        .setPriority(taskPriority)
                        .setDescription(taskDescription)
                        .createTask();

                if (presenter.addTask(task)) {
                    adapter.appendTask(task);
                } else {
                    System.err.println("Failed to add task " + task.getTitle());
                    showErrorToast("Failed to create task :(");
                }
            } else {
                System.err.println("Task creation was cancelled");
            }
        } else if (requestCode == EDIT_TASK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String taskTitle = data.getStringExtra(TASK_TITLE_EXTRA);
                String taskDescription = data.getStringExtra(TASK_DESCRIPTION_EXTRA);
                Task.TaskPriority taskPriority = (Task.TaskPriority) data.getSerializableExtra(TASK_PRIORITY_EXTRA);

                Task task = group.getTasks().get(lastClickedItemPosition);
                task.setTitle(taskTitle);
                task.setPriority(taskPriority);
                if (presenter.updateTask(task)) {
                    adapter.updateTask(task, lastClickedItemPosition);
                } else {
                    System.err.println("Failed to update task " + task.getTitle());
                    showErrorToast("Failed to update task :(");
                }
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_task_group, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(this);
        }

        return true;
    }

    private void showErrorToast(String msg) {
        Toast.makeText(TaskGroupActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filterTaskList(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterTaskList(newText);
        return false;
    }

    private void filterTaskList(String filterString) {
        adapter.getFilter().filter(filterString);
    }

}
