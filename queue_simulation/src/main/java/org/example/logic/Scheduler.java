package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTaskPerServers;
    private Strategy strategy;
    public Scheduler(int maxNoServers,int maxTaskPerServers){
        this.maxNoServers=maxNoServers;
        this.maxTaskPerServers=maxTaskPerServers;
        this.servers=new ArrayList<>();
        this.strategy=new TimeStrategy();

        for (int i = 0; i < this.maxNoServers; i++) {
            Server server = new Server(i);
            servers.add(server);
            Thread t = new Thread(server);
            t.start(); // PORNESTE THREAD-UL SERVERULUI
        }
    }
    public void changeStrategy(Strategy.SelectionPolicy policy){
        if(policy== Strategy.SelectionPolicy.shortest_queue){
            this.strategy=new TimeStrategy();
        }
        if(policy== Strategy.SelectionPolicy.shortest_time){
            this.strategy=new ShortestQueueStrategy();
        }

    }
    public void dispatchTask(Task t1){
        this.strategy.addTask(servers,t1);
    }
    public List<Server> getServers(){
        return servers;
    }
}
