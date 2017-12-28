package ru.nsu.ccfit.boltava.taskmanager.model;

import java.security.Timestamp;

/**
 * Created by alexey on 20.10.17.
 */


/**
 * Task class represents a Task which user can set and wants to complete
 */
public class Task {

    private int id;
    private String title;
    private String description;
    private TaskPriority priority;
    private Timestamp deadlineDate;
    private boolean completed;
    private int taskGroupId;


    private Task(String title, int taskGroupId) {
        this.title = title;
        this.taskGroupId = taskGroupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static TaskBuilder getTaskBuilder(String taskTitle, int taskGroupId) {
        return new TaskBuilder(taskTitle, taskGroupId);
    }

    public int getTaskGroupId() {
        return taskGroupId;
    }

    public int getId() {
        return id;
    }

    public enum TaskPriority {
        DEFAULT(0),
        LOW(1),
        MEDIUM(2),
        HIGH(3);

        private final int value;
        TaskPriority(int value) {
            this.value = value;
        }

        public int toInt() {
            return value;
        }
    }

    public static class TaskBuilder {

        private Task task;

        public TaskBuilder(String title, int groupId) {
            task = new Task(title, groupId);
            task.setPriority(TaskPriority.DEFAULT);
            task.setDescription("");
        }

        public TaskBuilder setDescription(String description) {
            if (description != null) {
                task.setDescription(description);
            }
            return this;
        }

        public TaskBuilder setPriority(TaskPriority priority) {
            if (priority != null) {
                task.setPriority(priority);
            }
            return this;
        }

        public TaskBuilder setId(int id) {
            task.id = id;
            return this;
        }

        public Task createTask() {
            return this.task;
        }

    }

}
