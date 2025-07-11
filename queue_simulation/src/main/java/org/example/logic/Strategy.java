package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public interface Strategy{
    public enum SelectionPolicy{
        shortest_queue,shortest_time;
    }
    public abstract void addTask(List<Server> list, Task t1);

}
