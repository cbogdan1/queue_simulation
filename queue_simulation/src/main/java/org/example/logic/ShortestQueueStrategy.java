package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy{
    public void addTask(List<Server> list, Task t1) {
        int minIndex;
        int minTasks;
        if (list.isEmpty()) {
            return;
        }
        else {
            minTasks = list.get(0).getNrOfTasks().get();
            minIndex = 0;
        }

        int currentIndex = 0;
        for (Server s : list) {
            int currentTasks = s.getNrOfTasks().get();
            if (currentTasks < minTasks) {
                minTasks = currentTasks;
                minIndex = currentIndex;
            }
            currentIndex++;
        }

        list.get(minIndex).addTask(t1);
    }

}
