    package org.example.logic;

    import org.example.gui.Controller;
    import org.example.gui.View;
    import org.example.model.Server;
    import org.example.model.Task;

    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;
    import java.awt.*;
    import java.util.*;
    import java.util.List;
    import java.util.concurrent.BlockingQueue;

    public class SimulationManager implements Runnable {
        // data read from UI
        public int timeLimit;// = 100; // maximum processing time - read from UI
        public int maxProcessingTime;// = 10;
        public int minProcessingTime;// = 2;
        public int maxArrivalTime;//=30;
        public int minArrivalTime;//=2;
        public int numberOfServers;// = 3;
        public int numberOfClients;// = 5;
        public Strategy.SelectionPolicy selectionPolicy = Strategy.SelectionPolicy.shortest_time;

        // entity responsible with queue management and client distribution
        private Scheduler scheduler;
        // frame for displaying simulation
        private Frame frame;
        // pool of tasks (client shopping in the store)
        private List<Task> generatedTasks;

        public SimulationManager() {
            // => create and start numberOfServers threads
            // => initialize selection strategy => createStrategy
            // initialize frame to display simulation
            // generate numberOfClients clients using generateNRandomTasks()
            // and store them to generatedTasks
            scheduler=new Scheduler(numberOfServers,numberOfClients);
            this.generatedTasks=new ArrayList<>();
            generateNRandomTasks();
            this.frame=new View();
            frame.setVisible(true);
        }
        public SimulationManager(int timeLimit,int maxProcessingTime,int minProcessingTime, int minArrivalTime,
                                 int maxArrivalTime,int numberOfClients,int numberOfServers,View view) {
            this.timeLimit=timeLimit;
            this.maxProcessingTime=maxProcessingTime;
            this.minProcessingTime=minProcessingTime;
            this.minArrivalTime=minArrivalTime;
            this.maxArrivalTime=maxArrivalTime;
            this.numberOfClients=numberOfClients;
            this.numberOfServers=numberOfServers;
            scheduler=new Scheduler(numberOfServers,numberOfClients);
            this.generatedTasks=new ArrayList<>();
            generateNRandomTasks();
            this.frame=view;
            frame.setVisible(true);
        }

        private void generateNRandomTasks() {
            // generate N random tasks:
            // - random processing time
            // minProcessingTime < processingTime < maxProcessingTime
            // - random arrivalTime
            // sort list with respect to arrivalTime
            Random rand = new Random();

            for (int i = 0; i < numberOfClients; i++) {
                int processingTime = rand.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;
                int arrivalTime = rand.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
                Task newTask = new Task(i + 1, arrivalTime, processingTime);
                generatedTasks.add(newTask);
            }

            // Sorteaza task-urile dupa arrivalTime
            Collections.sort(generatedTasks, (t1, t2) -> Integer.compare(t1.getArrivalTime(), t2.getArrivalTime()));
            Comparator<Task> idComp=new Comparator<Task>(){
                public int compare(Task t1,Task t2){
                        return 1;
                }
            };

        }
        private boolean allServersIdle() {
            for (Server server : scheduler.getServers()) {
                if (server.getWaitingPeriod().get() > 0 ||
                        server.getNrOfTasks().get() > 0 ||
                        server.getCurrentTask() != null) {
                    return false;
                }
            }
            return true;
        }
        public void run() {
            Logger.clearLog();
            int currentTime = 0;

            while (currentTime < timeLimit) {
                Logger.log("Time " + currentTime);
                System.out.println("Time " + currentTime);
                ((View) frame).appendToTextField("Time " + currentTime);

                // === WAITING CLIENTS ===
                StringBuilder waitingClients = new StringBuilder();
                if (!generatedTasks.isEmpty()) {
                    waitingClients.append("Waiting clients: ");
                    for (Task t : generatedTasks) {
                        waitingClients.append("(")
                                .append(t.getId()).append(",")
                                .append(t.getArrivalTime()).append(",")
                                .append(t.getServiceTime()).append("); ");
                    }
                } else {
                    waitingClients.append("Waiting clients: none");
                }

                Logger.log(waitingClients.toString());
                System.out.println(waitingClients);
                ((View) frame).appendToTextField(waitingClients.toString());

                // === QUEUES STATUS ===
                StringBuilder queuesStatus = new StringBuilder();
                List<Server> servers = scheduler.getServers();
                for (Server server : servers) {
                    BlockingQueue<Task> taskQueue = server.getTasks();
                    Task currentTask = server.getCurrentTask();

                    StringBuilder queueStatus = new StringBuilder("Queue " + server.getId() + ": ");

                    if (currentTask == null && taskQueue.isEmpty()) {
                        queueStatus.append("closed ");
                    } else {
                        if (currentTask != null) {
                            queueStatus.append("(")
                                    .append(currentTask.getId()).append(",")
                                    .append(currentTask.getArrivalTime()).append(",")
                                    .append(currentTask.getRemainingServiceTime()).append("); ");
                        }

                        for (Task t : taskQueue) {
                            queueStatus.append("(")
                                    .append(t.getId()).append(",")
                                    .append(t.getArrivalTime()).append(",")
                                    .append(t.getServiceTime()).append("); ");
                        }
                    }

                    Logger.log(queueStatus.toString());
                    System.out.println(queueStatus);
                    ((View) frame).appendToTextField(queueStatus.toString());
                    queuesStatus.append(queueStatus).append("\n");
                }

                // === UPDATE TABEL ===
                updateTable(currentTime, waitingClients.toString(), queuesStatus.toString());

                // === DISPATCH ===
                while (!generatedTasks.isEmpty() && generatedTasks.get(0).getArrivalTime() <= currentTime) {
                    Task task = generatedTasks.remove(0);
                    scheduler.dispatchTask(task);
                }

                // === CHECK FINAL === /
                if (generatedTasks.isEmpty() && allServersIdle()) {
                    String doneMsg = "All tasks processed. Ending simulation at time " + currentTime;
                    Logger.log(doneMsg);
                    System.out.println(doneMsg);
                    ((View) frame).appendToTextField(doneMsg);

                    // === CALCUL AVERAGE WAITING TIME PER QUEUE ===
                    double totalAverage = 0.0;
                    int queueCount = 0;

                    for (Server server : scheduler.getServers()) {
                        double avg = server.getAvgTime();
                        if (server.getTotalTasksProcessed() > 0) {
                            String queueAvgMsg = String.format("Queue %d average waiting time: %.2f", server.getId(), avg);
                            Logger.log(queueAvgMsg);
                            System.out.println(queueAvgMsg);
                            ((View) frame).appendToTextField(queueAvgMsg);

                            totalAverage += avg;
                            queueCount++;
                        }
                    }

                    double averageWaitingTime = queueCount > 0 ? totalAverage / queueCount : 0.0;
                    String avgMsg = String.format("Global average waiting time: %.2f", averageWaitingTime);
                    Logger.log(avgMsg);
                    System.out.println(avgMsg);
                    ((View) frame).appendToTextField(avgMsg);

                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                currentTime++;
            }
        }
        private void updateTable(int currentTime,String waitingClients, String queues) {
                DefaultTableModel model = (DefaultTableModel) ((View) frame).getDataTable().getModel();
                model.addRow(new Object[]{currentTime, waitingClients, queues});
        }

        public static void main(String[] args){
            JFrame frame = new View();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // frame.pack();
            frame.setVisible(true);
        }
    }
