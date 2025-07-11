package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeStrategy implements Strategy {
    public void addTask(List<Server> list, Task t1) {
        int minIndex;
        int minWaiting;
        if (list.isEmpty()) {
            return;
        }
        else {
            minWaiting = list.get(0).getWaitingPeriod().get();
            minIndex = 0;
        }

        int currentIndex = 0;
        for (Server s : list) {
            int currentWaiting = s.getWaitingPeriod().get();
            if (currentWaiting < minWaiting) {
                minWaiting = currentWaiting;
                minIndex = currentIndex;
            }
            currentIndex++;
        }

        list.get(minIndex).addTask(t1);
    }

}

