package ru.nsu.ccfit.boltava.taskmanager.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ru.nsu.ccfit.boltava.taskmanager.R;
import ru.nsu.ccfit.boltava.taskmanager.model.TaskGroup;
import ru.nsu.ccfit.boltava.taskmanager.presenter.task_group_list.TaskGroupListAdapter;
import ru.nsu.ccfit.boltava.taskmanager.presenter.task_group_list.TaskGroupListPresenter;

public class TaskGroupListActivity extends AppCompatActivity
        implements TaskGroupListAdapter.TaskGroupListItemClickListener {

    public static final String GROUP_ID_EXTRA = "GROUP_ID_EXTRA";

    private static final String ACTIVITY_TITLE = "Groups";

    private TaskGroupListPresenter presenter;
    private RecyclerView taskGroupList;
    private TaskGroupListAdapter adapter;
    private FloatingActionButton createTaskGroupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_group_list);
        this.setTitle(ACTIVITY_TITLE);

        presenter = new TaskGroupListPresenter(this);
        TaskGroup[] taskGroups = presenter.getTaskGroups();
        adapter = new TaskGroupListAdapter(taskGroups);

        taskGroupList = findViewById(R.id.rv_task_group_list);
        taskGroupList.setAdapter(adapter);
        taskGroupList.setLayoutManager(new LinearLayoutManager(this));
        taskGroupList.addItemDecoration(new TaskGroupListAdapter.DividerItemDecoration(this));
        adapter.setClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_group_list, menu);

        return true;
    }

    @Override
    public void onItemClicked(TaskGroup taskGroup, int position) {
        System.out.println("Group clicked: " + taskGroup.getTitle());
        Intent intent = new Intent(TaskGroupListActivity.this, TaskGroupActivity.class);
        intent.putExtra(GROUP_ID_EXTRA, taskGroup.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

}
