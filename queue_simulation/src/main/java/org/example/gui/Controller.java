package org.example.gui;

import org.example.logic.SimulationManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

public class Controller implements ActionListener {
    private SimulationManager gen;
    private View view;


    public Controller(View view) {
        this.view = view;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Start")) {
            startSimulation();
        }
    }
    public void startSimulation() {
        try {
            String timeLimitText = view.getTimeLimitField().getText();
            String maxProcessingTimeText = view.getMaxProcessingTimeField().getText();
            String minProcessingTimeText = view.getMinProcessingTimeField().getText();
            String minArrivalTimeText = view.getMinArrivalTimeField().getText();
            String maxArrivalTimeText = view.getMaxArrivalTimeField().getText();
            String numberOfClientsText = view.getNumberOfClientsField().getText();
            String numberOfServersText = view.getNumberOfServersField().getText();

            // Verificare de campuri goale
            if (timeLimitText.isEmpty() || maxProcessingTimeText.isEmpty() || minProcessingTimeText.isEmpty()
                    || minArrivalTimeText.isEmpty() || maxArrivalTimeText.isEmpty()
                    || numberOfClientsText.isEmpty() || numberOfServersText.isEmpty()) {

                JOptionPane.showMessageDialog(view,
                        "Toate campurile trebuie completate!",
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse to int (cu try-catch pt validare format)
            int timeLimit = Integer.parseInt(timeLimitText);
            int maxProcessingTime = Integer.parseInt(maxProcessingTimeText);
            int minProcessingTime = Integer.parseInt(minProcessingTimeText);
            int minArrivalTime = Integer.parseInt(minArrivalTimeText);
            int maxArrivalTime = Integer.parseInt(maxArrivalTimeText);
            int numberOfClients = Integer.parseInt(numberOfClientsText);
            int numberOfServers = Integer.parseInt(numberOfServersText);

            // Start simulare
            this.gen = new SimulationManager(timeLimit, maxProcessingTime, minProcessingTime,
                    minArrivalTime, maxArrivalTime, numberOfClients, numberOfServers, this.view);
            Thread t = new Thread(gen);
            t.start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view,
                    "Toate valorile trebuie sa fie numere intregi valide!",
                    "Format Invalid",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
