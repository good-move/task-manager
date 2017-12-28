package ru.nsu.ccfit.boltava.taskmanager.model;

import java.util.ArrayList;

/**
 * Created by alexey on 20.10.17.
 */


/**
 * TaskGroup represents a list of Tasks, which have something in common as intended by user.
 *
 */
public class TaskGroup {

    private int id;
    private ArrayList<Task> tasks = new ArrayList<>();
    private String title;
    private String keyColor;

    public TaskGroup(String title, String keyColor) {
        this.title = title;
        this.keyColor = keyColor;
    }

    public TaskGroup(int id, String title, String keyColor) {
        this.id = id;
        this.title = title;
        this.keyColor = keyColor;
    }

    public String getKeyColor() {
        return keyColor;
    }

    public void setKeyColor(String keyColor) {
        this.keyColor = keyColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return tasks.size();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public int getId() {
        return id;
    }
}
