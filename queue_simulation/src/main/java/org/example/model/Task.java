package org.example.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private Integer id;
    private Integer arrivalTime;
    private Integer serviceTime;
    private AtomicInteger remainingServiceTime;
    public Task(int id,int arrivalTime,int serviceTime){
        this.id=id;
        this.arrivalTime=arrivalTime;
        this.serviceTime=serviceTime;
        this.remainingServiceTime = new AtomicInteger(serviceTime);
    }
    public int getId(){
        return this.id;
    }
    public int getArrivalTime(){
        return this.arrivalTime;
    }
    public int getServiceTime(){
        return this.serviceTime;
    }
    @Override
    public String toString() {
        return id + "," + arrivalTime + "," + serviceTime;
    }
    public int getRemainingServiceTime() {
        return remainingServiceTime.get();
    }
    public void decreaseRemainingServiceTime() {
        remainingServiceTime.decrementAndGet();
    }
}
