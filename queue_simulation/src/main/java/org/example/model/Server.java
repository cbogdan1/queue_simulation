package org.example.model;

import org.example.logic.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Server implements Runnable {
    private final Integer id;
    private final BlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private final AtomicInteger nrOfTasks;
    private final AtomicReference<Task> currentTask = new AtomicReference<>(null);
    private final AtomicInteger totalWaitingTime = new AtomicInteger(0);
    private final AtomicInteger totalTasksProcessed = new AtomicInteger(0);

    public Server(Integer id) {
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
        nrOfTasks = new AtomicInteger(0);
        this.id = id;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Task processingTask = tasks.take();
                currentTask.set(processingTask);

                while (processingTask.getRemainingServiceTime() > 0) {
                    String processingMessage = "Processing task: (" + processingTask.getId() +
                            "," + processingTask.getArrivalTime() + "," +
                            processingTask.getRemainingServiceTime() + ") in queue " + id;

                    System.out.println(processingMessage);
                    // Logger.log(processingMessage);

                    Thread.sleep(1000);
                    processingTask.decreaseRemainingServiceTime();
                    waitingPeriod.decrementAndGet();
                }

                System.out.println("Finished task " + processingTask.getId());
                // Logger.log("Finished task " + processingTask.getId());

                nrOfTasks.decrementAndGet();
                currentTask.set(null);

                totalWaitingTime.addAndGet(processingTask.getServiceTime());
                totalTasksProcessed.incrementAndGet();
                nrOfTasks.decrementAndGet();

            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
                Logger.log("Thread interrupted.");
                Thread.currentThread().interrupt();
            }
        }
    }
    public BlockingQueue<Task> getTasks() {
        return this.tasks;
    }

    public void addTask(Task t1) {
        this.waitingPeriod.addAndGet(t1.getServiceTime());
        this.nrOfTasks.incrementAndGet();
        this.tasks.add(t1);
    }
    public AtomicInteger getWaitingPeriod() {
        return this.waitingPeriod;
    }

    public AtomicInteger getNrOfTasks() {
        return this.nrOfTasks;
    }

    public Integer getId() {
        return this.id;
    }

    public Task getCurrentTask() {
        return currentTask.get();
    }
    public int getTotalWaitingTime() {
        return totalWaitingTime.get();
    }

    public int getTotalTasksProcessed() {
        return totalTasksProcessed.get();
    }
    public Double getAvgTime() {
        int tasks = totalTasksProcessed.get();
        return tasks > 0 ? (double) totalWaitingTime.get() / tasks : 0.0;
    }
}
