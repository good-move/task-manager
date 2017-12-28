package ru.nsu.ccfit.boltava.taskmanager.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import ru.nsu.ccfit.boltava.taskmanager.R;
import ru.nsu.ccfit.boltava.taskmanager.model.Task;

public class TaskActivity extends AppCompatActivity {

    private EditText taskTitle;
    private Task.TaskPriority taskPriority;
    private EditText taskDescription;
    private RadioGroup taskPriorities;
    private Button cancelButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setTitle("Task View");

        taskTitle = findViewById(R.id.et_task_title);
        taskDescription = findViewById(R.id.et_task_description);
        taskPriorities = findViewById(R.id.rg_task_priority);
        cancelButton = findViewById(R.id.btn_cancel);
        saveButton = findViewById(R.id.btn_save);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close current intent without any changes
                Intent cancellationIntent = new Intent();
                setResult(RESULT_CANCELED, cancellationIntent);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save current task state and close the activity
                String title = taskTitle.getText().toString();
                if (title.trim().length() == 0) {
                    Toast.makeText(TaskActivity.this, "A task must at least have a title", Toast.LENGTH_LONG).show();
                    return;
                }

                int id = taskPriorities.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.rb_low_priority:
                        taskPriority = Task.TaskPriority.LOW;
                        break;
                    case R.id.rb_medium_priority:
                        taskPriority = Task.TaskPriority.MEDIUM;
                        break;
                    case R.id.rb_high_priority:
                        taskPriority = Task.TaskPriority.HIGH;
                        break;
                }

                Intent successIntent = new Intent();
                successIntent.putExtra(TaskGroupActivity.TASK_TITLE_EXTRA, title);
                successIntent.putExtra(TaskGroupActivity.TASK_DESCRIPTION_EXTRA, taskDescription.getText().toString());
                successIntent.putExtra(TaskGroupActivity.TASK_PRIORITY_EXTRA, taskPriority);
                setResult(RESULT_OK, successIntent);
                finish();
            }
        });

        Intent intent = getIntent();
        setTaskTitle(intent, TaskGroupActivity.TASK_TITLE_EXTRA);
        setPriority(intent, TaskGroupActivity.TASK_PRIORITY_EXTRA);
        setDescription(intent, TaskGroupActivity.TASK_DESCRIPTION_EXTRA);
    }

    private void setDescription(Intent intent, String key) {
        String description = intent.getStringExtra(key);
        if (description != null) {
            taskDescription.setText(description);
        }
    }

    private void setPriority(Intent intent, String key) {
        Task.TaskPriority priority = (Task.TaskPriority) intent.getSerializableExtra(key);
        if (priority != null) {
            RadioButton button;
            switch (priority) {
                case LOW:
                    button = taskPriorities.findViewById(R.id.rb_low_priority);
                    break;
                case MEDIUM:
                    button = taskPriorities.findViewById(R.id.rb_medium_priority);
                    break;
                case HIGH:
                    button = taskPriorities.findViewById(R.id.rb_high_priority);
                    break;
                case DEFAULT:
                    // TODO: change to NONE
                    button = taskPriorities.findViewById(R.id.rb_high_priority);
                    break;
                default:
                    throw new IllegalStateException("Unknown priority " + priority.toString());
            }
            button.setChecked(true);
        }
    }

    private void setTaskTitle(Intent intent, String key) {
        String taskTitleString = intent.getStringExtra(key);
        if (taskTitleString != null) {
            taskTitle.setText(taskTitleString);
        }
    }
}
